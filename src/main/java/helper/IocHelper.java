package helper;

import annotation.Autowired;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import utils.ClassUtil;
import utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @Author: TangXiaoDong
 * @Date: 11:22 2021/5/20
 * @Description: ioc helper
 *      实现spring ioc 功能
 */
public final class IocHelper {

    /**
     * 遍历bean容器所有的bean的属性，将带有@Autowired注解的属性注入到bean容器
     */
    static {
        // 遍历bean容器中的所有bean
        HashMap<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (MapUtils.isNotEmpty(beanMap)) {
            for (Map.Entry<Class<?>, Object> entrySet : beanMap.entrySet()) {
                // bean的Class类
                Class<?> cls = entrySet.getKey();
                // bean的实例
                Object obj = entrySet.getValue();
                // 反射获取属性
                Field[] beanFields = cls.getDeclaredFields();
                if (ArrayUtils.isNotEmpty(beanFields)) {
                    // 遍历bean属性
                    for (Field field : beanFields) {
                        // 判断属性是否带有@Autowired
                        if (field.isAnnotationPresent(Autowired.class)) {
                            // 属性类型
                            Class<?> beanFieldsClass = field.getType();
                            // 如果beanFieldsClass是接口，就获取接口对应的实现类
                            beanFieldsClass = findImplementClass(beanFieldsClass);
                            // 获取Class类对应的实例
                            Object beanFieldInstance = beanMap.get(beanFieldsClass);
                            if (beanFieldInstance != null) {
                                ReflectionUtil.setField(obj, field, beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取接口对应的实现类
     * @param beanFieldsClass
     * @return
     */
    private static Class<?> findImplementClass(Class<?> beanFieldsClass) {
        Class<?> implementClass = beanFieldsClass;
        // 接口对应的所有实现类
        Set<Class<?>> classSetBySuper = ClassHelper.getClassSetBySuper(implementClass);
        if (CollectionUtils.isNotEmpty(classSetBySuper)) {
            // 获取第一个实现类
            implementClass = classSetBySuper.iterator().next();
        }
        return implementClass;
    }


}
