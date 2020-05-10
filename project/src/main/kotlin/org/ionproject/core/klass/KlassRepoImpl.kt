package org.ionproject.core.klass

import org.ionproject.core.classSection.ClassSectionMapper
import org.ionproject.core.common.customExceptions.ResourceNotFoundException
import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.klass.mappers.KlassMapper
import org.ionproject.core.klass.model.FullKlass
import org.ionproject.core.klass.model.Klass
import org.springframework.stereotype.Component

@Component
class KlassRepoImplementation(
    private val tm: TransactionManager,
    private val klassMapper: KlassMapper,
    private val classSectionMapper: ClassSectionMapper
) : KlassRepo {
    /**
     * Retrieve the target [Class] resource from the database, with all its details.
     */
    override fun get(id: Int, calendarTerm: String): FullKlass? = tm.run { handle ->
        val klass = handle
            .createQuery(
                """select CR.id as cid, CR.acronym, C.term from dbo.Class as C
                join dbo.Course as CR on C.courseid=CR.id
                where CR.id=:cid and C.term=:term""".trimIndent()
            )
            .bind("cid", id)
            .bind("term", calendarTerm)
            .map(klassMapper)
            .findOne()

        if (!klass.isPresent) {
            return@run null
        }

        val klassObj = klass.get()
        val sections = handle
            .createQuery(
                """select CR.id as cid, CR.acronym, C.term, CS.id as sid from dbo.Class as C
                join dbo.ClassSection as CS on C.courseid=CS.courseid and C.term=CS.term
                join dbo.Course as CR on CR.id=C.courseid where CR.id=:cid and C.term=:term;""".trimIndent()
            )
            .bind("cid", id)
            .bind("term", calendarTerm)
            .map(classSectionMapper)
            .list()

        FullKlass(klassObj.courseId, klassObj.courseAcr, klassObj.calendarTerm, sections)
    }

    /**
     * Retrieve a list of [Class]es, with only the essential information i.e. IDs, name, etc.
     */
    override fun getPage(id: Int, page: Int, limit: Int): List<Klass> {
        val result = tm.run { handle ->
            handle
                    .createQuery(
                            """select CR.id as cid, CR.acronym, C.term from dbo.Class as C
                join dbo.Course as CR on C.courseid=CR.id
                where CR.id=:cid order by C.term offset :page limit :limit;""".trimIndent()
                    )
                    .bind("cid", id)
                    .bind("page", page * limit)
                    .bind("limit", limit)
                    .map(klassMapper)
                    .list()
        } as List<Klass>

        if(result.isEmpty()) {
            if(page > 0)
                throw ResourceNotFoundException("No results for page $page with limit $limit.")
        }

        return result
    }
}