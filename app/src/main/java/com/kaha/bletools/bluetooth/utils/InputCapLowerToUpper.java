package com.kaha.bletools.bluetooth.utils;

import android.text.method.ReplacementTransformationMethod;

/**
 * 小写变大写
 *
 * @author Darcy
 * @Date 2019/3/19
 * @package com.kaha.bletools.bluetooth.utils
 * @Desciption
 */
public class InputCapLowerToUpper extends ReplacementTransformationMethod {
    @Override
    protected char[] getOriginal() {
        char[] lower = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        return lower;
    }

    @Override
    protected char[] getReplacement() {
        char[] upper = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        return upper;
    }
}