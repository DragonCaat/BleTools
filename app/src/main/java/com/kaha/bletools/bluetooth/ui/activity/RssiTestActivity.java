package com.kaha.bletools.bluetooth.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleReadRssiResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.kaha.bletools.R;
import com.kaha.bletools.bluetooth.base.AppConst;
import com.kaha.bletools.bluetooth.entity.OutputData;
import com.kaha.bletools.bluetooth.entity.RssiData;
import com.kaha.bletools.bluetooth.ui.adapter.RssiAdapter;
import com.kaha.bletools.bluetooth.utils.FileUtil;
import com.kaha.bletools.bluetooth.utils.TimeUtil;
import com.kaha.bletools.bluetooth.utils.bluetooth.BluetoothManage;
import com.kaha.bletools.framework.ui.activity.BaseActivity;
import com.kaha.bletools.framework.utils.PermissionHelper;
import com.kaha.bletools.framework.utils.ToastUtil;
import com.kaha.bletools.framework.widget.CommonTopView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.login.LoginException;

import butterknife.BindView;
import butterknife.OnClick;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;

/**
 * 信号值实时监测的activity
 *
 * @author Darcy
 * @Date 2019/8/27
 * @package com.kaha.bletools.bluetooth.ui.activity
 * @Desciption
 */
public class RssiTestActivity extends BaseActivity {

    @BindView(R.id.topView)
    CommonTopView commonTopView;
    @BindView(R.id.rssi_recycleView)
    RecyclerView rssiRecycleView;

    @BindView(R.id.tv_mac)
    TextView tvMac;
    @BindView(R.id.tv_statues)
    TextView tvStatues;

    @BindView(R.id.tv_total_item)
    TextView tvTotalItem;

    private String mac;
    private String name;

    //信号值适配器
    private RssiAdapter rssiAdapter;
    //权限请求帮助类
    private PermissionHelper permissionHelper;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_rssi_test;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getIntent().getStringExtra(AppConst.KEY_1);
        mac = getIntent().getStringExtra(AppConst.KEY_2);
        commonTopView.setTitle(name);
        tvMac.setText(mac);
        initRecycleView();
        connection();
        permissionHelper = new PermissionHelper(this);

        commonTopView.setRightText("连接");
        commonTopView.setRightTextVis(true);
        //连接设备的点击事件
        commonTopView.setOnRightClickListener(new CommonTopView.OnRightClickListener() {
            @Override
            public void onRightClick(View view) {
                if (isConnect) {
                    ToastUtil.show(context, R.string.connection);
                    return;
                }
                connection();
            }
        });

    }

    //设备是否连接
    private boolean isConnect = false;

    @OnClick({R.id.fb_out_txt, R.id.btn_begin, R.id.btn_stop})
    public void onClick(View view) {
        switch (view.getId()) {
            //导出数据
            case R.id.fb_out_txt:
                saveToFile();
                break;

            //开始测试
            case R.id.btn_begin:

                break;

            //停止测试
            case R.id.btn_stop:
//                if (timer != null) {
//                    timer.cancel();
//                }
                break;
        }
    }


    /**
     * 导出文本
     *
     * @param ,
     * @return void
     * @date 2019-02-15
     */
    private void saveToFile() {
        //没有输入数据的时候
        if (rssiAdapter.getItemCount() == 0) {
            ToastUtil.show(context, R.string.no_output_data);
            return;
        }
        //请求写入文件权限
        permissionHelper.requestPermission(new PermissionHelper.PermissionCallBack() {
            @Override
            public void onPermissionGrant(String... permission) {
                //获取到权限，在此做导出文本操作
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //String outputStr = "";
                        List<RssiData> datas = rssiAdapter.getDatas();

                        File file = FileUtil.getFile(FileUtil.filePath, FileUtil.getFileName());

                        BufferedWriter bufferedWriter = null;
                        try {
                            bufferedWriter = new BufferedWriter(new FileWriter(file));
                            for (int i = 0; i < datas.size(); i++) {
                                bufferedWriter.write(datas.get(i).getTime() + datas.get(i).getRssi() + "\n");
                            }
                            bufferedWriter.flush();
                            bufferedWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show(context, R.string.output_success);
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onPermissionDenied(String... permission) {
                ToastUtil.show(context, R.string.permission_denied);
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }


    //初始化recycleView
    private void initRecycleView() {
        GridLayoutManager manager = new GridLayoutManager(context, 1);
        rssiRecycleView.setLayoutManager(manager);
        rssiAdapter = new RssiAdapter(context, null);
        rssiRecycleView.setAdapter(rssiAdapter);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                //do something
                readRssi();
            }
            super.handleMessage(msg);
        }
    };

    Timer timer;
    TimerTask timerTask;

    private void initTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
    }


    //连接设备
    private void connection() {
        BluetoothManage.getInstance().
                registerConnection(mac, mBleConnectStatusListener);
        showProgressDialog(getString(R.string.connecting));
        BluetoothManage.getInstance().connectionRequest(mac, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile data) {
                hideProgressDialog();
            }
        });
    }

    private int connectionFlag = 1;

    //读取信号值
    private void readRssi() {
        BluetoothManage.getInstance().readRssi(mac, new BleReadRssiResponse() {
            @Override
            public void onResponse(int code, Integer data) {
                if (code == REQUEST_SUCCESS) {
                    RssiData rssiData = new RssiData();
                    rssiData.setRssi(data);
                    rssiData.setTime(TimeUtil.getCurrentDate());
                    rssiData.setConnectionFlag(connectionFlag);
                    rssiAdapter.notifyData(rssiData);
                    tvTotalItem.setText(String.valueOf(rssiAdapter.getItemCount()));
                    rssiRecycleView.smoothScrollToPosition(rssiAdapter.getItemCount() - 1);
                }
            }
        });

    }


    /**
     * @date 2019-08-27
     * 蓝牙连接状态监听回调
     */
    private final BleConnectStatusListener mBleConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == STATUS_CONNECTED) {
                hideProgressDialog();
                tvStatues.setText(getString(R.string.connection));
                tvStatues.setTextColor(Color.GREEN);
                connectionFlag = 1;
                //连接上后就读取
                isConnect = true;
                destroyTimer();
                initTimer();
                timer.schedule(timerTask, 0, 1000);

            } else if (status == STATUS_DISCONNECTED) {
                hideProgressDialog();
                connectionFlag = 0;
                tvStatues.setText(getString(R.string.disconnection));
                tvStatues.setTextColor(Color.RED);
                isConnect = false;
                rssiAdapter.notifyDisconnect();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //断开连接
        BluetoothManage.getInstance().disConnection(mac);
        //解除监听
        BluetoothManage.getInstance()
                .unRegistConnection(mac, mBleConnectStatusListener);

        destroyTimer();
    }

    /**
     * destory上次使用的 Timer
     */
    public void destroyTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }
}
