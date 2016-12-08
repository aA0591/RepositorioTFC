-- Group [Group]
create table "APP"."GROUP" (
   "OID"  integer  not null,
   "GROUPNAME"  varchar(255),
  primary key ("OID")
);


-- Module [Module]
create table "APP"."MODULE" (
   "OID"  integer  not null,
   "MODULEID"  varchar(255),
   "MODULENAME"  varchar(255),
  primary key ("OID")
);


-- User [User]
create table "APP"."USER" (
   "OID"  integer  not null,
   "USERNAME"  varchar(255),
   "PASSWORD"  varchar(255),
   "EMAIL"  varchar(255),
   "FULL_NAME"  varchar(255),
  primary key ("OID")
);


-- Appointment [ent1]
create table "APP"."APPOINTMENT" (
   "OID"  integer  not null,
   "SUBJECT"  varchar(255),
   "START_DATE"  date,
   "ALL_DAY_EVENT"  smallint,
   "LOCATION"  varchar(255),
   "DESCRIPTION"  clob(10000),
   "PRIVATE"  smallint,
   "REMINDER_MESSAGE"  clob(10000),
  primary key ("OID")
);


-- Category [ent2]
create table "APP"."CATEGORY" (
   "OID"  integer  not null,
   "NAME"  varchar(255),
  primary key ("OID")
);


-- Calendar [ent3]
create table "APP"."CALENDAR" (
   "OID"  integer  not null,
   "NAME"  varchar(255),
   "CREATION"  date,
   "DESCRIPTION"  clob(10000),
  primary key ("OID")
);


-- Hour [ent5]
create table "APP"."HOUR" (
   "OID"  integer  not null,
   "HOUR"  time,
  primary key ("OID")
);


-- Comment [ent6]
create table "APP"."COMMENT" (
   "OID"  integer  not null,
   "DATE"  timestamp,
   "COMMENT"  clob(10000),
  primary key ("OID")
);


-- Group_DefaultModule [Group2DefaultModule_DefaultModule2Group]
alter table "APP"."GROUP"  add column  "MODULE_OID"  integer;
alter table "APP"."GROUP"   add constraint FK_GROUP_MODULE foreign key ("MODULE_OID") references "APP"."MODULE" ("OID");


-- Group_Module [Group2Module_Module2Group]
create table "APP"."GROUP_MODULE" (
   "GROUP_OID"  integer not null,
   "MODULE_OID"  integer not null,
  primary key ("GROUP_OID", "MODULE_OID")
);
alter table "APP"."GROUP_MODULE"   add constraint FK_GROUP_MODULE_GROUP foreign key ("GROUP_OID") references "APP"."GROUP" ("OID");
alter table "APP"."GROUP_MODULE"   add constraint FK_GROUP_MODULE_MODULE foreign key ("MODULE_OID") references "APP"."MODULE" ("OID");


-- User_DefaultGroup [User2DefaultGroup_DefaultGroup2User]
alter table "APP"."USER"  add column  "GROUP_OID"  integer;
alter table "APP"."USER"   add constraint FK_USER_GROUP foreign key ("GROUP_OID") references "APP"."GROUP" ("OID");


-- User_Group [User2Group_Group2User]
create table "APP"."USER_GROUP" (
   "USER_OID"  integer not null,
   "GROUP_OID"  integer not null,
  primary key ("USER_OID", "GROUP_OID")
);
alter table "APP"."USER_GROUP"   add constraint FK_USER_GROUP_USER foreign key ("USER_OID") references "APP"."USER" ("OID");
alter table "APP"."USER_GROUP"   add constraint FK_USER_GROUP_GROUP foreign key ("GROUP_OID") references "APP"."GROUP" ("OID");


-- Comment_User [rel13]
alter table "APP"."COMMENT"  add column  "USER_OID"  integer;
alter table "APP"."COMMENT"   add constraint FK_COMMENT_USER foreign key ("USER_OID") references "APP"."USER" ("OID");


-- Appointment_Category [rel15]
alter table "APP"."APPOINTMENT"  add column  "CATEGORY_OID"  integer;
alter table "APP"."APPOINTMENT"   add constraint FK_APPOINTMENT_CATEGORY foreign key ("CATEGORY_OID") references "APP"."CATEGORY" ("OID");


-- Appointment_Comment [rel16]
alter table "APP"."COMMENT"  add column  "APPOINTMENT_OID"  integer;
alter table "APP"."COMMENT"   add constraint FK_COMMENT_APPOINTMENT foreign key ("APPOINTMENT_OID") references "APP"."APPOINTMENT" ("OID");


