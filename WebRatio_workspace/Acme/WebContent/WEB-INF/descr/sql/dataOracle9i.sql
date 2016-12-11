-- -------- POPULATE DATABASE ---------------------------------------------------------------------------------------------

-- ---------- POPUPLATE TABLE `MODULE` ----------
INSERT INTO "ACME"."MODULE" ("OID", "MODULEID", "MODULENAME") VALUES (1, 'sv2', 'Acme Administrator');

-- ---------- POPUPLATE TABLE "GROUP" ----------
INSERT INTO "ACME"."GROUP" ("OID", "GROUPNAME", "MODULEOID") VALUES (1, 'manager', 1);

-- ---------- POPUPLATE TABLE "USER" ----------
INSERT INTO "ACME"."USER" ("OID", "USERNAME", "PASSWORD", "EMAIL", "GROUPOID") VALUES (1, 'manager', 'manager', 'manager@acme.com', 1);

-- ---------- POPUPLATE TABLE "USER_GROUP" ----------
INSERT INTO "ACME"."USER_GROUP" ("USEROID", "GROUPOID") VALUES (1, 1);

-- ---------- POPUPLATE TABLE "GROUP_MODULE" ----------
INSERT INTO "ACME"."GROUP_MODULE" ("GROUPOID", "MODULEOID") VALUES (1, 1);

-- ---------- POPUPLATE TABLE "STORE" ----------
INSERT INTO "ACME"."STORE" ("OID", "ADDRESS", "EMAIL", "MAP", "PHOTO") VALUES (1, 'Oslo', 'mailto:customer-care@acme.com', 'upload/map_1.jpg', 'upload/location_1.jpg');
INSERT INTO "ACME"."STORE" ("OID", "ADDRESS", "EMAIL", "MAP", "PHOTO") VALUES (2, 'Barcelona', 'mailto:customer-care@acme.com', 'upload/map_1.jpg', 'upload/location_1.jpg');
INSERT INTO "ACME"."STORE" ("OID", "ADDRESS", "EMAIL", "MAP", "PHOTO") VALUES (3, 'New York', 'mailto:customer-care@acme.com', 'upload/map_1.jpg', 'upload/location_1.jpg');
INSERT INTO "ACME"."STORE" ("OID", "ADDRESS", "EMAIL", "MAP", "PHOTO") VALUES (5, 'Montreal', 'mailto:customer-care@acme.com', 'upload/map_1.jpg', 'upload/location_1.jpg');
INSERT INTO "ACME"."STORE" ("OID", "ADDRESS", "EMAIL", "MAP", "PHOTO") VALUES (6, 'Hamburg', 'mailto:customer-care@acme.com', 'upload/map_1.jpg', 'upload/location_1.jpg');
INSERT INTO "ACME"."STORE" ("OID", "ADDRESS", "EMAIL", "MAP", "PHOTO") VALUES (7, 'Berlin', 'mailto:customer-care@acme.com', 'upload/map_1.jpg', 'upload/location_1.jpg');
INSERT INTO "ACME"."STORE" ("OID", "ADDRESS", "EMAIL", "MAP", "PHOTO") VALUES (8, 'Hong Kong', 'mailto:customer-care@acme.com', 'upload/map_1.jpg', 'upload/location_1.jpg');
INSERT INTO "ACME"."STORE" ("OID", "ADDRESS", "EMAIL", "MAP", "PHOTO") VALUES (9, 'Madrid', 'mailto:customer-care@acme.com', 'upload/map_1.jpg', 'upload/location_1.jpg');
INSERT INTO "ACME"."STORE" ("OID", "ADDRESS", "EMAIL", "MAP", "PHOTO") VALUES (10, 'Copenhagen', 'mailto:customer-care@acme.com', 'upload/map_1.jpg', 'upload/location_1.jpg');
INSERT INTO "ACME"."STORE" ("OID", "ADDRESS", "EMAIL", "MAP", "PHOTO") VALUES (11, 'Chicago', 'mailto:customer-care@acme.com', 'upload/map_1.jpg', 'upload/location_1.jpg');
INSERT INTO "ACME"."STORE" ("OID", "ADDRESS", "EMAIL", "MAP", "PHOTO") VALUES (12, 'London', 'mailto:customer-care@acme.com', 'upload/map_1.jpg', 'upload/location_1.jpg');
INSERT INTO "ACME"."STORE" ("OID", "ADDRESS", "EMAIL", "MAP", "PHOTO") VALUES (13, 'Sidney', 'mailto:customer-care@acme.com', 'upload/map_1.jpg', 'upload/location_1.jpg');
INSERT INTO "ACME"."STORE" ("OID", "ADDRESS", "EMAIL", "MAP", "PHOTO") VALUES (15, 'Tokyo', 'mailto:customer-care@acme.com', 'upload/map_1.jpg', 'upload/location_1.jpg');

