package org.ionproject.core.userApi.user.repo

import org.ionproject.core.userApi.user.model.User
import org.ionproject.core.userApi.user.model.UserEditInput

interface UserRepo {

    fun editUser(user: User, input: UserEditInput)

    fun deleteUser(user: User)
}
