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

create table friendship (
  id                        bigint auto_increment not null,
  friend_requester          bigint,
  friend_accepter           bigint,
  registration              datetime,
  constraint pk_friendship primary key (id))
;

create table post_comment (
  id                        bigint auto_increment not null,
  facebook_post_id          bigint,
  user_id                   bigint,
  content                   TEXT,
  constraint pk_post_comment primary key (id))
;

create table users (
  id                        bigint auto_increment not null,
  email                     varchar(255),
  name                      varchar(255),
  encrypted_password        varbinary(64) not null,
  registration              datetime,
  birthday                  datetime,
  adress                    varchar(255),
  gender                    varchar(255),
  token                     varchar(255),
  role                      varchar(255),
  constraint uq_users_email unique (email),
  constraint pk_users primary key (id))
;

alter table facebook_post add constraint fk_facebook_post_user_1 foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_facebook_post_user_1 on facebook_post (user_id);
alter table friendship add constraint fk_friendship_friendRequester_2 foreign key (friend_requester) references users (id) on delete restrict on update restrict;
create index ix_friendship_friendRequester_2 on friendship (friend_requester);
alter table friendship add constraint fk_friendship_friendAccepter_3 foreign key (friend_accepter) references users (id) on delete restrict on update restrict;
create index ix_friendship_friendAccepter_3 on friendship (friend_accepter);
alter table post_comment add constraint fk_post_comment_facebookPost_4 foreign key (facebook_post_id) references facebook_post (id) on delete restrict on update restrict;
create index ix_post_comment_facebookPost_4 on post_comment (facebook_post_id);
alter table post_comment add constraint fk_post_comment_user_5 foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_post_comment_user_5 on post_comment (user_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table facebook_post;

drop table friendship;

drop table post_comment;

drop table users;

SET FOREIGN_KEY_CHECKS=1;

