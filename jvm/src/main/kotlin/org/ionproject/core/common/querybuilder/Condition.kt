package org.ionproject.core.common.querybuilder

import java.time.OffsetDateTime

abstract class Condition(
    val column: String,
    val comparisonOperator: ComparisonOperator,
    val argName: String,
    val aggregationKind: AggregationOperator
) {
    fun build(count: Int): String {
        val conditions = Array(count) {
            String.format("$column ${comparisonOperator.symbol} ${formatVariable()}", "$argName$it")
        }

        return "(${conditions.joinToString(" $aggregationKind ")})"
    }

    open fun formatVariable(): String = ":%s"

    abstract fun parse(s: String): Any
}

class TimestampCondition(
    column: String,
    comparisonOperator: ComparisonOperator,
    arg: String,
    aggregationKind: AggregationOperator = AggregationOperator.AND
) : Condition(column, comparisonOperator, arg, aggregationKind) {

    override fun parse(s: String): OffsetDateTime =
        OffsetDateTime.parse(s)
}

class VarcharCondition(
    column: String,
    comparisonOperator: ComparisonOperator,
    arg: String,
    aggregationKind: AggregationOperator = AggregationOperator.OR
) : Condition(column, comparisonOperator, arg, aggregationKind) {
    override fun parse(s: String): Any = s
}

enum class AggregationOperator {
    AND,
    OR
}

enum class ComparisonOperator(val symbol: String) {
    GREATER_THAN(">"),
    LESSER_THAN("<"),
    GREATER_THAN_OR_EQUAL(">="),
    LESSER_THAN_OR_EQUAL("<="),
    EQUALS("="),
    NOT_EQUAL("!="),
    LIKE("like")
}
