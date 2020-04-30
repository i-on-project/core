CREATE SCHEMA dbo; --POSTGRES ALREADY USES 'public' SCHEMA BY DEFAULT

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
	id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name            VARCHAR(10) UNIQUE NOT NULL, -- e.g. "1920v"
	start_date      TIMESTAMP NOT NULL,
	end_date        TIMESTAMP NOT NULL CHECK(end_date > start_date),
	UNIQUE(start_date, end_date)
);

CREATE TABLE dbo.Class (
	id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	courseId        INT REFERENCES dbo.Course(id),
	calendarTerm    INT REFERENCES dbo.CalendarTerm(id),
	calendar        INT REFERENCES dbo.Calendar(id) UNIQUE,
	UNIQUE(courseId, calendarTerm)
);

CREATE TABLE dbo.ClassSection (
	id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name            VARCHAR(10) NOT NULL,
	classId         INT REFERENCES dbo.Class(id),
	calendar        INT REFERENCES dbo.Calendar(id) UNIQUE
);

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
