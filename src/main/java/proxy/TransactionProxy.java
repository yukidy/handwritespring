package proxy;

import annotation.Transactional;
import helper.DatabaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @Author: TangXiaoDong
 * @Date: 18:21 2021/5/26
 * @Description: 事务切面类
 *      实现了Proxy接口，doProxy方法就是判断代理方法上有没有@Transactional注解，如果有则加入事务管理，没有则直接执行
 */
public class TransactionProxy implements Proxy {

    private static final Logger log = LoggerFactory.getLogger(TransactionProxy.class);

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        Method method = proxyChain.getTargetMethod();
        // 判断目标方法是否包含@Transactional注解
        if (method.isAnnotationPresent(Transactional.class)) {
            try {
                DatabaseHelper.beginTransaction();
                log.debug("begin transaction");
                result = proxyChain.doChainProxy();
                log.debug("commit transaction");
                DatabaseHelper.commitTransaction();
            } catch (Exception e) {
                log.error("do proxy error, rollback transaction", e);
                DatabaseHelper.rollbackTransaction();
                throw new RuntimeException(e);
            }
        } else {
            result = proxyChain.doChainProxy();
        }
        return result;
    }
}
