CREATE TABLE dbo.ICalendarDataType (
    id             INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name           VARCHAR(16) NOT NULL UNIQUE
);

CREATE TABLE dbo.Language(
    id             INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name           VARCHAR(64) NOT NULL UNIQUE
);

-- some iCal property types
CREATE TABLE dbo.Description (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    value          VARCHAR(200) NOT NULL,
    language       INT REFERENCES dbo.Language(id)
)

CREATE TABLE dbo.Summary (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    value          VARCHAR(50) NOT NULL,
    language       INT REFERENCES dbo.Language(id)
);

CREATE TABLE dbo.Attachment (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    value          VARCHAR(50) NOT NULL
);

CREATE TABLE dbo.Due (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    type           INT REFERENCES dbo.ICalendarDataType(id),
    value          TIMESTAMP
);

CREATE TABLE dbo.Dtend (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    type           INT REFERENCES dbo.ICalendarDataType(id),
    value          TIMESTAMP
);

CREATE TABLE dbo.Dtstart (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    type           INT REFERENCES dbo.ICalendarDataType(id),
    value          TIMESTAMP
);

CREATE TABLE dbo.Categories (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    value          VARCHAR(64) NOT NULL,
    language       INT REFERENCES dbo.Language(id) -- OR language  VARCHAR(64) --nullable and define default in API docs. Prob english
);

---- for easier queries OR Calendar Components
---- these views will retrieve all the relevant Calendar Property types for a given calendar component
-- VTODO
CREATE OR REPLACE view dbo.v_Todo AS 
	SELECT 
        CalComp.calendar_id,
        Comp.id AS uid,
        Summ.value AS summary,
        Summ.language AS summary_language,
        Descr.value AS description,
        Descr.language AS description_language,
        Att.value AS attachment,
        Comp.dtstamp,
        Comp.created,
        D.value AS due,
        D.type AS due_value_data_type,
        Cat.value as category,
        Cat.language as category_language
	FROM dbo.CalendarComponent AS Comp
    JOIN dbo.CalendarComponents AS CalComp ON CalComp.component_id=Comp.id
    JOIN dbo.Summary AS Summ ON Comp.id = Summ.comp_id
    JOIN dbo.Description AS Descr ON Comp.id = Descr.comp_id
    JOIN dbo.Categories AS Cat ON Comp.id = Cat.comp_id
    JOIN dbo.Due AS D ON D.comp_id=Comp.id
    JOIN dbo.Attachment AS Att ON Comp.id=Att.comp_id
    WHERE Comp.type = 'T';

-- VEVENT
CREATE OR REPLACE view dbo.v_Event AS 
	SELECT 
        CalComp.calendar_id,
        Comp.id AS uid,
        Summ.value AS summary,
        Summ.language AS summary_language,
        Descr.value AS description,
        Descr.language AS description_language,
        Comp.dtstamp,
        Comp.created,
        Cat.value as category,
        Cat.language as category_language,
        DS.value as dtstart,
        DS.type as dtstart_value_data_type,
        DE.value AS dtend,
        DE.type AS dtend_value_data_type,
        RR.byday
	FROM dbo.CalendarComponent AS Comp
    JOIN dbo.CalendarComponents AS CalComp ON CalComp.component_id=Comp.id
    JOIN dbo.Summary AS Summ ON Comp.id = Summ.comp_id
    JOIN dbo.Description AS Descr ON Comp.id = Descr.comp_id
    JOIN dbo.Categories AS Cat ON Comp.id = Cat.comp_id
    JOIN dbo.Dtstart as DS ON Comp.id = DS.comp_id
    JOIN dbo.Dtend AS DE ON Comp.id=DE.comp_id
    JOIN dbo.RecurrenceRule AS RR ON Comp.id=RR.comp_id
    WHERE Comp.type = 'E';

-- VJOURNAL
CREATE OR REPLACE view dbo.v_Journal AS 
    SELECT 
        CalComp.calendar_id,
        Comp.id AS uid,
        Summ.value AS summary,
        Summ.language AS summary_language,
        Descr.value AS description,
        Descr.language AS description_language,
        Att.value AS attachment,
        Comp.dtstamp,
        Comp.created,
        Cat.value as category,
        Cat.language as category_language,
        DS.value as dtstart,
        DS.type as dtstart_value_data_type
    FROM dbo.CalendarComponent AS Comp
    JOIN dbo.CalendarComponents AS CalComp ON CalComp.component_id=Comp.id
    JOIN dbo.Summary AS Summ ON Comp.id = Summ.comp_id
    JOIN dbo.Description AS Descr ON Comp.id = Descr.comp_id
    JOIN dbo.Attachment AS Att ON Comp.id = Att.comp_id
    JOIN dbo.Categories AS Cat ON Comp.id = Cat.comp_id
    JOIN dbo.Dtstart as DS ON Comp.id = DS.comp_id
    WHERE Comp.type = 'J';

---- for creation of calendar components
---- these will verify constraints and insert in the appropriate tables
-- VTODO
CREATE OR REPLACE PROCEDURE dbo.newTodo(cid INT, summary VARCHAR(100), description VARCHAR(100), due TIMESTAMP)
AS $$
#print_strict_params ON
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
CREATE OR REPLACE PROCEDURE dbo.newEvent(cid INT, summary VARCHAR(100), description VARCHAR(100), dtstart TIMESTAMP, dtend TIMESTAMP)
AS $$
#print_strict_params ON
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
