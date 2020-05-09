package org.ionproject.core.calendar.language

import org.ionproject.core.calendar.icalendar.properties.parameters.Language
import org.ionproject.core.common.transaction.TransactionManager
import org.springframework.stereotype.Repository

interface LanguageRepo {
    fun byId(id: Int): Language?
}

@Repository
class LanguagaRepoImpl(
    private val transactionManager: TransactionManager,
    private val languageMapper: LanguageData.LanguageMapper
) : LanguageRepo {

    private val languages = hashMapOf<Int, Language>()

    init {
        populateLanguageMap()
    }

    private fun populateLanguageMap() {
        val languages = transactionManager.run {
            it
                .createQuery(LanguageData.ALL_LANGUAGES_QUERY)
                .map(languageMapper)
                .list()
        }
        languages?.forEach {
            this.languages += it
        }
    }

    override fun byId(id: Int): Language? =
        languages[id]

}