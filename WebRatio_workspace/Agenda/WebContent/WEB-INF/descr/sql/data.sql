-------------------------------------------------------------------
-------------------------------------------------------------------
-------------------------------------------------------------------

	------------------------------------------
	-- -------- POPULATE ALL TABLES ----------
	------------------------------------------



-- -------- POPULATE TABLE "CATEGORY" ----------


insert into "APP"."CATEGORY" ("OID", "NAME") values (1, 'Meeting');
insert into "APP"."CATEGORY" ("OID", "NAME") values (2, 'Call');
insert into "APP"."CATEGORY" ("OID", "NAME") values (3, 'Task');



-- -------- POPULATE TABLE "HOUR" ----------

insert into "APP"."HOUR" ("OID", "HOUR")  values (1, '00:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (2, '00:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (3, '01:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (4, '01:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (5, '02:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (6, '02:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (7, '03:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (8, '03:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (9, '04:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (10, '04:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (11, '05:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (12, '05:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (13, '06:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (14, '06:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (15, '06:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (16, '07:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (17, '07:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (18, '08:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (19, '08:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (20, '09:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (21, '09:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (22, '10:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (23, '10:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (24, '11:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (25, '11:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (26, '12:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (27, '12:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (28, '13:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (29, '13:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (30, '14:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (31, '14:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (32, '15:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (33, '15:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (34, '16:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (35, '16:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (36, '17:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (37, '17:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (38, '18:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (39, '18:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (40, '19:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (41, '19:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (42, '20:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (43, '20:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (44, '21:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (45, '21:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (46, '22:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (47, '22:30:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (48, '23:00:00');
insert into "APP"."HOUR" ("OID", "HOUR")  values (49, '23:30:00');


-- -------- POPULATE TABLE "USER" ----------


insert into "APP"."MODULE" ("OID", "MODULEID", "MODULENAME") values (1, 'sv1', 'Administrators');
insert into "APP"."MODULE" ("OID", "MODULEID", "MODULENAME") values (2, 'sv2', 'Users');


-- -------- POPULATE TABLE "GROUP" ----------

insert into "APP"."GROUP" ("OID", "GROUPNAME", "MODULE_OID") values (1, 'Administrators', 1);
insert into "APP"."GROUP" ("OID", "GROUPNAME", "MODULE_OID") values (2, 'Managers', 2);
insert into "APP"."GROUP" ("OID", "GROUPNAME", "MODULE_OID") values (3, 'Employees', 2);


-- -------- POPULATE TABLE "GROUP_MODULE" ----------

insert into "APP"."GROUP_MODULE" ("GROUP_OID", "MODULE_OID") values (1, 1);
insert into "APP"."GROUP_MODULE" ("GROUP_OID", "MODULE_OID") values (2, 2);
insert into "APP"."GROUP_MODULE" ("GROUP_OID", "MODULE_OID") values (3, 2);


-- -------- POPULATE TABLE "USER" ----------

insert into "APP"."USER" ("OID", "USERNAME", "PASSWORD", "EMAIL", "FULL_NAME", "GROUP_OID")  values (1, 'admin', 'admin', 'admin@admin.com', 'Administrator', 1);
insert into "APP"."USER" ("OID", "USERNAME", "PASSWORD", "EMAIL", "FULL_NAME", "GROUP_OID")  values (2, 'user', 'user', 'user@user.com', 'keith ross',2);
insert into "APP"."USER" ("OID", "USERNAME", "PASSWORD", "EMAIL", "FULL_NAME", "GROUP_OID")  values (3, 'neil', 'carter', 'neil@user.com', 'neil carter',2);
insert into "APP"."USER" ("OID", "USERNAME", "PASSWORD", "EMAIL", "FULL_NAME", "GROUP_OID")  values (4, 'joanne', 'griffiths', 'joanne@user.com', 'joanne griffiths',2);
insert into "APP"."USER" ("OID", "USERNAME", "PASSWORD", "EMAIL", "FULL_NAME", "GROUP_OID")  values (5, 'carol', 'smith', 'sarol@user.com', 'carol smith',2);
insert into "APP"."USER" ("OID", "USERNAME", "PASSWORD", "EMAIL", "FULL_NAME", "GROUP_OID")  values (6, 'peter', 'jhonson', 'peter@user.com', 'peter jhonson',3);
insert into "APP"."USER" ("OID", "USERNAME", "PASSWORD", "EMAIL", "FULL_NAME", "GROUP_OID")  values (7, 'Douglas', 'Smith', 'douglas@user.com', 'douglas smith',3);
insert into "APP"."USER" ("OID", "USERNAME", "PASSWORD", "EMAIL", "FULL_NAME", "GROUP_OID")  values (8, 'Rene', 'Lobo', 'rene@user.com', 'rene lobo',3);
insert into "APP"."USER" ("OID", "USERNAME", "PASSWORD", "EMAIL", "FULL_NAME", "GROUP_OID")  values (9, 'Melissa', 'Perez', 'melissa@user.com', 'melissa perez',3);



-- -------- POPULATE TABLE "CALENDAR" ----------

insert into "APP"."CALENDAR" ("OID", "NAME", "CREATION", "DESCRIPTION", "USER_OID") values (1,'Default Calendar', '2013-06-03', 'Shared Calendar', 2);
insert into "APP"."CALENDAR" ("OID", "NAME", "CREATION", "DESCRIPTION", "USER_OID") values (2,'Default Calendar', '2013-06-03', 'Shared Calendar', 3);
insert into "APP"."CALENDAR" ("OID", "NAME", "CREATION", "DESCRIPTION", "USER_OID") values (3,'Default Calendar', '2013-06-03', 'Shared Calendar', 4);
insert into "APP"."CALENDAR" ("OID", "NAME", "CREATION", "DESCRIPTION", "USER_OID") values (4,'Default Calendar', '2013-06-03', 'Shared Calendar', 5);
insert into "APP"."CALENDAR" ("OID", "NAME", "CREATION", "DESCRIPTION", "USER_OID") values (5,'Default Calendar', '2013-06-03', 'Shared Calendar', 6);
insert into "APP"."CALENDAR" ("OID", "NAME", "CREATION", "DESCRIPTION", "USER_OID") values (6,'Default Calendar', '2013-06-03', 'Shared Calendar', 7);
insert into "APP"."CALENDAR" ("OID", "NAME", "CREATION", "DESCRIPTION", "USER_OID") values (7,'Default Calendar', '2013-06-03', 'Shared Calendar', 8);
insert into "APP"."CALENDAR" ("OID", "NAME", "CREATION", "DESCRIPTION", "USER_OID") values (8,'Default Calendar', '2013-06-03', 'Shared Calendar', 9);
insert into "APP"."CALENDAR" ("OID", "NAME", "CREATION", "DESCRIPTION", "USER_OID") values (9,'Family Calendar', '2013-06-03', 'Family Appointments', 2);


-- -------- UPDATE TABLE "USER" ----------

update "APP"."USER" set "CALENDAR_OID" = 1 where OID = 2;
update "APP"."USER" set "CALENDAR_OID" = 2 where OID = 3;
update "APP"."USER" set "CALENDAR_OID" = 3 where OID = 4;
update "APP"."USER" set "CALENDAR_OID" = 4 where OID = 5;
update "APP"."USER" set "CALENDAR_OID" = 5 where OID = 6;
update "APP"."USER" set "CALENDAR_OID" = 6 where OID = 7;
update "APP"."USER" set "CALENDAR_OID" = 7 where OID = 8;
update "APP"."USER" set "CALENDAR_OID" = 8 where OID = 9;



-- -------- POPULATE TABLE "APPOINTMENT" ----------

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "HOUR_OID","HOUR_OID_2", "CALENDAR_OID" ) values (1, 'meeting with janet', '2013-06-10', 0, 'Milan', 'decide the new plans for ii quarter 2007', 0, 'Do not forget bring with you all new ideas.', 1, 2, 16, 20, 1);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "HOUR_OID","HOUR_OID_2", "CALENDAR_OID" ) values (2, 'get demo feedback', '2013-06-11', 0, 'Lomazzo', 'in this meeting we are going to discuss ideas about the demo.', 0, '<p>note: forward the feedback to ms. diamond as soon as possible.</p>', 1, 2, 22, 26, 1);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "HOUR_OID","HOUR_OID_2", "CALENDAR_OID" ) values (3, 'discuss pricing with dan lechter', '2013-06-12', 0, 'Como - Lombardy', 'for the upgrade to the new licenses', 1, '<p>Bring with you all the past bills.</p>', 2, 2, 35, 39, 1);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "CALENDAR_OID" ) values (4, 'Software technical review', '2013-06-13', 1, 'Lomazzo', 'Technical review', 0, 'Bring all the software demos for showing to the board of directors.', 1, 2, 1);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "HOUR_OID","HOUR_OID_2", "CALENDAR_OID" ) values (5, 'product roadmap', '2013-06-14', 0, 'Como - Lombardy', 'meeting with all our partners', 1, '<p>Bring with you all the documents related with the meeting</p>', 1, 2, 19, 31, 1);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "HOUR_OID","HOUR_OID_2", "CALENDAR_OID" ) values (6, 'discuss pricing', '2013-06-17', 0, 'New York', 'for the upgrade to the new licenses', 1, '', 2, 2, 36, 39, 1);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "HOUR_OID","HOUR_OID_2", "CALENDAR_OID" ) values (7, 'get proposal feedback', '2013-06-18', 0, 'Napoles', 'forward the feedback to mr. graham as soon as possible.', 0, 'Do not forget send the document in pdf format.', 3, 2, 32, 37, 1);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "CALENDAR_OID" ) values (8, 'choose the new package for the product', '2013-06-19', 1, 'Lomazzo', 'We are going to choose the colors and the size of the box.', 0, 'Bring all your designs and sketchs', 1, 2, 1);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "HOUR_OID","HOUR_OID_2", "CALENDAR_OID" ) values (9, 'decide the new graphic style for the website', '2013-06-20', 0, 'Lomazzo', 'In this meeting we are going to decide which colors are going to be used in the webpage.', 0, 'Bring all your computers to see the style in real time!!!', 1, 2, 23, 33, 1);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "HOUR_OID","HOUR_OID_2", "CALENDAR_OID" ) values (10, 'Meeting with human resources', '2013-06-21', 0, 'Lomazzo', 'to discuss new salary conditions and budget', 1, 'Be prepared', 1, 2, 29, 38, 1);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "HOUR_OID","HOUR_OID_2", "CALENDAR_OID" ) values (11, 'Kickoff Meeting Product', '2013-06-24', 0, 'washington D.C', 'final meeting for product R', 0, 'Bring with you final advices and ideas.', 2, 2, 33, 40, 1);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "HOUR_OID","HOUR_OID_2", "CALENDAR_OID" ) values (12, 'Software Course', '2013-06-25', 0, 'Lomazzo', 'Java Course for everyone', 1, 'everyone must bring their own computer!!', 1, 2, 21, 27, 1);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "HOUR_OID","HOUR_OID_2", "CALENDAR_OID" ) values (13, 'New software Planning', '2013-06-27', 0, 'Como - Lombardy', 'As there is a new client we need to make a plan', 1, 'Bring the past implementation documents to guide the process', 1, 2, 34, 42, 1);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "CALENDAR_OID" ) values (14, 'Standards Document Delivery', '2013-06-28', 1, 'Como - Lombardy', 'We have to finish the document on this day!!!', 1, '', 3, 2, 1);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "HOUR_OID","HOUR_OID_2", "CALENDAR_OID" ) values (15, 'Publish new Schedule', '2013-06-10', 0, 'Lomazzo', 'Publish in the website the new schedule', 0, 'We have to make the new schedule in a calendar style.', 3, 8, 28, 37, 7);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "CALENDAR_OID" ) values (16, 'application maintenance', '2013-06-12', 1, 'Lomazzo', 'Old application maintenance', 1, 'All the related products are going to be down for this day', 3, 8, 7);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "HOUR_OID","HOUR_OID_2", "CALENDAR_OID" ) values (17, 'Synchronization trial', '2013-06-18', 0, 'washington D.C', 'We have to synchronize the old and the new one system.', 1, 'We have to synchronize the old and the new one system.', 2, 8, 27, 36, 7);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "HOUR_OID","HOUR_OID_2", "CALENDAR_OID" ) values (18, 'Meeting with product client', '2013-06-20', 0, 'Como - Lombardy', 'meeting', 1, '', 1, 8, 40, 42, 7);


insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "HOUR_OID","HOUR_OID_2", "CALENDAR_OID" ) values (19, 'product evaluation', '2013-06-26', 0, 'Lomazzo', 'New product evaluation', 0, '', 1, 8, 26, 29, 7);


insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "CALENDAR_OID" ) values (20, 'send new boxes', '2013-06-28', 1, 'Lomazzo', 'send final production', 0, 'remind to bring the bills', 3, 8, 7);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "HOUR_OID","HOUR_OID_2", "CALENDAR_OID" ) values (21, 'group meeting', '2013-06-14', 0, 'Lomazzo', 'meeting to discuss the uploaded information', 1, '', 1, 8, 21, 7, 7);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "CALENDAR_OID" ) values (22, 'change tool passwords', '2013-06-24', 1, 'Como - Lombardy', 'you have to change all the web passwords', 0, 'put a hard password.', 3, 8, 7);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "CALENDAR_OID" ) values (23, 'roadmap execution', '2013-06-20', 1, 'Ilinois', 'Roadmap execution', 0, '', 2, 9, 8);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "CALENDAR_OID" ) values (24, 'My Mother Birthday', '2013-06-11', 1, 'Milan', 'It is my mother Birthday', 0, '', 3, 2, 9);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "HOUR_OID","HOUR_OID_2", "CALENDAR_OID" ) values (25, 'Kids medical test', '2013-06-22', 0, 'Como - Lombardy', 'take the kids to the medical center', 1, '', 3, 2, 29, 31, 9);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "HOUR_OID","HOUR_OID_2", "CALENDAR_OID" ) values (26, 'Go to cinema', '2013-06-23', 0, 'Milan', 'Go to the cinema with my family', 1, '', 3, 2, 19, 24, 9);

