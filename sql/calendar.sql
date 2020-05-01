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
    language       INT REFERENCES dbo.Language(id),
	PRIMARY KEY(comp_id, value, language)
);

CREATE TABLE IF NOT EXISTS dbo.Summary (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    value          VARCHAR(50) NOT NULL,
    language       INT REFERENCES dbo.Language(id),
	PRIMARY KEY(comp_id, value, language)
);

CREATE TABLE IF NOT EXISTS dbo.Attachment (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    value          VARCHAR(128) NOT NULL,
	PRIMARY KEY(comp_id, value)
);

CREATE TABLE IF NOT EXISTS dbo.Due (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    type           INT REFERENCES dbo.ICalendarDataType(id),
    value          TIMESTAMP,
	PRIMARY KEY(comp_id)
);

CREATE TABLE IF NOT EXISTS dbo.Dtend (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    type           INT REFERENCES dbo.ICalendarDataType(id),
    value          TIMESTAMP,
	PRIMARY KEY(comp_id)
);

CREATE TABLE IF NOT EXISTS dbo.Dtstart (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    type           INT REFERENCES dbo.ICalendarDataType(id),
    value          TIMESTAMP,
	PRIMARY KEY(comp_id)
);

CREATE TABLE IF NOT EXISTS dbo.Categories (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    value          INT REFERENCES dbo.Category(id),
	PRIMARY KEY(comp_id, value)
);


DROP VIEW IF EXISTS dbo.v_Journal;
DROP VIEW IF EXISTS dbo.v_Event;
DROP VIEW IF EXISTS dbo.v_Todo;
DROP VIEW IF EXISTS dbo.v_ComponentsAll;
DROP VIEW IF EXISTS dbo.v_ComponentsCommon;
---- for easier queries OR Calendar Components

CREATE OR REPLACE VIEW dbo.v_ComponentsCommon AS
	SELECT DISTINCT
		Comp.id AS uid,
		CalComp.calendar_id AS calendars,
		Comp.type,
		Summ.value AS summaries,
		Summ.language AS summaries_language,
		Descr.value AS descriptions,
		Descr.language AS descriptions_language,
		Comp.dtstamp,
        Comp.created,
        Cat.value AS categories
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
	ORDER BY 
		uid;

-- Mashup of all component types
CREATE OR REPLACE VIEW dbo.v_ComponentsAll AS 
	SELECT DISTINCT
		Comp.*,
		Att.value AS attachments,
		DS.value AS dtstart,
        DS.type AS dtstart_value_data_type,
		DE.value AS dtend,
        DE.type AS dtend_value_data_type,
		RR.byday,
		D.value AS due,
        D.type AS due_value_data_type
	FROM 
		dbo.v_ComponentsCommon AS Comp
    LEFT JOIN 
		dbo.Attachment AS Att ON Comp.uid = Att.comp_id
	LEFT JOIN
		dbo.Due AS D ON Comp.uid=D.comp_id
	LEFT JOIN 
		dbo.Dtstart as DS ON Comp.uid = DS.comp_id
	LEFT JOIN
		dbo.Dtend AS DE ON Comp.uid=DE.comp_id
    LEFT JOIN
		dbo.RecurrenceRule AS RR ON Comp.uid=RR.comp_id
	ORDER BY 
		uid;
		

---- these views will retrieve all the relevant Calendar Property types for a given calendar component
-- VTODO

CREATE OR REPLACE VIEW dbo.v_Todo AS 
    SELECT DISTINCT
		Comp.*,
		Att.value AS attachments,
		D.value AS due,
        D.type AS due_value_data_type
    FROM 
		dbo.v_ComponentsCommon AS Comp
    LEFT JOIN 
		dbo.Attachment AS Att ON Comp.uid = Att.comp_id
	JOIN
		dbo.Due AS D ON Comp.uid=D.comp_id
    WHERE 
		Comp.type = 'T'
	ORDER BY 
		uid;

-- VEVENT
CREATE OR REPLACE VIEW dbo.v_Event AS 
    SELECT DISTINCT
		Comp.*,
        DS.value AS dtstart,
        DS.type AS dtstart_value_data_type,
		DE.value AS dtend,
        DE.type AS dtend_value_data_type,
		RR.byday
    FROM 
		dbo.v_ComponentsCommon AS Comp
    JOIN 
		dbo.Dtstart as DS ON Comp.uid = DS.comp_id
	JOIN
		dbo.Dtend AS DE ON Comp.uid=DE.comp_id
    LEFT JOIN
		dbo.RecurrenceRule AS RR ON Comp.uid=RR.comp_id
    WHERE 
		Comp.type = 'E'
	ORDER BY 
		uid;

