package helper;

import constant.ConfigConstant;
import utils.PropsUtil;

import java.util.Properties;

import static utils.PropsUtil.loadProps;

/**
 * @Author: TangXiaoDong
 * @Date: 18:15 2021/5/19
 * @Description: 配置helper
 *     借助PropsUtil工具类, 实现自定义配置文件加载，用于加载用户自定义配置文件
 */
public final class ConfigHelper {

    private static final Properties CONFIG_PROPS = loadProps(ConfigConstant.CONFIG_FILE);

    /**
     * 获取JDBC驱动
     * @return
     */
    public static String gfinaletJdbcDriver() {
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_DRIVER);
    }


    /**
     * 获取JDBC url
     * @return
     */
    public static String getJdbcUrl() {
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_URL);
    }

    /**
     * 获取JDBC 连接用户名
     * @return
     */
    public static String getJdbcName() {
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_USERNAME);
    }


    /**
     * 获取JDBC 链接密码
     * @return
     */
    public static String getJdbcPassword() {
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_PASSWORD);
    }

    /**
     * 获取JSP 资源路径
     * @return
     */
    public static String getJspPath() {
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.APP_JSP_PATH);
    }

    /**
     * 获取应用基础包名
     * @return
     */
    public static String getAppBasePack() {
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.APP_BASE_PACKAGE);
    }

    /**
     * 获取应用静态资源路径
     * @return
     */
    public static String getAppAssetPath() {
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.APP_ASSET_PATH);
    }

    /**
     * 获取string类型的属性值
     * @return
     */
    public static String getString(String key) {
        return PropsUtil.getString(CONFIG_PROPS, key);
    }

    /**
     * 获取int类型的属性值
     * @return
     */
    public static int getInt(String key) {
        return PropsUtil.getInt(CONFIG_PROPS, key);
    }

    /**
     * 获取boolean类型的属性值
     * @return
     */
    public static boolean getBoolean(String key) {
        return PropsUtil.getBoolean(CONFIG_PROPS, key);
    }

}