insert into "APP"."APPOINTMENT" ("OID", "SUBJECT", "START_DATE", "ALL_DAY_EVENT", "LOCATION", "DESCRIPTION", "PRIVATE","REMINDER_MESSAGE", "CATEGORY_OID", "USER_OID", "CALENDAR_OID" ) values (27, 'Download kids school report', '2013-06-26', 1, 'Como - Lombardy', 'Download the report from the school website.', 1, '', 3, 2, 9);


-- -------- POPULATE TABLE "ATTENDEES_APPOINTMENT" ----------


insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (3,1);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (3,4);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (3,6);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (3,7);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (3,8);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (3,9);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (3,10);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (3,11);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (3,12);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (3,13);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (4,1);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (4,2);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (4,3);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (4,4);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (4,5);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (4,7);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (4,8);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (4,9);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (4,10);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (4,12);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (4,13);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (5,2);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (5,4);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (5,5);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (5,8);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (5,9);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (5,10);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (5,12);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (5,14);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (6,15);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (6,16);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (6,17);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (6,18);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (6,19);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (6,20);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (6,21);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (6,22);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (7,16);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (7,17);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (7,18);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (7,19);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (7,22);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (8,23);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (9,15);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (9,16);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (9,20);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (9,21);
insert into "APP"."ATTENDEES_APPOINTMENT" ("USER_OID", "APPOINTMENT_OID") values (9,22);



