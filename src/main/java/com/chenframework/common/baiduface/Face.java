package com.chenframework.common.baiduface;

import com.chenframework.common.baiduai.face.AipFace;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * 人脸接口
 */
public class Face {
    private Logger log = LoggerFactory.getLogger(Face.class);

    /**
     * 默认的用户组
     */
    public final static String DEFAULT_GROUPID = "default_group";

    private static Face face = new Face();

    private AipFace aipFace;

    private Face() {
        String appId = "18891443";
        String apiKey = "HjTnKfYG2tDTdF92PbqOWXlB";
        String secretKey = "fCkS3IVayPVIGtiIkuFHUGGs1KuQMCy5";

        aipFace = new AipFace(appId, apiKey, secretKey);

        // 可选：设置网络连接参数
        aipFace.setConnectionTimeoutInMillis(2000);
        aipFace.setSocketTimeoutInMillis(60000);

    }

    public static Face getInstance() {
        return face;
    }

    /**
     * 人脸检测,检测照片中是否存在人脸
     */
    public boolean detect(String base64Image) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("face_field", "age");
        options.put("max_face_num", "1");
        options.put("face_type", "LIVE");

        String imageType = "BASE64";
        JSONObject res = aipFace.detect(ImageUtil.cutBase64Image(base64Image), imageType, options);

        if (log.isDebugEnabled()) {
            log.debug("detect:" + res.toString(2));
        }

        if ("0".equals(getErrorCode(res))) {
            JSONObject result = res.getJSONObject("result");
            if (result != null && result.getInt("face_num") >= 0) {
                return true;
            }
        }

        return false;
    }

    /***
     * 人脸搜索,从人脸库中搜索指定的人脸信息：通过搜索返回的分值确定是否为同一个人脸
     * @return 返回用户的user_id
     */
    public String search(String base64Image, String groupIdList) {

        HashMap<String, String> options = new HashMap<String, String>();
        options.put("quality_control", "NORMAL");
        options.put("liveness_control", "LOW");
        // options.put("user_id", ""); // 当需要对特定用户进行比对时，指定user_id进行比对。即人脸认证功能。
        options.put("max_user_num", "1");// 查找后返回的用户数量。返回相似度最高的几个用户，默认为1，最多返回20个。
        String imageType = "BASE64";

        // 人脸搜索
        JSONObject res = aipFace.search(ImageUtil.cutBase64Image(base64Image), imageType, groupIdList, options);

        if (log.isDebugEnabled()) {
            log.debug("search:" + res.toString(2));
        }

        if ("0".equals(getErrorCode(res))) {
            JSONObject result = res.getJSONObject("result");
            if (result != null) {
                JSONArray userList = result.getJSONArray("user_list");
                if (userList != null && userList.length() > 0) {
                    JSONObject user = userList.getJSONObject(0);
                    double score = user.getDouble("score");

                    // 比对分值在80分以上认为是同一个人脸
                    if (score >= 80) {
                        return user.getString("user_id");
                    }

                }
            }
        }
        return null;
    }

    /**
     * 添加用户并注册人脸<br>
     */
    public JSONObject addUser(String base64Image, String groupId, String userId) {

        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("user_info", "");
        options.put("quality_control", "NORMAL");
        options.put("liveness_control", "LOW");

        String imageType = "BASE64";

        // 人脸注册
        JSONObject res = aipFace.addUser(ImageUtil.cutBase64Image(base64Image), imageType, groupId, userId, options);
        if (log.isDebugEnabled()) {
            log.debug("addUser:" + res.toString(2));
        }
        return res;

    }

    /**
     * 更新人脸
     */
    public JSONObject updateUser(String base64Image, String groupId, String userId) {

        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("user_info", "");
        options.put("quality_control", "NORMAL");
        options.put("liveness_control", "LOW");
        //  options.put("action_type", "replace"); // 执行更新操作，如果该uid不存在时，会返回错误。如果添加了action_type:replace,则不会报错，并自动注册该uid，操作结果等同注册新用户。

        String imageType = "BASE64";

        JSONObject res = aipFace.updateUser(ImageUtil.cutBase64Image(base64Image), imageType, groupId, userId, options);
        if (log.isDebugEnabled()) {
            log.debug("updateUser:" + res.toString(2));
        }
        return res;

    }

    /**
     * 删除人脸
     */
    public JSONObject faceDelete(String groupId, String userId, String faceToken) {
        HashMap<String, String> options = new HashMap<String, String>();
        JSONObject res = aipFace.faceDelete(userId, groupId, faceToken, options);
        if (log.isDebugEnabled()) {
            log.debug("faceDelete:" + res.toString(2));
        }
        return res;
    }

    /**
     * 删除人脸用户
     */
    public JSONObject deleteUser(String groupId, String userId) {
        HashMap<String, String> options = new HashMap<String, String>();
        JSONObject res = aipFace.deleteUser(groupId, userId, options);
        if (log.isDebugEnabled()) {
            log.debug("userDelete:" + res.toString(2));
        }
        return res;
    }

    /**
     * 解析error_code 如果是int类型转换为string
     */
    public static String getErrorCode(JSONObject res) {
        String errorCode = "";
        try {
            errorCode = res.getInt("error_code") + "";
        } catch (Exception e) {
            errorCode = res.getString("error_code");
        }
        return errorCode;
    }
}
