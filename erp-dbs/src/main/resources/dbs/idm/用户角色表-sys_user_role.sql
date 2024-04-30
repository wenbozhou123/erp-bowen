--建表语句
create table public.sys_user_role(
    sys_user_role_id bigint not null primary key ,
    user_id bigint,
    role_id bigint,
    created_by varchar(64),
    created_time timestamp,
    updated_by varchar(128),
    updated_time timestamp
);
--建索引
--主键索引，数据库自动创建, primary key标记的字段
create index nk_sys_user_role_user_id on public.sys_user_role using btree("user_id");
create index nk_sys_user_role_create_time on public.sys_user_role using btree("created_time");

--注释
comment on table public.sys_user_role is '用户角色表';
comment on column public.sys_user_role.sys_user_role_id is '主键ID';
comment on column public.sys_user_role.user_id is '用户ID';
comment on column public.sys_user_role.role_id is '角色ID';
comment on column public.sys_user_role.created_by is '创建人';
comment on column public.sys_user_role.created_time is '创建时间';
comment on column public.sys_user_role.updated_by is '更新人';
comment on column public.sys_user_role.updated_time is '更新时间';

--赋权
grant select,insert,update,delete,truncate on table public.sys_user_role to corey;

