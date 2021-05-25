import bean.Data;
import bean.Handler;
import bean.Param;
import bean.View;
import com.alibaba.fastjson.JSON;
import helper.ConfigHelper;
import helper.ControllerHelper;
import helper.RequestHelper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import utils.ReflectionUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author: TangXiaoDong
 * @Date: 17:00 2021/5/21
 * @Description: 实现spring mvc前端控制器
 *      现在的配置策略是对所有的请求进行拦截，在服务器启动时实例化
 *      DispatcherServlet实例化时首先加载init方法
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    /**
     * 初始化
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        // 初始化helper类
        HelperLoader.init();
        // 获取servletContext，用于注册servlet
        ServletContext servletContext = config.getServletContext();
        // 注册处理jsp和静态资源的servlet
        registerServlet(servletContext);
    }


    /**
     * DefaultServlet和JspServlet都是由Web容器创建
     * @param servletContext
     */
    private void registerServlet(ServletContext servletContext) {

        // 动态注册处理jsp的servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getJspPath() + "*");

        // 动态注册处理静态资源的默认servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping("/favicon.ico"); // 网站根目录默认头像-
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestMethod = req.getMethod().toUpperCase();
        String requestPath = req.getPathInfo();

        // 根据Tomcat配置路径的两种情况，一种是/userList，另一种是/context地址/userList
        String[] splits = requestPath.split("/");
        if (splits.length > 2) {
            requestPath = "/" + splits[2];
        }

        // 根据请求获取处理器（类似与spring mvc的映射处理器）
        Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
        if (handler != null) {
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = handler.getControllerMethod();

            // 获取请求参数
            Param param = RequestHelper.createParam(req);
            // 调用与请求对应的方法（类似于spring mvc的处理器适配器）
            Object result;
            Method actionMethod = handler.getControllerMethod();
            if (param == null || param.isEmpty()) {
                result = ReflectionUtil.invokeMethod(controllerBean, actionMethod);
            } else {
                result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
            }

            // 跳转页面或返回json数据（类似于spring mvc中的视图解析器）
            if (result instanceof View) {
                handleViewResult((View)result, req, resp);
            } else if (result instanceof Data) {
                handleDataResult((Data)result, resp);
            }
        }

    }


    /**
     * 返回Json数据
     * @param data
     * @param resp
     */
    private void handleDataResult(Data data, HttpServletResponse resp) throws IOException {
        Object model = data.getModel();
        if (model != null) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter pr = resp.getWriter();
            String json = JSON.toJSONString(model);
            pr.write(json);
            pr.flush();
            pr.close();
        }
    }


    /**
     * 跳转页面
     * @param view
     * @param req
     * @param resp
     */
    private void handleViewResult(View view, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String path = view.getPath();
        if (StringUtils.isNotEmpty(path)) {
            if (path.startsWith("/")) { // 重定向
                resp.sendRedirect(req.getContextPath() + path);
            } else { // 请求转发
                Map<String, Object> model = view.getModel();
                for (Map.Entry<String, Object> entry : model.entrySet()) {
                    req.setAttribute(entry.getKey(), entry.getValue());
                }
                req.getRequestDispatcher(ConfigHelper.getJspPath()).forward(req, resp); // 内部转发
            }
        }
    }


}
