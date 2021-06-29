package org.ionproject.core.ingestion

import org.ionproject.core.common.Uri
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IngestionController(val ingestionTask: IngestionTask) {

    // TODO: protect this resource
    @GetMapping(Uri.ingestionWebhook)
    fun processWebhookNotification(): ResponseEntity<Unit> {
        ingestionTask.processTask()
        return ResponseEntity.noContent().build()
    }
}
