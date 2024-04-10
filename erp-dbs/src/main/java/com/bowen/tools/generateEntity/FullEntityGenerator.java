package com.bowen.tools.generateEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.function.BiFunction;

/**
 * 根据表结构生成实体java类
 * */
public class FullEntityGenerator {

    public static void main(String[] args) throws Exception {
        GenerateEntityProperties properties = new GenerateEntityProperties();
        properties.setAnnotationType(GenerateTypeEnum.SPRING_DATA_JPA.name());
        generateJavaEntityByTableNames(properties);
    }

    public static void generateJavaEntityByTableNames(GenerateEntityProperties properties) throws SQLException {
        String url = properties.getUrl();
        String user = properties.getUser();
        String password = properties.getPassword();
        List<String> tableNames = properties.getTableNames();
        List<String> savePaths = properties.getSavePaths();
        String annotationType = properties.getAnnotationType();

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            DatabaseMetaData metaData = connection.getMetaData();
            for(int i = 0; i < tableNames.size(); i++) {
                generateJavaEntityByTableName(metaData, tableNames.get(i), annotationType, savePaths.get(i));
            }
        }
    }

    private static void generateJavaEntityByTableName(DatabaseMetaData metaData, String tableName, String annotationType, String savePath) throws SQLException {
        ResultSet columns = metaData.getColumns(null, null, tableName, null);

        StringBuilder classContent = new StringBuilder();
        List<String> methodContents = new ArrayList<>();
        List<String> packageAndImportContents = new ArrayList<>();
        packageAndImportContents.add("import java.io.Serializable;\n");

        //增加@Table等注解
        packageAndImportContents.addAll(GenerateEntityConstant.annotation2ImportContent.get(annotationType));

        String packagePath = savePath
                .replaceAll("\\\\", ".")
                .replaceAll("/", ".")
                .replaceAll("^.*src(\\.main\\.java\\.)?", "")
                + ";";

        String className = convertToCamelCase(tableName, true) + "Entity";

        //增加类注释
        classContent.append("/**\n" +
                " * @Description  \n" +
                " * @Author  ZhouBowen\n" +
                " * @Date "+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " \n" +
                " */\n");

        //生成类头和注解
        classContent.append(generateClassHeadAndAnnotation(tableName,className, annotationType));

        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            String columnType = columns.getString("TYPE_NAME");
            String remarks = columns.getString("REMARKS");
            String fieldName = convertToCamelCase(columnName, false);
            String fieldType = sqlTypeToJavaTypeForPostgres(columnType);

            //增加字段注释
            classContent.append("\t/**\n");
            classContent.append("\t * "+ remarks + "\n");
            classContent.append("\t */\n");
            // 生成字段
            classContent.append(generateClassFieldAndAnnotation(columns, columnName, fieldType, fieldName, annotationType));

            // Generate getter
            methodContents.add(generateGetter(fieldName, fieldType));

            // Generate setter
            methodContents.add(generateSetter(fieldName, fieldType));

            //Generate import
            String importContent = generateImport(fieldType);
            if (importContent != null) {
                packageAndImportContents.add(importContent);
            }

        }

        if (!packageAndImportContents.isEmpty()) {
            packageAndImportContents.add(0, "\n");
        }

        //Generate package and  import content
        packageAndImportContents.add( "\n");
        packageAndImportContents.add("package " + packagePath+ "\n");

        BiFunction<Integer, String, StringBuilder> StringBufferInsertMethodReference = classContent::insert;
        packageAndImportContents.forEach(e -> StringBufferInsertMethodReference.apply(0, e));

        // Append methods to class content
        methodContents.forEach(classContent::append);

        classContent.append("}\n");

        //System.out.println(classContent);
        generateJavaFile(classContent.toString(), savePath +"/"+className +".java");
    }

    //生成类头和注解
    public static String generateClassHeadAndAnnotation(String tableName, String className, String annotationType){
        StringBuilder resBuilder = new StringBuilder();
        if (GenerateTypeEnum.MY_BATIS_PLUS.name().equals(annotationType)) {
            resBuilder.append("@TableName(\"").append(tableName).append("\")\npublic class ")
                    .append(className).append(" implements Serializable ").append(" {\n")
                    .append("\tprivate static final long serialVersionUID =  "+Math.abs(new Random().nextLong())+"L;")
                    .append("\n\n");
        }

        if (resBuilder.isEmpty()) {
            resBuilder.append("@Entity\n@Table(name=\"").append(tableName).append("\")\npublic class ")
                    .append(className).append(" implements Serializable ").append(" {\n")
                    .append("\tprivate static final long serialVersionUID =  "+Math.abs(new Random().nextLong())+"L;")
                    .append("\n\n");
        }
        return resBuilder.toString();
    }

    //生成的属性注解
    public static String generateClassFieldAndAnnotation(ResultSet columns,String columnName, String fieldType, String fieldName, String annotationType) throws SQLException {
        StringBuilder resBuilder = new StringBuilder();
        if (GenerateTypeEnum.MY_BATIS_PLUS.name().equals(annotationType)) {
            if (columns.isFirst()) {
                resBuilder.append("\t@TableId(\"").append(columnName).append("\")\n");
            }else {
                resBuilder.append("\t@TableField(\"").append(columnName).append("\")\n");
            }
            resBuilder.append("\tprivate ").append(fieldType).append(" ").append(fieldName).append(";\n\n");
        }

        if (resBuilder.length() == 0) {
            if (columns.isFirst()) resBuilder.append("\t@Id\n");
            resBuilder.append("\t@Column(name=\"")
                    .append(columnName).append("\")\n")
                    .append("\tprivate ")
                    .append(fieldType).append(" ")
                    .append(fieldName).append(";\n\n");
        }
        return resBuilder.toString();
    }

    public static String convertToCamelCase(String input, boolean startWithCapital) {
        StringBuilder result = new StringBuilder();
        for (String part : input.split("_")) {
            if (result.isEmpty() && !startWithCapital) {
                result.append(part.toLowerCase());
            } else {
                result.append(part.substring(0, 1).toUpperCase()).append(part.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    private static String sqlTypeToJavaTypeForPostgres(String sqlType) {
        String sqlTypeLower = sqlType.toLowerCase();
        switch (sqlTypeLower) {
            case "varchar":
            case "char":
            case "text":
                return "String";
            case "int4":
            case "tinyint":
            case "smallint":
            case "mediumint":
            case "int":
                return "Integer";
            case "int8":
            case "bigint":
                return "Long";
            case "float":
            case "double":
            case "real":
                return "Double";
            case "decimal":
                return "BigDecimal";
            case "datetime":
            case "timestamp":
                return "LocalDateTime";
            case "date":
                return "LocalDate";
            case "time":
                return "LocalTime";
            case "blob":
            case "binary":
            case "clob":
            case "raw":
            case "image":
                return "InputStream";
            // Add more cases as needed
            default:
                return "Object";
        }
    }

    private static String generateGetter(String fieldName, String fieldType) {
        String convertFieldName = fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
        return "\tpublic " + fieldType + " get" + convertFieldName + "() {\n" +
                "\t\treturn this." + fieldName + ";\n" +
                "\t}\n\n";
    }

    private static String generateSetter(String fieldName, String fieldType) {
        String convertFieldName = fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
        return "\tpublic void set" + convertFieldName + "(" + fieldType + " " + fieldName + ") {\n" +
                "\t\tthis." + fieldName + " = " + fieldName + ";\n" +
                "\t}\n\n";
    }

    private static String generateImport(String fieldType){
        switch (fieldType) {
            case "BigDecimal":
                return "import java.math.BigDecimal;\n";
            case "LocalDateTime":
                return "import java.time.LocalDateTime;\n";
            case "LocalDate":
                return "import java.time.LocalDate;\n";
            case "LocalTime":
                return "import java.time.LocalTime;\n";
            case "InputStream":
                return "import java.io.InputStream;\n";
            // Add more cases as needed
            default:
                return null;
        }
    }

    public static void generateJavaFile(String content, String filePath) {
        try {
            Files.write(Paths.get(filePath), Collections.singleton(content), StandardOpenOption.CREATE, StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the file: " + e.getMessage());
        }
    }

}
