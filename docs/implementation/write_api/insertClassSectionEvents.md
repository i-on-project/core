This document describes the implementation decisions for the Write API's `insertEvents` operation.

# HTTP Requests
* RPC based requests
  - the message format and contents undergo a number of server-side verifications
  - the client need not navigate through the read API to find the needed operations

* `PUT /v0/write/insertClassSectionEvents`
  - analogous to `function insertEvents(message: JSON): JSON`
  - the message format is described in [this document](../../api/write/insertEvents.md)

* Idempotent
  - the contents of this request update the resources if they already exist
  - subsequent delivery of events for the same resources will give the same results

* Stable
  - if the operation fails, the state of the repository remains the same as before the request was made

* Transaction-less
  - this request does not depend on previous/subsequent requests
  - note that database interactions in the context of one request will be done inside a transaction, but long-term transactions which span more than one HTTP request will not exist (e.g. sagas)
  - each request is independent

## Request handling
When a request arrives at the server's appropriate controller, the following steps are executed:
* access control (provided by the Access Manager sub-system)
* content-type validation (reject if it is anything other than JSON)
* JSON validation against the statically defined schema
  - validates the overall structure of the object (participation of properties)
  - validates the value's types
  - *may* check the contents of values (e.g. number of characters of a string, Regex pattern matching a string, range of integers, etc)
* database transaction
  - attempt to insert or replace the specified `School`, `Programme` and `Calendar Term`
  - attempt to insert or replace `Class`, `Class Sections` and `Courses`
  - insert or replace `Events`
* send HTTP response
  - invalid JSON objects will be rejected early into the controller, resulting in a 400 response with all the failing constraints (e.g. missing properties, out of range values)
  - some constraints might not be 

# PHY Model Entities vs Write API Resources
The relationships of the database entities may not directly correspond to the semantics of the Write API operations.
In example, the request JSON object will contain collections of events for course objects even though only classes and class sections have events, not courses.
This format is simpler to use and the Core will infer what Class and Class Sections to apply the events on  in the context of the different courses (by making use of the properties placed in the root of the JSON object i.e. `calendarSection` and `calendarTerm`).

# Database interactions
Before inserting `Events` on to the database, we must first guarantee that the target `School`, `Programme`, `Class`, `Class Section`, `Calendar Term` and `Courses` are created or else we would be violating foreign key constraints.

The following steps will be implemented in separated stored procedures, which will be called from the controller:
* The `School`, `Programme` and `Calendar Term` entities may be created/updated in the beginning of the transaction once (to prevent repeated insertions when iterating through the events)

* While iterating through the collection of `courses`, the following steps are to be achieved:
  - inserting/updating the `Class`, `Class Section` and `Course`
  - iterate through the `events` collection and insert/update each and every `Event`

* If some constraint fails, the whole transaction aborts (rolling back any changes)
  - otherwise, commit all changes

# Performance

## Concurrency
At this point, every transaction is in the serializable level of isolation since no deep analysis was made yet in regards to this topic.

Further, all stored procedures are executed sequentially while looping through the items in order (e.g. `for each course { proc(); for each event { proc(); } }`)

