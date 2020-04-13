package org.ionproject.core.common

import org.springframework.beans.factory.annotation.Qualifier

/*
 * Annotation to be used on
 * endpoints that require authentication
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class RequiresAuthentication

