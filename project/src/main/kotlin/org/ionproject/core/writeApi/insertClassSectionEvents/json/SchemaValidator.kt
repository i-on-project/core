package org.ionproject.core.writeApi.insertClassSectionEvents.json

import com.fasterxml.jackson.databind.JsonNode
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import java.net.URI

private const val insertClassSectionEventsSchema = """{
  "definitions": {
    "academicObject": {
      "${'$'}id": "#academicObject",
      "type": "object",
      "properties": {
        "name": {
          "type": "string",
          "minLength": 2,
          "maxLength": 50
        },
        "acr": {
          "type": "string",
          "minLength": 2,
          "maxLength": 10
        }
      },
      "anyOf": [
        { "required": [ "name" ] },
        { "required": [ "acr" ] }
      ],
      "additionalProperties": false
    },
    "event": {
      "${'$'}id": "#event",
      "type": "object",
      "properties": {
        "title": { "type": "string" },
        "description": { "type": "string" },
        "location": {
          "type": "array",
          "items": { "type": "string" }
        },
        "beginTime": { "type": "string" },
        "duration": { "type": "string" },
        "weekday": {
          "type": "array",
          "items": {
            "type": "string",
            "enum": [ "MO", "TU", "WE", "TH", "FR", "SA", "SU" ]
          }
        }
      },
      "required": [ "beginTime", "duration" ],
      "additionalProperties": false
    },
    "course": {
      "${'$'}id": "#course",
      "properties": {
        "label": { "${'$'}ref": "#academicObject" },
        "events": {
          "type": "array",
          "items": { "${'$'}ref": "#event" }
        }
      },
      "required": [ "label", "events" ],
      "additionalProperties": false
    }
  },
  "${'$'}id": "#root",
  "type": "object",
  "properties": {
    "school": { "${'$'}ref": "#academicObject" },
    "programme": { "${'$'}ref": "#academicObject" },
    "calendarSection": { "type": "string" },
    "calendarTerm": { "type": "string" },
    "type": { "type": "string" },
    "lang": { "type": "string" },
    "courses": {
      "type": "array",
      "items": { "${'$'}ref": "#course" }
    }
  },
  "required": [ "school", "programme", "courses", "calendarTerm" ],
  "additionalProperties": false
}"""

object SchemaValidator {
    val schemaDocUri =
        URI("https://github.com/i-on-project/core/blob/docs/gh-123-sketch-write-api-format-doc/docs/api/write/schemas/insertClassSectionEvents.json")

    private val validator = JsonSchemaFactory
        .getInstance(SpecVersion.VersionFlag.V201909)
        .getSchema(insertClassSectionEventsSchema)

    fun validate(jsonNode: JsonNode): List<String> =
        validator
            .validate(jsonNode)
            .map { it.message }
}