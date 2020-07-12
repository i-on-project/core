package org.ionproject.core.writeApi.insertCalendarTerm

import com.fasterxml.jackson.databind.JsonNode
import org.ionproject.core.common.Media
import org.ionproject.core.common.ProblemJson
import org.ionproject.core.common.ResourceIdentifierAnnotation
import org.ionproject.core.writeApi.common.ResourceIds
import org.ionproject.core.writeApi.common.Uri
import org.ionproject.core.writeApi.insertCalendarTerm.json.OperationParams
import org.ionproject.core.writeApi.insertCalendarTerm.json.SchemaValidator
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class InsertCalendarTermController(private val services: InsertCalendarTermServices) {

    @ResourceIdentifierAnnotation(ResourceIds.INSERT_CALENDAR_TERM, ResourceIds.VERSION)
    @PutMapping(Uri.insertCalendarTerm, consumes = [Media.APPLICATION_JSON])
    fun insertCalendarTerm(@RequestBody json: JsonNode): ResponseEntity<Any> {

        /**
         * Analyze the received JSON object in comparison with the defined
         * JSON Schema (statically defined JSON object which enforces syntax and semantic restrictions on the requests).
         */
        val errMessages = SchemaValidator.validate(json)
        if (errMessages.isNotEmpty()) {
            // Invalid JSON received
            return ResponseEntity.badRequest().body(
                ProblemJson(
                    "/err/write/insertCalendarTerm/jsonSchemaConstraintViolation",
                    "JSON Schema constraint violation.",
                    400,
                    "The provided request body was invalid for the insertCalendarTerm' JSON Schema. Failed constraints: [ ${errMessages.joinToString(
                        ";"
                    )} ]. This operation's JSON Schema: ${SchemaValidator.schemaDocUri}.",
                    "/v0/insertCalendarTerm"
                )
            )
        }

        // Translate the JSON Node into an easier to use/read object
        val params = OperationParams.of(json)
        // Execute DB transaction and retrieve error message (if any)
        val err = services.insertCalendarTerm(params)
        return if (err != null) {
            ResponseEntity.badRequest().body(
                ProblemJson(
                    "/err/write/constraint",
                    "Transaction aborted",
                    400,
                    err,
                    "/v0/insertCalendarTerm"
                )
            )
        } else {
            // No error messages returned means transaction concluded successfully
            ResponseEntity.ok().build()
        }
    }
}
