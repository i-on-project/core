DROP VIEW IF EXISTS courseWithTerm;
CREATE VIEW courseWithTerm AS 
	SELECT co.*, cl.term FROM 
		(SELECT coI.id,coI.acronym,coI.name FROM dbo.Course AS coI 
			INNER JOIN dbo.Class AS clI ON coI.id=clI.courseId GROUP BY coI.id) AS co
		INNER JOIN
		(SELECT DISTINCT ON (clI.courseId) clI.courseId,clI.term, ctI.start_date FROM dbo.class AS clI
                        INNER JOIN dbo.CalendarTerm AS ctI ON clI.term=ctI.id 
				ORDER BY clI.courseId,ctI.start_date DESC) AS cl
		ON co.id=cl.courseId
