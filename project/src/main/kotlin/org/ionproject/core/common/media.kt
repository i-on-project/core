package org.ionproject.core.common

import org.springframework.http.MediaType

const val APPLICATION_TYPE = "application"
const val TEXT_TYPE = "text"
const val CALENDAR_SUBTYPE = "calendar"
const val PROBLEM_JSON_SUBTYPE = "problem+json"

const val TEXT_CALENDAR = "$TEXT_TYPE/$CALENDAR_SUBTYPE"
const val PROBLEM_JSON = "$APPLICATION_TYPE/$PROBLEM_JSON_SUBTYPE"

val PROBLEM_JSON_MEDIA_TYPE = MediaType(APPLICATION_TYPE, PROBLEM_JSON_SUBTYPE)
val TEXT_CALENDAR_MEDIA_TYPE = MediaType(TEXT_TYPE, CALENDAR_SUBTYPE)