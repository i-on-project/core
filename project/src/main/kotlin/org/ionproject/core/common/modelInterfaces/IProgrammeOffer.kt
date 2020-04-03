package org.ionproject.core.common.modelInterfaces

interface IProgrammeOffer {
    val id : Int
    val acronym : String
    val termNumber : Int
    val credits : Double
    val optional : Boolean
    val precedents : List<ICourse>?
}