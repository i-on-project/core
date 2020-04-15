package org.ionproject.core.classSection

import org.ionproject.core.common.customExceptions.ResourceNotFoundException
import org.ionproject.core.common.transaction.TransactionManager
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository

interface ClassSectionRepo {
    fun get(cid: Int, calendarTerm: String, id: String): ClassSection
}

@Repository
class ClassSectionRepoImplementation(private val tm: TransactionManager) : ClassSectionRepo {
    override fun get(cid: Int, calendarTerm: String, id: String): ClassSection = tm.run(TransactionIsolationLevel.READ_COMMITTED) { handle ->
        val sid = id.toUpperCase()
        val match = handle
            .createQuery(
                """select CR.id as cid, CR.acronym, CS.term, CS.id as sid from dbo.ClassSection as CS
                join dbo.Course as CR on CS.courseid=CR.id
                where CR.id=:cid and term=:term and CS.id=:sid;""".trimIndent()
            )
            .bind("cid", cid)
            .bind("sid", sid)
            .bind("term", calendarTerm)
            .map { ro, _ -> ClassSection(ro.getInt("cid"), ro.getString("acronym"), ro.getString("term"), ro.getString("sid")) }
            .findOne()

        if (!match.isPresent) {
            throw ResourceNotFoundException("")
        }

        match.get()
    } ?: throw ResourceNotFoundException("")
}