-- ---------- POPUPLATE TABLE "CATEGORY" ----------
INSERT INTO "ACME"."CATEGORY" ("OID", "CATEGORY") VALUES (1, 'Tables');
INSERT INTO "ACME"."CATEGORY" ("OID", "CATEGORY") VALUES (2, 'Chairs');
INSERT INTO "ACME"."CATEGORY" ("OID", "CATEGORY") VALUES (3, 'Lamps');

-- ---------- POPUPLATE TABLE "COMBINATION" ----------
INSERT INTO "ACME"."COMBINATION" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "PHOTO", "START_DATE", "END_DATE", "HIGHLIGHTED") VALUES (1, 1237, 'Perfect for your office, unbeatable for your home, the most versatile interior design is found at Acme.', 'Home Bargain', 3000, 'upload/combo_2.jpg', TO_DATE('2007-01-01', 'yyyy-mm-dd'), TO_DATE('2008-01-01', 'yyyy-mm-dd'), 1);
INSERT INTO "ACME"."COMBINATION" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "PHOTO", "START_DATE", "END_DATE", "HIGHLIGHTED") VALUES (2, 9898, 'Make your house a home with the best interior design and the unprecedented quality of our products.', 'Style & Value', 4550, 'upload/combo_1.jpg', TO_DATE('2007-02-05', 'yyyy-mm-dd'), TO_DATE('2007-11-03', 'yyyy-mm-dd'), 0);
INSERT INTO "ACME"."COMBINATION" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "PHOTO", "START_DATE", "END_DATE", "HIGHLIGHTED") VALUES (3, 1237, 'Meet with friends in the comfort of a stylish and functional setting.', 'Pick of the site', 500, 'upload/combo_3.jpg', TO_DATE('2007-01-01', 'yyyy-mm-dd'), TO_DATE('2008-01-01', 'yyyy-mm-dd'), 0);
INSERT INTO "ACME"."COMBINATION" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "PHOTO", "START_DATE", "END_DATE", "HIGHLIGHTED") VALUES (4, 4123, 'Perfect for your office, unbeatable for your home, the most versatile interior design is found at Acme.', 'Value set', 3500, 'upload/combo_3.jpg', TO_DATE('2007-07-31', 'yyyy-mm-dd'), TO_DATE('2007-07-09', 'yyyy-mm-dd'), 0);
INSERT INTO "ACME"."COMBINATION" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "PHOTO", "START_DATE", "END_DATE", "HIGHLIGHTED") VALUES (5, 1243, 'Make your house a home with the best interior design and the unprecedented quality of our products.', 'Big Bundle', 2000, 'upload/combo_1.jpg', TO_DATE('2007-01-01', 'yyyy-mm-dd'), TO_DATE('2008-01-01', 'yyyy-mm-dd'), 0);
INSERT INTO "ACME"."COMBINATION" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "PHOTO", "START_DATE", "END_DATE", "HIGHLIGHTED") VALUES (6, 6755, 'Perfect for your office, unbeatable for your home, the most versatile interior design is found at Acme.', 'Special of the day', 500, 'upload/combo_2.jpg', TO_DATE('2007-01-01', 'yyyy-mm-dd'),TO_DATE( '2008-01-01', 'yyyy-mm-dd'), 0);
INSERT INTO "ACME"."COMBINATION" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "PHOTO", "START_DATE", "END_DATE", "HIGHLIGHTED") VALUES (7, 9871, 'Perfect for your office, unbeatable for your home, the most versatile interior design is found at Acme.', 'Christmas Special', 3000, 'upload/combo_5.jpg', TO_DATE( '2007-03-06', 'yyyy-mm-dd'), TO_DATE( '2007-10-01', 'yyyy-mm-dd'), 0);

