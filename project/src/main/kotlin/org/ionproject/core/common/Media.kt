package org.ionproject.core.common

import org.springframework.http.MediaType

object Media {
    private const val APPLICATION_TYPE = "application"
    private const val SIREN_SUBTYPE = "vnd.siren+json"

    const val ALL = "*/*"
    const val APPLICATION_JSON = "${APPLICATION_TYPE}/json"
    const val PROBLEM_JSON = "${APPLICATION_TYPE}/problem+json"
    const val SIREN_TYPE = "${APPLICATION_TYPE}/${SIREN_SUBTYPE}"
    const val JSON_HOME = "${APPLICATION_TYPE}/json-home"

    val MEDIA_SIREN = MediaType(APPLICATION_TYPE, SIREN_SUBTYPE)
}