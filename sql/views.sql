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
drop procedure if exists dbo.sp_classCalendarCreate;
drop view if exists dbo.v_classCalendar;
--drop trigger if exists dbo.t_classCalendarCreate;

create view dbo.v_classCalendar as
	select Cl.courseid, Cl.term, Cal.id as calendarid from
	dbo.Class as Cl join dbo.Calendar as Cal on Cl.calendar=Cal.id;

create procedure dbo.sp_classCalendarCreate (courseid integer, calterm varchar(200))
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

--create trigger dbo.t_classCalendarCreate instead of insert
--	on dbo.v_classCalendar
--	execute procedure dbo.sp_classCalendarCreate ;

-- When creating a ClassSection, give it a new Calendar 
