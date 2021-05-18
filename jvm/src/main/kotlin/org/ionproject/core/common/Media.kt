package org.ionproject.core.common

import org.springframework.http.MediaType

object Media {
    private const val APPLICATION_TYPE = "application"
    private const val TEXT_TYPE = "text"
    private const val SIREN_SUBTYPE = "vnd.siren+json"
    private const val CALENDAR_SUBTYPE = "calendar"
    private const val JSON_SUBTYPE = "json"
    private const val HOME_SUBTYPE = "json-home"
    private const val PROBLEM_SUBTYPE = "problem+json"
    private const val FORM_URL_ENCODED_SUBTYPE = "x-www-form-urlencoded"

    const val ALL = "*/*"
    const val APPLICATION_JSON = "$APPLICATION_TYPE/$JSON_SUBTYPE"
    const val PROBLEM_JSON = "$APPLICATION_TYPE/$PROBLEM_SUBTYPE"
    const val SIREN_TYPE = "$APPLICATION_TYPE/$SIREN_SUBTYPE"
    const val JSON_HOME = "$APPLICATION_TYPE/$HOME_SUBTYPE"
    const val CALENDAR = "$TEXT_TYPE/$CALENDAR_SUBTYPE"
    const val FORM_URLENCODED_VALUE = "$APPLICATION_TYPE/$FORM_URL_ENCODED_SUBTYPE"

    val MEDIA_ALL = MediaType("*", "*")
    val MEDIA_SIREN = MediaType(APPLICATION_TYPE, SIREN_SUBTYPE)
    val MEDIA_JSON = MediaType(APPLICATION_TYPE, JSON_SUBTYPE)
    val MEDIA_PROBLEM = MediaType(APPLICATION_TYPE, PROBLEM_SUBTYPE)
    val MEDIA_HOME = MediaType(APPLICATION_TYPE, HOME_SUBTYPE)
    val MEDIA_TEXT_CALENDAR = MediaType(TEXT_TYPE, CALENDAR_SUBTYPE)
    val MEDIA_FORM_URLENCODED_VALUE = MediaType(APPLICATION_TYPE, FORM_URL_ENCODED_SUBTYPE)
}
