drop table if exists dbo.ClassSection;
drop table if exists dbo.Class;
drop table if exists dbo.Course;
drop table if exists dbo.Term;

create table dbo.Term (
	value varchar(10) primary key
);

create table dbo.Course (
	acronym varchar(5) primary key,
	name    varchar(100) unique not null
);

create table dbo.Class (
	course varchar(5) references dbo.Course(acronym),
	term   varchar(10) references dbo.Term(value),
	primary key(course, term)
);

create table dbo.ClassSection (
	id     varchar(5),
	course varchar(5),
	term   varchar(10),
	foreign key (course, term) references dbo.Class(course, term),
	primary key(id, course, term)
);



