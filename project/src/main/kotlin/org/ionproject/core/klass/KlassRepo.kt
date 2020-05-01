package org.ionproject.core.klass

import org.ionproject.core.klass.model.FullKlass
import org.ionproject.core.klass.model.Klass

interface KlassRepo {
    fun get(id: Int, calendarTerm: String): FullKlass?
    fun getPage(id: Int, page: Int, limit: Int): List<Klass>
}
