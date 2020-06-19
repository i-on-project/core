package org.ionproject.core.readApi.calendar.category

import org.ionproject.core.readApi.calendar.category.CategoryData.ALL_CATEGORIES_QUERY
import org.ionproject.core.readApi.calendar.icalendar.properties.parameters.Language
import org.ionproject.core.readApi.common.transaction.TransactionManager
import org.springframework.stereotype.Repository

interface CategoryRepo {
    fun byId(id: Int): Category?
}

@Repository
class CategoryRepoImpl(
    private val transactionManager: TransactionManager,
    private val categoryMapper: CategoryData.CategoryMapper
) : CategoryRepo {

    private val categories = hashMapOf<Int, Category>()

    init {
        populateCategoryMap()
    }

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

    override fun byId(id: Int): Category? =
        categories[id]

}

class Category(
    val value: String,
    val language: Language
)