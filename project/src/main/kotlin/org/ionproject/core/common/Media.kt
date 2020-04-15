package org.ionproject.core.common

import org.springframework.http.MediaType

object Media {
    private const val APPLICATION_TYPE = "application"
    private const val TEXT_TYPE = "text"
    private const val SIREN_SUBTYPE = "vnd.siren+json"
    private const val CALENDAR_SUBTYPE = "calendar"
    private const val PROBLEM_JSON_SUBTYPE = "problem+json"
    private const val JSON_HOME_SUBTYPE = "json-home"

    const val ALL = "*/*"
    const val APPLICATION_JSON = "${APPLICATION_TYPE}/json"
    const val PROBLEM_JSON = "${APPLICATION_TYPE}/${PROBLEM_JSON_SUBTYPE}"
    const val SIREN_TYPE = "${APPLICATION_TYPE}/${SIREN_SUBTYPE}"
    const val JSON_HOME = "${APPLICATION_TYPE}/$JSON_HOME_SUBTYPE"
    const val TEXT_CALENDAR = "$TEXT_TYPE/$CALENDAR_SUBTYPE"

    val SIREN_MEDIA_TYPE = MediaType(APPLICATION_TYPE, SIREN_SUBTYPE)
    val TEXT_CALENDAR_MEDIA_TYPE = MediaType(TEXT_TYPE, CALENDAR_SUBTYPE)
    val PROBLEM_JSON_MEDIA_TYPE = MediaType(APPLICATION_TYPE, PROBLEM_JSON_SUBTYPE)
}