-- -------- POPULATE TABLE "CALENDAR_APPOINTMENT" ----------

insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (1,1);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (1,2);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (1,3);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (1,4);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (1,5);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (1,6);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (1,7);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (1,8);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (1,9);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (1,10);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (1,11);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (1,12);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (1,13);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (1,14);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (2,1);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (2,4);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (2,6);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (2,7);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (2,8);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (2,9);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (2,10);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (2,11);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (2,12);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (2,13);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (3,1);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (3,2);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (3,3);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (3,4);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (3,5);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (3,7);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (3,8);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (3,9);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (3,10);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (3,12);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (3,13);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (4,2);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (4,4);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (4,5);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (4,8);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (4,9);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (4,10);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (4,12);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (4,14);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (5,15);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (5,16);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (5,17);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (5,18);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (5,19);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (5,20);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (5,21);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (5,22);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (6,16);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (6,17);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (6,18);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (6,19);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (6,22);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (7,15);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (7,16);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (7,17);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (7,18);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (7,19);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (7,20);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (7,21);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (7,22);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (7,23);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (8,15);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (8,16);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (8,20);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (8,21);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (8,22);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (8,23);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (9,24);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (9,25);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (9,26);
insert into "APP"."CALENDAR_APPOINTMENT" ("CALENDAR_OID", "APPOINTMENT_OID") values (9,27);



