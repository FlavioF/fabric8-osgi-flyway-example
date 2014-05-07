CREATE TABLE userentity (
    id int8 NOT NULL, 
    "name" varchar(255), 
    properties bytea, 
    version int8, 
    PRIMARY KEY (id)
);
