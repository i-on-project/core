CREATE TABLE dbo.ICalendarDataType (
    id             INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name           VARCHAR(16) NOT NULL UNIQUE
);

CREATE TABLE dbo.Language(
    id             INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name           VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE dbo.Category(
	id             INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name           VARCHAR(64) NOT NULL,
	language       INT REFERENCES dbo.Language(id),
	UNIQUE(name, language)
);

-- some iCal property types
CREATE TABLE dbo.Description (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    value          VARCHAR(200),
    language       INT REFERENCES dbo.Language(id)
);

CREATE TABLE dbo.Summary (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    value          VARCHAR(50) NOT NULL,
    language       INT REFERENCES dbo.Language(id)
);

CREATE TABLE dbo.Attachment (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    value          VARCHAR(128) NOT NULL
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
    value          INT REFERENCES dbo.Category(id)
);

---- for easier queries OR Calendar Components
---- these views will retrieve all the relevant Calendar Property types for a given calendar component
-- VTODO
CREATE OR REPLACE view dbo.v_Todo AS 
	SELECT DISTINCT
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
        Cat.value as category
	FROM dbo.CalendarComponent AS Comp
    JOIN dbo.CalendarComponents AS CalComp ON CalComp.comp_id=Comp.id
    JOIN dbo.Summary AS Summ ON Comp.id = Summ.comp_id
    JOIN dbo.Description AS Descr ON Comp.id = Descr.comp_id
    JOIN dbo.Categories AS Cat ON Comp.id = Cat.comp_id
    JOIN dbo.Due AS D ON D.comp_id=Comp.id
    JOIN dbo.Attachment AS Att ON Comp.id=Att.comp_id
    WHERE Comp.type = 'T';

-- VEVENT
CREATE OR REPLACE view dbo.v_Event AS 
	SELECT DISTINCT
        CalComp.calendar_id,
        Comp.id AS uid,
        Summ.value AS summary,
        Summ.language AS summary_language,
        Descr.value AS description,
        Descr.language AS description_language,
        Comp.dtstamp,
        Comp.created,
        Cat.value as category,
        DS.value as dtstart,
        DS.type as dtstart_value_data_type,
        DE.value AS dtend,
        DE.type AS dtend_value_data_type,
        RR.byday
	FROM dbo.CalendarComponent AS Comp
    JOIN dbo.CalendarComponents AS CalComp ON CalComp.comp_id=Comp.id
    JOIN dbo.Summary AS Summ ON Comp.id = Summ.comp_id
    JOIN dbo.Description AS Descr ON Comp.id = Descr.comp_id
    JOIN dbo.Categories AS Cat ON Comp.id = Cat.comp_id
    JOIN dbo.Dtstart as DS ON Comp.id = DS.comp_id
    JOIN dbo.Dtend AS DE ON Comp.id=DE.comp_id
    JOIN dbo.RecurrenceRule AS RR ON Comp.id=RR.comp_id
    WHERE Comp.type = 'E';

-- VJOURNAL
CREATE OR REPLACE view dbo.v_Journal AS 
    SELECT DISTINCT
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
        DS.value as dtstart,
        DS.type as dtstart_value_data_type
    FROM dbo.CalendarComponent AS Comp
    JOIN dbo.CalendarComponents AS CalComp ON CalComp.comp_id=Comp.id
    JOIN dbo.Summary AS Summ ON Comp.id = Summ.comp_id
    JOIN dbo.Description AS Descr ON Comp.id = Descr.comp_id
    JOIN dbo.Attachment AS Att ON Comp.id = Att.comp_id
    JOIN dbo.Categories AS Cat ON Comp.id = Cat.comp_id
    JOIN dbo.Dtstart as DS ON Comp.id = DS.comp_id
    WHERE Comp.type = 'J';

---- for creation of calendar components
---- these will verify constraints and insert in the appropriate tables
-- VEVENT
CREATE OR REPLACE PROCEDURE dbo.newEvent(
	cid INT,
	summary VARCHAR(50),
	description VARCHAR(200),
	category INT,
	dtstart TIMESTAMP,
    dtend TIMESTAMP,
    dtstart_dtend_type INT REFERENCES dbo.ICalendarDataType(id),
    byday VARCHAR(20)
) AS $$
#print_strict_params ON
DECLARE
	component_id INT;
	time TIMESTAMP;
BEGIN
	time := now();
    INSERT INTO dbo.CalendarComponent(type, dtstamp, created) VALUES
        ('E', time, time)
    RETURNING id INTO component_id;

	INSERT INTO dbo.CalendarComponents(calendar_id, comp_id) VALUES (cid, component_id);
    INSERT INTO dbo.Summary (comp_id, value) VALUES (component_id, summary);
	INSERT INTO dbo.Description (comp_id, value) VALUES (component_id, description);
	INSERT INTO dbo.Categories (comp_id, value) VALUES (component_id, category);
	INSERT INTO dbo.Dtstart (comp_id, type, value) VALUES (component_id, dtstart_dtend_type, dtstart);
    INSERT INTO dbo.Dtend (comp_id, type, value) VALUES (component_id, dtstart_dtend_type, dtend);
	INSERT INTO dbo.RecurrenceRule(comp_id, freq, byday) VALUES (component_id, 'WEEKLY', byday);
END
$$ LANGUAGE PLpgSQL;

-- VTODO
CREATE OR REPLACE PROCEDURE dbo.newTodo(
	cid INT,
	summary VARCHAR(50),
	description VARCHAR(200),
	attachment VARCHAR(128),
	category INT,
	dtstart TIMESTAMP
) AS $$
#print_strict_params ON
DECLARE
	component_id INT;
	time TIMESTAMP;
BEGIN
	time := now();
    INSERT INTO dbo.CalendarComponent(type, dtstamp, created) VALUES
        ('T', time, time)
    RETURNING id INTO component_id;

	INSERT INTO dbo.CalendarComponents(calendar_id, comp_id) VALUES (cid, component_id);

    INSERT INTO dbo.Summary (comp_id, value) VALUES (component_id, summary);
	INSERT INTO dbo.Description (comp_id, value) VALUES (component_id, description);
	INSERT INTO dbo.Attachment (comp_id, value) VALUES (component_id, attachment);
	INSERT INTO dbo.Categories (comp_id, value) VALUES (component_id, category);
	INSERT INTO dbo.Dtstart (comp_id, type, value) VALUES (component_id, (SELECT id FROM dbo.ICalendarDataType WHERE name='DATETIME'), dtstart);
END
$$ LANGUAGE PLpgSQL;

-- VJOURNAL
CREATE OR REPLACE PROCEDURE dbo.newJournal(
	cid INT,
	summary VARCHAR(50),
	description VARCHAR(200),
	attachment VARCHAR(128),
	category INT,
	dtstart TIMESTAMP
) AS $$
#print_strict_params ON
DECLARE
	component_id INT;
	time TIMESTAMP;
BEGIN
	time := now();
    INSERT INTO dbo.CalendarComponent(type, dtstamp, created) VALUES
        ('J', time, time)
    RETURNING id INTO component_id;

	INSERT INTO dbo.CalendarComponents(calendar_id, comp_id) VALUES (cid, component_id);

    INSERT INTO dbo.Summary (comp_id, value) VALUES (component_id, summary);
	INSERT INTO dbo.Description (comp_id, value) VALUES (component_id, description);
	INSERT INTO dbo.Attachment (comp_id, value) VALUES (component_id, attachment);
	INSERT INTO dbo.Categories (comp_id, value) VALUES (component_id, category);
	INSERT INTO dbo.Dtstart (comp_id, type, value) VALUES (component_id, (SELECT id FROM dbo.ICalendarDataType WHERE name='DATETIME'), dtstart);
END
$$ LANGUAGE PLpgSQL;
