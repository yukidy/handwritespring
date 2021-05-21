package bean;

import java.lang.reflect.Method;

/**
 * @Author: TangXiaoDong
 * @Date: 10:52 2021/5/21
 * @Description: 处理器类-封装 Controller 的 Class对象 和 Method方法
 */
public class Handler {

    /**
     * Controller类
     */
    private Class<?> controllerClass;

    /**
     * Controller方法
     */
    private Method controllerMethod;


    public Handler(Class<?> controllerClass, Method controllerMethod) {
        this.controllerClass = controllerClass;
        this.controllerMethod = controllerMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getControllerMethod() {
        return controllerMethod;
    }
}
