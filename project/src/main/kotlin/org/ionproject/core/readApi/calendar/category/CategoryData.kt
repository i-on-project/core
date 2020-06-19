package org.ionproject.core.readApi.calendar.category

import org.ionproject.core.readApi.calendar.language.LanguageRepo
import org.ionproject.core.readApi.common.customExceptions.ForeignKeyException
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