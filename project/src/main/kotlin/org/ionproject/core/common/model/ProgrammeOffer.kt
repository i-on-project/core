package org.ionproject.core.common.model

class ProgrammeOffer(val id: Int,
                     val courseAcr: String,
                     val programmeId: Int,
                     val courseId: Int,
                     val termNumber: Int,
                     val optional: Boolean) {


    /*
     * Programme Offer constructor with validations
     */
    private fun of(id : Int,
                   courseAcr : String,
                   programmeId: Int,
                   courseId : Int,
                   termNumber : Int,
                   optional : Boolean) {
        /*
        * Validations
        */

    }

    operator fun invoke(id : Int,
                        courseAcr : String,
                        programmeId: Int,
                        courseId : Int,
                        termNumber : Int,
                        optional : Boolean) = of(id, courseAcr, programmeId,  courseId, termNumber,optional)
}