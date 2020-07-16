Table of Contents
=================

- [Overview](#overview)
- [Docker Compose](#docker-compose)
- [Build](#build)
- [Running](#running)
- [Access management](#access-management)

Overview
========

The **i-on** initiative aims to build an extensible academic platform.
The Core subsystem is the repository of academic information which other subsystems will access through several HTTP APIs.

Docker Compose
==============

You may execute repetitive procedures running one command using only `Docker` + [Docker compose](https://docs.docker.com/compose/) (Docker does not ship with `docker-compose`, so you'll have to install it separately).
The `compose` configuration files are used to automate procedures such as setting up a database and building (/running) the server, running the integration tests, deploying, and so on without the need to manually build or install any dependencies (e.g. OpenJDK).

```sh
$ # Run the integration tests and exit
$ docker-compose -f .docker/compose_ci.yaml up --abort-on-container-exit core
$ 
$ # Remove docker images and clean resources
$ docker-compose -f .docker/compose_ci.yaml down
$
$ # In case you don't feel like installing docker-compose, you may use its docker-image as such
$ docker run --rm -it -v $PWD:$PWD -w $PWD -v /var/run/docker.sock:/var/run/docker.sock docker/compose:1.24.0 -f .docker/compose_ci.yaml up --abort-on-container-exit core
$
$ # Run the server and database on two containers
$ docker-compose -f .docker/compose_run.yaml up core
$ 
$ # Remove docker images and clean resources
$ docker-compose -f .docker/compose_run.yaml down
```
The process is similar to any other file on the `.docker` folder (e.g. `.docker/compose_deploy.yaml`).

The following tokens will be inserted to the database container, for ease of use:
- Read: `l7kowOOkliu21oXxNpuCyM47u2omkysxb8lv3qEhm5U`
- Write: `hfk0DXJ9LIPuhvrjDEmhYRv5Z0YRhOl1DMEEPIp42ok`
- Issue: `vUG-N_m_xVohFrnXcu2Jmt_KAeKfxQXV2LkLjJF4144`
- Revoke: `5eN-N7muBGix6X0N8jfau7Ou-3KcNHPAGVZNGWQ6ryw`

Build
=====

The server will connect to a database specified with the `JDBC_DATABASE_URL` environment variable.
For testing purposes, you may run the task `pgSetupDb` which will run a local database running on a preconfigured Docker container (you must have the docker daemon running and the proper permissions set).

The following is an example of operation:
```sh
$ cd core/project
$
$ # Start DB container (loaded with testing data)
$ # Use the SET command if in a Microsoft Windows shell
$ export JDBC_DATABASE_URL="jdbc:postgresql://localhost:10020/ion?user=unpriv&password=changeit"
$ ./gradlew pgSetupDb
$
$ # Runs the integration tests and builds
$ ./gradlew build
$
$ # Stop the DB running in the background
$ ./gradlew pgStop
```

You may also connect to the database running in the container with `docker exec -it pq-container psql -h localhost -d ion -U unpriv` (you may also use `psql` locally).
The parameters of the command may vary depending on the contents of the `JDBC_DATABASE_URL` variable you have defined earlier.

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

Access management
=================

All requests will be locked under an access control mechanism.
The following task creates the Issue token, used for creation of the client's read and write tokens.

```sh
$ export JDBC_DATABASE_URL="jdbc:postgresql://10.0.2.15:10020/myrealdb?user=unpriv&password=realdbsafepw"
$
$ # Get issue token
$ ./gradlew pgInsertIssueToken
$
$ # This task will insert a short lived read token, for convenience at test time
$ ./gradlew pgInsertReadToken
```

These tasks will output the string value of the token, which you may include in further requests.

```sh
$ # authenticated HTTP request
$ curl -X GET localhost:8080 -H "Authorization: Bearer previouslyinsertedtoken"
```

