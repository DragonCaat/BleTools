package com.kaha.bletools.litepal;

import org.litepal.crud.DataSupport;

/**
 * 命令
 *
 * @author Darcy
 * @Date 2019/2/14
 * @package com.kaha.bletools.litepal
 * @Desciption
 */
public class Command extends DataSupport {

    //保存的命令
    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
