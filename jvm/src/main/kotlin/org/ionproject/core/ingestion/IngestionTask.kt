package org.ionproject.core.ingestion

import org.eclipse.jgit.api.Git
import org.ionproject.core.ingestion.processor.IngestionProcessorRegistry
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.Executors
import kotlin.io.path.name
import kotlin.streams.toList

@Component
class IngestionTask(
    val registry: IngestionProcessorRegistry
) {

    companion object {
        private const val INGESTION_RATE = 60 * 60 * 1000L // 1 hour
        private const val REPOSITORY_DIR_NAME = "ingestion-data"
        private const val ACADEMIC_YEARS = "academic_years"
        private const val PROGRAMMES = "programmes"

        private val repositoryDir = File(REPOSITORY_DIR_NAME)
        private val log = LoggerFactory.getLogger(IngestionTask::class.java)
    }

    @Value("\${ingestion.institution_folder}")
    lateinit var institutionFolder: String

    @Value("\${ingestion.repository_remote}")
    lateinit var repositoryRemote: String

    @Value("\${ingestion.repository_remote_branch}")
    lateinit var repositoryRemoteBranch: String

    private val threadPool = Executors.newSingleThreadExecutor()

    private val academicYearsDir: Path by lazy {
        Paths.get(repositoryDir.absolutePath, institutionFolder, ACADEMIC_YEARS)
    }

    private val programmesDir: Path by lazy {
        Paths.get(repositoryDir.absolutePath, institutionFolder, PROGRAMMES)
    }

    @Scheduled(fixedRate = INGESTION_RATE)
    fun scheduleIngestion() {
        processTask()
    }

    fun processTask() {
        threadPool.submit {
            log.info("Checking ingestion repository for new data...")
            try {
                val git = downloadGitRepository()
                git.use {
                    val repo = git.repository
                    val headObject = repo.exactRef("HEAD").objectId
                    val commit = repo.parseCommit(headObject)
                    val commitHash = commit.name
                    val authorEmail = commit.authorIdent.emailAddress
                    log.info("Parsing commit $commitHash by $authorEmail")

                    processFiles()
                }
            } catch (e: Exception) {
                log.error("An exception occurred while performing ingestion.", e)
            }
        }
    }

    private fun downloadGitRepository(): Git {
        return if (repositoryDir.exists()) {
            val git = Git.open(repositoryDir)
            try {
                git.pull().call()
                git
            } catch (e: Throwable) {
                git.close()
                throw e
            }
        } else {
            Git.cloneRepository()
                .setURI(repositoryRemote)
                .setBranch(repositoryRemoteBranch)
                .setDirectory(repositoryDir)
                .call()
        }
    }

    private fun processFiles() {
        Files.list(academicYearsDir)
            .use { academicYears ->
                val latestYear = academicYears.toList()
                    .maxOrNull()
                    ?: throw Exception("No calendar years found for processing")

                Files.list(latestYear)
                    .use {
                        it.toList()
                            .firstOrNull()
                            ?.toFile()
                            ?.let { calendar -> registry[calendar.name]?.process(calendar) }
                            ?: throw Exception("Cannot process data because the calendar data was not found")
                    }
            }

        Files.list(programmesDir)
            .use { programmes ->
                // for each programme
                programmes.forEach { programme ->
                    // find the latest year of this programme
                    val latestYear = Files.list(programme)
                        .use {
                            it.toList()
                                .sortedDescending()
                                .getOrNull(0)
                        }

                    // parse the latest year
                    if (latestYear != null) {
                        Files.list(latestYear)
                            .use {
                                it.map { path -> path.toFile() }
                                    .forEach { file -> registry[file.name]?.process(file) }
                            }
                    }
                }
            }
    }
}
