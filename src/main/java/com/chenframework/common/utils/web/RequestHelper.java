package com.chenframework.common.utils.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * HttpServletRequest访问请求辅助类
 */
@Slf4j
public final class RequestHelper {

    /**
     * 本机默认IP地址
     */
    public static final String LOCAL_IP = "127.0.0.1";

    private RequestHelper() {
    }

    /**
     * 获取当前的请求对象
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            assert requestAttributes != null;
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        } catch (Exception e) {
            log.error("Can not get the HttpServletRequest from current request");
            return null;
        }
    }

    /**
     * 获取系统访问的基础路径
     * @param request HttpServletRequest
     * @return 根路径：例如:http://127.0.0.1:8080/minimes
     */
    public static String getBasePath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    /**
     * 获取网络请求端的IP地址
     * @param request HttpServletRequest
     */
    public static String getIPAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = LOCAL_IP;
        }
        return ip;
    }

    /**
     * 获取springMVC的RequestContext上下文
     * @param request HttpServletRequest
     */
    public static RequestContext getMVCRequestContext(HttpServletRequest request) {
        return new RequestContext(request);
    }

    /**
     * 获取ServletContext上下文
     * @param request HttpServletRequest
     */
    public static ServletContext getServletContext(HttpServletRequest request) {
        return request.getSession().getServletContext();
    }

    /**
     * 判断请求是否为AJAX请求
     * @param request HttpServletRequest
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        return header != null && "XMLHttpRequest".equals(header);
    }

}
