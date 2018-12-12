package com.kaha.bletools.bluetooth.utils.bluetooth;

import com.inuker.bluetooth.library.search.SearchResult;
import com.kaha.bletools.bluetooth.entity.BluetoothEntity;

import java.util.List;

/**
 * @author : Darcy
 * @package com.kaha.bletools.bluetooth.utils.bluetooth
 * @Date 2018-11-9 14:37
 * @Description 处理蓝牙的各种逻辑的工具类
 */
public class BleBluetoothHelper {


    private BluetoothEntity entity;

    /**
     * 将搜索到的设备转化为蓝牙实体
     *
     * @param device 搜索道的蓝牙实体数据
     * @return BluetoothEntity 蓝牙实体类
     * @Date 2018-11-12
     */
    public BluetoothEntity getEntity(SearchResult device) {
        entity = new BluetoothEntity();
        entity.setDeviceName(device.getName());
        entity.setDeviceAddress(device.getAddress());
        entity.setDeviceRSSI(device.rssi);
        return entity;
    }

    /**
     * 判断搜索到的设备是不是已经存入list的设备
     *
     * @param device 搜索到的设备
     * @param list   存放蓝牙设备的list
     * @return boolean true:同一个蓝牙设备 false :不是同一个设备
     * @Date 2018-11-13
     */
    public boolean isSame(SearchResult device, List<BluetoothEntity> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDeviceAddress().trim().equals(device.getAddress().trim())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}
