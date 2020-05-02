package org.ionproject.core.classSection

import org.ionproject.core.common.*
import org.springframework.http.HttpMethod

val classSectionClasses = arrayOf("class", "section")

fun ClassSection.toSiren(): Siren {
    val selfHref = Uri.forClassSectionById(courseId, calendarTerm, id)
    return SirenBuilder(this)
        .klass(*classSectionClasses)
        .link("self", selfHref)
        .link("collection", Uri.forKlassByCalTerm(courseId, calendarTerm))
        .action(
            Action(
                name = "delete",
                href = selfHref.toTemplate(),
                method = HttpMethod.DELETE,
                type = Media.ALL,
                isTemplated = false
            )
        )
        .toSiren()
}
