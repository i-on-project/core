INSERT INTO dbo.Programme(acronym, name, termSize) VALUES
('LEIC', 'licenciatura eng. inf.', 6),
('MEIC', 'mestrado eng. inf.',     4);

INSERT INTO dbo.CalendarTerm(id, start_date, end_date) VALUES
('1718v', to_timestamp(1586379923), to_timestamp(1586379933)),
('1718i', to_timestamp(1586379924), to_timestamp(1586379933)),
('1819v', to_timestamp(1586379925), to_timestamp(1586379933)),
('1819i', to_timestamp(1586379927), to_timestamp(1586379933)),
('1920v', to_timestamp(1586379929), to_timestamp(1586379933)),
('1920i', to_timestamp(1586379930), to_timestamp(1586379943)),
('2021v', to_timestamp(1586379940), to_timestamp(1586379953)),
('2021i', to_timestamp(1586379950), to_timestamp(1586379963));

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

INSERT INTO dbo.Token (hash, isValid, issuedAt, expiresAt, claims) VALUES 
('a00ffe411bc611ca81e1bfd5cd862586d89ca3b3a02fccc8586b547396bf60aa', TRUE, 1591544539044, 1591544539045,
'{"client_id":3, "scope": "urn:org:ionproject:scopes:api:read"}'),
('1681e5591f1bd814d69c8cdc657a0752707aff4d82d8b94d2c85185a289058ea', FALSE, 1591544539044, 1691544539044,
'{"client_id":4, "scope": "urn:org:ionproject:scopes:api:write"}'),
('92f9640fb837bb369afe725941f3d54464ff3c19d25de31a188bca72348de2b2', TRUE, 1591544539044, 1691544539044,
'{"client_id":5, "scope": "urn:org:ionproject:scopes:api:read_restricted"}');


INSERT INTO dbo.scopes (scope) VALUES
('urn:org:ionproject:scopes:api:read'),
('urn:org:ionproject:scopes:api:write'),
('urn:org:ionproject:scopes:token:issue'),
('urn:org:ionproject:scopes:api:read_restricted');

/*
* Don't use wildcards in the paths
* e.g. /v0/courses which a user may have permission to access
* can be tricked into accepting a request to a resource he is not allowed.
* e.g. GET "/v0/courses/../programmes"
* if the code accepts anything towards "/v0/courses*"
*
*This way of representing permissions is very extensive and would exponentially
*grow if all paths were repeated by each HTTP method, therefore the field
*method is a list of methods allowed for each resource.
*/
INSERT INTO dbo.policies(scope_id, method, version, path) VALUES
(1, 'GET', '*','/'),
(1, 'GET', 'v0', 'courses'),
(1, 'GET', 'v0', 'courses/?'),
(1, 'GET', 'v0', 'courses/?/classes'),
(1, 'GET', 'v0', 'courses/?/classes/?'),
(1, 'GET', 'v0', 'courses/?/classes/?/?'),
(1, 'GET', 'v0', 'courses/?/classes/?/calendar'),
(1, 'GET', 'v0', 'courses/?/classes/?/?/calendar'),
(1, 'GET', 'v0', 'courses/?/classes/?/calendar/?'),
(1, 'GET', 'v0', 'courses/?/classes/?/?/calendar/?'),
(1, 'GET', 'v0', 'programmes'),
(1, 'GET', 'v0', 'programmes/?'),
(1, 'GET', 'v0', 'programmes/?/offers'),
(1, 'GET', 'v0', 'programmes/?/offers/?'),
(1, 'GET', 'v0', 'calendar-terms'),
(1, 'GET', 'v0', 'calendar-terms/?'),
(2, 'PUT,POST', 'v0', 'courses'),
(2, 'PUT,POST', 'v0', 'courses/?'),
(2, 'PUT,POST', 'v0', 'courses/?/classes'),
(2, 'PUT,POST', 'v0', 'courses/?/classes/?'),
(2, 'PUT,POST', 'v0', 'courses/?/classes/?/?'),
(2, 'PUT,POST', 'v0', 'courses/?/classes/?/calendar'),
(2, 'PUT,POST', 'v0', 'courses/?/classes/?/?/calendar'),
(2, 'PUT,POST', 'v0', 'courses/?/classes/?/calendar/?'),
(2, 'PUT,POST', 'v0', 'courses/?/classes/?/?/calendar/?'),
(2, 'PUT,POST', 'v0', 'programmes'),                                            
(2, 'PUT,POST', 'v0', 'programmes/?'),                                       
(2, 'PUT,POST', 'v0', 'programmes/?/offers'),
(2, 'PUT,POST', 'v0', 'programmes/?/offers/?'),
(2, 'PUT,POST', 'v0', 'calendar-terms'),
(2, 'PUT,POST', 'v0', 'calendar-terms/?'),
(4, 'GET', 'v0', 'programmes'), --Testing read restricted scope
(4, 'GET', 'v0', 'programmes/?'),
(3, 'POST', '*', 'issueToken'),
(1, 'POST', '*', 'revokeToken'),
(2, 'POST', '*', 'revokeToken'),
(3, 'POST', '*', 'revokeToken'),
(4, 'POST', '*', 'revokeToken');






