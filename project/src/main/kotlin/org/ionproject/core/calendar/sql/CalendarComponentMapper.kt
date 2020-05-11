package org.ionproject.core.calendar.sql

import org.ionproject.core.calendar.category.CategoryRepo
import org.ionproject.core.calendar.icalendar.CalendarComponent
import org.ionproject.core.calendar.icalendar.Event
import org.ionproject.core.calendar.icalendar.Journal
import org.ionproject.core.calendar.icalendar.Todo
import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeCreated
import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeStamp
import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeDue
import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeEnd
import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeStart
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Attachment
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Categories
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Description
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Summary
import org.ionproject.core.calendar.icalendar.properties.components.recurrence.RecurrenceRule
import org.ionproject.core.calendar.icalendar.properties.components.relationship.UniqueIdentifier
import org.ionproject.core.calendar.icalendar.types.*
import org.ionproject.core.calendar.language.LanguageRepo
import org.ionproject.core.common.customExceptions.ForeignKeyException
import org.ionproject.core.common.customExceptions.UnknownCalendarComponentTypeException
import org.ionproject.core.startsAndEndsWith
import org.ionproject.core.toHexString
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.postgresql.util.PGobject
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.time.OffsetDateTime

@Component
class CalendarComponentMapper(
    private val languageRepo: LanguageRepo,
    private val categoryRepo: CategoryRepo
) : RowMapper<CalendarComponent> {

    override fun map(rs: ResultSet, ctx: StatementContext): CalendarComponent {
        val type = rs.getString(CalendarData.TYPE)
        val uid = UniqueIdentifier(rs.getInt(CalendarData.UID).toHexString())
        val categories = rs.getCategories(CalendarData.CATEGORIES)
        val summary = rs.getSummaries(CalendarData.SUMMARIES)
        val description = rs.getDescriptions(CalendarData.DESCRIPTIONS)
        val dtStamp = DateTimeStamp(rs.getDatetime(CalendarData.DTSTAMP))
        val created = DateTimeCreated(rs.getDatetime(CalendarData.CREATED))

        return when (type) {
            "E" -> Event(
                uid,
                summary,
                description,
                dtStamp,
                created,
                categories,
                DateTimeStart(rs.getDatetime(CalendarData.DTSTART)),
                DateTimeEnd(rs.getDatetime(CalendarData.DTEND)),
                rs.getRecurrenceRule(CalendarData.BYDAY)
            )
            "J" -> Journal(
                uid,
                summary,
                description,
                rs.getAttachments(CalendarData.ATTACHMENTS),
                dtStamp,
                DateTimeStart(rs.getDatetime(CalendarData.DTSTART)),
                created,
                categories
            )
            "T" -> Todo(
                uid,
                summary,
                description,
                rs.getAttachments(CalendarData.ATTACHMENTS),
                dtStamp,
                created,
                DateTimeDue(rs.getDatetime(CalendarData.DUE)),
                categories
            )
            else -> throw UnknownCalendarComponentTypeException("The type represented by $type is not known.")
        }
    }

    private fun ResultSet.getSummaries(columnName: String): Array<Summary> {
        val summaries = getCompositeArray(columnName) {
            val summaryLanguage = it[0] as Int
            val summary = it[1] as String

            Summary(
                summary,
                language = languageRepo.byId(summaryLanguage)
            )
        }

        return summaries.toTypedArray()
    }

    private fun ResultSet.getDescriptions(columnName: String): Array<Description> {
        val descriptions = getCompositeArray(columnName) {
            val descriptionLanguage = it[0] as Int
            val description = it[1] as String

            Description(
                description,
                language = languageRepo.byId(descriptionLanguage)
            )
        }

        return descriptions.toTypedArray()
    }

    private fun ResultSet.getCategories(columnName: String): Array<Categories> {
        val tempCats = getArray(columnName).array as Array<Int>

        val cats = tempCats.map { it }

        return cats.map {
            categoryRepo.byId(it) ?: throw ForeignKeyException("category", "categories")
        }.groupBy {
            it.language // group categories of this event by language
        }.map { pair ->
            val categories = pair.value

            Categories(
                categories.map { it.value },
                language = pair.key
            )
        }.toTypedArray()
    }

    private fun ResultSet.getRecurrenceRule(columnName: String): RecurrenceRule? {
        val weekDays = getString(columnName)?.split(",")?.map { WeekDay(WeekDay.Weekday.valueOf(it), null) }

        return if (weekDays == null) null
        else RecurrenceRule(
            Recur(byDay = weekDays)
        )
    }

    private fun <R> ResultSet.getCompositeArray(columnName: String, oper: (List<Any>) -> R): List<R> {
        val array = getArray(columnName).array as Array<Any>

        return array.map {
            val pgObject = it as PGobject
            pgObject.apply {
                value = value.removeSurrounding("(", ")")
            }

            val values = pgObject.value.split(',')

            val list = List(values.size) {
                val str = values[it]
                if (str.startsAndEndsWith('"')) {
                    str.removeSurrounding("\"")
                } else {
                    str.toInt()
                }
            }

            oper(list)
        }
    }

    private fun ResultSet.getDatetime(columnName: String): DateTime {
        val offsetTime = getObject(columnName, OffsetDateTime::class.java)
        return DateTime(
            Date(
                offsetTime.year,
                offsetTime.monthValue,
                offsetTime.dayOfMonth
            ),
            Time(
                offsetTime.hour,
                offsetTime.minute,
                offsetTime.second // TODO("Add offset")
            )
        )
    }

    private fun ResultSet.getAttachments(columnName: String): Array<Attachment> {
        val att = getArray(columnName).array as Array<String?>

        val attachments = mutableListOf<Attachment>()

        att.forEach {
            if (it == null) return emptyArray()

            attachments.add(
                Attachment(Uri(it))
            )
        }

        return attachments.toTypedArray()
    }
}
