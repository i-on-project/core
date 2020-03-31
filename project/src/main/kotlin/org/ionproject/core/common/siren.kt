package org.ionproject.core.common

import com.fasterxml.jackson.annotation.JsonProperty

class Relation(
    val rel: String,
    val href: String)

class Action(
    val name: String,
    val method: String)

class Embed(
    @JsonProperty("class") val klass: List<String>,
    val rel: List<String>,
    val properties: Any,
    val links: List<Relation>)

class Siren(
    @JsonProperty("class") val klass: List<String>,
    val properties: Any,
    val entities: List<Embed>,
    val actions: List<Action>,
    val links: List<Relation>)

class SirenBuilder(
    private val properties: Any,
    private val klass: MutableList<String> = mutableListOf(),
    private val rel: MutableList<String> = mutableListOf(),
    private val entities: MutableList<Embed> = mutableListOf(),
    private val actions: MutableList<Action> = mutableListOf(),
    private val links: MutableList<Relation> = mutableListOf()) {

    fun klass(vararg klasses: String): SirenBuilder {
        klass.addAll(klasses)
        return this
    }

    fun rel(vararg rels: String): SirenBuilder {
        rel.addAll(rels)
        return this
    }

    fun entities(vararg ents: Embed): SirenBuilder {
        entities.addAll(ents)
        return this
    }

    fun action(vararg acts: Action): SirenBuilder {
        actions.addAll(acts)
        return this
    }

    fun link(r: String, href: String): SirenBuilder {
        links.add(
            Relation(r, href))
        return this
    }

    fun toSiren() = Siren(
        klass,
        properties,
        entities,
        actions,
        links)

    fun toEmbed() = Embed(
        klass,
        rel,
        properties,
        links)
}
