package helper;

import bean.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author: TangXiaoDong
 * @Date: 15:41 2021/5/21
 * @Description: 请求helper
 *      前端控制器接收到Http请求，从Http获取请求参数，封装
 */
public class RequestHelper {

    /**
     * 获取请求参数
     * @param request
     * @return
     */
    public static Param createParam(HttpServletRequest request) {
        // 获取请求参数名
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Enumeration<String> paramNames = request.getParameterNames();
        // 没有参数
        if (!paramNames.hasMoreElements()) {
            return null;
        }

        // 遍历请求参数
        while (paramNames.hasMoreElements()) {
            String fieldName = paramNames.nextElement();
            String fieldValue = request.getParameter(fieldName);
            paramMap.put(fieldName, fieldValue);
        }

        return new Param(paramMap);
    }

}
