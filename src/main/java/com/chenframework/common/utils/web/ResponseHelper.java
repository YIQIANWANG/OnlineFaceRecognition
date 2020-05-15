package com.chenframework.common.utils.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * HttpServletResponse辅助工具类
 */
@Slf4j
public final class ResponseHelper {

    /**
     * 发送内容。使用UTF-8编码。
     */
    public static void render(HttpServletResponse response, String contentType, String text) {
        response.setContentType(contentType);
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        try {
            response.getWriter().write(text);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 发送json。使用UTF-8编码。
     */
    public static void renderJson(HttpServletResponse response, String text) {
        render(response, "application/json;charset=UTF-8", text);
    }

    /**
     * 发送文本。使用UTF-8编码。
     */
    public static void renderText(HttpServletResponse response, String text) {
        render(response, "text/plain;charset=UTF-8", text);
    }

    /**
     * 发送xml。使用UTF-8编码。
     */
    public static void renderXml(HttpServletResponse response, String text) {
        render(response, "text/xml;charset=UTF-8", text);
    }

    /**
     * 输出excel流
     */
    public static void writeExcelOutputStream(HttpServletResponse response, InputStream is, String fileName) throws IOException {
        fileName = new String(fileName.getBytes(), "ISO8859-1");
        response.setHeader("Content-disposition", fileName);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("Pragma", "No-cache");
        FileCopyUtils.copy(is, response.getOutputStream());
    }

}
