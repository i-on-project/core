
# How to obtain an import URL for a calendar?

The action of obtaining an import URL is discovered through the siren actions of the calendar resource.
To be able to obtain an import URL you also need to present, like always, a valid token with the correct scope, read in this case.

E.g. actions of the calendar associated with the course 5 of the class 1920v and class Section LI61D

```
"actions": [
    ...
    {
      "name": "import",
      "href": "/v0/import/5/1920v/LI61D/calendar{?type,startBefore,startAfter,endBefore,endAfter,summary}",
      "title": "Import calendar",
      "method": "GET",
      "isTemplated": true,
      "fields": [
        {
          "name": "type",
          "type": "text",
          "class": "https://example.org/param/free-text-query"
        },
        {
          "name": "startBefore",
          "type": "date",
          "class": "https://example.org/param/date-query"
        },
        {
          "name": "startAfter",
          "type": "date",
          "class": "https://example.org/param/date-query"
        },
        {
          "name": "endBefore",
          "type": "date",
          "class": "https://example.org/param/date-query"
        },
        {
          "name": "endAfter",
          "type": "date",
          "class": "https://example.org/param/date-query"
        },
        {
          "name": "summary",
          "type": "text",
          "class": "https://example.org/param/free-text-query"
        }
      ]
    },
    ...
  ]
```

or the following way for the calendar associated with the course of the class 1920v:

```
"actions": [
    ...
    {
      "name": "import",
      "href": "/v0/import/5/1920v/calendar{?type,startBefore,startAfter,endBefore,endAfter,summary}",
      "title": "Import calendar",
      "method": "GET",
      "isTemplated": true
    },
    "fields": [
        {
          "name": "type",
          "type": "text",
          "class": "https://example.org/param/free-text-query"
        },
        {
          "name": "startBefore",
          "type": "date",
          "class": "https://example.org/param/date-query"
        },
        {
          "name": "startAfter",
          "type": "date",
          "class": "https://example.org/param/date-query"
        },
        {
          "name": "endBefore",
          "type": "date",
          "class": "https://example.org/param/date-query"
        },
        {
          "name": "endAfter",
          "type": "date",
          "class": "https://example.org/param/date-query"
        },
        {
          "name": "summary",
          "type": "text",
          "class": "https://example.org/param/free-text-query"
        }
      ]
    ...
  ]
```


In case the user is trying to obtain an invalid calendar (Bad Request or Not Found) he will be informed of it, instead of producing an import link that points to an error. 

When an URL import link is requested, if the resource is valid the server will answer with something like:

{
  "url": "https://host1.dev.ionproject.org/v0/courses/5/classes/1920v/calendar?type=todo&access_token=mF6_7Ab..."
}

The above URL contains the necessary authentication in the access_token query parameters, when requested will return the expected resource to be found at that location.