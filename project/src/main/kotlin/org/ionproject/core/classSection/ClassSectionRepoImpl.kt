package org.ionproject.core.classSection

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
                .createQuery(
                    """select CR.id as cid, CR.acronym, CS.term, CS.id as sid from dbo.ClassSection as CS
                join dbo.Course as CR on CS.courseid=CR.id
                where CR.id=:cid and term=:term and CS.id=:sid;""".trimIndent()
                )
                .bind("cid", cid)
                .bind("sid", id.toUpperCase())
                .bind("term", calendarTerm)
                .map(classSectionMapper)
                .firstOrNull()
        }
}