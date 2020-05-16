# core
Repository for the Core i-on component

## Development

- To start the container-based Postgres server and setup the DB, run the `pgSetupDb` gradle task. To stop and remove it, run the `pgStop` gradle task.
- To build and run the tests do `JDBC_DATABASE_URL="jdbc:postgresql://localhost:5432/ion?user=postgres&password=changeit" ./gradlew test`.
- To connect to Postgres via `psql` do: `psql - U postgres -h localhost`.
  - `\c ion` connects to the `ion` database.
  - All the objects are in the `dbo` schema.