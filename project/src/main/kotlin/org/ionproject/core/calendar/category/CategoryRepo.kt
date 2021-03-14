package org.ionproject.core.calendar.category

import org.ionproject.core.calendar.category.CategoryData.ALL_CATEGORIES_QUERY
import org.ionproject.core.calendar.icalendar.properties.parameters.Language
import org.ionproject.core.common.transaction.TransactionManager
import org.springframework.stereotype.Repository

interface CategoryRepo {
    fun byId(id: Int): List<Category>?
}

@Repository
class CategoryRepoImpl(
    private val transactionManager: TransactionManager,
    private val categoryMapper: CategoryData.CategoryMapper
) : CategoryRepo {

    private val categories = hashMapOf<Int, MutableList<Category>>()

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
            categories.computeIfAbsent(it.first) {
                mutableListOf()
            }.add(it.second)
        }
    }

    override fun byId(id: Int): List<Category>? =
        categories[id]
}

class Category(
    val value: String,
    val language: Language
)
