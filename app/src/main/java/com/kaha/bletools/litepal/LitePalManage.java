package com.kaha.bletools.litepal;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作类
 *
 * @author Darcy
 * @Date 2019/2/14
 * @package com.kaha.bletools.litepal
 * @Desciption
 */
public class LitePalManage {
    private static LitePalManage instance;


    /**
     * 保存命令
     *
     * @param command 要保存的命令
     * @return Boolean true:保存成功 false:保存失败
     * @date 2019-02-14
     */
    public Boolean saveCommand(String command) {
        Command liteCommand = new Command();
        liteCommand.setCommand(command);
        boolean save = liteCommand.save();
        return save;
    }

    /**
     * 保存文件命令
     *
     * @param commandName 要保存的命令名称
     * @param commandValue 要保存的命令值
     * @return Boolean true:保存成功 false:保存失败
     * @date 2019-02-14
     */
    public Boolean saveFileCommand(String commandName,String commandValue) {
        FileCommand liteCommand = new FileCommand();
        liteCommand.setCommandName(commandName);
        liteCommand.setCommandValue(commandValue);
        boolean save = liteCommand.save();
        return save;
    }

    /**
     * 删除命令
     *
     * @param command 要删除的命令
     * @return void
     * @date 2019-02-14
     */
    public void removeCommand(Command command) {
        command.delete();
    }

    /**
     * 获取保存的命令的集合
     *
     * @param
     * @return List<Command> 命令集合
     * @date 2019-02-14
     */
    public List<Command> getCommandList() {
        List<Command> commandList;
        commandList = DataSupport.findAll(Command.class);
        if (null == commandList) {
            commandList = new ArrayList<>();
        }
        return commandList;
    }

    /**
     * 获取保存的文件命令的集合
     *
     * @param
     * @return List<Command> 命令集合
     * @date 2019-02-14
     */
    public List<FileCommand> getFileCommandList() {
        List<FileCommand> commandList;
        commandList = DataSupport.findAll(FileCommand.class);
        if (null == commandList) {
            commandList = new ArrayList<>();
        }
        return commandList;
    }


    public static LitePalManage getInstance() {
        if (null == instance) {
            synchronized (LitePalManage.class) {
                if (null == instance) {
                    instance = new LitePalManage();
                }
            }
        }
        return instance;
    }
}
