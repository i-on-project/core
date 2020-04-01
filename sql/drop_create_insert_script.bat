psql -h localhost -U postgres -d I-ON -f drop_tables.sql
psql -h localhost -U postgres -d I-ON -f create.sql
psql -h localhost -U postgres -d I-ON -f insert.sql