package org.ionproject.core.programme

import org.ionproject.core.common.Media
import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.ionproject.core.programme.model.Programme
import org.ionproject.core.programme.model.ProgrammeOffer
import org.ionproject.core.programme.representations.offerToDetailRepr
import org.ionproject.core.programme.representations.programmeToDetailRepr
import org.ionproject.core.programme.representations.programmesListRepr
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ProgrammeController(private val programmeServices: ProgrammeServices) {

    @GetMapping(Uri.programmes, produces = [Media.SIREN_TYPE])
    fun getProgrammes(): Siren =
        programmeServices.getProgrammes().programmesListRepr()


    @GetMapping(Uri.programmesById, produces = [Media.SIREN_TYPE])
    fun getProgramme(@PathVariable id: Int): Siren =
        programmeServices.getProgrammeById(id).programmeToDetailRepr()


    @GetMapping(Uri.programmeOfferById, produces = [Media.SIREN_TYPE])
    fun getOffer(@PathVariable idProgramme: Int, @PathVariable idOffer: Int): Siren =
        programmeServices.getOfferById(idOffer, idProgramme).offerToDetailRepr()


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