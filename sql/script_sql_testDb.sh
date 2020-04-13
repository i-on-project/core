echo "Droping Tables"
psql  -U postgres -d I-ON-TEST -f drop_tables.sql
echo "Creating Tables"
psql  -U postgres -d I-ON-TEST -f create.sql
echo "Inserting Data in Tables"
psql  -U postgres -d I-ON-TEST -f insert.sql
