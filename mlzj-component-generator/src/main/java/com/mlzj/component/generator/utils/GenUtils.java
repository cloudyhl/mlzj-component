package com.mlzj.component.generator.utils;


import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import com.mlzj.component.generator.entity.ColumnEntity;
import com.mlzj.component.generator.entity.TableEntity;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * 代码生成器   工具类
 *
 * @author yhl
 * @since 2022/8/4
 */
public class GenUtils {

    public static List<String> getTemplates(){
        List<String> templates = new ArrayList<String>();
        templates.add("template/Entity.java.vm");
        templates.add("template/Mapper.java.vm");
        templates.add("template/Mapper.xml.vm");
        templates.add("template/Service.java.vm");
        templates.add("template/ServiceImpl.java.vm");
        templates.add("template/Controller.java.vm");
        templates.add("template/AddReqDto.java.vm");
        templates.add("template/DelReqDto.java.vm");
        templates.add("template/ModReqDto.java.vm");
        templates.add("template/PageReqDto.java.vm");
        templates.add("template/AddRspDto.java.vm");
        templates.add("template/ModRspDto.java.vm");
        templates.add("template/DelRspDto.java.vm");
        templates.add("template/DetailRspDto.java.vm");
        templates.add("template/PageRspDto.java.vm");
        templates.add("template/Dto2PoConvert.java.vm");
        templates.add("template/Po2DtoConvert.java.vm");
        return templates;
    }

    /**
     * 生成代码
     */
    public static void generatorCode(Map<String, String> table,
                                     List<Map<String, String>> columns, ZipOutputStream zip) {
        //配置信息
        Configuration config = getConfig();
        boolean hasBigDecimal = false;
        //表信息
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(table.get("tableName" ));
        tableEntity.setComments(table.get("tableComment"));
        //表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), config.getString("tablePrefix"));
        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));

        //列信息
        List<ColumnEntity> columsList = new ArrayList<>();
        for(Map<String, String> column : columns){
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(column.get("columnName" ));
            String dataType = column.get("dataType" );
            if ("int".equals(dataType)){
                dataType = "INTEGER";
            } else if ("datetime".equals(dataType)){
                dataType = "TIMESTAMP";
            }
            columnEntity.setDataType(dataType.toUpperCase());
            columnEntity.setComments(column.get("columnComment" ));
            columnEntity.setExtra(column.get("extra" ));

            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.getString(dataType, "unknowType" );
            columnEntity.setAttrType(attrType);
            if (!hasBigDecimal && attrType.equals("BigDecimal" )) {
                hasBigDecimal = true;
            }
            //是否主键
            if ("PRI".equalsIgnoreCase(column.get("columnKey" )) && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
            }

            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);

        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
        Velocity.init(prop);
        String mainPath = config.getString("mainPath" );
        mainPath = StringUtils.isBlank(mainPath) ? "com.shsc" : mainPath;
        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableEntity.getTableName());
        map.put("comments", tableEntity.getComments());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("classname", tableEntity.getClassname());
        map.put("pathName", tableEntity.getClassname().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("mainPath", mainPath);
        map.put("package", config.getString("package" ));
        map.put("moduleName", config.getString("moduleName" ));
        map.put("author", config.getString("author" ));
        map.put("email", config.getString("email" ));
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        map.put("tableObj", config.getString("tableObj" ));
        map.put("objPrefix", config.getString("objPrefix" ));
        map.put("bizPrefix", config.getString("bizPrefix" ));
        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8" );
            tpl.merge(context, sw);

            try {
                //添加到zip
                zip.putNextEntry(new ZipEntry(getFileName(template, tableEntity.getClassName(), config.getString("package" ), config.getString("moduleName" ))));
                IOUtils.write(sw.toString(), zip, "UTF-8" );
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                throw new RRException("渲染模板失败，表名：" + tableEntity.getTableName(), e);
            }
        }
    }


    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "" );
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replaceFirst(tablePrefix, "" );
        }
        return columnToJava(tableName);
    }

    /**
     * 获取配置信息
     */
    public static Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties" );
        } catch (ConfigurationException e) {
            throw new RRException("获取配置文件失败，", e);
        }
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, String className, String packageName, String moduleName) {
        String packagePath = "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator;
        }

        if (template.contains("Entity.java.vm" )) {
            return packagePath + "entity" + File.separator + "po" + File.separator + className + "Po.java";
        }

        if (template.contains("Mapper.java.vm" )) {
            return packagePath + "mapper" + File.separator + className + "PoMapper.java";
        }

        if (template.contains("Service.java.vm" )) {
            return packagePath + "service" + File.separator + "I"+className + "Service.java";
        }

        if (template.contains("ServiceImpl.java.vm" )) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if (template.contains("Controller.java.vm" )) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }

        if (template.contains("AddReqDto.java.vm" )) {
            return packagePath + "dto" + File.separator + moduleName+ File.separator + "request" + File.separator + className + "AddReqDto.java";
        }

        if (template.contains("ModReqDto.java.vm" )) {
            return packagePath + "dto" + File.separator + moduleName+ File.separator + "request" + File.separator + className + "ModReqDto.java";
        }

        if (template.contains("DelReqDto.java.vm" )) {
            return packagePath + "dto" + File.separator + moduleName+ File.separator + "request" + File.separator + className + "DelReqDto.java";
        }

        if (template.contains("PageReqDto.java.vm" )) {
            return packagePath + "dto" + File.separator + moduleName+ File.separator + "request" + File.separator + className + "PageReqDto.java";
        }

        if (template.contains("AddRspDto.java.vm" )) {
            return packagePath + "dto" + File.separator + moduleName+ File.separator + "response" + File.separator + className + "AddRspDto.java";
        }

        if (template.contains("ModRspDto.java.vm" )) {
            return packagePath + "dto" + File.separator + moduleName+ File.separator + "response" + File.separator + className + "ModRspDto.java";
        }

        if (template.contains("DelRspDto.java.vm" )) {
            return packagePath + "dto" + File.separator + moduleName+ File.separator + "response" + File.separator + className + "DelRspDto.java";
        }

        if (template.contains("DetailRspDto.java.vm" )) {
            return packagePath + "dto" + File.separator + moduleName+ File.separator + "response" + File.separator + className + "DetailRspDto.java";
        }

        if (template.contains("PageRspDto.java.vm" )) {
            return packagePath + "dto" + File.separator + moduleName+ File.separator + "response" + File.separator + className + "PageRspDto.java";
        }

        if (template.contains("Dto2PoConvert.java.vm" )) {
            return packagePath + "convert" + File.separator + className + "Dto2PoConvert.java";
        }

        if (template.contains("Po2DtoConvert.java.vm" )) {
            return packagePath + "convert" + File.separator + className + "Po2DtoConvert.java";
        }

        if (template.contains("Mapper.xml.vm" )) {
            return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + className + "Mapper.xml";
        }

//        if (template.contains("menu.sql.vm" )) {
//            return className.toLowerCase() + "_menu.sql";
//        }

//        if (template.contains("index.vue.vm" )) {
//            return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "views" + File.separator + "modules" +
//                    File.separator + moduleName + File.separator + className.toLowerCase() + ".vue";
//        }
//
//        if (template.contains("add-or-update.vue.vm" )) {
//            return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "views" + File.separator + "modules" +
//                    File.separator + moduleName + File.separator + className.toLowerCase() + "-add-or-update.vue";
//        }

        return null;
    }
}
