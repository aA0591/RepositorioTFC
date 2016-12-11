-- Group [Group]
create table [GROUP] (
   [OID]  int  not null,
   [GROUP_NAME]  varchar(255),
  primary key ([OID])
);


-- Module [Module]
create table [MODULE] (
   [OID]  int  not null,
   [MODULE_ID]  varchar(255),
   [MODULE_NAME]  varchar(255),
  primary key ([OID])
);


-- User [User]
create table [USER] (
   [OID]  int  not null,
   [EMAIL]  varchar(255),
   [PASSWORD]  varchar(255),
   [FIRST_NAME]  varchar(255),
   [LAST_NAME]  varchar(255),
   [GENDER]  varchar(255),
   [BIRTH_DATE]  datetime,
   [SHIPPING_ADDRESS]  varchar(255),
   [USERNAME]  varchar(255),
  primary key ([OID])
);


-- Combination [ent4]
create table [COMBINATION] (
   [OID]  int  not null,
   [START_DATE]  datetime,
   [END_DATE]  datetime,
   [HIGHLIGHTED]  tinyint,
   [CODE]  int,
   [DESCRIPTION]  text,
   [NAME]  varchar(255),
   [PRICE]  double precision,
   [PHOTO]  varchar(255),
  primary key ([OID])
);


-- Store [ent5]
create table [STORE] (
   [OID]  int  not null,
   [ADDRESS]  varchar(255),
   [EMAIL]  varchar(255),
   [MAP]  varchar(255),
   [PHOTO]  varchar(255),
  primary key ([OID])
);


