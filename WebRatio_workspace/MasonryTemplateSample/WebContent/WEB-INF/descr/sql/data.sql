-- -------- POPULATE DATABASE ---------------------------------------------------------------------------------------------

-- ---------- POPUPLATE TABLE "PRODUCT" ----------
INSERT INTO "APP"."PRODUCT" ("OID", "DESCRIPTION", "NAME", "THUMBNAIL") VALUES (1, 'Stainless steel meets crystal and silk to make the comfort and look.', 'Wilderness', 'upload/small_chair_5.jpg');
INSERT INTO "APP"."PRODUCT" ("OID", "DESCRIPTION", "NAME", "THUMBNAIL") VALUES (2, 'The spirit of tradition, renovated by the most modern technologies and design.', 'Pink fantasy', 'upload/small_table_1.jpg');
INSERT INTO "APP"."PRODUCT" ("OID", "DESCRIPTION", "NAME", "THUMBNAIL") VALUES (3, 'Brighten up your living room with warmly illuminating ideas.', 'Allair', 'upload/small_chair_2.jpg');
INSERT INTO "APP"."PRODUCT" ("OID", "DESCRIPTION", "NAME", "THUMBNAIL") VALUES (4, 'The spirit of tradition, renovated by the most modern technologies and design.', 'Amplitude', 'upload/small_chair_2.jpg');
INSERT INTO "APP"."PRODUCT" ("OID", "DESCRIPTION", "NAME", "THUMBNAIL") VALUES (5, 'Perfect for your office, unbeatable for your home, the most versatile interior design is found at Acme.', 'Baronetto', 'upload/small_lamp_4.jpg');
INSERT INTO "APP"."PRODUCT" ("OID", "DESCRIPTION", "NAME", "THUMBNAIL") VALUES (6, 'Make your house a home with the best interior design and the unprecedented quality of our products.', 'Atlas', 'upload/small_table_4.jpg');
INSERT INTO "APP"."PRODUCT" ("OID", "DESCRIPTION", "NAME", "THUMBNAIL") VALUES (7, 'Perfect for your office, unbeatable for your home, the most versatile interior design is found at Acme.', 'Aladdin', 'upload/small_table_1.jpg');
INSERT INTO "APP"."PRODUCT" ("OID", "DESCRIPTION", "NAME", "THUMBNAIL") VALUES (8, 'High quality Italian design for relaxing and enjoying life with your family and friends.', 'Silvestream', 'upload/small_lamp_1.jpg');
INSERT INTO "APP"."PRODUCT" ("OID", "DESCRIPTION", "NAME", "THUMBNAIL") VALUES (9, 'Perfect for your office, unbeatable for your home, the most versatile interior design is found at Acme.', 'Sara', 'upload/small_lamp_2.jpg');
INSERT INTO "APP"."PRODUCT" ("OID", "DESCRIPTION", "NAME", "THUMBNAIL") VALUES (10, 'Meet with friends in the comfort of a stylish and functional setting.', 'Mambo', 'upload/small_table_1.jpg');
INSERT INTO "APP"."PRODUCT" ("OID", "DESCRIPTION", "NAME", "THUMBNAIL") VALUES (11, 'Stainless steel meets crystal and silk to make the comfort and look.', 'Euclid', 'upload/small_table_1.jpg');
INSERT INTO "APP"."PRODUCT" ("OID", "DESCRIPTION", "NAME", "THUMBNAIL") VALUES (12, 'Create your space and make it flexible enough to fit whatever the moment calls for, from meeting friends to enjoying your family.', 'Andros', 'upload/small_lamp_1.jpg');
INSERT INTO "APP"."PRODUCT" ("OID", "DESCRIPTION", "NAME", "THUMBNAIL") VALUES (13, 'High quality Italian design for relaxing and enjoying life with your family and friends.', 'Byron', 'upload/small_chair_3.jpg');
INSERT INTO "APP"."PRODUCT" ("OID", "DESCRIPTION", "NAME", "THUMBNAIL") VALUES (14, 'Create your space and make it flexible enough to fit whatever the moment calls for, from meeting friends to enjoying your family.', 'Landscape', 'upload/small_table_3.jpg');
INSERT INTO "APP"."PRODUCT" ("OID", "DESCRIPTION", "NAME", "THUMBNAIL") VALUES (15, 'The spirit of tradition, renovated by the most modern technologies and design.', 'Rodolfo', 'upload/small_table_1.jpg');
INSERT INTO "APP"."PRODUCT" ("OID", "DESCRIPTION", "NAME", "THUMBNAIL") VALUES (16, 'A fabulous piece of furniture for relaxing with friends.', 'Lucid', 'upload/small_table_5.jpg');
INSERT INTO "APP"."PRODUCT" ("OID", "DESCRIPTION", "NAME", "THUMBNAIL") VALUES (17, 'A marvellous lamp shedding a new light to your family life.', 'Blue Fountain', 'upload/small_lamp_5.jpg');

-- ---------- POPUPLATE TABLE "PRODUCT_PRODUCT" ----------
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (1, 2);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (1, 3);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (1, 5);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (1, 7);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (1, 8);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (1, 9);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (1, 12);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (1, 15);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (1, 16);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (2, 3);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (2, 5);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (2, 6);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (2, 8);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (2, 10);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (2, 11);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (2, 14);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (2, 16);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (3, 5);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (3, 6);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (3, 8);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (3, 9);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (3, 11);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (3, 12);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (3, 17);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (4, 5);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (4, 6);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (4, 8);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (4, 11);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (4, 12);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (4, 13);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (4, 14);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (4, 16);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (5, 7);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (5, 8);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (5, 10);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (5, 11);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (5, 12);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (5, 15);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (5, 17);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (6, 9);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (6, 11);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (6, 13);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (6, 15);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (6, 17);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (7, 9);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (7, 14);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (9, 10);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (9, 14);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (10, 12);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (11, 13);
INSERT INTO "APP"."PRODUCT_PRODUCT" ("PRODUCT_OID", "PRODUCT_OID_2") VALUES (11, 16);
