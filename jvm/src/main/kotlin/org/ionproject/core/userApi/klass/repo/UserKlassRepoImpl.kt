package org.ionproject.core.userApi.klass.repo

import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.ionproject.core.common.customExceptions.BadRequestException
import org.ionproject.core.common.customExceptions.ResourceNotFoundException
import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.toNullable
import org.ionproject.core.userApi.klass.model.UserKlass
import org.ionproject.core.userApi.klass.model.UserKlassSection
import org.ionproject.core.userApi.klass.sql.UserKlassData
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import org.springframework.stereotype.Repository

@Repository
class UserKlassRepoImpl(val tm: TransactionManager) : UserKlassRepo {

    override fun getSubscribedClasses(userId: String, pagination: Pagination) =
        tm.run {
            it.createQuery(UserKlassData.GET_USER_CLASSES)
                .bind(UserKlassData.USER_ID, userId)
                .bind(UserKlassData.OFFSET, pagination.offset)
                .bind(UserKlassData.LIMIT, pagination.limit)
                .mapTo<UserKlass>()
                .toList()
        }

    override fun getSubscribedClass(userId: String, classId: Int) =
        tm.run {
            val klass = findSubscribedClass(userId, classId, it)
                ?: throw ResourceNotFoundException("The user $userId is not subscribed to the class $classId")

            val subscribedClassSections = it.createQuery(UserKlassData.GET_USER_CLASS_SECTIONS)
                .bind(UserKlassData.USER_ID, userId)
                .bind(UserKlassData.CLASS_ID, classId)
                .mapTo<String>()
                .toSet()

            klass + subscribedClassSections
        }

    override fun isSubscribedToClass(userId: String, classId: Int) =
        tm.run { isSubscribedToClass(userId, classId, it) }

    override fun subscribeToClass(userId: String, classId: Int) =
        tm.run { subscribeToClass(userId, classId, it) }

    override fun unsubscribeFromClass(userId: String, classId: Int) {
        tm.run {
            // the user needs to be subscribed
            if (!isSubscribedToClass(userId, classId, it))
                throw BadRequestException("The user $userId is not subscribed to the class $classId")

            it.createUpdate(UserKlassData.DELETE_USER_CLASS)
                .bind(UserKlassData.USER_ID, userId)
                .bind(UserKlassData.CLASS_ID, classId)
                .execute()
        }
    }

    override fun getSubscribedClassSection(userId: String, classId: Int, sectionId: String) =
        tm.run {
            findSubscribedClassSection(userId, classId, sectionId, it)
                ?: throw ResourceNotFoundException("The user $userId is not subscribed to the class $classId section $sectionId")
        }

    override fun isSubscribedToClassSection(userId: String, classId: Int, sectionId: String) =
        tm.run { isSubscribedToClassSection(userId, classId, sectionId, it) }

    override fun subscribeToClassSection(userId: String, classId: Int, sectionId: String) =
        tm.run {
            if (isSubscribedToClassSection(userId, classId, sectionId, it)) {
                false
            } else {
                // subscribe the user to the class if not subscribed already
                subscribeToClass(userId, classId, it)

                it.createUpdate(UserKlassData.INSERT_USER_CLASS_SECTION)
                    .bind(UserKlassData.USER_ID, userId)
                    .bind(UserKlassData.CLASS_ID, classId)
                    .bind(UserKlassData.SECTION_ID, sectionId)
                    .execute()

                true
            }
        }

    override fun unsubscribeFromClassSection(userId: String, classId: Int, sectionId: String) {
        tm.run {
            if (!isSubscribedToClassSection(userId, classId, sectionId))
                throw BadRequestException("The user $userId is not subscribed to the class $classId section $sectionId")

            it.createUpdate(UserKlassData.DELETE_USER_CLASS_SECTION)
                .bind(UserKlassData.USER_ID, userId)
                .bind(UserKlassData.CLASS_ID, classId)
                .bind(UserKlassData.SECTION_ID, sectionId)
                .execute()
        }
    }

    private fun checkClass(classId: Int, handle: Handle) {
        val count = handle.createQuery(UserKlassData.GET_CLASS_COUNT_BY_ID)
            .bind(UserKlassData.CLASS_ID, classId)
            .mapTo<Int>()
            .one() ?: 0

        if (count != 1)
            throw BadRequestException("Invalid class: $classId")
    }

    private fun subscribeToClass(userId: String, classId: Int, handle: Handle): Boolean {
        return if (isSubscribedToClass(userId, classId, handle)) {
            false
        } else {
            handle.createUpdate(UserKlassData.INSERT_USER_CLASS)
                .bind(UserKlassData.USER_ID, userId)
                .bind(UserKlassData.CLASS_ID, classId)
                .execute()

            true
        }
    }

    private fun isSubscribedToClass(userId: String, classId: Int, handle: Handle): Boolean {
        checkClass(classId, handle)
        return findSubscribedClass(userId, classId, handle) != null
    }

    private fun findSubscribedClass(userId: String, classId: Int, handle: Handle): UserKlass? {
        return handle.createQuery(UserKlassData.GET_USER_CLASS)
            .bind(UserKlassData.USER_ID, userId)
            .bind(UserKlassData.CLASS_ID, classId)
            .mapTo<UserKlass>()
            .findOne()
            .toNullable()
    }

    private fun checkClassSection(classId: Int, sectionId: String, handle: Handle) {
        checkClass(classId, handle)
        val count = handle.createQuery(UserKlassData.GET_CLASS_SECTION_COUNT_BY_ID)
            .bind(UserKlassData.CLASS_ID, classId)
            .bind(UserKlassData.SECTION_ID, sectionId)
            .mapTo<Int>()
            .one() ?: 0

        if (count != 1)
            throw BadRequestException("Invalid class section: $sectionId")
    }

    private fun isSubscribedToClassSection(userId: String, classId: Int, sectionId: String, handle: Handle): Boolean {
        checkClassSection(classId, sectionId, handle)
        return findSubscribedClassSection(userId, classId, sectionId, handle) != null
    }

    private fun findSubscribedClassSection(userId: String, classId: Int, sectionId: String, handle: Handle): UserKlassSection? {
        return handle.createQuery(UserKlassData.GET_USER_CLASS_SECTION)
            .bind(UserKlassData.USER_ID, userId)
            .bind(UserKlassData.CLASS_ID, classId)
            .bind(UserKlassData.SECTION_ID, sectionId)
            .mapTo<UserKlassSection>()
            .findOne()
            .toNullable()
    }
}
