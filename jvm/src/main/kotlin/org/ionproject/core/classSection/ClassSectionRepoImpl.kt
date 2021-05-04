package org.ionproject.core.classSection

import org.ionproject.core.classSection.sql.ClassSectionData.CAL_TERM
import org.ionproject.core.classSection.sql.ClassSectionData.CID
import org.ionproject.core.classSection.sql.ClassSectionData.CLASS_SECTION_QUERY
import org.ionproject.core.classSection.sql.ClassSectionData.ID
import org.ionproject.core.classSection.sql.ClassSectionMapper
import org.ionproject.core.common.transaction.TransactionManager
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository

@Repository
class ClassSectionRepoImplementation(
    private val tm: TransactionManager,
    private val classSectionMapper: ClassSectionMapper
) : ClassSectionRepo {
    override fun get(cid: Int, calendarTerm: String, id: String): ClassSection? =
        tm.run(TransactionIsolationLevel.READ_COMMITTED) { handle ->
            handle
                .createQuery(CLASS_SECTION_QUERY)
                .bind(CID, cid)
                .bind(ID, id.toUpperCase())
                .bind(CAL_TERM, calendarTerm)
                .map(classSectionMapper)
                .firstOrNull()
        }
}
