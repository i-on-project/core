package org.ionproject.core.calendar.language

import org.ionproject.core.calendar.icalendar.properties.parameters.Language
import org.ionproject.core.common.transaction.TransactionManager
import org.springframework.stereotype.Repository

interface LanguageRepo {
    fun byId(id: Int): Language?
}

@Repository
class LanguageRepoImpl(
    private val transactionManager: TransactionManager,
    private val languageMapper: LanguageData.LanguageMapper
) : LanguageRepo {

    private val languages: Map<Int, Language> by lazy {
        transactionManager.run {
            it.createQuery(LanguageData.ALL_LANGUAGES_QUERY)
                .map(languageMapper)
                .toMap()
        }
    }

    override fun byId(id: Int): Language? =
        languages[id]
}
