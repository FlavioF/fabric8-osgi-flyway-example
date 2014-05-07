CREATE TABLE role (
    id int8 NOT NULL, 
    "name" varchar(255),
    PRIMARY KEY (id)
);

INSERT INTO "public".role (id, "name") 
VALUES (1, 'SuperAdmin');
INSERT INTO "public".role (id, "name") 
VALUES (2, 'Admin');
INSERT INTO "public".role (id, "name") 
VALUES (3, 'User');

