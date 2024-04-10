package com.bowen.tools.generateEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateEntityConstant {

    //JPA默认的引用
    private static List<String> defaultJpaImportContents = new ArrayList<>(){{
        add("import javax.persistence.Column;\n");
        add("import javax.persistence.Entity;\n");
        add("import javax.persistence.Id;\n");
        add("import javax.persistence.Table;\n");
    }};

    //SpringDataJpa默认的引用
    private static List<String> defaultSpringDataJpaImportContents = new ArrayList<>(){{
        add("import jakarta.persistence.Column;\n");
        add("import jakarta.persistence.Entity;\n");
        add("import jakarta.persistence.Id;\n");
        add("import jakarta.persistence.Table;\n");
    }};

    //mybatis-plus默认的引用
    private static List<String> defaultMybatisPLusImportContents = new ArrayList<>(){{
        add("import com.baomidou.mybatisplus.annotation.TableName;\n");
        add("import com.baomidou.mybatisplus.annotation.TableId;\n");
        add("import com.baomidou.mybatisplus.annotation.TableField;\n");
    }};


    public static Map<String, List<String>> annotation2ImportContent = new HashMap<>(){{
        put(GenerateTypeEnum.JPA.name(), defaultJpaImportContents);
        put(GenerateTypeEnum.SPRING_DATA_JPA.name(), defaultSpringDataJpaImportContents);
        put(GenerateTypeEnum.MY_BATIS_PLUS.name(), defaultMybatisPLusImportContents);

    }};
}
