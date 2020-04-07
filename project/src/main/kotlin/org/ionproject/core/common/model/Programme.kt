package org.ionproject.core.common.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.ionproject.core.common.modelInterfaces.IProgramme
import org.ionproject.core.common.modelInterfaces.IProgrammeOffer

class Programme(override val id: Int,
                override val name: String,
                override val acronym: String,
                override val termSize: Int,
                override val offers: List<IProgrammeOffer>) : IProgramme {


    /*
    * Programme constructor with validations
    */
    private fun of(id: Int,
                   name : String,
                   acronym : String,
                   termSize : Int,
                   offers : List<IProgrammeOffer>) {
        /*
         * Validations
         */

    }

    operator fun invoke(id: Int,
                        name : String,
                        acronym : String,
                        termSize : Int,
                        offers : List<IProgrammeOffer>) = of(id, name, acronym, termSize, offers)
}