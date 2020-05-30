# Intro

This document introduces the design of the system in-charge of processing the access token and enforcing the correct measures based on the policies and request context.

# Components
This system at the moment is divided in 4 major components:
* PEP
* PDP
* PAP
* Policy Store

## PEP
Policy Enforcement Point is the gateway of the system, it intercepts the request and checks if there is a token associated to it.
If there is a token, it invokes the PDP for further processing, otherwise returns 401 Unauthenticated response.

The PDP will make a decision, allow or not allow the request to continue, the PEP will enforce that decision.

## PDP 
The PDP receives a request object that contains the token, the method and the URL of the request.
The PDP will then have to evaluate the policies against the actions associated to the request object, if the PDP decides the request is allowed/valid it will return a positive decision to the PEP to allow further processing otherwise a negative decision and the request will drop and return an appropiate error message to the client.

To obtain the set of policies associated to the token the PDP will call the PAP.

## PAP
The Policy Administration Point will define the policies to be verified, for that it will have to check the Policy Store.

## Policy Store
The Policy Store is the place where all policies are stored, in this case, a database.
An example of possible structure to support the pysical storage of policies can be observed in the SQL_Access_Control_Design document.


# Example of a valid request
Android client makes a "GET" request for "/courses" passing the correct Access Token in the Authorization Header.

```
Client ----> PEP -----> API 
              /\
              |
              |
              \/
              PDP <----> PAP <---> Policy Store
```

1ºPEP will intercept the request and ask the PDP component if it should allow the request to continue or reject it.

2ºThe PDP will contact the PAP to obtain the list of policies. 

3ºThe PAP will obtain the policies from the policy store and return them to the PDP. 

4ºPDP will evaluate the policies based on the request content.
For example PAP could have returned "Allow: {GET: [/courses, /programmes]},...".
Based on the request "GET: /courses", the PDP will successfully verify that the action is valid and return a positive decision to PEP. 

5º PEP will then enforce the decision made by the PDP. (Allow to reach the API or reject the request)


# Notes:
* If the client has multiple scopes his total permissions are the union of all permissions.
* The permissions are always positive. (It describes what is allowed than what is not allowed)
* For safety the policies are stored in a specific way instead of using wildcards (e.g. POST ALL)

# Possible policies to verify
Possible list of policies to verify on a request:

1º Is that client blocked from making requests? (E.g. IP banned for security)
2º Does the token exist?
3º Is the token revoked?
4º Is the token expired?
5º Is the client allowed to do that?

Those policies are just an example does not mean they will be implemented.