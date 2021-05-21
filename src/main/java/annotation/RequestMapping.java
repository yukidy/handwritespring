package annotation;

import enums.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @Author: TangXiaoDong
 * @Date: 13:20 2021/5/15
 * @Description: RequestMapping(处理器方法)注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

    /**
     *
     * @return
     */
    String value() default "";

    RequestMethod method() default RequestMethod.GET;

}
