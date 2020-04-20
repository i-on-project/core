package org.ionproject.core.programme

import org.ionproject.core.common.*
import org.ionproject.core.common.model.Programme
import org.ionproject.core.common.model.ProgrammeOffer
import org.ionproject.core.programme.representations.offerToDetailRepr
import org.ionproject.core.programme.representations.programmeToDetailRepr
import org.ionproject.core.programme.representations.programmesListRepr
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ProgrammeController(private val programmeServices: ProgrammeServices) {

    @GetMapping(Uri.programmes, produces = [Media.SIREN_TYPE])
    fun getProgrammes() : Siren =
            programmesListRepr(programmeServices.getProgrammes())


    @GetMapping(Uri.programmesById, produces = [Media.SIREN_TYPE])
    fun getProgramme(@PathVariable id : Int) : Siren =
            programmeToDetailRepr(programmeServices.getProgrammeById(id))


    @GetMapping(Uri.programmeOfferById, produces = [Media.SIREN_TYPE])
    fun getOffer(@PathVariable id : Int) : Siren =
            offerToDetailRepr(programmeServices.getOfferById(id))


    @PutMapping(Uri.programmesById)
    fun editProgramme(programme : Programme) {
        TODO("Write API")
    }

    @PostMapping(Uri.programmes)
    fun addProgramme(programme : Programme) {
        TODO("Write API")
    }

    @PostMapping(Uri.programmeByIdOffer)
    fun addOffer(id : Int, offer : ProgrammeOffer) {
        TODO("Write API")
    }
}