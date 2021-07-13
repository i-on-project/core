package org.ionproject.core.ingestion

import org.eclipse.jgit.api.Git
import org.ionproject.core.ingestion.processor.IngestionProcessorRegistry
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Path
import java.util.concurrent.Executors
import kotlin.io.path.name

@Component
class IngestionTask(
    val registry: IngestionProcessorRegistry
) {

    companion object {
        private const val INGESTION_RATE = 60 * 60 * 1000L // 1 hour
        private const val REPOSITORY_DIR_NAME = "ingestion-data"

        private val repositoryDir = File(REPOSITORY_DIR_NAME)
        private val log = LoggerFactory.getLogger(IngestionTask::class.java)
    }

    @Value("\${ingestion.institution_folder}")
    private lateinit var institutionFolder: String

    @Value("\${ingestion.repository_remote}")
    private lateinit var repositoryRemote: String

    @Value("\${ingestion.repository_remote_branch}")
    private lateinit var repositoryRemoteBranch: String

    private val institutionPath: Path by lazy { Path.of(repositoryDir.absolutePath, institutionFolder) }

    private val threadPool = Executors.newSingleThreadExecutor()

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

                    registry.processDirectory(institutionPath)
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
}
