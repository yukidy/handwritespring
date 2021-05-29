package proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author: TangXiaoDong
 * @Date: 10:45 2021/5/26
 * @Description: 代理工厂类
 *
 */
public class ProxyFactory {

    /**
     * 输入一个目标类和一组Proxy代理，输出一个代理对象
     * @param targetClass
     * @param proxyList
     * @param <T>
     * @return
     */
    public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyList){
        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            /**
             * 代理方法
             *      每次调用目标方法时会先创建一个ProxyChain对象
             */
            @Override
            public Object intercept(Object targetObject, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                return new ProxyChain(targetClass, targetObject, method, methodProxy, objects, proxyList).doChainProxy();
            }
        });
    }

}
