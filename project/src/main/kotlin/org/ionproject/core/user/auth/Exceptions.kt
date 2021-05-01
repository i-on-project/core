package org.ionproject.core.user.auth

import java.lang.RuntimeException

class InvalidClientIdException(clientId: String) : RuntimeException("The client_id $clientId is invalid!")

class InvalidNotificationMethodException(method: String) : RuntimeException("The notification method $method is invalid!")