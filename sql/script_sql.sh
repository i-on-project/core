echo "Droping Tables"
psql  -U postgres -1 -d I-ON -f drop_tables.sql
echo "Creating Tables"
psql  -U postgres -1 -d I-ON -f create.sql
echo "Inserting Data in Tables"
psql  -U postgres -1 -d I-ON -f insert.sql
