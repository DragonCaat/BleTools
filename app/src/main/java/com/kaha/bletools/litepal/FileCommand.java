package com.kaha.bletools.litepal;

import org.litepal.crud.DataSupport;

/**
 * 从文件夹中获取的命令
 *
 * @author Darcy
 * @Date 2019/7/9
 * @package com.kaha.bletools.litepal
 * @Desciption
 */
public class FileCommand extends DataSupport {

    private String commandValue;//命令的值

    private String commandName;//命令的名称

    public String getCommandValue() {
        return commandValue;
    }

    public void setCommandValue(String commandValue) {
        this.commandValue = commandValue;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }
}
