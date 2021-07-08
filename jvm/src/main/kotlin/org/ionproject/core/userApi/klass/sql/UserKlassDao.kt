package org.ionproject.core.userApi.klass.sql

import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.ionproject.core.userApi.klass.model.UserKlass
import org.ionproject.core.userApi.klass.model.UserKlassSection
import org.ionproject.core.userApi.user.model.User
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface UserKlassDao {

    @SqlUpdate(
        """
            insert into dbo.UserClasses (user_id, class_id)
            values (:user.userId, :classId)
        """
    )
    fun createUserClass(user: User, classId: Int)

    @SqlUpdate(
        """
            delete from dbo.UserClasses
            where user_id = :user.userId and class_id = :classId
        """
    )
    fun deleteUserClass(user: User, classId: Int)

    @SqlQuery(
        """
            select c.id, c.courseId, cr.acronym as courseAcr, cr.name as courseName, c.calendarTerm
            from (dbo.UserClasses uc join dbo.Class c on uc.class_id = c.id)
            join dbo.Course cr on c.courseId = cr.id
            where uc.user_id = :user.userId
            offset :pagination.offset
            limit :pagination.limit
        """
    )
    fun getUserClasses(user: User, pagination: Pagination): Set<UserKlass>

    @SqlQuery(
        """
            select c.id, c.courseId, cr.acronym as courseAcr, cr.name as courseName, c.calendarTerm
            from (dbo.UserClasses uc join dbo.Class c on uc.class_id = c.id)
            join dbo.Course cr on c.courseId = cr.id
            where uc.user_id = :user.userId and uc.class_id = :classId
        """
    )
    fun getUserClass(user: User, classId: Int): UserKlass?

    @SqlUpdate(
        """
            insert into dbo.UserClassSections (user_id, class_id, class_section_id)
            values (:user.userId, :classId, :sectionId)
        """
    )
    fun createUserClassSection(user: User, classId: Int, sectionId: String)

    @SqlUpdate(
        """
            delete from dbo.UserClassSections
            where user_id = :user.userId and class_id = :classId and class_section_id = :sectionId
        """
    )
    fun deleteUserClassSection(user: User, classId: Int, sectionId: String)

    @SqlQuery(
        """
            select ucs.class_section_id as id, ucs.class_id as classId, cl.courseId, cr.acronym as courseAcr, cr.name as courseName, cl.calendarTerm
            from (dbo.UserClassSections ucs join dbo.Class cl on ucs.class_id = cl.id)
            join dbo.Course cr on cl.courseId = cr.id
            where user_id = :user.userId
            offset :pagination.offset
            limit :pagination.limit
        """
    )
    fun getUserClassSections(user: User, pagination: Pagination): Set<UserKlassSection>

    @SqlQuery(
        """
            select ucs.class_section_id as id, ucs.class_id as classId, cl.courseId, cr.acronym as courseAcr, cr.name as courseName, cl.calendarTerm
            from (dbo.UserClassSections ucs join dbo.Class cl on ucs.class_id = cl.id)
            join dbo.Course cr on cl.courseId = cr.id
            where user_id = :user.userId and class_id = :classId
        """
    )
    fun getUserClassSectionsForClass(user: User, classId: Int): Set<UserKlassSection>

    @SqlQuery(
        """
            select ucs.class_section_id as id, ucs.class_id as classId, cl.courseId, cr.acronym as courseAcr, cr.name as courseName, cl.calendarTerm
            from (dbo.UserClassSections ucs join dbo.Class cl on ucs.class_id = cl.id)
            join dbo.Course cr on cl.courseId = cr.id
            where user_id = :user.userId and class_id = :classId and class_section_id = :sectionId
        """
    )
    fun getUserClassSection(user: User, classId: Int, sectionId: String): UserKlassSection?

    @SqlQuery(
        """
            select count(*) from dbo.Class
            where id = :classId
        """
    )
    fun getClassCount(classId: Int): Int

    @SqlQuery(
        """
            select count(*) from dbo.ClassSection
            where classId = :classId and id = :sectionId
        """
    )
    fun getClassSectionCount(classId: Int, sectionId: String): Int
}
