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

create table dbo.CalendarComponent (
	id             int generated always as identity unique not null,
	cid            int references dbo.Calendar(id),
	title          varchar(50) not null,
	message        varchar(200),
	start_date     timestamp not null,
	end_date       timestamp,
	primary key(id, cid)
);

create table dbo.RecurrenceRule (
	id         int,
	cid        int,
	freq       varchar(20),
	byday      varchar(20),
	start_time time,
	duration   interval,
	foreign key (id, cid) references dbo.CalendarComponent(id, cid),
	primary key (id, cid, byday)
);

-- views

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

