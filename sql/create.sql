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
	type		   char not null,
	summary        varchar(50) not null,
	description    varchar(200),
	dtstart        timestamp not null,
	dtend          timestamp,
	primary key (id)
);

create table dbo.RecurrenceRule (
	id         int,
	freq       varchar(20),
	byday      varchar(20),
	foreign key (id) references dbo.CalendarComponent(id),
	primary key (id, byday)
);
