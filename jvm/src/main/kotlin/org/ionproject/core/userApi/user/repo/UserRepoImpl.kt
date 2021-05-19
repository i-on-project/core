package org.ionproject.core.userApi.user.repo

import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.ionproject.core.common.customExceptions.ResourceNotFoundException
import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.toNullable
import org.ionproject.core.userApi.user.model.User
import org.ionproject.core.userApi.user.model.UserEditInput
import org.ionproject.core.userApi.user.sql.UserData
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import org.springframework.stereotype.Repository

@Repository
class UserRepoImpl(val tm: TransactionManager) : UserRepo {

    override fun getUsers(pagination: Pagination): List<User> =
        tm.run {
            it.createQuery(UserData.GET_USERS)
                .bind(UserData.OFFSET, pagination.offset)
                .bind(UserData.LIMIT, pagination.limit)
                .mapTo<User>()
                .list()
        }

    override fun getUser(userId: String): User =
        tm.run { getUserById(userId, it) }

    override fun editUser(userId: String, input: UserEditInput) {
        tm.run {
            // check if the user exists
            getUserById(userId, it)

            it.createUpdate(UserData.EDIT_USER)
                .bind(UserData.NAME, input.name)
                .bind(UserData.USER_ID, userId)
                .execute()
        }
    }

    override fun deleteUser(userId: String) {
        tm.run {
            // check if the user exists
            getUserById(userId, it)

            it.createUpdate(UserData.DELETE_USER)
                .bind(UserData.USER_ID, userId)
                .execute()
        }
    }

    private fun getUserById(userId: String, handle: Handle): User {
        return handle.createQuery(UserData.GET_USER_BY_ID)
            .bind(UserData.USER_ID, userId)
            .mapTo<User>()
            .findOne()
            .toNullable() ?: throw ResourceNotFoundException("Couldn't find a user with id $userId")
    }
}
