package proxy;

/**
 * @Author: TangXiaoDong
 * @Date: 15:41 2021/5/25
 * @Description: aop顶层代理接口
 *      doProxy()执行链式代理
 */
public interface Proxy {

    /**
     * 执行链式代理
     *      可将多个代理通过一个链子串联起来，一个个去执行，执行顺序取决于添加到链上的先后顺序
     * @param proxyChain
     * @return
     * @throws Throwable
     */
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
