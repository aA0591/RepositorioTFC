-- Group [Group]
create table "APP"."GROUP" (
   "OID"  integer  not null,
   "GROUP_NAME"  varchar(255),
  primary key ("OID")
);


-- Module [Module]
create table "APP"."MODULE" (
   "OID"  integer  not null,
   "MODULE_ID"  varchar(255),
   "MODULE_NAME"  varchar(255),
  primary key ("OID")
);


-- User [User]
create table "APP"."USER" (
   "OID"  integer  not null,
   "EMAIL"  varchar(255),
   "PASSWORD"  varchar(255),
   "FIRST_NAME"  varchar(255),
   "LAST_NAME"  varchar(255),
   "GENDER"  varchar(255),
   "BIRTH_DATE"  date,
   "SHIPPING_ADDRESS"  varchar(255),
   "USERNAME"  varchar(255),
  primary key ("OID")
);


-- Combination [ent4]
create table "APP"."COMBINATION" (
   "OID"  integer  not null,
   "START_DATE"  date,
   "END_DATE"  date,
   "HIGHLIGHTED"  smallint,
   "CODE"  integer,
   "DESCRIPTION"  clob(10000),
   "NAME"  varchar(255),
   "PRICE"  double,
   "PHOTO"  varchar(255),
  primary key ("OID")
);


-- Store [ent5]
create table "APP"."STORE" (
   "OID"  integer  not null,
   "ADDRESS"  varchar(255),
   "EMAIL"  varchar(255),
   "MAP"  varchar(255),
   "PHOTO"  varchar(255),
  primary key ("OID")
);


