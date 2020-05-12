package org.ionproject.core.klass

import org.ionproject.core.common.customExceptions.ResourceNotFoundException
import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.klass.sql.KlassClassSectionMapper
import org.ionproject.core.klass.sql.KlassData.CAL_TERM
import org.ionproject.core.klass.sql.KlassData.CID
import org.ionproject.core.klass.sql.KlassData.GET_CLASSES_QUERY
import org.ionproject.core.klass.sql.KlassData.GET_CLASS_QUERY
import org.ionproject.core.klass.sql.KlassData.GET_CLASS_SECTIONS_QUERY
import org.ionproject.core.klass.sql.KlassData.LIMIT
import org.ionproject.core.klass.sql.KlassData.OFFSET
import org.ionproject.core.klass.sql.KlassMapper
import org.ionproject.core.klass.model.FullKlass
import org.ionproject.core.klass.model.Klass
import org.springframework.stereotype.Component

@Component
class KlassRepoImplementation(
    private val tm: TransactionManager,
    private val klassMapper: KlassMapper,
    private val classSectionMapper: KlassClassSectionMapper
) : KlassRepo {
    /**
     * Retrieve the target [Class] resource from the database, with all its details.
     */
    override fun get(id: Int, calendarTerm: String): FullKlass? = tm.run { handle ->
        val klass = handle
            .createQuery(GET_CLASS_QUERY)
            .bind(CID, id)
            .bind(CAL_TERM, calendarTerm)
            .map(klassMapper)
            .findOne()

        if (!klass.isPresent) {
            return@run null
        }

        val klassObj = klass.get()
        val sections = handle
            .createQuery(GET_CLASS_SECTIONS_QUERY)
            .bind(CID, id)
            .bind(CAL_TERM, calendarTerm)
            .map(classSectionMapper)
            .list()

        FullKlass(
            klassObj.courseId,
            klassObj.courseAcr,
            klassObj.calendarTerm,
            sections)
    }

    /**
     * Retrieve a list of [Class]es, with only the essential information i.e. IDs, name, etc.
     */
    override fun getPage(id: Int, page: Int, limit: Int): List<Klass> {
        val result = tm.run { handle ->
            handle
                .createQuery(GET_CLASSES_QUERY)
                .bind(CID, id)
                .bind(OFFSET, page * limit)
                .bind(LIMIT, limit)
                .map(klassMapper)
                .list()
        } as List<Klass>

        if (result.isEmpty()) {
            if (page > 0)
                throw ResourceNotFoundException("No results for page $page with limit $limit.")
        }

        return result
    }
}