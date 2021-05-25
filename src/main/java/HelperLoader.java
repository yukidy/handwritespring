import annotation.Controller;
import helper.BeanHelper;
import helper.ClassHelper;
import helper.ControllerHelper;
import helper.IocHelper;
import utils.ClassUtil;

/**
 * @Author: TangXiaoDong
 * @Date: 16:38 2021/5/21
 * @Description:
 *      对创建的helper工具类，在入口程序进行集中加载（不添加也会加载），集中加载helper的静态代码块
 */
public class HelperLoader {

    /**
     * 集中初始化ioc helper
     */
    public static void init() {
        Class<?> [] classArray = {
                ClassHelper.class,
                BeanHelper.class,
                ControllerHelper.class,
                IocHelper.class
        };

        for (Class<?> cls : classArray) {
            ClassUtil.loadClass(cls.getName());
        }
    }

}
