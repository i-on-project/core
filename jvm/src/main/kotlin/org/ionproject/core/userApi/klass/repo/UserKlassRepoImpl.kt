package org.ionproject.core.userApi.klass.repo

import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.ionproject.core.common.customExceptions.BadRequestException
import org.ionproject.core.common.customExceptions.ResourceNotFoundException
import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.userApi.klass.sql.UserKlassDao
import org.ionproject.core.userApi.user.model.User
import org.jdbi.v3.sqlobject.kotlin.attach
import org.springframework.stereotype.Repository

@Repository
class UserKlassRepoImpl(val tm: TransactionManager) : UserKlassRepo {

    override fun getSubscribedClasses(user: User, pagination: Pagination) =
        tm.run {
            val dao = it.attach<UserKlassDao>()
            dao.getUserClasses(user, pagination)
        }

    override fun getSubscribedClassSections(user: User, pagination: Pagination) =
        tm.run {
            val dao = it.attach<UserKlassDao>()
            dao.getUserClassSections(user, pagination)
        }

    override fun getSubscribedClass(user: User, classId: Int) =
        tm.run {
            val dao = it.attach<UserKlassDao>()
            val klass = dao.getUserClass(user, classId)
                ?: throw ResourceNotFoundException("The user ${user.userId} is not subscribed to the class $classId")

            val subscribedClassSections = dao.getUserClassSectionsForClass(user, classId)
            klass + subscribedClassSections
        }

    override fun isSubscribedToClass(user: User, classId: Int) =
        tm.run {
            val dao = it.attach<UserKlassDao>()
            isSubscribedToClass(user, classId, dao)
        }

    override fun subscribeToClass(user: User, classId: Int) =
        tm.run {
            val dao = it.attach<UserKlassDao>()
            subscribeToClass(user, classId, dao)
        }

    override fun unsubscribeFromClass(user: User, classId: Int) {
        tm.run {
            val dao = it.attach<UserKlassDao>()
            // the user needs to be subscribed
            if (!isSubscribedToClass(user, classId, dao))
                throw BadRequestException("The user ${user.userId} is not subscribed to the class $classId")

            dao.deleteUserClass(user, classId)
        }
    }

    override fun getSubscribedClassSection(user: User, classId: Int, sectionId: String) =
        tm.run {
            val dao = it.attach<UserKlassDao>()
            dao.getUserClassSection(user, classId, sectionId)
                ?: throw ResourceNotFoundException("The user ${user.userId} is not subscribed to the class $classId section $sectionId")
        }

    override fun isSubscribedToClassSection(user: User, classId: Int, sectionId: String) =
        tm.run {
            val dao = it.attach<UserKlassDao>()
            isSubscribedToClassSection(user, classId, sectionId, dao)
        }

    override fun subscribeToClassSection(user: User, classId: Int, sectionId: String) =
        tm.run {
            val dao = it.attach<UserKlassDao>()
            if (isSubscribedToClassSection(user, classId, sectionId, dao)) {
                false
            } else {
                // subscribe the user to the class if not subscribed already
                subscribeToClass(user, classId, dao)
                dao.createUserClassSection(user, classId, sectionId)
                true
            }
        }

    override fun unsubscribeFromClassSection(user: User, classId: Int, sectionId: String) {
        tm.run {
            val dao = it.attach<UserKlassDao>()
            if (!isSubscribedToClassSection(user, classId, sectionId))
                throw BadRequestException("The user ${user.userId} is not subscribed to the class $classId section $sectionId")

            dao.deleteUserClassSection(user, classId, sectionId)
        }
    }

    private fun checkClass(classId: Int, dao: UserKlassDao) {
        val count = dao.getClassCount(classId)
        if (count != 1)
            throw BadRequestException("Invalid class: $classId")
    }

    private fun subscribeToClass(user: User, classId: Int, dao: UserKlassDao): Boolean {
        return if (isSubscribedToClass(user, classId, dao)) {
            false
        } else {
            dao.createUserClass(user, classId)
            true
        }
    }

    private fun isSubscribedToClass(user: User, classId: Int, dao: UserKlassDao): Boolean {
        checkClass(classId, dao)
        return dao.getUserClass(user, classId) != null
    }

    private fun checkClassSection(classId: Int, sectionId: String, dao: UserKlassDao) {
        checkClass(classId, dao)
        val count = dao.getClassSectionCount(classId, sectionId)
        if (count != 1)
            throw BadRequestException("Invalid class section: $sectionId")
    }

    private fun isSubscribedToClassSection(user: User, classId: Int, sectionId: String, dao: UserKlassDao): Boolean {
        checkClassSection(classId, sectionId, dao)
        return dao.getUserClassSection(user, classId, sectionId) != null
    }
}
