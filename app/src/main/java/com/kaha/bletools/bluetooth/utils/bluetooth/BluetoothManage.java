package com.kaha.bletools.bluetooth.utils.bluetooth;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.kaha.bletools.framework.base.BluetoothApplication;

/**
 * @author : Darcy
 * @package com.kaha.bletools.bluetooth.utils.bluetooth
 * @Date 2018-11-7 15:36
 * @Description 蓝牙管理类
 */
public class BluetoothManage {

    //作为一个全局单例，管理所有BLE设备的连接。
    private BluetoothClient bluetoothClient;

    public BluetoothClient getBluetoothClient() {
        return bluetoothClient;
    }

    private static BluetoothManage instance;


    /**
     * 是否支持ble 蓝牙
     *
     * @return boolean true :支持 false:不支持
     * @Date 2018-11-07
     */
    public boolean isSupport() {
        boolean bleSupported = bluetoothClient.isBleSupported();
        if (bleSupported) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设备蓝牙是否打开
     *
     * @return boolean
     * @Date 2018-11-07
     */
    public boolean isOpen() {
        boolean bluetoothOpened = bluetoothClient.isBluetoothOpened();
        if (bluetoothOpened) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 搜索蓝牙设备
     * <br>
     * 每次搜索的时候都要重新生成对应的request
     * 不可复用
     * </br>
     *
     * @param response 搜索蓝牙回调
     * @return void
     * @Date 2018-11-13
     */
    public void scanBluetooth(SearchResponse response) {
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(1000 * 30, 3)   // 先扫BLE设备3次，每次30s
                //.searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
                //.searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build();
        bluetoothClient.search(request,response);
    }


    private BluetoothManage() {
        bluetoothClient = new BluetoothClient(BluetoothApplication.getInstance());
    }

    public static BluetoothManage getInstance() {
        if (null == instance) {
            synchronized (BluetoothManage.class) {
                if (null == instance) {
                    instance = new BluetoothManage();
                }
            }
        }
        return instance;
    }
}
