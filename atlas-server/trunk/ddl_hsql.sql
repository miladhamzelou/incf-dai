alter table Format_MimeType drop constraint FKD05A48D66A1A26A0;
alter table Format_MimeType drop constraint FKD05A48D638778B29;
alter table Output drop constraint FK8D7605215E9CC97F;
alter table Output_Format drop constraint FK6AE604F58F7F1260;
alter table Output_Format drop constraint FK6AE604F51684F449;
alter table ProcessDescription_Input drop constraint FKA3247318EAAAE1C0;
alter table ProcessDescription_Input drop constraint FKA3247318345A05F5;
alter table ProcessDescription_Output drop constraint FKCC0B6773EAAAE1C0;
alter table ProcessDescription_Output drop constraint FKCC0B6773C1F14E2F;
drop table Format if exists;
drop table Format_MimeType if exists;
drop table Input if exists;
drop table MimeType if exists;
drop table Output if exists;
drop table Output_Format if exists;
drop table ProcessDescription if exists;
drop table ProcessDescription_Input if exists;
drop table ProcessDescription_Output if exists;
create table Format (id bigint generated by default as identity (start with 1), encoding varchar(255), schema varchar(255), primary key (id));
create table Format_MimeType (Format_id bigint not null, mimeTypes_id bigint not null, unique (mimeTypes_id));
create table Input (id bigint generated by default as identity (start with 1), abstract varchar(255), dataType varchar(255), identifier varchar(255), title varchar(255), primary key (id));
create table MimeType (id bigint generated by default as identity (start with 1), mimeType varchar(255), primary key (id));
create table Output (id bigint generated by default as identity (start with 1), abstract varchar(255), identifier varchar(255), title varchar(255), defaultFormat_id bigint, primary key (id));
create table Output_Format (Output_id bigint not null, supportedFormats_id bigint not null, unique (supportedFormats_id));
create table ProcessDescription (id bigint generated by default as identity (start with 1), abstract varchar(255), identifier varchar(255), title varchar(255), primary key (id));
create table ProcessDescription_Input (ProcessDescription_id bigint not null, inputs_id bigint not null, unique (inputs_id));
create table ProcessDescription_Output (ProcessDescription_id bigint not null, outputs_id bigint not null, unique (outputs_id));
alter table Format_MimeType add constraint FKD05A48D66A1A26A0 foreign key (Format_id) references Format;
alter table Format_MimeType add constraint FKD05A48D638778B29 foreign key (mimeTypes_id) references MimeType;
alter table Output add constraint FK8D7605215E9CC97F foreign key (defaultFormat_id) references Format;
alter table Output_Format add constraint FK6AE604F58F7F1260 foreign key (Output_id) references Output;
alter table Output_Format add constraint FK6AE604F51684F449 foreign key (supportedFormats_id) references Format;
alter table ProcessDescription_Input add constraint FKA3247318EAAAE1C0 foreign key (ProcessDescription_id) references ProcessDescription;
alter table ProcessDescription_Input add constraint FKA3247318345A05F5 foreign key (inputs_id) references Input;
alter table ProcessDescription_Output add constraint FKCC0B6773EAAAE1C0 foreign key (ProcessDescription_id) references ProcessDescription;
alter table ProcessDescription_Output add constraint FKCC0B6773C1F14E2F foreign key (outputs_id) references Output;
