# Integration Tests
This document will list and compare the different policies for integration testing of the `Core` server.
[Here is a reference to the comparison between Spring Unit testing vs Spring Integration testing made over at Spring Docs.](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/testing.html#spring-mvc-test-vs-end-to-end-integration-tests)

## Spring Boot Tests

### pros
* the output makes it very easy to debug
    - the failing handler is pointed out by the testing framework
    - a stack trace is laid out for you
    - the contents of the request/response are shown
* do not require a server running as an independent background process
    - a `WebApplicationContext` is used instead
* use `TestContext` to execute tests in parallel
    - optimal for read API tests, since non of its requests are unsafe
* Spring IoC context caching is possible
    - the same `WebApplicationContext` will be shared among tests, thereby only starting the server once
    - ["Support for the caching of loaded contexts is important, because startup time can become an issue — not because of the overhead of Spring itself, but because the objects instantiated by the Spring container take time to instantiate."](https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/testing.html#testing-ctx-management), Spring Docs

### cons
* this type of integration testing is distinct from authentic client-server interactions
    - with different conditions, errors might also manifest in a different way (or not manifest at all)
* further dependence on the Spring framework's implementation
    - performance is almost fully dictated by the frameworks implementation
    - bugs on the testing framework might break tests

## Pure End-to-End Tests
To further clarify, these types of tests will act as real clients to an authentic server instance.

### pros
* it's as close to the real client-server interaction as possible
* not bound to the Spring framework's implementation
    - i.e. the tests can take any form such as gradle tasks, scripts, etc
* if the server is refactored to use a different framework or environment, the tests may remain intact
    - e.g. refactoring the whole code to use WebFlux instead
    - e.g. refactoring the whole code for .NET instead

### cons
* the server is a black-box
    - only the HTTP response will be tested, so internal errors might remain undetected
    - failed tests' responses will not explicitly indicate what portion of the code caused the error
    - reverse engineering will be needed in order to debug

# Testing performance
This section will compare the different testing policies in terms of usefulness and performance.

## End-to-End
Very "rough around the edges" end-to-end tests using the custom [gradle task](https://github.com/i-on-project/core/commit/a83793f585bcad2a113f9c37582ae7ee1b69f7d1) `fullBuild` and [JUnit tests](https://github.com/i-on-project/core/commit/f634fe482a288f7fcda4abb0d45509bc11284aa8).
The unit tests will connect to the server using `HttpClient` instances and are available in a [separate branch](https://github.com/i-on-project/core/tree/test/gh-72-integration-testing-options) (run using: `./gradlew fullBuild`).

### Result
* `./gradlew clean fullBuild  1.55s user 0.11s system 13% cpu 12.735 total`
* roughly 13 seconds

### Procedure
* the task executes the following steps:
    - `gradle assemble`
    - launching `java -server` as a process and detach it from the current process
    - polling until server responds (using synchronous `Socket`s)
    - `gradle test`
    - kill the server's process

### Notes
* all spring boot tests were removed
* unit tests will connect to the server using `HttpClient`
* the retrieved payload will be compared with a hard coded string (i.e. the expected JSON object)
    - the retrieved payload will have to be the exact same every time
    - any slight change will cause the test to fail
    - the order of each and every JSON property matters
* deliberately placing bugs in the code will make the string validation exit with error
    - further details on the error will have to be manually detected

## Spring Boot tests
### Result
* `./gradlew clean build test  1.10s user 0.06s system 12% cpu 9.106 total`
    - roughly 9 seconds

### Procedure
* server only starts once in the beginning
    - all tests will reuse the same server through DI of `MockMvc`
    - `WebApplicationContext` is cached
* expected representations are built on each unit test using `SirenBuilder`
* response payloads are iterated using `MvcMatcher`

### Notes
* benevolent towards slight changes
    - e.g. the order of the JSON properties
    - e.g. trailing spaces of the JSON object

# Conclusion
In my opinion, Spring Boot tests would be the better option to implement the integration tests.
In this particular case, better performance could be achieved with the Spring Boot tests with lower build/test times.

However, the results achieved are mostly limited to the framework's implementation, as opposed to the "homemade" integration tests which may be improved by hand.
Furthermore, Spring Boot will have framework specific lingo making it harder to read and modify for newcomers.
All integration tests will have to be reimplemented if the source code is refactored for use with a different framework/environment.


