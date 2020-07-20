package org.ionproject.core.writeApi.insertCalendarTerm

import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.writeApi.insertCalendarTerm.json.OperationParams
import org.ionproject.core.writeApi.insertCalendarTerm.sql.InsertCalendarTerm.DTEND_PARAM
import org.ionproject.core.writeApi.insertCalendarTerm.sql.InsertCalendarTerm.DTSTART_PARAM
import org.ionproject.core.writeApi.insertCalendarTerm.sql.InsertCalendarTerm.ID
import org.ionproject.core.writeApi.insertCalendarTerm.sql.InsertCalendarTerm.INSERT_CAL_TERM_DATES
import org.springframework.stereotype.Component

@Component
class InsertCalendarTermRepoImpl (
    private val tm: TransactionManager
) : InsertCalendarTermRepo {

    /**
     * Ignores intervals at the moment, only updates calendar term dates
     */
    override fun insertCalTerm(params: OperationParams) {
        tm.run { handle ->
            {
                handle.createCall(INSERT_CAL_TERM_DATES)
                    .bind(ID, params.calendarTerm)
                    .bind(DTSTART_PARAM, params.startDate)
                    .bind(DTEND_PARAM, params.endDate)
                    .invoke()
            }()
        }
    }

}
