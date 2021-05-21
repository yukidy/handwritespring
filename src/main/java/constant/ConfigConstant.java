package constant;

/**
 * @Author: TangXiaoDong
 * @Date: 11:17 2021/5/19
 * @Description: 配置基础常量
 */
public interface ConfigConstant {

    // 配置文件名
    String CONFIG_FILE = "handspring.properties";

    //数据源
    String JDBC_DRIVER = "handspring.framework.jdbc.driver";
    String JDBC_URL = "handspring.framework.jdbc.url";
    String JDBC_USERNAME = "handspring.framework.jdbc.username";
    String JDBC_PASSWORD = "handspring.framework.jdbc.password";

    //java源码地址
    String APP_BASE_PACKAGE = "handspring.framework.app.base_package";
    //jsp页面路径
    String APP_JSP_PATH = "handspring.framework.app.jsp_path";
    //静态资源路径
    String APP_ASSET_PATH = "handspring.framework.app.asset_path";

}
