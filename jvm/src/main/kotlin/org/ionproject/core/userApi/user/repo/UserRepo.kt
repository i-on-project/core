package org.ionproject.core.userApi.user.repo

import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.ionproject.core.userApi.user.model.User
import org.ionproject.core.userApi.user.model.UserEditInput

interface UserRepo {

    fun getUsers(pagination: Pagination): List<User>

    fun getUser(userId: String): User

    fun editUser(userId: String, input: UserEditInput)

    fun deleteUser(userId: String)

}