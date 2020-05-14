insert into dbo.Programme(acronym, name, termSize) values
	('LEIC', 'licenciatura eng. inf.', 6),
	('MEIC', 'mestrado eng. inf.', 4);

insert into dbo.CalendarTerm(id, start_date, end_date) values
	( '1718v', to_timestamp(1586379923), to_timestamp(1586379933) ),
	( '1718i', to_timestamp(1586379924), to_timestamp(1586379933) ),
	( '1819v', to_timestamp(1586379925), to_timestamp(1586379933) ),
	( '1819i', to_timestamp(1586379927), to_timestamp(1586379933) ),
	( '1920v', to_timestamp(1586379929), to_timestamp(1586379933) ),
	( '1920i', to_timestamp(1586379930), to_timestamp(1586379943) ),
	( '2021v', to_timestamp(1586379940), to_timestamp(1586379953) ),
	( '2021i', to_timestamp(1586379950), to_timestamp(1586379963) );

insert into dbo.Course(acronym, name) values
	( 'SL', 'Software Laboratory' ),
	( 'WAD', 'Web Applications Development' ),
	( 'DM', 'Discrete Mathematics' );

	
insert into dbo.ProgrammeOffer(programmeId, courseId, optional, termNumber) VALUES 
	(1, 2, TRUE, 3),
	(1, 1, FALSE, 4),
	(1, 3, FALSE, 1);

call dbo.sp_classCalendarCreate('1718v', 1);
call dbo.sp_classCalendarCreate('1718v', 2);
call dbo.sp_classCalendarCreate('1718v', 3);
call dbo.sp_classCalendarCreate('1718i', 1);
call dbo.sp_classCalendarCreate('1718i', 2);
call dbo.sp_classCalendarCreate('1718i', 3);
call dbo.sp_classCalendarCreate('1819v', 1);
call dbo.sp_classCalendarCreate('1819v', 2);
call dbo.sp_classCalendarCreate('1819v', 3);
call dbo.sp_classCalendarCreate('1819i', 1);
call dbo.sp_classCalendarCreate('1819i', 2);
call dbo.sp_classCalendarCreate('1819i', 3);
call dbo.sp_classCalendarCreate('1920v', 1);
call dbo.sp_classCalendarCreate('1920v', 2);
call dbo.sp_classCalendarCreate('1920v', 3);
call dbo.sp_classCalendarCreate('1920i', 1);
call dbo.sp_classCalendarCreate('1920i', 2);
call dbo.sp_classCalendarCreate('1920i', 3);
call dbo.sp_classCalendarCreate('2021v', 1);
call dbo.sp_classCalendarCreate('2021v', 2);
call dbo.sp_classCalendarCreate('2021v', 3);
call dbo.sp_classCalendarCreate('2021i', 1);
call dbo.sp_classCalendarCreate('2021i', 2);
call dbo.sp_classCalendarCreate('2021i', 3);

