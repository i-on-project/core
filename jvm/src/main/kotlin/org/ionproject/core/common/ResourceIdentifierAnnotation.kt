package org.ionproject.core.common

/**
 * The previous mechanism of defining policies was very flawed.
 *
 * 1º If the URI changed the policy would have to be altered manually
 *
 * 2º Had security vulnerabilities if a distracted developer created an method that fell in the
 * range of another method with parameters.
 * e.g. "/v0/courses/5 <-- path parameter" & "/v0/courses/import" <-- no path parameters
 *
 * If a scope had defined that read can access to the first case, the same policy will allow the token to access
 * the 2nd case too, which is NOT CORRECT
 *
 * Using the name of the controller would also be a bad ideia because it can change very frequently.
 *
 *
 *
 * !!!!IMPORTANT!!! !!!IMPORTANT!!!!!!!IMPORTANT!!!!!!!IMPORTANT!!!!!!!IMPORTANT!!!!!!!IMPORTANT!!!!!!!IMPORTANT!!!
 * Avoid changing the value in the annotation, but if you do it change the value in the sql policies too, or the
 * resource will become impossible to access.
 *
 * Also check in the sql
 */

@Target(AnnotationTarget.FUNCTION)
annotation class ResourceIdentifierAnnotation(val resourceName: String, val version: String)
