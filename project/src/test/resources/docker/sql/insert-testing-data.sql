INSERT INTO dbo.Programme(acronym, name, termSize) VALUES
('LEIC', 'licenciatura eng. inf.', 6),
('MEIC', 'mestrado eng. inf.',     4);

INSERT INTO dbo.Instant(date, time) VALUES
(DATE '2017-02-01', null),
(DATE '2017-06-10', null),
(DATE '2017-09-01', null),
(DATE '2017-12-22', null),
(DATE '2018-02-01', null),
(DATE '2018-06-10', null),
(DATE '2018-09-01', null),
(DATE '2018-12-22', null),
(DATE '2019-02-01', null),
(DATE '2019-06-10', null),
(DATE '2019-09-01', null),
(DATE '2019-12-22', null);

INSERT INTO dbo._CalendarTerm(id, start_date, end_date) VALUES
('1718v', 1, 2),
('1718i', 3, 4),
('1819v', 5, 6),
('1819i', 7, 8),
('1920v', 9, 10),
('1920i', 11, 12);

INSERT INTO dbo.Course(acronym, name) values
('SL',  'Software Laboratory'),
('WAD', 'Web Applications Development'),
('DM',  'Discrete Mathematics'),
('PS', 'Project and Seminary'),
('CC', 'Cloud computing');
	
-- Access Manager Mock Data for beta version
--Notes:
--exp: 1691121207 on first 2 tokens is around the year 2023 
--client id holds no meaning for now as there is no registration/authentication method
-- last 2 tokens are expired and revoked for testing purposes
INSERT INTO dbo.Token(hash,isValid,issuedAt,expiresAt,derivedToken,fatherHash,claims) VALUES
('a00ffe411bc611ca81e1bfd5cd862586d89ca3b3a02fccc8586b547396bf60aa', TRUE, 1591544539044, 1591544539045,FALSE,'',
'{"scope": "urn:org:ionproject:scopes:api:read"}'),
('1681e5591f1bd814d69c8cdc657a0752707aff4d82d8b94d2c85185a289058ea', FALSE, 1591544539044, 1691544539044,FALSE,'',
'{"scope": "urn:org:ionproject:scopes:api:write"}'),
('92f9640fb837bb369afe725941f3d54464ff3c19d25de31a188bca72348de2b2', TRUE, 1591544539044, 1691544539044,FALSE,'',
'{"scope": "urn:org:ionproject:scopes:api:read_restricted"}');

-- Tokens for use with a local testing database/server
-- In orger:
-- Read token: l7kowOOkliu21oXxNpuCyM47u2omkysxb8lv3qEhm5U
-- Write token: hfk0DXJ9LIPuhvrjDEmhYRv5Z0YRhOl1DMEEPIp42ok
-- Issue token: vUG-N_m_xVohFrnXcu2Jmt_KAeKfxQXV2LkLjJF4144
-- Revoke token: 5eN-N7muBGix6X0N8jfau7Ou-3KcNHPAGVZNGWQ6ryw
INSERT INTO dbo.Token(hash,isValid,issuedAt,expiresAt,derivedToken,fatherHash,claims) VALUES
('5f5efa16d66d290bb31667bfffd6d9e37776a862ef116cbaa415f650a7283c0e','t',1593338117608,9999999999999,FALSE,'','{"scope":"urn:org:ionproject:scopes:api:read"}'),
('58e46fa9f1441d4fe9798ce1015eeab9231aa66cdee7638177c33a1b64ab534c','t',1593338126153,9999999999999,FALSE,'','{"scope":"urn:org:ionproject:scopes:api:write"}'),
('1784968c5536e2ed449507982b9cc281017a0a74c41d96f02dab31bbb7c6138f','t',1593338132242,9999999999999,FALSE,'','{"scope":"urn:org:ionproject:scopes:token:issue"}'),
('d09e6277cb226dc7ed1bab94028461b2b1e3b693618a77ae76e049538be846ce','t',1593338132242,9999999999999,FALSE,'','{"scope":"urn:org:ionproject:scopes:api:revoke"}');


INSERT INTO dbo.ProgrammeOffer(programmeId, courseId, optional) VALUES
(1, 2, TRUE),
(1, 1, FALSE),
(1, 3, FALSE),
(1, 4, FALSE),
(1, 5, TRUE),
(2, 3, FALSE);

