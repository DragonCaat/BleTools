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


    /**
     * 判断是否是十六进制
     *
     * @param str
     * @return boolean
     * @date 2019-02-14
     */
    public static boolean isHexNumber(String str) {
        boolean flag = false;
        for (int i = 0; i < str.length(); i++) {
            char cc = str.charAt(i);
            if (cc == '0' || cc == '1' || cc == '2' || cc == '3' || cc == '4' || cc == '5' || cc == '6' || cc == '7' || cc == '8' || cc == '9' || cc == 'A' || cc == 'B' || cc == 'C' ||
                    cc == 'D' || cc == 'E' || cc == 'F' || cc == 'a' || cc == 'b' || cc == 'c' || cc == 'c' || cc == 'd' || cc == 'e' || cc == 'f') {
                flag = true;
            }
        }
        return flag;
    }

}
