package org.ionproject.core.common.modelInterfaces

interface IProgrammeOffer {
    val id : Int
    val programmeId : Int
    val courseId : Int
    val courseAcr : String
    val termNumber : Int
    val optional : Boolean
}