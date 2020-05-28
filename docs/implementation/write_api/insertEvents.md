This document describes the implementation decisions for the Write API's `insertEvents` operation.

# HTTP Requests
* RPC based requests
  - the message format and contents undergo a number of server-side verifications
  - the client need not navigate through the read API to find the needed operations

* `PUT /v0/rpc/insertEvents`
  - analogous to `function insertEvents(message: JSON): JSON`
  - the message format is described in [this document](../../api/write/insertEvents.md)

* Idempotent
  - the contents of this request update the resources if they already exist
  - subsequent delivery of events for the same resources will give the same results

* Stable
  - if the operation fails, the state of the repository remains the same as before the request was made

* Transaction-less
  - this request does not depend on previous/subsequent requests

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
  - attempt to insert or replace `Class`, `Class Sections` (if any) and `Courses`
  - insert or replace `Events`
* send HTTP response
  - Useful information to include?

# PHY Model Entities vs Write API Resources
The relationships of the database entities may not directly correspond to the semantics of the Write API operations.
In this case, the Write API's `Class` resource might not have the same constraints and relations as a `Class` entity in the physical model, as we will see later on.
This means that the server will have to arrange the incoming messages so that all database's constraints are respected upon insertion of new rows.

By allowing clients to make use of a simpler interface, they do not need to delve into all the details of the underlying physical model.

* `Events` apply to `Classes` and `Class Sections`
  - failing to determine what `Class` or `Class Section` any of the `Events` refer to will abort the operation

* The granularity of a `Class` or a `Class Section` is different from the one in the physical model
  - in the database's PHY model, `WAD 1718v` and `LS 1718v` are different `Classes` unrelated to any `Programme`
  - in the Write API, a `Class` is the group of `Programme Offers` of the same semester. So, `WAD 1718v` and `LS 1718v` could belong to the same `Class` if they belong to the same `Programme` (e.g. `LEIC`).

# Database interactions
Before inserting `Events` on to the database, we must first guarantee that the target `School`, `Programme`, `Class`, `Class Section`, `Calendar Term` and `Courses` are created or else we would be violating foreign key constraints.

* The `School`, `Programme` and `Calendar Term` entities may be created/updated in the beginning of the transaction once (to prevent repeated insertions when iterating through the events)

* While iterating through the collection of `courses`, the following steps are to be achieved:
  - inserting/updating the `Class`, `Class Section` (if `calendarSection` is present) and `Course`
  - iterate through the `events` collection and insert/update each and every `Event`
  - the `Events` belong to the whole `Class` if the `calendarSection` was not provided

* If some constraint fails, the whole transaction aborts (rolling back any changes)
  - otherwise, commit all changes

