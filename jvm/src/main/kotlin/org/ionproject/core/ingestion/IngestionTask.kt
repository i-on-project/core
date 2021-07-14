package org.ionproject.core.ingestion

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.diff.DiffFormatter
import org.eclipse.jgit.lib.Config
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.treewalk.CanonicalTreeParser
import org.eclipse.jgit.treewalk.EmptyTreeIterator
import org.ionproject.core.ingestion.processor.IngestionProcessorRegistry
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.File
import java.io.OutputStream
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

    private var processedCommitHash: String? = null

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
                    val changes = getChanges(repo)
                    if (changes.isNotEmpty())
                        registry.processDirectory(institutionPath, changes)
                    else
                        log.info("No changes were detected")
                }
            } catch (e: Exception) {
                log.error("An exception occurred while performing ingestion.", e)
            }
        }
    }

    private fun getChanges(repo: Repository): Set<Path> {
        val walk = RevWalk(repo)
        val headObject = repo.exactRef("HEAD").objectId
        val latestCommit = repo.parseCommit(headObject)
        val latestTree = walk.parseTree(latestCommit.tree.id)

        // create the commit tree iterator
        val latestParser = CanonicalTreeParser()
        repo.newObjectReader().use {
            latestParser.reset(it, latestTree)
        }

        val latestCommitHash = latestCommit.name
        val latestCommitAuthor = latestCommit.authorIdent.emailAddress
        log.info("Processing commit $latestCommitHash by $latestCommitAuthor")

        val currentParser = if (processedCommitHash == null) {
            EmptyTreeIterator()
        } else {
            val processedCommitObject = repo.resolve(processedCommitHash).toObjectId()
            val processedCommit = repo.parseCommit(processedCommitObject)
            val processedTree = walk.parseTree(processedCommit)

            log.info("Calculating difference: ${processedCommit.name} .. $latestCommitHash")

            val processedParser = CanonicalTreeParser()
            repo.newObjectReader().use {
                processedParser.reset(it, processedTree)
            }

            processedParser
        }

        this.processedCommitHash = latestCommitHash

        val formatter = DiffFormatter(OutputStream.nullOutputStream())
        repo.newObjectReader().use {
            formatter.setReader(it, Config())
            val diffEntries = formatter.scan(currentParser, latestParser)
            return diffEntries.map { f -> f.newPath }
                .map { f -> Path.of(repositoryDir.absolutePath, f) }
                .toSet()
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
