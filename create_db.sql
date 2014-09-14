create table app_table(id integer primary key, 
                       name text, 
                       commentary text);

create table app_attribute(
                       id integer primary key, 
                       table_id integer);

-- индексы - аггрегация атрибутов с целью создания связей между таблицами (составной FK)
create table app_index(id integer primary key, 
                       table_id integer);

create table app_index_attribute(index_id integer, 
                       attribute_id integer);

create table app_relation(id integer primary key, 
                       table_from_id integer, 
                       table_to_id integer, 
                       identify integer); -- 0 - связь не идентифицирующая, 1 - идентифицирующая

-- атрибуты, появившиеся при создании связи между таблицами
create table relation_to_attr(relation_id integer, 
                              attribute_id integer);
