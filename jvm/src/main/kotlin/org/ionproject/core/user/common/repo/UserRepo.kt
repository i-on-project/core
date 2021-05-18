package org.ionproject.core.user.common.repo

import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.ionproject.core.user.common.model.User
import org.ionproject.core.user.common.model.UserEditInput

interface UserRepo {

    fun getUsers(pagination: Pagination): List<User>

    fun getUser(userId: String): User

    fun editUser(userId: String, input: UserEditInput)

    fun deleteUser(userId: String)

}