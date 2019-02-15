package com.kaha.bletools.framework.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;

import com.kaha.bletools.bluetooth.utils.SPUtil;
import com.kaha.bletools.framework.utils.LanguageUtil;

import org.litepal.LitePal;

import java.util.Locale;

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

        //初始化数据库
        LitePal.initialize(this);

        LanguageUtil.setLanguage(this);
    }

    public static BluetoothApplication getInstance() {
        return instance;
    }
}
