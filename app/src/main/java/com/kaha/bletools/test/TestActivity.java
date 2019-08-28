package com.kaha.bletools.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kaha.bletools.R;
import com.kaha.bletools.framework.ui.activity.BaseActivity;

/**
 * @author Darcy
 * @Date 2019/7/19
 * @package com.kaha.bletools.test
 * @Desciption
 */
public class TestActivity extends BaseActivity {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String d = "ATMTLG=+274-sdg";

        String[] split = d.split("-");

        Log.i("hello", "onCreate: "+split[0]+"-----"+split[1]);

    }

}
