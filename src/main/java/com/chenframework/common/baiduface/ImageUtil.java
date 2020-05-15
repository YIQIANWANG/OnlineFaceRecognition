package com.chenframework.common.baiduface;

import com.chenframework.common.utils.SystemUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

/**
 * 图片处理工具类
 */
public class ImageUtil {

    /**
     * 去除base64编码的图片头部内容
     */
    public static String cutBase64Image(String image) {
        if (image != null && image.startsWith("data:")) {
            return image.substring(image.indexOf("base64") + 7);
        }
        return image;
    }

    /**
     * 将base64编码的图片保存到指定路径
     */
    public static boolean saveBase64Image(String base64Image, String filePath) {
        try {

            Path path = Paths.get(filePath);
            // byte[] bytes = Base64.getDecoder().decode(cutBase64Image(base64Image));
            byte[] bytes = Base64.getMimeDecoder().decode(cutBase64Image(base64Image));
            Files.write(path, bytes, StandardOpenOption.CREATE);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 将base64格式的图片文件保存到upload目录下指定的目录
     */
    public static String saveBase64Image(HttpServletRequest request, String folderName, String faceImage) {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File folder = new File(request.getSession().getServletContext().getRealPath("/static/upload/" + folderName + "/" + date));
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String fileName = SystemUtil.getUUID() + ".png";
        File imageFile = new File(folder, fileName);
        boolean flag = ImageUtil.saveBase64Image(faceImage, imageFile.getAbsolutePath());
        if (flag) {
            return "/static/upload/" + folderName + "/" + date + "/" + fileName;
        }
        return null;
    }

    /**
     * 将图片转换为base64格式
     */
    public static String convertImage2Base64(File image) {
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(image);
            data = new byte[in.available()];
            in.read(data);
            return Base64.getEncoder().encodeToString(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }
}
