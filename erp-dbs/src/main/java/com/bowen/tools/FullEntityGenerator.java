package com.bowen.tools;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.*;
import java.util.*;

/**
 * 根据表结构生成实体java类
 * */
public class FullEntityGenerator {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:postgresql://127.0.0.1:5432/corey";
        String user = "corey";
        String password = "admin_1234";
        String tableName = "product_info";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, tableName, null);

            StringBuilder classContent = new StringBuilder();
            List<String> methodContents = new ArrayList<>();
            classContent.append("@Entity\n@Table(name=\"").append(tableName).append("\")\npublic class ")
                    .append(convertToCamelCase(tableName, true)).append(" {\n\n");

            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String columnType = columns.getString("TYPE_NAME");
                String fieldName = convertToCamelCase(columnName, false);
                String fieldType = sqlTypeToJavaTypeForPostgres(columnType);

                // Assuming ID for the first column
                if (columns.isFirst()) classContent.append("    @Id\n");

                classContent.append("    @Column(name=\"").append(columnName).append("\")\n")
                        .append("    private ").append(fieldType).append(" ").append(fieldName).append(";\n\n");

                // Generate getter
                methodContents.add(generateGetter(fieldName, fieldType));

                // Generate setter
                methodContents.add(generateSetter(fieldName, fieldType));
            }

            // Append methods to class content
            methodContents.forEach(classContent::append);

            classContent.append("}\n");

            System.out.println(classContent);
        }
    }

    private static String convertToCamelCase(String input, boolean startWithCapital) {
        StringBuilder result = new StringBuilder();
        for (String part : input.split("_")) {
            if (result.length() == 0 && !startWithCapital) {
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
        return "    public " + fieldType + " get" + convertToCamelCase(fieldName, true) + "() {\n" +
                "        return this." + fieldName + ";\n" +
                "    }\n\n";
    }

    private static String generateSetter(String fieldName, String fieldType) {
        return "    public void set" + convertToCamelCase(fieldName, true) + "(" + fieldType + " " + fieldName + ") {\n" +
                "        this." + fieldName + " = " + fieldName + ";\n" +
                "    }\n\n";
    }
}
