insert into dbo.Programme(acronym, name, termSize) values
	('LEIC', 'licenciatura eng. inf.', 6),
	('MEIC', 'mestrado eng. inf.', 4);

insert into dbo.CalendarTerm(name, start_date, end_date) values
	('1718v', TIMESTAMP '2018-02-15 00:00:00', TIMESTAMP '2018-06-15 00:00:00'),
	('1718i', TIMESTAMP '2017-09-05 00:00:00', TIMESTAMP '2018-01-05 00:00:00'),
	('1819v', TIMESTAMP '2019-02-15 00:00:00', TIMESTAMP '2019-06-15 00:00:00'),
	('1819i', TIMESTAMP '2018-09-05 00:00:00', TIMESTAMP '2019-01-05 00:00:00'),
	('1920v', TIMESTAMP '2020-02-15 00:00:00', TIMESTAMP '2020-06-15 00:00:00'),
	('1920i', TIMESTAMP '2019-09-05 00:00:00', TIMESTAMP '2020-01-05 00:00:00'),
	('2021v', TIMESTAMP '2021-02-15 00:00:00', TIMESTAMP '2021-06-15 00:00:00'),
	('2021i', TIMESTAMP '2020-09-05 00:00:00', TIMESTAMP '2021-01-05 00:00:00');

insert into dbo.Calendar values (default);
insert into dbo.Calendar values (default);
insert into dbo.Calendar values (default);
insert into dbo.Calendar values (default);
insert into dbo.Calendar values (default);
insert into dbo.Calendar values (default);

insert into dbo.Course(acronym, name) values
	('SL', 'Software Laboratory'),
	('WAD', 'Web Applications Development'),
	('DM', 'Discrete Mathematics');
	
insert into dbo.ProgrammeOffer(programmeId, courseId, optional, termNumber) VALUES 
	(1, 2, TRUE, 3),
	(1, 1, FALSE, 4),
	(1, 3, FALSE, 1);

insert into dbo.Class(calendarTerm, courseId) values
	(1, 1),
	(1, 2),
	(1, 3),
	(2, 1),
	(2, 2),
	(2, 3),
	(3, 1),
	(3, 2),
	(3, 3),
	(4, 1),
	(4, 2),
	(4, 3),
	(5, 1),
	(5, 2),
	(5, 3),
	(6, 1),
	(6, 2),
	(6, 3),
	(7, 1),
	(7, 2),
	(7, 3),
	(8, 1),
	(8, 2),
	(8, 3);

insert into dbo.ClassSection(classId, name) values
	(1, '1D'),
	(1, '2D'),
	(1, '1N'),
	(2, '1D'),
	(2, '1N'),
	(2, '2D'),
	(3, '1D'),
	(3, '2D'),
	(3, '1N'),
	(4, '1D'),
	(4, '2D'),
	(4, '1N'),
	(5, '1D'),
	(5, '2D'),
	(5, '1N'),
	(6, '1D'),
	(6, '2D'),
	(6, '1N'),
	(7, '1D'),
	(7, '2D'),
	(7, '1N'),
	(8, '1D'),
	(8, '2D'),
	(8, '1N'),
	(9, '1D'),
	(9, '2D'),
	(9, '1N'),
	(10, '1D'),
	(10, '2D'),
	(10, '1N'),
	(11, '1D'),
	(11, '2D'),
	(11, '1N'),
	(12, '1D'),
	(12, '2D'),
	(12, '1N'),
	(13, '1D'),
	(13, '2D'),
	(13, '1N'),
	(14, '1D'),
	(14, '2D'),
	(14, '1N'),
	(15, '1D'),
	(15, '2D'),
	(15, '1N'),
	(16, '1D'),
	(16, '2D'),
	(16, '1N'),
	(17, '1D'),
	(17, '2D'),
	(17, '1N'),
	(18, '1D'),
	(18, '2D'),
	(18, '1N'),
	(19, '1D'),
	(19, '2D'),
	(19, '1N'),
	(20, '1D'),
	(20, '2D'),
	(20, '1N'),
	(21, '1D'),
	(21, '2D'),
	(21, '1N'),
	(22, '1D'),
	(22, '2D'),
	(22, '1N'),
	(23, '1D'),
	(23, '2D'),
	(23, '1N'),
	(24, '1D'),
	(24, '2D'),
	(24, '1N');

INSERT INTO dbo.Language(name) VALUES 
('pt-PT'),
('en-US'),
('en-GB');

INSERT INTO dbo.ICalendarDataType(name) VALUES
('DATE'),
('DATETIME');

INSERT INTO dbo.Category(name, language) VALUES
('EXAME', 1),
('EXAM', 2),
('EXAM', 3);

CALL dbo.newJournal(1, 'some summary', 'this is a description', 'https://www.google.com', 1, TIMESTAMP '2020-04-10 14:00:00');
-- CALL dbo.newTodo(1, 'do the thing', 'I have to do the thing soon enough', TIMESTAMP '2021-06-19 14:00:00');
--CALL dbo.newTodo(1, 'do another thing', 'another one', TIMESTAMP '2022-06-19 14:00:00');
--CALL dbo.newEvent(1, '1st exam WAD', 'Normal season exam for WAD-1920v', TIMESTAMP '2020-06-19 14:00:00', TIMESTAMP '2020-06-19 15:00:00');

