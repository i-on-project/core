package org.ionproject.core.klass

import org.ionproject.core.classSection.ClassSection
import org.ionproject.core.common.customExceptions.ResourceNotFoundException
import org.ionproject.core.common.transaction.TransactionManager
import org.springframework.stereotype.Component

interface KlassRepo {
    fun get(id: Int, calendarTerm: String): FullKlass
    fun getPage(id: Int, page: Int, limit: Int): List<Klass>
}

@Component
class KlassRepoImplementation(private val tm: TransactionManager) : KlassRepo {

    /**
     * Retrieve the target [Class] resource from the database, with all its details.
     */
    override fun get(id: Int, calendarTerm: String): FullKlass = tm.run { handle ->
        val klass = handle
            .createQuery(
                """select CR.id as cid, CR.acronym, C.term from dbo.Class as C
                join dbo.Course as CR on C.courseid=CR.id
                where CR.id=:cid and C.term=:term""".trimIndent())
            .bind("cid", id)
            .bind("term", calendarTerm)
            .map { ro, _ -> Klass(ro.getInt("cid"), ro.getString("acronym"), ro.getString("term")) }
            .findOne()

        if (!klass.isPresent) {
            throw ResourceNotFoundException("")
        }

        val klassObj = klass.get()
        val sections = handle
            .createQuery(
                """select CR.id as cid, CR.acronym, C.term, CS.id as sid from dbo.Class as C
                join dbo.ClassSection as CS on C.courseid=CS.courseid and C.term=CS.term
                join dbo.Course as CR on CR.id=C.courseid where CR.id=:cid and C.term=:term;""".trimIndent())
            .bind("cid", id)
            .bind("term", calendarTerm)
            .map { ro, _ -> ClassSection(ro.getInt("cid"), ro.getString("acronym"), ro.getString("term"), ro.getString("sid")) }
            .list()

        FullKlass(klassObj.courseId, klassObj.courseAcr, klassObj.calendarTerm, sections)
    } ?: throw ResourceNotFoundException("")

    /**
     * Retrieve a list of [Class]es, with only the essential information i.e. IDs, name, etc.
     */
    override fun getPage(id: Int, page: Int, limit: Int): List<Klass> = tm.run { handle ->
        handle
            .createQuery(
                """select CR.id as cid, CR.acronym, C.term from dbo.Class as C
                join dbo.Course as CR on C.courseid=CR.id
                where CR.id=:cid order by C.term offset :page limit :limit;""".trimIndent())
            .bind("cid", id)
            .bind("page", page)
            .bind("limit", limit)
            .map { ro, _ -> Klass(ro.getInt("cid"), ro.getString("acronym"), ro.getString("term")) }
            .list()
    } ?: throw ResourceNotFoundException("")
}