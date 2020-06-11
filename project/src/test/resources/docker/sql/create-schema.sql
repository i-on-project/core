CREATE SCHEMA dbo; --POSTGRES ALREADY USES 'public' SCHEMA BY DEFAULT

------- PHY Model --------
CREATE TABLE dbo.Programme(
	id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	acronym         VARCHAR(10) UNIQUE,				
	name            VARCHAR(100) UNIQUE,			-- It may be NULL in this phase
	termSize        INT					
);

CREATE TABLE dbo.Calendar (
	id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
);

CREATE TABLE dbo.Course (
	id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	acronym         VARCHAR(10) UNIQUE,	
	name            VARCHAR(100) UNIQUE				-- It may be NULL in this phase
);

CREATE TABLE dbo.ProgrammeOffer(
	id              INT GENERATED ALWAYS AS IDENTITY,
	programmeId     INT REFERENCES dbo.Programme(id),
	courseId        INT REFERENCES dbo.Course(id),
	termNumber      INT, 
	optional        BOOLEAN,
	PRIMARY KEY(programmeId, courseId, termNumber)
);

CREATE TABLE dbo.CalendarTerm (
	id         VARCHAR(20) PRIMARY KEY, -- e.g. "1920v"
	start_date TIMESTAMP,
	end_date   TIMESTAMP CHECK(end_date > start_date),
	UNIQUE(start_date, end_date)
);

CREATE TABLE dbo.Class (
	id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	courseId        INT REFERENCES dbo.Course(id),
	calendarTerm    VARCHAR(20) REFERENCES dbo.CalendarTerm(id),
	calendar        INT REFERENCES dbo.Calendar(id) UNIQUE,
	UNIQUE(courseId, calendarTerm)
);

CREATE TABLE dbo.ClassSection (
	id              VARCHAR(10),
	classId         INT REFERENCES dbo.Class(id),
	calendar        INT REFERENCES dbo.Calendar(id) UNIQUE,
    PRIMARY KEY(id, classId)
);

-- CALENDAR
CREATE TABLE dbo.CalendarComponent (
	id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    dtstamp         TIMESTAMP NOT NULL,
    created         TIMESTAMP NOT NULL,
    type            CHAR NOT NULL
);

CREATE TABLE dbo.CalendarComponents (
	calendar_id     INT REFERENCES dbo.Calendar(id),
	comp_id         INT REFERENCES dbo.CalendarComponent(id),
	PRIMARY KEY (calendar_id, comp_id)
);

CREATE TABLE dbo.RecurrenceRule (
	comp_id     	INT REFERENCES dbo.CalendarComponent(id),
	freq            VARCHAR(20),
	byday           VARCHAR(20),
	until           TIMESTAMP,
	PRIMARY KEY (comp_id, byday)
);

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
		RR.until,
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
		RR.byday,
		RR.until
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
    byday VARCHAR(20),
    until TIMESTAMP,
    stamp_time TIMESTAMP DEFAULT now()
) AS $$
#print_strict_params ON
DECLARE
	component_id INT;
BEGIN
    INSERT INTO dbo.CalendarComponent(type, dtstamp, created) VALUES
    ('E', stamp_time, stamp_time)
    RETURNING id INTO component_id;

	INSERT INTO dbo.CalendarComponents(calendar_id, comp_id) VALUES (cid, component_id);
	
    INSERT INTO dbo.Summary (comp_id, value, language)
	SELECT component_id, UNNEST(summary), UNNEST(summary_language);
	
	INSERT INTO dbo.Description (comp_id, value, language)
	SELECT component_id, UNNEST(description), UNNEST(description_language);
	
	INSERT INTO dbo.Categories (comp_id, value)
	SELECT component_id, UNNEST(category);
	
	INSERT INTO dbo.Dtstart (comp_id, type, value) VALUES
    (component_id, dtstart_dtend_type, dtstart);
	
    INSERT INTO dbo.Dtend (comp_id, type, value) VALUES
    (component_id, dtstart_dtend_type, dtend);
	
	IF byday IS NOT NULL THEN
		INSERT INTO dbo.RecurrenceRule(comp_id, freq, byday, until) VALUES
        (component_id, 'WEEKLY', byday, until);
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
	due_value_type INT,
    stamp_time TIMESTAMP default now()
) AS $$
#print_strict_params ON
DECLARE
	component_id INT;
BEGIN
    INSERT INTO dbo.CalendarComponent(type, dtstamp, created) VALUES
    ('T', stamp_time, stamp_time)
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
	dtstart_value_type INT,
	stamp_time TIMESTAMP default now()
) AS $$
#print_strict_params ON
DECLARE
	component_id INT;
BEGIN
    INSERT INTO dbo.CalendarComponent(type, dtstamp, created) VALUES
    ('J', stamp_time, stamp_time)
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

------- VIEWS --------
CREATE VIEW dbo.courseWithTerm AS
	SELECT co.*, cl.calendarterm FROM 
		(SELECT coI.id,coI.acronym,coI.name FROM dbo.Course AS coI 
			INNER JOIN dbo.Class AS clI ON coI.id=clI.courseId GROUP BY coI.id) AS co
		INNER JOIN
		(SELECT DISTINCT ON (clI.courseId) clI.courseId,clI.calendarterm, ctI.start_date FROM dbo.class AS clI
                        INNER JOIN dbo.CalendarTerm AS ctI ON clI.calendarterm=ctI.id 
				ORDER BY clI.courseId,ctI.start_date DESC) AS cl
		ON co.id=cl.courseId;

------- SPs --------
-- When creating a Class, give it a new Calendar 
CREATE OR REPLACE PROCEDURE dbo.sp_classCalendarCreate (calterm VARCHAR(200), courseid INT)
AS $$
#print_strict_params on
DECLARE
calid INT;
BEGIN
	INSERT INTO dbo.Calendar VALUES (DEFAULT) returning id INTO calid;

	INSERT INTO dbo.Class(courseid, calendarterm, calendar) VALUES
	(courseid, calterm, calid);
END
$$ LANGUAGE plpgsql;

-- When creating a ClassSection, give it a new Calendar 
CREATE OR REPLACE PROCEDURE dbo.sp_classSectionCalendarCreate (classId INT, sid VARCHAR(200))
AS $$
#print_strict_params on
DECLARE
calid INT;
BEGIN
	INSERT INTO dbo.Calendar VALUES (DEFAULT) returning id INTO calid;

	INSERT INTO dbo.ClassSection(id, classId, calendar) VALUES
	(sid, classId, calid);
END
$$ LANGUAGE plpgsql;


-- Access manager 

CREATE TABLE dbo.Token(
	id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	hash CHAR(64) UNIQUE,	--64 hexa chars = 256 bits hash
	isValid BOOLEAN,
	issuedAt BIGINT,
	expiresAt BIGINT,
	claims JSONB
);

CREATE TABLE dbo.scopes(
	id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	scope VARCHAR(100) UNIQUE --urn:org:ionproject:scopes:api:read
);

CREATE TABLE dbo.policies(
	id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	scope_id INT REFERENCES dbo.scopes(id),
	method VARCHAR(10),		-- get, post...
	version VARCHAR(10),	--	v0, v1...
	path VARCHAR(100)		-- .../courses
);