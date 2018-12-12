package com.kaha.bletools.framework.base;

import android.support.multidex.MultiDexApplication;

import com.kaha.bletools.bluetooth.utils.SPUtil;

/**
 * @author : Darcy
 * @package com.kaha.bletools.framework.base
 * @Date 2018-11-7 15:43
 * @Description 运用的application
 */
public class BluetoothApplication extends MultiDexApplication {
    private static BluetoothApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //初始化sharePreference
        SPUtil.getInstance().init();

    }

    public static BluetoothApplication getInstance() {
        return instance;
    }
}