-- ---------- POPUPLATE TABLE "TECHRECORD" ----------
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (1, 'upload/colors.jpg', '144x34x300');
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (2, 'upload/colors.jpg', '144x111x100');
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (3, 'upload/colors.jpg', '144x34x300');
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (4, 'upload/colors.jpg', '144x34x100');
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (5, 'upload/colors.jpg', '144x111x100');
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (6, 'upload/colors.jpg', '11x23x23');
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (7, 'upload/colors.jpg', '144x23x170');
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (8, 'upload/colors.jpg', '144x34x100');
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (9, 'upload/colors.jpg', '144x34x150');
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (10, 'upload/colors.jpg', '144x34x300');
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (11, 'upload/colors.jpg', '144x34x150');
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (12, 'upload/colors.jpg', '144x34x150');
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (13, 'upload/colors.jpg', '144x111x100');
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (14, 'upload/colors.jpg', '144x11x130');
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (15, 'upload/colors.jpg', '144x11x130');
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (16, 'upload/colors.jpg', '144x111x100');
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (17, 'upload/colors.jpg', '144x34x300');
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (18, 'upload/colors.jpg', '544x33x100');
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (19, 'upload/colors.jpg', '144x111x100');
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (20, 'upload/colors.jpg', '144x11x130');
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (21, 'upload/colors.jpg', '23x45x67');
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (22, 'upload/colors.jpg', '4x5x6');
INSERT INTO "ACME"."TECHRECORD" ("OID", "COLORS", "DIMENSIONS") VALUES (23, 'upload/colors.jpg', '12x34x11');

