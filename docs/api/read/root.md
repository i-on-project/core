# `Root`
The `root` resource is the entry point of the API. It allows for any client application to navigate through different resources with no out-of-band knowledge of the API.

The `root` resource is represented in the [application/json-home](https://mnot.github.io/I-D/json-home/) media type.

The paths to the different resources will start with a version number (e.g. `/v0/courses/`). This prevents breaking compatibility with clients using an older version of the API, allowing them to gradually adapt to the newer version.

## API object
Contains information about the API.

* `title`: the name of the API
    - e.g. "i-on Core"

* `describedBy`: *link* to the API documentation
    - e.g. "https://github.com/i-on-project/core/tree/feature/gh-16-basic-impl/docs/api"

## Resources
This property of the JSON Home object has members of link relation type.
These describe in great detail the possible resources the client may want to access initially.

Important considerations for clients:
* the location of the resource is provided by the `hrefTemplate` property when a URI Template is used (this means the resource can be accessed via a range of URIs) or an `href` property when there is only one possible URI. Both properties will not be included simultaneously, but at least one will. The `hrefTemplate` property will be followed by an `hrefVars` property describing the parameters of the URI Template.

Available "entry point" resources:
* `courses`
* `calendar-terms`
* `search`
* `programmes`
* `revokeToken`

## Example
```json
{
    "api": {
        "title": "i-on Core",
        "links": {
            "describedBy": "https://github.com/i-on-project/core/tree/master/docs/api"
        }
    },
    "resources": {
        "courses": {
            "hrefTemplate": "/v0/courses{?page,limit}",
            "hrefVars": {
                "limit": "/api-docs/params/limit",
                "page": "/api-docs/params/page"
            },
            "hints": {
                "allow": [
                    "GET"
                ],
                "formats": {
                    "application/vnd.siren+json": {}
                },
                "docs": "https://github.com/i-on-project/core/tree/master/docs/api/courses.md"
            }
        },
        "calendar-terms": {
            "hrefTemplate": "/v0/calendar-terms{?page,limit}",
            "hrefVars": {
                "limit": "/api-docs/params/limit",
                "page": "/api-docs/params/page"
            },
            "hints": {
                "allow": [
                    "GET"
                ],
                "formats": {
                    "application/vnd.siren+json": {}
                },
                "docs": "https://github.com/i-on-project/core/tree/master/docs/api/calendar-terms.md"
            }
        },
        "search": {
            "hrefTemplate": "/v0/search{?query,types,limit,page}",
            "hrefVars": {
                "query": "/api-docs/params/query",
                "types": "/api-docs/params/types",
                "limit": "/api-docs/params/limit",
                "page": "/api-docs/params/page"
            },
            "hints": {
                "allow": [
                    "GET"
                ],
                "formats": {
                    "application/vnd.siren+json": {}
                },
                "docs": "https://github.com/i-on-project/core/tree/master/docs/api/search.md"
            }
        },
        "programmes": {
            "hrefTemplate": "/v0/programmes{?page,limit}",
            "hrefVars": {
                "limit": "/api-docs/params/limit",
                "page": "/api-docs/params/page"
            },
            "hints": {
                "allow": [
                    "GET"
                ],
                "formats": {
                    "application/vnd.siren+json": {}
                },
                "docs": "https://github.com/i-on-project/core/tree/master/docs/api/programme.md"
            }
        },
        "revokeToken": {
            "href": "/revokeToken",
            "hints": {
                "allow": [
                    "POST"
                ],
                "formats": {
                    "application/x-www-form-urlencoded": {}
                },
                "docs": "https://github.com/i-on-project/core/tree/master/docs/access_control/Http_Exchanges.md"
            }
        }
    }
}
```
