package org.ionproject.core.common.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.ionproject.core.common.modelInterfaces.IProgramme
import org.ionproject.core.common.modelInterfaces.IProgrammeOffer

class Programme(override val name: String,
                override val acronym: String,
                override val termSize: Int,
                @JsonIgnore override val offers: List<IProgrammeOffer>) : IProgramme {
    /*
     * Added the annotation `@JsonIgnore` to the field `offers` to avoid jackson from serializing
     * the field, and follow the documentation.
     * Also on the deserialization is not needed to create a programme.
     * TODO: É UMA MÁ PRÁTICA FAZER ISTO?
     *  @JsonProperty para o setter se for necessário
     */

    /*
    * Programme constructor with validations
    */
    private fun of(name : String,
                   acronym : String,
                   termSize : Int,
                   offers : List<IProgrammeOffer>) {
        /*
         * Validations
         */

    }

    operator fun invoke(name : String,
                        acronym : String,
                        termSize : Int,
                        offers : List<IProgrammeOffer>) = of(name, acronym, termSize, offers)
}