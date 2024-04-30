--建表语句
create table public.sys_role_menu(
    sys_role_menu_id bigint not null primary key ,
    role_id bigint,
    perms_id bigint,
    created_by varchar(64),
    created_time timestamp,
    updated_by varchar(128),
    updated_time timestamp
);
--建索引
--主键索引，数据库自动创建, primary key标记的字段
create index nk_sys_role_menu_role_id on public.sys_role_menu using btree("role_id");
create index nk_sys_role_menu_create_time on public.sys_role_menu using btree("created_time");

--注释
comment on table public.sys_role_menu is '角色权限表';
comment on column public.sys_role_menu.sys_role_menu_id is '主键ID';
comment on column public.sys_role_menu.role_id is '角色ID';
comment on column public.sys_role_menu.perms_id is '权限ID';
comment on column public.sys_role_menu.created_by is '创建人';
comment on column public.sys_role_menu.created_time is '创建时间';
comment on column public.sys_role_menu.updated_by is '更新人';
comment on column public.sys_role_menu.updated_time is '更新时间';

--赋权
grant select,insert,update,delete,truncate on table public.sys_role_menu to corey;

