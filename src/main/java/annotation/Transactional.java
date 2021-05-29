package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: TangXiaoDong
 * @Date: 15:35 2021/5/25
 * @Description: 事务注解
 *      实现思路：在目标方法上添加@Transactional注解后，该方法就拥有了事务管理
 *      spring事务的实现是通过spring aop，前置增强为开始事务，后置增强是提交事务，异常增强是事务回滚
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {
}