-- Attendees_Appointment [rel20]
create table "APP"."ATTENDEES_APPOINTMENT" (
   "USER_OID"  integer not null,
   "APPOINTMENT_OID"  integer not null,
  primary key ("USER_OID", "APPOINTMENT_OID")
);
alter table "APP"."ATTENDEES_APPOINTMENT"   add constraint FK_ATTENDEES_APPOINTMENT_USER foreign key ("USER_OID") references "APP"."USER" ("OID");
alter table "APP"."ATTENDEES_APPOINTMENT"   add constraint FK_ATTENDEES_APPOINTMENT_APPOI foreign key ("APPOINTMENT_OID") references "APP"."APPOINTMENT" ("OID");


-- User_Calendar [rel21]
alter table "APP"."CALENDAR"  add column  "USER_OID"  integer;
alter table "APP"."CALENDAR"   add constraint FK_CALENDAR_USER foreign key ("USER_OID") references "APP"."USER" ("OID");


-- Calendar_Appointment [rel22]
create table "APP"."CALENDAR_APPOINTMENT" (
   "CALENDAR_OID"  integer not null,
   "APPOINTMENT_OID"  integer not null,
  primary key ("CALENDAR_OID", "APPOINTMENT_OID")
);
alter table "APP"."CALENDAR_APPOINTMENT"   add constraint FK_CALENDAR_APPOINTMENT_CALEND foreign key ("CALENDAR_OID") references "APP"."CALENDAR" ("OID");
alter table "APP"."CALENDAR_APPOINTMENT"   add constraint FK_CALENDAR_APPOINTMENT_APPOIN foreign key ("APPOINTMENT_OID") references "APP"."APPOINTMENT" ("OID");


-- Appointment_Owner [rel3]
alter table "APP"."APPOINTMENT"  add column  "USER_OID"  integer;
alter table "APP"."APPOINTMENT"   add constraint FK_APPOINTMENT_USER foreign key ("USER_OID") references "APP"."USER" ("OID");


-- Appointment_StartTime [rel4]
alter table "APP"."APPOINTMENT"  add column  "HOUR_OID"  integer;
alter table "APP"."APPOINTMENT"   add constraint FK_APPOINTMENT_HOUR_2 foreign key ("HOUR_OID") references "APP"."HOUR" ("OID");


-- DefaultCalendar_User [rel5]
alter table "APP"."USER"  add column  "CALENDAR_OID"  integer;
alter table "APP"."USER"   add constraint FK_USER_CALENDAR foreign key ("CALENDAR_OID") references "APP"."CALENDAR" ("OID");


-- Appointment_EndTime [rel6]
alter table "APP"."APPOINTMENT"  add column  "HOUR_OID_2"  integer;
alter table "APP"."APPOINTMENT"   add constraint FK_APPOINTMENT_HOUR foreign key ("HOUR_OID_2") references "APP"."HOUR" ("OID");


-- Appointment_OriginCalendar [rel7]
alter table "APP"."APPOINTMENT"  add column  "CALENDAR_OID"  integer;
alter table "APP"."APPOINTMENT"   add constraint FK_APPOINTMENT_CALENDAR foreign key ("CALENDAR_OID") references "APP"."CALENDAR" ("OID");


-- Appointment.category name [ent1#att12]
create view "APP"."APPOINTMENT_CATEGORY_NAME_VIEW" as
select AL1."OID" as "OID", AL2."NAME" as "DER_ATTR"
from  "APP"."APPOINTMENT" AL1 
               left outer join "APP"."CATEGORY" AL2 on AL1."CATEGORY_OID"=AL2."OID";


-- Comment.poster [ent6#att27]
create view "APP"."COMMENT_POSTER_VIEW" as
select AL1."OID" as "OID", AL2."FULL_NAME" as "DER_ATTR"
from  "APP"."COMMENT" AL1 
               left outer join "APP"."USER" AL2 on AL1."USER_OID"=AL2."OID";


-- User_AllGroup [rel14]
create view "APP"."USER_2_ALLGROUP_VIEW" as
select AL1."OID" as "S_OID", AL2."OID" as "T_OID"
from  "APP"."USER" AL1 ,
         "APP"."GROUP" AL2 
where AL2."OID" in (select AL4."GROUP_OID"
from  "APP"."USER" AL3 
               inner join "APP"."USER_GROUP" AL4 on AL3."OID"=AL4."USER_OID"
where AL3."OID" = AL1."OID") OR AL2."OID" in (select AL5."GROUP_OID"
from  "APP"."USER" AL5 
where AL5."OID" = AL1."OID");



