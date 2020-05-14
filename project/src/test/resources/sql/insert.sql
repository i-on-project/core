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
('DM',  'Discrete Mathematics');
	
INSERT INTO dbo.ProgrammeOffer(programmeId, courseId, optional, termNumber) VALUES 
(1, 2, TRUE,  3),
(1, 1, FALSE, 4),
(1, 3, FALSE, 1),
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
CALL dbo.sp_classCalendarCreate('1920v', 1);
CALL dbo.sp_classCalendarCreate('1920v', 2);
CALL dbo.sp_classCalendarCreate('1920v', 3);
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
CALL dbo.sp_classSectionCalendarCreate(13, '1D');
CALL dbo.sp_classSectionCalendarCreate(13, '2D');
CALL dbo.sp_classSectionCalendarCreate(13, '1N');
CALL dbo.sp_classSectionCalendarCreate(14, '1D');
CALL dbo.sp_classSectionCalendarCreate(14, '2D');
CALL dbo.sp_classSectionCalendarCreate(14, '1N');
CALL dbo.sp_classSectionCalendarCreate(15, '1D');
CALL dbo.sp_classSectionCalendarCreate(15, '2D');
CALL dbo.sp_classSectionCalendarCreate(15, '1N');
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

CALL dbo.newJournal(1,
    ARRAY['some summary', 'uma sinopse'], -- summary
    ARRAY[8, 1], -- summary's language
    ARRAY['this is a description'], -- description
    ARRAY[8], -- description's language
    ARRAY[1, 2, 3], -- categories
    ARRAY['https://www.google.com'], -- attachments
    TIMESTAMP '2020-04-10 14:00:00', -- DTstart
    5,
    TIMESTAMP '2020-01-01 16:35:30');

