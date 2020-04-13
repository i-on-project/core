package org.ionproject.core.common.model

import org.ionproject.core.common.modelInterfaces.ICourse

/*
 * Represents the entity course
 */
class Course(override val id: Int,
             override val acronym: String,
             override val name: String) : ICourse {

    /*
     * Redefines invoke for a constructor with validations,
     * in case that any validation fails, returns null.
     *
     * Constraint verifications like lengths are managed by the
     * postgresql db.
     */
    companion object {
        fun of(id: Int,
               acronym : String,
               name: String) : Course? {

            if(acronym.trim() == "" || name.trim() == "")
                return null;

            return Course(id, acronym, name)
        }

        operator fun invoke(id: Int,
                            acronym : String,
                            name: String) : Course? = of(id, acronym, name)
    }
}