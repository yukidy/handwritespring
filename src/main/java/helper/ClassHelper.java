package helper;

import annotation.Controller;
import annotation.Service;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import utils.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: TangXiaoDong
 * @Date: 9:20 2021/5/20
 * @Description: class类helper
 *      在class助手类自身被加载时，获取应用基础包名，通过ClassUtil将基础包中的类加载到Class Set集合中
 */
public final class ClassHelper {

    /**
     * 应用基础包类的集合
     */
    private static final Set<Class<?>> CLASS_SET;

    static {
        // 获取基础包名
        String basePackage = ConfigHelper.getAppBasePack();
        // 获取基础包名下的所有类 -> Set
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }


    /**
     * 获取基础应用包下的所有类
     * @return
     */
    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }


    /**
     * 获取基础应用包下的所有Service类
     * @return
     */
    public static Set<Class<?>> getServiceClassSet() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET) {
            if (cls.isAnnotationPresent(Service.class)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }


    /**
     * 获取基础应用包下的所有Controller类
     * @return
     */
    public static Set<Class<?>> getControllerClassSet() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET) {
            if (cls.isAnnotationPresent(Controller.class)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }


    /**
     * 获取基础应用包下的所有类bean类 （包括：Controller类和Service类）
     * @return
     */
    public static Set<Class<?>> getBeanClassSet() {
        Set<Class<?>> beanClassSet = new HashSet<Class<?>>();
        beanClassSet.addAll(getControllerClassSet());
        beanClassSet.addAll(getServiceClassSet());
        return beanClassSet;
    }


    /**
     * 获取基础包下某父类的所有子类，或某接口的所有实现类
     * @param superClass 父类/接口
     * @return
     */
    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET) {
            // isAssignableFrom：检查cls是否是superClass的子类或接口实现类，或是否相同
            if (superClass.isAssignableFrom(cls) && !superClass.equals(cls)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }


    /**
     * 获取基础包名下带有某注解的所有类
     * @param annotationClass
     * @return
     */
    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET) {
            if (cls.isAnnotationPresent(annotationClass)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }

}
