package org.ionproject.core.writeApi.insertCalendarTerm

import org.ionproject.core.common.transaction.TransactionManager
import org.springframework.stereotype.Component

@Component
class InsertCalendarTermRepoImpl (
    private val tm: TransactionManager
) : InsertCalendarTermRepo {

}
