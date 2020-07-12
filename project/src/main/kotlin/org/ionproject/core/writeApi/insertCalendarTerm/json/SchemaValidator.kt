package org.ionproject.core.writeApi.insertCalendarTerm.json

import com.fasterxml.jackson.databind.JsonNode
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import java.net.URI

private const val insertCalendarTermSchema = """{
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
    "date": {
      "${'$'}id": "#date",
      "type": "string"
    },
    "interval": {
      "${'$'}id": "#interval",
      "properties": {
        "startDate": { "${'$'}ref": "#date" },
        "endDate": { "${'$'}ref": "#date" },
        "name": { "type": "string" },
        "curricularTerm": {
          "type": "array",
          "items": { "type": "integer" }
        },
        "excludes": {
          "type": "array",
          "items": { "type": "integer" }
        },
        "categories": {
          "type": "array",
          "items": { "type": "integer" }
        }
      },
      "required": [ "name", "startDate", "endDate" ],
      "additionalProperties": false
    }
  },
  "${'$'}id": "#root",
  "type": "object",
  "properties": {
    "school": { "${'$'}ref": "#academicObject" },
    "calendarTerm": { "type": "string" },
    "language": { "type": "string" },
    "startDate": { "${'$'}ref": "#date" },
    "endDate": { "${'$'}ref": "#date" },
    "intervals": {
      "type": "array",
      "items": { "${'$'}ref": "#interval" }
    }
  },
  "required": [ "school", "calendarTerm", "startDate", "endDate", "intervals" ],
  "additionalProperties": false
}"""

object SchemaValidator {
    val schemaDocUri =
        URI("https://github.com/i-on-project/core/blob/docs/gh-123-sketch-write-api-format-doc/docs/api/write/schemas/insertCalendarTermEvents.json")

    private val validator = JsonSchemaFactory
        .getInstance(SpecVersion.VersionFlag.V201909)
        .getSchema(insertCalendarTermSchema)

    fun validate(jsonNode: JsonNode): List<String> =
        validator
            .validate(jsonNode)
            .map { it.message }
}
