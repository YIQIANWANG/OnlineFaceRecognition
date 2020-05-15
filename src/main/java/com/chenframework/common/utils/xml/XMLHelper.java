package com.chenframework.common.utils.xml;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Small helper class that lazy loads DOM and SAX reader and keep them for fast use afterwards.
 */
@Slf4j
public class XMLHelper {

    private SAXReader saxReader;

    /**
     * 创建dom4j SAXReader,将会添加所有的错误验证到errorsList中
     * @param errorsList     错误验证列表
     * @param entityResolver 解决方案
     */
    public SAXReader createSAXReader(List<SAXParseException> errorsList, EntityResolver entityResolver) {
        if (saxReader == null) {
            saxReader = new SAXReader();
        }
        saxReader.setEntityResolver(entityResolver);
        saxReader.setErrorHandler(new ErrorLogger(errorsList));
        saxReader.setMergeAdjacentText(true);
        saxReader.setValidation(true);
        return saxReader;
    }

    private static class ErrorLogger implements ErrorHandler {
        private List<SAXParseException> errors;

        ErrorLogger(List<SAXParseException> errors) {
            this.errors = errors;
        }

        public void error(SAXParseException error) {
            errors.add(error);
        }

        public void fatalError(SAXParseException error) {
            error(error);
        }

        public void warning(SAXParseException warn) {
        }
    }

    /**
     * 将dom4j文档写入到指定的文件中
     */
    public static boolean writeToFile(Document docment, File file) {
        XMLWriter writer = null;
        try {
            writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8), getOutputFormat());
            writer.write(docment);
            return true;
        } catch (IOException e) {
            log.error("Failed to write xml document to file", e);
            return false;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将dom4j document转换为输入流
     */
    public static InputStream toInputStream(Document document) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLWriter writer = null;
        ByteArrayInputStream in = null;
        try {
            writer = new XMLWriter(out, getOutputFormat());
            writer.write(document);
            in = new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            log.error("Failed!", e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return in;
    }

    /**
     * 获取统一的xml文件格式化工具
     */
    public static OutputFormat getOutputFormat() {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setIndentSize(4);
        format.setLineSeparator("\n");
        format.setEncoding("UTF-8");
        return format;
    }
}
