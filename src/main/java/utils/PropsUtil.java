package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author: TangXiaoDong
 * @Date: 11:23 2021/5/19
 * @Description: 读取属性文件工具类
 */
public final class PropsUtil {

    private static final Logger log = LoggerFactory.getLogger(PropsUtil.class);

    /**
     * 加载属性文件
     * @param fileName 文件名
     * @return
     */
    public static Properties loadProps (String fileName) {
        Properties props = null;
        InputStream is = null;
        try {
            is = ClassUtil.getClassLoader().getResourceAsStream(fileName);
            if (is == null) {
                throw new FileNotFoundException(fileName + " file is not found!!");
            }
            props = new Properties();
            props.load(is);
        } catch (IOException e) {
            log.error("load properties file failure", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("close input stream failure", e);
                }
            }
        }

        return props;
    }


    /**
     * 获取String类型的属性值
     * @param properties
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(Properties properties, String key, String defaultValue) {
        String value = defaultValue;
        if (properties.containsKey(key)) {
            value = properties.getProperty(key);
        }
        return value;
    }

    /**
     * 获取String类型的属性值（默认值为空字符串）
     * @param properties
     * @param key
     * @return
     */
    public static String getString(Properties properties, String key) {
        return getString(properties, key, "");
    }


    /**
     * 获取int类型的属性值
     * @param properties
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Properties properties, String key, int defaultValue) {
        int value = defaultValue;
        if (properties.containsKey(key)) {
            value = Integer.parseInt(properties.getProperty(key));
        }
        return value;
    }

    /**
     * 获取int类型的属性值（默认值为0）
     * @param properties
     * @param key
     * @return
     */
    public static int getInt(Properties properties, String key) {
        return getInt(properties, key, 0);
    }


    /**
     * 获取boolean类型的属性值（默认值是false）
     * @param properties
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(Properties properties, String key, boolean defaultValue) {
        boolean value = defaultValue;
        if (properties.containsKey(key)) {
            value = Boolean.parseBoolean(properties.getProperty(key));
        }
        return value;
    }

    /**
     * 获取boolean类型的属性值（默认值是false）
     * @param properties
     * @param key
     * @return
     */
    public static boolean getBoolean(Properties properties, String key) {
        return getBoolean(properties, key, false);
    }

}
