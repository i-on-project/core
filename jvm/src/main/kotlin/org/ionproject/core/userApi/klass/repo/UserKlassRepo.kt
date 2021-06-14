package org.ionproject.core.userApi.klass.repo

import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.ionproject.core.userApi.klass.model.UserKlass
import org.ionproject.core.userApi.klass.model.UserKlassSection

interface UserKlassRepo {

    fun getSubscribedClasses(userId: String, pagination: Pagination): List<UserKlass>

    fun getSubscribedClass(userId: String, classId: Int): UserKlass

    fun isSubscribedToClass(userId: String, classId: Int): Boolean

    fun subscribeToClass(userId: String, classId: Int): Boolean

    fun unsubscribeFromClass(userId: String, classId: Int)

    fun getSubscribedClassSection(userId: String, classId: Int, sectionId: String): UserKlassSection

    fun isSubscribedToClassSection(userId: String, classId: Int, sectionId: String): Boolean

    fun subscribeToClassSection(userId: String, classId: Int, sectionId: String): Boolean

    fun unsubscribeFromClassSection(userId: String, classId: Int, sectionId: String)
}
