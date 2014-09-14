create table app_table(id integer primary key, 
                       name text not null, 
                       commentary text);

create table app_attribute(
                       id integer primary key, 
                       name text not null, 
                       type text not null,
                       definition text,
                       commentary text,
                       table_id integer references app_table(id));

-- добавить constraints для атрибутов - в отдельную таблицу или к ним же

-- индексы - аггрегация атрибутов с целью создания связей между таблицами (составной FK)
create table app_index(id integer primary key, 
                       name text not null, 
                       commentary text,
                       table_id integer references app_table(id));

create table app_index_attribute(index_id integer references app_index(id), 
                       attribute_id integer references app_attribute(id));

create table app_relation(id integer primary key, 
                       table_from_id integer references app_table(id), 
                       table_to_id integer references app_table(id), 
                       identify integer); -- 0 - связь не идентифицирующая, 1 - идентифицирующая

-- атрибуты, появившиеся при создании связи между таблицами
create table relation_to_attr(relation_id integer references app_relation(id), 
                              attribute_id integer references app_attribute(id));
