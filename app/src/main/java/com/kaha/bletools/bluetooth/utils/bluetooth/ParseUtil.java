package com.kaha.bletools.bluetooth.utils.bluetooth;

/**
 * 解析数据的工具类
 *
 * @author Darcy
 * @Date 2019/6/21
 * @package com.kaha.bletools.bluetooth.utils.bluetooth
 * @Desciption
 */
public class ParseUtil {


    /**
     * 解析有效的内容
     *
     * @param data 硬件返回的数据
     * @return String 解析到后的有效数据
     * @date 2019-06-21
     */
    public static String parseFirstData(String data) {
        String result;

        result = data.substring(12, data.length());

        return result;
    }

    /**
     * 解析有效的内容
     *
     * @param data 硬件返回的数据
     * @return String 解析到后的有效数据
     * @date 2019-06-21
     */
    public static String parseContentData(String data) {
        String result;

        result = data.substring(6, data.length());

        return result;
    }


}