INSERT INTO dbo.ProgrammeOffer(programmeId, courseId, optional, termNumber) VALUES 
(1, 2, TRUE,  3),
(1, 1, FALSE, 6),
(1, 3, FALSE, 1),
(1, 4, TRUE, 6),
(1, 5, FALSE, 6),
(2, 3, FALSE, 1);

CALL dbo.sp_classCalendarCreate('1718v', 1);
CALL dbo.sp_classCalendarCreate('1718v', 2);
CALL dbo.sp_classCalendarCreate('1718v', 3);
CALL dbo.sp_classCalendarCreate('1718i', 1);
CALL dbo.sp_classCalendarCreate('1718i', 2);
CALL dbo.sp_classCalendarCreate('1718i', 3);
CALL dbo.sp_classCalendarCreate('1819v', 1);
CALL dbo.sp_classCalendarCreate('1819v', 2);
CALL dbo.sp_classCalendarCreate('1819v', 3);
CALL dbo.sp_classCalendarCreate('1819i', 1);
CALL dbo.sp_classCalendarCreate('1819i', 2);
CALL dbo.sp_classCalendarCreate('1819i', 3);
CALL dbo.sp_classCalendarCreate('1920v', 2);	-- calendar nº13 WAD
CALL dbo.sp_classCalendarCreate('1920v', 4);	-- calendar nº14 PS
CALL dbo.sp_classCalendarCreate('1920v', 5);	-- calendar nº15 CC
CALL dbo.sp_classCalendarCreate('1920i', 1);
CALL dbo.sp_classCalendarCreate('1920i', 2);
CALL dbo.sp_classCalendarCreate('1920i', 3);
CALL dbo.sp_classCalendarCreate('2021v', 1);
CALL dbo.sp_classCalendarCreate('2021v', 2);
CALL dbo.sp_classCalendarCreate('2021v', 3);
CALL dbo.sp_classCalendarCreate('2021i', 1);
CALL dbo.sp_classCalendarCreate('2021i', 2);
CALL dbo.sp_classCalendarCreate('2021i', 3);

