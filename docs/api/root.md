# `Root`
The `root` resource is the entry point of the API. It allows for any client application to navigate through different resources with no out-of-band knowledge of the API.

The `root` resource is represented in the [application/json-home](https://mnot.github.io/I-D/json-home/) media type.

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

## Example
```javascript
{
    "api": {
        "title": "i-on Core",
        "links": {
            "author": "mailto:core@ion.pt",
            "describedBy": "https://github.com/i-on-project/core/tree/feature/gh-16-basic-impl/docs/api"
        }
    },
    "resources": {
        "courses": {
            "hrefTemplate": "/v0/courses?limit,page",
            "hrefVars": {
                "limit": "/api-docs/params/limit",
                "page": "/api-docs/params/page"
            },
            "hints": {
                "allow": ["GET", "POST", "PATCH"]  
            }
        },
        "terms": {
            "hrefTemplate": "/v0/calendar-terms?limit,page",
            "hrefVars": {
                "limit": "/api-docs/params/limit",
                "page": "/api-docs/params/page"
            },
            "hints": {
                "allow": ["GET"],
				"docs": "https://github.com/i-on-project/core/tree/feature/gh-16-basic-impl/docs/api/courses.md"
            }
        }
    }
}
```
