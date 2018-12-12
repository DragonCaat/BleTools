package com.kaha.bletools.bluetooth.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.kaha.bletools.R;
import com.kaha.bletools.framework.utils.ToastUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author : Darcy
 * @package com.kaha.bletools.bluetooth.widget
 * @Date 2018-11-8 16:47
 * @Description 第一个页面的自定义popWindow
 */
public class ConfirmPopWindow extends PopupWindow {
    private Context context;
//    @BindView(R.id.ll_about_our)
//    LinearLayout llAboutOur;
//    @BindView(R.id.ll_current_version)
//    LinearLayout llCurrentOur;

    public ConfirmPopWindow(Context context) {
        super(context);
        this.context = context;
        initalize();
    }

    private void initalize() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pop_window_layout, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        initWindow();
    }

    private void initWindow() {
        DisplayMetrics d = context.getResources().getDisplayMetrics();
        this.setWidth((int) (d.widthPixels * 0.35));
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        backgroundAlpha((Activity) context, 0.9f);//0.0-1.0
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha((Activity) context, 1f);
            }
        });
    }

    //设置添加屏幕的背景透明度
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    public void showAtBottom(View view) {
        //弹窗位置设置
        showAsDropDown(view, Math.abs((view.getWidth() - getWidth()) / 2), 0);
        //showAtLocation(view, Gravity.TOP | Gravity.RIGHT, 10, 110);//有偏差
    }

    @OnClick({R.id.ll_about_our, R.id.ll_current_version})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_current_version:
                ToastUtil.show(context,"当前版本");
                break;
            case R.id.ll_about_our:
                ToastUtil.show(context,"关于我们");
                break;
            default:
                break;
        }
    }

}

