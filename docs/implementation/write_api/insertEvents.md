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

