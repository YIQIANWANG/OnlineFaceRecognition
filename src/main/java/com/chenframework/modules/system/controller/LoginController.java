package com.chenframework.modules.system.controller;

import com.chenframework.common.model.view.AjaxJson;
import com.chenframework.common.utils.DateUtil;
import com.chenframework.common.utils.codec.RSAUtil;
import com.chenframework.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@Controller
@RequestMapping(value = "${path.admin}")
public class LoginController {
    private final static String RSA_P_KEY = "_rsa_private_key_"; // 用于保存密码解密的私钥的session key

    @Autowired
    private Config config;

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        createRSAKey(request);
        boolean isDev = config.isDevProfiles();
        model.addAttribute("year", DateUtil.getYear());
        model.addAttribute("randCode", isDev ? "debug" : "");
        return "/main/login";
    }

    @ResponseBody
    @PostMapping("/login")
    public AjaxJson login(String userName, String password, String validCode, HttpServletRequest request) {
        AjaxJson json = new AjaxJson();

        HttpSession session = request.getSession();
        RSAPrivateKey privateKey = (RSAPrivateKey) session.getAttribute(RSA_P_KEY);
        String rawPassword = RSAUtil.decrypt(privateKey, password);
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(rawPassword)) {
            json.setError("账户或者密码不能为空");
        } else {
            if ("superadmin".equals(userName) && "123456".equals(password)) {
                request.getSession().setAttribute("login", "1");
                json.setSuccess("登录成功");
            } else {
                json.setError("账户密码错误");
            }
        }
        return json;
    }

    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request, Model model) {
        request.getSession().removeAttribute("login");
        return "/main/login";
    }

    @GetMapping(value = "/index")
    public String index(Model model) {
        return "/main/index";
    }

    @GetMapping(value = "/home")
    public String home(Model model) {
        return "/main/home";
    }

    private void createRSAKey(HttpServletRequest request) {
        // 生成密码加密信息
        Map<String, Object> map = RSAUtil.getKeys();
        RSAPublicKey publicKey = (RSAPublicKey) map.get(RSAUtil.K_PUBLIC);
        RSAPrivateKey privateKey = (RSAPrivateKey) map.get(RSAUtil.K_PRIVATE);

        // 公钥信息输出到页面进行加密
        String publicKeyExponent = publicKey.getPublicExponent().toString(16);
        String publicKeyModulus = publicKey.getModulus().toString(16);
        request.setAttribute("publicKeyExponent", publicKeyExponent);
        request.setAttribute("publicKeyModulus", publicKeyModulus);

        // 私钥保留至session,进行验证
        request.getSession().setAttribute(RSA_P_KEY, privateKey);
    }
}
