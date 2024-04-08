--建表语句
create table public.product_info(
    product_id varchar(32) not null primary key ,
    product_name varchar(256),
    product_abbr varchar(128),
    mnemonic_code varchar(128),
    product_pkg_spec varchar(128),
    sales_spec varchar(128),
    place_of_origin varchar(512),
    bar_code varchar(512),
    share decimal,
    main_meas_unit varchar(128),
    sec_meas_unit varchar(128),
    sec_meas_gross_weight varchar(128),
    meas_unit_convert decimal,
    product_type varchar(128),
    remark varchar(128),
    product_attr varchar(128),
    default_repo varchar(1024),
    created_by varchar(64),
    created_time timestamp(128),
    updated_by varchar(128),
    updated_time timestamp(64)
);
--建索引
--主键索引，数据库自动创建, primary key标记的字段
create index nk_product_info_product_name on public.product_info using btree("product_name");
create index nk_product_info_create_time on public.product_info using btree("created_time");
create index nk_product_info_bar_code on public.product_info using btree("bar_code");


--注释
comment on table public.product_info is '商品表';
comment on column public.product_info.product_id is '商品ID';
comment on column public.product_info.product_name is '商品名称';
comment on column public.product_info.product_abbr is '商品简名';
comment on column public.product_info.mnemonic_code is '助记码';
comment on column public.product_info.product_pkg_spec is '包装规格';
comment on column public.product_info.sales_spec is '销售规格';
comment on column public.product_info.place_of_origin is '产地';
comment on column public.product_info.bar_code is '条形码';
comment on column public.product_info.share is '份额';
comment on column public.product_info.main_meas_unit is '主计量单位';
comment on column public.product_info.sec_meas_unit is '辅计量单位';
comment on column public.product_info.sec_meas_gross_weight is '辅计量毛重';
comment on column public.product_info.meas_unit_convert is '计量单位换算';
comment on column public.product_info.product_type is '商品分类';
comment on column public.product_info.remark is '备注';
comment on column public.product_info.product_attr is '销售,采购,附件,影响库存<当销售或采购的属性没有勾选时,此商品在销售或采购查询商品的时候无法查询出来>';
comment on column public.product_info.default_repo is '默认仓库';
comment on column public.product_info.created_by is '创建人';
comment on column public.product_info.created_time is '创建时间';
comment on column public.product_info.updated_by is '更新人';
comment on column public.product_info.updated_time is '更新时间';

--赋权
grant select,insert,update,delete,truncate on table public.product_info to corey;

