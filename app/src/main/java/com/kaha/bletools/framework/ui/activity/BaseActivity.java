package com.kaha.bletools.framework.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.kaha.bletools.framework.utils.PermissionHelper;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author : Darcy
 * @package com.kaha.bletools
 * @Date 2018-11-7 11:37
 * @Description 所有activity的基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Context context;
    protected Activity activity;

    private Unbinder unbinder;

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
}
