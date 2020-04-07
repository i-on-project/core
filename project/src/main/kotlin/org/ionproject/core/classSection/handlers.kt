package org.ionproject.core.classSection

import org.ionproject.core.common.Action
import org.ionproject.core.common.Siren
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri

val classSectionClasses = arrayOf("class", "section")

class ClassSectionOutputModel(
    val cs: ClassSection) {

    fun toSiren(): Siren {
        val selfHref = Uri.forClassSectionById(cs.course, cs.calendarTerm, cs.id)
        return SirenBuilder(cs)
            .klass(*classSectionClasses)
            .link("self", selfHref)
            .link("collection", Uri.forKlassByTerm(cs.course, cs.calendarTerm))
            .action(Action.genDeleteAction(selfHref))
            .toSiren()
    }
}
