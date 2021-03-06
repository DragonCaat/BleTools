package com.kaha.bletools.framework.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaha.bletools.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author : Darcy
 * @package com.kaha.bletools.framework.widget
 * @Date 2018-11-7 11:55
 * @Description activity的头布局
 */
public class CommonTopView extends RelativeLayout {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_right_image)
    RelativeLayout rlRight;
    @BindView(R.id.iv_right_image)
    ImageView ivRightImage;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.iv_left_image)
    ImageView ivLeftImage;

    @BindView(R.id.tv_right_text)
    TextView tvRightText;

    //statueBar占位
    @BindView(R.id.view)
    View view;

    public CommonTopView(Context context) {
        super(context);
    }

    public CommonTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout
                .layout_head, this);
        ButterKnife.bind(this, view);

    }

    public CommonTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @OnClick({R.id.rl_back, R.id.tv_right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                Context context = getContext();
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
                break;

            //右边按钮点击事件
            case R.id.tv_right_text:
                if (rightClickListener != null) {
                    rightClickListener.onRightClick(view);
                }
                break;
        }
    }

    /**
     * 设置左边的按钮是否可见
     *
     * @param isVis true 是可见 false 是不可见
     * @date 2018-11-07
     */
    public void setLeftVis(Boolean isVis) {
        if (isVis) {
            rlBack.setVisibility(VISIBLE);
        } else {
            rlBack.setVisibility(GONE);
        }
    }

    /**
     * 设置右边的文字是否可见
     *
     * @param isVis boolean
     * @return void
     * @date 2019-02-20
     */
    public void setRightTextVis(Boolean isVis) {
        if (isVis) {
            tvRightText.setVisibility(VISIBLE);
        } else {
            tvRightText.setVisibility(GONE);
        }
    }

    /**
     * 设置右边的文字
     *
     * @param text 要设置的文字
     * @return void
     * @date 2019-02-20
     */
    public void setRightText(String text) {
        tvRightText.setText(text);
    }

    /**
     * 设置左边的图片
     *
     * @param id 图片资源的id
     * @return void
     * @Date 2018-11-07
     */
    public void setLeftImage(int id) {
        ivLeftImage.setImageResource(id);
    }

    /**
     * 设置标题
     *
     * @param title 标题
     * @date 2018-11-07
     */
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    /**
     * 设置右边的图片
     *
     * @param id 图片的id
     * @date 2018-11-07
     */
    public void setRightImage(int id) {
        ivRightImage.setImageResource(id);
    }

    /**
     * 设置右边的布局可见
     *
     * @date 2018-11-07
     */
    public void setRightImageVis() {
        rlRight.setVisibility(VISIBLE);
    }


    private OnRightClickListener rightClickListener;

    public void setOnRightClickListener(OnRightClickListener listener) {
        this.rightClickListener = listener;
    }

    public interface OnRightClickListener {

        void onRightClick(View view);
    }
}
