package org.ionproject.core.programme

import org.ionproject.core.common.ResourceIdentifierAnnotation
import org.ionproject.core.common.ResourceIds
import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.ionproject.core.programme.representations.offerListRepr
import org.ionproject.core.programme.representations.offerToDetailRepr
import org.ionproject.core.programme.representations.programmeToDetailRepr
import org.ionproject.core.programme.representations.programmesListRepr
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ProgrammeController(private val repo: ProgrammeRepoImpl) {

    @ResourceIdentifierAnnotation(ResourceIds.GET_PROGRAMMES, ResourceIds.VERSION_0)
    @GetMapping(Uri.programmes)
    fun getProgrammes(): ResponseEntity<Siren> {
        val programmes = repo.getProgrammes()
        return ResponseEntity.ok(programmes.programmesListRepr())
    }

    @ResourceIdentifierAnnotation(ResourceIds.GET_PROGRAMME, ResourceIds.VERSION_0)
    @GetMapping(Uri.programmesById)
    fun getProgramme(@PathVariable id: Int): ResponseEntity<Siren> {
        val programme = repo.getProgrammeById(id)
        return ResponseEntity.ok(programme.programmeToDetailRepr())
    }

    @ResourceIdentifierAnnotation(ResourceIds.GET_OFFERS, ResourceIds.VERSION_0)
    @GetMapping(Uri.programmeByIdOffer)
    fun getProgrammeOffers(
        @PathVariable id: Int,
        pagination:
    ): ResponseEntity<Siren> {
        val programmeOffers = repo.getProgrammeOffers(id)
        return ResponseEntity.ok(programmeOffers.offerListRepr())
    }

    @ResourceIdentifierAnnotation(ResourceIds.GET_OFFER, ResourceIds.VERSION_0)
    @GetMapping(Uri.programmeOfferById)
    fun getOffer(@PathVariable idProgramme: Int, @PathVariable idOffer: Int): ResponseEntity<Siren> {
        val offer = repo.getOfferById(idProgramme, idOffer)
        return ResponseEntity.ok(offer.offerToDetailRepr())
    }
}
