package com.kaha.bletools.framework.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author : Darcy
 * @package com.kaha.bletools.framework
 * @Date 2018-11-7 11:51
 * @Description 所有fragment的基类
 */
public abstract class BaseFragment extends Fragment {

    protected Context context;
    protected Activity activity;

    private Unbinder unbinder;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    protected abstract int getLayoutId();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    /**
     * 跳转界面
     *
     * @param cls 要跳转的activity
     */
    public void skipPage(Class<? extends Activity> cls) {
        Intent intent = new Intent(activity, cls);
        startActivity(intent);
    }
}
