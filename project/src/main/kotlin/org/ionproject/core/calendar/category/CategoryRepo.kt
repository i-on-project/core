package org.ionproject.core.calendar.category

import org.ionproject.core.calendar.category.CategoryData.ALL_CATEGORIES_QUERY
import org.ionproject.core.calendar.icalendar.properties.parameters.Language
import org.ionproject.core.common.transaction.TransactionManager
import org.springframework.stereotype.Repository

interface CategoryRepo {
    fun byId(id: Int) : Pair<Language, String>?
}

@Repository
class CategoryRepoImpl(
    private val transactionManager: TransactionManager,
    private val categoryMapper: CategoryData.CategoryMapper
) : CategoryRepo {
    init {
        populateCategoryMap()
    }

    private val categories = hashMapOf<Int, Pair<Language, String>>()

    private fun populateCategoryMap() {
        transactionManager.run {
            it
                .createQuery(ALL_CATEGORIES_QUERY)
                .map(categoryMapper)
                .list()
        }?.forEach {
            categories += it
        }
    }

    override fun byId(id: Int): Pair<Language, String>? =
        categories[id]

}