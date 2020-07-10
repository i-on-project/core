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
          "minLength": 1,
          "maxLength": 100
        },
        "acr": {
          "type": "string",
          "minLength": 1,
          "maxLength": 50
        }
      },
      "required": [ "acr" ],
      "additionalProperties": false
    },
    "eventRecurrent": {
      "${'$'}id": "#event-recurrent",
      "type": "object",
      "properties": {
        "title": { "type": "string" },
        "description": { "type": "string" },
        "location": {
          "type": "array",
          "uniqueItems": true,
          "items": { "type": "string" }
        },
        "category": {
          "type": "integer",
          "minimum": 0
        },
        "beginTime": {
          "type": "string",
          "pattern": "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]${'$'}"
        },
        "duration": {
          "type": "string",
          "pattern": "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]${'$'}"
        },
        "weekday": {
          "type": "array",
          "minItems": 1,
          "maxItems": 7,
          "uniqueItems": true,
          "items": {
            "type": "string",
            "enum": [ "MO", "TU", "WE", "TH", "FR", "SA", "SU" ]
          }
        }
      },
      "required": [ "weekday", "beginTime", "duration", "category" ],
      "additionalProperties": false
    },
    "eventNonRecurrent": {
      "${'$'}id": "#event-non-recurrent",
      "type": "object",
      "properties": {
        "title": { "type": "string" },
        "description": { "type": "string" },
        "location": {
          "type": "array",
          "uniqueItems": true,
          "items": { "type": "string" }
        },
        "category": {
          "type": "integer",
          "minimum": 0
        },
        "startDate": {
          "type": "string",
          "pattern": "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])T(2[0-3]|[01][0-9]):[0-5][0-9]${'$'}"
        },
        "endDate": {
          "type": "string",
          "pattern": "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])T(2[0-3]|[01][0-9]):[0-5][0-9]${'$'}"
        }
      },
      "required": [ "startDate", "endDate", "category" ],
      "additionalProperties": false
    },
    "event": {
      "${'$'}id": "#event",
      "anyOf": [
        { "${'$'}ref": "#event-non-recurrent" },
        { "${'$'}ref": "#event-recurrent" }
      ]
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
    "language": { "type": "string" },
    "courses": {
      "type": "array",
      "minItems": 1,
      "items": { "${'$'}ref": "#course" }
    }
  },
  "required": [ "school", "programme", "courses", "calendarTerm", "calendarSection" ],
  "additionalProperties": false
}
"""

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
