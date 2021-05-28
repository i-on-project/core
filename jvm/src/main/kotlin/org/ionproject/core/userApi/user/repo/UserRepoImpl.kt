package org.ionproject.core.userApi.user.repo

import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.userApi.user.model.User
import org.ionproject.core.userApi.user.model.UserEditInput
import org.ionproject.core.userApi.user.sql.UserData
import org.springframework.stereotype.Repository

@Repository
class UserRepoImpl(val tm: TransactionManager) : UserRepo {

    override fun editUser(user: User, input: UserEditInput) {
        tm.run {
            it.createUpdate(UserData.EDIT_USER)
                .bind(UserData.NAME, input.name)
                .bind(UserData.USER_ID, user.userId)
                .execute()
        }
    }

    override fun deleteUser(user: User) {
        tm.run {

            it.createUpdate(UserData.DELETE_USER)
                .bind(UserData.USER_ID, user.userId)
                .execute()
        }
    }
}
