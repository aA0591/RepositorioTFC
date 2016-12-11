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
   "USERNAME"  varchar(255),
   "PASSWORD"  varchar(255),
   "EMAIL"  varchar(255),
   "FIRST_NAME"  varchar(255),
   "LAST_NAME"  varchar(255),
   "GENDER"  varchar(255),
   "BIRTHDATE"  date,
   "SHIPPING_ADDRESS"  varchar(255),
  primary key ("OID")
);


-- Book [ent1]
create table "APP"."BOOK" (
   "OID"  integer  not null,
   "ISBN"  varchar(255),
   "DESCRIPTION"  clob(10000),
   "TITLE"  varchar(255),
   "LIST_PRICE"  double,
   "COVER_THUMBNAIL"  varchar(255),
   "HIGHLIGHTED"  smallint,
   "COVER"  varchar(255),
   "DISCOUNT"  double,
   "LANGUAGE"  varchar(255),
   "PAGES"  varchar(255),
   "PUBLISHED"  varchar(255),
   "AUTHORS_LIST"  varchar(255),
   "RATING"  integer,
  primary key ("OID")
);


-- Comment [ent10]
create table "APP"."COMMENT" (
   "OID"  integer  not null,
   "COMMENT"  clob(10000),
   "ADDED_ON"  timestamp,
  primary key ("OID")
);


-- Order [ent12]
create table "APP"."ORDER" (
   "OID"  integer  not null,
   "DATE"  timestamp,
   "STATUS"  smallint,
  primary key ("OID")
);


-- Order Item [ent2]
create table "APP"."ORDER_ITEM" (
   "OID"  integer  not null,
   "QUANTITY"  integer,
  primary key ("OID")
);


-- Author [ent4]
create table "APP"."AUTHOR" (
   "OID"  integer  not null,
   "NAME"  varchar(255),
  primary key ("OID")
);


-- Category [ent6]
create table "APP"."CATEGORY" (
   "OID"  integer  not null,
   "CATEGORY"  varchar(255),
  primary key ("OID")
);


-- Editorial Review [ent9]
create table "APP"."EDITORIAL_REVIEW" (
   "OID"  integer  not null,
   "REVIEWER"  varchar(255),
   "REVIEW"  clob(10000),
   "ADDED_ON"  timestamp,
  primary key ("OID")
);


-- Group_Default Module [Group2DefaultModule_DefaultModule2Group]
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


-- User_Default Group [User2DefaultGroup_DefaultGroup2User]
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


-- Order_User [rel1]
alter table "APP"."ORDER"  add column  "USER_OID"  integer;
alter table "APP"."ORDER"   add constraint FK_ORDER_USER foreign key ("USER_OID") references "APP"."USER" ("OID");


-- Category_Related Category [rel11]
create table "APP"."CATEGORY_RELATED_CATEGORY" (
   "CATEGORY_OID"  integer not null,
   "CATEGORY_OID_2"  integer not null,
  primary key ("CATEGORY_OID", "CATEGORY_OID_2")
);
alter table "APP"."CATEGORY_RELATED_CATEGORY"   add constraint FK_CATEGORY_RELATED_CATEGORY_C foreign key ("CATEGORY_OID") references "APP"."CATEGORY" ("OID");
alter table "APP"."CATEGORY_RELATED_CATEGORY"   add constraint FK_CATEGORY_RELATED_CATEGORY_2 foreign key ("CATEGORY_OID_2") references "APP"."CATEGORY" ("OID");


-- Order Item_Order [rel12]
alter table "APP"."ORDER_ITEM"  add column  "ORDER_OID"  integer;
alter table "APP"."ORDER_ITEM"   add constraint FK_ORDER_ITEM_ORDER foreign key ("ORDER_OID") references "APP"."ORDER" ("OID");


-- Book_Author [rel15]
create table "APP"."BOOK_AUTHOR" (
   "BOOK_OID"  integer not null,
   "AUTHOR_OID"  integer not null,
  primary key ("BOOK_OID", "AUTHOR_OID")
);
alter table "APP"."BOOK_AUTHOR"   add constraint FK_BOOK_AUTHOR_BOOK foreign key ("BOOK_OID") references "APP"."BOOK" ("OID");
alter table "APP"."BOOK_AUTHOR"   add constraint FK_BOOK_AUTHOR_AUTHOR foreign key ("AUTHOR_OID") references "APP"."AUTHOR" ("OID");


-- Category_Sub Category [rel16]
alter table "APP"."CATEGORY"  add column  "CATEGORY_OID"  integer;
alter table "APP"."CATEGORY"   add constraint FK_CATEGORY_CATEGORY foreign key ("CATEGORY_OID") references "APP"."CATEGORY" ("OID");


-- Order Item_Book [rel17]
alter table "APP"."ORDER_ITEM"  add column  "BOOK_OID"  integer;
alter table "APP"."ORDER_ITEM"   add constraint FK_ORDER_ITEM_BOOK foreign key ("BOOK_OID") references "APP"."BOOK" ("OID");


