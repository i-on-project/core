package org.ionproject.core.common.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.ionproject.core.common.modelInterfaces.ICourse
import org.ionproject.core.common.modelInterfaces.IProgrammeOffer

class ProgrammeOffer(override val id: Int,
                     override val acronym: String,
                     override val termNumber: Int,
                     override val credits: Double,
                     override val optional: Boolean,
                     @JsonIgnore override val precedents: List<ICourse>? = null) : IProgrammeOffer {
    /*
     * Added the annotation `@JsonIgnore` to the field `precedents` to avoid jackson from serializing
     * the field, and follow the documentation.
     * Also on the deserialization is not needed to create a programme.
     * TODO: É UMA MÁ PRÁTICA FAZER ISTO?
     *  @JsonProperty para o setter se for necessário
     */

    /*
     * Programme Offer constructor with validations
     */
    private fun of(id : Int,
                   acronym : String,
                   termNumber : Int,
                   credits : List<IProgrammeOffer>,
                   optional : Boolean,
                   precedents : List<String>) {
        /*
        * Validations
        */

    }

    operator fun invoke(id : Int,
                        acronym : String,
                        termNumber : Int,
                        credits : List<IProgrammeOffer>,
                        optional : Boolean,
                        precedents : List<String>) = of(id, acronym, termNumber, credits, optional, precedents)
}