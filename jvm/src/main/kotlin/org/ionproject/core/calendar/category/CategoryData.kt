package org.ionproject.core.calendar.category

import org.ionproject.core.calendar.language.LanguageRepo
import org.ionproject.core.common.customExceptions.ForeignKeyException
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.springframework.stereotype.Component
import java.sql.ResultSet

object CategoryData {
    private const val CATEGORY_TABLE = "dbo.CategoryLanguage"
    private const val CATEGORY_ABBR = "cat"
    private const val CATEGORY = "$CATEGORY_TABLE $CATEGORY_ABBR"

    private const val ID_COLUMN = "category"
    private const val NAME_COLUMN = "name"
    private const val LANGUAGE_COLUMN = "language"

    private const val ID = "$CATEGORY_ABBR.$ID_COLUMN"
    private const val NAME = "$CATEGORY_ABBR.$NAME_COLUMN"
    private const val LANGUAGE = "$CATEGORY_ABBR.$LANGUAGE_COLUMN"

    const val ALL_CATEGORIES_QUERY = """
        SELECT
            $ID,
            $NAME,
            $LANGUAGE
        FROM $CATEGORY
        ORDER BY
            $ID ASC, $LANGUAGE ASC
    """

    @Component
    class CategoryMapper(
        private val languageRepo: LanguageRepo
    ) : RowMapper<Pair<Int, Category>> {
        override fun map(rs: ResultSet, ctx: StatementContext): Pair<Int, Category> =
            rs.getInt(ID_COLUMN) to (
                Category(
                    rs.getString(NAME_COLUMN),
                    languageRepo.byId(rs.getInt(LANGUAGE_COLUMN)) ?: throw ForeignKeyException("category", "language")
                )
                )
    }
}
