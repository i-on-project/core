package org.ionproject.core.userApi.klass.sql

object UserKlassData {

    const val USER_ID = "user_id"
    const val CLASS_ID = "class_id"
    const val SECTION_ID = "section_id"
    const val OFFSET = "offset"
    const val LIMIT = "limit"

    const val GET_USER_CLASSES = """
        select c.id, c.courseId, cr.acronym as courseAcr, c.calendarTerm
        from (dbo.UserClasses uc join dbo.Class c on uc.class_id = c.id)
        join dbo.Course cr on c.courseId = cr.id
        where uc.user_id = :$USER_ID
        offset :$OFFSET
        limit :$LIMIT
    """

    const val GET_USER_CLASS = """
        $GET_USER_CLASSES and uc.class_id = :$CLASS_ID
    """

    const val INSERT_USER_CLASS = """
        insert into dbo.UserClasses (user_id, class_id)
        values (:$USER_ID, :$CLASS_ID)
    """

    const val DELETE_USER_CLASS = """
        delete from dbo.UserClasses
        where user_id = :$USER_ID and class_id :$CLASS_ID
    """

    const val GET_CLASS_SECTIONS = """
        select class_section_id as id
        from dbo.UserClassSections
        where user_id = :$USER_ID and class_id = :$CLASS_ID
    """

    const val INSERT_USER_CLASS_SECTION = """
        insert into dbo.UserClassSections (user_id, class_id, class_section_id)
        values (:$USER_ID, :$CLASS_ID, :$SECTION_ID)
    """
}
