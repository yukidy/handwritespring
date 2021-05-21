package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Author: TangXiaoDong
 * @Date: 10:42 2021/5/20
 * @Description: 构建Bean容器工具类
 *      通过反射实现Bean容器
 */
public final class ReflectionUtil {

    private static final Logger log = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 创建新实例
     * @param cls
     * @return
     */
    public static Object newInstance(Class<?> cls) {
        Object instance;
        try {
            instance = cls.newInstance();
        } catch (Exception e) {
            log.error("new instance failure", e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 根据类名创建新实例
     * @param className
     * @return
     */
    public static Object newInstance(String className) {
        Class<?> cls = ClassUtil.loadClass(className);
        return newInstance(cls);
    }

    /**
     * 调用方法
     */
    public static Object invokeMethod(Object obj, Method method, Object ...args) {
        Object result;
        try {
            method.setAccessible(true);
            result = method.invoke(obj, args);
        } catch (Exception e) {
            log.error("fail to load method", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 设置成员变量值
     */
    public static void setField(Object obj, Field field, Object ...args) {
        try {
            field.setAccessible(true);
            field.set(obj, args);
        } catch (Exception e) {
            log.error("fail to set field", e);
            throw new RuntimeException(e);
        }

    }

}
