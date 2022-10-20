package com.cn.jmw.data.provider.jdbc.adapter;

import com.cn.jmw.data.provider.base.entity.JdbcDriverInfo;
import com.cn.jmw.data.provider.base.entity.JdbcProperties;
import com.cn.jmw.data.provider.base.entity.PageInfo;
import com.cn.jmw.data.provider.base.entity.common.ValueType;
import com.cn.jmw.data.provider.base.entity.db.Column;
import com.cn.jmw.data.provider.base.entity.db.Dataframe;
import com.cn.jmw.data.provider.base.entity.db.ExecutionParam;
import com.cn.jmw.data.provider.base.utils.DataTypeUtils;
import com.cn.jmw.data.provider.base.utils.EntityValidatorUtil;
import com.cn.jmw.data.provider.jdbc.JdbcProvider;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月08日 18:25
 * @Version 1.0
 */
@Slf4j
public class JdbcDataProviderAdapter implements Closeable {

    private JdbcProperties jdbcProperties;
    private JdbcDriverInfo driverInfo;
    private DataSource dataSource;
    protected boolean init;

    /**
     * @Author jmw
     * @Description 连接池只初始化一次
     * @Date 12:18 2022/10/13
     */
    public final void init(JdbcProperties jdbcProperties, JdbcDriverInfo driverInfo) {
        try {
            this.jdbcProperties = jdbcProperties;
            this.driverInfo = driverInfo;
            this.dataSource = JdbcProvider.getDataSourceConnectionPool().createDataSource(jdbcProperties);
        } catch (Exception e) {
            log.error("data provider init error", e);
        }
        this.init = true;
    }

    /**
     * @Author jmw
     * @Description 销毁
     * @Date 18:29 2022/10/8
     */
    @Override
    public void close() throws IOException {
        System.out.println("shutdown");
    }

    public boolean test(JdbcProperties jdbcProperties){
        //数据验证 注释的约束进行验证（NotBlank）
        EntityValidatorUtil.validate(jdbcProperties);
        try{
            Class.forName(jdbcProperties.getDriverClass());
        } catch (ClassNotFoundException e) {
            String errMsg = "Driver class not found " + jdbcProperties.getDriverClass();
            log.error(errMsg, e);
        }
        try {
            //jdk原生方法 进行链接数据库测试，不报错为链接成功
            DriverManager.getConnection(jdbcProperties.getUrl(), jdbcProperties.getUser(), jdbcProperties.getPassword());
        } catch (SQLException sqlException) {
            log.error(sqlException.getSQLState()+"\n"+sqlException.getMessage());
        }
        return true;
    }

    public Dataframe executionOnSource(ExecutionParam executionParam) throws SQLException {
        Dataframe dataframe;
        String sql = executionParam.getSql();

        dataframe = execute(sql);
        return dataframe;
    }

    /**
     * 用于未支持SQL分页的数据库，使用通用的分页方案进行分页。
     *
     * @param selectSql 提交至数据源执行的SQL
     * @param pageInfo  需要执行的分页信息
     * @return 分页后的数据
     * @throws SQLException SQL执行异常
     */
    protected Dataframe execute(String selectSql, PageInfo pageInfo) throws SQLException {
        Dataframe dataframe;
        try (Connection conn = getConn()) {
            try (Statement statement = conn.createStatement()) {
                statement.setFetchSize((int) Math.min(pageInfo.getPageSize(), 10_000));
                try (ResultSet resultSet = statement.executeQuery(selectSql)) {
                    try {
                        resultSet.absolute((int) Math.min(pageInfo.getTotal(), (pageInfo.getPageNo() - 1) * pageInfo.getPageSize()));
                    } catch (Exception e) {
                        int count = 0;
                        while (count < (pageInfo.getPageNo() - 1) * pageInfo.getPageSize() && resultSet.next()) {
                            count++;
                        }
                    }
                    dataframe = parseResultSet(resultSet, pageInfo.getPageSize());
                    return dataframe;
                }
            }
        }
    }

    /**
     * 直接执行，返回所有数据，用于支持已经支持分页的数据库，或者不需要分页的查询。
     *
     * @param sql 直接提交至数据源执行的SQL，通常已经包含了分页
     * @return 全量数据
     * @throws SQLException SQL执行异常
     */
    protected Dataframe execute(String sql) throws SQLException {
        try (Connection conn = getConn()) {
            try (Statement statement = conn.createStatement()) {
                try (ResultSet rs = statement.executeQuery(sql)) {
                    return parseResultSet(rs);
                }
            }
        }
    }

    protected Connection getConn() throws SQLException {
        return dataSource.getConnection();
    }

    protected Dataframe parseResultSet(ResultSet rs) throws SQLException {
        return parseResultSet(rs, Long.MAX_VALUE);
    }

    protected Dataframe parseResultSet(ResultSet rs, long count) throws SQLException {
        Dataframe dataframe = new Dataframe();
        List<Column> columns = getColumns(rs);
        ArrayList<List<Object>> rows = new ArrayList<>();
        int c = 0;
        while (rs.next()) {
            ArrayList<Object> row = new ArrayList<>();
            rows.add(row);
            for (int i = 1; i < columns.size() + 1; i++) {
                row.add(getObjFromResultSet(rs, i));
            }
            c++;
            if (c >= count) {
                break;
            }
        }
        dataframe.setColumns(columns);
        dataframe.setRows(rows);
        return dataframe;
    }

    protected List<Column> getColumns(ResultSet rs) throws SQLException {
        ArrayList<Column> columns = new ArrayList<>();
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            String columnTypeName = rs.getMetaData().getColumnTypeName(i);
            String columnName = rs.getMetaData().getColumnLabel(i);
            ValueType valueType = DataTypeUtils.sqlType2DataType(columnTypeName);
            columns.add(Column.of(valueType, columnName));
        }
        return columns;
    }

    protected Object getObjFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        Object obj = rs.getObject(columnIndex);
        if (obj instanceof Boolean) {
            obj = rs.getObject(columnIndex).toString();
        } else if (obj instanceof LocalDateTime) {
            obj = rs.getTimestamp(columnIndex);
        }
        return obj;
    }

}
