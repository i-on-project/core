package org.ionproject.core.ingestion

import org.eclipse.jgit.api.Git
import org.ionproject.core.ingestion.processor.IngestionProcessorRegistry
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.File
import java.util.concurrent.Executors

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

    private val academicYearsDir: File by lazy {
        File(REPOSITORY_DIR_NAME + File.separator + institutionFolder + File.separator + ACADEMIC_YEARS)
    }

    private val programmesDir: File by lazy {
        File(REPOSITORY_DIR_NAME + File.separator + institutionFolder + File.separator + PROGRAMMES)
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
        academicYearsDir.listFiles()
            ?.sortedDescending()
            ?.get(0)
            ?.listFiles()
            ?.first()
            ?.let { calendar -> registry[calendar.name]?.process(calendar) }
            ?: throw Exception("Cannot process data because the calendar data was not found")

        programmesDir.listFiles()
            ?.mapNotNull {
                it.listFiles()
                    ?.sortedDescending()
                    ?.get(0)
            }?.map {
                it.listFiles()?.toList() ?: emptyList()
            }?.flatten()?.forEach {
                registry[it.name]?.process(it)
            }
    }

}