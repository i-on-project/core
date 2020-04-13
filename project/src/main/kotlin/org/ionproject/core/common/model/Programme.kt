package org.ionproject.core.common.model

class Programme(val id: Int,
                val name: String? = "",
                val acronym: String,
                val termSize: Int,
                val offers: MutableList<ProgrammeOffer>) {


    /*
    * Programme constructor with validations
    */
    private fun of(id: Int,
                   name : String?,
                   acronym : String,
                   termSize : Int,
                   offers : List<ProgrammeOffer>) {
        /*
         * Validations
         */

    }

    operator fun invoke(id: Int,
                        name : String?,
                        acronym : String,
                        termSize : Int,
                        offers : List<ProgrammeOffer>) = of(id, name, acronym, termSize, offers)
}