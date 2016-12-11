create table "APP"."GROUP" (
   "OID"  integer  not null,
   "GROUPNAME"  varchar(255),
  primary key ("OID")
);


create table "APP"."MODULE" (
   "OID"  integer  not null,
   "MODULEID"  varchar(255),
   "MODULENAME"  varchar(255),
  primary key ("OID")
);



create table "APP"."USER" (
   "OID"  integer  not null,
   "USERNAME"  varchar(255),
   "PASSWORD"  varchar(255),
   "EMAIL"  varchar(255),
  primary key ("OID")
);



create table "APP"."EMPLOYEE" (
   "USER_OID"  integer  not null,
   "FIRST_NAME"  varchar(255),
   "PHONE"  varchar(255),
   "GENDER"  varchar(255),
   "LOCATION"  varchar(255),
   "BIRTHDATE"  date,
   "IM_USERNAME"  varchar(255),
   "PIC"  varchar(255),
   "ENTRYDATE"  date,
   "LAST_NAME"  varchar(255),
  primary key ("USER_OID")
);



create table "APP"."ROLE" (
   "OID"  integer  not null,
   "NAME"  varchar(255),
  primary key ("OID")
);



create table "APP"."ORGANIZATION_UNIT" (
   "OID"  integer  not null,
   "NAME"  varchar(255),
   "DESCRIPTION"  clob(10000),
   "TYPE"  varchar(255),
  primary key ("OID")
);



alter table "APP"."GROUP"  add column  "MODULE_OID"  integer;
alter table "APP"."GROUP"   add constraint FK_GROUP_MODULE foreign key ("MODULE_OID") references "APP"."MODULE" ("OID");



create table "APP"."GROUP_MODULE" (
   "GROUP_OID"  integer not null,
   "MODULE_OID"  integer not null,
  primary key ("GROUP_OID", "MODULE_OID")
);
alter table "APP"."GROUP_MODULE"   add constraint FK_GROUP_MODULE_GROUP foreign key ("GROUP_OID") references "APP"."GROUP" ("OID");
alter table "APP"."GROUP_MODULE"   add constraint FK_GROUP_MODULE_MODULE foreign key ("MODULE_OID") references "APP"."MODULE" ("OID");



alter table "APP"."USER"  add column  "GROUP_OID"  integer;
alter table "APP"."USER"   add constraint FK_USER_GROUP foreign key ("GROUP_OID") references "APP"."GROUP" ("OID");



create table "APP"."USER_ROLE" (
   "USER_OID"  integer not null,
   "GROUP_OID"  integer not null,
  primary key ("USER_OID", "GROUP_OID")
);
alter table "APP"."USER_ROLE"   add constraint FK_USER_ROLE_USER foreign key ("USER_OID") references "APP"."USER" ("OID");
alter table "APP"."USER_ROLE"   add constraint FK_USER_ROLE_GROUP foreign key ("GROUP_OID") references "APP"."GROUP" ("OID");



alter table "APP"."EMPLOYEE"  add column  "ORGANIZATION_UNIT_OID"  integer;
alter table "APP"."EMPLOYEE"   add constraint FK_EMPLOYEE_ORGANIZATION_UNIT foreign key ("ORGANIZATION_UNIT_OID") references "APP"."ORGANIZATION_UNIT" ("OID");



alter table "APP"."ORGANIZATION_UNIT"  add column  "ORGANIZATION_UNIT_OID"  integer;
alter table "APP"."ORGANIZATION_UNIT"   add constraint FK_ORGANIZATION_UNIT_ORGANIZAT foreign key ("ORGANIZATION_UNIT_OID") references "APP"."ORGANIZATION_UNIT" ("OID");



alter table "APP"."EMPLOYEE"  add column  "ROLE_OID"  integer;
alter table "APP"."EMPLOYEE"   add constraint FK_EMPLOYEE_ROLE foreign key ("ROLE_OID") references "APP"."ROLE" ("OID");



alter table "APP"."ORGANIZATION_UNIT"  add column  "EMPLOYEE_OID"  integer;
alter table "APP"."ORGANIZATION_UNIT"   add constraint FK_ORGANIZATION_UNIT_EMPLOYEE foreign key ("EMPLOYEE_OID") references "APP"."EMPLOYEE" ("USER_OID");



alter table "APP"."EMPLOYEE"   add constraint FK_EMPLOYEE_USER foreign key ("USER_OID") references "APP"."USER" ("OID");



create view "APP"."employee_full_name_view" as
select AL1."USER_OID" as "USER_OID",  (AL1."FIRST_NAME"||' '||AL1."LAST_NAME") as "DER_ATTR"
from  "APP"."EMPLOYEE" AL1 ;


