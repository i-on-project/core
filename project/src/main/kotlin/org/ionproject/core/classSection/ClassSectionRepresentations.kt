package org.ionproject.core.classSection

import org.ionproject.core.common.Action
import org.ionproject.core.common.Siren
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.springframework.web.util.UriTemplate

val classSectionClasses = arrayOf("class", "section")

fun ClassSection.toSiren(): Siren {
    val selfHref = Uri.forClassSectionById(courseId, calendarTerm, id)
    return SirenBuilder(this)
        .klass(*classSectionClasses)
        .link("self", selfHref)
        .link("collection", Uri.forKlassByCalTerm(courseId, calendarTerm))
        .action(Action.genDeleteAction(UriTemplate("${Uri.forClassSectionById(courseId, calendarTerm, id)}${Uri.rfcPagingQuery}")))
        .toSiren()
}
