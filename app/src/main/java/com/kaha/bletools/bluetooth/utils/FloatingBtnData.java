package com.kaha.bletools.bluetooth.utils;

import android.content.Context;
import android.util.Log;

import com.kaha.bletools.R;
import com.kaha.bletools.bluetooth.base.AppConst;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 浮动按钮的item
 *
 * @author Darcy
 * @Date 2019/2/14
 * @package com.kaha.bletools.bluetooth.utils
 * @Desciption
 */
public class FloatingBtnData {
    private static List<RFACLabelItem> items;

    public static List<RFACLabelItem> getFloatingData(Context context) {
        items = new ArrayList<>(5);
        items.add(new RFACLabelItem<Integer>()
                .setLabel(context.getString(R.string.wipe_data))
                .setResId(R.mipmap.clean_white)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setLabelColor(0xffd84315)
                .setLabelBackgroundDrawable(context.getDrawable(R.drawable.rectangle_transparent))
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel(context.getString(R.string.export_text))
                .setResId(R.mipmap.output_white)
                .setIconNormalColor(0xff4e342e)
                .setIconPressedColor(0xff3e2723)
                .setLabelColor(0xff4e342e)
                .setLabelSizeSp(14)
                .setLabelBackgroundDrawable(context.getDrawable(R.drawable.rectangle_transparent))
                .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel(context.getString(R.string.analysis_data))
                .setResId(R.mipmap.analysis_data)
                .setIconNormalColor(0xff056f00)
                .setIconPressedColor(0xff0d5302)
                .setLabelColor(0xff056f00)
                .setLabelBackgroundDrawable(context.getDrawable(R.drawable.rectangle_transparent))
                .setWrapper(2)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel(context.getString(R.string.database))
                .setResId(R.mipmap.database)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffccb693)
                .setLabelColor(0xffccb693)
                .setLabelBackgroundDrawable(context.getDrawable(R.drawable.rectangle_transparent))
                .setWrapper(3)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel(context.getString(R.string.dfu_update))
                .setResId(R.mipmap.update)
                .setIconNormalColor(0xffccb793)
                .setIconPressedColor(0xffccb693)
                .setLabelColor(0xffccb693)
                .setLabelBackgroundDrawable(context.getDrawable(R.drawable.rectangle_transparent))
                .setWrapper(3)
        );

        return items;

    }
}
