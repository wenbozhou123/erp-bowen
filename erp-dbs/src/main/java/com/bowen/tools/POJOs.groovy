import com.intellij.database.model.DasTable
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil
import java.text.SimpleDateFormat

/*
 * Available context bindings:
 *   SELECTION   Iterable<DasObject>
 *   PROJECT     project
 *   FILES       files helper
 * intellij 根据表生成java实体类的 groovy 脚本
 */

packageName = ""

typeMapping = [
        (~/(?i)tinyint|smallint|mediumint|int/)    : "Integer",
        (~/(?i)bigint/)                         : "Long",
        (~/(?i)float|double|real/)            : "Double",
        (~/(?i)decimal/)                       :"BigDecimal",
        (~/(?i)datetime/)                      : "LocalDateTime",
        (~/(?i)timestamp/)                     : "LocalDateTime",
        (~/(?i)date/)                           : "LocalDate",
        (~/(?i)time/)                           : "LocalTime",
        (~/(?i)blob|binary|bfile|clob|raw|image/): "InputStream",
        (~/(?i)/)                                : "String"
]


FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store generated files") { dir ->
    SELECTION.filter { it instanceof DasTable }.each { generate(it, dir) }
}

def generate(table, dir) {
    packageName = getPackageName(dir)
    def className = javaName(table.getName(), true)+"Entity"
    def fields = calcFields(table)
    new File(dir, className + ".java").withPrintWriter("utf-8") { out -> generate(table,out, className, fields) }
}

def generate(table,out, className, fields) {
    out.println "package $packageName"
    out.println ""
    out.println "import java.io.Serializable;"
    out.println "import lombok.Data;"

    Set<String> importTypes = []

    fields.each() {
        if (it.type != "") {
            if (it.type == "BigDecimal" || it.type == "LocalDateTime" || it.type == "LocalDate" || it.type == "LocalTime"|| it.type == "InputStream")
                importTypes += it.type
        }
    }

    if(importTypes.size()>0){
        importTypes.forEach{
            if (it == 'BigDecimal') out.println("import java.math.BigDecimal;")
            if (it == 'LocalDateTime') out.println("import java.time.LocalDateTime;")
            if (it == 'LocalDate') out.println("import java.time.LocalDate;")
            if (it == 'LocalTime') out.println("import java.time.LocalTime;")
            if (it == 'InputStream') out.println("import java.io.InputStream;")
        }
    }

    out.println ""
    out.println ""
    out.println "/**\n" +
            " * @Description  \n" +
            " * @Author  dw\n" +
            " * @Date "+ new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " \n" +
            " */"
    out.println "@Data"
    out.println "public class $className implements Serializable {"
    out.println ""
    out.println genSerialID()
    fields.each() {
        out.println ""
        // 输出注释
        if (isNotEmpty(it.commoent)) {
            out.println "\t/**"
            out.println "\t * ${it.commoent.toString()}"
            out.println "\t */"
        }

        // 输出成员变量
        out.println "\tprivate ${it.type} ${it.name};"
    }

    out.println ""

    //生成get set函数
    /*fields.each() {
        out.println ""
        out.println "  public ${it.type} get${it.name.capitalize()}() {"
        out.println "    return ${it.name};"
        out.println "  }"
        out.println ""
        out.println "  public void set${it.name.capitalize()}(${it.type} ${it.name}) {"
        out.println "    this.${it.name} = ${it.name};"
        out.println "  }"
        out.println ""
    }*/

    out.println "}"
}

def calcFields(table) {
    DasUtil.getColumns(table).reduce([]) { fields, col ->
        def spec = Case.LOWER.apply(col.getDataType().getSpecification())

        def typeStr = typeMapping.find { p, t -> p.matcher(spec).find() }.value
        def comm =[
                spec: spec,
                colName : col.getName(),
                name :  javaName(col.getName(), false),
                type : typeStr,
                commoent: col.getComment()]
        fields += [comm]
    }
}

def javaName(str, capitalize) {
    def s = com.intellij.psi.codeStyle.NameUtil.splitNameIntoWords(str)
            .collect { Case.LOWER.apply(it).capitalize() }
            .join("")
            .replaceAll(/[^\p{javaJavaIdentifierPart}[_]]/, "_")
    capitalize || s.length() == 1? s : Case.LOWER.apply(s[0]) + s[1..-1]
}

static String genSerialID()
{
    return "\tprivate static final long serialVersionUID =  "+Math.abs(new Random().nextLong())+"L;"
}

def isNotEmpty(content) {
    return content != null && content.toString().trim().length() > 0
}

// 获取包所在文件夹路径
def getPackageName(dir) {
    return dir.toString().replaceAll("\\\\", ".").replaceAll("/", ".").replaceAll("^.*src(\\.main\\.java\\.)?", "") + ";"
}