<div align="center" style="margin-bottom: 30px;">
    <div style="margin-bottom: 30px">
        <img src="./resources/graphics/core-logo.svg" height="200px" alt="i-on core logo" />
    </div>
    <div>
        <img src="https://img.shields.io/github/license/i-on-project/core" />
        <img src="https://img.shields.io/github/contributors/i-on-project/core" />
        <img src="https://img.shields.io/github/stars/i-on-project/core" />
        <img src="https://img.shields.io/github/issues/i-on-project/core" />
        <img src="https://img.shields.io/github/issues-pr/i-on-project/core" />
    </div>
</div>

Table of Contents
=================

- [Overview](#overview)
- [Docker Compose](#docker-compose)
    - [Running with Docker](#running-with-docker)
- [Manual Build](#build)
    - [Build Frontend](#building-react-frontend)
    - [Build Spring App](#building-spring-app)
    - [Running without Docker](#running-without-docker)

Overview
========

The **i-on** initiative aims to build an extensible academic platform.
The Core subsystem is the repository of academic information which other subsystems will access through several HTTP APIs.

Docker Compose
==============

You may execute repetitive procedures running one command using only [Docker](https://docs.docker.com/) + [Docker compose](https://docs.docker.com/compose/) (Docker does not ship with `docker-compose`, so you'll have to install it separately).
The `compose` configuration files are used to automate procedures such as setting up a database and building (/running) the server, running the integration tests, deploying, and so on without the need to manually build or install any dependencies (e.g. OpenJDK).

```sh
# Run the integration tests and exit
docker-compose -f .docker/compose_ci.yaml up --abort-on-container-exit core
 
# Remove docker images and clean resources
docker-compose -f .docker/compose_ci.yaml down
```

The process is similar to any other file on the `.docker` folder (e.g. `.docker/compose_deploy.yaml`).

## Warning to Windows Users

Since Windows uses CRLF (carriage-return line-feed) line endings you can encounter problems while trying to run the application using Docker and Docker Compose. 

To solve the problem you must choose the `Checkout as-is, commit Unix-style line endings` option during the Git installation process. This option guarantees that every file is converted to LF once you commit, and when checking out (e.g pull) there are no line-ending conversions, thus the line-endings will be LF.

If you have already installed Git on your machine you can change the `core.autocrlf` configuration key as shown below:

```sh
git config --global core.autocrlf input
```

More information about this topic is covered in [this stackoverflow answer](https://stackoverflow.com/a/20653073).

## Running with Docker

To run i-on core locally we can use the `compose_run` docker compose file:

```sh
# Run the server and database on two containers
docker-compose -f .docker/compose_run.yaml up core
```

After running this command the i-on Core should be available on port `10023` and the database on port `10021`.

The following tokens will be inserted to the database container, for ease of use:
- Read: `l7kowOOkliu21oXxNpuCyM47u2omkysxb8lv3qEhm5U`
- Issue: `vUG-N_m_xVohFrnXcu2Jmt_KAeKfxQXV2LkLjJF4144`
- Revoke: `5eN-N7muBGix6X0N8jfau7Ou-3KcNHPAGVZNGWQ6ryw`

To clean up the core and database containers we can use docker compose again:

```sh
# Remove docker images and clean resources
docker-compose -f .docker/compose_run.yaml down
```

Build
=====

## Building React Frontend

The React frontend makes use of the [yarn](https://yarnpkg.com/) package manager to manage its dependencies. To test the react application you can do:

```sh
cd core-react/

# Install the dependencies
yarn install

# Start the devolopment server
yarn start
```

This process should start a React development server on `localhost:3000`.

If you want to build the production artifacts and place them on the resources folder of the JVM project you can use:

```sh
cd core-react/

# Do a production build
yarn build
```

This will output the production build to `jvm/src/main/resources/static` as configured in the `core-react/.env` file.

## Building Spring App

The server will connect to a database specified with the `JDBC_DATABASE_URL` environment variable.
For testing purposes, you may run the task `pgSetupDb` which will run a local database running on a preconfigured Docker container (you must have the docker daemon running and the proper permissions set).

The following is an example of operation:
```sh
cd jvm/

# Start DB container (loaded with testing data)
# Use the SET command if in a Microsoft Windows shell
export JDBC_DATABASE_URL="jdbc:postgresql://localhost:10020/ion?user=unpriv&password=changeit"
./gradlew pgreset

# Runs the integration tests and builds
./gradlew build

# Stop the DB running in the background
./gradlew pgStop
```

You may also connect to the database running in the container with `docker exec -it pq-container psql -h localhost -d ion -U unpriv` (you may also use `psql` locally).
The parameters of the command may vary depending on the contents of the `JDBC_DATABASE_URL` variable you have defined earlier.

## Running without Docker

The previous sub-sections have explained how to build the i-on Core Spring App that includes both frontend (as a resource) and the backend. To run the application that was built you can execute:
```sh
cd core/project

# The ./gradlew build command was executed in the previous section

export JDBC_DATABASE_URL="jdbc:postgresql://10.0.2.15:10020/myrealdb?user=unpriv&password=realdbsafepw"

# Run the server
java -server -jar ./build/libs/core-0.1.jar
```

The server will open on port `8080` which you may change by specifying an option (e.g `java -server -Dserver.port=10022 -jar ./build/libs/core-0.1.jar`)
