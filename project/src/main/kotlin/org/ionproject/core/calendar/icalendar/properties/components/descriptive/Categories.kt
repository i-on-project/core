package org.ionproject.core.calendar.icalendar.properties.components.descriptive

import org.ionproject.core.calendar.icalendar.properties.MultiValuedProperty
import org.ionproject.core.calendar.icalendar.properties.ParameterizedProperty
import org.ionproject.core.calendar.icalendar.properties.parameters.Language
import org.ionproject.core.calendar.icalendar.properties.parameters.PropertyParameter
import org.ionproject.core.calendar.icalendar.types.MultiValue
import org.ionproject.core.calendar.icalendar.types.Text
import org.ionproject.core.calendar.toText

class Categories(
  vararg categories: String,
  val language: Language? = null
) : ParameterizedProperty, MultiValuedProperty<Text> {

  constructor(categories: List<String>, language: Language?) : this(*categories.toTypedArray(), language = language)

  override val parameters: List<PropertyParameter>
    get() = listOfNotNull(language)

  override val name: String
    get() = "CATEGORIES"

  override val value: MultiValue<Text> = MultiValue(*categories.toText())
}

