CREATE TABLE app_table (id integer(10) NOT NULL, time integer(10) NOT NULL, status varchar(255) NOT NULL, name varchar(255) NOT NULL, commentary varchar(255), is_deleted integer(10));
CREATE TABLE app_attribute (id integer(10) NOT NULL, time integer(10) NOT NULL, status varchar(255) NOT NULL, table_id integer(10) NOT NULL, name varchar(255) NOT NULL, type varchar(255) NOT NULL, definition varchar(255), commentary varchar(255), is_primary integer(10), is_nullable integer(10), is_unique integer(10), is_deleted integer(10));
CREATE TABLE app_index (id integer(10) NOT NULL, time integer(10) NOT NULL, status varchar(255) NOT NULL, table_id integer(10) NOT NULL, name varchar(255) NOT NULL, commentary varchar(255), is_deleted integer(10));
CREATE TABLE app_index_attribute (id integer(10), status varchar(255), time integer(10), index_id integer(10) NOT NULL, attribute_id integer(10) NOT NULL, is_deleted integer(10));
CREATE TABLE app_relation (id integer(10) NOT NULL, time integer(10) NOT NULL, status varchar(255) NOT NULL, table_from_id integer(10) NOT NULL, table_to_id integer(10) NOT NULL, index_id integer(10) NOT NULL, identify integer(10), is_deleted integer(10));
CREATE TABLE relation_to_attr (id integer(10), status varchar(255), time integer(10), attribute_id integer(10) NOT NULL, relation_id integer(10), is_deleted integer(10));

