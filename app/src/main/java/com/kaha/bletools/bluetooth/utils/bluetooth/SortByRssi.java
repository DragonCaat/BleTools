package com.kaha.bletools.bluetooth.utils.bluetooth;

import com.kaha.bletools.bluetooth.entity.BluetoothEntity;

import java.util.Comparator;

/**
 * @author : Darcy
 * @package com.kaha.bletools.bluetooth.utils.bluetooth
 * @Date 2018-11-13 14:02
 * @Description 根据信号强弱排序
 */
public class SortByRssi implements Comparator {

    public int compare(Object o1, Object o2) {
        BluetoothEntity s1 = (BluetoothEntity) o1;
        BluetoothEntity s2 = (BluetoothEntity) o2;
        if (s1.getDeviceRSSI() < s2.getDeviceRSSI())
            return 1;
        if (s1.getDeviceRSSI() == s2.getDeviceRSSI())
            return 0;
        return -1;
    }
}
