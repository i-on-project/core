package org.ionproject.core.common.transaction

import org.postgresql.util.PSQLException

/**
 * Errors retrieved from the table supplied at https://www.postgresql.org/docs/current/errcodes-appendix.html
 * Errors are of type [String] because [PSQLException.SQLState] also is.
 */
object SQLErrors {
    const val SYNTAX_ERROR = "42601"
}