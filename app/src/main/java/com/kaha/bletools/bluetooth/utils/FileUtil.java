package com.kaha.bletools.bluetooth.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.kaha.bletools.framework.utils.ToastUtil;
import com.kaha.bletools.litepal.Command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Darcy
 * @Date 2018/12/28
 * @package com.kaha.bletool.utils
 * @Desciption 文件工具类
 */
public class FileUtil {

    @SuppressLint("SdCardPath")
    public static final String filePath = "/sdcard/Akaha/";

    public static final String commandPath = "/sdcard/bleToolsCommand";

    /**
     * 生成文件夹名称
     *
     * @param ,
     * @return String 生成的文件名称
     * @date 2018-12-28
     */
    @SuppressLint("SimpleDateFormat")
    public static String getFileName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date(System.currentTimeMillis());
        String fileName = simpleDateFormat.format(date) + ".txt";
        return fileName;
    }


    /**
     * 生成对应的文件 txt
     *
     * @param fileName 文件名称
     * @param filePath 文件路径
     * @return file 生成的文件
     * @date 2018-12-28
     */
    public static File getFile(String filePath, String fileName) {
        //先生成文件夹后，再生成文件，不然会有异常
        makeRootFile();
        File file = new File(filePath + fileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }


    /**
     * 生成对应的文件 txt
     *
     * @param filePath 文件路径
     * @return file 生成的文件
     * @date 2018-12-28
     */
    public static File getFile(String filePath) {
        //先生成文件夹后，再生成文件，不然会有异常
        makeRootFile();
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }


    /**
     * 将文本写入到文件
     *
     * @param content 要写入的内容
     * @return void
     * @date 2019-02-15
     */
    public static void writeTextToFile(String content) {
        getFile(filePath, getFileName());
        //文本的名称
        String strFilePath = filePath + getFileName();
        File file = new File(strFilePath);
        try {
            if (!file.exists()) {
                //文件不存在
                file.getParentFile().mkdir();
                file.createNewFile();
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
            randomAccessFile.seek(file.length());
            randomAccessFile.write(content.getBytes());
            randomAccessFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将文本写入到文件
     *
     * @param command 要写入的内容
     * @return void
     * @date 2019-02-15
     */
    public static void writeCommandToFile(String command) {
        getFile(commandPath, "bleCommand");
        //文本的名称
        String strFilePath = commandPath + "bleCommand";
        File file = new File(strFilePath);
        try {
            if (!file.exists()) {
                //文件不存在
                file.getParentFile().mkdir();
                file.createNewFile();
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
            randomAccessFile.seek(file.length());
            randomAccessFile.write(command.getBytes());
//            randomAccessFile.write("\r");
//            randomAccessFile.write("\n");
            randomAccessFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 生成对应的file.txt 文件
     *
     * @param fileName txt文件名称
     * @param filePath txt文件路径
     * @return File txt文件
     * @date 2018-12-28
     */
    public static File makeTxtFile(String filePath, String fileName) {
        File file = null;
        makeRootFile();
        file = new File(filePath + fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 生成文件夹
     * <b>
     * 这里生成的是项目的根目录即 Akaha 这个名称的文件夹
     * </b>
     *
     * @param ,
     * @return void
     * @date 2018-12-28
     */
    public static void makeRootFile() {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                //如果文件夹不存在就创建
                file.mkdir();
            }
        } catch (Exception e) {

        }
    }

    //获取文件路径
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) { // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
//                Log.i(TAG,"isExternalStorageDocument***"+uri.toString());
//                Log.i(TAG,"docId***"+docId);
//                以下是打印示例：
//                isExternalStorageDocument***content://com.android.externalstorage.documents/document/primary%3ATset%2FROC2018421103253.wav
//                docId***primary:Test/ROC2018421103253.wav
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } // DownloadsProvider
            else if (isDownloadsDocument(uri)) { //                Log.i(TAG,"isDownloadsDocument***"+uri.toString());
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            } // MediaProvider
            else if (isMediaDocument(uri)) { //                Log.i(TAG,"isMediaDocument***"+uri.toString());
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) { //            Log.i(TAG,"content***"+uri.toString());
            return getDataColumn(context, uri, null, null);
        } // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) { //            Log.i(TAG,"file***"+uri.toString());
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    /**
     * 根据行读取内容
     *
     * @return
     */
    public static List<String> getFileCommand(Context context, String filePath) {

        if (TextUtils.isEmpty(filePath)) {
            ToastUtil.show(context, "文件不存在");
            return null;
        }

        List newList = new ArrayList<String>();
        try {
            File file = new File(filePath);
            int count = 0;//初始化 key值
            if (file.isFile() && file.exists()) {//文件存在
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                while ((lineTxt = br.readLine()) != null) {
                    if (!"".equals(lineTxt)) {
                        //String reds = lineTxt.split("\\+")[0];  //java 正则表达式
                        //newList.add(count, reds);
                        boolean add = newList.add(lineTxt);
                        count++;
                    }

                }
                isr.close();
                br.close();
            } else {
                ToastUtil.show(context, "can not find file");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newList;
    }
}
