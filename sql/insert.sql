insert into dbo.Programme(acronym, name, termSize) values
	('LEIC', 'licenciatura eng. inf.', 6),
	('MEIC', 'mestrado eng. inf.', 4);

insert into dbo.CalendarTerm(id, start_date, end_date) values
	( '1718v', null, null ),
	( '1718i', null, null ),
	( '1819v', null, null ),
	( '1819i', null, null ),
	( '1920v', null, null ),
	( '1920i', null, null ),
	( '2021v', null, null ),
	( '2021i', null, null );

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
	
insert into dbo.ProgrammeOffer(programmeAcronym, courseAcronym, optional, termNumber, credits) VALUES 
	('LEIC', 'WAD', TRUE, 3, 5),
	('LEIC', 'SL', FALSE, 4,6),
	('LEIC', 'DM', FALSE, 1, 5);

--Significado: offerId 1 (WAD) é dependente de SL e DM
insert into dbo.OfferDependencies(offerId, courseAcronym) VALUES --WAD 1, SL 2, DM 3
	(1, 'SL'),
	(1, 'DM');


insert into dbo.Class(term, course) values
	( '1718v', 'SL'  ),
	( '1718v', 'WAD' ),
	( '1718v', 'DM'  ),
	( '1718i', 'SL'  ),
	( '1718i', 'WAD' ),
	( '1718i', 'DM'  ),
	( '1819v', 'SL'  ),
	( '1819v', 'WAD' ),
	( '1819v', 'DM'  ),
	( '1819i', 'SL'  ),
	( '1819i', 'WAD' ),
	( '1819i', 'DM'  ),
	( '1920v', 'SL'  ),
	( '1920v', 'WAD' ),
	( '1920v', 'DM'  ),
	( '1920i', 'SL'  ),
	( '1920i', 'WAD' ),
	( '1920i', 'DM'  ),
	( '2021v', 'SL'  ),
	( '2021v', 'WAD' ),
	( '2021v', 'DM'  ),
	( '2021i', 'SL'  ),
	( '2021i', 'WAD' ),
	( '2021i', 'DM'  );

insert into dbo.ClassSection(term, course, id) values
	( '1718v', 'SL', '1D'  ),
	( '1718v', 'SL', '2D'  ),
	( '1718v', 'SL', '1N'  ),
	( '1718v', 'WAD', '1D' ),
	( '1718v', 'WAD', '2D' ),
	( '1718v', 'WAD', '1N' ),
	( '1718v', 'DM', '1D' ),
	( '1718v', 'DM', '2D' ),
	( '1718v', 'DM', '1N' ),
	( '1718i', 'SL', '1D' ),
	( '1718i', 'SL', '2D' ),
	( '1718i', 'SL', '1N' ),
	( '1718i', 'WAD', '1D' ),
	( '1718i', 'WAD', '2D' ),
	( '1718i', 'WAD', '1N' ),
	( '1718i', 'DM', '1D'  ),
	( '1718i', 'DM', '2D'  ),
	( '1718i', 'DM', '1N'  ),
	( '1819v', 'SL', '1D'  ),
	( '1819v', 'SL', '2D'  ),
	( '1819v', 'SL', '1N'  ),
	( '1819v', 'WAD', '1D' ),
	( '1819v', 'WAD', '2D' ),
	( '1819v', 'WAD', '1N' ),
	( '1819v', 'DM', '1D'  ),
	( '1819v', 'DM', '2D'  ),
	( '1819v', 'DM', '1N'  ),
	( '1819i', 'SL', '1D'  ),
	( '1819i', 'SL', '2D'  ),
	( '1819i', 'SL', '1N'  ),
	( '1819i', 'WAD', '1D' ),
	( '1819i', 'WAD', '2D' ),
	( '1819i', 'WAD', '1N' ),
	( '1819i', 'DM', '1D'  ),
	( '1819i', 'DM', '2D'  ),
	( '1819i', 'DM', '1N'  ),
	( '1920v', 'SL', '1D'  ),
	( '1920v', 'SL', '2D'  ),
	( '1920v', 'SL', '1N'  ),
	( '1920v', 'WAD', '1D' ),
	( '1920v', 'WAD', '2D' ),
	( '1920v', 'WAD', '1N' ),
	( '1920v', 'DM', '1D'  ),
	( '1920v', 'DM', '2D'  ),
	( '1920v', 'DM', '1N'  ),
	( '1920i', 'SL', '1D'  ),
	( '1920i', 'SL', '2D'  ),
	( '1920i', 'SL', '1N'  ),
	( '1920i', 'WAD', '1D' ),
	( '1920i', 'WAD', '2D' ),
	( '1920i', 'WAD', '1N' ),
	( '1920i', 'DM', '1D'  ),
	( '1920i', 'DM', '2D'  ),
	( '1920i', 'DM', '1N'  ),
	( '2021v', 'SL', '1D'  ),
	( '2021v', 'SL', '2D'  ),
	( '2021v', 'SL', '1N'  ),
	( '2021v', 'WAD', '1D' ),
	( '2021v', 'WAD', '2D' ),
	( '2021v', 'WAD', '1N' ),
	( '2021v', 'DM', '1D'  ),
	( '2021v', 'DM', '2D'  ),
	( '2021v', 'DM', '1N'  ),
	( '2021i', 'SL', '1D'  ),
	( '2021i', 'SL', '2D'  ),
	( '2021i', 'SL', '1N'  ),
	( '2021i', 'WAD', '1D' ),
	( '2021i', 'WAD', '2D' ),
	( '2021i', 'WAD', '1N' ),
	( '2021i', 'DM', '1D'  ),
	( '2021i', 'DM', '2D'  ),
	( '2021i', 'DM', '1N'  );
