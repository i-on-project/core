package org.ionproject.core.common

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

class Relation(
        val rel: String,
        val href: URI,
        val type: String? = null,
        val title: String? = null,
        @JsonProperty("class") val klass: List<String>? = null)

class Field(
        val name: String,
        val type: String? = null,
        val title: String? = null,
        @JsonProperty("class") val klass: String? = null)

class Action(
        val name: String,
        val href: URI,
        val title: String? = null,
        val method: HttpMethod? = null,
        val type: String? = null,
        val isTemplated: Boolean? = null,
        val fields: List<Field>? = null) {

    // Some actions that will be used constantly
    companion object {
        fun genAddItemAction(href: URI) = Action(
                name = "add-item",
                title = "Add Item",
                method = HttpMethod.POST,
                href = href,
                isTemplated = false,
                type = JSON_MEDIA_TYPE)

        fun genSearchAction(href: URI) = Action(
                name = "search",
                title = "Search items",
                method = HttpMethod.GET,
                href = href,
                isTemplated = true,
                type = JSON_MEDIA_TYPE,
                fields = listOf(
                        Field(name = "limit", type = "number", klass = "param/limit"),
                        Field(name = "page", type = "number", klass = "param/page")
                ))

        fun genDeleteAction(href: URI) = Action(
                name = "delete",
                href = href,
                method = HttpMethod.GET,
                type = "*/*",
                isTemplated = false)

        fun genEditAction(href: URI) = Action(
                name = "edit",
                href = href,
                method = HttpMethod.PATCH,
                type = JSON_MEDIA_TYPE,
                isTemplated = false)
    }
}

open class Siren(
        @JsonProperty("class") val klass: List<String>? = null,
        val properties: Any? = null,
        val entities: List<EmbeddedRepresentation>? = null,
        val title: String? = null,
        val actions: List<Action>? = null,
        val links: List<Relation>? = null)

/**
 * An embedded siren object, with a relationship to the parent siren object.
 */
class EmbeddedRepresentation(
        val rel: List<String>,
        @JsonProperty("class") klass: List<String>? = null,
        properties: Any? = null,
        entities: List<EmbeddedRepresentation>? = null,
        title: String? = null,
        actions: List<Action>? = null,
        links: List<Relation>? = null) : Siren(klass, properties, entities, title, actions, links)

class SirenBuilder(
        private var properties: Any = Unit,
        private var rel: MutableList<String> = mutableListOf(),
        private var title: String? = null,
        private var klass: MutableList<String>? = null,
        private var entities: MutableList<EmbeddedRepresentation>? = null,
        private var actions: MutableList<Action>? = null,
        private var links: MutableList<Relation>? = null) {

    fun klass(vararg klasses: String): SirenBuilder {
        if (klass == null) {
            klass = mutableListOf()
        }
        klass?.addAll(klasses)
        return this
    }

    fun rel(vararg rels: String): SirenBuilder {
        rel.addAll(rels)
        return this
    }

    fun entities(ents: Iterable<EmbeddedRepresentation>?): SirenBuilder {
        if (entities == null) {
            entities = mutableListOf()
        }

        if (ents != null) {
            entities?.addAll(ents)
        }
        return this
    }

    fun action(ac: Action): SirenBuilder {
        if (actions == null) {
            actions = mutableListOf()
        }
        actions?.add(ac)
        return this
    }

    fun title(content: String): SirenBuilder {
        title = content
        return this
    }

    fun link(r: String, href: URI): SirenBuilder {
        if (links == null) {
            links = mutableListOf()
        }
        links?.add(Relation(r, href))
        return this
    }

    fun toEmbed() = EmbeddedRepresentation(
            rel,
            klass,
            properties,
            entities,
            title,
            actions,
            links)

    fun toSiren() = Siren(
            klass,
            properties,
            entities,
            title,
            actions,
            links)
}