-- -------- POPULATE TABLE "COMMENT" ----------

insert into "APP"."COMMENT" ("OID", "DATE", "COMMENT", "APPOINTMENT_OID", "USER_OID") values (1, '2013-06-21 11:55:15', 'i have several useful ideas', 3, 9);
insert into "APP"."COMMENT" ("OID", "DATE", "COMMENT", "APPOINTMENT_OID", "USER_OID") values (2, '2013-06-21 11:55:15', 'this product is almost finished!!', 3, 1);
insert into "APP"."COMMENT" ("OID", "DATE", "COMMENT", "APPOINTMENT_OID", "USER_OID") values (3, '2013-06-21 11:55:15', 'I will wait for the call', 8, 3);
insert into "APP"."COMMENT" ("OID", "DATE", "COMMENT", "APPOINTMENT_OID", "USER_OID") values (4, '2013-06-21 11:55:15', 'it is almost ready!!!!', 5, 2);
insert into "APP"."COMMENT" ("OID", "DATE", "COMMENT", "APPOINTMENT_OID", "USER_OID") values (5, '2013-06-21 11:55:15', 'it has to be bigger than the last one', 5, 8);
insert into "APP"."COMMENT" ("OID", "DATE", "COMMENT", "APPOINTMENT_OID", "USER_OID") values (6, '2013-06-21 11:55:15', 'we have to execute all the code before the meeting.', 5, 4);
insert into "APP"."COMMENT" ("OID", "DATE", "COMMENT", "APPOINTMENT_OID", "USER_OID") values (7, '2013-06-21 11:55:15', 'We have to bring all the documents!!!', 7, 1);
insert into "APP"."COMMENT" ("OID", "DATE", "COMMENT", "APPOINTMENT_OID", "USER_OID") values (8, '2013-06-21 11:55:15', 'Stronger than 12345 :)', 7, 2);
insert into "APP"."COMMENT" ("OID", "DATE", "COMMENT", "APPOINTMENT_OID", "USER_OID") values (9, '2013-06-21 11:55:15', 'I will bring all the bills for showing to them', 3, 1);
