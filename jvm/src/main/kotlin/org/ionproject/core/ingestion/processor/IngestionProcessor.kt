package org.ionproject.core.ingestion.processor

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.reflect.KClass

private val log = LoggerFactory.getLogger("IngestionProcessor")

interface IngestionProcessor<T> {

    fun process(data: T)
}

class ProcessorWrapper<T : Any>(
    private val mapper: ObjectMapper,
    private val processor: IngestionProcessor<T>,
    private val klass: KClass<T>
) {

    fun process(file: File) {
        log.info("Processing file: ${file.path}")
        val obj = mapper.readValue(file, klass.java)
        processor.process(obj)
        log.info("Processed data: $obj")
    }
}

class IngestionProcessorRegistry(val mapper: ObjectMapper) {

    val processors = mutableMapOf<String, ProcessorWrapper<*>>()

    inline fun <reified T : Any> register(fileName: String, processor: IngestionProcessor<T>) {
        processors[fileName] = ProcessorWrapper(mapper, processor, T::class)
    }

    operator fun get(fileName: String) = processors[fileName]
}
