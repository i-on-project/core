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
