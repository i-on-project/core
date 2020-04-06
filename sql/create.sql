CREATE SCHEMA dbo; --POSTGRES ALREADY USES 'public' SCHEMA BY DEFAULT

create table dbo.Programme(
	id   		INT generated always as identity primary key
	acronym 	VARCHAR(10),				
	name		VARCHAR(100) UNIQUE,			-- It may be null in this phase
	termSize	INT					
);

create table dbo.Calendar (
	id   int generated always as identity primary key
);

create table dbo.Course (
	id   		INT generated always as identity primary key
	acronym  varchar(10),	
	name     varchar(100) unique,				-- It may be null in this phase
);


create table dbo.ProgrammeOffer(
	programmeAcronym	VARCHAR(10) REFERENCES dbo.Programme(acronym),
	courseAcronym 		VARCHAR(10) REFERENCES dbo.Course(acronym),
	termNumber		INT, 
	optional		BOOLEAN,
	PRIMARY KEY(programmeAcronym, courseAcronym, termNumber)
);

create table dbo.CalendarTerm (
	id         varchar(10) primary key, -- e.g. "1920v"
	start_date timestamp,
	end_date   timestamp check(end_date > start_date),
	unique(start_date, end_date)
);

create table dbo.Class (
	course   varchar(10) references dbo.Course(acronym),
	term     varchar(10) references dbo.CalendarTerm(id),
	calendar int references dbo.Calendar(id),
	primary key(course, term)
);

create table dbo.ClassSection (
	id       varchar(10),
	course   varchar(10),
	term     varchar(10),
	calendar int references dbo.Calendar(id),
	foreign key(course, term) references dbo.Class(course, term),
	primary key(id, course, term)
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
