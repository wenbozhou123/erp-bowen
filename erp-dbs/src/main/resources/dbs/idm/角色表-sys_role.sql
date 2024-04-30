--建表语句
create table public.sys_role(
    role_id bigint not null primary key ,
    role_name varchar(256),
    remark varchar(128),
    created_by varchar(64),
    created_time timestamp,
    updated_by varchar(128),
    updated_time timestamp
);
--建索引
--主键索引，数据库自动创建, primary key标记的字段
create index nk_sys_role_role_name on public.sys_role using btree("role_name");
create index nk_sys_role_create_time on public.sys_role using btree("created_time");

--注释
comment on table public.sys_role is '角色表';
comment on column public.sys_role.role_id is '主键ID';
comment on column public.sys_role.role_name is '角色名称';
comment on column public.sys_role.remark is '备注';
comment on column public.sys_role.created_by is '创建人';
comment on column public.sys_role.created_time is '创建时间';
comment on column public.sys_role.updated_by is '更新人';
comment on column public.sys_role.updated_time is '更新时间';

--赋权
grant select,insert,update,delete,truncate on table public.sys_role to corey;

