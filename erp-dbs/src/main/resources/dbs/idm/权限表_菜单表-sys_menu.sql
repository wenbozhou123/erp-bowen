--建表语句
create table public.sys_menu(
    perms_id bigint not null primary key ,
    perms varchar(500),
    created_by varchar(64),
    created_time timestamp,
    updated_by varchar(128),
    updated_time timestamp
);
--建索引
--主键索引，数据库自动创建, primary key标记的字段
create index nk_sys_menu_create_time on public.sys_menu using btree("created_time");

--注释
comment on table public.sys_menu is '权限表/菜单表';

comment on column public.sys_menu.perms_id is '主键ID';
comment on column public.sys_menu.perms is '授权(多个用逗号分隔，如：user:list,user:create)';
comment on column public.sys_menu.created_by is '创建人';
comment on column public.sys_menu.created_time is '创建时间';
comment on column public.sys_menu.updated_by is '更新人';
comment on column public.sys_menu.updated_time is '更新时间';

--赋权
grant select,insert,update,delete,truncate on table public.sys_menu to corey;

