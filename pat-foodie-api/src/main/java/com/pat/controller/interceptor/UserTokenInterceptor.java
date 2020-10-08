package com.pat.controller.interceptor;

import com.pat.utils.JsonUtils;
import com.pat.utils.RedisOperator;
import com.pat.utils.ResJSONResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/10/7
 * @Modify
 * @since
 */
public class UserTokenInterceptor implements HandlerInterceptor {

    public static final String REDIS_USER_TOKEN = "REDIS_USER_TOKEN";

    @Autowired
    private RedisOperator redisOperator;

    /**
     * 拦截请求，在访问 Controller 之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /**
         * false：请求被拦截，被驳回，验证出现问题
         * true：请求在经过验证校验之后，是OK的，便可以放行
         */
        String userId = request.getHeader("userId");
        String headerUserToken = request.getHeader("headerUserToken");

        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(headerUserToken)) {
            String uniqueToken = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
            if (StringUtils.isBlank(uniqueToken)) {
                returnErrorResponse(response, ResJSONResult.errorMsg("请登陆......."));
                return false;
            } else {
                if (!uniqueToken.equals(headerUserToken)) {
                    returnErrorResponse(response, ResJSONResult.errorMsg("账号在异地登陆......."));
                    return false;
                }
            }
        } else {
            returnErrorResponse(response, ResJSONResult.errorMsg("请登陆......."));
            return false;
        }
        return true;
    }

    public void returnErrorResponse(HttpServletResponse response, ResJSONResult resJSONResult) {

        OutputStream out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/json");
            out = response.getOutputStream();
            out.write(JsonUtils.objectToJson(resJSONResult).getBytes("utf-8"));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 请求访问Controller之后，渲染视图之前
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 请求访问Controller之后，渲染视图之后
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
