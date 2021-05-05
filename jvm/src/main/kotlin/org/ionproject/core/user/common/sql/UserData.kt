package org.ionproject.core.user.common.sql

object UserData {

    const val USER_ID = "userId"
    const val EMAIL = "email"

    const val GET_USER_BY_EMAIL = """
        select * from dbo.UserAccount where email = :$EMAIL
    """

    const val INSERT_USER = """
        insert into dbo.UserAccount (user_id, email)
        values
        (:$USER_ID, :$EMAIL)
    """

}