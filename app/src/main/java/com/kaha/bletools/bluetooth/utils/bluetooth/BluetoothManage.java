package com.kaha.bletools.bluetooth.utils.bluetooth;

import android.util.Log;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.kaha.bletools.bluetooth.utils.ByteAndStringUtil;
import com.kaha.bletools.framework.base.BluetoothApplication;

import java.util.UUID;

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
     * 判断设备是否已经连接
     *
     * @param mac 蓝牙地址
     * @return boolean true:已经连接  false 断开连接
     * @date 2019-02-13
     */
    public Boolean isConnect(String mac) {
        int status = bluetoothClient.getConnectStatus(mac);
        if (Constants.STATUS_DEVICE_CONNECTED == status) {
            return true;
        } else {
            return false;
        }
    }


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
     * 注册连接监听
     *
     * @param mac                       蓝牙mac地址
     * @param mBleConnectStatusListener 蓝牙状态连接监听
     * @return void
     * @date 2019-01-23
     */
    public void registerConnection(String mac, BleConnectStatusListener mBleConnectStatusListener) {
        bluetoothClient.registerConnectStatusListener(mac, mBleConnectStatusListener);
    }

    /**
     * @param mac                       蓝牙mac地址
     * @param mBleConnectStatusListener 蓝牙连接监听
     * @return void
     * @date 2019-01-23
     */
    public void unRegistConnection(String mac, BleConnectStatusListener mBleConnectStatusListener) {
        bluetoothClient.unregisterConnectStatusListener(mac, mBleConnectStatusListener);
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
     * 读取数据
     *
     * @param mac           mac地址
     * @param serviceUuid   服务
     * @param characterUuid 特征
     * @return void
     * @date 2019-02-15
     */
    public void readData(String mac, UUID serviceUuid, UUID characterUuid, BleReadResponse readResponse) {
        bluetoothClient.read(mac, serviceUuid, characterUuid, readResponse);
    }

    /**
     * 读取数据
     *
     * @param mac           mac地址
     * @param serviceUuid   服务
     * @param characterUuid 特征
     * @return String 读取到的字符串
     * @date 2019-02-15
     */
    public String readData(String mac, UUID serviceUuid, UUID characterUuid) {
        final String[] result = new String[1];
        readData(mac, serviceUuid, characterUuid, new BleReadResponse() {
            @Override
            public void onResponse(int code, byte[] data) {
                String hexString = ByteAndStringUtil.bytesToHexString(data);
                result[0] = ByteAndStringUtil.hexStringToString(hexString);
            }
        });
        return result[0];
    }


    /**
     * 打开通知
     *
     * @param mac           蓝牙mac地址
     * @param serviceUuid   要通知的服务的uuid
     * @param characterUuid 特征的uuid
     * @param response      回调
     * @return void
     * @date 2019-02-14
     */
    public void openNotify(String mac, UUID serviceUuid, UUID characterUuid, BleNotifyResponse response) {
        bluetoothClient.notify(mac, serviceUuid, characterUuid, response);
    }

    /**
     * 关闭通知
     *
     * @param mac 蓝牙mac地址
     * @return void
     * @date 2019-01-23
     */
    public void closeNotify(String mac, UUID serviceUuid, UUID characterUuid) {
        bluetoothClient.unnotify(mac, serviceUuid, characterUuid, new BleUnnotifyResponse() {
            @Override
            public void onResponse(int code) {

            }
        });
    }

    /**
     * 写入命令
     *
     * @param mac           蓝牙mac地址
     * @param characterUuid 特征
     * @param serviceUuid   服务
     * @param command       命令
     * @return void
     * @date 2019-02-14
     */
    public void writeCommand(String mac, UUID serviceUuid, UUID characterUuid, String command, BleWriteResponse response) {
        String regex = "^[A-Fa-f0-9]+$";
        if (command.matches(regex)) {
            //是十六进制
            bluetoothClient.write(mac, serviceUuid, characterUuid,
                    ByteAndStringUtil.hexStringToByte(command), response);
        } else {
            //是文本
            byte[] bytes = ByteAndStringUtil.hexStringToByte(ByteAndStringUtil.str2HexStr(command));
            bluetoothClient.write(mac, serviceUuid, characterUuid,
                    bytes, response);
        }
    }

    /**
     * 连接蓝牙设备
     *
     * @param mac             蓝牙mac地址
     * @param connectResponse 连接回调
     * @return void
     * @date 2019-01-23
     */
    public void connectionRequest(String mac, BleConnectResponse connectResponse) {
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(0)   // 连接如果失败重试1次
                .setConnectTimeout(30000)   // 连接超时30s
                .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
                .setServiceDiscoverTimeout(20000)  // 发现服务超时20s
                .build();
        bluetoothClient.connect(mac, options, connectResponse);
    }

    /**
     * 断开连接
     *
     * @param mac 蓝牙mac地址
     * @return void
     * @date 2019-01-23
     */
    public void disConnection(String mac) {
        bluetoothClient.disconnect(mac);
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
        bluetoothClient.search(request, response);
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
