package org.ionproject.core.common.modelInterfaces

interface IProgramme {
    val name : String
    val acronym : String
    val termSize : Int
    val offers : List<IProgrammeOffer>
}