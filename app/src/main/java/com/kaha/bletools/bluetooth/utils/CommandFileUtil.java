package com.kaha.bletools.bluetooth.utils;

import com.kaha.bletools.litepal.Command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 保存命令到本地的文件工具类
 *
 * @author Darcy
 * @Date 2019/2/21
 * @package com.kaha.bletools.bluetooth.utils
 * @Desciption
 */
public class CommandFileUtil {

    /**
     * @param tArrayList 要保存的数组
     * @param fileName   生成的文件名称
     * @return void
     * @date 2019-02-21
     */
    public static void saveCommand2SDCard(List tArrayList, String fileName) {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        FileInputStream fileInputStream = null;
        try {
            File file = FileUtil.getFile(File.separator + FileUtil.commandPath + File.separator + fileName);
            fileOutputStream = new FileOutputStream(file.toString());
            //新建一个内容为空的文件
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(tArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (objectOutputStream != null) {
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (fileOutputStream != null) {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取本地的List数据
     *
     * @return ArrayList
     * @date 2019-02-21
     */
    public ArrayList<Command> getCommandList(String fileName) {
        ObjectInputStream objectInputStream = null;
        FileInputStream fileInputStream = null;
        ArrayList<Command> savedArrayList = new ArrayList<>();
        try {
            File file = FileUtil.getFile(File.separator + FileUtil.commandPath + File.separator + fileName);
            fileInputStream = new FileInputStream(file.toString());
            objectInputStream = new ObjectInputStream(fileInputStream);
            savedArrayList = (ArrayList<Command>) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return savedArrayList;
    }


}
