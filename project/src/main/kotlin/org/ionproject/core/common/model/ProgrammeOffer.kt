package org.ionproject.core.common.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.ionproject.core.common.modelInterfaces.ICourse
import org.ionproject.core.common.modelInterfaces.IProgrammeOffer

class ProgrammeOffer(override val id: Int,
                     override val courseAcr: String,
                     override val programmeId: Int,
                     override val courseId: Int,
                     override val termNumber: Int,
                     override val optional: Boolean) : IProgrammeOffer {


    /*
     * Programme Offer constructor with validations
     */
    private fun of(id : Int,
                   courseAcr : String,
                   courseId : Int,
                   termNumber : Int,
                   optional : Boolean) {
        /*
        * Validations
        */

    }

    operator fun invoke(id : Int,
                        courseAcr : String,
                        courseId : Int,
                        termNumber : Int,
                        optional : Boolean) = of(id, courseAcr, courseId, termNumber,optional)
}