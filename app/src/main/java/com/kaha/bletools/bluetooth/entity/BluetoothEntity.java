package com.kaha.bletools.bluetooth.entity;

/**
 * @author : Darcy
 * @package com.kaha.bletools.bluetooth.entity
 * @Date 2018-11-12 15:51
 * @Description 蓝牙设备信息的实体类
 */
public class BluetoothEntity {

    private String deviceName;
    private String deviceAddress;
    private int deviceRSSI;


    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public int getDeviceRSSI() {
        return deviceRSSI;
    }

    public void setDeviceRSSI(int deviceRSSI) {
        this.deviceRSSI = deviceRSSI;
    }
}
