package proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @Author: TangXiaoDong
 * @Date: 17:25 2021/5/25
 * @Description: 切面抽象类
 *      实现Proxy接口, 类中定义了各种切入点判断和各种增强，当执行doProxy()方法时
 */
public abstract class AspectProxy implements Proxy {

    private static final Logger log = LoggerFactory.getLogger(AspectProxy.class);

    @Override
    public final Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;
        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] methodParams = proxyChain.getMethodParams();

        // 开始增强
        begin();
        try {
            if (intercept(method, methodParams)) { // 切入点判断
                // 前置增强
                before(method, methodParams);
                result = proxyChain.doChainProxy();
                after(method, methodParams);
            } else {
                result = proxyChain.doChainProxy();
            }
        } catch (Exception e) {
            log.error("proxy failure", e);
            error(method, methodParams, e);
            throw e;
        } finally {
            end();
        }
        return result;
    }

    /**
     * 开始增强
     */
    public void begin() {
    }

    /**
     * 切入点判断
     * @param method
     * @param params
     * @return
     * @throws Throwable
     */
    public boolean intercept(Method method, Object[] params) throws Throwable {
        return true;
    }

    /**
     * 前置增强
     * @param method
     * @param params
     * @throws Throwable
     */
    public void before(Method method, Object[] params) throws Throwable {
    }


    /**
     * 后置增强
     * @param method
     * @param params
     * @throws Throwable
     */
    public void after(Method method, Object[] params) throws Throwable {
    }


    /**
     * 异常增强
     * @param method
     * @param params
     * @throws Throwable
     */
    public void error(Method method, Object[] params, Throwable e) throws Throwable {
    }

    /**
     * 最终增强
     */
    public void end() {
    }

}
