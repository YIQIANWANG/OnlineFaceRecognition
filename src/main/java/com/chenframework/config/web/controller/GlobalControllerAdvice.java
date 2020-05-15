package com.chenframework.config.web.controller;

import com.chenframework.common.exception.BusinessException;
import com.chenframework.common.exception.FileUploadException;
import com.chenframework.common.model.view.AjaxJson;
import com.chenframework.common.utils.StringUtil;
import com.chenframework.common.utils.web.RequestHelper;
import com.chenframework.config.Globals;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局Controller增强，定义 @ExceptionHandler、@InitBinder、@ModelAttribute 注解的方法
 */
@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    @Value("${server.error.path}")
    private String errorView;

    @Autowired
    private ResourceUrlProvider resourceUrlProvider;

    /**
     * 处理静态资源的URL,主要用于处理应用层的的css和js等资源文件
     */
    @ModelAttribute("urls")
    public ResourceUrlProvider urls() {
        return this.resourceUrlProvider;
    }

    /**
     * 绑定数组参数的最大个数
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(1024 * 10);
    }

    /**
     * 捕获文件上传大小限制异常<br>
     * 捕获流程：设置框架限定的上传文件大小足够大（保证文件上传的时候，不被框架捕获超限抛异常），再通过业务控制层判定实际上传的文件大小，<br>
     * 如果判定超限，则抛出自定义异常{@link FileUploadException}，最后在此捕获处理<br>
     * 注意：系统限定文件上传均通过ajax方式上传
     */
    @ExceptionHandler(value = FileUploadException.class)
    @ResponseBody
    public AjaxJson errorUploadHandler(HttpServletRequest request, HttpServletResponse response, FileUploadException ex) {
        boolean isAjax = RequestHelper.isAjaxRequest(request);
        log.error("The uploaded file size exceeds the maximum limit, ajax request:" + isAjax);
        if (isAjax) {
            return AjaxJson.createError("文件大小超过限制[限制" + (ex.getMaxSize() / (1024 * 1024)) + "MB]，请重新上传");
        }
        return null;
    }

    /**
     * 捕获系统业务异常
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public AjaxJson errorBusinessHandler(HttpServletRequest request, HttpServletResponse response, BusinessException ex) {
        boolean isAjax = RequestHelper.isAjaxRequest(request);
        log.error("System business is abnormal, ajax request:" + isAjax, ex);
        if (isAjax) {
            return new AjaxJson(AjaxJson.CODE_EXCEPTION, "错误提示:" + ex.getMessage());
        }
        return null;
    }

    /**
     * 全局异常处理<br>
     * 系统应用异常由此捕获，404异常由应用框架捕获GlobalErrorController，返回的错误页面由应用框架统一定义（ajax例外）
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ModelAndView errorHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        boolean isAjax = RequestHelper.isAjaxRequest(request);
        log.error("System access exception, ajax request:" + isAjax, ex);

        int status;
        if (ex instanceof org.apache.shiro.authz.AuthorizationException) { // 未授权
            status = Globals.HTTP_CODE_NOAUTH;
        } else {
            status = Globals.HTTP_CODE_EXCEPTION;
        }

        if (isAjax) {
            response.setStatus(status);
            return null;
        } else {
            String errorMsg = ex.getMessage();
            errorMsg = StringUtil.isNotEmpty2(errorMsg) ? ("：" + errorMsg) : "";

            Map<String, Object> model = new HashMap<>();
            model.put("status", status);
            model.put("error", HttpStatus.valueOf(status).getReasonPhrase());
            model.put("path", request.getRequestURI());
            model.put("message", status == Globals.HTTP_CODE_NOAUTH ? "您没有访问权限" : ("服务器出现异常" + errorMsg));

            response.setStatus(status);
            return new ModelAndView(errorView, model);
        }
    }

}
