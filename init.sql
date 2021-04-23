create user planner;
alter user planner with encrypted password '123';
create database planner;
grant all privileges on database planner to planner;
