package org.ionproject.core.klass

import org.ionproject.core.classSection.ClassSection
import org.ionproject.core.common.transaction.TransactionManager
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
    fun get(acr: String, calendarTerm: String): FullKlass?
    fun getPage(acr: String, page: Int, size: Int): List<Klass>?
}

@Component
class KlassRepoImplementation(private val tm: TransactionManager) : KlassRepo {

    /**
     * Retrieve the target [Class] resource from the database, with all its details.
     */
    override fun get(acr: String, calendarTerm: String): FullKlass? =
            tm.run(TransactionIsolationLevel.READ_COMMITTED) { handle ->
        val acrUpper = acr.toUpperCase()

        val klass = handle
            .createQuery(
                """select CR.acronym, C.term from dbo.Class as C
                join dbo.Course as CR on C.courseid=CR.id
                where CR.acronym=:acr and C.term=:term""".trimIndent())
            .bind("acr", acrUpper)
            .bind("term", calendarTerm)
            .map { ro, _ -> Klass(ro.getString("acronym"), ro.getString("term")) }
            .findOne()

        if (klass.isEmpty) {
            throw ClassNotInDbException()
        }

        val sections = handle
            .createQuery(
                """select CR.acronym, C.term, CS.id from dbo.Class as C
                join dbo.ClassSection as CS on C.courseid=CS.courseid and C.term=CS.term
                join dbo.Course as CR on CR.id=C.courseid where CR.acronym=:acr and C.term=:term;""".trimIndent())
            .bind("acr", acrUpper)
            .bind("term", calendarTerm)
            .map { ro, _ -> ClassSection(ro.getString("acronym"), ro.getString("term"), ro.getString("id")) }
            .list()

        FullKlass(klass.get().course, klass.get().calendarTerm, sections)
    }

    /**
     * Retrieve a list of [Class]es, with only the essential information i.e. IDs, name, etc.
     */
    override fun getPage(acr: String, page: Int, size: Int): List<Klass>? = tm.run(TransactionIsolationLevel.READ_COMMITTED) { handle ->
        val acrUpper = acr.toUpperCase()

        handle
            .createQuery(
                """select CR.acronym, C.term from dbo.Class as C
                join dbo.Course as CR on C.courseid=CR.id
                where CR.acronym=:acr order by C.term offset :page limit :size;""".trimIndent())
            .bind("acr", acrUpper)
            .bind("page", page)
            .bind("size", size)
            .map { ro, _ -> Klass(ro.getString("acronym"), ro.getString("term")) }
            .list()
    }
}