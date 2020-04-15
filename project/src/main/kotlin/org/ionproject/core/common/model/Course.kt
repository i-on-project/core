package org.ionproject.core.common.model

/*
 * Represents the entity course
 */
class Course(
    val id: Int,
    val acronym: String,
    val name: String? = "",
    val term: String? = ""
) {
    //Term field is not part of the entity but is needed to represent the most recent term that is offered

    /**
    Page and limit are query parameters, they coexist with eachother and one without the other
     * mean nothing. (e.g. page=1(or more)&limit=0 makes no sense, and the parameters are ignored)
     * The default is 0 as its a optional parameter, when limit is 0 (or under) the sql query behaves as the parameters
     * are not there and a limit is not estabilished. ( will return all rows )
     */

    /*
     * Redefines invoke for a constructor with validations,
     * in case that any validation fails, returns null.
     *
     * Constraint verifications like lengths are managed by the
     * postgresql db.
     */
    companion object {
        fun of(
            id: Int,
            acronym: String,
            name: String?,
            term: String?
        ): Course? {

            if (acronym.trim() == "" || name?.trim() == "")
                return null

            return Course(id, acronym, name, term)
        }

        operator fun invoke(
            id: Int,
            acronym: String,
            name: String?,
            term: String?
        ): Course? = of(id, acronym, name, term)
    }
}