-- ---------- POPUPLATE TABLE "PRODUCT" ----------
INSERT INTO "ACME"."PRODUCT" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "THUMBNAIL", "HIGHLIGHTED", "CATEGORYOID") VALUES (1, 5125, 'Stainless steel meets crystal and silk to make the comfort and look.', 'Wilderness', 1500, 'upload/small_chair_5.jpg', 0, 2);
INSERT INTO "ACME"."PRODUCT" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "THUMBNAIL", "HIGHLIGHTED", "CATEGORYOID") VALUES (2, 5126, 'The spirit of tradition, renovated by the most modern technologies and design.', 'Pink fantasy', 3500, 'upload/small_table_1.jpg', 0, 1);
INSERT INTO "ACME"."PRODUCT" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "THUMBNAIL", "HIGHLIGHTED", "CATEGORYOID") VALUES (3, 5127, 'Brighten up your living room with warmly illuminating ideas.', 'Allair', 4550, 'upload/small_chair_2.jpg', 0, 2);
INSERT INTO "ACME"."PRODUCT" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "THUMBNAIL", "HIGHLIGHTED", "CATEGORYOID") VALUES (4, 6755, 'The spirit of tradition, renovated by the most modern technologies and design.', 'Amplitude', 3000, 'upload/small_chair_2.jpg', 0, 2);
INSERT INTO "ACME"."PRODUCT" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "THUMBNAIL", "HIGHLIGHTED", "CATEGORYOID") VALUES (5, 8630, 'Perfect for your office, unbeatable for your home, the most versatile interior design is found at Acme.', 'Baronetto', 3000, 'upload/small_lamp_4.jpg', 0, 3);
INSERT INTO "ACME"."PRODUCT" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "THUMBNAIL", "HIGHLIGHTED", "CATEGORYOID") VALUES (6, 1243, 'Make your house a home with the best interior design and the unprecedented quality of our products.', 'Atlas', 1000, 'upload/small_table_4.jpg', 0, 1);
INSERT INTO "ACME"."PRODUCT" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "THUMBNAIL", "HIGHLIGHTED", "CATEGORYOID") VALUES (7, 1237, 'Perfect for your office, unbeatable for your home, the most versatile interior design is found at Acme.', 'Aladdin', 1500, 'upload/small_table_1.jpg', 1, 1);
INSERT INTO "ACME"."PRODUCT" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "THUMBNAIL", "HIGHLIGHTED", "CATEGORYOID") VALUES (8, 7145, 'High quality Italian design for relaxing and enjoying life with your family and friends.', 'Silvestream', 1500, 'upload/small_lamp_1.jpg', 0, 3);
INSERT INTO "ACME"."PRODUCT" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "THUMBNAIL", "HIGHLIGHTED", "CATEGORYOID") VALUES (9, 4678, 'Perfect for your office, unbeatable for your home, the most versatile interior design is found at Acme.', 'Sara', 500, 'upload/small_lamp_2.jpg', 0, 3);
INSERT INTO "ACME"."PRODUCT" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "THUMBNAIL", "HIGHLIGHTED", "CATEGORYOID") VALUES (10, 1243, 'Meet with friends in the comfort of a stylish and functional setting.', 'Mambo', 1000, 'upload/small_table_1.jpg', 0, 1);
INSERT INTO "ACME"."PRODUCT" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "THUMBNAIL", "HIGHLIGHTED", "CATEGORYOID") VALUES (11, 1238, 'Stainless steel meets crystal and silk to make the comfort and look.', 'Euclid', 4550, 'upload/small_table_1.jpg', 0, 1);
INSERT INTO "ACME"."PRODUCT" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "THUMBNAIL", "HIGHLIGHTED", "CATEGORYOID") VALUES (13, 4680, 'Create your space and make it flexible enough to fit whatever the moment calls for, from meeting friends to enjoying your family.', 'Andros', 1200, 'upload/small_lamp_1.jpg', 0, 3);
INSERT INTO "ACME"."PRODUCT" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "THUMBNAIL", "HIGHLIGHTED", "CATEGORYOID") VALUES (14, 7148, 'High quality Italian design for relaxing and enjoying life with your family and friends.', 'Byron', 1000, 'upload/small_chair_3.jpg', NULL, 2);
INSERT INTO "ACME"."PRODUCT" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "THUMBNAIL", "HIGHLIGHTED", "CATEGORYOID") VALUES (16, 1245, 'Create your space and make it flexible enough to fit whatever the moment calls for, from meeting friends to enjoying your family.', 'Landscape', 500, 'upload/small_table_3.jpg', 0, 1);
INSERT INTO "ACME"."PRODUCT" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "THUMBNAIL", "HIGHLIGHTED", "CATEGORYOID") VALUES (17, 4123, 'The spirit of tradition, renovated by the most modern technologies and design.', 'Rodolfo', 4550, 'upload/small_table_1.jpg', 0, 1);
INSERT INTO "ACME"."PRODUCT" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "THUMBNAIL", "HIGHLIGHTED", "CATEGORYOID") VALUES (21, 9876, 'A fabulous piece of furniture for relaxing with friends.', 'Lucid', 234, 'upload/small_table_5.jpg', 0, 1);
INSERT INTO "ACME"."PRODUCT" ("OID", "CODE", "DESCRIPTION", "NAME", "PRICE", "THUMBNAIL", "HIGHLIGHTED", "CATEGORYOID") VALUES (22, 3456, 'A marvellous lamp shedding a new light to your family life.', 'Blue Fountain', 124, 'upload/small_lamp_5.jpg', 0, 3);

