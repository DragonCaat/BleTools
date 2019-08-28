package com.kaha.bletools.bluetooth.entity;

/**
 * @author Darcy
 * @Date 2019/8/27
 * @package com.kaha.bletools.bluetooth.entity
 * @Desciption
 */
public class RssiData {

    private String time;//取值时间

    private int rssi;//蓝牙信号值


    private int connectionFlag;//0::断开连接 1:连接

    public int getConnectionFlag() {
        return connectionFlag;
    }

    public void setConnectionFlag(int connectionFlag) {
        this.connectionFlag = connectionFlag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
}
