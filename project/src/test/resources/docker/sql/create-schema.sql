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
	document   TSVECTOR GENERATED ALWAYS AS (to_tsvector('english', id)) STORED,
	UNIQUE(start_date, end_date)
);

CREATE TABLE dbo.Class (
	id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	courseId        INT REFERENCES dbo.Course(id),
	calendarTerm    VARCHAR(20) REFERENCES dbo.CalendarTerm(id),
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

CREATE TABLE dbo.RecurrenceRule (
	comp_id     	INT REFERENCES dbo.CalendarComponent(id) ON DELETE CASCADE,
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

CREATE TABLE IF NOT EXISTS dbo.Dtend (
    comp_id        INT REFERENCES dbo.CalendarComponent(id) ON DELETE CASCADE,
    type           INT REFERENCES dbo.ICalendarDataType(id),
    value          TIMESTAMP,
	PRIMARY KEY(comp_id)
);

CREATE TABLE IF NOT EXISTS dbo.Dtstart (
    comp_id        INT REFERENCES dbo.CalendarComponent(id) ON DELETE CASCADE,
    type           INT REFERENCES dbo.ICalendarDataType(id),
    value          TIMESTAMP,
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
  location VARCHAR(128),
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

    IF location IS NOT NULL THEN
        INSERT INTO dbo.Location(comp_id, value) VALUES
        (component_id, location);
    END IF;
	
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

-----------------------------------------------------
-- Write API

CREATE OR REPLACE PROCEDURE dbo.sp_createOrReplaceSchool (
  schoolName VARCHAR(100),
  schoolAcr VARCHAR(50),
  programmeName VARCHAR(100),
  programmeAcr VARCHAR(50),
  programmeTermSize INT,
  calendarTerm VARCHAR(50))
AS $$
#print_strict_params ON
BEGIN
  IF (schoolName IS NULL AND schoolAcr IS NULL) OR (programmeAcr IS NULL AND programmeName IS NULL) THEN
    RAISE 'For the School and Programme parameters, you must provide at least a "name" or an "acronym"';
  END IF;

  -- Insert or update Programme
  -- coalesce: if any of the supplied parameters is null, use the previous value of the column instead
  UPDATE
    dbo.programme
  SET
    name     = COALESCE(programmeName, name),
    acronym  = COALESCE(programmeAcr, acronym),
    termSize = COALESCE(programmeTermSize, termSize)
  WHERE
    name = programmeName OR acronym = programmeAcr;

  INSERT INTO
    dbo.Programme(name, acronym, termSize)
  SELECT
    programmeName, programmeAcr, programmeTermSize
  WHERE
    NOT EXISTS(
      SELECT id FROM dbo.Programme p
      WHERE p.name = programmeName OR p.acronym=programmeAcr
    );

  -- Insert or update CalendarTerm
  INSERT INTO
    dbo.CalendarTerm(id, start_date, end_date)
  SELECT
    calendarTerm, NULL, NULL
  WHERE
    NOT EXISTS(
      SELECT id FROM dbo.CalendarTerm c WHERE c.id=calendarTerm
    ) AND calendarTerm IS NOT NULL;

END
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE dbo.sp_createOrReplaceCourse (
  courseName VARCHAR(100),
  courseAcr VARCHAR(50),
  calendarSection VARCHAR(50),
  calTerm VARCHAR(50),
  lang VARCHAR(10))
AS $$
#print_strict_params ON
DECLARE
	clid INT;
	cid INT;
	cscal INT;
  langid INT;
BEGIN
  IF (courseName IS NULL AND courseAcr IS NULL) THEN
    RAISE 'For the Course parameters, you must provide at least a "name" or an "acronym"';
  END IF;

  SELECT id INTO langid FROM dbo.Language WHERE name = lang;
  IF (langid IS NULL) THEN
    RAISE '% language does not exist', lang;
  END IF;

  -- Insert or update Programme
  -- coalesce: if any of the supplied parameters is null, use the previous value of the column instead
  UPDATE
    dbo.course
  SET
    name     = COALESCE(courseName, name),
    acronym  = COALESCE(courseAcr, acronym)
  WHERE
    name = courseName OR acronym = courseAcr;

  INSERT INTO
    dbo.Course(name, acronym)
  SELECT
    courseName, courseAcr
  WHERE
    NOT EXISTS(
      SELECT id FROM dbo.course
      WHERE name = courseName OR acronym = courseAcr
    );

  SELECT id INTO cid FROM dbo.Course WHERE name = courseName OR acronym = courseAcr;

  -- Creating the Class
  PERFORM
    dbo.f_classCalendarCreate (calTerm, cid)
  WHERE
    NOT EXISTS(
      SELECT id FROM dbo.Class
      WHERE courseId = cid AND calendarTerm = calTerm
    );
    
  SELECT id INTO clid FROM dbo.Class WHERE courseId = cid AND calendarTerm = calTerm;

  -- Creating the ClassSection
  PERFORM
    dbo.f_classSectionCalendarCreate (clid, calendarSection)
  WHERE
    NOT EXISTS(
      SELECT id FROM dbo.ClassSection C
      WHERE C.classid = clid AND C.id = calendarSection
    );
    
  -- Store ClassSection's ID and Calendar
  SELECT
    calendar INTO cscal
  FROM
    dbo.ClassSection C
  WHERE
    C.classid = clid AND C.id = calendarSection;
  
  -- Deletes all of the Class Section's events
  DELETE FROM
    dbo.CalendarComponent
  WHERE id IN (
    SELECT
      ccs.comp_id
    FROM
      dbo.calendarcomponents ccs
    WHERE
      ccs.calendar_id = cscal
  ) AND type = 'E';

  RAISE NOTICE '% course, % class, % section, % language, % calendar', cid, clid, calendarSection, langid, cscal;
END
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE dbo.sp_createClassSectionEvent(
  courseName VARCHAR(100),
  courseAcr VARCHAR(50),
  calendarSection VARCHAR(50),
  calTerm VARCHAR(50),
  summary VARCHAR(50),
  description VARCHAR(200),
  lang VARCHAR(10),
  categid INTEGER,
  dtstart TIMESTAMP,
  dtend TIMESTAMP,
  week_days VARCHAR(20),
  location VARCHAR(128)
) AS $$
#print_strict_params ON
DECLARE
  calid INT;
  component_id INT;
  langid INT;
  dttype INT;
BEGIN

  -- get target calendar ID (the class section's calendar)
  SELECT
    CS.calendar INTO calid
  FROM
    dbo.Course C JOIN
    dbo.Class CL ON C.id = CL.courseid JOIN
    dbo.ClassSection CS ON CS.classid = CL.id
  WHERE
    ( C.acronym = courseAcr OR C.name = courseName )
    AND CL.calendarTerm = calTerm AND CS.id = calendarSection;

  SELECT L.id INTO langid FROM dbo.Language L WHERE L.name = lang; 

  -- get the DATE TIME icalendar type
  SELECT id INTO dttype FROM dbo.icalendardatatype WHERE name = 'DATE-TIME';

  raise notice '% cal, % category, % lang', calid, categid, langid;
	
  CALL dbo.newEvent(calid,
      ARRAY[summary],
      ARRAY[langid],
      ARRAY[description],
      ARRAY[langid],
      ARRAY[categid],
      dtstart,
      dtend,
      dttype, -- dtstart dtend type
      location,
      week_days, -- by day
      NULL -- until
  );
END
$$ LANGUAGE PLpgSQL;

