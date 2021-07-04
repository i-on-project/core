package org.ionproject.core.calendar.language

import org.ionproject.core.calendar.icalendar.properties.parameters.Language
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.springframework.stereotype.Component
import java.sql.ResultSet

object LanguageData {
    private const val LANGUAGE_TABLE = "dbo.Language"
    private const val LANGUAGE_ABBR = "lang"
    private const val LANGUAGE = "$LANGUAGE_TABLE $LANGUAGE_ABBR"

    private const val ID_COLUMN = "id"
    private const val NAME_COLUMN = "name"

    private const val ID = "$LANGUAGE_ABBR.$ID_COLUMN"
    private const val NAME = "$LANGUAGE_ABBR.$NAME_COLUMN"

    private const val SELECT = """
        SELECT $ID, $NAME
    """

    const val ALL_LANGUAGES_QUERY = """
        $SELECT
        FROM $LANGUAGE
    """

    @Component
    class LanguageMapper : RowMapper<Pair<Int, Language>> {
        override fun map(rs: ResultSet, ctx: StatementContext): Pair<Int, Language> =
            rs.getInt(ID_COLUMN) to Language(rs.getString(NAME_COLUMN))
    }
}