-- Category [pkg1#ent1]
create table "APP"."CATEGORY" (
   "OID"  integer  not null,
   "CATEGORY"  varchar(255),
  primary key ("OID")
);


-- Image [pkg1#ent7]
create table "APP"."IMAGE" (
   "OID"  integer  not null,
   "DESCRIPTION"  clob(10000),
   "PICTURE"  varchar(255),
  primary key ("OID")
);


-- Product [pkg1#ent8]
create table "APP"."PRODUCT" (
   "OID"  integer  not null,
   "CODE"  integer,
   "DESCRIPTION"  clob(10000),
   "NAME"  varchar(255),
   "PRICE"  double,
   "THUMBNAIL"  varchar(255),
   "HIGHLIGHTED"  smallint,
  primary key ("OID")
);


-- Tech Record [pkg1#ent9]
create table "APP"."TECH_RECORD" (
   "OID"  integer  not null,
   "COLORS"  varchar(255),
   "DIMENSIONS"  varchar(255),
  primary key ("OID")
);


-- Group_DefaultModule [Group2DefaultModule_DefaultModule2Group]
alter table "APP"."GROUP"  add column  "MODULE_OID"  integer;
alter table "APP"."GROUP"   add constraint FK_GROUP_MODULE foreign key ("MODULE_OID") references "APP"."MODULE" ("OID");
create index "IDX_GROUP_MODULE" on "APP"."GROUP"("MODULE_OID");


-- Group_Module [Group2Module_Module2Group]
create table "APP"."GROUP_MODULE" (
   "GROUP_OID"  integer not null,
   "MODULE_OID"  integer not null,
  primary key ("GROUP_OID", "MODULE_OID")
);
alter table "APP"."GROUP_MODULE"   add constraint FK_GROUP_MODULE_GROUP foreign key ("GROUP_OID") references "APP"."GROUP" ("OID");
alter table "APP"."GROUP_MODULE"   add constraint FK_GROUP_MODULE_MODULE foreign key ("MODULE_OID") references "APP"."MODULE" ("OID");
create index "IDX_GROUP_MODULE_GROUP" on "APP"."GROUP_MODULE"("GROUP_OID");
create index "IDX_GROUP_MODULE_MODULE" on "APP"."GROUP_MODULE"("MODULE_OID");


-- User_DefaultGroup [User2DefaultGroup_DefaultGroup2User]
alter table "APP"."USER"  add column  "GROUP_OID"  integer;
alter table "APP"."USER"   add constraint FK_USER_GROUP foreign key ("GROUP_OID") references "APP"."GROUP" ("OID");
create index "IDX_USER_GROUP" on "APP"."USER"("GROUP_OID");


-- User_Group [User2Group_Group2User]
create table "APP"."USER_GROUP" (
   "USER_OID"  integer not null,
   "GROUP_OID"  integer not null,
  primary key ("USER_OID", "GROUP_OID")
);
alter table "APP"."USER_GROUP"   add constraint FK_USER_GROUP_USER foreign key ("USER_OID") references "APP"."USER" ("OID");
alter table "APP"."USER_GROUP"   add constraint FK_USER_GROUP_GROUP foreign key ("GROUP_OID") references "APP"."GROUP" ("OID");
create index "IDX_USER_GROUP_USER" on "APP"."USER_GROUP"("USER_OID");
create index "IDX_USER_GROUP_GROUP" on "APP"."USER_GROUP"("GROUP_OID");


-- TechRecord_Product [rel10]
create table "APP"."TECHRECORD_PRODUCT" (
   "PRODUCT_OID"  integer not null,
   "TECH_RECORD_OID"  integer not null,
  primary key ("PRODUCT_OID", "TECH_RECORD_OID")
);
alter table "APP"."TECHRECORD_PRODUCT"   add constraint FK_TECHRECORD_PRODUCT_PRODUCT foreign key ("PRODUCT_OID") references "APP"."PRODUCT" ("OID");
alter table "APP"."TECHRECORD_PRODUCT"   add constraint FK_TECHRECORD_PRODUCT_TECH_REC foreign key ("TECH_RECORD_OID") references "APP"."TECH_RECORD" ("OID");
create index "IDX_TECHRECORD_PRODUCT_PRODUCT" on "APP"."TECHRECORD_PRODUCT"("PRODUCT_OID");
create index "IDX_TECHRECORD_PRODUCT_TECH_RE" on "APP"."TECHRECORD_PRODUCT"("TECH_RECORD_OID");


-- Image_Product [rel11]
alter table "APP"."IMAGE"  add column  "PRODUCT_OID"  integer;
alter table "APP"."IMAGE"   add constraint FK_IMAGE_PRODUCT foreign key ("PRODUCT_OID") references "APP"."PRODUCT" ("OID");
create index "IDX_IMAGE_PRODUCT" on "APP"."IMAGE"("PRODUCT_OID");


-- Category_Product [rel12]
alter table "APP"."PRODUCT"  add column  "CATEGORY_OID"  integer;
alter table "APP"."PRODUCT"   add constraint FK_PRODUCT_CATEGORY foreign key ("CATEGORY_OID") references "APP"."CATEGORY" ("OID");
create index "IDX_PRODUCT_CATEGORY" on "APP"."PRODUCT"("CATEGORY_OID");


-- Product_Combination [rel9]
create table "APP"."PRODUCT_COMBINATION" (
   "PRODUCT_OID"  integer not null,
   "COMBINATION_OID"  integer not null,
  primary key ("PRODUCT_OID", "COMBINATION_OID")
);
alter table "APP"."PRODUCT_COMBINATION"   add constraint FK_PRODUCT_COMBINATION_PRODUCT foreign key ("PRODUCT_OID") references "APP"."PRODUCT" ("OID");
alter table "APP"."PRODUCT_COMBINATION"   add constraint FK_PRODUCT_COMBINATION_COMBINA foreign key ("COMBINATION_OID") references "APP"."COMBINATION" ("OID");
create index "IDX_PRODUCT_COMBINATION_PRODUC" on "APP"."PRODUCT_COMBINATION"("PRODUCT_OID");
create index "IDX_PRODUCT_COMBINATION_COMBIN" on "APP"."PRODUCT_COMBINATION"("COMBINATION_OID");


-- User.full name [User#att9]
create view "APP"."USER_FULL_NAME_VIEW" as
select AL1."OID" as "OID",  (AL1."FIRST_NAME"||' '||AL1."LAST_NAME") as "DER_ATTR"
from  "APP"."USER" AL1 ;


-- Combination.# products [ent4#att51]
create view "APP"."COMBINATION_PRODUCTS_NUMBER_VI" as
select AL1."OID" as "OID", count(distinct AL2."PRODUCT_OID") as "DER_ATTR"
from  "APP"."COMBINATION" AL1 
               left outer join "APP"."PRODUCT_COMBINATION" AL2 on AL1."OID"=AL2."COMBINATION_OID"
group by AL1."OID";


-- Category.# products [pkg1#ent1#att8]
create view "APP"."CATEGORY_PRODUCTS_NUMBER_VIEW" as
select AL1."OID" as "OID", count(distinct AL2."OID") as "DER_ATTR"
from  "APP"."CATEGORY" AL1 
               left outer join "APP"."PRODUCT" AL2 on AL1."OID"=AL2."CATEGORY_OID"
group by AL1."OID";


-- Product.# photos [pkg1#ent8#att50]
create view "APP"."PRODUCT_PHOTOS_NUMBER_VIEW" as
select AL1."OID" as "OID", count(distinct AL2."OID") as "DER_ATTR"
from  "APP"."PRODUCT" AL1 
               left outer join "APP"."IMAGE" AL2 on AL1."OID"=AL2."PRODUCT_OID"
group by AL1."OID";
