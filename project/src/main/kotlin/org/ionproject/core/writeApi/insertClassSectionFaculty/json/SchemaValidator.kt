package org.ionproject.core.writeApi.insertClassSectionFaculty.json

import com.fasterxml.jackson.databind.JsonNode
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import java.net.URI

private const val insertClassSectionFacultySchema = """{
  "definitions": {
    "academicObject": {
      "${'$'}id": "#academicObject",
      "type": "object",
      "properties": {
        "name": {
          "type": "string",
          "minLength": 2,
          "maxLength": 100
        },
        "acr": {
          "type": "string",
          "minLength": 1,
          "maxLength": 10
        }
      },
      "required": [ "acr" ],
      "additionalProperties": false
    },
    "teacher": {
      "${'$'}id": "#teacher",
      "type": "object",
      "properties": {
        "name": { "type": "string" }
      },
      "required": [ "name" ],
      "additionalProperties": false
    },
    "course": {
      "${'$'}id": "#course",
      "properties": {
        "label": { "${'$'}ref": "#academicObject" },
        "teachers": {
          "type": "array",
          "minItems": 1,
          "items": { "${'$'}ref": "#teacher" }
        }
      },
      "required": [ "label", "teachers" ],
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
}"""

object SchemaValidator {
    val schemaDocUri =
        URI("https://github.com/i-on-project/core/blob/docs/gh-123-sketch-write-api-format-doc/docs/api/write/schemas/insertClassSectionFaculty.json")

    private val validator = JsonSchemaFactory
        .getInstance(SpecVersion.VersionFlag.V201909)
        .getSchema(insertClassSectionFacultySchema)

    fun validate(jsonNode: JsonNode): List<String> =
        validator
            .validate(jsonNode)
            .map { it.message }
}
