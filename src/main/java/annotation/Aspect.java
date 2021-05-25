package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: TangXiaoDong
 * @Date: 11:51 2021/5/25
 * @Description: 切面注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    /**
     * 包名
     */
    String pkg() default "";

    /**
     * 类名
     * @return
     */
    String cls() default "";

}
