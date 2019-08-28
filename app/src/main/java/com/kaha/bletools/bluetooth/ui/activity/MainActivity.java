package com.kaha.bletools.bluetooth.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.kaha.bletools.R;
import com.kaha.bletools.bluetooth.base.AppConst;
import com.kaha.bletools.bluetooth.ui.activity.SearchActivity;
import com.kaha.bletools.bluetooth.utils.FileUtil;
import com.kaha.bletools.bluetooth.utils.SPUtil;
import com.kaha.bletools.bluetooth.utils.bluetooth.BluetoothManage;
import com.kaha.bletools.bluetooth.widget.ConfirmPopWindow;
import com.kaha.bletools.framework.ui.activity.BaseActivity;
import com.kaha.bletools.framework.utils.PermissionHelper;
import com.kaha.bletools.framework.utils.ToastUtil;
import com.kaha.bletools.framework.widget.CommonTopView;
import com.kaha.bletools.litepal.FileCommand;
import com.kaha.bletools.litepal.LitePalManage;

import org.litepal.crud.DataSupport;

import java.util.List;

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

    @BindView(R.id.btn_use_mode)
    Button btnUseMode;

    @BindView(R.id.btn_rssi_mode)
    Button btnRssiMode;

    @BindView(R.id.tv_kaha)
    ImageView ivKaha;

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

        updateUi();
        topView.setTitle("卡哈科技");
        checkSupport();
        checkBluetoothOpened();
        permissionHelper = new PermissionHelper(this);
        permissionHelper.requestPermission(permissionCallBack,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    //检查状态，更新ui
    private void updateUi() {
        USE_MODE = SPUtil.getInstance().getInt(AppConst.USE_MODE, 0);
        if (USE_MODE == 0) {
            btnUseMode.setText(R.string.private_string);
        } else {
            btnUseMode.setText(R.string.public_string);
        }
        //信号值
        RSSI_MODE = SPUtil.getInstance().getInt(AppConst.RSSI_MODEL, 0);
        if (RSSI_MODE == 0) {
            btnRssiMode.setText(R.string.normal_model);
        } else {
            btnRssiMode.setText(R.string.rssi_model);
        }
    }

    @OnClick({R.id.rl_right_image, R.id.fb_search,
            R.id.btn_file_command, R.id.btn_use_mode, R.id.btn_rssi_mode})
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

            //从文件中读取命令
            case R.id.btn_file_command:
                checkFileCommand();
                break;
            //选择工作模式
            case R.id.btn_use_mode:
                if (USE_MODE == 0) {
                    USE_MODE = 1;
                    btnUseMode.setText(R.string.public_string);
                } else {
                    USE_MODE = 0;
                    btnUseMode.setText(R.string.private_string);
                }
                SPUtil.getInstance().putInt(AppConst.USE_MODE, USE_MODE);
                break;

            //信号值测试模式
            case R.id.btn_rssi_mode:
                if (RSSI_MODE == 0) {
                    RSSI_MODE = 1;
                    btnRssiMode.setText(R.string.rssi_model);
                } else {
                    RSSI_MODE = 0;
                    btnRssiMode.setText(R.string.normal_model);
                }
                SPUtil.getInstance().putInt(AppConst.RSSI_MODEL, RSSI_MODE);
                break;
        }
    }

    private int USE_MODE = 0; //0:私有 1:公有

    //信号值值测试模式
    private int RSSI_MODE = 0; //0:私有 1:公有

    //检查本地的命令文件
    private void checkFileCommand() {
        //请求写入文件权限
        permissionHelper.requestPermission(new PermissionHelper.PermissionCallBack() {
            @Override
            public void onPermissionGrant(String... permission) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }

            @Override
            public void onPermissionDenied(String... permission) {
                ToastUtil.show(context, R.string.permission_denied);
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                final String path = FileUtil.getPath(context, uri);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<String> fileCommandList = FileUtil.getFileCommand(context, path);
                        if (fileCommandList.size() > 0) {

                            for (String s : fileCommandList) {
                                Log.i("hello", "run: " + s + "\n");
                            }

                            //先将数据库中数据删除
                            DataSupport.deleteAll(FileCommand.class);
                            for (int i = 0; i < fileCommandList.size(); i++) {
                                String[] fileCommand = fileCommandList.get(i).trim().split("-");

//                                Log.i("hello", "run1: " + fileCommandList.get(i).trim());
//                                Log.i("hello", "run: " + fileCommand[0] + "####" + fileCommand[1]);

                                LitePalManage.getInstance().saveFileCommand(fileCommand[1], fileCommand[0]);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show(context, "命令已经缓存到本地");
                            }
                        });
                    }
                }).start();
            }
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
                isOpenBluetooth = false;
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
