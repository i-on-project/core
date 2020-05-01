package org.ionproject.core.calendar.category

import org.ionproject.core.calendar.icalendar.properties.parameters.Language
import org.ionproject.core.calendar.language.LanguageRepo
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.springframework.stereotype.Component
import java.sql.ResultSet

object CategoryData {
    private const val CATEGORY_TABLE = "dbo.Category"
    private const val CATEGORY_ABBR = "cat"
    private const val CATEGORY = "$CATEGORY_TABLE $CATEGORY_ABBR"

    private const val ID_COLUMN = "id"
    private const val NAME_COLUMN = "name"
    private const val LANGUAGE_COLUMN = "language"

    private const val ID = "$CATEGORY_ABBR.$ID_COLUMN"
    private const val NAME = "$CATEGORY_ABBR.$NAME_COLUMN"
    private const val LANGUAGE = "$CATEGORY_ABBR.$LANGUAGE_COLUMN"

    private const val SELECT = """
        SELECT
            $ID,
            $NAME,
            $LANGUAGE
    """

    const val ALL_CATEGORIES_QUERY = """
        $SELECT
        FROM $CATEGORY
    """

    @Component
    class CategoryMapper(
        private val languageRepo: LanguageRepo
    ) : RowMapper<Pair<Int, Pair<Language, String>>> {
        override fun map(rs: ResultSet, ctx: StatementContext): Pair<Int, Pair<Language, String>> =
            rs.getInt(ID_COLUMN) to (
                    (languageRepo.byId(rs.getInt(LANGUAGE_COLUMN)) ?: TODO("FOREIGN KEY EXCEPTION")) to
                            rs.getString(NAME_COLUMN)
            )
    }
}