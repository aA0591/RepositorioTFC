-- User  [User]
create table "ACME"."USER" (
   "OID"  number(10,0)  not null,
   "USERNAME"  varchar2(255 char),
   "PASSWORD"  varchar2(255 char),
   "EMAIL"  varchar2(255 char),
  primary key ("OID")
);


-- Group  [Group]
create table "ACME"."GROUP" (
   "OID"  number(10,0)  not null,
   "GROUPNAME"  varchar2(255 char),
  primary key ("OID")
);


-- Module  [Module]
create table "ACME"."MODULE" (
   "OID"  number(10,0)  not null,
   "MODULEID"  varchar2(255 char),
   "MODULENAME"  varchar2(255 char),
  primary key ("OID")
);


-- Product  [ent1]
create table "ACME"."PRODUCT" (
   "OID"  number(10,0)  not null,
   "CODE"  number(10,0),
   "DESCRIPTION"  clob,
   "NAME"  varchar2(255 char),
   "PRICE"  double precision,
   "THUMBNAIL"  varchar2(255 char),
   "HIGHLIGHTED"  number(1,0),
  primary key ("OID")
);


-- TechRecord  [ent2]
create table "ACME"."TECHRECORD" (
   "OID"  number(10,0)  not null,
   "COLORS"  varchar2(255 char),
   "DIMENSIONS"  varchar2(255 char),
  primary key ("OID")
);


-- BigImage  [ent3]
create table "ACME"."BIGIMAGE" (
   "OID"  number(10,0)  not null,
   "DESCRIPTION"  clob,
   "PICTURE"  varchar2(255 char),
  primary key ("OID")
);


-- Combination  [ent4]
create table "ACME"."COMBINATION" (
   "OID"  number(10,0)  not null,
   "CODE"  number(10,0),
   "DESCRIPTION"  clob,
   "NAME"  varchar2(255 char),
   "PRICE"  double precision,
   "PHOTO"  varchar2(255 char),
   "START_DATE"  date,
   "END_DATE"  date,
   "HIGHLIGHTED"  number(1,0),
  primary key ("OID")
);


-- Store  [ent5]
create table "ACME"."STORE" (
   "OID"  number(10,0)  not null,
   "ADDRESS"  varchar2(255 char),
   "EMAIL"  varchar2(255 char),
   "MAP"  varchar2(255 char),
   "PHOTO"  varchar2(255 char),
  primary key ("OID")
);


-- Category  [ent6]
create table "ACME"."CATEGORY" (
   "OID"  number(10,0)  not null,
   "CATEGORY"  varchar2(255 char),
  primary key ("OID")
);


-- User_Group  [User2Group_Group2User]
create table "ACME"."USER_GROUP" (
   "USEROID"  number(10,0),
   "GROUPOID"  number(10,0),
  primary key ("USEROID", "GROUPOID")
);
alter table "ACME"."USER_GROUP"   add constraint FK_USER_GROUP_USER foreign key ("USEROID") references "ACME"."USER" ("OID");
alter table "ACME"."USER_GROUP"   add constraint FK_USER_GROUP_GROUP foreign key ("GROUPOID") references "ACME"."GROUP" ("OID");


-- User_DefaultGroup  [User2DefaultGroup_DefaultGroup2User]
alter table "ACME"."USER"  ADD  "GROUPOID"  number(10,0);
alter table "ACME"."USER"   add constraint FK_GROUP foreign key ("GROUPOID") references "ACME"."GROUP" ("OID");


-- Group_DefaultModule  [Group2DefaultModule_DefaultModule2Group]
alter table "ACME"."GROUP"  ADD  "MODULEOID"  number(10,0);
alter table "ACME"."GROUP"   add constraint FK_MODULE foreign key ("MODULEOID") references "ACME"."MODULE" ("OID");


-- Group_Module  [Group2Module_Module2Group]
create table "ACME"."GROUP_MODULE" (
   "GROUPOID"  number(10,0),
   "MODULEOID"  number(10,0),
  primary key ("GROUPOID", "MODULEOID")
);
alter table "ACME"."GROUP_MODULE"   add constraint FK_GROUP_MODULE_GROUP foreign key ("GROUPOID") references "ACME"."GROUP" ("OID");
alter table "ACME"."GROUP_MODULE"   add constraint FK_GROUP_MODULE_MODULE foreign key ("MODULEOID") references "ACME"."MODULE" ("OID");


-- Product_Combination  [rel1_rel2]
create table "ACME"."PRODUCT_COMBINATION" (
   "PRODUCTOID"  number(10,0),
   "COMBINATIONOID"  number(10,0),
  primary key ("PRODUCTOID", "COMBINATIONOID")
);
alter table "ACME"."PRODUCT_COMBINATION"   add constraint FK_PRODUCT_COMBINATION_PRODUCT foreign key ("PRODUCTOID") references "ACME"."PRODUCT" ("OID");
alter table "ACME"."PRODUCT_COMBINATION"   add constraint FK_PRODUCT_COMBINATION_COMBINA foreign key ("COMBINATIONOID") references "ACME"."COMBINATION" ("OID");


-- TechRecord_Product  [rel4_rel3]
create table "ACME"."TECHRECORD_PRODUCT" (
   "PRODUCTOID"  number(10,0),
   "TECHRECORDOID"  number(10,0),
  primary key ("PRODUCTOID", "TECHRECORDOID")
);
alter table "ACME"."TECHRECORD_PRODUCT"   add constraint FK_TECHRECORD_PRODUCT_PRODUCT foreign key ("PRODUCTOID") references "ACME"."PRODUCT" ("OID");
alter table "ACME"."TECHRECORD_PRODUCT"   add constraint FK_TECHRECORD_PRODUCT_TECHRECO foreign key ("TECHRECORDOID") references "ACME"."TECHRECORD" ("OID");


-- BigImage_Product  [rel6_rel5]
alter table "ACME"."BIGIMAGE"  ADD  "PRODUCTOID"  number(10,0);
alter table "ACME"."BIGIMAGE"   add constraint FK_PRODUCT foreign key ("PRODUCTOID") references "ACME"."PRODUCT" ("OID");


-- Category_Product  [rel8_rel7]
alter table "ACME"."PRODUCT"  ADD  "CATEGORYOID"  number(10,0);
alter table "ACME"."PRODUCT"   add constraint FK_CATEGORY foreign key ("CATEGORYOID") references "ACME"."CATEGORY" ("OID");