INSERT INTO dbo.ProgrammeOfferTerm(offerId, termNumber) VALUES
(1, 4),
(1, 6),
(2, 6),
(3, 1),
(4, 6),
(5, 6),
(6, 1);

SELECT dbo.f_classCalendarCreate('1718v', 1);
SELECT dbo.f_classCalendarCreate('1718v', 2);
SELECT dbo.f_classCalendarCreate('1718v', 3);
SELECT dbo.f_classCalendarCreate('1718i', 1);
SELECT dbo.f_classCalendarCreate('1718i', 2);
SELECT dbo.f_classCalendarCreate('1718i', 3);
SELECT dbo.f_classCalendarCreate('1819v', 1);
SELECT dbo.f_classCalendarCreate('1819v', 2);
SELECT dbo.f_classCalendarCreate('1819v', 3);
SELECT dbo.f_classCalendarCreate('1819i', 1);
SELECT dbo.f_classCalendarCreate('1819i', 2);
SELECT dbo.f_classCalendarCreate('1819i', 3);
SELECT dbo.f_classCalendarCreate('1920v', 2);	-- calendar nº13 WAD
SELECT dbo.f_classCalendarCreate('1920v', 4);	-- calendar nº14 PS
SELECT dbo.f_classCalendarCreate('1920v', 5);	-- calendar nº15 CC
SELECT dbo.f_classCalendarCreate('1920i', 1);
SELECT dbo.f_classCalendarCreate('1920i', 2);
SELECT dbo.f_classCalendarCreate('1920i', 3);

SELECT dbo.f_classSectionCalendarCreate(1,  '1D');
SELECT dbo.f_classSectionCalendarCreate(1,  '2D');
SELECT dbo.f_classSectionCalendarCreate(1,  '1N');
SELECT dbo.f_classSectionCalendarCreate(2,  '1D');
SELECT dbo.f_classSectionCalendarCreate(2,  '2D');
SELECT dbo.f_classSectionCalendarCreate(2,  '1N');
SELECT dbo.f_classSectionCalendarCreate(3,  '1D');
SELECT dbo.f_classSectionCalendarCreate(3,  '2D');
SELECT dbo.f_classSectionCalendarCreate(3,  '1N');
SELECT dbo.f_classSectionCalendarCreate(4,  '1D');
SELECT dbo.f_classSectionCalendarCreate(4,  '2D');
SELECT dbo.f_classSectionCalendarCreate(4,  '1N');
SELECT dbo.f_classSectionCalendarCreate(5,  '1D');
SELECT dbo.f_classSectionCalendarCreate(5,  '2D');
SELECT dbo.f_classSectionCalendarCreate(5,  '1N');
SELECT dbo.f_classSectionCalendarCreate(6,  '1D');
SELECT dbo.f_classSectionCalendarCreate(6,  '2D');
SELECT dbo.f_classSectionCalendarCreate(6,  '1N');
SELECT dbo.f_classSectionCalendarCreate(7,  '1D');
SELECT dbo.f_classSectionCalendarCreate(7,  '2D');
SELECT dbo.f_classSectionCalendarCreate(7,  '1N');
SELECT dbo.f_classSectionCalendarCreate(8,  '1D');
SELECT dbo.f_classSectionCalendarCreate(8,  '2D');
SELECT dbo.f_classSectionCalendarCreate(8,  '1N');
SELECT dbo.f_classSectionCalendarCreate(9,  '1D');
SELECT dbo.f_classSectionCalendarCreate(9,  '2D');
SELECT dbo.f_classSectionCalendarCreate(9,  '1N');
SELECT dbo.f_classSectionCalendarCreate(10, '1D');
SELECT dbo.f_classSectionCalendarCreate(10, '2D');
SELECT dbo.f_classSectionCalendarCreate(10, '1N');
SELECT dbo.f_classSectionCalendarCreate(11, '1D');
SELECT dbo.f_classSectionCalendarCreate(11, '2D');
SELECT dbo.f_classSectionCalendarCreate(11, '1N');
SELECT dbo.f_classSectionCalendarCreate(12, '1D');
SELECT dbo.f_classSectionCalendarCreate(12, '2D');
SELECT dbo.f_classSectionCalendarCreate(12, '1N');
SELECT dbo.f_classSectionCalendarCreate(13, 'LI61D');	--classSection WAD calendar nº37
SELECT dbo.f_classSectionCalendarCreate(13, 'LI61N');	--classSection WAD calendar nº38
SELECT dbo.f_classSectionCalendarCreate(14, 'LI61D');	--classSection PS Dia calendar nº39
SELECT dbo.f_classSectionCalendarCreate(14, 'LI61N');	--classSection PS Noite calendar nº40
SELECT dbo.f_classSectionCalendarCreate(15, 'LI61D');	--classSection CC calendar nº41
SELECT dbo.f_classSectionCalendarCreate(15, 'LI61N');	--classSection CC calendar nº42
SELECT dbo.f_classSectionCalendarCreate(16, '1D');
SELECT dbo.f_classSectionCalendarCreate(16, '2D');
SELECT dbo.f_classSectionCalendarCreate(16, '1N');
SELECT dbo.f_classSectionCalendarCreate(17, '1D');
SELECT dbo.f_classSectionCalendarCreate(17, '2D');
SELECT dbo.f_classSectionCalendarCreate(17, '1N');
SELECT dbo.f_classSectionCalendarCreate(18, '1D');
SELECT dbo.f_classSectionCalendarCreate(18, '2D');
SELECT dbo.f_classSectionCalendarCreate(18, '1N');