-- ---------- POPUPLATE TABLE "PRODUCT_COMBINATION" ----------
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (2, 2);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (2, 3);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (2, 5);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (2, 7);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (3, 7);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (4, 3);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (4, 4);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (5, 2);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (5, 6);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (5, 7);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (7, 2);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (7, 3);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (8, 1);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (8, 7);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (9, 4);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (10, 2);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (11, 2);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (11, 3);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (11, 6);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (13, 4);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (13, 5);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (14, 5);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (21, 2);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (21, 5);
INSERT INTO "ACME"."PRODUCT_COMBINATION" ("PRODUCTOID", "COMBINATIONOID") VALUES (22, 4);


-- ---------- POPUPLATE TABLE "TECHRECORD_PRODUCT" ----------
INSERT INTO "ACME"."TECHRECORD_PRODUCT" ("PRODUCTOID", "TECHRECORDOID") VALUES (1, 12);
INSERT INTO "ACME"."TECHRECORD_PRODUCT" ("PRODUCTOID", "TECHRECORDOID") VALUES (2, 9);
INSERT INTO "ACME"."TECHRECORD_PRODUCT" ("PRODUCTOID", "TECHRECORDOID") VALUES (3, 13);
INSERT INTO "ACME"."TECHRECORD_PRODUCT" ("PRODUCTOID", "TECHRECORDOID") VALUES (4, 15);
INSERT INTO "ACME"."TECHRECORD_PRODUCT" ("PRODUCTOID", "TECHRECORDOID") VALUES (5, 3);
INSERT INTO "ACME"."TECHRECORD_PRODUCT" ("PRODUCTOID", "TECHRECORDOID") VALUES (6, 8);
INSERT INTO "ACME"."TECHRECORD_PRODUCT" ("PRODUCTOID", "TECHRECORDOID") VALUES (7, 2);
INSERT INTO "ACME"."TECHRECORD_PRODUCT" ("PRODUCTOID", "TECHRECORDOID") VALUES (8, 17);
INSERT INTO "ACME"."TECHRECORD_PRODUCT" ("PRODUCTOID", "TECHRECORDOID") VALUES (9, 14);
INSERT INTO "ACME"."TECHRECORD_PRODUCT" ("PRODUCTOID", "TECHRECORDOID") VALUES (10, 1);
INSERT INTO "ACME"."TECHRECORD_PRODUCT" ("PRODUCTOID", "TECHRECORDOID") VALUES (11, 19);
INSERT INTO "ACME"."TECHRECORD_PRODUCT" ("PRODUCTOID", "TECHRECORDOID") VALUES (13, 10);
INSERT INTO "ACME"."TECHRECORD_PRODUCT" ("PRODUCTOID", "TECHRECORDOID") VALUES (14, 20);
INSERT INTO "ACME"."TECHRECORD_PRODUCT" ("PRODUCTOID", "TECHRECORDOID") VALUES (16, 5);
INSERT INTO "ACME"."TECHRECORD_PRODUCT" ("PRODUCTOID", "TECHRECORDOID") VALUES (17, 6);
INSERT INTO "ACME"."TECHRECORD_PRODUCT" ("PRODUCTOID", "TECHRECORDOID") VALUES (21, 21);
INSERT INTO "ACME"."TECHRECORD_PRODUCT" ("PRODUCTOID", "TECHRECORDOID") VALUES (22, 23);