-- Category [pkg1#ent1]
create table [CATEGORY] (
   [OID]  int  not null,
   [CATEGORY]  varchar(255),
  primary key ([OID])
);


-- Image [pkg1#ent7]
create table [IMAGE] (
   [OID]  int  not null,
   [DESCRIPTION]  text,
   [PICTURE]  varchar(255),
  primary key ([OID])
);


-- Product [pkg1#ent8]
create table [PRODUCT] (
   [OID]  int  not null,
   [CODE]  int,
   [DESCRIPTION]  text,
   [NAME]  varchar(255),
   [PRICE]  double precision,
   [THUMBNAIL]  varchar(255),
   [HIGHLIGHTED]  tinyint,
  primary key ([OID])
);


-- Tech Record [pkg1#ent9]
create table [TECH_RECORD] (
   [OID]  int  not null,
   [COLORS]  varchar(255),
   [DIMENSIONS]  varchar(255),
  primary key ([OID])
);


-- Group_DefaultModule [Group2DefaultModule_DefaultModule2Group]
alter table [GROUP]  add  [MODULE_OID]  int;
alter table [GROUP]   add constraint FK_GROUP_MODULE foreign key ([MODULE_OID]) references [MODULE] ([OID]);
create index [IDX_GROUP_MODULE] on [GROUP]([MODULE_OID]);


-- Group_Module [Group2Module_Module2Group]
create table [GROUP_MODULE] (
   [GROUP_OID]  int not null,
   [MODULE_OID]  int not null,
  primary key ([GROUP_OID], [MODULE_OID])
);
alter table [GROUP_MODULE]   add constraint FK_GROUP_MODULE_GROUP foreign key ([GROUP_OID]) references [GROUP] ([OID]);
alter table [GROUP_MODULE]   add constraint FK_GROUP_MODULE_MODULE foreign key ([MODULE_OID]) references [MODULE] ([OID]);
create index [IDX_GROUP_MODULE_GROUP] on [GROUP_MODULE]([GROUP_OID]);
create index [IDX_GROUP_MODULE_MODULE] on [GROUP_MODULE]([MODULE_OID]);


-- User_DefaultGroup [User2DefaultGroup_DefaultGroup2User]
alter table [USER]  add  [GROUP_OID]  int;
alter table [USER]   add constraint FK_USER_GROUP foreign key ([GROUP_OID]) references [GROUP] ([OID]);
create index [IDX_USER_GROUP] on [USER]([GROUP_OID]);


-- User_Group [User2Group_Group2User]
create table [USER_GROUP] (
   [USER_OID]  int not null,
   [GROUP_OID]  int not null,
  primary key ([USER_OID], [GROUP_OID])
);
alter table [USER_GROUP]   add constraint FK_USER_GROUP_USER foreign key ([USER_OID]) references [USER] ([OID]);
alter table [USER_GROUP]   add constraint FK_USER_GROUP_GROUP foreign key ([GROUP_OID]) references [GROUP] ([OID]);
create index [IDX_USER_GROUP_USER] on [USER_GROUP]([USER_OID]);
create index [IDX_USER_GROUP_GROUP] on [USER_GROUP]([GROUP_OID]);


-- TechRecord_Product [rel10]
create table [TECHRECORD_PRODUCT] (
   [PRODUCT_OID]  int not null,
   [TECH_RECORD_OID]  int not null,
  primary key ([PRODUCT_OID], [TECH_RECORD_OID])
);
alter table [TECHRECORD_PRODUCT]   add constraint FK_TECHRECORD_PRODUCT_PRODUCT foreign key ([PRODUCT_OID]) references [PRODUCT] ([OID]);
alter table [TECHRECORD_PRODUCT]   add constraint FK_TECHRECORD_PRODUCT_TECH_REC foreign key ([TECH_RECORD_OID]) references [TECH_RECORD] ([OID]);
create index [IDX_TECHRECORD_PRODUCT_PRODUCT] on [TECHRECORD_PRODUCT]([PRODUCT_OID]);
create index [IDX_TECHRECORD_PRODUCT_TECH_RE] on [TECHRECORD_PRODUCT]([TECH_RECORD_OID]);


-- Image_Product [rel11]
alter table [IMAGE]  add  [PRODUCT_OID]  int;
alter table [IMAGE]   add constraint FK_IMAGE_PRODUCT foreign key ([PRODUCT_OID]) references [PRODUCT] ([OID]);
create index [IDX_IMAGE_PRODUCT] on [IMAGE]([PRODUCT_OID]);


-- Category_Product [rel12]
alter table [PRODUCT]  add  [CATEGORY_OID]  int;
alter table [PRODUCT]   add constraint FK_PRODUCT_CATEGORY foreign key ([CATEGORY_OID]) references [CATEGORY] ([OID]);
create index [IDX_PRODUCT_CATEGORY] on [PRODUCT]([CATEGORY_OID]);


-- Product_Combination [rel9]
create table [PRODUCT_COMBINATION] (
   [PRODUCT_OID]  int not null,
   [COMBINATION_OID]  int not null,
  primary key ([PRODUCT_OID], [COMBINATION_OID])
);
alter table [PRODUCT_COMBINATION]   add constraint FK_PRODUCT_COMBINATION_PRODUCT foreign key ([PRODUCT_OID]) references [PRODUCT] ([OID]);
alter table [PRODUCT_COMBINATION]   add constraint FK_PRODUCT_COMBINATION_COMBINA foreign key ([COMBINATION_OID]) references [COMBINATION] ([OID]);
create index [IDX_PRODUCT_COMBINATION_PRODUC] on [PRODUCT_COMBINATION]([PRODUCT_OID]);
create index [IDX_PRODUCT_COMBINATION_COMBIN] on [PRODUCT_COMBINATION]([COMBINATION_OID]);


-- User.full name [User#att9]
create view [USER_FULL_NAME_VIEW] as
select AL1.[OID] as [OID],  (AL1.[FIRST_NAME]+' '+AL1.[LAST_NAME]) as [DER_ATTR]
from  [USER] AL1 ;


-- Combination.# products [ent4#att51]
create view [COMBINATION_PRODUCTS_NUMBER_VI] as
select AL1.[OID] as [OID], count(distinct AL2.[PRODUCT_OID]) as [DER_ATTR]
from  [COMBINATION] AL1 
               left outer join [PRODUCT_COMBINATION] AL2 on AL1.[OID]=AL2.[COMBINATION_OID]
group by AL1.[OID];


-- Category.# products [pkg1#ent1#att8]
create view [CATEGORY_PRODUCTS_NUMBER_VIEW] as
select AL1.[OID] as [OID], count(distinct AL2.[OID]) as [DER_ATTR]
from  [CATEGORY] AL1 
               left outer join [PRODUCT] AL2 on AL1.[OID]=AL2.[CATEGORY_OID]
group by AL1.[OID];


-- Product.# photos [pkg1#ent8#att50]
create view [PRODUCT_PHOTOS_NUMBER_VIEW] as
select AL1.[OID] as [OID], count(distinct AL2.[OID]) as [DER_ATTR]
from  [PRODUCT] AL1 
               left outer join [IMAGE] AL2 on AL1.[OID]=AL2.[PRODUCT_OID]
group by AL1.[OID];


