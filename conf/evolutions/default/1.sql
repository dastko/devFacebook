# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table facebook_post (
  id                        bigint auto_increment not null,
  content                   TEXT,
  user_id                   bigint,
  likes                     integer,
  date                      datetime,
  constraint pk_facebook_post primary key (id))
;

create table post_comment (
  id                        bigint auto_increment not null,
  facebook_post_id          bigint,
  user_id                   bigint,
  content                   TEXT,
  constraint pk_post_comment primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  email                     varchar(255),
  name                      varchar(255),
  encrypted_password        varbinary(64) not null,
  registration              datetime,
  birthday                  datetime,
  adress                    varchar(255),
  gender                    varchar(255),
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id))
;

alter table facebook_post add constraint fk_facebook_post_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_facebook_post_user_1 on facebook_post (user_id);
alter table post_comment add constraint fk_post_comment_facebookPost_2 foreign key (facebook_post_id) references facebook_post (id) on delete restrict on update restrict;
create index ix_post_comment_facebookPost_2 on post_comment (facebook_post_id);
alter table post_comment add constraint fk_post_comment_user_3 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_post_comment_user_3 on post_comment (user_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table facebook_post;

drop table post_comment;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

