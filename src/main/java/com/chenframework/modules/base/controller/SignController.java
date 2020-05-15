package com.chenframework.modules.base.controller;

import com.chenframework.common.baiduface.Face;
import com.chenframework.common.controller.BaseController;
import com.chenframework.common.model.PageParams;
import com.chenframework.common.model.view.AjaxJson;
import com.chenframework.common.model.view.TablePage;
import com.chenframework.common.utils.BooleanUtil;
import com.chenframework.modules.base.entity.Sign;
import com.chenframework.modules.base.entity.SignDetail;
import com.chenframework.modules.base.service.SignDetailService;
import com.chenframework.modules.base.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "${path.admin}/base/sign")
public class SignController extends BaseController {

    @Autowired
    private SignService signService;
    @Autowired
    private SignDetailService signDetailService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        return "/base/sign/list";
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public TablePage list(String signId, String no, PageParams pageParams) {
        Page<SignDetail> page = this.signDetailService.findAll(signId, no, pageParams);

        List<SignDetail> details = this.signDetailService.findAll();
        page.getContent().forEach(v -> {
            long times = details.stream().filter(value -> value.getNo().equals(v.getNo()) && !BooleanUtil.isTrue(value.getIsSuccess())).count();
            v.setTimes((int) times);
        });

        return new TablePage(page);
    }

    @RequestMapping(value = "/statistic", method = RequestMethod.GET)
    public String statistic(Model model) {
        model.addAttribute("signs", this.signService.findAll());
        return "/base/sign/statistic";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson create(String name) {
        if (StringUtils.isEmpty(name)) {
            return AjaxJson.createError("请输入课堂名");
        }
        if (signService.check(name)) {
            return AjaxJson.createError("该课堂名已存在");
        }
        Sign sign = this.signService.create(name);
        return AjaxJson.createSuccess("创建成功").addData("sign", sign);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson delete(String signId) {
        this.signService.deleteSign(signId);
        return AjaxJson.createSuccess("删除成功");
    }

    @RequestMapping(value = "/do", method = RequestMethod.GET)
    public String doPage(String signId, Model model) {
        model.addAttribute("signId", signId);
        return "/base/sign/do";
    }

    /**
     * 人脸识别签到
     */
    @RequestMapping(value = "/do", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson sign(HttpServletRequest request, String signId, String faceImage) {
        String faceUserId = Face.getInstance().search(faceImage, Face.DEFAULT_GROUPID); // 学生学号
        if (StringUtils.isEmpty(faceUserId)) {
            return AjaxJson.createError("未搜索该人员信息");
        } else {
            SignDetail signDetail = this.signDetailService.find(signId, faceUserId);
            if (signDetail == null) {
                return AjaxJson.createError("该人员不在当前课程中");
            } else {
                signDetail.setIsSuccess(true);
                signDetail.setSignTime(new Date());
                this.signDetailService.update(signDetail);

                Sign sign = this.signService.findById(signId);
                List<SignDetail> details = this.signDetailService.findAll(signId);
                long count = details.stream().filter(v -> BooleanUtil.isTrue(v.getIsSuccess())).count();
                sign.setSignNum((int) count);
                signService.update(sign);

                return AjaxJson.createSuccess("学号：【" + signDetail.getNo() + "】，姓名：【" + signDetail.getNo() + "】，签到成功");
            }
        }
    }
}
