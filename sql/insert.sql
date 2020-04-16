insert into dbo.Programme(acronym, name, termSize) values
	('LEIC', 'licenciatura eng. inf.', 6),
	('MEIC', 'mestrado eng. inf.', 4);

insert into dbo.CalendarTerm(id, start_date, end_date) values
	( '1718v', to_timestamp(1586379923), null ),
	( '1718i', to_timestamp(1586379924), null ),
	( '1819v', to_timestamp(1586379925), null ),
	( '1819i', to_timestamp(1586379927), null ),
	( '1920v', to_timestamp(1586379929), null ),
	( '1920i', to_timestamp(1586379930), null ),
	( '2021v', to_timestamp(1586379940), null ),
	( '2021i', to_timestamp(1586379950), null );

insert into dbo.Calendar values (default);
insert into dbo.Calendar values (default);
insert into dbo.Calendar values (default);
insert into dbo.Calendar values (default);
insert into dbo.Calendar values (default);
insert into dbo.Calendar values (default);

insert into dbo.Course(acronym, name) values
	( 'SL', 'Software Laboratory' ),
	( 'WAD', 'Web Applications Development' ),
	( 'DM', 'Discrete Mathematics' );
	
insert into dbo.ProgrammeOffer(programmeId, courseId, optional, termNumber) VALUES 
	(1, 2, TRUE, 3),
	(1, 1, FALSE, 4),
	(1, 3, FALSE, 1);

insert into dbo.Class(term, courseId) values
	( '1718v', 1  ),
	( '1718v', 2 ),
	( '1718v', 3  ),
	( '1718i', 1  ),
	( '1718i', 2 ),
	( '1718i', 3  ),
	( '1819v', 1  ),
	( '1819v', 2 ),
	( '1819v', 3  ),
	( '1819i', 1  ),
	( '1819i', 2 ),
	( '1819i', 3  ),
	( '1920v', 1  ),
	( '1920v', 2 ),
	( '1920v', 3  ),
	( '1920i', 1  ),
	( '1920i', 2 ),
	( '1920i', 3  ),
	( '2021v', 1  ),
	( '2021v', 2 ),
	( '2021v', 3  ),
	( '2021i', 1  ),
	( '2021i', 2 ),
	( '2021i', 3  );

insert into dbo.ClassSection(term, courseId, id) values
	( '1718v', 1, '1D'  ),
	( '1718v', 1, '2D'  ),
	( '1718v', 1, '1N'  ),
	( '1718v', 2, '1D' ),
	( '1718v', 2, '2D' ),
	( '1718v', 2, '1N' ),
	( '1718v', 3, '1D' ),
	( '1718v', 3, '2D' ),
	( '1718v', 3, '1N' ),
	( '1718i', 1, '1D' ),
	( '1718i', 1, '2D' ),
	( '1718i', 1, '1N' ),
	( '1718i', 2, '1D' ),
	( '1718i', 2, '2D' ),
	( '1718i', 2, '1N' ),
	( '1718i', 3, '1D'  ),
	( '1718i', 3, '2D'  ),
	( '1718i', 3, '1N'  ),
	( '1819v', 1, '1D'  ),
	( '1819v', 1, '2D'  ),
	( '1819v', 1, '1N'  ),
	( '1819v', 2, '1D' ),
	( '1819v', 2, '2D' ),
	( '1819v', 2, '1N' ),
	( '1819v', 3, '1D'  ),
	( '1819v', 3, '2D'  ),
	( '1819v', 3, '1N'  ),
	( '1819i', 1, '1D'  ),
	( '1819i', 1, '2D'  ),
	( '1819i', 1, '1N'  ),
	( '1819i', 2, '1D' ),
	( '1819i', 2, '2D' ),
	( '1819i', 2, '1N' ),
	( '1819i', 3, '1D'  ),
	( '1819i', 3, '2D'  ),
	( '1819i', 3, '1N'  ),
	( '1920v', 1, '1D'  ),
	( '1920v', 1, '2D'  ),
	( '1920v', 1, '1N'  ),
	( '1920v', 2, '1D' ),
	( '1920v', 2, '2D' ),
	( '1920v', 2, '1N' ),
	( '1920v', 3, '1D'  ),
	( '1920v', 3, '2D'  ),
	( '1920v', 3, '1N'  ),
	( '1920i', 1, '1D'  ),
	( '1920i', 1, '2D'  ),
	( '1920i', 1, '1N'  ),
	( '1920i', 2, '1D' ),
	( '1920i', 2, '2D' ),
	( '1920i', 2, '1N' ),
	( '1920i', 3, '1D'  ),
	( '1920i', 3, '2D'  ),
	( '1920i', 3, '1N'  ),
	( '2021v', 1, '1D'  ),
	( '2021v', 1, '2D'  ),
	( '2021v', 1, '1N'  ),
	( '2021v', 2, '1D' ),
	( '2021v', 2, '2D' ),
	( '2021v', 2, '1N' ),
	( '2021v', 3, '1D'  ),
	( '2021v', 3, '2D'  ),
	( '2021v', 3, '1N'  ),
	( '2021i', 1, '1D'  ),
	( '2021i', 1, '2D'  ),
	( '2021i', 1, '1N'  ),
	( '2021i', 2, '1D' ),
	( '2021i', 2, '2D' ),
	( '2021i', 2, '1N' ),
	( '2021i', 3, '1D'  ),
	( '2021i', 3, '2D'  ),
	( '2021i', 3, '1N'  );

INSERT INTO dbo.CalendarComponent(cid, type, summary, description, dtstart, dtend) VALUES
(1, 'E', '1st exam WAD', 'Normal season exam for WAD-1920v', TIMESTAMP '2020-06-19 14:00:00', TIMESTAMP '2020-06-19 16:30:00');