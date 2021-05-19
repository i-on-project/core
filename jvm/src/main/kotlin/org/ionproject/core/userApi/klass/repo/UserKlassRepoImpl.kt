package org.ionproject.core.userApi.klass.repo

import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.ionproject.core.common.customExceptions.BadRequestException
import org.ionproject.core.common.customExceptions.ResourceNotFoundException
import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.toNullable
import org.ionproject.core.userApi.klass.model.UserKlass
import org.ionproject.core.userApi.klass.sql.UserKlassData
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import org.springframework.stereotype.Repository

@Repository
class UserKlassRepoImpl(val tm: TransactionManager) : UserKlassRepo {

    override fun getSubscribedClasses(userId: String, pagination: Pagination): List<UserKlass> =
        tm.run {
            it.createQuery(UserKlassData.GET_USER_CLASSES)
                .bind(UserKlassData.USER_ID, userId)
                .bind(UserKlassData.OFFSET, pagination.offset)
                .bind(UserKlassData.LIMIT, pagination.limit)
                .mapTo<UserKlass>()
                .list()
        }

    override fun getSubscribedClass(userId: String, classId: Int): UserKlass =
        tm.run {
            val klass = findSubscribedClass(userId, classId, it)
                ?: throw ResourceNotFoundException("The user $userId is not subscribed to the class $classId")

            val subscribedClassSections = it.createQuery(UserKlassData.GET_CLASS_SECTIONS)
                .bind(UserKlassData.USER_ID, userId)
                .bind(UserKlassData.CLASS_ID, classId)
                .mapTo<String>()
                .toSet()

            klass + subscribedClassSections
        }

    override fun isSubscribedToClass(userId: String, classId: Int): Boolean =
        tm.run { isSubscribedToClass(userId, classId, it) }

    override fun subscribeToClass(userId: String, classId: Int): Boolean =
        tm.run {
            if (isSubscribedToClass(userId, classId, it)) {
                false
            } else {
                it.createUpdate(UserKlassData.INSERT_USER_CLASS)
                    .bind(UserKlassData.USER_ID, userId)
                    .bind(UserKlassData.CLASS_ID, classId)

                true
            }
        }

    override fun unsubscribeFromClass(userId: String, classId: Int) {
        tm.run {
            // the user needs to be subscribed
            findSubscribedClass(userId, classId, it)
                ?: throw BadRequestException("The user $userId is not subscribed to the class $classId")

            it.createUpdate(UserKlassData.DELETE_USER_CLASS)
                .bind(UserKlassData.USER_ID, userId)
                .bind(UserKlassData.CLASS_ID, classId)
                .execute()
        }
    }

    override fun getSubscribedClassSection(userId: String, classId: Int, sectionId: String) {
        TODO("Not yet implemented")
    }

    override fun isSubscribedToClassSection(userId: String, classId: Int, sectionId: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun subscribeToClassSection(userId: String, classId: Int, sectionId: String) {
        TODO("Not yet implemented")
    }

    override fun unsubscribeFromClassSection(userId: String, classId: Int, sectionId: String) {
        TODO("Not yet implemented")
    }

    private fun isSubscribedToClass(userId: String, classId: Int, handle: Handle) =
        findSubscribedClass(userId, classId, handle) != null

    private fun findSubscribedClass(userId: String, classId: Int, handle: Handle): UserKlass? {
        return handle.createQuery(UserKlassData.GET_USER_CLASS)
            .bind(UserKlassData.USER_ID, userId)
            .bind(UserKlassData.CLASS_ID, classId)
            .mapTo<UserKlass>()
            .findOne()
            .toNullable()
    }

}