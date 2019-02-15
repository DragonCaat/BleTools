package com.kaha.bletools.bluetooth.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.kaha.bletools.R;
import com.kaha.bletools.bluetooth.ui.activity.SearchActivity;
import com.kaha.bletools.bluetooth.utils.bluetooth.BluetoothManage;
import com.kaha.bletools.bluetooth.widget.ConfirmPopWindow;
import com.kaha.bletools.framework.ui.activity.BaseActivity;
import com.kaha.bletools.framework.utils.PermissionHelper;
import com.kaha.bletools.framework.utils.ToastUtil;
import com.kaha.bletools.framework.widget.CommonTopView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author : Darcy
 * @package com.kaha.bletools.bluetooth
 * @Date 2018-11-7 14:24
 * @Description 主界面
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.toView)
    CommonTopView topView;
    //是否支持ble蓝牙
    @BindView(R.id.tv_support_statues)
    TextView tvSupportStatues;
    //蓝牙是否打开
    @BindView(R.id.tv_bluetooth_statues)
    TextView tvBluetoothStatues;
    @BindView(R.id.rl_right_image)
    RelativeLayout rlRightImage;
    //动态请求权限类
    private PermissionHelper permissionHelper;

    private boolean isOpenBluetooth;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topView.setLeftVis(false);
        topView.setRightImageVis();
        topView.setRightImage(R.mipmap.right_menu);

        checkSupport();
        checkBluetoothOpened();

        permissionHelper = new PermissionHelper(this);

        permissionHelper.requestPermission(permissionCallBack, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @OnClick({R.id.rl_right_image, R.id.fb_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_right_image:
                showPopWindow();
                break;

            //搜索按钮
            case R.id.fb_search:
                checkSupport();
                if (isOpenBluetooth) {
                    skipPage(SearchActivity.class);
                } else {
                    //打开蓝牙
                    BluetoothManage.getInstance().getBluetoothClient().openBluetooth();
                    BluetoothManage.getInstance().getBluetoothClient().
                            registerBluetoothStateListener(mBluetoothStateListener);
                }
                break;
        }
    }

    /**
     * 蓝牙是否打开的监听回调
     *
     * @date 2018-11-09
     */
    private final BluetoothStateListener mBluetoothStateListener = new BluetoothStateListener() {
        @Override
        public void onBluetoothStateChanged(boolean openOrClosed) {
            if (openOrClosed) {
                tvBluetoothStatues.setText("on");
                isOpenBluetooth = true;

                skipPage(SearchActivity.class);
            } else {
                isOpenBluetooth= false;
                ToastUtil.show(context, "蓝牙未打开");
            }
        }

    };

    //检查是否支持ble蓝牙
    @SuppressLint("SetTextI18n")
    private void checkSupport() {
        boolean support = BluetoothManage.getInstance().isSupport();
        if (support) {
            tvSupportStatues.setTextColor(Color.GRAY);
            tvSupportStatues.setText("support");
        } else {
            tvSupportStatues.setTextColor(Color.RED);
            tvSupportStatues.setText("unSupport");
        }
    }
    //检查蓝牙是否打开
    @SuppressLint("SetTextI18n")
    private void checkBluetoothOpened() {
        boolean isOpen = BluetoothManage.getInstance().isOpen();
        if (isOpen) {
            tvBluetoothStatues.setText("on");
            isOpenBluetooth = true;
        } else {
            tvBluetoothStatues.setText("off");
            isOpenBluetooth = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    //请求权限回调
    PermissionHelper.PermissionCallBack permissionCallBack = new PermissionHelper.PermissionCallBack() {
        @Override
        public void onPermissionGrant(String... permission) {

        }

        @Override
        public void onPermissionDenied(String... permission) {

        }
    };

    /**
     * 展示popWindow
     *
     * @return void
     * @Date 2018-11-08
     */
    private void showPopWindow() {
        new ConfirmPopWindow(context).showAtBottom(rlRightImage);
    }

}