call dbo.sp_classSectionCalendarCreate(1, '1D');
call dbo.sp_classSectionCalendarCreate(1, '2D');
call dbo.sp_classSectionCalendarCreate(1, '1N');
call dbo.sp_classSectionCalendarCreate(2, '1D');
call dbo.sp_classSectionCalendarCreate(2, '2D');
call dbo.sp_classSectionCalendarCreate(2, '1N');
call dbo.sp_classSectionCalendarCreate(3, '1D');
call dbo.sp_classSectionCalendarCreate(3, '2D');
call dbo.sp_classSectionCalendarCreate(3, '1N');
call dbo.sp_classSectionCalendarCreate(4, '1D');
call dbo.sp_classSectionCalendarCreate(4, '2D');
call dbo.sp_classSectionCalendarCreate(4, '1N');
call dbo.sp_classSectionCalendarCreate(5, '1D');
call dbo.sp_classSectionCalendarCreate(5, '2D');
call dbo.sp_classSectionCalendarCreate(5, '1N');
call dbo.sp_classSectionCalendarCreate(6, '1D');
call dbo.sp_classSectionCalendarCreate(6, '2D');
call dbo.sp_classSectionCalendarCreate(6, '1N');
call dbo.sp_classSectionCalendarCreate(7, '1D');
call dbo.sp_classSectionCalendarCreate(7, '2D');
call dbo.sp_classSectionCalendarCreate(7, '1N');
call dbo.sp_classSectionCalendarCreate(8, '1D');
call dbo.sp_classSectionCalendarCreate(8, '2D');
call dbo.sp_classSectionCalendarCreate(8, '1N');
call dbo.sp_classSectionCalendarCreate(9, '1D');
call dbo.sp_classSectionCalendarCreate(9, '2D');
call dbo.sp_classSectionCalendarCreate(9, '1N');
call dbo.sp_classSectionCalendarCreate(10, '1D');
call dbo.sp_classSectionCalendarCreate(10, '2D');
call dbo.sp_classSectionCalendarCreate(10, '1N');
call dbo.sp_classSectionCalendarCreate(11, '1D');
call dbo.sp_classSectionCalendarCreate(11, '2D');
call dbo.sp_classSectionCalendarCreate(11, '1N');
call dbo.sp_classSectionCalendarCreate(12, '1D');
call dbo.sp_classSectionCalendarCreate(12, '2D');
call dbo.sp_classSectionCalendarCreate(12, '1N');
call dbo.sp_classSectionCalendarCreate(13, '1D');
call dbo.sp_classSectionCalendarCreate(13, '2D');
call dbo.sp_classSectionCalendarCreate(13, '1N');
call dbo.sp_classSectionCalendarCreate(14, '1D');
call dbo.sp_classSectionCalendarCreate(14, '2D');
call dbo.sp_classSectionCalendarCreate(14, '1N');
call dbo.sp_classSectionCalendarCreate(15, '1D');
call dbo.sp_classSectionCalendarCreate(15, '2D');
call dbo.sp_classSectionCalendarCreate(15, '1N');
call dbo.sp_classSectionCalendarCreate(16, '1D');
call dbo.sp_classSectionCalendarCreate(16, '2D');
call dbo.sp_classSectionCalendarCreate(16, '1N');
call dbo.sp_classSectionCalendarCreate(17, '1D');
call dbo.sp_classSectionCalendarCreate(17, '2D');
call dbo.sp_classSectionCalendarCreate(17, '1N');
call dbo.sp_classSectionCalendarCreate(18, '1D');
call dbo.sp_classSectionCalendarCreate(18, '2D');
call dbo.sp_classSectionCalendarCreate(18, '1N');
call dbo.sp_classSectionCalendarCreate(19, '1D');
call dbo.sp_classSectionCalendarCreate(19, '2D');
call dbo.sp_classSectionCalendarCreate(19, '1N');
call dbo.sp_classSectionCalendarCreate(20, '1D');
call dbo.sp_classSectionCalendarCreate(20, '2D');
call dbo.sp_classSectionCalendarCreate(20, '1N');
call dbo.sp_classSectionCalendarCreate(21, '1D');
call dbo.sp_classSectionCalendarCreate(21, '2D');
call dbo.sp_classSectionCalendarCreate(21, '1N');
call dbo.sp_classSectionCalendarCreate(22, '1D');
call dbo.sp_classSectionCalendarCreate(22, '2D');
call dbo.sp_classSectionCalendarCreate(22, '1N');
call dbo.sp_classSectionCalendarCreate(23, '1D');
call dbo.sp_classSectionCalendarCreate(23, '2D');
call dbo.sp_classSectionCalendarCreate(23, '1N');
call dbo.sp_classSectionCalendarCreate(24, '1D');
call dbo.sp_classSectionCalendarCreate(24, '2D');
call dbo.sp_classSectionCalendarCreate(24, '1N');

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

CALL dbo.newJournal(1, ARRAY['some summary', 'uma sinopse'], ARRAY[2,1], ARRAY['this is a description'], ARRAY[2], ARRAY[1,2,3], ARRAY['https://www.google.com'], TIMESTAMP '2020-04-10 14:00:00', 1, TIMESTAMP '2020-05-11 16:26:30');
CALL dbo.newTodo(1, ARRAY['do the thing'], ARRAY[2], ARRAY['I have to do the thing soon enough'], ARRAY[2], ARRAY[1,2], ARRAY['https://www.google.com'], TIMESTAMP '2021-06-19 14:00:00', 1, TIMESTAMP '2020-05-11 16:26:30');
CALL dbo.newTodo(1, ARRAY['don''t do the thing'], ARRAY[2], ARRAY['I have to do the thing soon enough'], ARRAY[2], ARRAY[1,2], NULL, TIMESTAMP '2021-06-19 14:00:00', 1, TIMESTAMP '2020-05-11 16:26:30');
CALL dbo.newEvent(1, ARRAY['1st exam WAD'], ARRAY[2], ARRAY['Normal season exam for WAD-1920v'], ARRAY[2], ARRAY[3], TIMESTAMP '2020-06-19 14:00:00', TIMESTAMP '2020-06-19 15:00:00', 2, NULL, TIMESTAMP '2020-05-11 16:26:30');
CALL dbo.newJournal(25, ARRAY['uma sinopse'], ARRAY[1], ARRAY['this is a description'], ARRAY[2], ARRAY[1,2,3], ARRAY['https://www.example.com'], TIMESTAMP '2020-05-14 12:13:00', 1, TIMESTAMP '2020-05-11 16:26:30');