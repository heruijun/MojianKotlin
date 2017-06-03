package com.it51buy.mojian.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.it51buy.mojian.exception.NoSdCardException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();

    private static final String ROOT_PATH = "/MoJian/";
    private static final String PHOTO_PATH = "/DCIM/Camera/";
    private static final String SCREEN_SHOT_PATH = "/DCIM/Screenshots/";
    private static final String QRCODE_PATH = "Qrcode/";
    private static final String CONTACT_PATH = "Contact/";
    private static final String LOG_PATH = "Log/";

    public final static String tmpSuffix = ".tmp";
    public final static char rightparenthesis = ')';
    public final static char leftparenthesis = '(';

    /**
     * sd卡是否卸载
     *
     * @return
     */
    public static boolean isSDCardMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && FileUtil.isFileCanReadAndWrite(Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    /**
     * 得到根目录
     *
     * @throws NoSdCardException
     */
    public static String getRootPath() throws NoSdCardException {
        if (isSDCardMounted()) {
            return Environment.getExternalStorageDirectory().getPath() + ROOT_PATH;
        } else {
            throw new NoSdCardException();
        }
    }

    /**
     * 得到照片完整路径（包括照片名称）
     *
     * @return
     * @throws NoSdCardException
     */
    public static File getPhotoFile() throws NoSdCardException {
        if (isSDCardMounted()) {
            File storageDir = new File(Environment.getExternalStorageDirectory() + PHOTO_PATH);
            try {
                return createImageFile(storageDir);
            } catch (IOException e) {
                throw new NoSdCardException();
            }
        } else {
            throw new NoSdCardException();
        }
    }

    public static File getScreenShotFile() throws NoSdCardException {
        if (isSDCardMounted()) {
            File storageDir = new File(Environment.getExternalStorageDirectory() + SCREEN_SHOT_PATH);
            try {
                return createImageFile(storageDir);
            } catch (IOException e) {
                throw new NoSdCardException();
            }
        } else {
            throw new NoSdCardException();
        }
    }

    private static File createImageFile(File storageDir) throws IOException, NoSdCardException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File albumF = getAlbumDir(storageDir);
        File imageF = File.createTempFile(imageFileName, ".jpg", albumF);
        return imageF;
    }

    private static File getAlbumDir(File storageDir) throws NoSdCardException {
        if (!storageDir.mkdirs() && !storageDir.exists()) {
            throw new NoSdCardException();
        }
        return storageDir;
    }

    public static void galleryAddPic(String photoPath, Context context) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 得到Qrcode文件夹路径
     *
     * @return
     * @throws NoSdCardException
     */
    public static String getQrcodePath() throws NoSdCardException {
        return getRootPath() + QRCODE_PATH;
    }

    public static String getQrcodeFile() throws NoSdCardException {
        return getQrcodePath() + "Qrcode.jpg";
    }

    public static String getContactPath() throws NoSdCardException {
        return getRootPath() + CONTACT_PATH;
    }

    public static String getContactFile() throws NoSdCardException {
        return getContactPath() + System.currentTimeMillis() + ".png";
    }

    public static String getLogPath() throws NoSdCardException {
        return getRootPath() + LOG_PATH;
    }

    public static String getLogFile() throws NoSdCardException {
        return getLogPath() + "mj_crash.txt";
    }

    /**
     * 是否可读可写
     *
     * @param filePath
     * @return
     */
    public static boolean isFileCanReadAndWrite(String filePath) {
        if (null != filePath && filePath.length() > 0) {
            File f = new File(filePath);
            if (null != f && f.exists()) {
                return f.canRead() && f.canWrite();
            }
        }
        return false;
    }

    /**
     * 修改文件权限
     *
     * @param permission
     * @param path
     */
    public static void chmod(String permission, String path) {
        try {
            String command = "chmod " + permission + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (IOException e) {
        }
    }

    /**
     * 新建文件
     *
     * @param fileFullName
     * @return
     * @throws IOException
     */
    public static void newFile(String fileFullName) throws IOException {
        int pos = StringUtil.getPathLastIndex(fileFullName);
        if (pos > 0) {
            String strFolder = fileFullName.substring(0, pos);
            File file = new File(strFolder);
            if (!file.isDirectory()) {
                file.mkdirs();
            }
        }
        File file = new File(fileFullName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
    }

    /**
     * Prints some data to a file using a BufferedWriter
     */
    public static boolean writeToFile(String filename, String data, boolean append) {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(filename, append));
            bufferedWriter.write(data);
            return true;
        } catch (FileNotFoundException ex) {
            MJLog.e(TAG, "FileNotFoundException", ex);
        } catch (IOException ex) {
            MJLog.e(TAG, "IOException", ex);
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
            } catch (IOException ex) {
                MJLog.e(TAG, "IOException", ex);
            }
        }
        return false;
    }

}