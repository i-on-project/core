package org.ionproject.core.programme

import org.ionproject.core.common.*
import org.ionproject.core.common.modelInterfaces.IProgramme
import org.ionproject.core.common.modelInterfaces.IProgrammeOffer
import org.springframework.web.bind.annotation.*

@RestController
class ProgrammeSpringController(private val services: ProgrammeServices) {

    @GetMapping(PROGRAMMES_PATH)
    fun getProgrammes() : Siren {
        val programmes = services.readProgrammes()
        return ProgrammesOutputModel(programmes).toSirenObject()
    }

    @GetMapping(PROGRAMMES_PATH_ACR)
    fun getProgramme(@PathVariable acr : String) : Siren {
        val programme = services.readProgramme(acr)
        return ProgrammeOutputModel(programme).toSirenObject()
    }

    @PutMapping(PROGRAMMES_PATH_ACR)
    fun editProgramme(programme : IProgramme) {
        TODO("Write API")
    }

    @PostMapping(PROGRAMMES_PATH)
    fun addProgramme(programme : IProgramme) {
        TODO("Write API")
    }

    @GetMapping(PROGRAMMES_PATH_ACR_OFFER_ID)
    fun getOffer(@PathVariable id : Int) : Siren {
        val offer = services.readOffer(id)
        return ProgrammeOfferOutputModel(offer).toSirenObject()
    }

    @PostMapping(PROGRAMMES_PATH_ACR_OFFER)
    fun addOffer(acr : String, offer : IProgrammeOffer) {
        TODO("Write API")
    }
}