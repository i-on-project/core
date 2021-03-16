package org.ionproject.core.home

import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.util.UriTemplate
import java.net.URI

class JsonHomeBuilderException(message: String) : Exception(message)

enum class Status {
    deprecated, gone
}

// Immutables
class ApiObject(
    val title: String,
    val links: Map<String, URI>? = null
)

/**
 * This is the JsonHome object returned by the controller and then serialized to JSON format.
 * A JSON home object is divided into two: the API object and the Resources object.
 *
 * The Resources object includes further embedded objects, each describing a resource.
 */
class JsonHome(
    val api: ApiObject,
    val resources: Map<String, ResourceObject>? = null
)

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
    val docs: URI? = null
)

class ResourceObject(
    val href: URI? = null,
    val hrefTemplate: UriTemplate? = null,
    val hrefVars: MutableMap<String, URI>? = null,
    val authSchemes: List<AuthenticationScheme>? = null,
    val hints: Hints? = null
)

class AuthenticationScheme(
    val scheme: String,
    val realms: List<String>
)

// Mutables
/**
 * Provides a factory-like interfacing for adding the various fields of the JSON Home object.
 *
 * The terminal method [toJsonHome] will return an immutable [JsonHome] object which will
 * include all the configurations provided to the builder via functions.
 */
class JsonHomeBuilder(
    private val title: String,
    private var links: MutableMap<String, URI>? = null,
    private var resources: MutableMap<String, ResourceObject>? = null
) {

    fun link(name: String, href: URI): JsonHomeBuilder {
        if (links == null) {
            links = mutableMapOf()
        }
        links?.set(name, href)
        return this
    }

    fun newResource(name: String, build: (ResourceBuilder) -> ResourceObject): JsonHomeBuilder {
        val rb = ResourceBuilder(name)
        val resource = build(rb)
        putResource(name, resource)
        return this
    }

    fun putResource(name: String, resource: ResourceObject): JsonHomeBuilder {
        if (resources == null) {
            resources = mutableMapOf()
        }
        resources?.put(name, resource)
        return this
    }

    /**
     * Terminal function.
     * @return an immutable [JsonHome].
     */
    fun toJsonHome(): JsonHome = JsonHome(
        ApiObject(title, links), resources
    )
}

class ResourceBuilder(
    private val name: String,
    private var href: URI? = null,
    private var hrefTemplate: UriTemplate? = null,
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
    private var formats: MutableMap<MediaType, Unit>? = null
) {

    /**
     * Configure the resource object with a static URI.
     * You cannot call the [hrefTemplate] or [hrefVar] functions, from here on out.
     */
    fun href(href: URI): ResourceBuilder {
        if (hrefTemplate != null || hrefVars != null) {
            throw JsonHomeBuilderException("In JSON Home, you can specify href for static URIs, or hrefTemplate+hrefVars for URI Templates, but not both.")
        }
        this.href = href
        return this
    }

    /**
     * Configure the resource object with a templated URI.
     * You cannot call the [href] function, from here on out.
     */
    fun hrefTemplate(uri: UriTemplate): ResourceBuilder {
        if (href != null) {
            throw JsonHomeBuilderException("In JSON Home, you can specify href for static URIs, or hrefTemplate+hrefVars for URI Templates, but not both.")
        }
        this.hrefTemplate = uri
        return this
    }

    /**
     * Describe the different variables of the templated URI configured with [hrefTemplate].
     * You cannot call the [href] function, from here on out.
     */
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

    fun formats(vararg types: MediaType): ResourceBuilder {
        if (formats == null) {
            formats = mutableMapOf()
        }
        // From the JsonHome spec: "Content MUST be an object, whose keys are media types, and values are objects, currently empty."
        // Therefore, Unit is used
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

    /**
     * Terminal function.
     * @return an immutable [ResourceObject].
     */
    fun toResourceObject(): ResourceObject {
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
                docs
            )
        }
        return ResourceObject(href, hrefTemplate, hrefVars, authSchemes, hints)
    }
}
