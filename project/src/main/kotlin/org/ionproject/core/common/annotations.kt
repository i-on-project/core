package org.ionproject.core.common

import org.springframework.beans.factory.annotation.Qualifier

/*
 * Annotation to be used on
 * endpoints that require authentication
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class RequiresAuthentication

/*
 * Annotation used to indicate if
 * its context its mockMode or not.
 *
 * Use App_mode = true when desired
 * to use mock data instead of the real
 * repo's.
 */
const val APP_MODE = true

@Target(AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class MockMode(val value : Boolean)