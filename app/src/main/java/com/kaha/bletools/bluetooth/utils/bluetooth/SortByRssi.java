package com.kaha.bletools.bluetooth.utils.bluetooth;

import com.inuker.bluetooth.library.search.SearchResult;
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
        SearchResult s1 = (SearchResult) o1;
        SearchResult s2 = (SearchResult) o2;
        if (s1.rssi < s2.rssi)
            return 1;
        if (s1.rssi == s2.rssi)
            return 0;
        return -1;
    }
}
