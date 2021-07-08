package org.ionproject.core.userApi.klass.repo

import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.ionproject.core.userApi.klass.model.UserKlass
import org.ionproject.core.userApi.klass.model.UserKlassSection
import org.ionproject.core.userApi.user.model.User

interface UserKlassRepo {

    fun getSubscribedClasses(user: User, pagination: Pagination): Set<UserKlass>

    fun getSubscribedClassSections(user: User, pagination: Pagination): Set<UserKlassSection>

    fun getSubscribedClass(user: User, classId: Int): UserKlass

    fun isSubscribedToClass(user: User, classId: Int): Boolean

    fun subscribeToClass(user: User, classId: Int): Boolean

    fun unsubscribeFromClass(user: User, classId: Int)

    fun getSubscribedClassSection(user: User, classId: Int, sectionId: String): UserKlassSection

    fun isSubscribedToClassSection(user: User, classId: Int, sectionId: String): Boolean

    fun subscribeToClassSection(user: User, classId: Int, sectionId: String): Boolean

    fun unsubscribeFromClassSection(user: User, classId: Int, sectionId: String)
}
