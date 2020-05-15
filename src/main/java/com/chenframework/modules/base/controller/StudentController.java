package com.chenframework.modules.base.controller;

import com.chenframework.common.baiduface.ErrorInfo;
import com.chenframework.common.baiduface.Face;
import com.chenframework.common.baiduface.ImageUtil;
import com.chenframework.common.controller.BaseController;
import com.chenframework.common.controller.UploadFile;
import com.chenframework.common.exception.BusinessException;
import com.chenframework.common.model.PageParams;
import com.chenframework.common.model.view.AjaxJson;
import com.chenframework.common.model.view.TablePage;
import com.chenframework.common.utils.StringUtil;
import com.chenframework.common.utils.clazz.BeanUtil;
import com.chenframework.modules.base.entity.Student;
import com.chenframework.modules.base.service.StudentService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "${path.admin}/base/student")
public class StudentController extends BaseController {

    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        return "/base/student/list";
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public TablePage list(Student queryParams, PageParams pageParams) {
        Page<Student> page = this.studentService.findAll(queryParams, pageParams);
        return new TablePage(page);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson delete(String[] ids) {
        studentService.delete(ids);
        return sucDelete();
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model) {
        return "/base/student/form";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson add(Student params, HttpServletRequest request, MultipartFile file) throws Exception {

        UploadFile uploadFile = super.uploadFile(request, file, "photo");
        if (uploadFile.getHasFile()) {
            params.setPhoto(uploadFile.getPath());
            String faceToken = this.updateFace(uploadFile, params.getNo(), null);
            params.setFaceToken(faceToken);
        } else {
            throw new BusinessException("请上传学生照片");
        }

        this.studentService.save(params);
        return sucSave();
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public String update(String id, Model model) {
        Student bean = this.studentService.findById(id);
        model.addAttribute("bean", bean);
        return add(model);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson update(Student params, HttpServletRequest request, MultipartFile file) throws Exception {
        Student user = this.studentService.findById(params.getId());
        BeanUtil.copyBeanNotNull2SameBean(params, user);

        UploadFile uploadFile = super.uploadFile(request, file, "photo");
        if (uploadFile.getHasFile()) {
            user.setPhoto(uploadFile.getPath());
            String faceToken = this.updateFace(uploadFile, user.getNo(), user.getFaceToken());
            user.setFaceToken(faceToken);
        }
        studentService.update(user);

        return sucUpdate();
    }

    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson validate(String property, String value) {
        boolean flag = this.studentService.exists(property, value);
        if (flag) {
            return invalid(property, "");
        }
        return new AjaxJson();
    }

    /**
     * 上传照片到人脸库
     */
    public String updateFace(UploadFile file, String no, String oldFaceToken) {

        // 转码并注册人脸库
        String faceToken = null;
        JSONObject faceRes = null;
        if (StringUtil.isNotEmpty(oldFaceToken)) { // 更新
            faceRes = Face.getInstance().updateUser(ImageUtil.convertImage2Base64(file.getFile()), Face.DEFAULT_GROUPID, no);
        } else { // 注册
            faceRes = Face.getInstance().addUser(ImageUtil.convertImage2Base64(file.getFile()), Face.DEFAULT_GROUPID, no);
        }

        String errorCode = Face.getErrorCode(faceRes);
        //代码为223103为人脸库无此人信息，此时重新注册
        if ("223103".equals(errorCode)) {
            faceRes = Face.getInstance().addUser(ImageUtil.convertImage2Base64(file.getFile()), Face.DEFAULT_GROUPID, no);
        }
        if ("0".equals(errorCode)) {
            JSONObject result = faceRes.getJSONObject("result");
            if (result != null) {
                faceToken = result.getString("face_token");
            } else {
                throw new BusinessException("照片上传失败");
            }
        } else {
            throw new BusinessException(ErrorInfo.getError(errorCode));
        }

        if (faceToken != null) {
            return faceToken;
        }
        throw new BusinessException("人脸照片上传人脸库失败");
    }
}
