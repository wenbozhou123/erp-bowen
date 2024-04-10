package com.bowen.tools;

/**
 * 生成java 实体类的 入参类
 * */
public class GenerateEntityProperties {
    /**
     * 数据库URL
     * */
    private String url = "jdbc:postgresql://127.0.0.1:5432/corey";
    /**
     * 数据库用户名
     * */
    private String user = "corey";
    /**
     * 数据库密码
     * */
    private String password = "admin_1234";
    /**
     * 需要身成的表名
     * */
    private String tableName = "product_info";
    /**
     * 生成java类保存路径
     * */
    private String savePath = "/home/zhoubowen/myProjects/erp-plus/base-info/src/main/java/com/bowen/baseinfo/archiveMana/productsArchive";

    /**
     * 注解类型，
     * JPA     一般有hibernate-core实现，JPA 3.0之前的版本
     * SPRING_DATA_JPA      spring-boot-starter-data-jpa实现，JPA 3.0及更高版本的一部分
     * MY_BATIS_PLUS        mybatis-plus实现
     * */
    private String annotationType = GenerateTypeEnum.JPA.name();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getAnnotationType() {
        return annotationType;
    }

    public void setAnnotationType(String annotationType) {
        this.annotationType = annotationType;
    }
}