-- Category_Book [rel2]
alter table "APP"."BOOK"  add column  "CATEGORY_OID"  integer;
alter table "APP"."BOOK"   add constraint FK_BOOK_CATEGORY foreign key ("CATEGORY_OID") references "APP"."CATEGORY" ("OID");


-- Book_Related Book [rel3]
create table "APP"."BOOK_RELATED_BOOK" (
   "BOOK_OID"  integer not null,
   "BOOK_OID_2"  integer not null,
  primary key ("BOOK_OID", "BOOK_OID_2")
);
alter table "APP"."BOOK_RELATED_BOOK"   add constraint FK_BOOK_RELATED_BOOK_BOOK foreign key ("BOOK_OID") references "APP"."BOOK" ("OID");
alter table "APP"."BOOK_RELATED_BOOK"   add constraint FK_BOOK_RELATED_BOOK_BOOK_2 foreign key ("BOOK_OID_2") references "APP"."BOOK" ("OID");


-- Book_Editorial Review [rel4]
alter table "APP"."EDITORIAL_REVIEW"  add column  "BOOK_OID"  integer;
alter table "APP"."EDITORIAL_REVIEW"   add constraint FK_EDITORIAL_REVIEW_BOOK foreign key ("BOOK_OID") references "APP"."BOOK" ("OID");


-- Book_Comment [rel5]
alter table "APP"."COMMENT"  add column  "BOOK_OID"  integer;
alter table "APP"."COMMENT"   add constraint FK_COMMENT_BOOK foreign key ("BOOK_OID") references "APP"."BOOK" ("OID");


-- Book.# comments [ent1#att15]
create view "APP"."BOOK_COMMENTS_VIEW" as
select AL1."OID" as "OID", count(distinct AL2."OID") as "DER_ATTR"
from  "APP"."BOOK" AL1 
               left outer join "APP"."COMMENT" AL2 on AL1."OID"=AL2."BOOK_OID"
group by AL1."OID";


-- Book.# rewiews [ent1#att16]
create view "APP"."BOOK_REWIEWS_VIEW" as
select AL1."OID" as "OID", count(distinct AL2."OID") as "DER_ATTR"
from  "APP"."BOOK" AL1 
               left outer join "APP"."EDITORIAL_REVIEW" AL2 on AL1."OID"=AL2."BOOK_OID"
group by AL1."OID";


-- Book.price [ent1#att26]
create view "APP"."BOOK_PRICE_VIEW" as
select AL1."OID" as "OID", AL1."LIST_PRICE" - (AL1."LIST_PRICE" * AL1."DISCOUNT") / 100 as "DER_ATTR"
from  "APP"."BOOK" AL1 ;


-- Book.saving [ent1#att27]
create view "APP"."BOOK_SAVING_VIEW" as
select AL1."OID" as "OID", AL1."LIST_PRICE" - AL2."DER_ATTR" as "DER_ATTR"
from  "APP"."BOOK" AL1 
               left outer join "APP"."BOOK_PRICE_VIEW" AL2 on AL1."OID"=AL2."OID";


-- Order.full name [ent12#att7]
create view "APP"."ORDER_FULL_NAME_VIEW" as
select AL1."OID" as "OID",  (AL2."FIRST_NAME"||' '||AL2."LAST_NAME") as "DER_ATTR"
from  "APP"."ORDER" AL1 
               left outer join "APP"."USER" AL2 on AL1."USER_OID"=AL2."OID";


-- Order Item.partial amount [ent2#att44]
create view "APP"."ORDER_ITEM_PARTIAL_AMOUNT_VIEW" as
select AL1."OID" as "OID", AL2."DER_ATTR" * AL1."QUANTITY" as "DER_ATTR"
from  "APP"."ORDER_ITEM" AL1 
               left outer join "APP"."BOOK_PRICE_VIEW" AL2 on AL1."BOOK_OID"=AL2."OID";


-- Order.total amount [ent12#att47]
create view "APP"."ORDER_TOTAL_AMOUNT_VIEW" as
select AL1."OID" as "OID", sum(AL3."DER_ATTR") as "DER_ATTR"
from  "APP"."ORDER" AL1 
               left outer join "APP"."ORDER_ITEM" AL2 on AL1."OID"=AL2."ORDER_OID"
               left outer join "APP"."ORDER_ITEM_PARTIAL_AMOUNT_VIEW" AL3 on AL2."OID"=AL3."OID"
group by AL1."OID";


-- Category.# books [ent6#att13]
create view "APP"."CATEGORY_BOOKS_VIEW" as
select AL1."OID" as "OID", count(distinct AL2."OID") as "DER_ATTR"
from  "APP"."CATEGORY" AL1 
               left outer join "APP"."BOOK" AL2 on AL1."OID"=AL2."CATEGORY_OID"
group by AL1."OID";


-- Category.has parent [ent6#att48]
create view "APP"."CATEGORY_HAS_PARENT_VIEW" as
select AL1."OID" as "OID", count(distinct AL1."CATEGORY_OID") as "DER_ATTR"
from  "APP"."CATEGORY" AL1 
group by AL1."OID";