-- WAD 1718v Class
CALL dbo.newTodo(2,
    ARRAY['[WAD]: Assignment #1'],
    ARRAY[8],
    ARRAY['The first assignment. The goal is to implement an HTTP API...'],
    ARRAY[8],
    ARRAY[1,2],
    ARRAY['https://tools.ietf.org/html/rfc7231'],
    TIMESTAMP '2021-04-19 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newTodo(2,
    ARRAY['[WAD]: Assignment #2'],
    ARRAY[8],
    ARRAY['The second assignment. The goal is to implement a Web Client for the API...'],
    ARRAY[8],
    ARRAY[1,2],
    NULL,
    TIMESTAMP '2021-06-12 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEvent(2,
    ARRAY['1st Exam WAD'],
    ARRAY[8],
    ARRAY['Normal season exam for WAD-1920v'],
    ARRAY[8],
    ARRAY[8],
    TIMESTAMP '2020-06-19 18:00:00', -- dtstart
    TIMESTAMP '2020-06-19 19:30:00', -- dtend
    2, -- dtstart dtend type
    'FR', -- days of week when this event repeats
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEvent(2,
    ARRAY['2st Exam WAD'],
    ARRAY[8],
    ARRAY['Second season exam for WAD-1920v'],
    ARRAY[8],
    ARRAY[8],
    TIMESTAMP '2020-07-01 10:00:00', -- dtstart
    TIMESTAMP '2020-07-01 12:30:00', -- dtend
    2, -- dtstart dtend type
    'WE', -- days of week when this event repeats
    TIMESTAMP '2020-01-01 16:35:30');

-- WAD 1718v 1D
CALL dbo.newEvent(28,
    ARRAY['WAD Monday Lecture'],
    ARRAY[8],
    ARRAY['Lectures of the WAD curricular unit, for the 1718v-1D Class section'],
    ARRAY[8],
    ARRAY[8],
    TIMESTAMP '2020-02-10 10:00:00', -- dtstart
    TIMESTAMP '2020-02-10 12:30:00', -- dtend
    5, -- dtstart dtend type
    'MO', -- days of week when this event repeats
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEvent(28,
    ARRAY['WAD Wednesday Lecture'],
    ARRAY[8],
    ARRAY['Lectures of the WAD curricular unit, for the 1718v-1D Class section'],
    ARRAY[8],
    ARRAY[8],
    TIMESTAMP '2020-02-12 10:00:00', -- dtstart
    TIMESTAMP '2020-02-12 12:30:00', -- dtend
    5, -- dtstart dtend type
    'WE', -- days of week when this event repeats
    TIMESTAMP '2020-01-01 16:35:30');

-- WAD 1718v 2D
CALL dbo.newEvent(29,
    ARRAY['WAD Monday Lecture'],
    ARRAY[8],
    ARRAY['Lectures of the WAD curricular unit, for the 1718v-1N Class section'],
    ARRAY[8],
    ARRAY[8],
    TIMESTAMP '2020-02-10 10:00:00', -- dtstart
    TIMESTAMP '2020-02-10 12:30:00', -- dtend
    5, -- dtstart dtend type
    'MO', -- days of week when this event repeats
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEvent(29,
    ARRAY['WAD Wednesday Lecture'],
    ARRAY[8],
    ARRAY['Lectures of the WAD curricular unit, for the 1718v-2D Class section'],
    ARRAY[8],
    ARRAY[8],
    TIMESTAMP '2020-02-12 10:00:00', -- dtstart
    TIMESTAMP '2020-02-12 12:30:00', -- dtend
    5, -- dtstart dtend type
    'WE', -- days of week when this event repeats
    TIMESTAMP '2020-01-01 16:35:30');

-- WAD 1718v 1N
CALL dbo.newEvent(30,
    ARRAY['WAD Lecture'],
    ARRAY[8],
    ARRAY['Lectures of the WAD curricular unit, for the 1718v-1N Class section'],
    ARRAY[8],
    ARRAY[8],
    TIMESTAMP '2020-02-11 10:00:00', -- dtstart
    TIMESTAMP '2020-02-11 12:30:00', -- dtend
    5, -- dtstart dtend type
    'TU,WE', -- days of week when this event repeats
    TIMESTAMP '2020-01-01 16:35:30');

-- SL 1718v Class
CALL dbo.newTodo(1,
    ARRAY['[SL]: Assignment #1'],
    ARRAY[8],
    ARRAY['The first assignment. The goal is to implement a CLI...'],
    ARRAY[8],
    ARRAY[1,2],
    ARRAY['https://tools.ietf.org/html/rfc7231'],
    TIMESTAMP '2021-04-08 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newTodo(1,
    ARRAY['[SL]: Assignment #2'],
    ARRAY[8],
    ARRAY['The second assignment. The goal is to implement a Web Client for the API...'],
    ARRAY[8],
    ARRAY[1,2],
    NULL,
    TIMESTAMP '2021-06-20 23:59:00', -- due
    5,
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEvent(1,
    ARRAY['1st Exam SL'],
    ARRAY[8],
    ARRAY['Normal season exam for SL-1718v'],
    ARRAY[8],
    ARRAY[8],
    TIMESTAMP '2020-06-10 18:00:00', -- dtstart
    TIMESTAMP '2020-06-10 19:30:00', -- dtend
    2, -- dtstart dtend type
    'FR', -- days of week when this event repeats
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEvent(1,
    ARRAY['2st Exam SL'],
    ARRAY[8],
    ARRAY['Second season exam for SL-1718v'],
    ARRAY[8],
    ARRAY[8],
    TIMESTAMP '2020-06-24 10:00:00', -- dtstart
    TIMESTAMP '2020-07-24 12:30:00', -- dtend
    2, -- dtstart dtend type
    'WE', -- days of week when this event repeats
    TIMESTAMP '2020-01-01 16:35:30');

-- SL 1718v 1D
CALL dbo.newEvent(25,
    ARRAY['SL Monday Lecture'],
    ARRAY[8],
    ARRAY['Lectures of the SL curricular unit, for the 1718v-1D Class section'],
    ARRAY[8],
    ARRAY[8],
    TIMESTAMP '2020-02-10 10:00:00', -- dtstart
    TIMESTAMP '2020-02-10 12:30:00', -- dtend
    5, -- dtstart dtend type
    'MO', -- days of week when this event repeats
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEvent(25,
    ARRAY['SL Wednesday Lecture'],
    ARRAY[8],
    ARRAY['Lectures of the SL curricular unit, for the 1718v-1D Class section'],
    ARRAY[8],
    ARRAY[8],
    TIMESTAMP '2020-02-12 10:00:00', -- dtstart
    TIMESTAMP '2020-02-12 12:30:00', -- dtend
    5, -- dtstart dtend type
    'WE', -- days of week when this event repeats
    TIMESTAMP '2020-01-01 16:35:30');

-- SL 1718v 2D
CALL dbo.newEvent(26,
    ARRAY['SL Monday Lecture'],
    ARRAY[8],
    ARRAY['Lectures of the SL curricular unit, for the 1718v-1N Class section'],
    ARRAY[8],
    ARRAY[8],
    TIMESTAMP '2020-02-10 10:00:00', -- dtstart
    TIMESTAMP '2020-02-10 12:30:00', -- dtend
    5, -- dtstart dtend type
    'MO', -- days of week when this event repeats
    TIMESTAMP '2020-01-01 16:35:30');

CALL dbo.newEvent(26,
    ARRAY['SL Wednesday Lecture'],
    ARRAY[8],
    ARRAY['Lectures of the SL curricular unit, for the 1718v-2D Class section'],
    ARRAY[8],
    ARRAY[8],
    TIMESTAMP '2020-02-12 10:00:00', -- dtstart
    TIMESTAMP '2020-02-12 12:30:00', -- dtend
    5, -- dtstart dtend type
    'WE', -- days of week when this event repeats
    TIMESTAMP '2020-01-01 16:35:30');

-- SL 1718v 1N
CALL dbo.newEvent(27,
    ARRAY['SL Lecture'],
    ARRAY[8],
    ARRAY['Lectures of the SL curricular unit, for the 1718v-1N Class section'],
    ARRAY[8],
    ARRAY[8],
    TIMESTAMP '2020-02-11 10:00:00', -- dtstart
    TIMESTAMP '2020-02-11 12:30:00', -- dtend
    5, -- dtstart dtend type
    'TU,WE', -- days of week when this event repeats
    TIMESTAMP '2020-01-01 16:35:30');

