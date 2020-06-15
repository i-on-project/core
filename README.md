Table of Contents
=================

- [Overview](#overview)
- [Build](#build)
- [Running](#running)

Overview
========

The **i-on** initiative aims to build an extensible academic platform.
The Core subsystem is the repository of academic information which other subsystems will access through several HTTP APIs.

Build
=====

The server will connect to a database specified with the `JDBC_DATABASE_URL` environment variable.
For testing purposes, you may run the task `pgSetupDb` which will run a local database running on a preconfigured Docker container (you must have the docker daemon running and the proper permissions set).

The following is an example of operation:
```sh
$ cd core/project
$
$ # Start DB container (loaded with testing data)
$ export JDBC_DATABASE_URL="jdbc:postgresql://localhost:10020/ion?user=unpriv&password=changeit"
$ ./gradlew pgSetupDb
$
$ # Run the integration/unit tests
$ ./gradlew test
$
$ # Build
$ ./gradlew build
$
$ # Stop the DB running in the background
$ ./gradlew pgStop
```

In Microsoft Window's shell the procedure should be similar with the exception of `export` being `SET` and paths `/` being `\`.

You may also connect to the database running in the container with `docker exec -it pq-container psql -h localhost -d ion -U unpriv` (you may also use `psql` locally).
The parameters of the command may vary depending on the contents of the `JDBC_DATABASE_URL` variable you have defined earlier.

## Updating `JDBC_DATABASE_URL`
If you want to change the connection string, you have to run `./gradlew clean -p buildSrc` before doing so.

Running
=======

The following commands exemplify how to launch the server.
Remember to point `JDBC_DATABASE_URL` to your target database.
```sh
$ cd core/project
$
$ # Generate JAR
$ ./gradlew assemble
$
$ export JDBC_DATABASE_URL="jdbc:postgresql://10.0.2.15:10020/myrealdb?user=unpriv&password=realdbsafepw"
$
$ # Run server
$ java -server -jar ./build/libs/core-0.1.jar
```

If you previously ran the `build` task, you may skip `assemble`.
The server will open on port `8080` which you may change by specifying an option (e.g `java -server -Dserver.port=10022 -jar ./build/libs/core-0.1.jar`)


