package helper;

import annotation.RequestMapping;
import bean.Handler;
import bean.Request;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: TangXiaoDong
 * @Date: 10:59 2021/5/21
 * @Description: 映射处理器
 *      定义了一个"请求-处理器" 的映射 REQUEST_MAP,
 *      REQUEST_MAP 就相当于Spring MVC里的映射处理器, 接收到请求后返回对应的处理器.
 *      利用ClassUtil获取带有Controller注解的所有类，遍历这些类的所有方法，将带有RequestMapping注解的方法存储到"映射处理器"
 *      ：requestMethod和requestPath 封装到请求对象 request
 */
public final class ControllerHelper {

    /**
     * 定义mvc映射处理器
     */
    private static final Map<Request, Handler> REQUEST_MAP = new HashMap<Request, Handler>();

    /**
     * 封装映射处理器
     */
    static {
        // 1、遍历所有Controller类
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if (CollectionUtils.isNotEmpty(controllerClassSet)) {
            for (Class<?> cls : controllerClassSet) {
                // 暴力反射
                Method[] controllerMethods = cls.getDeclaredMethods();
                if (ArrayUtils.isNotEmpty(controllerMethods)) {
                    // 遍历该类的所有方法
                    for (Method method : controllerMethods) {
                        // 判断该方法上是否带有RequestMapping注解
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                            String requestPath = requestMapping.value(); // 请求路径
                            String requestMethod = requestMapping.method().name(); // 请求方法

                            // 封装请求和处理器
                            Request request = new Request(requestMethod, requestPath);
                            Handler handler = new Handler(cls, method);
                            REQUEST_MAP.put(request, handler);
                        }
                    }
                }
            }
        }

    }


    /**
     * 获取Handler
     * @param requestMethod 请求方法名
     * @param requestPath 请求路径
     * @return
     */
    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return REQUEST_MAP.get(request);
    }


}