CALL dbo.sp_classSectionCalendarCreate(1,  '1D');
CALL dbo.sp_classSectionCalendarCreate(1,  '2D');
CALL dbo.sp_classSectionCalendarCreate(1,  '1N');
CALL dbo.sp_classSectionCalendarCreate(2,  '1D');
CALL dbo.sp_classSectionCalendarCreate(2,  '2D');
CALL dbo.sp_classSectionCalendarCreate(2,  '1N');
CALL dbo.sp_classSectionCalendarCreate(3,  '1D');
CALL dbo.sp_classSectionCalendarCreate(3,  '2D');
CALL dbo.sp_classSectionCalendarCreate(3,  '1N');
CALL dbo.sp_classSectionCalendarCreate(4,  '1D');
CALL dbo.sp_classSectionCalendarCreate(4,  '2D');
CALL dbo.sp_classSectionCalendarCreate(4,  '1N');
CALL dbo.sp_classSectionCalendarCreate(5,  '1D');
CALL dbo.sp_classSectionCalendarCreate(5,  '2D');
CALL dbo.sp_classSectionCalendarCreate(5,  '1N');
CALL dbo.sp_classSectionCalendarCreate(6,  '1D');
CALL dbo.sp_classSectionCalendarCreate(6,  '2D');
CALL dbo.sp_classSectionCalendarCreate(6,  '1N');
CALL dbo.sp_classSectionCalendarCreate(7,  '1D');
CALL dbo.sp_classSectionCalendarCreate(7,  '2D');
CALL dbo.sp_classSectionCalendarCreate(7,  '1N');
CALL dbo.sp_classSectionCalendarCreate(8,  '1D');
CALL dbo.sp_classSectionCalendarCreate(8,  '2D');
CALL dbo.sp_classSectionCalendarCreate(8,  '1N');
CALL dbo.sp_classSectionCalendarCreate(9,  '1D');
CALL dbo.sp_classSectionCalendarCreate(9,  '2D');
CALL dbo.sp_classSectionCalendarCreate(9,  '1N');
CALL dbo.sp_classSectionCalendarCreate(10, '1D');
CALL dbo.sp_classSectionCalendarCreate(10, '2D');
CALL dbo.sp_classSectionCalendarCreate(10, '1N');
CALL dbo.sp_classSectionCalendarCreate(11, '1D');
CALL dbo.sp_classSectionCalendarCreate(11, '2D');
CALL dbo.sp_classSectionCalendarCreate(11, '1N');
CALL dbo.sp_classSectionCalendarCreate(12, '1D');
CALL dbo.sp_classSectionCalendarCreate(12, '2D');
CALL dbo.sp_classSectionCalendarCreate(12, '1N');
CALL dbo.sp_classSectionCalendarCreate(13, 'LI61D');	--classSection WAD calendar nº37
CALL dbo.sp_classSectionCalendarCreate(13, 'LI61N');	--classSection WAD calendar nº38
CALL dbo.sp_classSectionCalendarCreate(14, 'LI61D');	--classSection PS Dia calendar nº39
CALL dbo.sp_classSectionCalendarCreate(14, 'LI61N');	--classSection PS Noite calendar nº40
CALL dbo.sp_classSectionCalendarCreate(15, 'LI61D');	--classSection CC calendar nº41
CALL dbo.sp_classSectionCalendarCreate(15, 'LI61N');	--classSection CC calendar nº42
CALL dbo.sp_classSectionCalendarCreate(16, '1D');
CALL dbo.sp_classSectionCalendarCreate(16, '2D');
CALL dbo.sp_classSectionCalendarCreate(16, '1N');
CALL dbo.sp_classSectionCalendarCreate(17, '1D');
CALL dbo.sp_classSectionCalendarCreate(17, '2D');
CALL dbo.sp_classSectionCalendarCreate(17, '1N');
CALL dbo.sp_classSectionCalendarCreate(18, '1D');
CALL dbo.sp_classSectionCalendarCreate(18, '2D');
CALL dbo.sp_classSectionCalendarCreate(18, '1N');
CALL dbo.sp_classSectionCalendarCreate(19, '1D');
CALL dbo.sp_classSectionCalendarCreate(19, '2D');
CALL dbo.sp_classSectionCalendarCreate(19, '1N');
CALL dbo.sp_classSectionCalendarCreate(20, '1D');
CALL dbo.sp_classSectionCalendarCreate(20, '2D');
CALL dbo.sp_classSectionCalendarCreate(20, '1N');
CALL dbo.sp_classSectionCalendarCreate(21, '1D');
CALL dbo.sp_classSectionCalendarCreate(21, '2D');
CALL dbo.sp_classSectionCalendarCreate(21, '1N');
CALL dbo.sp_classSectionCalendarCreate(22, '1D');
CALL dbo.sp_classSectionCalendarCreate(22, '2D');
CALL dbo.sp_classSectionCalendarCreate(22, '1N');
CALL dbo.sp_classSectionCalendarCreate(23, '1D');
CALL dbo.sp_classSectionCalendarCreate(23, '2D');
CALL dbo.sp_classSectionCalendarCreate(23, '1N');
CALL dbo.sp_classSectionCalendarCreate(24, '1D');
CALL dbo.sp_classSectionCalendarCreate(24, '2D');
CALL dbo.sp_classSectionCalendarCreate(24, '1N');


