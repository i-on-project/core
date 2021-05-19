package org.ionproject.core.userApi.klass.model

data class UserKlass(
    val id: Int,
    val courseId: Int,
    val courseAcr: String,
    val calendarTerm: String,
    val sections: Set<String>? = null
) {
    operator fun plus(newSections: Set<String>): UserKlass {
        val combined = if (sections == null) {
            newSections
        } else {
            val set = sections.toMutableSet()
            set.addAll(newSections)
            set
        }

        return UserKlass(
            id,
            courseId,
            courseAcr,
            calendarTerm,
            combined
        )
    }
}