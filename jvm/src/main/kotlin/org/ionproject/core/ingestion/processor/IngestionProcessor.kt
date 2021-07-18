package org.ionproject.core.ingestion.processor

import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Path
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

private val log = LoggerFactory.getLogger("IngestionProcessor")

@Target(AnnotationTarget.CLASS)
annotation class FileIngestion(val name: String, val throwOnError: Boolean = false)

interface IngestionProcessor<T> {

    fun process(data: T)
}

interface IngestionObjectMapper {

    fun <T : Any> map(file: File, klass: KClass<T>): T
}

data class ProcessorWrapper<T : Any>(
    val processor: IngestionProcessor<T>,
    val klass: KClass<T>,
    val throwOnError: Boolean
)

class IngestionProcessorRegistry(private val fileExtension: String, private val mapper: IngestionObjectMapper) {

    private val wrappers = linkedMapOf<String, ProcessorWrapper<*>>()

    inline fun <reified T : Any> register(processor: IngestionProcessor<T>) {
        register(processor, T::class)
    }

    fun <T : Any> register(processor: IngestionProcessor<T>, klass: KClass<T>) {
        val fileAnnotation = processor::class.findAnnotation<FileIngestion>()
            ?: throw Exception("A processor must be annotated with ${FileIngestion::class.simpleName}")

        wrappers[fileAnnotation.name] = ProcessorWrapper(processor, klass, fileAnnotation.throwOnError)
    }

    @Suppress("unchecked_cast")
    fun processChanges(path: Path, changes: Set<Path>) {
        val toProcess = mutableMapOf<String, MutableList<File>>()
        changes.filter { it.startsWith(path) }
            .forEach {
                val file = it.toAbsolutePath()
                    .toFile()

                if (file.name.endsWith(fileExtension)) {
                    val filename = file.name.split(".")[0]

                    toProcess.computeIfAbsent(filename) { mutableListOf() }
                        .add(file)
                }
            }

        wrappers.forEach { (k, v) ->
            v as ProcessorWrapper<Any>

            val files = toProcess[k]
            if (files != null) {
                val processor = v.processor
                files.forEach {
                    try {
                        log.info("Processing file: $it")
                        val data = mapper.map(it, v.klass)
                        processor.process(data)
                    } catch (e: Exception) {
                        if (v.throwOnError)
                            throw e

                        log.error("A non-fatal exception occurred: ", e)
                    }
                }
            }
        }
    }
}
