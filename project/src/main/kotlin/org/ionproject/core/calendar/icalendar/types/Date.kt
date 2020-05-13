package org.ionproject.core.calendar.icalendar.types

import org.ionproject.core.toInt

class Date private constructor(
  private val year: Year,
  private val month: Month,
  private val day: MonthDay
) : ICalendarDataType {
  companion object {
    private const val name: String = "DATE"

    fun of(year: Int, month: Int, day: Int): Date =
      Date(
        Year(year),
        Month(month),
        MonthDay(day)
      )

    operator fun invoke(year: Int, month: Int, day: Int): Date = of(year, month, day)

    fun parse(dateComp: CharSequence): Date =
      Date(
        dateComp.subSequence(0, 4).toInt(),
        dateComp.subSequence(4, 6).toInt(),
        dateComp.subSequence(6, 8).toInt()
      )

  }

  override val value: Any
    get() = toString()

  override val name: String
    get() = Companion.name

  override fun toString(): String {
    return "$year$month$day"
  }

  private class Year(
    val value: Int
  ) {
    init {
      if (value < 0 || value > 9999) throw IllegalArgumentException("A year must be between 0 and 9999.")
    }

    override fun toString(): String {
      return String.format("%04d", value)
    }
  }

  private class Month(
    private val value: Int
  ) {
    init {
      if (value < 0 || value > 12) throw IllegalArgumentException("Months must be between 0 and 12.")
    }

    override fun toString(): String {
      return String.format("%02d", value)
    }
  }

  private class MonthDay(
    private val value: Int
  ) {
    init {
      if (value < 0 || value > 31) throw IllegalArgumentException("Month days must be between 0 and 31.")
    }

    override fun toString(): String {
      return String.format("%02d", value)
    }
  }
}
