CREATE SCHEMA dbo; 
--Tive que criar um schema para poder usar o dbo,
--no entanto isso era desnecessário, postgres ja usa o
--schema 'public' por default sem ter que dar ao trabalho de 
--indexar. https://www.postgresql.org/docs/8.1/ddl-schemas.html 5.7.2

create table dbo.Programme(
	acronym 	VARCHAR(10) PRIMARY KEY,
	name		VARCHAR(100) UNIQUE NOT NULL,
	termSize	INT					--qual é um range aceitavel de termSizes?
);

create table dbo.Calendar (
	id   int generated always as identity primary key
);

create table dbo.Course (
	acronym  varchar(10) primary key,
	name     varchar(100) unique not null,
	calendar int references dbo.Calendar(id)
);

--No caso em que exista alguma regra que diz que a UC apenas
--pode ser oferecida APENAS no semestre 4 e 6 isso vai ficar mais complexo
--porque n existe a entidade curricularTerm,
--é um atributo single-value da tabela. 
create table dbo.ProgrammeOffer(
	id					INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	programmeAcronym	VARCHAR(10) REFERENCES dbo.Programme(acronym),
	courseAcronym 		VARCHAR(10) REFERENCES dbo.Course(acronym),
	optional			BOOLEAN,
	termNumber			INT, 				--INSTEAD OF trigger deve verificar se termNumber é >0 e <termSize de programme
	credits				INT					--qual é um range aceitavel de credits?
);

--Eu continuo a achar que a relação de precedencias/dependencias
--devia ser entre programmeOffer<->programmeOffer de N para N
--sem obrigatoriedade. é menos chaves compostas e a tabela offers
--tem que ter de qualquer maneira as UC's oferecidas...
create table dbo.OfferDependencies(			--Assumi que cada grafo de dependencias depende do programme
	id				INT GENERATED ALWAYS AS IDENTITY,
	offerId			INT REFERENCES dbo.ProgrammeOffer(id),
	courseAcronym 	VARCHAR(10) REFERENCES dbo.Course(acronym)
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
