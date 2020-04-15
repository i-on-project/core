package org.ionproject.core.programme

import org.ionproject.core.common.Media
import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.ionproject.core.common.model.Programme
import org.ionproject.core.common.model.ProgrammeOffer
import org.ionproject.core.programme.representations.offerToDetailRepr
import org.ionproject.core.programme.representations.programmeToDetailRepr
import org.ionproject.core.programme.representations.programmesListRepr
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ProgrammeController(private val programmeServices: ProgrammeServices) {

    @GetMapping(Uri.programmes)
    fun getProgrammes(): ResponseEntity<Siren> =
        programmeServices.getProgrammes()
            .let {
                ResponseEntity.ok()
                    .header("Content-Type", Media.SIREN_TYPE.toString())
                    .body(programmesListRepr(it))
            }

    @GetMapping(Uri.programmesById)
    fun getProgramme(@PathVariable id: Int): ResponseEntity<Siren> =
        programmeServices.getProgrammeById(id)
            .let {
                ResponseEntity.ok()
                    .header("Content-Type", Media.SIREN_TYPE.toString())
                    .body(programmeToDetailRepr((it)))
            }


    @GetMapping(Uri.programmeOfferById)
    fun getOffer(@PathVariable id: Int): ResponseEntity<Siren> =
        programmeServices.getOfferById(id)
            .let {
                ResponseEntity.ok()
                    .header("Content-Type", Media.SIREN_TYPE.toString())
                    .body(offerToDetailRepr(it))
            }


    @PutMapping(Uri.programmesById)
    fun editProgramme(programme: Programme) {
        TODO("Write API")
    }

    @PostMapping(Uri.programmes)
    fun addProgramme(programme: Programme) {
        TODO("Write API")
    }

    @PostMapping(Uri.programmeByIdOffer)
    fun addOffer(id: Int, offer: ProgrammeOffer) {
        TODO("Write API")
    }
}