INSERT INTO dbo.Language(name) VALUES
('pt-PT'),
('en-US'),
('en-GB'),
('ab'),
('aa'),
('af'),
('ak'),
('sq'),
('am'),
('ar'),
('an'),
('hy'),
('as'),
('av'),
('ae'),
('ay'),
('az'),
('bm'),
('ba'),
('eu'),
('be'),
('bn'),
('bh'),
('bi'),
('bs'),
('br'),
('bg'),
('my'),
('ca'),
('ch'),
('ce'),
('ny'),
('zh'),
('zh-Hans'),
('zh-Hant'),
('cv'),
('kw'),
('co'),
('cr'),
('hr'),
('cs'),
('da'),
('dv'),
('nl'),
('dz'),
('eo'),
('et'),
('ee'),
('fo'),
('fj'),
('fi'),
('fr'),
('ff'),
('gl'),
('gd'),
('gv'),
('ka'),
('de'),
('el'),
('kl'),
('gn'),
('gu'),
('ht'),
('ha'),
('he'),
('hz'),
('hi'),
('ho'),
('hu'),
('is'),
('io'),
('ig'),
('id'),
('in'),
('ia'),
('ie'),
('iu'),
('ik'),
('ga'),
('it'),
('ja'),
('jv'),
('kn'),
('kr'),
('ks'),
('kk'),
('km'),
('ki'),
('rw'),
('rn'),
('ky'),
('kv'),
('kg'),
('ko'),
('ku'),
('kj'),
('lo'),
('la'),
('lv'),
('li'),
('ln'),
('lt'),
('lu'),
('lg'),
('lb'),
('mk'),
('mg'),
('ms'),
('ml'),
('mt'),
('mi'),
('mr'),
('mh'),
('mo'),
('mn'),
('na'),
('nv'),
('ng'),
('nd'),
('ne'),
('no'),
('nb'),
('nn'),
('oc'),
('oj'),
('cu'),
('or'),
('om'),
('os'),
('pi'),
('ps'),
('fa'),
('pl'),
('pa'),
('qu'),
('rm'),
('ro'),
('ru'),
('se'),
('sm'),
('sg'),
('sa'),
('sr'),
('sh'),
('st'),
('tn'),
('sn'),
('ii'),
('sd'),
('si'),
('sk'),
('sl'),
('so'),
('nr'),
('es'),
('su'),
('sw'),
('ss'),
('sv'),
('tl'),
('ty'),
('tg'),
('ta'),
('tt'),
('te'),
('th'),
('bo'),
('ti'),
('to'),
('ts'),
('tr'),
('tk'),
('tw'),
('ug'),
('uk'),
('ur'),
('uz'),
('ve'),
('vi'),
('vo'),
('wa'),
('cy'),
('wo'),
('fy'),
('xh'),
('yi'),
('ji'),
('yo'),
('za');

INSERT INTO dbo.ICalendarDataType(name) VALUES
('BINARY'),
('BOOLEAN'),
('CAL-ADDRESS'),
('DATE'),
('DATE-TIME'),
('DURATION');

INSERT INTO dbo.Category(name, language) VALUES
('Exame',       1),
('Aula',        1),
('Laboratório', 1),
('Aviso',       1),
('Entrega',     1),
('Teste',       1),
('Exam',        2),
('Exam',        3),
('Lecture',     2),
('Lecture',     3),
('Laboratory',  2),
('Laboratory',  3),
('Warning',     2),
('Warning',     3),
('Deadline',    2),
('Deadline',    3),
('Test',        2),
('Test',        3);

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

