package com.cn.jmw.data.provider.base.local;

import com.cn.jmw.data.provider.base.entity.PageInfo;
import com.cn.jmw.data.provider.base.entity.db.Column;
import com.cn.jmw.data.provider.base.entity.db.Dataframe;
import com.cn.jmw.data.provider.base.entity.db.Dataframes;
import com.cn.jmw.data.provider.base.entity.db.ExecutionParam;
import com.cn.jmw.data.provider.base.utils.DataTypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.h2.tools.DeleteDbFiles;
import org.h2.tools.SimpleResultSet;
import org.springframework.util.CollectionUtils;

import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月21日 16:29
 * @Version 1.0
 */
@Slf4j
public class LocalPersistentDB {

    private static final String MEM_URL = "jdbc:h2:mem:/";

    private static final String H2_PARAM = ";LOG=0;DATABASE_TO_UPPER=false;MODE=MySQL;CASE_INSENSITIVE_IDENTIFIERS=TRUE;CACHE_SIZE=65536;LOCK_MODE=0;UNDO_LOG=0";

    //创建临时表
    private static final String CREATE_TEMP_TABLE = "CREATE TABLE IF NOT EXISTS `%s` AS (SELECT * FROM FUNCTION_TABLE('%s'))";

    //临时结果集缓存
    private static final Map<String, Dataframe> TEMP_RS_CACHE = new ConcurrentHashMap<>();

    //设置过期SQL
    private static final String SET_EXPIRE_SQL = "INSERT INTO `cache_expire` VALUES( '%s', PARSEDATETIME('%s','%s')) ";

    //删除过期SQL
    private static final String DELETE_EXPIRE_SQL = "DELETE FROM `cache_expire` WHERE `source_id`='%s' ";

    private static final String CACHE_EXPIRE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS `cache_expire` ( `source_id` VARCHAR(128),`expire_time` DATETIME )";


    public static Dataframe executeLocalQuery(ExecutionParam executeParam, Dataframes dataframes) throws Exception {
        return executeLocalQuery(executeParam, dataframes, false,null);
    }

    public static void init() {
        try {
            Class.forName("org.h2.Driver");
            try (Connection connection = getConnection(true, null)) {
                Statement statement = connection.createStatement();
                statement.execute(CACHE_EXPIRE_TABLE_SQL);
            }
        } catch (Exception e) {
            log.error("H2 init error", e);
        }
    }

    public static Dataframe executeLocalQuery(ExecutionParam executeParam, Dataframes dataframes, boolean isPersistent,Date expire) throws Exception {
        String url = getConnectionUrl(isPersistent, dataframes.getKey());
        //字符串常量池中存在对应字面量，则intern()方法返回该字面量的地址；如果不存在，则创建一个对应的字面量，并返回该字面量的地址
        synchronized (url.intern()) {
            return isPersistent ? executeInLocalDB( executeParam, dataframes, expire) : executeInMemDB(executeParam, dataframes);
        }
    }

    /**
     * 持久化查询，将数据插入到H2表中，再进行查询
     */
    private static Dataframe executeInLocalDB(ExecutionParam executeParam, Dataframes dataframes, Date expire) throws Exception {
        try (Connection connection = getConnection(true, dataframes.getKey())) {
            if (!dataframes.isEmpty()) {

                for (Dataframe dataframe : dataframes.getDataframes()) {
                    //将数据量注册为表
                    registerDataAsTable(dataframe, connection);
                }

                if (expire != null) {
                    //设置缓存过期时间
                    setCacheExpire(dataframes.getKey(), expire);
                }

            }
            return execute(connection, executeParam,dataframes);
        }
    }


    /**
     * 检查数据源缓存是否过期。如果过期,删除缓存
     *
     * @param cacheKey source 唯一标识
     */
    public static boolean checkCacheExpired(String cacheKey) throws SQLException {
        try (Connection connection = getConnection(true, null)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `cache_expire` WHERE `source_id`='" + cacheKey + "'");
            if (resultSet.next()) {
                Timestamp cacheExpire = resultSet.getTimestamp("expire_time");
                if (cacheExpire.after(new java.util.Date())) {
                    return false;
                }
                clearCache(cacheKey);
            }
        }
        return true;
    }

