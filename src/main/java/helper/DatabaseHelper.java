package helper;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: TangXiaoDong
 * @Date: 16:28 2021/5/27
 * @Description: 数据库操作管理 helper
 *      该助手类用于增删改查，事务等一系列的数据库操作
 */
public final class DatabaseHelper {

    private static final Logger log = LoggerFactory.getLogger(DatabaseHelper.class);

    private static final ThreadLocal<Connection> CONNECTION_HOLDER;

    private static final QueryRunner QUERY_RUNNER;

    private static final BasicDataSource DATA_SOURCE;

    static {
        CONNECTION_HOLDER = new ThreadLocal<Connection>();
        QUERY_RUNNER = new QueryRunner();
        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(ConfigHelper.getJdbcDriver()); // 数据库驱动
        DATA_SOURCE.setUrl(ConfigHelper.getJdbcUrl()); // 数据库Url
        DATA_SOURCE.setUsername(ConfigHelper.getJdbcName());
        DATA_SOURCE.setPassword(ConfigHelper.getJdbcPassword());
    }


    /**
     * 获取数据源
     * @return
     */
    public static BasicDataSource getDataSource() {
        return DATA_SOURCE;
    }


    /**
     * 获取数据库连接
     * @return
     */
    public static Connection getConnection() {
        Connection connection = CONNECTION_HOLDER.get();
        if (connection == null) {
            try {
                connection = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                log.error("sql connection failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(connection);
            }
        }
        return connection;
    }


    /**
     * 开启事务
     */
    public static void beginTransaction() {
        Connection conn = CONNECTION_HOLDER.get();
        if (conn != null) {
            try {
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                log.error("begin transaction failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(conn);
            }
        }

    }

    /**
     * 提交事务
     */
    public static void commitTransaction() {
        Connection conn = CONNECTION_HOLDER.get();
        if (conn != null) {
            try {
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                log.error("commit transaction failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction() {
        Connection conn = CONNECTION_HOLDER.get();
        if (conn != null) {
            try {
                conn.rollback();
                conn.close();
            } catch (SQLException e) {
                log.error("rollback transaction failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }


    /**
     * 查询实体
     * @param entityClass 实体类
     * @param sql 查询语句
     * @param params 查询条件参数
     * @param <T> 第一个<T>表示泛型，第二个T表示返回的是T类型的数据，第三个T表示限制泛型，只能传入T类型的参数
     * @return
     */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T entity;
        try {
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
        } catch (SQLException e) {
            log.error("query entity failure", e);
            throw new RuntimeException(e);
        }
        return entity;
    }


    /**
     * 查询实体列表
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList;
        try {
            Connection conn = getConnection();
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            log.error("query entity list failure", e);
            throw new RuntimeException(e);
        }
        return entityList;
    }


    /**
     * 数据库操作，包括（update, delete, insert）
     * @param sql
     * @param params
     * @return
     */
    public static int update(String sql, Object... params) {
        int rows;
        try {
            Connection conn = getConnection();
            rows = QUERY_RUNNER.update(conn, sql, params);
        } catch (SQLException e) {
            log.error("executor update failure", e);
            throw new RuntimeException(e);
        }
        return rows;
    }


    /**
     * 插入实体
     * @param entityClass
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
        if (MapUtils.isEmpty(fieldMap)) {
            log.error("can not insert entity: field map is empty");
            return false;
        }

        String sql = "INSERT INTO " + entityClass.getSimpleName(); // 待优化点 TODO
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
        values.replace(values.lastIndexOf(", "), values.length(), ")");
        sql += columns + " VALUES " + values;

        Object[] params = fieldMap.values().toArray();
        return update(sql, params) == 1;
    }


    /**
     * 更新实体对象
     * @param entityClass
     * @param id
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap) {
        if (MapUtils.isEmpty(fieldMap)) {
            log.error("can not update entity: field map is empty");
            return false;
        }

        String sql = "UPDATE " + entityClass.getSimpleName() + " SET ";
        StringBuilder columns = new StringBuilder();
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(" = ?, ");
        }

        sql += columns.substring(0, columns.lastIndexOf(", ")) + " WHERE id = ?";

        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);

        Object[] params = paramList.toArray();
        return update(sql, params) == 1;
    }


    /**
     * 删除实体
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    public static <T> boolean deleteEntity(Class<T> entityClass, long id) {
        String sql = "DELETE FROM " + entityClass.getSimpleName() + " WHERE id = ?";
        return update(sql, id) == 1;
    }


}
