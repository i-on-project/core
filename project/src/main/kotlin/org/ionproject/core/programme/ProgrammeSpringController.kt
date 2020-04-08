package org.ionproject.core.programme

import org.ionproject.core.common.*
import org.ionproject.core.common.modelInterfaces.IProgramme
import org.ionproject.core.common.modelInterfaces.IProgrammeOffer
import org.ionproject.core.programme.representations.offerToDetailRepr
import org.ionproject.core.programme.representations.programmeToDetailRepr
import org.ionproject.core.programme.representations.programmesListRepr
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ProgrammeSpringController(private val programmeServices: ProgrammeServices) {

    @GetMapping(PROGRAMMES_PATH)
    fun getProgrammes() : Siren = programmesListRepr(
            programmeServices.getProgrammes()
    )

    @GetMapping(PROGRAMMES_PATH_ACR)
    fun getProgramme(@PathVariable acr : String) : ResponseEntity<Siren> =
            programmeServices.getProgrammeByAcr(acr)
                    ?.let { ResponseEntity.ok(programmeToDetailRepr((it))) }
                    ?: ResponseEntity.notFound().build()

    @GetMapping(PROGRAMMES_PATH_ACR_OFFER_ID)
    fun getOffer(@PathVariable id : Int) : ResponseEntity<Siren> =
            programmeServices.getOfferById(id)
                    ?.let { ResponseEntity.ok(offerToDetailRepr(it)) }
                    ?: ResponseEntity.notFound().build()

    @PutMapping(PROGRAMMES_PATH_ACR)
    fun editProgramme(programme : IProgramme) {
        TODO("Write API")
    }

    @PostMapping(PROGRAMMES_PATH)
    fun addProgramme(programme : IProgramme) {
        TODO("Write API")
    }

    @PostMapping(PROGRAMMES_PATH_ACR_OFFER)
    fun addOffer(acr : String, offer : IProgrammeOffer) {
        TODO("Write API")
    }
}