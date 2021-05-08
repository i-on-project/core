package org.ionproject.core.user.common.jwt

import org.springframework.stereotype.Component
import javax.crypto.SecretKey
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@Component
class JwtSecretKeyProvider(val secretKeyProvider: SecretKeyProvider): ReadOnlyProperty<Any?, SecretKey> {

    companion object {
        private const val KEYSTORE_FILE_NAME = "sym_jwt"
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>) =
        secretKeyProvider.loadSecretKey(KEYSTORE_FILE_NAME)

}