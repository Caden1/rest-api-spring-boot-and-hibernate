insert into user_details(birth_date, id, name)
values(current_date(), 1001, 'Caden');

insert into user_details(birth_date, id, name)
values(current_date(), 1002, 'John');

insert into user_details(birth_date, id, name)
values(current_date(), 1003, 'Jane');

insert into post(id, user_id, description)
values(2001, 1001, 'I want to learn AWS');

insert into post(id, user_id, description)
values(2002, 1001, 'I want to learn DevOps');

insert into post(id, user_id, description)
values(2003, 1002, 'I want to get AWS certified');

insert into post(id, user_id, description)
values(2004, 1002, 'I want to learn Multi Cloud');