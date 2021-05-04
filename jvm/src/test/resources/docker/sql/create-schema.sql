CREATE SCHEMA dbo; --POSTGRES ALREADY USES 'public' SCHEMA BY DEFAULT

------- PHY Model --------
CREATE TABLE dbo.Programme(
	id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	acronym         VARCHAR(10) UNIQUE,				
	name            VARCHAR(100) UNIQUE,			-- It may be NULL in this phase
	termSize        INT,
	document        TSVECTOR GENERATED ALWAYS AS (to_tsvector('english', coalesce(acronym, '') || ' ' || coalesce(name,''))) STORED
);

CREATE TABLE dbo.Calendar (
	id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
);

CREATE TABLE dbo.Course (
	id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	acronym         VARCHAR(10) UNIQUE,	
	name            VARCHAR(100) UNIQUE,				-- It may be NULL in this phase
	document        TSVECTOR GENERATED ALWAYS AS (to_tsvector('english', coalesce(acronym, '') || ' ' || coalesce(name,''))) STORED
);

CREATE TABLE dbo.ProgrammeOffer(
	id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	programmeId     INT REFERENCES dbo.Programme(id),
	courseId        INT REFERENCES dbo.Course(id),
	optional        BOOLEAN,
	UNIQUE(programmeId, courseId)
);

CREATE TABLE dbo.ProgrammeOfferTerm(
	offerId         INT REFERENCES dbo.ProgrammeOffer(id),
	termNumber      INT NOT NULL,
	PRIMARY KEY(offerId, termNumber)
);

CREATE TABLE dbo.Instant (
    id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    date            DATE,
    time            TIME
);

CREATE TABLE dbo._CalendarTerm (
	id         VARCHAR(20) PRIMARY KEY, -- e.g. "1920v"
	start_date INT REFERENCES dbo.Instant(id),
	end_date   INT REFERENCES dbo.Instant(id),
	-- CHECK((SELECT value FROM dbo.Instant WHERE id=end_date) > (SELECT value FROM dbo.Instant WHERE id=start_date)), -- This check is no longer possible with the adittion of instants
	document   TSVECTOR GENERATED ALWAYS AS (to_tsvector('english', id)) STORED,
	UNIQUE(start_date, end_date)
);

-- New view to maintain old functionality
CREATE VIEW dbo.CalendarTerm AS
    SELECT
        _CalendarTerm.id,
        start_instant.date + COALESCE(start_instant.time, TIME '00:00:00') as start_date,
        end_instant.date + COALESCE(end_instant.time, TIME '00:00:00') as end_date,
        document
    FROM dbo._CalendarTerm
    JOIN dbo.Instant start_instant ON start_date = start_instant.id
    JOIN dbo.Instant end_instant ON end_date = end_instant.id
    WHERE
        start_instant.date IS NOT NULL
        AND
        end_instant.date IS NOT NULL
    ;


CREATE TABLE dbo.Class (
	id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	courseId        INT REFERENCES dbo.Course(id),
	calendarTerm    VARCHAR(20) REFERENCES dbo._CalendarTerm(id),
	calendar        INT REFERENCES dbo.Calendar(id) UNIQUE,
	document        TSVECTOR GENERATED ALWAYS AS (to_tsvector('english', calendarTerm)) STORED,
	UNIQUE(courseId, calendarTerm)
);

CREATE TABLE dbo.ClassSection (
	id              VARCHAR(10),
	classId         INT REFERENCES dbo.Class(id),
	calendar        INT REFERENCES dbo.Calendar(id) UNIQUE,
	document        TSVECTOR GENERATED ALWAYS AS (to_tsvector('english', id)) STORED,
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
	comp_id         INT REFERENCES dbo.CalendarComponent(id) ON DELETE CASCADE,
	PRIMARY KEY (calendar_id, comp_id)
);

CREATE TABLE dbo._RecurrenceRule (
	comp_id     	INT REFERENCES dbo.CalendarComponent(id) ON DELETE CASCADE,
	freq            VARCHAR(20),
	byday           VARCHAR(20),
	until           INT REFERENCES dbo.Instant(id),
	PRIMARY KEY (comp_id, byday)
);

