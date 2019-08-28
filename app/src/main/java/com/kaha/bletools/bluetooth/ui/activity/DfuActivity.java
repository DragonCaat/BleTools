package com.kaha.bletools.bluetooth.ui.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchResult;
import com.kaha.bletools.R;
import com.kaha.bletools.bluetooth.base.AppConst;
import com.kaha.bletools.bluetooth.ui.service.DfuService;
import com.kaha.bletools.bluetooth.utils.bluetooth.BluetoothManage;
import com.kaha.bletools.framework.ui.activity.BaseActivity;
import com.kaha.bletools.framework.utils.ToastUtil;
import com.kaha.bletools.framework.widget.CommonTopView;

import butterknife.BindView;
import butterknife.OnClick;
import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuServiceController;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;

import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;
import static com.kaha.bletools.bluetooth.utils.FileUtil.getPath;

/**
 * 蓝牙升级的activity
 *
 * @author Darcy
 * @Date 2019/7/22
 * @package com.kaha.bletools.bluetooth.ui.activity
 * @Desciption
 */
public class DfuActivity extends BaseActivity {

    @BindView(R.id.topView)
    CommonTopView topView;
    @BindView(R.id.tv_update)
    TextView airUpgradeTv;
    @BindView(R.id.pb)
    ProgressBar proBar;

    @BindView(R.id.tv_device_name)
    TextView tvDeviceNmae;
    @BindView(R.id.tv_device_address)
    TextView tvAddress;

    @BindView(R.id.tv_connect_statues)
    TextView tvConnectStatues;

    private DfuServiceInitiator starter;

    private String mac;
    private String name;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_dfu;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topView.setTitle(getString(R.string.dfu_update));
        name = getIntent().getStringExtra(AppConst.KEY_1);
        mac = getIntent().getStringExtra(AppConst.KEY_2);

        starter = new DfuServiceInitiator(mac)
                .setDeviceName("Smart Lock")
                .setKeepBond(false)
                .setPacketsReceiptNotificationsEnabled(true)
                .setPacketsReceiptNotificationsValue(10);
        // If you want to have experimental buttonless DFU feature supported call additionally:
        //安卓8以下将通知关闭
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            starter.setForeground(false);
            starter.setDisableNotification(true);
        }
        starter.setUnsafeExperimentalButtonlessServiceInSecureDfuEnabled(true);
        //setDeviceName为设备过滤条件 setzip传入raw文件的路径即可
        initDeviceInfo();
    }


    @OnClick({R.id.tv_select_file})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_file:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                break;
        }
    }

    String path;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri == null)
                return;
            if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                path = uri.getPath();
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = getPath(this, uri);
            }

            if (TextUtils.isEmpty(path)){
                ToastUtil.show(context,R.string.file_path_invalid);
                return;
            }
            starter.setZip(path);
            starter.start(this, DfuService.class); //启动升级服务
            proBar.setVisibility(View.VISIBLE);
        }
    }


    //初始化设备的信息
    private void initDeviceInfo() {
        tvDeviceNmae.setText(name);
        tvAddress.setText(mac);
        int connectStatus = BluetoothManage.getInstance().getBluetoothClient().getConnectStatus(mac);
        if (connectStatus ==Constants.STATUS_DEVICE_CONNECTED){
            tvConnectStatues.setText(R.string.connection);
            tvConnectStatues.setTextColor(getResources().getColor(R.color.green_53ce78));
        }else {
            tvConnectStatues.setText(R.string.disconnection);
            tvConnectStatues.setTextColor(getResources().getColor(R.color.red_ff4c4c));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        DfuServiceListenerHelper.registerProgressListener(this, mDfuProgressListener); //监听升级进度

        BluetoothManage.getInstance().registerConnection(mac,mBleConnectStatusListener);
    }

    //蓝牙连接状态监听
    private final BleConnectStatusListener mBleConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == STATUS_CONNECTED) {
                tvConnectStatues.setText(R.string.connection);
                tvConnectStatues.setTextColor(getResources().getColor(R.color.green_53ce78));
            } else if (status == STATUS_DISCONNECTED) {
                tvConnectStatues.setText(R.string.disconnection);
                tvConnectStatues.setTextColor(getResources().getColor(R.color.red_ff4c4c));
            }
        }
    };




    /**
     * dfu升级监听
     */
    private final DfuProgressListener mDfuProgressListener = new DfuProgressListener() {
        @Override
        public void onDeviceConnecting(String deviceAddress) {
            proBar.setIndeterminate(true);
            airUpgradeTv.setText(R.string.dfu_status_connecting);
        }

        @Override
        public void onDeviceConnected(String deviceAddress) {

        }


        @Override
        public void onDfuProcessStarting(String deviceAddress) {
            proBar.setIndeterminate(true);
            airUpgradeTv.setText(R.string.dfu_status_starting);
        }

        @Override
        public void onDfuProcessStarted(String deviceAddress) {

        }

        @Override
        public void onEnablingDfuMode(String deviceAddress) {
            proBar.setIndeterminate(true);
            airUpgradeTv.setText(R.string.dfu_status_switching_to_dfu);
        }

        @Override
        public void onProgressChanged(String deviceAddress, int percent, float speed, float avgSpeed, int currentPart, int partsTotal) {
            proBar.setProgress(percent);
            proBar.setIndeterminate(false);
            airUpgradeTv.setText(percent + "%");
        }

        @Override
        public void onFirmwareValidating(String deviceAddress) {
            proBar.setIndeterminate(true);
            airUpgradeTv.setText(R.string.dfu_status_validating);
        }

        @Override
        public void onDeviceDisconnecting(String deviceAddress) {
           // Log.i("dfu", "onDeviceDisconnecting");
        }

        @Override
        public void onDeviceDisconnected(String deviceAddress) {
            proBar.setIndeterminate(true);
            airUpgradeTv.setText(R.string.dfu_status_disconnecting);

        }

        @Override
        public void onDfuCompleted(String deviceAddress) {
            proBar.setVisibility(View.GONE);
            ToastUtil.show(context, "升级成功");
            airUpgradeTv.setText(R.string.dfu_status_completed);
            proBar.setProgress(100);
            proBar.setIndeterminate(false);
            //升级成功
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // if this activity is still open and upload process was completed, cancel the notification
                    final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(DfuService.NOTIFICATION_ID);


                }
            }, 200);
            BluetoothManage.getInstance().connectionRequest(mac, new BleConnectResponse() {
                @Override
                public void onResponse(int code, BleGattProfile data) {

                }
            });
        }

        @Override
        public void onDfuAborted(String deviceAddress) {
            proBar.setVisibility(View.GONE);
            //升级流产，失败
            airUpgradeTv.setText(R.string.dfu_status_aborted);
            // let's wait a bit until we cancel the notification. When canceled immediately it will be recreated by service again.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(DfuActivity.this, "升级出错", Toast.LENGTH_SHORT).show();
                    // if this activity is still open and upload process was completed, cancel the notification
                    final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(DfuService.NOTIFICATION_ID);
                }
            }, 200);

        }

        @Override
        public void onError(String deviceAddress, int error, int errorType, String message) {
            airUpgradeTv.setText(getString(R.string.dfu_status_aborted));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // if this activity is still open and upload process was completed, cancel the notification
                    final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(DfuService.NOTIFICATION_ID);

                }
            }, 200);

        }
    };

    @Override
    protected void onPause() {//取消监听
        DfuServiceListenerHelper.unregisterProgressListener(this, mDfuProgressListener);
        BluetoothManage.getInstance().unRegistConnection(mac,mBleConnectStatusListener);
        super.onPause();
    }
}