-- ---------- POPUPLATE TABLE "BIGIMAGE" ----------
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (1, 'The spirit of tradition, renovated by the most modern technologies and design.', 'upload/lamp_2.jpg', 14);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (2, 'High quality Italian design for relaxing and enjoying life with your family and friends.', 'upload/table_2.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (3, 'Brighten up your living room with warmly illuminating ideas.', 'upload/table_1.jpg', 11);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (4, 'Meet with friends in the comfort of a stylish and functional setting.', 'upload/lamp_4.jpg', 11);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (5, 'Perfect for your office, unbeatable for your home, the most versatile interior design is found at Acme.', 'upload/chair_3.jpg', 3);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (6, 'Brighten up your living room with warmly illuminating ideas.', 'upload/lamp_4.jpg', 4);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (7, 'Perfect for your office, unbeatable for your home, the most versatile interior design is found at Acme.', 'upload/lamp_3.jpg', 10);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (8, 'Make your house a home with the best interior design and the unprecedented quality of our products.', 'upload/lamp_1.jpg', 17);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (9, 'The spirit of tradition, renovated by the most modern technologies and design.', 'upload/table_3.jpg', 2);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (10, 'Brighten up your living room with warmly illuminating ideas.', 'upload/chair_1.jpg', 3);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (11, 'Meet with friends in the comfort of a stylish and functional setting.', 'upload/table_5.jpg', 8);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (12, 'High quality Italian design for relaxing and enjoying life with your family and friends.', 'upload/table_1.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (13, 'The spirit of tradition, renovated by the most modern technologies and design.', 'upload/table_5.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (14, 'Stainless steel meets crystal and silk to make the comfort and look.', 'upload/lamp_5.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (15, 'The spirit of tradition, renovated by the most modern technologies and design.', 'upload/chair_1.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (16, 'Make your house a home with the best interior design and the unprecedented quality of our products.', 'upload/lamp_5.jpg', 1);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (17, 'High quality Italian design for relaxing and enjoying life with your family and friends.', 'upload/table_4.jpg', 9);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (18, 'Stainless steel meets crystal and silk to make the comfort and look.', 'upload/chair_3.jpg', 6);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (19, 'The spirit of tradition, renovated by the most modern technologies and design.', 'upload/chair_4.jpg', 16);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (20, 'Brighten up your living room with warmly illuminating ideas.', 'upload/table_4.jpg', 2);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (21, 'Brighten up your living room with warmly illuminating ideas.', 'upload/lamp_5.jpg', 8);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (22, 'Meet with friends in the comfort of a stylish and functional setting.', 'upload/lamp_2.jpg', 7);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (23, 'Brighten up your living room with warmly illuminating ideas.', 'upload/table_2.jpg', 14);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (24, 'Perfect for your office, unbeatable for your home, the most versatile interior design is found at Acme.', 'upload/chair_5.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (25, 'Make your house a home with the best interior design and the unprecedented quality of our products.', 'upload/table_2.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (26, 'The spirit of tradition, renovated by the most modern technologies and design.', 'upload/chair_4.jpg', 16);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (27, 'Meet with friends in the comfort of a stylish and functional setting.', 'upload/table_4.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (28, 'Create your space and make it flexible enough to fit whatever the moment calls for, from meeting friends to enjoying your family.', 'upload/table_5.jpg', 10);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (29, 'Create your space and make it flexible enough to fit whatever the moment calls for, from meeting friends to enjoying your family.', 'upload/chair_1.jpg', 11);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (30, 'Perfect for your office, unbeatable for your home, the most versatile interior design is found at Acme.', 'upload/chair_5.jpg', 4);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (31, 'Brighten up your living room with warmly illuminating ideas.', 'upload/chair_5.jpg', 1);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (32, 'Perfect for your office, unbeatable for your home, the most versatile interior design is found at Acme.', 'upload/table_4.jpg', 11);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (33, 'Brighten up your living room with warmly illuminating ideas.', 'upload/lamp_4.jpg', 16);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (34, 'Stainless steel meets crystal and silk to make the comfort and look.', 'upload/lamp_4.jpg', 17);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (35, 'Create your space and make it flexible enough to fit whatever the moment calls for, from meeting friends to enjoying your family.', 'upload/lamp_5.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (36, 'Make your house a home with the best interior design and the unprecedented quality of our products.', 'upload/lamp_3.jpg', 8);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (37, 'High quality Italian design for relaxing and enjoying life with your family and friends.', 'upload/table_1.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (38, 'The spirit of tradition, renovated by the most modern technologies and design.', 'upload/chair_2.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (39, 'The spirit of tradition, renovated by the most modern technologies and design.', 'upload/table_2.jpg', 3);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (40, 'Make your house a home with the best interior design and the unprecedented quality of our products.', 'upload/table_4.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (41, 'Perfect for your office, unbeatable for your home, the most versatile interior design is found at Acme.', 'upload/lamp_4.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (42, 'Perfect for your office, unbeatable for your home, the most versatile interior design is found at Acme.', 'upload/table_5.jpg', 13);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (43, 'Meet with friends in the comfort of a stylish and functional setting.', 'upload/lamp_4.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (44, 'Brighten up your living room with warmly illuminating ideas.', 'upload/lamp_3.jpg', 16);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (45, 'Perfect for your office, unbeatable for your home, the most versatile interior design is found at Acme.', 'upload/table_3.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (46, 'Create your space and make it flexible enough to fit whatever the moment calls for, from meeting friends to enjoying your family.', 'upload/chair_4.jpg', 4);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (47, 'Brighten up your living room with warmly illuminating ideas.', 'upload/table_5.jpg', 5);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (48, 'The spirit of tradition, renovated by the most modern technologies and design.', 'upload/table_4.jpg', 13);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (49, 'High quality Italian design for relaxing and enjoying life with your family and friends.', 'upload/table_3.jpg', 8);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (50, 'Meet with friends in the comfort of a stylish and functional setting.', 'upload/chair_3.jpg', 11);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (51, 'Brighten up your living room with warmly illuminating ideas.', 'upload/chair_4.jpg', 4);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (52, 'Brighten up your living room with warmly illuminating ideas.', 'upload/chair_4.jpg', 4);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (53, 'The spirit of tradition, renovated by the most modern technologies and design.', 'upload/table_2.jpg', 3);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (54, 'Make your house a home with the best interior design and the unprecedented quality of our products.', 'upload/chair_4.jpg', 16);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (55, 'High quality Italian design for relaxing and enjoying life with your family and friends.', 'upload/table_3.jpg', 17);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (56, 'Stainless steel meets crystal and silk to make the comfort and look.', 'upload/lamp_3.jpg', 11);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (57, 'Meet with friends in the comfort of a stylish and functional setting.', 'upload/table_2.jpg', 10);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (58, 'Meet with friends in the comfort of a stylish and functional setting.', 'upload/lamp_1.jpg', 1);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (59, 'Meet with friends in the comfort of a stylish and functional setting.', 'upload/table_5.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (60, 'Brighten up your living room with warmly illuminating ideas.', 'upload/lamp_5.jpg', 11);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (61, 'Brighten up your living room with warmly illuminating ideas.', 'upload/chair_2.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (62, 'Create your space and make it flexible enough to fit whatever the moment calls for, from meeting friends to enjoying your family.', 'upload/table_3.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (63, 'High quality Italian design for relaxing and enjoying life with your family and friends.', 'upload/chair_3.jpg', 9);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (64, 'The spirit of tradition, renovated by the most modern technologies and design.', 'upload/chair_1.jpg', 17);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (65, 'Make your house a home with the best interior design and the unprecedented quality of our products.', 'upload/table_4.jpg', 10);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (66, 'Meet with friends in the comfort of a stylish and functional setting.', 'upload/lamp_3.jpg', 1);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (67, 'Meet with friends in the comfort of a stylish and functional setting.', 'upload/table_3.jpg', 3);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (68, 'Meet with friends in the comfort of a stylish and functional setting.', 'upload/lamp_3.jpg', 2);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (69, 'Meet with friends in the comfort of a stylish and functional setting.', 'upload/chair_3.jpg', 6);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (70, 'Stainless steel meets crystal and silk to make the comfort and look.', 'upload/table_4.jpg', 3);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (71, 'High quality Italian design for relaxing and enjoying life with your family and friends.', 'upload/chair_3.jpg', 8);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (72, 'Brighten up your living room with warmly illuminating ideas.', 'upload/chair_4.jpg', 1);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (73, 'Create your space and make it flexible enough to fit whatever the moment calls for, from meeting friends to enjoying your family.', 'upload/table_5.jpg', 2);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (74, 'The spirit of tradition, renovated by the most modern technologies and design.', 'upload/lamp_4.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (75, 'Meet with friends in the comfort of a stylish and functional setting.', 'upload/lamp_1.jpg', 4);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (76, 'Brighten up your living room with warmly illuminating ideas.', 'upload/table_5.jpg', 7);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (77, 'Stainless steel meets crystal and silk to make the comfort and look.', 'upload/chair_5.jpg', 3);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (78, 'The spirit of tradition, renovated by the most modern technologies and design.', 'upload/lamp_1.jpg', 3);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (79, 'Brighten up your living room with warmly illuminating ideas.', 'upload/lamp_3.jpg', 7);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (80, 'Stainless steel meets crystal and silk to make the comfort and look.', 'upload/lamp_1.jpg', 10);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (81, 'Create your space and make it flexible enough to fit whatever the moment calls for, from meeting friends to enjoying your family.', 'upload/chair_4.jpg', 6);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (82, 'High quality Italian design for relaxing and enjoying life with your family and friends.', 'upload/table_2.jpg', 13);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (83, 'High quality Italian design for relaxing and enjoying life with your family and friends.', 'upload/table_4.jpg', 17);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (84, 'Meet with friends in the comfort of a stylish and functional setting.', 'upload/lamp_3.jpg', 11);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (85, 'The spirit of tradition, renovated by the most modern technologies and design.', 'upload/chair_2.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (86, 'Create your space and make it flexible enough to fit whatever the moment calls for, from meeting friends to enjoying your family.', 'upload/table_2.jpg', 9);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (87, 'The spirit of tradition, renovated by the most modern technologies and design.', 'upload/lamp_4.jpg', 5);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (88, 'Stainless steel meets crystal and silk to make the comfort and look.', 'upload/chair_3.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (89, 'Make your house a home with the best interior design and the unprecedented quality of our products.', 'upload/table_3.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (90, 'Meet with friends in the comfort of a stylish and functional setting.', 'upload/lamp_1.jpg', 1);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (91, 'Stainless steel meets crystal and silk to make the comfort and look.', 'upload/table_2.jpg', 16);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (92, 'Make your house a home with the best interior design and the unprecedented quality of our products.', 'upload/lamp_4.jpg', 17);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (93, 'Perfect for your office, unbeatable for your home, the most versatile interior design is found at Acme.', 'upload/lamp_3.jpg', 4);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (94, 'Make your house a home with the best interior design and the unprecedented quality of our products.', 'upload/table_5.jpg', 11);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (95, 'Create your space and make it flexible enough to fit whatever the moment calls for, from meeting friends to enjoying your family.', 'upload/chair_4.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (96, 'Create your space and make it flexible enough to fit whatever the moment calls for, from meeting friends to enjoying your family.', 'upload/lamp_2.jpg', 2);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (97, 'Stainless steel meets crystal and silk to make the comfort and look.', 'upload/table_2.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (98, 'The spirit of tradition, renovated by the most modern technologies and design.', 'upload/chair_2.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (99, 'Perfect for your office, unbeatable for your home, the most versatile interior design is found at Acme.', 'upload/lamp_4.jpg', 6);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (100, 'Brighten up your living room with warmly illuminating ideas.', 'upload/lamp_4.jpg', 5);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (101, 'A speheric object for a spheric world.', 'upload/bigimage2.jpg', NULL);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (102, 'See the beauty of this article in the glitter of the night.', 'upload/lamp_5_1.jpg', 22);
INSERT INTO "ACME"."BIGIMAGE" ("OID", "DESCRIPTION", "PICTURE", "PRODUCTOID") VALUES (103, 'A mix of technology and innovative design that will add value to your h', 'upload/lamp_4.jpg', 22);



