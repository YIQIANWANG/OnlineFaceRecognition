package com.chenframework.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 文件操作工具类
 */
@Slf4j
public class FileUtil {

    public final static int BYTE_SIZE = 1024;

    /**
     * 比较两个文件是否完全一致，通过文件的MD5来比较
     */
    public static boolean compareFile(File file1, File file2) {
        String fileMd51 = getFileMD5(file1);
        String fileMd52 = getFileMD5(file2);
        return fileMd51 != null && fileMd51.equals(fileMd52);
    }

    /**
     * 文件复制拷贝到另一个文件
     * @param coverlay 是否覆盖
     */
    public static boolean copyFile(File sourceFile, File destFile, boolean coverlay) {
        if (!sourceFile.exists()) {
            log.warn("Copy file failed,the source file [{}] not exist!", sourceFile);
            return false;
        }
        if (!sourceFile.isFile()) {
            log.warn("Copy file failed,the source file, [{}]  not a file!", sourceFile);
            return false;
        }
        if (destFile.exists() && !destFile.isFile()) {
            log.warn("Copy file failed,the target file, [{}]  not a file!", destFile);
            return false;
        }

        File targetFile;
        if (destFile.exists()) {
            if (coverlay) {
                targetFile = destFile;
            } else {
                log.warn("Copy file failed,the target file  [{}]  exist!", destFile);
                return false;
            }
        } else {
            File targetParent = destFile.getParentFile();
            if (!targetParent.exists()) {
                targetParent.mkdirs();
            }
            targetFile = new File(targetParent, destFile.getName());
        }
        return writeFileByStream(sourceFile, targetFile);
    }

    /**
     * 删除文件，单个文件直接删除，如果是目录，递归删除目录下面的所有文件
     */
    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            return true;
        }

        if (file.isFile()) {
            return file.delete();
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            boolean flag = true;
            if (files != null) {
                for (File file_ : files) {
                    if (file_.isFile()) {
                        flag = file_.delete();
                    } else if (file_.isDirectory()) {
                        flag = deleteFile(file_);
                    }
                }
            }
            return flag;
        }

        return false;
    }

    /**
     * 获取文件的MD5
     */
    public static String getFileMD5(File file) {
        if (file.exists() && file.isFile()) {
            MessageDigest digest;
            FileInputStream in = null;

            byte[] buffer = new byte[BYTE_SIZE];
            int length;
            try {
                digest = MessageDigest.getInstance("MD5");
                in = new FileInputStream(file);
                while ((length = in.read(buffer)) != -1) {
                    digest.update(buffer, 0, length);
                }
                BigInteger bigInt = new BigInteger(1, digest.digest());
                return bigInt.toString(16);
            } catch (Exception e) {
                log.error("Failed to get the MD5 value of the file", e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取文件的大小
     */
    public static String getFileSize(File file) {
        long length = file.length();
        if (length < 1024) {
            return length + "B";
        } else {
            double value = (long) new BigDecimal(length / BYTE_SIZE).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
            if (value < 1024) {
                return value + "KB";
            } else {
                value = new BigDecimal(value / BYTE_SIZE).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
            }

            if (value < 1024) {
                return value + "MB";
            } else {
                value = new BigDecimal(value / BYTE_SIZE).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                return value + "GB";
            }
        }

    }

    /**
     * 读取文件内容
     */
    public static String readFile(File file) {
        InputStreamReader fileReader = null;
        BufferedReader bufferedReader = null;
        StringBuilder result = new StringBuilder();
        try {
            fileReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append("\n");
            }
        } catch (Exception e) {
            log.error("Failed to read the file:" + e.getMessage(), e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result.toString();
    }

    /**
     * I/O方式写文件
     */
    public static boolean writeFileByStream(File srcFile, File descFile) {
        int readByte;
        InputStream ins = null;
        OutputStream outs = null;
        try {
            ins = new FileInputStream(srcFile);
            outs = new FileOutputStream(descFile);
            byte[] buf = new byte[1024];
            while ((readByte = ins.read(buf)) != -1) {
                outs.write(buf, 0, readByte);
            }
            return true;
        } catch (Exception e) {
            log.error("Failed to write the file:" + e.getMessage(), e);
            return false;
        } finally {
            if (outs != null) {
                try {
                    outs.close();
                } catch (IOException oute) {
                    oute.printStackTrace();
                }
            }
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException ine) {
                    ine.printStackTrace();
                }
            }
        }
    }
}
