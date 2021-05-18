package org.ionproject.core.programme.sql

import org.ionproject.core.programme.model.ProgrammeOffer
import org.jdbi.v3.core.result.RowReducer
import org.jdbi.v3.core.result.RowView
import org.springframework.stereotype.Component
import java.util.stream.Stream

class MutableProgrammeOffer(
    val id: Int,
    val courseAcr: String,
    val courseName: String,
    val programmeId: Int,
    val courseId: Int,
    val termNumber: MutableList<Int>,
    val optional: Boolean
) {
    fun toProgrammeOffer(): ProgrammeOffer = ProgrammeOffer(id, courseAcr, courseName, programmeId, courseId, termNumber, optional)
}

@Component
class ProgrammeOfferRowReducer : RowReducer<MutableMap<Int, MutableProgrammeOffer>, ProgrammeOffer> {
    override fun accumulate(container: MutableMap<Int, MutableProgrammeOffer>, rowView: RowView) {
        val id = rowView.getInt("id")
        val termNumber = rowView.getInt("termNumber")
        val offer = container[id]
        if (offer == null) {
            container[id] = MutableProgrammeOffer(
                id,
                rowView.getString("courseAcr"),
                rowView.getString("courseName"),
                rowView.getInt("programmeId"),
                rowView.getInt("courseId"),
                mutableListOf(termNumber),
                rowView.getBoolean("optional")
            )
        } else {
            offer.termNumber.add(termNumber)
        }
    }

    override fun stream(container: MutableMap<Int, MutableProgrammeOffer>): Stream<ProgrammeOffer> {
        return container.mapValues { it.value.toProgrammeOffer() }.values.stream()
    }

    override fun container(): MutableMap<Int, MutableProgrammeOffer> = mutableMapOf()
}

private fun RowView.getInt(name: String): Int = getColumn(name, Integer::class.java).toInt()
private fun RowView.getBoolean(name: String): Boolean = getColumn(name, java.lang.Boolean::class.java).booleanValue()
private fun RowView.getString(name: String): String = getColumn(name, String::class.java)
