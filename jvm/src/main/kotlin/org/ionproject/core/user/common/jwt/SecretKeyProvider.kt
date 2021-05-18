package org.ionproject.core.user.common.jwt

import org.ionproject.core.common.customExceptions.InternalServerErrorException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.util.ResourceUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception
import java.lang.RuntimeException
import java.security.KeyStore
import java.security.SecureRandom
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Pattern
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

@Component
class SecretKeyProvider {

    companion object {
        private const val SECRET_KEY_ENTRY = "SECRET_KEY"
        // https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyStore
        private const val KEY_STORE_TYPE = "pkcs12"
        // https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyGenerator
        private const val KEY_ALGORITHM = "HmacSHA256"
        private const val KEYS_FOLDER = "keys"
        private const val KEY_FILE_EXTENSION = "pfx"

        private val filePattern = Pattern.compile("(?:[A-z]+:)?([\\w/]+)(?:.[A-z]+)?")

        private fun String.parsePath(): String {
            // ignore classpath: or file:, since these files must be loaded from the filesystem (aka file:)
            val matcher = filePattern.matcher(this)
            if (!matcher.find())
                throw InternalServerErrorException("Invalid secret key file pattern")

            val filePath = matcher.group(1)
            // results in file:keys/path.ext
            return "file:$KEYS_FOLDER${File.separator}$filePath.$KEY_FILE_EXTENSION"
        }
    }

    @Value("\${core.secret-key-password}")
    lateinit var secretKeyPassword: String

    private val keyGenerator: KeyGenerator by lazy {
        KeyGenerator.getInstance(KEY_ALGORITHM)
    }

    private val keyCache = ConcurrentHashMap<String, SecretKey>()

    fun loadSecretKey(path: String): SecretKey {
        val parsedPath = path.parsePath()
        return keyCache.computeIfAbsent(parsedPath) { _ ->
            val file = ResourceUtils.getFile(parsedPath)
            if (!file.exists()) {
                val folder = File(KEYS_FOLDER)
                if (!folder.mkdirs() || !file.createNewFile())
                    throw RuntimeException("Unexpected: Could not create a keystore file!")

                try {
                    createNewSecretKey(file)
                } catch (ex: Exception) {
                    file.delete()
                    if (folder.listFiles()?.isEmpty() == true)
                        folder.delete()

                    throw ex
                }
            } else {
                val keyStore = createKeyStore()
                FileInputStream(file).use {
                    val password = secretKeyPassword.toCharArray()
                    keyStore.load(it, password)
                    val see = keyStore.getEntry(SECRET_KEY_ENTRY, KeyStore.PasswordProtection(password))
                        as KeyStore.SecretKeyEntry

                    see.secretKey
                }
            }
        }
    }

    private fun createNewSecretKey(file: File): SecretKey {
        keyGenerator.init(SecureRandom())
        val key = keyGenerator.generateKey()

        // store the key in a new keystore
        val keyStore = createKeyStore()
        val password = secretKeyPassword.toCharArray()
        keyStore.load(null, null)
        keyStore.setEntry(SECRET_KEY_ENTRY, KeyStore.SecretKeyEntry(key), KeyStore.PasswordProtection(password))
        FileOutputStream(file).use {
            keyStore.store(it, password)
            return key
        }
    }

    private fun createKeyStore() = KeyStore.getInstance(KEY_STORE_TYPE)
}
