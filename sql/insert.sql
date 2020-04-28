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

call dbo.sp_classSectionCalendarCreate('1718v', 1, '1D');
call dbo.sp_classSectionCalendarCreate('1718v', 1, '2D');
call dbo.sp_classSectionCalendarCreate('1718v', 1, '1N');
call dbo.sp_classSectionCalendarCreate('1718v', 2, '1D');
call dbo.sp_classSectionCalendarCreate('1718v', 2, '2D');
call dbo.sp_classSectionCalendarCreate('1718v', 2, '1N');
call dbo.sp_classSectionCalendarCreate('1718v', 3, '1D');
call dbo.sp_classSectionCalendarCreate('1718v', 3, '2D');
call dbo.sp_classSectionCalendarCreate('1718v', 3, '1N');
call dbo.sp_classSectionCalendarCreate('1718i', 1, '1D');
call dbo.sp_classSectionCalendarCreate('1718i', 1, '2D');
call dbo.sp_classSectionCalendarCreate('1718i', 1, '1N');
call dbo.sp_classSectionCalendarCreate('1718i', 2, '1D');
call dbo.sp_classSectionCalendarCreate('1718i', 2, '2D');
call dbo.sp_classSectionCalendarCreate('1718i', 2, '1N');
call dbo.sp_classSectionCalendarCreate('1718i', 3, '1D');
call dbo.sp_classSectionCalendarCreate('1718i', 3, '2D');
call dbo.sp_classSectionCalendarCreate('1718i', 3, '1N');
call dbo.sp_classSectionCalendarCreate('1819v', 1, '1D');
call dbo.sp_classSectionCalendarCreate('1819v', 1, '2D');
call dbo.sp_classSectionCalendarCreate('1819v', 1, '1N');
call dbo.sp_classSectionCalendarCreate('1819v', 2, '1D');
call dbo.sp_classSectionCalendarCreate('1819v', 2, '2D');
call dbo.sp_classSectionCalendarCreate('1819v', 2, '1N');
call dbo.sp_classSectionCalendarCreate('1819v', 3, '1D');
call dbo.sp_classSectionCalendarCreate('1819v', 3, '2D');
call dbo.sp_classSectionCalendarCreate('1819v', 3, '1N');
call dbo.sp_classSectionCalendarCreate('1819i', 1, '1D');
call dbo.sp_classSectionCalendarCreate('1819i', 1, '2D');
call dbo.sp_classSectionCalendarCreate('1819i', 1, '1N');
call dbo.sp_classSectionCalendarCreate('1819i', 2, '1D');
call dbo.sp_classSectionCalendarCreate('1819i', 2, '2D');
call dbo.sp_classSectionCalendarCreate('1819i', 2, '1N');
call dbo.sp_classSectionCalendarCreate('1819i', 3, '1D');
call dbo.sp_classSectionCalendarCreate('1819i', 3, '2D');
call dbo.sp_classSectionCalendarCreate('1819i', 3, '1N');
call dbo.sp_classSectionCalendarCreate('1920v', 1, '1D');
call dbo.sp_classSectionCalendarCreate('1920v', 1, '2D');
call dbo.sp_classSectionCalendarCreate('1920v', 1, '1N');
call dbo.sp_classSectionCalendarCreate('1920v', 2, '1D');
call dbo.sp_classSectionCalendarCreate('1920v', 2, '2D');
call dbo.sp_classSectionCalendarCreate('1920v', 2, '1N');
call dbo.sp_classSectionCalendarCreate('1920v', 3, '1D');
call dbo.sp_classSectionCalendarCreate('1920v', 3, '2D');
call dbo.sp_classSectionCalendarCreate('1920v', 3, '1N');
call dbo.sp_classSectionCalendarCreate('1920i', 1, '1D');
call dbo.sp_classSectionCalendarCreate('1920i', 1, '2D');
call dbo.sp_classSectionCalendarCreate('1920i', 1, '1N');
call dbo.sp_classSectionCalendarCreate('1920i', 2, '1D');
call dbo.sp_classSectionCalendarCreate('1920i', 2, '2D');
call dbo.sp_classSectionCalendarCreate('1920i', 2, '1N');
call dbo.sp_classSectionCalendarCreate('1920i', 3, '1D');
call dbo.sp_classSectionCalendarCreate('1920i', 3, '2D');
call dbo.sp_classSectionCalendarCreate('1920i', 3, '1N');
call dbo.sp_classSectionCalendarCreate('2021v', 1, '1D');
call dbo.sp_classSectionCalendarCreate('2021v', 1, '2D');
call dbo.sp_classSectionCalendarCreate('2021v', 1, '1N');
call dbo.sp_classSectionCalendarCreate('2021v', 2, '1D');
call dbo.sp_classSectionCalendarCreate('2021v', 2, '2D');
call dbo.sp_classSectionCalendarCreate('2021v', 2, '1N');
call dbo.sp_classSectionCalendarCreate('2021v', 3, '1D');
call dbo.sp_classSectionCalendarCreate('2021v', 3, '2D');
call dbo.sp_classSectionCalendarCreate('2021v', 3, '1N');
call dbo.sp_classSectionCalendarCreate('2021i', 1, '1D');
call dbo.sp_classSectionCalendarCreate('2021i', 1, '2D');
call dbo.sp_classSectionCalendarCreate('2021i', 1, '1N');
call dbo.sp_classSectionCalendarCreate('2021i', 2, '1D');
call dbo.sp_classSectionCalendarCreate('2021i', 2, '2D');
call dbo.sp_classSectionCalendarCreate('2021i', 2, '1N');
call dbo.sp_classSectionCalendarCreate('2021i', 3, '1D');
call dbo.sp_classSectionCalendarCreate('2021i', 3, '2D');
call dbo.sp_classSectionCalendarCreate('2021i', 3, '1N');
