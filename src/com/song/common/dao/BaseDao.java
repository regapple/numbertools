package com.song.common.dao;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.song.common.util.DateUtils;

/**
 * 基于spring jdbcTemple的基础DAO
 * 
 * 
 */
public class BaseDao {

    private static Logger logger = Logger.getLogger(BaseDao.class);
    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    /**
     * 常见MYSQL字段类型对应的java数据类型：
     * 
     * int java.lang.Integer
     * double java.lang.Double
     * decimal java.math.BigDecimal
     * char java.lang.String
     * varchar java.lang.String
     * date java.sql.Date
     * time java.sql.Time
     * timestamp java.sql.Timestamp
     * datetime java.sql.Timestamp
     */

    /**
     * 根据完整SQL查询，返回结果为List
     * 
     * @param sql
     * @return
     */
    public List<Map<String, Object>> queryForList(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * 根据完整SQL查询，返回结果为SqlRowSet
     * SqlRowSet其实就是ResultSet
     * 
     * @param sql
     * @return
     */
    public SqlRowSet queryForRowSet(String sql) {
        return jdbcTemplate.queryForRowSet(sql);
    }

    /**
     * 根据完整SQL查询，返回结果为包含bean对象的List
     * 注意：1. bean对象必须有无参构造函数
     * 2. bean的成员变量名必须和数据库字段名相同
     * 3. bean的成员变量必须是String
     * 4. 对于某些数据库字段，转成字符串是否有精度的问题还有待验证
     * 
     * @param sql
     * @param T bean对象的Class
     * @return
     */
    public List<Object> queryForObjectList(String sql, Class<?> T) {
        List<Object> ls = new ArrayList<Object>();

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> map : result) {
            try {
                Object obj = T.newInstance();
                Iterator<String> it = map.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    try {
                        Method method = T.getMethod(
                                "set" + key.substring(0, 1).toUpperCase()
                                        + key.substring(1), String.class);
                        if (method != null && obj != null) {
                            method.invoke(obj,
                                    map.get(key) != null ? map.get(key)
                                            .toString() : null);
                        }
                    } catch (NoSuchMethodException e) {
                        logger.error("NoSuchMethodException:", e);
                    } catch (Exception e) {
                        logger.error("queryForObjectList:", e);
                    }
                }
                ls.add(obj);
            } catch (Exception e) {
                logger.error("queryForObjectList:", e);
            }
        }

