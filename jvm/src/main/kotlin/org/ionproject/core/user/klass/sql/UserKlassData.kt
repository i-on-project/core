package org.ionproject.core.user.klass.sql

object UserKlassData {

    const val USER_ID = "user_id"
    const val CLASS_ID = "class_id"
    const val SECTION_ID = "section_id"

    const val GET_USER_CLASSES = """
        select id, courseId, acronym as courseAcr, calendarTerm
        from (dbo.UserClasses uc join dbo.Class c on uc.class_id = c.id)
        join dbo.Course cr on c.courseId = cr.id
        where user_id = :$USER_ID
    """

    const val INSERT_USER_CLASS = """
        insert into dbo.UserClasses (user_id, class_id)
        values (:$USER_ID, :$CLASS_ID)
    """

    const val GET_CLASS_SECTIONS = """
        select class_id as classId, class_section_id as id
        from dbo.UserClassSections
        where user_id = :$USER_ID and class_id = :$CLASS_ID
    """

    const val INSERT_USER_CLASS_SECTION = """
        insert into dbo.UserClassSections (user_id, class_id, class_section_id)
        values (:$USER_ID, :$CLASS_ID, :$SECTION_ID)
    """

}