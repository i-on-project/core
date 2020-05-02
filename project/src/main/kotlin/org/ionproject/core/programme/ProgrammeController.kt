package org.ionproject.core.programme

import org.ionproject.core.common.Media
import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.ionproject.core.programme.model.Programme
import org.ionproject.core.programme.model.ProgrammeOffer
import org.ionproject.core.programme.representations.offerToDetailRepr
import org.ionproject.core.programme.representations.programmeToDetailRepr
import org.ionproject.core.programme.representations.programmesListRepr
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ProgrammeController(private val programmeServices: ProgrammeServices) {

    @GetMapping(Uri.programmes, produces = [Media.SIREN_TYPE])
    fun getProgrammes(): ResponseEntity<Siren> {
        val programmes = programmeServices.getProgrammes()

        return ResponseEntity.ok(programmes.programmesListRepr())
    }

    @GetMapping(Uri.programmesById, produces = [Media.SIREN_TYPE])
    fun getProgramme(@PathVariable id: Int): ResponseEntity<Siren> {
        val programme = programmeServices.getProgrammeById(id)

        programme?.let { return ResponseEntity.ok(it.programmeToDetailRepr()) }
        return ResponseEntity.notFound().build()
    }

    @GetMapping(Uri.programmeOfferById, produces = [Media.SIREN_TYPE])
    fun getOffer(@PathVariable idProgramme: Int, @PathVariable idOffer: Int): ResponseEntity<Siren> {
        val offer = programmeServices.getOfferById(idOffer, idProgramme)

        offer?.let { return ResponseEntity.ok(it.offerToDetailRepr()) }
        return ResponseEntity.notFound().build()
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