        return ls;
    }

    /**
     * 根据完整SQL查询，返回结果为包含bean对象的List
     * 注意：1. bean对象必须有无参构造函数
     * 2. bean的成员变量名必须和数据库字段名相同
     * 3. bean的各个成员变量类型要和数据库查询结果的字段类型相同，比如数据库为int，查询出来的结果对象被jdbcTemple转换成Integer
     * 4. 对于某些数据库字段，转成字符串是否有精度的问题还有待验证
     * 
     * @param sql
     * @param T bean对象的Class
     * @return
     */
    public List<Object> queryForObjectListStrict(String sql, Class<?> T) {
        List<Object> ls = new ArrayList<Object>();

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> map : result) {
            try {
                Object obj = T.newInstance();
                Iterator<String> it = map.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    Object value = map.get(key);
                    try {
                        if (value != null) {
                            Method method = T.getMethod(
                                    "set" + key.substring(0, 1).toUpperCase()
                                            + key.substring(1),
                                    value.getClass());
                            if (method != null && obj != null) {
                                method.invoke(obj, value);
                            }
                        }
                    } catch (NoSuchMethodException e) {
                        // System.out.println(e.getMessage());
                    } catch (Exception e) {
                        logger.error("queryForObjectListStrict:", e);
                    }
                }
                ls.add(obj);
            } catch (Exception e) {
                logger.error("queryForObjectListStrict:", e);
            }
        }

        return ls;
    }

    /**
     * 根据完整SQL查询，返回结果为包含bean对象的List
     * 注意：1. bean对象必须有无参构造函数
     * 2. bean的成员变量名可以和数据库字段名不同，通过第三个参数namedMap完成对照
     * 3. bean的成员变量必须是String
     * 4. 对于某些数据库字段，转成字符串是否有精度的问题还有待验证
     * 
     * @param sql
     * @param T bean对象的Class
     * @param namedMap bean的字段名和数据库字段名的对照关系
     * @return
     */
    public List<Object> queryForObjectListByNamedMap(String sql, Class<?> T,
            Map<String, String> namedMap) {
        List<Object> ls = new ArrayList<Object>();
        if (namedMap == null || namedMap.size() == 0) {
            return ls;
        }

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> map : result) {
            try {
                Object obj = T.newInstance();
                Iterator<String> it = map.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    String actualName = namedMap.get(key);
                    try {
                        if (actualName != null
                                && actualName.trim().length() >= 1) {
                            Method method = T.getMethod("set"
                                    + actualName.substring(0, 1).toUpperCase()
                                    + actualName.substring(1), String.class);
                            if (method != null && obj != null) {
                                method.invoke(obj, map.get(key) != null ? map
                                        .get(key).toString() : null);
                            }
                        }
                    } catch (NoSuchMethodException e) {
                        // System.out.println(e.getMessage());
                    } catch (Exception e) {
                        logger.error("queryForObjectListByNamedMap:", e);
                    }
                }
                ls.add(obj);
            } catch (Exception e) {
                logger.error("queryForObjectListByNamedMap:", e);
            }
        }

        return ls;
    }

    /**
     * 根据完整SQL查询，返回结果为包含bean对象的List
     * 注意：1. bean对象必须有无参构造函数
     * 2. bean的成员变量名可以和数据库字段名不同，通过第三个参数namedMap完成对照
     * 3. bean的各个成员变量类型要和数据库查询结果的字段类型相同，比如数据库为int，查询出来的结果对象被jdbcTemple转换成Integer
     * 4. 对于某些数据库字段，转成字符串是否有精度的问题还有待验证
     * 
     * @param sql
     * @param T bean对象的Class
     * @param namedMap bean的字段名和数据库字段名的对照关系
     * @return
     */
    public List<Object> queryForObjectListStrictByNamedMap(String sql,
            Class<?> T, Map<String, String> namedMap) {
        List<Object> ls = new ArrayList<Object>();
        if (namedMap == null || namedMap.size() == 0) {
            return ls;
        }

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> map : result) {
            try {
                Object obj = T.newInstance();
                Iterator<String> it = map.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    Object value = map.get(key);
                    String actualName = namedMap.get(key);

                    try {
                        if (value != null && actualName != null
                                && actualName.trim().length() >= 1) {
                            Method method = T
                                    .getMethod(
                                            "set"
                                                    + actualName
                                                            .substring(0, 1)
                                                            .toUpperCase()
                                                    + actualName.substring(1),
                                            value.getClass());
                            if (method != null && obj != null) {
                                method.invoke(obj, value);
                            }
                        }
                    } catch (NoSuchMethodException e) {
                        // System.out.println(e.getMessage());
                    } catch (Exception e) {
                        logger.error("queryForObjectListStrictByNamedMap:", e);
                    }
                }
                ls.add(obj);
            } catch (Exception e) {
                logger.error("queryForObjectListStrictByNamedMap:", e);
            }
        }

        return ls;
    }

    /**
     * 执行插入，更新和删除
     * 
     * @param sql
     * @return
     */
    public int update(String sql) {
        return jdbcTemplate.update(sql);
    }

    /**
     * 根据Bean生成SQL
     * 注意：本方法使用的Bean必须是convertDBToPO方法生成的，传入其他Bean本方法不保证能成功执行！
     * 
     * @param obj convertDBToPO方法生成的Bean对象
     * @return
     * @throws Exception
     */
    public String genSqlFromBean(Object obj)
        throws Exception {
        if (obj != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Field field = obj.getClass().getDeclaredField("tableName");
            if (field != null) {
                StringBuffer sql = new StringBuffer();
                // 表名必须是String类型
                String tableName = (String) field.get(obj);
                // 获取对应关系
                Method method = obj.getClass().getDeclaredMethod("getNamedMap");
                if (method != null) {
                    // 字段
                    List<String> columns = new ArrayList<String>();
                    // 值
                    List<Object> values = new ArrayList<Object>();
                    @SuppressWarnings("unchecked")
                    Map<String, String> map = (Map<String, String>) method
                            .invoke(obj);
                    if (map != null) {
                        Iterator<String> it = map.keySet().iterator();
                        while (it.hasNext()) {
                            String key = it.next();
                            // 本工程所有MySQL表的id字段都是自增，所以这里忽略id字段
                            if (!"id".equalsIgnoreCase(key)) {
                                String param = map.get(key);
                                Method m = obj.getClass().getDeclaredMethod(
                                        "get"
                                                + param.substring(0, 1)
                                                        .toUpperCase()
                                                + param.substring(1));
                                if (m != null) {
                                    columns.add(key);
                                    values.add(m.invoke(obj));
                                }
                            }
                        }
                    }

                    if (columns.size() > 0 && values.size() > 0
                            && columns.size() == values.size()) {
                        sql.append("insert into ").append(tableName)
                                .append(" (");
                        for (int i = 0; i < columns.size(); i++) {
                            sql.append(columns.get(i));
                            if (i < columns.size() - 1) {
                                sql.append(", ");
                            }
                        }
                        sql.append(") values (");
                        for (int i = 0; i < values.size(); i++) {
                            Object value = values.get(i);
                            if (value instanceof String) {
                                sql.append("'")
                                        .append(transferSingleQuote((String) value))
                                        .append("'");
                            } else if (value instanceof Integer) {
                                sql.append(((Integer) value).intValue());
                            } else if (value instanceof Double) {
                                sql.append(((Double) value).doubleValue());
                            } else if (value instanceof BigDecimal) {
                                sql.append(((BigDecimal) value).toString());
                            } else if (value instanceof java.sql.Date) {
                                sql.append("'")
                                        .append(DateUtils
                                                .formatDate((java.sql.Date) value))
                                        .append("'");
                            } else if (value instanceof java.sql.Time) {
                                sql.append("'")
                                        .append(sdf
                                                .format((java.sql.Time) value))
                                        .append("'");
                            } else if (value instanceof java.sql.Timestamp) {
                                sql.append("'")
                                        .append(DateUtils
                                                .formatDateTime((java.sql.Timestamp) value))
                                        .append("'");
                            } else {
                                sql.append(value);
                            }

                            if (i < columns.size() - 1) {
                                sql.append(", ");
                            }
                        }
                        sql.append(")");
                        logger.debug(sql);
                        return sql.toString();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 将Bean中的数据保存到数据库
     * 注意：本方法使用的Bean必须是convertDBToPO方法生成的，传入其他Bean本方法不保证能成功执行！
     * 
     * @param obj convertDBToPO方法生成的Bean对象
     * @return
     * @throws Exception
     */
    public int insertBean(Object obj)
        throws Exception {
        if (obj != null) {
            return jdbcTemplate.update(genSqlFromBean(obj));
        } else {
            return 0;
        }
    }

    /**
     * 批量将Bean中的数据保存到数据库
     * 注意：本方法使用的Bean必须是convertDBToPO方法生成的，传入其他Bean本方法不保证能成功执行！
     * 
     * @param obj convertDBToPO方法生成的Bean对象
     * @return
     * @throws Exception
     */
    public <T> int[] batchInsertBeans(List<T> objs)
        throws Exception {
        if (objs != null && objs.size() > 0) {
            List<String> sqls = new ArrayList<String>();
            for (Object obj : objs) {
                sqls.add(genSqlFromBean(obj));
            }
            return jdbcTemplate
                    .batchUpdate(sqls.toArray(new String[sqls.size()]));
        }
        return new int[0];
    }

    /**
     * 批量插入，更新和删除
     * 
     * @param sql
     * @return
     */
    public int[] batchUpdate(String[] sql) {
        return jdbcTemplate.batchUpdate(sql);
    }

    /**
     * 批量插入，更新和删除
     * 
     * @param sql
     * @param batchArgs 包含参数数组的List对象
     * @return
     */
    public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
        return jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    /**
     * 根据原始SQL进行分页查询
     * 注意，这里只针对MYSQL
     * 
     * @param sql 原始SQL，就是未包含分页功能的SQL
     * @param pageNo 当前页数
     * @param pageSize 每页记录条数
     * @return
     */
    public Page getPageByOriSql(String sql, int pageNo, int pageSize) {
        String totalSql = "select count(1) from (" + sql + ") as temp";
        sql = sql + " limit " + (pageNo - 1) * pageSize + ", " + pageSize;
        logger.debug("分页totalSql = " + totalSql);
        logger.debug("分页sql = " + sql);
        return getPage(totalSql, sql, pageNo, pageSize);
    }

    /**
     * 根据原始SQL进行分页查询
     * 注意，这里只针对MYSQL
     * 
     * @param sql 原始SQL，就是未包含分页功能的SQL
     * @param pageNo 当前页数
     * @param pageSize 每页记录条数
     * @param T bean对象的Class
     * @param namedMap bean的字段名和数据库字段名的对照关系
     * @param strictMod 是否使用字段类型严格匹配，true：使用 false：不使用
     * @return
     */
    public Page getPageByOriSql(String sql, int pageNo, int pageSize,
            Class<?> T, Map<String, String> namedMap, boolean strictMod) {
        String totalSql = "select count(1) from (" + sql + ") as temp";
        sql = sql + " limit " + (pageNo - 1) * pageSize + ", " + pageSize;
        logger.debug("分页totalSql = " + totalSql);
        logger.debug("分页sql = " + sql);
        return getPage(totalSql, sql, pageNo, pageSize, T, namedMap, strictMod);
    }

    /**
     * 根据分页SQL进行分页查询
     * 注意，这里只针对MYSQL
     * 
     * @param totalSql 查询总条数的SQL
     * @param sql 查询分页数据的SQL
     * @param pageNo 当前页数
     * @param pageSize 每页记录条数
     * @return
     */
    public Page getPage(String totalSql, String sql, int pageNo, int pageSize) {
        return getPage(totalSql, sql, pageNo, pageSize, null, null, false);
    }

    /**
     * 根据分页SQL进行分页查询
     * 注意，这里只针对MYSQL
     * 
     * @param totalSql 查询总条数的SQL
     * @param sql 查询分页数据的SQL
     * @param pageNo 当前页数
     * @param pageSize 每页记录条数
     * @param T bean对象的Class
     * @param namedMap bean的字段名和数据库字段名的对照关系
     * @param strictMod 是否使用字段类型严格匹配，true：使用 false：不使用
     * @return
     */
    public Page getPage(String totalSql, String sql, int pageNo, int pageSize,
            Class<?> T, Map<String, String> namedMap, boolean strictMod) {
        Page page = new Page();
        @SuppressWarnings("all")
        int totalCount = jdbcTemplate.queryForInt(totalSql);
        int totalPage = (totalCount / pageSize)
                + (totalCount % pageSize > 0 ? 1 : 0);
        List<?> result = new ArrayList<Object>();
        if (T == null) {
            result = jdbcTemplate.queryForList(sql);
        } else {
            if (namedMap == null) {
                if (strictMod) {
                    result = this.queryForObjectListStrict(sql, T);
                } else {
                    result = this.queryForObjectList(sql, T);
                }
            } else {
                if (strictMod) {
                    result = this.queryForObjectListStrictByNamedMap(sql, T,
                            namedMap);
                } else {
                    result = this
                            .queryForObjectListByNamedMap(sql, T, namedMap);
                }
            }
        }

        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        page.setTotalCount(totalCount);
        page.setTotalPage(totalPage);
        page.setResult(result);
        return page;
    }

    /**
     * 测试数据库字段类型
     * 字段结果类型：数据库字段对应的java类型
     * 字段类型：数据库字段本身的类型
     * 
     * @param sql
     */
    public void testColumnType(String sql) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        org.springframework.jdbc.support.rowset.SqlRowSetMetaData meta = rowSet
                .getMetaData();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            System.out.println("字段名：" + meta.getColumnName(i) + "  字段结果类型："
                    + meta.getColumnClassName(i) + "  字段类型："
                    + meta.getColumnTypeName(i));
        }
    }

    /**
     * 反转数据库表为JAVA BEAN文件
     * 
     * @param tableName 数据库表名，必填
     * @param className JAVA BEAN的类名，如果不传，则默认和数据库表名相同
     * @param packagePath JAVA BEAN的包路径，比如：com.test.bean，如果不填，则把文件到C盘根目录下
     */
    public void convertDBToPO(String tableName, String className,
            String packagePath) {
        if (tableName == null || "".equals(tableName.trim())) {
            System.out.println("必须输入数据库的表名！");
            return;
        }

        if (className == null || "".equals(className.trim())) {
            className = tableName.substring(0, 1).toUpperCase()
                    + tableName.substring(1);
        } else {
            className = className.substring(0, 1).toUpperCase()
                    + className.substring(1);
        }

        String file = "c:\\" + className + ".java";

        StringBuffer content = new StringBuffer();
        if (packagePath != null && !"".equals(packagePath.trim())) {
            content.append("package ").append(packagePath).append(";")
                    .append("\n");
            content.append("\n");

            try {
                String srcFolderName = "src";
                String projectPath = new File("").getAbsolutePath();

                SAXReader reader = new SAXReader();
                Document doc = reader.read(new File(projectPath
                        + "\\.classpath"));
                Element root = doc.getRootElement();
                List<?> ls = root.elements();
                for (Object obj : ls) {
                    Element e = (Element) obj;
                    Attribute a = e.attribute("kind");
                    // System.out.println(a.getValue());
                    if ("src".equals(a.getValue())) {
                        Attribute b = e.attribute("path");
                        // System.out.println(b.getValue());
                        srcFolderName = b.getValue();
                        break;
                    }
                }

                file = projectPath + "\\" + srcFolderName + "\\"
                        + packagePath.replace(".", "\\") + "\\" + className
                        + ".java";
            } catch (Exception e) {
                System.out.println("查询工程源文件目录失败！文件将默认写入C盘根目录下。");
                logger.error("queryForObjectListStrictByNamedMap:", e);
            }
        }
        content.append("public class ").append(className).append(" {")
                .append("\n");
        content.append("\t").append("public java.lang.String tableName = \"")
                .append(tableName).append("\";").append("\n");

        List<String> oldColumnNames = new ArrayList<String>();
        Map<String, String> columnInfo = new LinkedHashMap<String, String>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from "
                + tableName + " limit 0, 1");
        org.springframework.jdbc.support.rowset.SqlRowSetMetaData meta = rowSet
                .getMetaData();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            String name = meta.getColumnName(i);
            name = name.replace(" ", "");
            StringBuffer newName = new StringBuffer();
            for (int k = 0; k < name.length(); k++) {
                if (name.charAt(k) != '_' && name.charAt(k) != '-') {
                    if (k - 1 >= 0
                            && (name.charAt(k - 1) == '_' || name.charAt(k - 1) == '-')) {
                        newName.append(String.valueOf(name.charAt(k))
                                .toUpperCase());
                    } else {
                        newName.append(name.charAt(k));
                    }
                }
            }

            oldColumnNames.add(name);
            columnInfo.put(newName.toString(), meta.getColumnClassName(i));
            content.append("\t").append("private ")
                    .append(meta.getColumnClassName(i)).append(" ")
                    .append(newName).append(";").append("\n");
            // System.out.println("字段名：" + meta.getColumnName(i) + "  字段结果类型：" +
            // meta.getColumnClassName(i) + "  字段类型：" +
            // meta.getColumnTypeName(i));
        }

        content.append("\n");
        content.append("\t")
                .append("public static java.util.Map<String, String> getNamedMap() {")
                .append("\n");
        content.append("\t\t")
                .append("java.util.Map<String, String> map = new java.util.HashMap<String, String>();")
                .append("\n");
        Iterator<String> it0 = columnInfo.keySet().iterator();
        int i = 0;
        while (it0.hasNext()) {
            String oldName = oldColumnNames.get(i);
            String newName = it0.next();
            content.append("\t\t").append("map.put(\"").append(oldName)
                    .append("\", \"").append(newName).append("\");")
                    .append("\n");
            i++;
        }
        content.append("\t\t").append("return map;").append("\n");
        content.append("\t").append("}").append("\n");
        content.append("\n");

        content.append("\t").append("public String toString() {").append("\n");
        content.append("\t\t").append("return ");

        Iterator<String> it = columnInfo.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            content.append("\"").append(key).append("=\"").append(" + ")
                    .append(key).append(" + \"  \" + ");
        }

        content = new StringBuffer(content.substring(0, content.length() - 10));
        content.append(";").append("\n");
        content.append("\t").append("}").append("\n");
        content.append("\n");

        Iterator<String> it1 = columnInfo.keySet().iterator();
        while (it1.hasNext()) {
            String key = it1.next();
            String value = columnInfo.get(key);

            content.append("\t")
                    .append("public ")
                    .append(value)
                    .append(" ")
                    .append("get")
                    .append(key.substring(0, 1).toUpperCase()
                            + key.substring(1)).append("() {").append("\n");
            content.append("\t\t").append("return ").append(key).append(";")
                    .append("\n");
            content.append("\t").append("}").append("\n");

            content.append("\t")
                    .append("public void ")
                    .append("set")
                    .append(key.substring(0, 1).toUpperCase()
                            + key.substring(1)).append("(").append(value)
                    .append(" ").append(key).append(") {").append("\n");
            content.append("\t\t").append("this.").append(key).append(" = ")
                    .append(key).append(";").append("\n");
            content.append("\t").append("}").append("\n");
        }

        content.append("}");
        // System.out.println(content);

        try {
            System.out.println(file);
            java.io.FileOutputStream fos = new java.io.FileOutputStream(
                    new java.io.File(file));
            fos.write(content.toString().getBytes());
            fos.close();
            System.out.println("JAVA BEAN生成成功！请把上面的路径刷新一下就能看到了。");
        } catch (Exception e) {
            logger.error("convertDBToPO:", e);
        }
    }



	/**
     * 转义中文里可能包含的单引号
     * 
     * @param s
     * @return
     */
    public static String transferSingleQuote(String s) {
        if (s != null) {
            return s.replace("'", "''");
        }
        return s;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