CALL dbo.newEvent(2,
    ARRAY['1st Exam WAD'],
    ARRAY[2],
    ARRAY['Normal season exam for WAD-1718v'],
    ARRAY[2],
    ARRAY[7],
    TIMESTAMP '2020-06-19 18:00:00', -- dtstart
    TIMESTAMP '2020-06-19 19:30:00', -- dtend
    2, -- dtstart dtend type
    NULL,
    NULL,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEvent(2,
    ARRAY['2nd Exam WAD'],
    ARRAY[2],
    ARRAY['Second season exam for WAD-1718v'],
    ARRAY[2],
    ARRAY[7],
    TIMESTAMP '2020-07-01 10:00:00', -- dtstart
    TIMESTAMP '2020-07-01 12:30:00', -- dtend
    2, -- dtstart dtend type
    NULL,
    NULL,
    TIMESTAMP '2020-01-01 16:35:30');

-- WAD 1718v 1D
CALL dbo.newEvent(28,
    ARRAY['WAD Monday Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the WAD curricular unit, for the 1718v-1D Class section'],
    ARRAY[2],
    ARRAY[9],
    TIMESTAMP '2020-02-10 10:00:00', -- dtstart
    TIMESTAMP '2020-02-10 12:30:00', -- dtend
    5, -- dtstart dtend type
    'MO', -- days of week when this event repeats
    TIMESTAMP '2020-06-12 23:50:00',
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEvent(28,
    ARRAY['WAD Wednesday Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the WAD curricular unit, for the 1718v-1D Class section'],
    ARRAY[2],
    ARRAY[9],
    TIMESTAMP '2020-02-12 10:00:00', -- dtstart
    TIMESTAMP '2020-02-12 12:30:00', -- dtend
    5, -- dtstart dtend type
    'WE', -- days of week when this event repeats
    TIMESTAMP '2020-06-12 23:50:00',
    TIMESTAMP '2020-01-01 16:35:30');

-- WAD 1718v 2D
CALL dbo.newEvent(29,
    ARRAY['WAD Monday Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the WAD curricular unit, for the 1718v-1N Class section'],
    ARRAY[2],
    ARRAY[9],
    TIMESTAMP '2020-02-10 10:00:00', -- dtstart
    TIMESTAMP '2020-02-10 12:30:00', -- dtend
    5, -- dtstart dtend type
    'MO', -- days of week when this event repeats
    TIMESTAMP '2020-06-12 23:50:00',
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEvent(29,
    ARRAY['WAD Wednesday Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the WAD curricular unit, for the 1718v-2D Class section'],
    ARRAY[2],
    ARRAY[9],
    TIMESTAMP '2020-02-12 10:00:00', -- dtstart
    TIMESTAMP '2020-02-12 12:30:00', -- dtend
    5, -- dtstart dtend type
    'WE', -- days of week when this event repeats
    TIMESTAMP '2020-06-12 23:50:00',
    TIMESTAMP '2020-01-01 16:35:30');

-- WAD 1718v 1N
CALL dbo.newEvent(30,
    ARRAY['WAD Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the WAD curricular unit, for the 1718v-1N Class section'],
    ARRAY[2],
    ARRAY[9],
    TIMESTAMP '2020-02-11 10:00:00', -- dtstart
    TIMESTAMP '2020-02-11 12:30:00', -- dtend
    5, -- dtstart dtend type
    'TU,WE', -- days of week when this event repeats
    TIMESTAMP '2020-06-12 23:50:00',
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

CALL dbo.newEvent(1,
    ARRAY['1st Exam SL'],
    ARRAY[2],
    ARRAY['Normal season exam for SL-1718v'],
    ARRAY[2],
    ARRAY[7],
    TIMESTAMP '2020-06-10 18:00:00', -- dtstart
    TIMESTAMP '2020-06-10 19:30:00', -- dtend
    2, -- dtstart dtend type
    NULL,
    NULL,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEvent(1,
    ARRAY['2nd Exam SL'],
    ARRAY[2],
    ARRAY['Second season exam for SL-1718v'],
    ARRAY[2],
    ARRAY[7],
    TIMESTAMP '2020-06-24 10:00:00', -- dtstart
    TIMESTAMP '2020-07-24 12:30:00', -- dtend
    2, -- dtstart dtend type
    NULL,
    NULL,
    TIMESTAMP '2020-01-01 16:35:30');

-- SL 1718v 1D
CALL dbo.newEvent(25,
    ARRAY['SL Monday Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the SL curricular unit, for the 1718v-1D Class section'],
    ARRAY[2],
    ARRAY[9],
    TIMESTAMP '2020-02-10 10:00:00', -- dtstart
    TIMESTAMP '2020-02-10 12:30:00', -- dtend
    5, -- dtstart dtend type
    'MO', -- days of week when this event repeats
    TIMESTAMP '2020-06-12 23:50:00',
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEvent(25,
    ARRAY['SL Wednesday Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the SL curricular unit, for the 1718v-1D Class section'],
    ARRAY[2],
    ARRAY[9],
    TIMESTAMP '2020-02-12 10:00:00', -- dtstart
    TIMESTAMP '2020-02-12 12:30:00', -- dtend
    5, -- dtstart dtend type
    'WE', -- days of week when this event repeats
    TIMESTAMP '2020-06-12 23:50:00',
    TIMESTAMP '2020-01-01 16:35:30');

-- SL 1718v 2D
CALL dbo.newEvent(26,
    ARRAY['SL Monday Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the SL curricular unit, for the 1718v-1N Class section'],
    ARRAY[2],
    ARRAY[9],
    TIMESTAMP '2020-02-10 10:00:00', -- dtstart
    TIMESTAMP '2020-02-10 12:30:00', -- dtend
    5, -- dtstart dtend type
    'MO', -- days of week when this event repeats
    TIMESTAMP '2020-06-12 23:50:00',
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEvent(26,
    ARRAY['SL Wednesday Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the SL curricular unit, for the 1718v-2D Class section'],
    ARRAY[2],
    ARRAY[9],
    TIMESTAMP '2020-02-12 10:00:00', -- dtstart
    TIMESTAMP '2020-02-12 12:30:00', -- dtend
    5, -- dtstart dtend type
    'WE', -- days of week when this event repeats
    TIMESTAMP '2020-06-12 23:50:00',
    TIMESTAMP '2020-01-01 16:35:30');

-- SL 1718v 1N
CALL dbo.newEvent(27,
    ARRAY['SL Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the SL curricular unit, for the 1718v-1N Class section'],
    ARRAY[2],
    ARRAY[9],
    TIMESTAMP '2020-02-11 10:00:00', -- dtstart
    TIMESTAMP '2020-02-11 12:30:00', -- dtend
    5, -- dtstart dtend type
    'TU,WE', -- days of week when this event repeats
    TIMESTAMP '2020-06-12 23:50:00',
    TIMESTAMP '2020-01-01 16:35:30');


-- 1920v calendar information --

-- <LI61D lectures> --
-- <PS lectures> --
CALL dbo.newEvent(63,
    ARRAY['PS Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the PS curricular unit for the 1920v-LI61D Class section'],
    ARRAY[2],
    ARRAY[9],
    TIMESTAMP '2020-02-11 12:30:00', -- dtstart
    TIMESTAMP '2020-02-11 14:00:00', -- dtend
    5, -- dtstart dtend type
    'TU', -- days of week when this event repeats
    TIMESTAMP '2020-06-12 23:50:00',
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEvent(63,
    ARRAY['PS Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the PS curricular unit for the 1920v-LI61D Class section'],
    ARRAY[2],
    ARRAY[9],
    TIMESTAMP '2020-02-11 11:00:00', -- dtstart
    TIMESTAMP '2020-02-11 14:00:00', -- dtend
    5, -- dtstart dtend type
    'WE', -- days of week when this event repeats
    TIMESTAMP '2020-06-12 23:50:00',
    TIMESTAMP '2020-01-01 16:35:30');
-- </PS lectures> --

-- <WAD lectures> --
CALL dbo.newEvent(61,
    ARRAY['WAD Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the WAD curricular unit for the 1920v-LI61D Class section'],
    ARRAY[2],
    ARRAY[9],
    TIMESTAMP '2020-02-11 11:00:00', -- dtstart
    TIMESTAMP '2020-02-11 12:30:00', -- dtend
    5, -- dtstart dtend type
    'MO', -- days of week when this event repeats
    TIMESTAMP '2020-06-12 23:50:00',
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEvent(61,
    ARRAY['WAD Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the WAD curricular unit for the 1920v-LI61D Class section'],
    ARRAY[2],
    ARRAY[9],
    TIMESTAMP '2020-02-11 11:00:00', -- dtstart
    TIMESTAMP '2020-02-11 14:00:00', -- dtend
    5, -- dtstart dtend type
    'TH', -- days of week when this event repeats
    TIMESTAMP '2020-06-12 23:50:00',
    TIMESTAMP '2020-01-01 16:35:30');
-- </WAD lecture> --

-- <CC lectures> --
CALL dbo.newEvent(65,
    ARRAY['CC Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the CC curricular unit for the 1920v-LI61D Class section'],
    ARRAY[2],
    ARRAY[9],
    TIMESTAMP '2020-02-11 09:30:00', -- dtstart
    TIMESTAMP '2020-02-11 11:00:00', -- dtend
    5, -- dtstart dtend type
    'MO', -- days of week when this event repeats
    TIMESTAMP '2020-06-12 23:50:00',
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEvent(65,
    ARRAY['CC Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the CC curricular unit for the 1920v-LI61D Class section'],
    ARRAY[2],
    ARRAY[9],
    TIMESTAMP '2020-02-11 09:30:00', -- dtstart
    TIMESTAMP '2020-02-11 12:30:00', -- dtend
    5, -- dtstart dtend type
    'FR', -- days of week when this event repeats
    TIMESTAMP '2020-06-12 23:50:00',
    TIMESTAMP '2020-01-01 16:35:30');
-- </CC lectures> --

-- </LI61D lectures> --



-- <LI61N lectures> --
-- <PS lectures> --
CALL dbo.newEvent(64,
    ARRAY['PS Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the PS curricular unit for the 1920v-LI61N Class section'],
    ARRAY[2],
    ARRAY[9],
    TIMESTAMP '2020-02-11 20:00:00', -- dtstart
    TIMESTAMP '2020-02-11 23:00:00', -- dtend
    5, -- dtstart dtend type
    'MO', -- days of week when this event repeats
    TIMESTAMP '2020-06-12 23:50:00',
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEvent(64,
    ARRAY['PS Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the PS curricular unit for the 1920v-LI61N Class section'],
    ARRAY[2],
    ARRAY[9],
    TIMESTAMP '2020-02-11 18:30:00', -- dtstart
    TIMESTAMP '2020-02-11 20:00:00', -- dtend
    5, -- dtstart dtend type
    'WE', -- days of week when this event repeats
    TIMESTAMP '2020-06-12 23:50:00',
    TIMESTAMP '2020-01-01 16:35:30');
-- </PS lectures> --

-- <WAD lectures> --
CALL dbo.newEvent(62,
    ARRAY['WAD Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the WAD curricular unit for the 1920v-LI61N Class section'],
    ARRAY[2],
    ARRAY[9],
    TIMESTAMP '2020-02-11 18:30:00', -- dtstart
    TIMESTAMP '2020-02-11 20:00:00', -- dtend
    5, -- dtstart dtend type
    'TH', -- days of week when this event repeats
    TIMESTAMP '2020-06-12 23:50:00',
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEvent(62,
    ARRAY['WAD Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the WAD curricular unit for the 1920v-LI61N Class section'],
    ARRAY[2],
    ARRAY[9],
    TIMESTAMP '2020-02-11 20:00:00', -- dtstart
    TIMESTAMP '2020-02-11 23:00:00', -- dtend
    5, -- dtstart dtend type
    'FR', -- days of week when this event repeats
    TIMESTAMP '2020-06-12 23:50:00',
    TIMESTAMP '2020-01-01 16:35:30');
-- </WAD lecture> --

-- <CC lectures> --
CALL dbo.newEvent(66,
    ARRAY['CC Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the CC curricular unit for the 1920v-LI61N Class section'],
    ARRAY[2],
    ARRAY[9],
    TIMESTAMP '2020-02-11 18:30:00', -- dtstart
    TIMESTAMP '2020-02-11 20:00:00', -- dtend
    5, -- dtstart dtend type
    'MO', -- days of week when this event repeats
    TIMESTAMP '2020-06-12 23:50:00',
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEvent(66,
    ARRAY['CC Lecture'],
    ARRAY[2],
    ARRAY['Lectures of the CC curricular unit for the 1920v-LI61N Class section'],
    ARRAY[2],
    ARRAY[9],
    TIMESTAMP '2020-02-11 20:00:00', -- dtstart
    TIMESTAMP '2020-02-11 23:00:00', -- dtend
    5, -- dtstart dtend type
    'WE', -- days of week when this event repeats
    TIMESTAMP '2020-06-12 23:50:00',
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
-- </WAD Assignments> --

-- <CC Assignments> --
CALL dbo.newTodo(15,
    ARRAY['[CC]: Virtual Machines with GCP'],
    ARRAY[2],
    ARRAY['Creation of virtual machines using Google Cloud Platform.'],
    ARRAY[2],
    ARRAY[11],
    NULL,
    TIMESTAMP '2020-03-31 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newTodo(15,
    ARRAY['[CC]: Distributed Services'],
    ARRAY[2],
    ARRAY['Development of distributed services using RMI and gRPC.'],
    ARRAY[2],
    ARRAY[11],
    NULL,
    TIMESTAMP '2020-04-17 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newTodo(15,
    ARRAY['[CC]: Google Cloud Storage API'],
    ARRAY[2],
    ARRAY['Using the GCP API.'],
    ARRAY[2],
    ARRAY[11],
    NULL,
    TIMESTAMP '2020-05-03 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newTodo(15,
    ARRAY['[CC]: Google Firestore Service'],
    ARRAY[2],
    ARRAY['Acessing Google Firestore Service using Java API.'],
    ARRAY[2],
    ARRAY[11],
    NULL,
    TIMESTAMP '2020-05-10 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newTodo(15,
    ARRAY['[CC]: Google Pub/Sub Service'],
    ARRAY[2],
    ARRAY['Acessing Google Pub/Sub Service using Java API.'],
    ARRAY[2],
    ARRAY[11],
    NULL,
    TIMESTAMP '2020-05-17 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newTodo(15,
    ARRAY['[CC]: Instances management'],
    ARRAY[2],
    ARRAY['Managing instances and firewall rules...'],
    ARRAY[2],
    ARRAY[11],
    NULL,
    TIMESTAMP '2020-05-17 23:59:00', -- due
    5,
    TIMESTAMP '2020-05-28 16:35:30');

CALL dbo.newTodo(15,
    ARRAY['[CC]: Final Assignment'],
    ARRAY[2],
    ARRAY['Implementing a system for task execution...'],
    ARRAY[2],
    ARRAY[15],
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
CALL dbo.newEvent(13,
    ARRAY['1st Exam WAD'],
    ARRAY[2],
    ARRAY['Normal season exam for WAD-1718v'],
    ARRAY[2],
    ARRAY[7],
    TIMESTAMP '2020-06-19 18:00:00', -- dtstart, WRONG DATE CORRECT AFTER ISEL RELEASES THE LATEST EXAM MAP
    TIMESTAMP '2020-06-19 19:30:00', -- dtend
    2, -- dtstart dtend type
    NULL,
    NULL,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEvent(13,
    ARRAY['2nd Exam WAD'],
    ARRAY[2],
    ARRAY['Second season exam for WAD-1718v'],
    ARRAY[2],
    ARRAY[7],
    TIMESTAMP '2020-07-01 10:00:00', -- dtstart, WRONG DATE CORRECT AFTER ISEL RELEASES THE LATEST EXAM MAP
    TIMESTAMP '2020-07-01 12:30:00', -- dtend
    2, -- dtstart dtend type
    NULL,
    NULL,
    TIMESTAMP '2020-01-01 16:35:30');
-- </WAD Exams> --

-- <CC Exams> --
CALL dbo.newEvent(15,
    ARRAY['Exam CC'],
    ARRAY[2],
    ARRAY['Exam for CC-1920v'],
    ARRAY[2],
    ARRAY[7],
    TIMESTAMP '2020-07-01 10:00:00', -- dtstart, WRONG DATE CORRECT AFTER ISEL RELEASES THE LATEST EXAM MAP
    TIMESTAMP '2020-07-01 12:30:00', -- dtend
    2, -- dtstart dtend type
    NULL,
    NULL,
    TIMESTAMP '2020-01-01 16:35:30');
-- </CC Exams> --
-- </Exams> --


-- End of 1920v calendar information --

