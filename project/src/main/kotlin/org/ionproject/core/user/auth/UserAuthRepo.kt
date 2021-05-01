package org.ionproject.core.user.auth

import org.ionproject.core.user.auth.model.AuthRequest

interface UserAuthRepo {

    fun addAuthRequest(authRequest: AuthRequest, inTransaction: suspend () -> Unit): AuthRequest

}