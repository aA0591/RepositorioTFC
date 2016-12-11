-- Group [Group]
create table "GROUP" (
   "OID"  int4  not null,
   "GROUP_NAME"  varchar(255),
  primary key ("OID")
);


-- Module [Module]
create table "MODULE" (
   "OID"  int4  not null,
   "MODULE_ID"  varchar(255),
   "MODULE_NAME"  varchar(255),
  primary key ("OID")
);


-- User [User]
create table "USER" (
   "OID"  int4  not null,
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
create table "COMBINATION" (
   "OID"  int4  not null,
   "START_DATE"  date,
   "END_DATE"  date,
   "HIGHLIGHTED"  bool,
   "CODE"  int4,
   "DESCRIPTION"  text,
   "NAME"  varchar(255),
   "PRICE"  float8,
   "PHOTO"  varchar(255),
  primary key ("OID")
);


-- Store [ent5]
create table "STORE" (
   "OID"  int4  not null,
   "ADDRESS"  varchar(255),
   "EMAIL"  varchar(255),
   "MAP"  varchar(255),
   "PHOTO"  varchar(255),
  primary key ("OID")
);


-- Category [pkg1#ent1]
create table "CATEGORY" (
   "OID"  int4  not null,
   "CATEGORY"  varchar(255),
  primary key ("OID")
);


-- Image [pkg1#ent7]
create table "IMAGE" (
   "OID"  int4  not null,
   "DESCRIPTION"  text,
   "PICTURE"  varchar(255),
  primary key ("OID")
);


-- Product [pkg1#ent8]
create table "PRODUCT" (
   "OID"  int4  not null,
   "CODE"  int4,
   "DESCRIPTION"  text,
   "NAME"  varchar(255),
   "PRICE"  float8,
   "THUMBNAIL"  varchar(255),
   "HIGHLIGHTED"  bool,
  primary key ("OID")
);


-- Tech Record [pkg1#ent9]
create table "TECH_RECORD" (
   "OID"  int4  not null,
   "COLORS"  varchar(255),
   "DIMENSIONS"  varchar(255),
  primary key ("OID")
);


-- Group_DefaultModule [Group2DefaultModule_DefaultModule2Group]
alter table "GROUP"  add column  "MODULE_OID"  int4;
alter table "GROUP"   add constraint fk_group_module foreign key ("MODULE_OID") references "MODULE" ("OID");
create index "idx_group_module" on "GROUP"("MODULE_OID");


-- Group_Module [Group2Module_Module2Group]
create table "GROUP_MODULE" (
   "GROUP_OID"  int4 not null,
   "MODULE_OID"  int4 not null,
  primary key ("GROUP_OID", "MODULE_OID")
);
alter table "GROUP_MODULE"   add constraint fk_group_module_group foreign key ("GROUP_OID") references "GROUP" ("OID");
alter table "GROUP_MODULE"   add constraint fk_group_module_module foreign key ("MODULE_OID") references "MODULE" ("OID");
create index "idx_group_module_group" on "GROUP_MODULE"("GROUP_OID");
create index "idx_group_module_module" on "GROUP_MODULE"("MODULE_OID");


-- User_DefaultGroup [User2DefaultGroup_DefaultGroup2User]
alter table "USER"  add column  "GROUP_OID"  int4;
alter table "USER"   add constraint fk_user_group foreign key ("GROUP_OID") references "GROUP" ("OID");
create index "idx_user_group" on "USER"("GROUP_OID");


-- User_Group [User2Group_Group2User]
create table "USER_GROUP" (
   "USER_OID"  int4 not null,
   "GROUP_OID"  int4 not null,
  primary key ("USER_OID", "GROUP_OID")
);
alter table "USER_GROUP"   add constraint fk_user_group_user foreign key ("USER_OID") references "USER" ("OID");
alter table "USER_GROUP"   add constraint fk_user_group_group foreign key ("GROUP_OID") references "GROUP" ("OID");
create index "idx_user_group_user" on "USER_GROUP"("USER_OID");
create index "idx_user_group_group" on "USER_GROUP"("GROUP_OID");


-- TechRecord_Product [rel10]
create table "TECHRECORD_PRODUCT" (
   "PRODUCT_OID"  int4 not null,
   "TECH_RECORD_OID"  int4 not null,
  primary key ("PRODUCT_OID", "TECH_RECORD_OID")
);
alter table "TECHRECORD_PRODUCT"   add constraint fk_techrecord_product_product foreign key ("PRODUCT_OID") references "PRODUCT" ("OID");
alter table "TECHRECORD_PRODUCT"   add constraint fk_techrecord_product_tech_rec foreign key ("TECH_RECORD_OID") references "TECH_RECORD" ("OID");
create index "idx_techrecord_product_product" on "TECHRECORD_PRODUCT"("PRODUCT_OID");
create index "idx_techrecord_product_tech_re" on "TECHRECORD_PRODUCT"("TECH_RECORD_OID");


-- Image_Product [rel11]
alter table "IMAGE"  add column  "PRODUCT_OID"  int4;
alter table "IMAGE"   add constraint fk_image_product foreign key ("PRODUCT_OID") references "PRODUCT" ("OID");
create index "idx_image_product" on "IMAGE"("PRODUCT_OID");


-- Category_Product [rel12]
alter table "PRODUCT"  add column  "CATEGORY_OID"  int4;
alter table "PRODUCT"   add constraint fk_product_category foreign key ("CATEGORY_OID") references "CATEGORY" ("OID");
create index "idx_product_category" on "PRODUCT"("CATEGORY_OID");


-- Product_Combination [rel9]
create table "PRODUCT_COMBINATION" (
   "PRODUCT_OID"  int4 not null,
   "COMBINATION_OID"  int4 not null,
  primary key ("PRODUCT_OID", "COMBINATION_OID")
);
alter table "PRODUCT_COMBINATION"   add constraint fk_product_combination_product foreign key ("PRODUCT_OID") references "PRODUCT" ("OID");
alter table "PRODUCT_COMBINATION"   add constraint fk_product_combination_combina foreign key ("COMBINATION_OID") references "COMBINATION" ("OID");
create index "idx_product_combination_produc" on "PRODUCT_COMBINATION"("PRODUCT_OID");
create index "idx_product_combination_combin" on "PRODUCT_COMBINATION"("COMBINATION_OID");


-- User.full name [User#att9]
create view "USER_FULL_NAME_VIEW" as
select AL1."OID" as "OID",  (AL1."FIRST_NAME"||' '||AL1."LAST_NAME") as "der_attr"
from  "USER" AL1 ;


-- Combination.# products [ent4#att51]
create view "COMBINATION_PRODUCTS_NUMBER_VI" as
select AL1."OID" as "OID", count(distinct AL2."PRODUCT_OID") as "der_attr"
from  "COMBINATION" AL1 
               left outer join "PRODUCT_COMBINATION" AL2 on AL1."OID"=AL2."COMBINATION_OID"
group by AL1."OID";


-- Category.# products [pkg1#ent1#att8]
create view "CATEGORY_PRODUCTS_NUMBER_VIEW" as
select AL1."OID" as "OID", count(distinct AL2."OID") as "der_attr"
from  "CATEGORY" AL1 
               left outer join "PRODUCT" AL2 on AL1."OID"=AL2."CATEGORY_OID"
group by AL1."OID";


-- Product.# photos [pkg1#ent8#att50]
create view "PRODUCT_PHOTOS_NUMBER_VIEW" as
select AL1."OID" as "OID", count(distinct AL2."OID") as "der_attr"
from  "PRODUCT" AL1 
               left outer join "IMAGE" AL2 on AL1."OID"=AL2."PRODUCT_OID"
group by AL1."OID";

