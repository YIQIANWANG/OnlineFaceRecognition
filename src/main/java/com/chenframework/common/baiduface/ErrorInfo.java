package com.chenframework.common.baiduface;

import java.util.HashMap;
import java.util.Map;

/**
 * 错误信息
 */
public final class ErrorInfo {

    private static Map<String, String> map;
    static {
        map = new HashMap<String, String>();
        map.put("4", "集群超限额");
        map.put("17", "每天流量超限额");
        map.put("18", "QPS超限额");
        map.put("19", "请求总量超限额");
        map.put("100", "无效的access_token参数");
        map.put("110", "Access Token失效");
        map.put("111", "Access token过期");
        map.put("216100", "参数异常，具体异常原因详见error_msg");
        map.put("216101", "缺少必须的参数，具体异常原因详见error_msg");
        map.put("216102", "请求了不支持的服务");
        map.put("216103", "请求超长");
        map.put("216110", "appid不存在");
        map.put("216111", "userid信息非法");
        map.put("216200", "图片为空或者base64解码错误");
        map.put("216201", "图片格式错误");
        map.put("216202", "图片大小错误");
        map.put("216300", "数据库异常");
        map.put("216400", "后端识别服务异常");
        map.put("216402", "未找到人脸");
        map.put("216500", "未知错误");
        map.put("216611", "用户不存在");
        map.put("216613", "删除用户图片记录失败");
        map.put("216614", "两两比对中图片数少于2张，无法比较");
        map.put("216615", "服务处理该图片失败");
        map.put("216616", "图片已存在");
        map.put("216617", "新增用户图片失败");
        map.put("216618", "组内用户为空");
        map.put("216631", "本次请求添加的用户数量超限");
        map.put("216501", "传入的生活照中没有找到人脸");
        map.put("216600", "身份证格式错误，请检查后重新输入");
        map.put("216601", "身份证号与姓名匹配失败");
        map.put("216602", "人脸被遮挡了，质量检测不通过");
        map.put("216603", "人脸光照不好，质量检测不通过");
        map.put("216604", "人脸不完整，质量检测不通过");
        map.put("216605", "人脸置信度低，质量检测不通过");
        map.put("216606", "人脸模糊，质量检测不通过");
        map.put("216607", "公安库无此人图片或公安小图质量不佳");
        map.put("216608", "输入的生活照活体校验不通过");
        map.put("216609", "人脸左右角度过大");
        map.put("216610", "人脸俯仰角度过大");
        map.put("SDK100", "图片大小超限");
        map.put("SDK101", "图片边长不符合要求");
        map.put("SDK102", "读取图片文件错误");
        map.put("SDK108", "连接超时或读取数据超时");
        map.put("SDK109", "不支持的图片格式");
        map.put("4", "集群超限额");
        map.put("14", "IAM鉴权失败，建议用户参照文档自查生成sign的方式是否正确，或换用控制台中ak sk的方式调用");
        map.put("17", "每天流量超限额");
        map.put("18", "QPS超限额");
        map.put("19", "请求总量超限额");
        map.put("100", "无效参数");
        map.put("110", "Access Token失效");
        map.put("111", "Access token过期");
        map.put("222001", "必要参数未传入");
        map.put("222002", "参数格式错误");
        map.put("222003", "参数格式错误");
        map.put("222004", "参数格式错误");
        map.put("222005", "参数格式错误");
        map.put("222006", "参数格式错误");
        map.put("222007", "参数格式错误");
        map.put("222008", "参数格式错误");
        map.put("222009", "参数格式错误");
        map.put("222010", "参数格式错误");
        map.put("222011", "参数格式错误");
        map.put("222012", "参数格式错误");
        map.put("222013", "参数格式错误");
        map.put("222014", "参数格式错误");
        map.put("222015", "参数格式错误");
        map.put("222016", "参数格式错误");
        map.put("222017", "参数格式错误");
        map.put("222018", "参数格式错误");
        map.put("222019", "参数格式错误");
        map.put("222020", "参数格式错误");
        map.put("222021", "参数格式错误");
        map.put("222022", "参数格式错误");
        map.put("222023", "参数格式错误");
        map.put("222024", "参数格式错误");
        map.put("222025", "参数格式错误");
        map.put("222026", "参数格式错误");
        map.put("222201", "服务端请求失败");
        map.put("222202", "图片中没有人脸");
        map.put("222203", "无法解析人脸");
        map.put("222204", "从图片的url下载");
        map.put("222205", "服务端请求失败");
        map.put("222206", "服务端请求失败");
        map.put("222207", "未找到匹配的用户");
        map.put("222208", "图片的数量错误");
        map.put("222209", "face token不存在");
        map.put("222300", "人脸图片添加失败");
        map.put("222301", "获取人脸图片失败");
        map.put("222302", "服务端请求失败");
        map.put("222303", "获取人脸图片失败");
        map.put("223100", "操作的用户组不存在");
        map.put("223101", "该用户组已存在");
        map.put("223102", "该用户已存在");
        map.put("223103", "找不到该用户");
        map.put("223104", "group_list包含组");
        map.put("223105", "该人脸已存在");
        map.put("223106", "该人脸不存在");
        map.put("223110", "uid_list包含数量过多");
        map.put("223111", "目标用户组不存在");
        map.put("223112", "quality_conf格式不正确");
        map.put("223113", "人脸有被遮挡");
        map.put("223114", "人脸模糊");
        map.put("223115", "人脸光照不好");
        map.put("223116", "人脸不完整");
        map.put("223117", "app_list包含app数量");
        map.put("223118", "质量控制项错误");
        map.put("223119", "活体控制项错误");
        map.put("223120", "活体检测未通过");
        map.put("223121", "质量检测未通过 左眼");
        map.put("null", "遮挡程度过高");
        map.put("223122", "质量检测未通过 右眼");
        map.put("223123", "质量检测未通过 左脸");
        map.put("223124", "质量检测未通过 右脸");
        map.put("223125", "质量检测未通过 下巴遮挡程度过高");
        map.put("223126", "质量检测未通过 鼻子遮挡程度过高");
        map.put("223127", "质量检测未通过 嘴巴");
        map.put("222350", "公安网图片不存在或");
        map.put("222351", "身份证号与姓名不匹配或该");
        map.put("222352", "身份证名字格式错误");
        map.put("222353", "身份证号码格式错误");
        map.put("222354", "公安库里不存在此身份证号");
        map.put("222355", "身份证号码正确，公安库里没有");
        map.put("222360", "身份证号码或名字非法（公安网校");
        map.put("222901", "系统繁忙");
        map.put("222902", "系统繁忙");
        map.put("222903", "系统繁忙");
        map.put("222904", "系统繁忙");
        map.put("222905", "系统繁忙");
        map.put("222906", "系统繁忙");
        map.put("222907", "系统繁忙");
        map.put("222908", "系统繁忙");
        map.put("222909", "系统繁忙");
        map.put("222910", "系统繁忙");
        map.put("222911", "系统繁忙");
        map.put("222912", "系统繁忙");
        map.put("222913", "系统繁忙");
        map.put("222914", "系统繁忙");
        map.put("222915", "系统繁忙");
        map.put("222916", "系统繁忙");
        map.put("222361", "系统繁忙");
    }

    public static String getError(int code) {
        return map.get(code + "");
    }

    public static String getError(String code) {
        return map.get(code);
    }
}
