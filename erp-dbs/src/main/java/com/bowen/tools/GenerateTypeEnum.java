package com.bowen.tools;

public enum GenerateTypeEnum {

    /**
     * 一般有hibernate-core实现，JPA 3.0之前的版本
     * */
    JPA,
    /**
     * spring-boot-starter-data-jpa实现，JPA 3.0及更高版本的一部分
     * */
    SPRING_DATA_JPA,
    /**
     * mybatis-plus实现
     * */
    MY_BATIS_PLUS;
}
