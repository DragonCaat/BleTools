package com.kaha.bletools.bluetooth.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaha.bletools.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 选择蓝牙的布局
 *
 * @author : Darcy
 * @package com.kaha.bletools.framework.widget
 * @Date 2018-11-7 11:55
 * @Description
 */
public class BleSelectView extends RelativeLayout {

    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;

    public BleSelectView(Context context) {
        super(context);
    }

    public BleSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout
                .layout_ble_select, this);
        ButterKnife.bind(this, view);
        //引入自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BleSelectView);
        tvContent.setText(ta.getString(R.styleable.BleSelectView_contentText));
        ivIcon.setImageResource(ta.getResourceId(R.styleable.BleSelectView_image, -1));
        ta.recycle();
    }

    public BleSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置内容
     *
     * @param content 要设置的内容
     * @return void
     * @date 2019-02-12
     */
    public void setTvContent(String content) {
        tvContent.setText(content);
    }

    /**
     * 获取内容的字符
     *
     * @param ，
     * @return String 内容的字符
     * @date 2019-02-13
     */
    public String getContentString() {
        return tvContent.getText().toString().trim();
    }


    /**
     * 设置图标
     *
     * @param imageId 图片的id
     * @return void
     * @date 2019-02-12
     */
    public void setIvIcon(int imageId) {
        ivIcon.setImageResource(imageId);
    }

}
