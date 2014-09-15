create table app_table(id integer primary key, 
                       name text not null, 
                       commentary text);

create table app_attribute(
                       id integer primary key, 
                       name text not null, 
                       type text not null,
                       definition text,
                       commentary text,
                       is_primary integer, -- 0 - не пк, 1 - пк
                       is_nullable integer, -- 0 - not null, 1 - null
                       is_unique integer, -- 0 - not unique, 1 - unique
                       table_id integer references app_table(id) on delete cascade);

-- добавить constraints для атрибутов - в отдельную таблицу или к ним же

-- индексы - аггрегация атрибутов с целью создания связей между таблицами (составной FK)
create table app_index(id integer primary key, 
                       name text not null, 
                       commentary text,
                       table_id integer references app_table(id) on delete cascade);

create table app_index_attribute(index_id integer references app_index(id) on delete cascade, 
                       attribute_id integer references app_attribute(id) on delete cascade);

create table app_relation(id integer primary key, 
                       table_from_id integer references app_table(id) on delete cascade, 
                       table_to_id integer references app_table(id) on delete cascade, 
                       index_id integer references app_index(id) on delete cascade,
                       identify integer); -- 0 - связь не идентифицирующая, 1 - идентифицирующая

-- атрибуты, появившиеся при создании связи между таблицами
create table relation_to_attr(relation_id integer references app_relation(id) on delete cascade, 
                              attribute_id integer references app_attribute(id) on delete cascade);
