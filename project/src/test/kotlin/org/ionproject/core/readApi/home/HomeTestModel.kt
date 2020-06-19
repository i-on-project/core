package org.ionproject.core.readApi.home

fun Home(
    api: Map<String, Any>,
    resources: Map<String, Any>
) : Map<String, Any> = mapOf(
    "api" to api,
    "resources" to resources
)

fun Api (
    title: String,
    describedBy: String
) : Map<String, Any> =
    mapOf(
        "title" to title,
        "links" to mapOf(
            "describedBy" to describedBy
        )
    )

fun Resource (
    hrefTemplate: String,
    hrefVars: Map<String, String>,
    hints: Map<String, Any>
) : Map<String, Any> = mapOf(
    "hrefTemplate" to hrefTemplate,
    "hrefVars" to hrefVars,
    "hints" to hints
)

/**
 * Resource overload for resources
 * that don't have templating like programmes
 */
fun Resource (
        href: String,
        hints: Map<String, Any>
) : Map<String, Any> = mapOf(
        "href" to href,
        "hints" to hints
)

fun Hints(
    allow: List<String>,
    formats: Map<String, Any>,
    docs: String
) : Map<String, Any> = mapOf(
    "allow" to allow,
    "formats" to formats,
    "docs" to docs
)