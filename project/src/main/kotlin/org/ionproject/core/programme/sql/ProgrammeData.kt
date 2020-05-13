package org.ionproject.core.programme.sql

internal object ProgrammeData {
  const val SCHEMA = "dbo"
  const val PROGRAMME = "programme"
  const val PROGRAMME_OFFER = "programmeOffer"

  const val COURSE = "course"
  const val COURSE_ID = "courseid"
  const val COURSE_ACR = "courseAcr"
  const val ID = "id"
  const val PROGRAMME_ID = "programmeid"
  const val ACRONYM = "acronym"
  const val NAME = "name"
  const val TERM_SIZE = "termsize"
  const val TERM_NUMBER = "termnumber"
  const val OPTIONAL = "optional"

  const val GET_PROGRAMMES_QUERY = """
    SELECT * FROM $SCHEMA.$PROGRAMME
  """

  const val GET_PROGRAMME_BY_ID_QUERY = """
    select $ID, $ACRONYM, $NAME, $TERM_SIZE FROM $SCHEMA.$PROGRAMME where $ID=:$ID
  """

  const val GET_PROGRAMME_OFFERS_QUERY = """
    select po.*,co.$ACRONYM AS $COURSE_ACR from $SCHEMA.$PROGRAMME_OFFER AS po inner join $SCHEMA.$COURSE as co
    on po.$COURSE_ID=co.$ID
    where $PROGRAMME_ID=:$ID 
    order by po.$ID
  """

  const val GET_OFFER_DETAILS_BY_ID = """
    select po.$ID as $ID, $ACRONYM as courseAcr, $PROGRAMME_ID, $COURSE_ID, $TERM_NUMBER, $OPTIONAL 
    from $SCHEMA.$PROGRAMME_OFFER po join $SCHEMA.course c on po.$COURSE_ID=c.$ID 
    where po.$ID=:$ID and $PROGRAMME_ID=:$PROGRAMME_ID
  """
}