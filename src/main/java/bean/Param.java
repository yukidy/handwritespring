package bean;

import org.apache.commons.collections4.MapUtils;

import java.util.Map;

/**
 * @Author: TangXiaoDong
 * @Date: 11:56 2021/5/21
 * @Description: 用于封装Controller方法的参数
 */
public class Param {

    private Map<String, Object> paramMap;

    public Param() {
    }

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    /**
     * 判断参数集合是否为空
     */
    public boolean isEmpty() {
        return MapUtils.isEmpty(paramMap);
    }
}
