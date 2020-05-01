psql -h localhost -U postgres -d I-ON -f drop.sql
psql -h localhost -U postgres -d I-ON -f create.sql
psql -h localhost -U postgres -d I-ON -f views.sql
psql -h localhost -U postgres -d I-ON -f insert.sql
