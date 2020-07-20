package org.ionproject.core.writeApi.insertCalendarTerm

import org.ionproject.core.writeApi.insertCalendarTerm.json.OperationParams

interface InsertCalendarTermRepo {
    fun insertCalTerm(params: OperationParams)

}
