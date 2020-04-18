-- some iCal property types
create table dbo.Due (
    id             int generated always as identity primary key,
    comp_id        int references dbo.CalendarComponent(id),
    value          timestamp
);
create table dbo.Dtend (
    id             int generated always as identity primary key,
    comp_id        int references dbo.CalendarComponent(id),
    value          timestamp
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

---- for easier queries or Calendar Components
---- these views will retrieve all the relevant Calendar Property types for a given calendar component
-- VTODO
create or replace view dbo.v_Todo as 
	select CalComp.calendar_id, Comp.id as component_id, Comp.type, Comp.summary, Comp.description, Comp.dtstart, D.value as due
	from dbo.CalendarComponent as Comp join
	dbo.CalendarComponents as CalComp on CalComp.component_id=Comp.id join
	dbo.Due as D on D.comp_id=Comp.id;

-- VEVENT
create or replace view dbo.v_Event as 
	select CalComp.calendar_id, Comp.id as component_id, Comp.type, Comp.summary, Comp.description, Comp.dtstart, D.value as dtend
	from dbo.CalendarComponent as Comp join
	dbo.CalendarComponents as CalComp on CalComp.component_id=Comp.id join
	dbo.Dtend as D on D.comp_id=Comp.id;

---- for creation of calendar components
---- these will verify constraints and insert in the appropriate tables
-- VTODO
create or replace procedure dbo.newTodo(cid int, summary varchar(100), description varchar(100), due timestamp)
as $$
#print_strict_params on
DECLARE
	comp_id INT;
BEGIN
    INSERT INTO dbo.CalendarComponent(type, summary, description, dtstart) VALUES (
        'T', summary, description, now()
    ) RETURNING id INTO comp_id;

	INSERT INTO dbo.CalendarComponents(calendar_id, component_id) VALUES (cid, comp_id);

    INSERT INTO dbo.Due (comp_id, value) VALUES (comp_id, due);
END
$$ LANGUAGE PLpgSQL;

-- VEVENT
create or replace procedure dbo.newEvent(cid int, summary varchar(100), description varchar(100), dtstart timestamp, dtend timestamp)
as $$
#print_strict_params on
DECLARE
	comp_id INT;
BEGIN
    INSERT INTO dbo.CalendarComponent(type, summary, description, dtstart) VALUES (
        'E', summary, description, now()
    ) RETURNING id INTO comp_id;

	INSERT INTO dbo.CalendarComponents(calendar_id, component_id) VALUES (cid, comp_id);

    INSERT INTO dbo.Dtend (comp_id, value) VALUES (comp_id, dtend);
END
$$ LANGUAGE PLpgSQL;
