package org.ionproject.core.common


import org.springframework.http.MediaType

object Media {
    private const val APPLICATION_TYPE = "application"
    private const val SIREN_SUBTYPE = "vnd.siren+json"
    private const val JSON_HOME_SUBTYPE = "json-home"

    val APPLICATION_JSON = MediaType.APPLICATION_JSON
    val PROBLEM_JSON = MediaType.APPLICATION_PROBLEM_JSON
    val JSON_HOME = MediaType(APPLICATION_TYPE, JSON_HOME_SUBTYPE)
    val SIREN_TYPE = MediaType(APPLICATION_TYPE, SIREN_SUBTYPE)
}