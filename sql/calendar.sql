create table dbo.CalendarDataTypes (
    id             int generated always as identity unique not null,
    name           varchar(64) not null unique, -- TEXT, DATETIME, DATE, BINARY, URI
    primary key(id)
);
create table dbo.Due (
    id             int generated always as identity primary key,
    comp_id        int references dbo.CalendarComponent(id),
    value          timestamp,
    type           int references dbo.CalendarDataTypes(id)
);
create table dbo.Language(
    id             int generated always as identity primary key,
    name           varchar(64)
);
create table dbo.Categories (
    id             int generated always as identity primary key,
    comp_id        int references dbo.CalendarComponent(id),
    value          varchar(64),
    language       int references dbo.Language(id) -- or language  varchar(64) --nullable and define default in API docs. Prob english
);

create or replace procedure dbo.newTodo(cid int, summary varchar(100), description varchar(100), due int, dtype int)
as $$
#print_strict_params on
DECLARE
	comp_id INT;
BEGIN
    INSERT INTO dbo.CalendarComponent(calendar_id, summary, description, due, dtstart) VALUES (
        cid, summary, description, due, now()
    ) RETURNING id INTO comp_id;

    INSERT INTO dbo.Due (comp_id, value, type) VALUES (
        comp_id,
        due,
        dtype
    );
END
$$ LANGUAGE PLpgSQL;
