# `Root`
The `root` resource is the entry point of the API. It allows for any client application to navigate through the different resources with no out-of-band knowledge of the API.

The `root` resource is represented in the [application/json-home](https://mnot.github.io/I-D/json-home/) media type.

## API object
Contains information about the API.

* `title`: the name of the API
    - e.g. "i-on Core"

## Resources
An object containing some resources, aswell as some of the available operations for said resources.

Available resources:
* `Courses` 

## Example
```javascript
{
    "api": {
        "title": "i-on Core",
        "links": {
            "author": "mailto:core@ion.pt",
            "describedBy": "/api-docs/"
        }
    },
    "resources": {
        "courses": {
            "href": "/v0/courses{?limit,page}",
            "hrefVars": {
                "limit": "/api-docs/params/limit",
                "page": "/api-docs/params/page"
            },
            "hints": {
                "allow": ["GET", "POST", "PATCH"]  
            }
        },
        "terms": {
            "href": "/v0/terms{?limit,page}",
            "hrefVars": {
                "limit": "/api-docs/params/limit",
                "page": "/api-docs/params/page"
            },
            "hints": {
                "allow": ["GET"]  
            }
        }
    }
}
```