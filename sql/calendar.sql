CREATE TABLE IF NOT EXISTS dbo.ICalendarDataType (
    id             INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name           VARCHAR(16) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS dbo.Language(
    id             INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name           VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS dbo.Category(
	id             INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name           VARCHAR(64) NOT NULL,
	language       INT REFERENCES dbo.Language(id),
	UNIQUE(name, language)
);

-- some iCal property types
CREATE TABLE IF NOT EXISTS dbo.Description (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    value          VARCHAR(200),
    language       INT REFERENCES dbo.Language(id)
);

CREATE TABLE IF NOT EXISTS dbo.Summary (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    value          VARCHAR(50) NOT NULL,
    language       INT REFERENCES dbo.Language(id)
);

CREATE TABLE IF NOT EXISTS dbo.Attachment (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    value          VARCHAR(128) NOT NULL
);

CREATE TABLE IF NOT EXISTS dbo.Due (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    type           INT REFERENCES dbo.ICalendarDataType(id),
    value          TIMESTAMP
);

CREATE TABLE IF NOT EXISTS dbo.Dtend (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    type           INT REFERENCES dbo.ICalendarDataType(id),
    value          TIMESTAMP
);

CREATE TABLE IF NOT EXISTS dbo.Dtstart (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    type           INT REFERENCES dbo.ICalendarDataType(id),
    value          TIMESTAMP
);

CREATE TABLE IF NOT EXISTS dbo.Categories (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    value          INT REFERENCES dbo.Category(id)
);

---- for easier queries OR Calendar Components
DROP VIEW dbo.v_Components;
CREATE OR REPLACE VIEW dbo.v_Components AS
	SELECT DISTINCT
		Comp.id AS uid,
		ARRAY_AGG(DISTINCT CalComp.calendar_id) AS calendars,
		ARRAY_AGG(DISTINCT MERGE_LANGUAGE_TEXT(Summ.language, Summ.value)) AS summaries,
        ARRAY_AGG(DISTINCT MERGE_LANGUAGE_TEXT(Descr.language, Descr.value)) AS descriptions,
        Comp.dtstamp,
        Comp.created,
        ARRAY_AGG(DISTINCT Cat.value) AS categories
    FROM 
		dbo.CalendarComponent AS Comp
    JOIN 
		dbo.CalendarComponents AS CalComp ON CalComp.comp_id=Comp.id
    JOIN 
		dbo.Summary AS Summ ON Comp.id = Summ.comp_id
    JOIN 
		dbo.Description AS Descr ON Comp.id = Descr.comp_id
    JOIN 
		dbo.Categories AS Cat ON Comp.id = Cat.comp_id
	GROUP BY
		uid
	ORDER BY 
		uid;

---- these views will retrieve all the relevant Calendar Property types for a given calendar component
-- VTODO
DROP VIEW dbo.v_Todo;
CREATE OR REPLACE VIEW dbo.v_Todo AS 
    SELECT DISTINCT
		Comp.id AS uid,
		ARRAY_AGG(DISTINCT CalComp.calendar_id) AS calendars,
		ARRAY_AGG(DISTINCT MERGE_LANGUAGE_TEXT(Summ.language, Summ.value)) AS summaries,
        ARRAY_AGG(DISTINCT MERGE_LANGUAGE_TEXT(Descr.language, Descr.value)) AS descriptions,
        Comp.dtstamp,
        Comp.created,
        ARRAY_AGG(DISTINCT Cat.value) AS categories,
		ARRAY_AGG(DISTINCT Att.value) AS attachments,
		D.value AS due,
        D.type AS due_value_data_type
    FROM 
		dbo.CalendarComponent AS Comp
    JOIN 
		dbo.CalendarComponents AS CalComp ON CalComp.comp_id=Comp.id
    JOIN 
		dbo.Summary AS Summ ON Comp.id = Summ.comp_id
    JOIN 
		dbo.Description AS Descr ON Comp.id = Descr.comp_id
    JOIN 
		dbo.Categories AS Cat ON Comp.id = Cat.comp_id
	JOIN 
		dbo.Attachment AS Att ON Comp.id = Att.comp_id
	JOIN
		dbo.Due AS D ON Comp.id=D.comp_id
    WHERE 
		Comp.type = 'T'
	GROUP BY
		uid, due, due_value_data_type
	ORDER BY 
		uid;

-- VEVENT
DROP VIEW dbo.v_Event;
CREATE OR REPLACE VIEW dbo.v_Event AS 
    SELECT DISTINCT
		Comp.id AS uid,
		ARRAY_AGG(DISTINCT CalComp.calendar_id) AS calendars,
		ARRAY_AGG(DISTINCT MERGE_LANGUAGE_TEXT(Summ.language, Summ.value)) AS summaries,
        ARRAY_AGG(DISTINCT MERGE_LANGUAGE_TEXT(Descr.language, Descr.value)) AS descriptions,
        Comp.dtstamp,
        Comp.created,
        ARRAY_AGG(DISTINCT Cat.value) AS categories,
        DS.value AS dtstart,
        DS.type AS dtstart_value_data_type,
		DE.value AS dtend,
        DE.type AS dtend_value_data_type,
		ARRAY_AGG(DISTINCT RR.byday)
    FROM 
		dbo.CalendarComponent AS Comp
    JOIN 
		dbo.CalendarComponents AS CalComp ON CalComp.comp_id=Comp.id
    JOIN 
		dbo.Summary AS Summ ON Comp.id = Summ.comp_id
    JOIN 
		dbo.Description AS Descr ON Comp.id = Descr.comp_id
    JOIN 
		dbo.Categories AS Cat ON Comp.id = Cat.comp_id
    JOIN 
		dbo.Dtstart as DS ON Comp.id = DS.comp_id
	JOIN
		dbo.Dtend AS DE ON Comp.id=DE.comp_id
    JOIN
		dbo.RecurrenceRule AS RR ON Comp.id=RR.comp_id
    WHERE 
		Comp.type = 'E'
	GROUP BY
		uid, dtstart, dtstart_value_data_type, dtend, dtend_value_data_type
	ORDER BY 
		uid;

-- VJOURNAL
DROP VIEW dbo.v_Journal;
CREATE OR REPLACE VIEW dbo.v_Journal AS 
    SELECT DISTINCT
		Comp.id AS uid,
		ARRAY_AGG(DISTINCT CalComp.calendar_id) AS calendars,
		ARRAY_AGG(DISTINCT MERGE_LANGUAGE_TEXT(Summ.language, Summ.value)) AS summaries,
        ARRAY_AGG(DISTINCT MERGE_LANGUAGE_TEXT(Descr.language, Descr.value)) AS descriptions,
        ARRAY_AGG(DISTINCT Att.value) AS attachments,
        Comp.dtstamp,
        Comp.created,
        ARRAY_AGG(DISTINCT Cat.value) AS categories,
        DS.value AS dtstart,
        DS.type AS dtstart_value_data_type
    FROM 
		dbo.CalendarComponent AS Comp
    JOIN 
		dbo.CalendarComponents AS CalComp ON CalComp.comp_id=Comp.id
    JOIN 
		dbo.Summary AS Summ ON Comp.id = Summ.comp_id
    JOIN 
		dbo.Description AS Descr ON Comp.id = Descr.comp_id
    JOIN 
		dbo.Attachment AS Att ON Comp.id = Att.comp_id
    JOIN 
		dbo.Categories AS Cat ON Comp.id = Cat.comp_id
    JOIN 
		dbo.Dtstart as DS ON Comp.id = DS.comp_id
    WHERE 
		Comp.type = 'J'
	GROUP BY
		uid, dtstart, dtstart_value_data_type
	ORDER BY 
		uid;

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
    dtstart_dtend_type INT,
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
