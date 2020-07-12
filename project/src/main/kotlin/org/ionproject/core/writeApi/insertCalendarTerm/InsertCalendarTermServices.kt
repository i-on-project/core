package org.ionproject.core.writeApi.insertCalendarTerm

import org.ionproject.core.writeApi.insertCalendarTerm.json.OperationParams
import org.springframework.stereotype.Component

/**
 * The main goal of this class is to unclutter the Controller class.
 */
@Component
class InsertCalendarTermServices(private val repo: InsertCalendarTermRepo) {
    fun insertCalendarTerm(params: OperationParams): String? {
        //TODO
        return null
    }
}
