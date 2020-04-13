package org.ionproject.core.common

/*
 * Path/URI's Construction
 */

const val VERSION = "v0"
const val COURSE_ENTITY = "courses"
const val CLASS_ENTITY = "classes"
const val PROGRAMME_ENTITY = "programmes"
const val PROGRAMME_OFFER_ENTITY = "offers"

const val ACR = "{acr}"
const val ID = "{id}"

/*
 * Courses URI's
 */
const val COURSES_PATH = "/${VERSION}/${COURSE_ENTITY}"
const val COURSES_PATH_ACR = "${COURSES_PATH}/${ACR}"

/*
 * Programmes URI's
 */
const val PROGRAMMES_PATH = "/${VERSION}/${PROGRAMME_ENTITY}"
const val PROGRAMMES_PATH_ACR = "${PROGRAMMES_PATH}/${ACR}"
const val PROGRAMMES_PATH_ACR_OFFER = "${PROGRAMMES_PATH_ACR}/${PROGRAMME_OFFER_ENTITY}"
const val PROGRAMMES_PATH_ACR_OFFER_ID = "${PROGRAMMES_PATH_ACR}/${PROGRAMME_OFFER_ENTITY}/${ID}"

/*
 * REL's URI's
 */
const val REL = "rel"

const val REL_CLASS = "/${REL}/class"
const val REL_PROGRAMME_OFFER = "/${REL}/programmeOffer"
const val REL_COURSE = "/${REL}/course"