    /**
     * 非持久化查询，通过函数表注册数据为临时表，执行一次后丢弃表数据。
     */
    private static Dataframe executeInMemDB( ExecutionParam executeParam, Dataframes dataframes) throws Exception {
        Connection connection = getConnection(false, dataframes.getKey());
        try {
            for (Dataframe dataframe : dataframes.getDataframes()) {
                registerDataAsTable(dataframe, connection);
            }
            //注册表持久化
            return execute(connection, executeParam,dataframes);
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                log.error("connection close error ", e);
            }
            for (Dataframe df : dataframes.getDataframes()) {
                //内存数据注销
                unregisterData(df.getId());
            }
        }

    }

    private static Dataframe execute(Connection connection, ExecutionParam executeParam,Dataframes dataframes) throws Exception {
        //TODO 语言解析器 没有拼接出对应SQL
        String sql = buildSql(executeParam,dataframes);
        log.debug(sql);

        ResultSet resultSet = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);
        PageInfo pageInfo = executeParam.getPageInfo();
        resultSet.last();
        pageInfo.setTotal(resultSet.getRow());
        resultSet.first();

        resultSet.absolute((int) Math.min(pageInfo.getTotal(), (pageInfo.getPageNo() - 1) * pageInfo.getPageSize()));
        Dataframe dataframe = ResultSetMapper.mapToTableData(resultSet, pageInfo.getPageSize());
        dataframe.setPageInfo(pageInfo);
        dataframe.setScript(sql);
        return dataframe;

    }

    private static String buildSql(ExecutionParam executeParam,Dataframes dataframes) {
        String sql = "SELECT *  FROM  ( SELECT * FROM `"+ dataframes.getDataframes().get(0).getName() +"` )  AS `DATART_VTABLE`";
        return sql;
    }

    /**
     * 把数据注册注册为临时表，用于SQL查询
     *
     * @param dataframe 二维表数据
     */
    private static void registerDataAsTable(Dataframe dataframe, Connection connection) throws SQLException {
        if (Objects.isNull(dataframe)) {
            log.error("Empty data cannot be registered as a temporary table");
        }

        // 处理脏数据
        dataframe.getRows().parallelStream().forEach(row -> {
            for (int i = 0; i < row.size(); i++) {
                Object val = row.get(i);
                if (val instanceof String && StringUtils.isBlank(val.toString())) {
                    row.set(i, null);
                }
            }
        });

        createFunctionTableIfNotExists(connection);

        TEMP_RS_CACHE.put(dataframe.getId(), dataframe);
        // register temporary table
        String sql = String.format(CREATE_TEMP_TABLE, dataframe.getName(), dataframe.getId());
        try {
            System.out.println("-----"+sql);
            connection.prepareStatement(sql).execute();
        } catch (Exception e) {
            //忽略重复创建表导致的异常
        }
    }

    /**
     * @Description 设置过期时间
     * 
     * 
     * 
     * @Author jmw
     * @Date 17:09 2022/10/21
     */
    private static void setCacheExpire(String sourceId, java.util.Date date) throws SQLException {
        try (Connection connection = getConnection(true, null)) {
            Statement statement = connection.createStatement();
            // delete first
            statement.execute(String.format(DELETE_EXPIRE_SQL, statement));
            // insert expire
            String sql = String.format(SET_EXPIRE_SQL, sourceId, DateFormatUtils.format(date, Const.DEFAULT_DATE_FORMAT), Const.DEFAULT_DATE_FORMAT);
            statement.execute(sql);
        }
    }


    private static void createFunctionTableIfNotExists(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("CREATE ALIAS FUNCTION_TABLE  FOR \"com.cn.jmw.data.provider.base.local.LocalPersistentDB.dataframeTable\"");
        } catch (SQLException ignored) {
        }
    }

    /**
     * 函数表对应函数，直接从Dataframe 返回一个 ResultSet.
     *
     * @param conn   ResultSet 对应连接
     * @param dataId ResultSet 对应 Dataframe
     */
    public static ResultSet dataframeTable(Connection conn, String dataId) throws SQLException {
        Dataframe dataframe = TEMP_RS_CACHE.get(dataId);
        if (dataframe == null) {
            log.error("The dataframe " + dataId + " does not exist");
        }
        SimpleResultSet rs = new SimpleResultSet();
        if (!CollectionUtils.isEmpty(dataframe.getColumns())) {
            // add columns
            for (Column column : dataframe.getColumns()) {
                rs.addColumn(column.columnName(), DataTypeUtils.valueType2SqlTypes(column.getType()), -1, -1);
            }
        }
        if (conn.getMetaData().getURL().equals("jdbc:columnlist:connection")) {
            return rs;
        }
        // add rows
        if (!CollectionUtils.isEmpty(dataframe.getRows())) {
            for (List<Object> row : dataframe.getRows()) {
                rs.addRow(row.toArray());
            }
        }
        return rs;
    }

    public static void clearCache(String cacheKey) throws SQLException {
        try (Connection connection = getConnection(true, null)) {
            connection.createStatement().execute(String.format(DELETE_EXPIRE_SQL, cacheKey));
            DeleteDbFiles.execute(getDbFileBasePath(), cacheKey, false);
        }
    }


    private static void unregisterData(String dataId) {
        TEMP_RS_CACHE.remove(dataId);
    }


    /**
     * @Description Get localDB connecntion
     *
     *
     *
     * @Author jmw
     * @Date 16:52 2022/10/21
     */
    private static Connection getConnection(boolean persistent, String database) throws SQLException {
        return DriverManager.getConnection(getConnectionUrl(persistent, database));
    }

    private static String getConnectionUrl(boolean isPersistent, String database) {
        return isPersistent ? getDatabaseUrl(database) : MEM_URL + "DB" + database + H2_PARAM;
    }

    private static String getDatabaseUrl(String database) {
        if (database==null)database = "datart_meta";
        return String.format("jdbc:h2:file:%s/%s" + H2_PARAM, getDbFileBasePath(), database);
    }

    private static String getDbFileBasePath() {
//        return "C:\\Users\\jmw\\Desktop\\test\\datart\\files\\h2\\dbs";
        log.info("本地持久化数据库："+System.getProperty("user.dir") + "files/h2/dbs");
        return System.getProperty("user.dir") + "files/h2/dbs";
    }

}