-- VJOURNAL
CREATE OR REPLACE VIEW dbo.v_Journal AS 
    SELECT DISTINCT
		Comp.*,
		Att.value AS attachments,
        DS.value AS dtstart,
        DS.type AS dtstart_value_data_type
    FROM 
		dbo.v_ComponentsCommon AS Comp
    JOIN 
		dbo.Attachment AS Att ON Comp.uid = Att.comp_id
    JOIN 
		dbo.Dtstart as DS ON Comp.uid = DS.comp_id
    WHERE 
		Comp.type = 'J'
	ORDER BY 
		uid;

---- for creation of calendar components
---- these will verify constraints and insert in the appropriate tables
-- VEVENT
CREATE OR REPLACE PROCEDURE dbo.newEvent(
	cid INT,
	summary VARCHAR(50)[],
	summary_language INT[],
	description VARCHAR(200)[],
	description_language INT[],
	category INT[],
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
	
    INSERT INTO dbo.Summary (comp_id, value, language)
	SELECT component_id, UNNEST(summary), UNNEST(summary_language);
	
	INSERT INTO dbo.Description (comp_id, value, language)
	SELECT component_id, UNNEST(description), UNNEST(description_language);
	
	INSERT INTO dbo.Categories (comp_id, value)
	SELECT component_id, UNNEST(category);
	
	INSERT INTO dbo.Dtstart (comp_id, type, value) VALUES (component_id, dtstart_dtend_type, dtstart);
	
    INSERT INTO dbo.Dtend (comp_id, type, value) VALUES (component_id, dtstart_dtend_type, dtend);
	
	IF byday IS NOT NULL THEN
		INSERT INTO dbo.RecurrenceRule(comp_id, freq, byday) VALUES (component_id, 'WEEKLY', byday);
	END IF;
	
END
$$ LANGUAGE PLpgSQL;

-- VTODO
CREATE OR REPLACE PROCEDURE dbo.newTodo(
	cid INT,
	summary VARCHAR(50)[],
	summary_language INT[],
	description VARCHAR(200)[],
	description_language INT[],
	category INT[],
	attachment VARCHAR(128)[],
	due TIMESTAMP,
	due_value_type INT
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

    INSERT INTO dbo.Summary (comp_id, value, language)
	SELECT component_id, UNNEST(summary), UNNEST(summary_language);
	
	INSERT INTO dbo.Description (comp_id, value, language)
	SELECT component_id, UNNEST(description), UNNEST(description_language);
	
	INSERT INTO dbo.Categories (comp_id, value)
	SELECT component_id, UNNEST(category);
	
	INSERT INTO dbo.Attachment (comp_id, value)
	SELECT component_id, UNNEST(attachment);
	
	INSERT INTO dbo.Due (comp_id, type, value) VALUES (component_id, due_value_type, due);
END
$$ LANGUAGE PLpgSQL;

-- VJOURNAL
CREATE OR REPLACE PROCEDURE dbo.newJournal(
	cid INT,
	summary VARCHAR(50)[],
	summary_language INT[],
	description VARCHAR(200)[],
	description_language INT[],
	category INT[],
	attachment VARCHAR(128)[],
	dtstart TIMESTAMP,
	dtstart_value_type INT
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
	
    INSERT INTO dbo.Summary (comp_id, value, language)
	SELECT component_id, UNNEST(summary), UNNEST(summary_language);
	
	INSERT INTO dbo.Description (comp_id, value, language)
	SELECT component_id, UNNEST(description), UNNEST(description_language);
	
	INSERT INTO dbo.Categories (comp_id, value)
	SELECT component_id, UNNEST(category);
	
	INSERT INTO dbo.Attachment (comp_id, value)
	SELECT component_id, UNNEST(attachment);
	
	INSERT INTO dbo.Dtstart (comp_id, type, value) VALUES (component_id, dtstart_value_type, dtstart);
	
END
$$ LANGUAGE PLpgSQL;