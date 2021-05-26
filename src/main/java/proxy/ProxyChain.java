package proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: TangXiaoDong
 * @Date: 15:44 2021/5/25
 * @Description: 代理链类
 *      proxyList存储的是代理列表（即增强列表），当执行doProxyChain()方法时，会按照顺序执行增强，最后再执行目标方法
 */
public class ProxyChain {

    private final Class<?> targetClass; // 目标类
    private final Object targetObject; // 目标对象
    private final Method targetMethod; // 目标方法
    private final MethodProxy methodProxy; // 方法代理
    private final Object[] methodParams; // 方法参数
    private List<Proxy> proxyList = new ArrayList<Proxy>(); // 代理列表
    private int proxyIndex = 0; // 代理索引

    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxyList = proxyList;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    /**
     * 递归执行
     * @return
     * @throws Throwable
     */
    public Object doChainProxy() throws Throwable {
        Object methodResult;
        if (proxyIndex < proxyList.size()) {
            // 执行增强方法
            methodResult = proxyList.get(proxyIndex++).doProxy(this);
        } else {
            // 目标方法最后执行且只执行一次
            methodResult = methodProxy.invokeSuper(targetObject, methodParams);
        }
        return methodResult;
    }

}
