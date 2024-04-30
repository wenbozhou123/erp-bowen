--建表语句
create table public.sys_user(
    user_id bigint not null primary key ,
    user_code varchar(256),
    name varchar(500),
    password varchar(512),
    password_salt varchar(512),
    created_by varchar(64),
    created_time timestamp,
    updated_by varchar(128),
    updated_time timestamp
);
--建索引
--主键索引，数据库自动创建, primary key标记的字段
create index nk_sys_user_user_code on public.sys_user using btree("user_code");
create index nk_sys_user_create_time on public.sys_user using btree("created_time");

--注释
comment on table public.sys_user is '用户表';
comment on column public.sys_user.user_id is '主键ID';
comment on column public.sys_user.user_code is '用户登录账号';
comment on column public.sys_user.name is '用户姓名';
comment on column public.sys_user.password is '加密密码';
comment on column public.sys_user.password_salt is '密码对应的盐';
comment on column public.sys_user.created_by is '创建人';
comment on column public.sys_user.created_time is '创建时间';
comment on column public.sys_user.updated_by is '更新人';
comment on column public.sys_user.updated_time is '更新时间';

--赋权
grant select,insert,update,delete,truncate on table public.sys_user to corey;

