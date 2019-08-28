package com.kaha.bletools.bluetooth.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 输出数据
 *
 * @author Darcy
 * @Date 2019/2/14
 * @package com.kaha.bletools.bluetooth.entity
 * @Desciption
 */
public class OutputData extends DataSupport implements Serializable {

    private String outputString;

    public String getOutputString() {
        return outputString;
    }

    public OutputData(String outputString) {
        this.outputString = outputString;
    }
}
