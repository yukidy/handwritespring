package helper;

import utils.ReflectionUtil;

import java.util.HashMap;
import java.util.Set;

/**
 * @Author: TangXiaoDong
 * @Date: 11:03 2021/5/20
 * @Description: bean容器helper
 */
public final class BeanHelper {

    // HashMap用来存储基础包下所有的Class实例，相当于spring的bean容器
    private static final HashMap<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();

    static {
        // 获取包下的所有bean类
        Set<Class<?>> CLASS_SET = ClassHelper.getBeanClassSet();
        // 将所有bean类生成实例存储进HashMap容器
        for (Class<?> cls : CLASS_SET) {
            Object obj = ReflectionUtil.newInstance(cls);
            BEAN_MAP.put(cls, obj);
        }
    }

    /**
     * 获取bean容器
     * @return
     */
    public static HashMap<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    /**
     * 获取bean实例
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> cls) {
        if (BEAN_MAP.containsKey(cls)) {
            throw new RuntimeException("can not get bean by class :" + cls);
        }
        return (T) BEAN_MAP.get(cls);
    }


    /**
     * 设置bean实例
     * @param cls
     * @param obj
     */
    public static void setBean(Class<?> cls, Object obj) {
        BEAN_MAP.put(cls, obj);
    }


}
