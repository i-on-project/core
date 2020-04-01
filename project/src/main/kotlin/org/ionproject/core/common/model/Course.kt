package org.ionproject.core.common.model

import org.ionproject.core.common.modelInterfaces.ICourse

/*
 * Represents the entity course
 */
class Course(override val acronym: String,
             override val name: String,
             override val calendarId: Int) : ICourse {

    /*
     * Redefines invoke for a constructor with validations,
     * in case that any validation fails, returns null.
     *
     * Constraint verifications like lengths are managed by the
     * postgresql db.
     */
    companion object {
        fun of(acronym : String,
               name: String,
               calendarId: Int) : Course? {
            if(acronym.trim() == "" || name.trim() == "" || calendarId < 0)
                return null;

            return Course(acronym, name, calendarId)
        }

        operator fun invoke(acronym : String,
                            name: String,
                            calendarId: Int) : Course? = of(acronym, name, calendarId)
    }
}