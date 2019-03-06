package com.kaha.bletools.bluetooth.utils;

import android.annotation.SuppressLint;

import com.kaha.bletools.litepal.Command;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
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
}
