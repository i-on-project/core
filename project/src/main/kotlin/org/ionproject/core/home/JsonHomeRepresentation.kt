package org.ionproject.core.home

import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

class JsonHomeBuilderException(message: String) : Exception(message)

enum class Status {
    deprecated, gone
}

// Immutables
class ApiObject(
    val title: String,
    val links: Map<String, URI>? = null)

class JsonHome(
    val api: ApiObject,
    val resources: Map<String, ResourceObject>? = null)

class Hints(
    val allow: List<HttpMethod>? = null,
    val formats: Map<MediaType, Unit>? = null,
    val status: Status? = null,
    val preconditionRequired: List<String>? = null,
    val acceptPost: List<MediaType>? = null,
    val acceptPatch: List<MediaType>? = null,
    val acceptPut: List<MediaType>? = null,
    val acceptRanges: List<String>? = null,
    val acceptPrefer: List<String>? = null,
    val docs: URI? = null)

class ResourceObject(
    val href: URI? = null,
    val hrefTemplate: String? = null,
    val hrefVars: MutableMap<String, URI>? = null,
    val authSchemes: List<AuthenticationScheme>? = null,
    val hints: Hints? = null)

class AuthenticationScheme(
    val scheme: String,
    val realms: List<String>)

// Mutables
class ResourceBuilder(
    private val parent: JsonHomeBuilder,
    private val name: String,
    private var href: URI? = null,
    private var hrefTemplate: String? = null,
    private var hrefVars: MutableMap<String, URI>? = null,
    private var status: Status? = null,
    private var preconditionRequired: MutableList<String>? = null,
    private var acceptPost: MutableList<MediaType>? = null,
    private var acceptPatch: MutableList<MediaType>? = null,
    private var acceptPut: MutableList<MediaType>? = null,
    private var acceptRanges: MutableList<String>? = null,
    private var acceptPrefer: MutableList<String>? = null,
    private var docs: URI? = null,
    private var allow: MutableList<HttpMethod>? = null,
    private var authSchemes: MutableList<AuthenticationScheme>? = null,
    private var formats: MutableMap<MediaType, Unit>? = null) {

    fun href(href: URI): ResourceBuilder {
        if (hrefTemplate != null || hrefVars != null) {
            throw JsonHomeBuilderException("In JSON Home, you can specify href for static URIs, or hrefTemplate+hrefVars for URI Templates, but not both.")
        }
        this.href = href
        return this
    }

    fun hrefTemplate(uri: String): ResourceBuilder {
        if (href != null) {
            throw JsonHomeBuilderException("In JSON Home, you can specify href for static URIs, or hrefTemplate+hrefVars for URI Templates, but not both.")
        }
        this.hrefTemplate = uri
        return this
    }

    fun hrefVar(varName: String, spec: URI): ResourceBuilder {
        if (href != null) {
            throw JsonHomeBuilderException("In JSON Home, you can specify href for static URIs, or hrefTemplate+hrefVars for URI Templates, but not both.")
        }
        if (hrefVars == null) {
            hrefVars = mutableMapOf()
        }
        this.hrefVars?.put(varName, spec)
        return this
    }

    fun allow(vararg methods: HttpMethod): ResourceBuilder {
        if (allow == null) {
            allow = mutableListOf()
        }
        allow?.addAll(methods)
        return this
    }

    fun authScheme(scheme: String, vararg realms: String): ResourceBuilder {
        if (authSchemes == null) {
            authSchemes = mutableListOf()
        }
        authSchemes?.add(AuthenticationScheme(scheme, realms.toList()))
        return this
    }

    fun preconditions(vararg conds: String): ResourceBuilder {
        if (preconditionRequired == null) {
            preconditionRequired = mutableListOf()
        }
        preconditionRequired?.addAll(conds)
        return this
    }

    private fun acceptMethod(method: HttpMethod) {
        if (allow?.contains(method) != true) allow(method)
    }

    fun accept(method: HttpMethod, vararg types: MediaType): ResourceBuilder {
        when (method) {
            HttpMethod.POST -> {
                acceptMethod(method)
                if (acceptPost == null) {
                    acceptPost = mutableListOf()
                }
                acceptPost?.addAll(types)
            }
            HttpMethod.PUT -> {
                acceptMethod(method)
                if (acceptPut == null) {
                    acceptPut = mutableListOf()
                }
                acceptPut?.addAll(types)
            }
            HttpMethod.PATCH -> {
                acceptMethod(method)
                if (acceptPatch == null) {
                    acceptPatch = mutableListOf()
                }
                acceptPatch?.addAll(types)
            }
            else -> throw JsonHomeBuilderException("$method method is not supported for JSON home.")
        }
        return this
    }

    /**
     * From the JsonHome spec: "Content MUST be an object, whose keys are media types, and values are objects, currently empty."
     */
    fun formats(vararg types: MediaType): ResourceBuilder {
        if (formats == null) {
            formats = mutableMapOf()
        }
        types.forEach { formats?.put(it, Unit) }
        return this
    }

    fun status(status: Status): ResourceBuilder {
        this.status = status
        return this
    }

    fun docs(href: URI): ResourceBuilder {
        docs = href
        return this
    }

    fun toResourceObject(): JsonHomeBuilder {
        if (hrefTemplate != null && hrefVars == null) {
            throw JsonHomeBuilderException("When using hrefTemplate, you must then specify its variables.")
        }

        var hints: Hints? = null
        if (allow != null || formats != null) {
            hints = Hints(
                allow,
                formats,
                status,
                preconditionRequired,
                acceptPost,
                acceptPatch,
                acceptPut,
                acceptRanges,
                acceptPrefer,
                docs)
        }
        return parent.putResource(name, ResourceObject(href, hrefTemplate, hrefVars, authSchemes, hints))
    }
}

class JsonHomeBuilder(
    private val title: String,
    private var links: MutableMap<String, URI>? = null,
    private var resources: MutableMap<String, ResourceObject>? = null) {

    fun link(name: String, href: URI): JsonHomeBuilder {
        if (links == null) {
            links = mutableMapOf()
        }
        links?.set(name, href)
        return this
    }

    fun newResource(name: String): ResourceBuilder =
        ResourceBuilder(this, name)

    fun putResource(name: String, resource: ResourceObject): JsonHomeBuilder {
        if (resources == null) {
            resources = mutableMapOf()
        }
        resources?.put(name, resource)
        return this
    }

    fun toJsonHome(): JsonHome = JsonHome(
        ApiObject(title, links),
        resources)
}