-- WAD 1718v Class
CALL dbo.newTodo(2,
    ARRAY['[WAD]: Assignment #1'],
    ARRAY[2],
    ARRAY['The first assignment. The goal is to implement an HTTP API...'],
    ARRAY[2],
    ARRAY[1,2],
    ARRAY['https://tools.ietf.org/html/rfc7231'],
    TIMESTAMP '2021-04-19 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newTodo(2,
    ARRAY['[WAD]: Assignment #2'],
    ARRAY[2],
    ARRAY['The second assignment. The goal is to implement a Web Client for the API...'],
    ARRAY[2],
    ARRAY[1,2],
    NULL,
    TIMESTAMP '2021-06-12 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEventWithInstants(2,
    ARRAY['1st Exam WAD'],
    ARRAY[2],
    ARRAY['Normal season exam for WAD-1718v'],
    ARRAY[2],
    ARRAY[1],
    TIMESTAMP '2020-06-19 18:00:00', -- dtstart
    TIMESTAMP '2020-06-19 19:30:00', -- dtend
    2, -- dtstart dtend type
    'Room G.2.6', -- Location
    NULL,
    NULL,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEventWithInstants(2,
    ARRAY['2nd Exam WAD'],
    ARRAY[2],
    ARRAY['Second season exam for WAD-1718v'],
    ARRAY[2],
    ARRAY[1],
    TIMESTAMP '2020-07-01 10:00:00', -- dtstart
    TIMESTAMP '2020-07-01 12:30:00', -- dtend
    2, -- dtstart dtend type
    'Room G.2.2', -- Location
    NULL,
    NULL,
    TIMESTAMP '2020-01-01 16:35:30');

-- WAD 1718v 1D
CALL dbo.newEventWithDateReferences(28,
    ARRAY['WAD Monday Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the WAD curricular unit, for the 1718v-1D Class section'],
    ARRAY[2],
    ARRAY[2],
    1, -- start_instant
    TIME '10:00:00', -- dtstart
    TIME '12:30:00', -- dtend
    5, -- dtstart dtend type
    'Room G.2.1', -- Location
    'MO', -- days of week when this event repeats
    2, -- until
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEventWithDateReferences(28,
    ARRAY['WAD Wednesday Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the WAD curricular unit, for the 1718v-1D Class section'],
    ARRAY[2],
    ARRAY[2],
    1, -- start_instant
    TIME '10:00:00', -- dtstart
    TIME '12:30:00', -- dtend
    5, -- dtstart dtend type
    'Room G.2.4', -- Location
    'WE', -- days of week when this event repeats
    2, -- until
    TIMESTAMP '2020-01-01 16:35:30');

-- WAD 1718v 2D
CALL dbo.newEventWithDateReferences(29,
    ARRAY['WAD Monday Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the WAD curricular unit, for the 1718v-1N Class section'],
    ARRAY[2],
    ARRAY[2],
    1, -- start_instant
    TIME '10:00:00', -- dtstart
    TIME '12:30:00', -- dtend
    5, -- dtstart dtend type
    'Room G.1.4', -- Location
    'MO', -- days of week when this event repeats
    2, -- until
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEventWithDateReferences(29,
    ARRAY['WAD Wednesday Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the WAD curricular unit, for the 1718v-2D Class section'],
    ARRAY[2],
    ARRAY[2],
    1, -- start_instant
    TIME '10:00:00', -- dtstart
    TIME '12:30:00', -- dtend
    5, -- dtstart dtend type
    'Room G.1.15', -- Location
    'WE', -- days of week when this event repeats
    2, -- until
    TIMESTAMP '2020-01-01 16:35:30');

-- WAD 1718v 1N
CALL dbo.newEventWithDateReferences(30,
    ARRAY['WAD Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the WAD curricular unit, for the 1718v-1N Class section'],
    ARRAY[2],
    ARRAY[2],
    1, -- start_instant
    TIME '10:00:00', -- dtstart
    TIME '12:30:00', -- dtend
    5, -- dtstart dtend type
    'Room G.0.9', -- Location
    'TU,WE', -- days of week when this event repeats
    2, -- until
    TIMESTAMP '2020-01-01 16:35:30');

-- SL 1718v Class
CALL dbo.newTodo(1,
    ARRAY['[SL]: Assignment #1'],
    ARRAY[2],
    ARRAY['The first assignment. The goal is to implement a CLI...'],
    ARRAY[2],
    ARRAY[1,2],
    ARRAY['https://tools.ietf.org/html/rfc7231'],
    TIMESTAMP '2021-04-08 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newTodo(1,
    ARRAY['[SL]: Assignment #2'],
    ARRAY[2],
    ARRAY['The second assignment. The goal is to implement a Web Client for the API...'],
    ARRAY[2],
    ARRAY[1,2],
    NULL,
    TIMESTAMP '2021-06-20 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEventWithInstants(1,
    ARRAY['1st Exam SL'],
    ARRAY[2],
    ARRAY['Normal season exam for SL-1718v'],
    ARRAY[2],
    ARRAY[1],
    TIMESTAMP '2020-06-10 18:00:00', -- dtstart
    TIMESTAMP '2020-06-10 19:30:00', -- dtend
    2, -- dtstart dtend type
    'Room A.2.5', -- Location
    NULL,
    NULL,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEventWithInstants(1,
    ARRAY['2nd Exam SL'],
    ARRAY[2],
    ARRAY['Second season exam for SL-1718v'],
    ARRAY[2],
    ARRAY[1],
    TIMESTAMP '2020-06-24 10:00:00', -- dtstart
    TIMESTAMP '2020-07-24 12:30:00', -- dtend
    2, -- dtstart dtend type
    'Room F.1.2', -- Location
    NULL,
    NULL,
    TIMESTAMP '2020-01-01 16:35:30');

-- SL 1718v 1D
CALL dbo.newEventWithDateReferences(25,
    ARRAY['SL Monday Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the SL curricular unit, for the 1718v-1D Class section'],
    ARRAY[2],
    ARRAY[2],
    1, -- start_instant
    TIME '10:00:00', -- dtstart
    TIME '12:30:00', -- dtend
    5, -- dtstart dtend type
    'Room F.1.3', -- Location
    'MO', -- days of week when this event repeats
    2, -- until
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEventWithDateReferences(25,
    ARRAY['SL Wednesday Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the SL curricular unit, for the 1718v-1D Class section'],
    ARRAY[2],
    ARRAY[2],
    1, -- start_instant
    TIME '10:00:00', -- dtstart
    TIME '12:30:00', -- dtend
    5, -- dtstart dtend type
    'Room F.1.4', -- Location
    'WE', -- days of week when this event repeats
    2, -- until
    TIMESTAMP '2020-01-01 16:35:30');

-- SL 1718v 2D
CALL dbo.newEventWithDateReferences(26,
    ARRAY['SL Monday Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the SL curricular unit, for the 1718v-1N Class section'],
    ARRAY[2],
    ARRAY[2],
    1, -- start_instant
    TIME '10:00:00', -- dtstart
    TIME '12:30:00', -- dtend
    5, -- dtstart dtend type
    'Room F.1.4', -- Location
    'MO', -- days of week when this event repeats
    2, -- until
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEventWithDateReferences(26,
    ARRAY['SL Wednesday Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the SL curricular unit, for the 1718v-2D Class section'],
    ARRAY[2],
    ARRAY[2],
    1, -- start_instant
    TIME '10:00:00', -- dtstart
    TIME '12:30:00', -- dtend
    5, -- dtstart dtend type
    'Room G.1.13', -- Location
    'WE', -- days of week when this event repeats
    2, -- until
    TIMESTAMP '2020-01-01 16:35:30');

-- SL 1718v 1N
CALL dbo.newEventWithDateReferences(27,
    ARRAY['SL Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the SL curricular unit, for the 1718v-1N Class section'],
    ARRAY[2],
    ARRAY[2],
    1, -- start_instant
    TIME '10:00:00', -- dtstart
    TIME '12:30:00', -- dtend
    5, -- dtstart dtend type
    'Room G.2.9', -- Location
    'TU,WE', -- days of week when this event repeats
    2, -- until
    TIMESTAMP '2020-01-01 16:35:30');


-- 1920v calendar information --

-- <LI61D lectures> --
-- <PS lectures> --
CALL dbo.newEventWithDateReferences(57,
    ARRAY['PS Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the PS curricular unit for the 1920v-LI61D Class section'],
    ARRAY[2],
    ARRAY[2],
    9, -- start_instant
    TIME '12:30:00', -- dtstart
    TIME '14:00:00', -- dtend
    5, -- dtstart dtend type
    'Room G.2.8', -- Location
    'TU', -- days of week when this event repeats
    10, -- until
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEventWithDateReferences(57,
    ARRAY['PS Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the PS curricular unit for the 1920v-LI61D Class section'],
    ARRAY[2],
    ARRAY[2],
    9, -- start_instant
    TIME '11:00:00', -- dtstart
    TIME '14:00:00', -- dtend
    5, -- dtstart dtend type
    'Room G.1.2', -- Location
    'WE', -- days of week when this event repeats
    10, -- until
    TIMESTAMP '2020-01-01 16:35:30');
-- </PS lectures> --

-- <WAD lectures> --
CALL dbo.newEventWithDateReferences(55,
    ARRAY['WAD Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the WAD curricular unit for the 1920v-LI61D Class section'],
    ARRAY[2],
    ARRAY[2],
    9, -- start_instant
    TIME '11:00:00', -- dtstart
    TIME '12:30:00', -- dtend
    5, -- dtstart dtend type
    'Room G.0.1', -- Location
    'MO', -- days of week when this event repeats
    10, -- until
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEventWithDateReferences(55,
    ARRAY['WAD Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the WAD curricular unit for the 1920v-LI61D Class section'],
    ARRAY[2],
    ARRAY[2],
    9, -- start_instant
    TIME '11:00:00', -- dtstart
    TIME '14:00:00', -- dtend
    5, -- dtstart dtend type
    'Room G.0.11', -- Location
    'TH', -- days of week when this event repeats
    10, -- until
    TIMESTAMP '2020-01-01 16:35:30');
-- </WAD lecture> --

-- <CC lectures> --
CALL dbo.newEventWithDateReferences(59,
    ARRAY['CC Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the CC curricular unit for the 1920v-LI61D Class section'],
    ARRAY[2],
    ARRAY[2],
    9, -- start_instant
    TIME '09:30:00', -- dtstart
    TIME '11:00:00', -- dtend
    5, -- dtstart dtend type
    'Room G.2.5', -- Location
    'MO', -- days of week when this event repeats
    10, -- until
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEventWithDateReferences(59,
    ARRAY['CC Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the CC curricular unit for the 1920v-LI61D Class section'],
    ARRAY[2],
    ARRAY[2],
    9, -- start_instant
    TIME '09:30:00', -- dtstart
    TIME '12:30:00', -- dtend
    5, -- dtstart dtend type
    'Room G.1.12', -- Location
    'FR', -- days of week when this event repeats
    10, -- until
    TIMESTAMP '2020-01-01 16:35:30');
-- </CC lectures> --

-- </LI61D lectures> --



-- <LI61N lectures> --
-- <PS lectures> --
CALL dbo.newEventWithDateReferences(58,
    ARRAY['PS Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the PS curricular unit for the 1920v-LI61N Class section'],
    ARRAY[2],
    ARRAY[2],
    9, -- start_instant
    TIME '20:00:00', -- dtstart
    TIME '23:00:00', -- dtend
    5, -- dtstart dtend type
    'Room G.0.2', -- Location
    'MO', -- days of week when this event repeats
    10, -- until
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEventWithDateReferences(58,
    ARRAY['PS Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the PS curricular unit for the 1920v-LI61N Class section'],
    ARRAY[2],
    ARRAY[2],
    9, -- start_instant
    TIME '18:30:00', -- dtstart
    TIME '20:00:00', -- dtend
    5, -- dtstart dtend type
    'Room G.0.4', -- Location
    'WE', -- days of week when this event repeats
    10, -- until
    TIMESTAMP '2020-01-01 16:35:30');
-- </PS lectures> --

-- <WAD lectures> --
CALL dbo.newEventWithDateReferences(56,
    ARRAY['WAD Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the WAD curricular unit for the 1920v-LI61N Class section'],
    ARRAY[2],
    ARRAY[2],
    9, -- start_instant
    TIME '18:30:00', -- dtstart
    TIME '20:00:00', -- dtend
    5, -- dtstart dtend type
    'Room G.0.4', -- Location
    'TH', -- days of week when this event repeats
    10, -- until
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEventWithDateReferences(56,
    ARRAY['WAD Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the WAD curricular unit for the 1920v-LI61N Class section'],
    ARRAY[2],
    ARRAY[2],
    9, -- start_instant
    TIME '20:00:00', -- dtstart
    TIME '23:00:00', -- dtend
    5, -- dtstart dtend type
    'Room G.0.8', -- Location
    'FR', -- days of week when this event repeats
    10, -- until
    TIMESTAMP '2020-01-01 16:35:30');
-- </WAD lecture> --

-- <CC lectures> --
CALL dbo.newEventWithDateReferences(60,
    ARRAY['CC Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the CC curricular unit for the 1920v-LI61N Class section'],
    ARRAY[2],
    ARRAY[2],
    9, -- start_instant
    TIME '18:30:00', -- dtstart
    TIME '20:00:00', -- dtend
    5, -- dtstart dtend type
    'Room E.1.6', -- Location
    'MO', -- days of week when this event repeats
    10, -- until
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEventWithDateReferences(60,
    ARRAY['CC Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the CC curricular unit for the 1920v-LI61N Class section'],
    ARRAY[2],
    ARRAY[2],
    9, -- start_instant
    TIME '20:00:00', -- dtstart
    TIME '23:00:00', -- dtend
    5, -- dtstart dtend type
    'Room G.0.12', -- Location
    'WE', -- days of week when this event repeats
    10, -- until
    TIMESTAMP '2020-01-01 16:35:30');
-- </CC lectures> --
-- </LI61N lectures> --


-- <Assignments> --

-- <WAD Assignments> --
CALL dbo.newTodo(13,
    ARRAY['[WAD]: Assignment #1'],
    ARRAY[2],
    ARRAY['The first assignment. The goal is to implement an HTTP API...'],
    ARRAY[2],
    ARRAY[1,2],
    ARRAY['https://tools.ietf.org/html/rfc7231'],
    TIMESTAMP '2020-04-19 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newTodo(13,
    ARRAY['[WAD]: Assignment #2'],
    ARRAY[2],
    ARRAY['The second assignment. The goal is to implement a Web Client for the API...'],
    ARRAY[2],
    ARRAY[1,2],
    NULL,
    TIMESTAMP '2020-05-25 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newTodo(13,
    ARRAY['[WAD]: Assignment #3'],
    ARRAY[2],
    ARRAY['The third and final assignment. Wrapping it up...'],
    ARRAY[2],
    ARRAY[1,2],
    NULL,
    TIMESTAMP '2020-07-5 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');
-- </WAD Assignments> --

-- <CC Assignments> --
CALL dbo.newTodo(15,
    ARRAY['[CC]: Virtual Machines with GCP'],
    ARRAY[2],
    ARRAY['Creation of virtual machines using Google Cloud Platform.'],
    ARRAY[2],
    ARRAY[3],
    NULL,
    TIMESTAMP '2020-03-31 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newTodo(15,
    ARRAY['[CC]: Distributed Services'],
    ARRAY[2],
    ARRAY['Development of distributed services using RMI and gRPC.'],
    ARRAY[2],
    ARRAY[3],
    NULL,
    TIMESTAMP '2020-04-17 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newTodo(15,
    ARRAY['[CC]: Google Cloud Storage API'],
    ARRAY[2],
    ARRAY['Using the GCP API.'],
    ARRAY[2],
    ARRAY[3],
    NULL,
    TIMESTAMP '2020-05-03 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newTodo(15,
    ARRAY['[CC]: Google Firestore Service'],
    ARRAY[2],
    ARRAY['Acessing Google Firestore Service using Java API.'],
    ARRAY[2],
    ARRAY[3],
    NULL,
    TIMESTAMP '2020-05-10 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newTodo(15,
    ARRAY['[CC]: Google Pub/Sub Service'],
    ARRAY[2],
    ARRAY['Acessing Google Pub/Sub Service using Java API.'],
    ARRAY[2],
    ARRAY[3],
    NULL,
    TIMESTAMP '2020-05-17 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newTodo(15,
    ARRAY['[CC]: Instances management'],
    ARRAY[2],
    ARRAY['Managing instances and firewall rules...'],
    ARRAY[2],
    ARRAY[3],
    NULL,
    TIMESTAMP '2020-05-17 23:59:00', -- due
    5,
    TIMESTAMP '2020-05-28 16:35:30');

CALL dbo.newTodo(15,
    ARRAY['[CC]: Final Assignment'],
    ARRAY[2],
    ARRAY['Implementing a system for task execution...'],
    ARRAY[2],
    ARRAY[5],
    NULL,
    TIMESTAMP '2020-06-20 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');
-- </CC Assignments> --

-- <PS Dates> --
CALL dbo.newTodo(14,
    ARRAY['[PS]: Poster and beta version'],
    ARRAY[2],
    ARRAY['Delivery of the project poster and beta version.'],
    ARRAY[2],
    ARRAY[1,2],
    NULL,
    TIMESTAMP '2020-06-15 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newTodo(14,
    ARRAY['[PS]: Final Version'],
    ARRAY[2],
    ARRAY['Delivery of the project final version.'],
    ARRAY[2],
    ARRAY[1,2],
    NULL,
    TIMESTAMP '2020-09-12 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newTodo(14,
    ARRAY['[PS]: Final Version Special Season'],
    ARRAY[2],
    ARRAY['Delivery of the project final version during special season.'],
    ARRAY[2],
    ARRAY[1,2],
    NULL,
    TIMESTAMP '2020-09-26 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');
-- </PS Dates> --

-- <Assignments> --


-- <Exams> --

-- <WAD Exams> --
CALL dbo.newEventWithInstants(13,
    ARRAY['1st Exam WAD'],
    ARRAY[2],
    ARRAY['Normal season exam for WAD-1920v'],
    ARRAY[2],
    ARRAY[1],
    TIMESTAMP '2020-06-19 18:00:00', -- dtstart, WRONG DATE CORRECT AFTER ISEL RELEASES THE LATEST EXAM MAP
    TIMESTAMP '2020-06-19 19:30:00', -- dtend
    2, -- dtstart dtend type
    'Room A.2.10', -- Location
    NULL,
    NULL,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEventWithInstants(13,
    ARRAY['2nd Exam WAD'],
    ARRAY[2],
    ARRAY['Second season exam for WAD-1920v'],
    ARRAY[2],
    ARRAY[1],
    TIMESTAMP '2020-07-01 10:00:00', -- dtstart, WRONG DATE CORRECT AFTER ISEL RELEASES THE LATEST EXAM MAP
    TIMESTAMP '2020-07-01 12:30:00', -- dtend
    2, -- dtstart dtend type
    'Room A.2.10', -- Location
    NULL,
    NULL,
    TIMESTAMP '2020-01-01 16:35:30');
-- </WAD Exams> --

-- <CC Exams> --
CALL dbo.newEventWithInstants(15,
    ARRAY['Exam CC'],
    ARRAY[2],
    ARRAY['Exam for CC-1920v'],
    ARRAY[2],
    ARRAY[1],
    TIMESTAMP '2020-07-01 10:00:00', -- dtstart, WRONG DATE CORRECT AFTER ISEL RELEASES THE LATEST EXAM MAP
    TIMESTAMP '2020-07-01 12:30:00', -- dtend
    2, -- dtstart dtend type
    'Room A.2.12', -- Location
    NULL,
    NULL,
    TIMESTAMP '2020-01-01 16:35:30');
-- </CC Exams> --
-- </Exams> --


-- End of 1920v calendar information --

