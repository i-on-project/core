package org.ionproject.core.readApi.klass

import org.ionproject.core.readApi.klass.model.FullKlass
import org.ionproject.core.readApi.klass.model.Klass

interface KlassRepo {
    fun get(id: Int, calendarTerm: String): FullKlass?
    fun getPage(id: Int, page: Int, limit: Int): List<Klass>
}
