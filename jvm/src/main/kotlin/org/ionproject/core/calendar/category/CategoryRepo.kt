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

    private val categories: HashMap<Int, MutableList<Category>> by lazy {
        val map = hashMapOf<Int, MutableList<Category>>()
        transactionManager.run {
            it.createQuery(ALL_CATEGORIES_QUERY)
                .map(categoryMapper)
                .forEach { pair ->
                    map.computeIfAbsent(pair.first) {
                        mutableListOf()
                    }.add(pair.second)
                }
        }

        map
    }

    override fun byId(id: Int): List<Category>? =
        categories[id]
}

class Category(
    val value: String,
    val language: Language
)
