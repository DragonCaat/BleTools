package com.kaha.bletools.bluetooth.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 虎丘十六进制时间
 *
 * @author Darcy
 * @Date 2019/5/16
 * @package com.kaha.bletools.bluetooth.utils
 * @Desciption
 */
public class TimeUtil {


    /**
     * 获取当前的日期
     *
     * @return String 当前的日期
     * @Date 2018-11-20
     */
    public static String getCurrentDate() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat Formate = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");// HH:mm:ss
        // 获取当前时间
        Date date = new Date(System.currentTimeMillis());
        String currentTime = Formate.format(date);
        return currentTime;
    }


    public static String getHexTime() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());

        String format = simpleDateFormat.format(date);
        String[] temp;
        temp = format.split("-");
        //Log.i("hello", "getTime: "+format);
        String first = temp[0];
        first = first.substring(2, 4);

        String HexString = intToHex(20) + intToHex(Integer.parseInt(first));
        for (int i = 1; i < 6; i++) {
            String s = intToHex(Integer.parseInt(temp[i]));
            String s1;
            if (s.length() == 1)
                s1 = "0" + s;
            else
                s1 = s;

            HexString = HexString + s1;
        }

        return HexString;
    }

    /**
     * 获取手机的时区信息
     * <p>
     * anyway u must be rich enough
     */
    public static String getHexTimeZone() {
        String displayName = TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
        String[] temp;
        temp = displayName.split("T");
        String TimeZone = temp[1];
        String[] temp1 = TimeZone.split(":");
        String startTimeZone = temp1[0];
        String first1 = startTimeZone;

        String start1 = first1.substring(1, 3);

        String s1 = intToHex(Integer.parseInt(start1));
        String s2;
        if (s1.length() == 1)
            s2 = "0" + s1;
        else
            s2 = s1;
        String HexString = str2HexStr("+") + s2;
        String endTimeZone = temp1[1];
        String s = intToHex(Integer.parseInt(endTimeZone));

        if ("00".equals(endTimeZone))
            HexString = HexString + "AA";
        else
            HexString = HexString + s;
        return HexString;
    }

    /**
     * 字符串转换成为16进制(无需Unicode编码)
     *
     * @param str
     * @return
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            // sb.append(' ');
        }
        return sb.toString().trim();
    }


    /**
     * 十进制转换成十六进制
     *
     * @param n 十进制数字
     */
    public static String intToHex(int n) {
        //StringBuffer s = new StringBuffer();
        StringBuilder sb = new StringBuilder(8);
        String a;
        char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        while (n != 0) {
            sb = sb.append(b[n % 16]);
            n = n / 16;
        }
        a = sb.reverse().toString();
        return a;
    }
}
