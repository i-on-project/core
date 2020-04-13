package org.ionproject.core.klass

import org.ionproject.core.classSection.ClassSection
import org.ionproject.core.common.transaction.ITransactionManager
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Component

/**
 * When the target resource [Class] does not exist, this exception will be thrown.
 *
 * This exception <b>shall</b> not be thrown if the [Class] resource does exist, but one or more of
 * its properties don't (e.g. WAD, 1920v [Class] exists, but has no [ClassSections]).
 */
class ClassNotInDbException : Exception()

interface KlassRepo {
    fun get(id: Int, calendarTerm: String): FullKlass
    fun getPage(id: Int, page: Int, limit: Int): List<Klass>
}

@Component
class KlassRepoImplementation(private val tm: ITransactionManager) : KlassRepo {

    /**
     * Retrieve the target [Class] resource from the database, with all its details.
     */
    override fun get(id: Int, calendarTerm: String): FullKlass = tm.run(TransactionIsolationLevel.READ_COMMITTED) { handle ->
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
            throw ClassNotInDbException()
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
    }

    /**
     * Retrieve a list of [Class]es, with only the essential information i.e. IDs, name, etc.
     */
    override fun getPage(id: Int, page: Int, limit: Int): List<Klass> = tm.run(TransactionIsolationLevel.READ_COMMITTED) { handle ->
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
    }
}