CREATE VIEW dbo.RecurrenceRule AS
    SELECT
        comp_id,
        freq,
        byday,
        date + COALESCE(time, TIME '00:00:00') as until
    FROM
        dbo._RecurrenceRule
    JOIN
        dbo.Instant ON id = until;

CREATE TABLE IF NOT EXISTS dbo.ICalendarDataType (
    id             INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name           VARCHAR(16) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS dbo.Language(
    id             INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name           VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS dbo.Category(
	id             INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS dbo.CategoryLanguage(
	category       INT REFERENCES dbo.Category(id),
	name           VARCHAR(64) NOT NULL,
    language       INT REFERENCES dbo.Language(id),
	PRIMARY KEY (category, language)
);

-- some iCal property types
CREATE TABLE IF NOT EXISTS dbo.Description (
    comp_id        INT REFERENCES dbo.CalendarComponent(id) ON DELETE CASCADE,
    value          VARCHAR(200),
    language       INT REFERENCES dbo.Language(id),
	PRIMARY KEY(comp_id, value, language)
);

CREATE TABLE IF NOT EXISTS dbo.Summary (
    comp_id        INT REFERENCES dbo.CalendarComponent(id) ON DELETE CASCADE,
    value          VARCHAR(50) NOT NULL,
    language       INT REFERENCES dbo.Language(id),
	PRIMARY KEY(comp_id, value, language)
);

CREATE TABLE IF NOT EXISTS dbo.Attachment (
    comp_id        INT REFERENCES dbo.CalendarComponent(id) ON DELETE CASCADE,
    value          VARCHAR(128) NOT NULL,
	PRIMARY KEY(comp_id, value)
);

CREATE TABLE IF NOT EXISTS dbo.Due (
    comp_id        INT REFERENCES dbo.CalendarComponent(id) ON DELETE CASCADE,
    type           INT REFERENCES dbo.ICalendarDataType(id),
    value          TIMESTAMP,
	PRIMARY KEY(comp_id)
);

CREATE TABLE IF NOT EXISTS dbo._Dtend (
    comp_id        INT REFERENCES dbo.CalendarComponent(id) ON DELETE CASCADE,
    type           INT REFERENCES dbo.ICalendarDataType(id),
    date           INT REFERENCES dbo.Instant(id),
    time           TIME,
	PRIMARY KEY(comp_id)
);

CREATE TABLE IF NOT EXISTS dbo._Dtstart (
    comp_id        INT REFERENCES dbo.CalendarComponent(id) ON DELETE CASCADE,
    type           INT REFERENCES dbo.ICalendarDataType(id),
    date           INT REFERENCES dbo.Instant(id),
    time           TIME,
	PRIMARY KEY(comp_id)
);

CREATE TABLE IF NOT EXISTS dbo.Categories (
    comp_id        INT REFERENCES dbo.CalendarComponent(id) ON DELETE CASCADE,
    value          INT REFERENCES dbo.Category(id),
	PRIMARY KEY(comp_id, value)
);

CREATE TABLE IF NOT EXISTS dbo.Location (
    comp_id        INT REFERENCES dbo.CalendarComponent(id) ON DELETE CASCADE,
    value          VARCHAR(128) NOT NULL
);

CREATE TABLE IF NOT EXISTS dbo.AuthClient(
    client_id      CHAR(36) PRIMARY KEY,
    client_name    VARCHAR(100) UNIQUE,
    client_url     VARCHAR(200) NULL
);

CREATE TABLE IF NOT EXISTS dbo.AuthUserScope(
    scope_id       VARCHAR(100) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS dbo.AuthRequest(
    auth_req_id    CHAR(36) PRIMARY KEY,
    secret_id      VARCHAR(100) UNIQUE,
    login_hint     VARCHAR(200) NULL,
    user_agent     VARCHAR(200),
    client_id      CHAR(36) REFERENCES dbo.AuthClient(client_id) ON DELETE CASCADE,
    ntf_method     VARCHAR(20),
    expires_on     TIMESTAMP DEFAULT NOW(),
    verified       BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS dbo.AuthRequestScope(
    id             INT GENERATED ALWAYS AS IDENTITY,
    auth_req_id    CHAR(36) REFERENCES dbo.AuthRequest(auth_req_id),
    scope_id       VARCHAR(100) REFERENCES dbo.AuthUserScope(scope_id)
);

CREATE TABLE IF NOT EXISTS dbo.UserAccount(
    user_id        CHAR(36) PRIMARY KEY,
    name           VARCHAR(100),
    email          VARCHAR(200) UNIQUE,
    created_at     TIMESTAMP DEFAULT NOW()
);

------- VIEWS --------

CREATE OR REPLACE VIEW dbo.Dtend AS
    SELECT
        comp_id,
        type,
        i.date + COALESCE(dt.time, i.time, TIME '00:00:00') as value
    FROM dbo._Dtend dt
    JOIN dbo.Instant i ON dt.date = i.id
    WHERE i.date IS NOT NULL;

CREATE OR REPLACE VIEW dbo.Dtstart AS
    SELECT
        comp_id,
        type,
        i.date + COALESCE(dt.time, i.time, TIME '00:00:00') as value
    FROM dbo._Dtstart dt
    JOIN dbo.Instant i ON dt.date = i.id
    WHERE i.date IS NOT NULL;


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
    WHERE
        NOT (  -- if is event and not a valid event don't include in the result
            Comp.type = 'E'
            AND
            (
                Comp.id NOT IN (SELECT comp_id FROM dbo.DtStart)
                OR
                Comp.id NOT IN (SELECT comp_id FROM dbo.DtEnd))
            )
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
        D.type AS due_value_data_type,
        L.value AS location
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
    LEFT JOIN
        dbo.Location AS L ON Comp.uid=L.comp_id
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
		RR.until,
        L.value AS location
    FROM 
		dbo.v_ComponentsCommon AS Comp
    JOIN 
		dbo.Dtstart as DS ON Comp.uid = DS.comp_id
	JOIN
		dbo.Dtend AS DE ON Comp.uid=DE.comp_id
    LEFT JOIN
		dbo.RecurrenceRule AS RR ON Comp.uid=RR.comp_id
    LEFT JOIN
        dbo.Location AS L ON Comp.uid=L.comp_id
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
CREATE OR REPLACE FUNCTION dbo.newEvent(
	cid INT,
	summary VARCHAR(50)[],
	summary_language INT[],
	description VARCHAR(200)[],
	description_language INT[],
	category INT[],
    location VARCHAR(128),
    stamp_time TIMESTAMP DEFAULT now()
) RETURNS INT AS $$
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

  IF location IS NOT NULL THEN
    INSERT INTO dbo.Location(comp_id, value) VALUES
      (component_id, location);
  END IF;

  RETURN component_id;
END
$$ LANGUAGE PLpgSQL;

CREATE OR REPLACE PROCEDURE dbo.newEventWithInstants(
	cid INT,
	summary VARCHAR(50)[],
	summary_language INT[],
	description VARCHAR(200)[],
	description_language INT[],
	category INT[],
	dtstart TIMESTAMP,
    dtend TIMESTAMP,
    dtstart_dtend_type INT,
    location VARCHAR(128),
    byday VARCHAR(20),
    until TIMESTAMP,
    stamp_time TIMESTAMP DEFAULT now()
) AS $$
#print_strict_params ON
DECLARE
	component_id INT;
	start_instant INT;
	end_instant INT;
	until_instant INT;
BEGIN

  SELECT dbo.newEvent(cid, summary, summary_language, description, description_language, category, location, stamp_time)
  INTO component_id;

  INSERT INTO dbo.Instant(date, time) VALUES (dtstart::DATE, dtstart::TIME) RETURNING id INTO start_instant;
  INSERT INTO dbo.Instant(date, time) VALUES (dtend::DATE, dtend::TIME) RETURNING id INTO end_instant;

  INSERT INTO dbo._Dtstart (comp_id, type, date, time) VALUES
    (component_id, dtstart_dtend_type, start_instant, null);

  INSERT INTO dbo._Dtend (comp_id, type, date, time) VALUES
    (component_id, dtstart_dtend_type, end_instant, null);

  IF byday IS NOT NULL THEN
    INSERT INTO dbo.Instant(date, time) VALUES (until::DATE, until::TIME) RETURNING id INTO until_instant;

    INSERT INTO dbo._RecurrenceRule(comp_id, freq, byday, until) VALUES
      (component_id, 'WEEKLY', byday, until_instant);
  END IF;

END
$$ LANGUAGE PLpgSQL;

CREATE OR REPLACE PROCEDURE dbo.newEventWithDateReferences(
	cid INT,
	summary VARCHAR(50)[],
	summary_language INT[],
	description VARCHAR(200)[],
	description_language INT[],
	category INT[],
	start_instant INT,
    start_time TIME,
    end_time TIME,
    dtstart_dtend_type INT,
    location VARCHAR(128),
    byday VARCHAR(20),
    until INT,
    stamp_time TIMESTAMP DEFAULT now()
) AS $$
#print_strict_params ON
DECLARE
	component_id INT;
BEGIN

  SELECT dbo.newEvent(cid, summary, summary_language, description, description_language, category, location, stamp_time)
  INTO component_id;

  INSERT INTO dbo._Dtstart (comp_id, type, date, time) VALUES
    (component_id, dtstart_dtend_type, start_instant, start_time);

  INSERT INTO dbo._Dtend (comp_id, type, date, time) VALUES
    (component_id, dtstart_dtend_type, start_instant, end_time);

  IF byday IS NOT NULL THEN
    INSERT INTO dbo._RecurrenceRule(comp_id, freq, byday, until) VALUES
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

CREATE VIEW dbo.courseWithTerm AS
	SELECT co.*, cl.calendarterm
	FROM dbo.Course co
	JOIN
        (
            SELECT DISTINCT ON (courseId) calendarTerm, courseId
            FROM dbo.Class
            INNER JOIN dbo.CalendarTerm ON Class.calendarTerm = CalendarTerm.id
            ORDER BY courseId, start_date DESC
        ) cl ON co.id=cl.courseId;

CREATE OR REPLACE FUNCTION dbo.f_classCalendarCreate (calterm VARCHAR(200), courseid INT)
RETURNS INT
AS $$
#print_strict_params on
DECLARE
calid INT;
classid INT;
BEGIN
	INSERT INTO dbo.Calendar VALUES (DEFAULT) returning id INTO calid;

	INSERT INTO dbo.Class(courseid, calendarterm, calendar) VALUES
  (courseid, calterm, calid) RETURNING id INTO classid;

  RETURN classid;
END
$$ LANGUAGE plpgsql;

-- When creating a ClassSection, give it a new Calendar 
CREATE OR REPLACE FUNCTION dbo.f_classSectionCalendarCreate (classId INT, sid VARCHAR(200))
RETURNS VARCHAR(200)
AS $$
#print_strict_params on
DECLARE
calid INT;
csid VARCHAR(200);
BEGIN
	INSERT INTO dbo.Calendar VALUES (DEFAULT) returning id INTO calid;

	INSERT INTO dbo.ClassSection(id, classId, calendar) VALUES
	(sid, classId, calid) RETURNING id INTO csid;

  RETURN csid;
END
$$ LANGUAGE plpgsql;

-----------------------------------------------------
-- Access manager 

CREATE TABLE dbo.Token(
	id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	hash CHAR(64) UNIQUE,	--64 hexa chars = 256 bits hash
	isValid BOOLEAN,
	issuedAt BIGINT,
	expiresAt BIGINT,
  derivedToken BOOLEAN DEFAULT FALSE,
  fatherHash CHAR(64) DEFAULT '', --used only if its a derived token
	claims JSONB
);

CREATE TABLE dbo.scopes(
	id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	scope VARCHAR(100) UNIQUE --urn:org:ionproject:scopes:api:read
);

CREATE TABLE dbo.policies(
	id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	scope_id INT REFERENCES dbo.scopes(id),
	method VARCHAR(50),		-- get, post...
	version VARCHAR(10),	--	v0, v1...
	resource VARCHAR(100),-- "getProgrammes", "getProgramme"
  UNIQUE(scope_id, method, version, resource)
);
