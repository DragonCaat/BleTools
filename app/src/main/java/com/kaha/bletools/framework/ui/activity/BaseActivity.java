package com.kaha.bletools.framework.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.kaha.bletools.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基类
 *
 * @author : Darcy
 * @package com.kaha.bletools
 * @Date 2018-11-7 11:37
 * @Description 所有activity的基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Context context;
    protected AppCompatActivity activity;

    private Unbinder unbinder;

    private static Dialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        context = this;
        activity = this;
        setContentView(setLayoutId());
        unbinder = ButterKnife.bind(this);
    }

    /**
     * 返回activity的布局
     *
     * @return int 布局的id
     * @date 2018-11-07
     */
    protected abstract int setLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }

    /**
     * 跳转界面
     *
     * @param cls 要跳转的activity
     */
    public void skipPage(Class<? extends Activity> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    /**
     * 显示加载框
     *
     * @return void
     * @Date 2018-11-19
     */
    public void showProgressDialog(String s) {
        if (progressDialog != null)
            progressDialog = null;
        progressDialog = new Dialog(context, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.layout_progress);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.gray_f3f3f3);
        TextView msg = progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText(s);

        progressDialog.show();

        //当dialog被dismiss的时候停止连接
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //如果正在连接则取消连接
                //mClient.disconnection(macStr);
            }
        });
        progressDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 隐藏搜索框
     *
     * @return void
     * @Date 2018-11-19
     */
    public void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    /**
     * 根据手机的分辨率从px(像素)的单位转成dp
     *
     * @param pxValue 要转换的像素值
     */
    public int px2dip(float pxValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从dp的单位转成px(像素)
     *
     * @param dpValue 要转换的dp值
     */
    public int dip2px(int dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
