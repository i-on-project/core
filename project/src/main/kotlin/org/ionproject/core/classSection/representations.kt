package org.ionproject.core.classSection

import org.ionproject.core.common.Action
import org.ionproject.core.common.Siren
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.springframework.web.util.UriTemplate

val classSectionClasses = arrayOf("class", "section")

object ClassSectionToSiren {
    fun toSiren(cs: ClassSection): Siren {
        val selfHref = Uri.forClassSectionById(cs.courseId, cs.calendarTerm, cs.id)
        return SirenBuilder(cs)
            .klass(*classSectionClasses)
            .link("self", selfHref)
            .link("collection", Uri.forKlassByTerm(cs.courseId, cs.calendarTerm))
            .action(Action.genDeleteAction(UriTemplate("${Uri.forClassSectionById(cs.courseId, cs.calendarTerm, cs.id)}${Uri.pagingQuery}")))
            .toSiren()
    }
}
