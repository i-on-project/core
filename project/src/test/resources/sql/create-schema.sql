CREATE SCHEMA dbo; --POSTGRES ALREADY USES 'public' SCHEMA BY DEFAULT

create table dbo.Programme(
	id   		INT generated always as identity primary key,
	acronym 	VARCHAR(10) unique,				
	name		VARCHAR(100) UNIQUE,			-- It may be null in this phase
	termSize	INT					
);

create table dbo.Calendar (
	id   int generated always as identity primary key
);

create table dbo.Course (
	id   	 INT generated always as identity primary key,
	acronym  varchar(10) unique,	
	name     varchar(100) unique				-- It may be null in this phase
);


create table dbo.ProgrammeOffer(
	id   	 		INT generated always as identity,
	programmeId 		INT REFERENCES dbo.Programme(id),
	courseId 		INT REFERENCES dbo.Course(id),
	termNumber		INT, 
	optional		BOOLEAN,
	PRIMARY KEY(programmeId, courseId, termNumber)
);

create table dbo.CalendarTerm (
	id         varchar(10) primary key, -- e.g. "1920v"
	start_date timestamp,
	end_date   timestamp check(end_date > start_date),
	unique(start_date, end_date)
);

create table dbo.Class (
	courseId INT references dbo.Course(id),
	term     varchar(10) references dbo.CalendarTerm(id),
	calendar int references dbo.Calendar(id),
	primary key(courseId, term)
);

create table dbo.ClassSection (
	id       varchar(10),
	courseId   INT,
	term     varchar(10),
	calendar int references dbo.Calendar(id),
	foreign key(courseId, term) references dbo.Class(courseId, term),
	primary key(id, courseId, term)
);


------------- CALENDAR ----------------
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
	PRIMARY KEY (comp_id, byday)
);

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
    language       INT REFERENCES dbo.Language(id),
	PRIMARY KEY(comp_id, value, language)
);

CREATE TABLE dbo.Summary (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    value          VARCHAR(50) NOT NULL,
    language       INT REFERENCES dbo.Language(id),
	PRIMARY KEY(comp_id, value, language)
);

CREATE TABLE dbo.Attachment (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    value          VARCHAR(128) NOT NULL,
	PRIMARY KEY(comp_id, value)
);

CREATE TABLE dbo.Due (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    type           INT REFERENCES dbo.ICalendarDataType(id),
    value          TIMESTAMP,
	PRIMARY KEY(comp_id)
);

CREATE TABLE dbo.Dtend (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    type           INT REFERENCES dbo.ICalendarDataType(id),
    value          TIMESTAMP,
	PRIMARY KEY(comp_id)
);

CREATE TABLE dbo.Dtstart (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    type           INT REFERENCES dbo.ICalendarDataType(id),
    value          TIMESTAMP,
	PRIMARY KEY(comp_id)
);

CREATE TABLE dbo.Categories (
    comp_id        INT REFERENCES dbo.CalendarComponent(id),
    value          INT REFERENCES dbo.Category(id),
	PRIMARY KEY(comp_id, value)
);

-- component views
CREATE VIEW dbo.v_ComponentsCommon AS
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

CREATE VIEW dbo.v_ComponentsAll AS
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

CREATE VIEW dbo.v_Todo AS
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

CREATE VIEW dbo.v_Event AS
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

CREATE VIEW dbo.v_Journal AS
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

---------------------- VIEWS -----------------------

DROP VIEW IF EXISTS courseWithTerm;
CREATE VIEW courseWithTerm AS
	SELECT co.*, cl.term FROM
		(SELECT coI.id,coI.acronym,coI.name FROM dbo.Course AS coI
			INNER JOIN dbo.Class AS clI ON coI.id=clI.courseId GROUP BY coI.id) AS co
		INNER JOIN
		(SELECT DISTINCT ON (clI.courseId) clI.courseId,clI.term, ctI.start_date FROM dbo.class AS clI
                        INNER JOIN dbo.CalendarTerm AS ctI ON clI.term=ctI.id
				ORDER BY clI.courseId,ctI.start_date DESC) AS cl
		ON co.id=cl.courseId;

-- When creating a Class, give it a new Calendar
create or replace procedure dbo.sp_classCalendarCreate (calterm varchar(200), courseid integer)
AS $$
#print_strict_params on
DECLARE
calid int;
BEGIN
	insert into dbo.Calendar values (default) returning id into calid;
	insert into dbo.Class(courseid, term, calendar) values
		(courseid, calterm, calid);
END
$$ LANGUAGE plpgsql;

-- When creating a ClassSection, give it a new Calendar
create or replace procedure dbo.sp_classSectionCalendarCreate (calterm varchar(200), courseid integer, sid varchar(200))
AS $$
#print_strict_params on
DECLARE
calid int;
BEGIN
	insert into dbo.Calendar values (default) returning id into calid;
	insert into dbo.ClassSection(id, courseid, term, calendar) values
		(sid, courseid, calterm, calid);
END
$$ LANGUAGE plpgsql;

CREATE PROCEDURE dbo.newEvent(
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
CREATE PROCEDURE dbo.newTodo(
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
CREATE PROCEDURE dbo.newJournal(
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

CREATE FUNCTION MERGE_LANGUAGE_TEXT(language INT, text VARCHAR)
RETURNS VARCHAR
LANGUAGE PLpgSQL AS $$
BEGIN
	RETURN language || ':' || text;
END;
$$