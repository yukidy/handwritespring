package helper;

import annotation.Aspect;
import annotation.Service;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proxy.AspectProxy;
import proxy.Proxy;
import proxy.ProxyFactory;
import proxy.TransactionProxy;
import utils.ClassUtil;

import java.util.*;

/**
 * @Author: TangXiaoDong
 * @Date: 11:48 2021/5/26
 * @Description: aop助手类
 *      用来初始化整个AOP框架，框架中的所有bean对象都是由bean实例获取的，然后再执行该实例方法
 *          初始化整个AOP框架实际上就是用代理对象覆盖掉Bean容器中的目标对象，然后通过目标类的Class对象获取到Bean容器的对象就是代理对象
 *
 *      事务代理和普通代理的区别：
 *          事务代理默认所有带有@Service注解的对象都被代理了，通过Service的Class对象获取bean容器中的所有代理对象，
 *          再执行代理对象时判断是否有@Transactional注解，有则加上事务管理，没有则直接执行
 */
public final class AopHelper {

    private static final Logger log = LoggerFactory.getLogger(AopHelper.class);

    static {
        try {
            // 切面类-目标类集合的映射
            Map<Class<?>, Set<Class<?>>> aspectMap = createAspectMap();
            // 目标类。切面对象列表的映射
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(aspectMap);
            // 把切面对象织入到目标类中，创建代理对象
            for (Map.Entry<Class<?>, List<Proxy>> aspectEntry : targetMap.entrySet()) {
                Class<?> targetClass = aspectEntry.getKey();
                List<Proxy> proxyList = aspectEntry.getValue();
                Object proxy = ProxyFactory.createProxy(targetClass, proxyList);
                // 覆盖bean容器目标类对应的实例，下次从bean容器里获取的就是代理对象了
                BeanHelper.setBean(targetClass, proxy);
            }
        } catch (Exception e) {
            log.error("aop failure", e);
        }
    }


    /**
     * 将切面类-目标类集合的映射关系 -> 目标类-切面对象列表的映射关系
     * @param aspectMap
     * @return
     */
    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> aspectMap) throws Exception {
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
        for (Map.Entry<Class<?>, Set<Class<?>>> aspectEntry : aspectMap.entrySet()) {
            // 切面类
            Class<?> proxyClass = aspectEntry.getKey();
            // 目标类集合
            Set<Class<?>> targetClassSet = aspectEntry.getValue();
            // 创建目标类-切面对象列表的映射关系
            for (Class<?> targetClass : targetClassSet) {
                Proxy aspect = (Proxy) proxyClass.newInstance();
                if (targetMap.containsKey(proxyClass)) {
                    targetMap.get(targetClass).add(aspect);
                } else {
                    // 切面类对象列表
                    List<Proxy> proxyList = new LinkedList<Proxy>();
                    proxyList.add(aspect);
                    targetMap.put(targetClass, proxyList);
                }
            }

        }
        return targetMap;
    }


    /**
     * 获取切面类-目标类集合的映射
     * @return
     * @throws Exception
     */
    private static Map<Class<?>, Set<Class<?>>> createAspectMap() throws Exception {
        Map<Class<?>, Set<Class<?>>> aspectMap = new HashMap<Class<?>, Set<Class<?>>>();
        addAspectProxy(aspectMap);
        addTransactionProxy(aspectMap);
        return aspectMap;
    }

    /**
     * 获取事务切面类-目标类集合的映射
     * @param aspectMap
     */
    private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> aspectMap) {
        Set<Class<?>> serviceClassSet = ClassHelper.getClassSetByAnnotation(Service.class);
        aspectMap.put(TransactionProxy.class, serviceClassSet);
    }


    /**
     * 获取普通切面类-目标类集合的映射
     * @param aspectMap
     */
    private static void addAspectProxy(Map<Class<?>, Set<Class<?>>> aspectMap) throws Exception {
        // 所有实现了AspectProxy抽象类的切面
        Set<Class<?>> aspectClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
        if (CollectionUtils.isNotEmpty(aspectClassSet)) {
            for (Class<?> aspectClass : aspectClassSet) {
                if (aspectClass.isAnnotationPresent(Aspect.class)) {
                    Aspect aspect = aspectClass.getAnnotation(Aspect.class);
                    // 与该切面对应的目标类集合
                    Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                    aspectMap.put(aspectClass, targetClassSet);
                }
            }
        }
    }

    /**
     * 根据@Aspect
     * @param aspect
     * @return
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception {
        Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
        // 包名
        String pkg = aspect.pkg();
        // 类名
        String cls = aspect.cls();
        // 如果包名与类名均不为空，则添加指定类
        if (!pkg.equals("") && !cls.equals("")) {
            targetClassSet.add(Class.forName(pkg + "." + cls));
        }
        // 如果包名不为空，类名为空，则添加该包名下的所有类
        else if (!pkg.equals("") && cls.equals("")) {
            targetClassSet.addAll(ClassUtil.getClassSet(pkg));
        }
        return targetClassSet;
    }


}
