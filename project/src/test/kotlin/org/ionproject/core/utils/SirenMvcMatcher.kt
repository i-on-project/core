package org.ionproject.core.utils

import org.ionproject.core.common.*
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl

fun Siren.matchMvc(matcher: MockMvcResultMatchersDsl, root: String = "$") {
    // match each klass of the siren object to the received response
    klass?.forEachIndexed { i, k -> matcher.jsonPath("${root}.class[${i}]") { value(k) } }
    title?.let { matcher.jsonPath("${root}.title") { value(it) } }

    // embedded siren components' matchMvc tests
    entities?.forEachIndexed { i, e -> e.matchMvc(matcher, "${root}.entities[${i}]") }
    actions?.forEachIndexed { i, a -> a.matchMvc(matcher, "${root}.actions[${i}]") }
    links?.forEachIndexed { i, r -> r.matchMvc(matcher, "${root}.links[${i}]") }
}

fun EmbeddedRepresentation.matchMvc(matcher: MockMvcResultMatchersDsl, root: String = "$") {
    // match each klass of the siren object to the received response
    klass?.forEachIndexed { i, k -> matcher.jsonPath("${root}.class[${i}]") { value(k) } }
    rel.forEachIndexed { i, r -> matcher.jsonPath("${root}.rel[${i}]") { value(r) } }
    title?.let { matcher.jsonPath("${root}.title") { value(it) } }

    // embedded siren components' matchMvc tests
    entities?.forEachIndexed { i, e -> e.matchMvc(matcher, "${root}.entities[${i}]") }
    actions?.forEachIndexed { i, a -> a.matchMvc(matcher, "${root}.actions[${i}]") }
    links?.forEachIndexed { i, r -> r.matchMvc(matcher, "${root}.links[${i}]") }
}

fun Action.matchMvc(matcher: MockMvcResultMatchersDsl, root: String) {
    // essentials
    matcher.jsonPath("${root}.name") { value(name) }
    matcher.jsonPath("${root}.href") { value(href.toString()) }
    // optionals
    type?.let { matcher.jsonPath("${root}.type") { value(it) } }
    method?.let { matcher.jsonPath("${root}.method") { value(it.toString()) } }
    isTemplated?.let { matcher.jsonPath("${root}.isTemplated") { value(it) } }
    fields?.forEachIndexed { i, f -> f.matchMvc(matcher, "${root}.fields[${i}]") }
}

fun Field.matchMvc(matcher: MockMvcResultMatchersDsl, root: String) {
    matcher.jsonPath("${root}.name") { value(name) }
    type?.let { matcher.jsonPath("${root}.type") { value(it) } }
    title?.let { matcher.jsonPath("${root}.title") { value(it) } }
    klass?.let { matcher.jsonPath("${root}.class") { value(it) } }
}

fun Relation.matchMvc(matcher: MockMvcResultMatchersDsl, root: String) {
    rel.forEachIndexed { i, relName -> matcher.jsonPath("${root}.rel[${i}]") { value(relName) } }
    matcher.jsonPath("${root}.href") { value(href.toString()) }
    type?.let { matcher.jsonPath("${root}.type") { value(it) } }
    title?.let { matcher.jsonPath("${root}.title") { value(it) } }
    klass?.let { matcher.jsonPath("${root}.class") { value(it) } }
}