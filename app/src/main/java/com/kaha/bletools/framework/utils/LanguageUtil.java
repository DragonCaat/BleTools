package com.kaha.bletools.framework.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * app语言设置
 *
 * @author Darcy
 * @Date 2019/2/12
 * @package com.kaha.bletools.framework.utils
 * @Desciption
 */
public class LanguageUtil {

    /**
     * 设置APP语言
     *
     * @param context c
     * @return void
     * @date 2019-02-12
     */
    public static void setLanguage(Context context) {
        //获取系统当前的语言
        String able = context.getResources().getConfiguration().locale.getLanguage();
        Resources resources = context.getResources();//获得res资源对象
        Configuration config = resources.getConfiguration();//获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();
        //根据系统语言进行设置
        if (able.equals("zh")) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
            resources.updateConfiguration(config, dm);

        } else if (able.equals("en")) {
            config.locale = Locale.US;
            resources.updateConfiguration(config, dm);
        }
    }
}
