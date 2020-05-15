package com.chenframework.common.controller;

import com.chenframework.common.exception.FileUploadException;
import com.chenframework.common.model.view.AjaxJson;
import com.chenframework.common.model.view.TableList;
import com.chenframework.common.model.view.TablePage;
import com.chenframework.common.utils.CollectionUtil;
import com.chenframework.common.utils.DateUtil;
import com.chenframework.common.utils.SystemUtil;
import com.chenframework.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Controller基础支持类
 */
public abstract class BaseController {
    protected Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected Config config;

    protected AjaxJson invalid(String field, String msg) {
        AjaxJson json = new AjaxJson(AjaxJson.CODE_VALIDATE);
        json.addData(field, msg);
        return json;
    }

    /**
     * 返回页面表格数据
     */
    protected TableList tableList(List<?> list) {
        return new TableList(list);
    }

    /**
     * 返回页面表格数据
     */
    protected TablePage tablePage(List<?> list) {
        return new TablePage(list);
    }

    /**
     * 返回页面表格数据
     */
    protected TablePage tablePage(Page<?> page) {
        return new TablePage(page);
    }

    /**
     * 删除成功提示
     */
    protected AjaxJson sucDelete() {
        return AjaxJson.createSuccess("删除成功");
    }

    /**
     * 添加成功提示
     */
    protected AjaxJson sucSave() {
        return AjaxJson.createSuccess("保存成功");
    }

    /**
     * 修改成功提示信息
     */
    protected AjaxJson sucUpdate() {
        return AjaxJson.createSuccess("修改成功");
    }

    /**
     * 解析多文件上传
     */
    protected List<UploadFile> uploadFile(HttpServletRequest request, String folderName) throws Exception {
        List<UploadFile> files = CollectionUtil.newArrayList();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                MultipartFile file = multiRequest.getFile(iter.next());
                UploadFile uploadFile = this.uploadFile(multiRequest, file, folderName);
                if (uploadFile.getHasFile()) {
                    files.add(uploadFile);
                }
            }
        }
        return files;
    }

    /**
     * 单文件上传解析
     */
    protected UploadFile uploadFile(HttpServletRequest request, MultipartFile file, String folderName) throws Exception {
        if (file != null && !file.isEmpty()) {
            if (file.getSize() > config.getSysUploadMaxUploadSize()) {
                throw new FileUploadException(config.getSysUploadMaxUploadSize(), file.getSize());
            } else {
                String fileOriName = file.getOriginalFilename();
                assert fileOriName != null;
                int index = fileOriName.lastIndexOf(".");
                String ext = "";
                if (index > 0) {
                    ext = fileOriName.substring(index);
                }
                String fileName = SystemUtil.getUUID() + ext;

                String date = folderName + "/" + DateUtil.formatDatetime(new Date(), "yyyyMMdd");
                File folder = new File(config.getSysUploadPath(), date);
                if (!folder.exists()) {
                    boolean isMkDirs = folder.mkdirs();
                    if (isMkDirs) {
                        log.debug("Create a new folder:{}", folder);
                    }
                }
                File targetFile = new File(folder, fileName);
                file.transferTo(targetFile);

                String path = "/upload/" + date + "/" + fileName;
                return UploadFile.builder().fileName(fileName).originalFilename(fileOriName).path(path).file(targetFile).hasFile(true).build();
            }
        }
        return UploadFile.builder().hasFile(false).build();
    }

}
