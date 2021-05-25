package bean;

import java.util.Map;

/**
 * @Author: TangXiaoDong
 * @Date: 15:07 2021/5/21
 * @Description: 用于封装Controller方法的视图返回结果
 */
public class View {

    /**
     * 视图路径
     */
    private String path;

    /**
     * 数据模型
     */
    private Map<String, Object> model;


    public View(String path, Map<String, Object> model) {
        this.path = path;
        this.model = model;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    public View addModel(String key, Object value) {
        model.put(key, value);
        return this;
    }

}
