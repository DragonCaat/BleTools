package com.kaha.bletools.bluetooth.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchResult;
import com.kaha.bletools.R;
import com.kaha.bletools.bluetooth.base.AppConst;
import com.kaha.bletools.bluetooth.entity.OutputData;
import com.kaha.bletools.bluetooth.ui.adapter.NotifyOutputAdapter;
import com.kaha.bletools.bluetooth.utils.ByteAndStringUtil;
import com.kaha.bletools.bluetooth.utils.FileUtil;
import com.kaha.bletools.bluetooth.utils.FloatingBtnData;
import com.kaha.bletools.bluetooth.utils.bluetooth.BluetoothManage;
import com.kaha.bletools.bluetooth.widget.BleSelectView;
import com.kaha.bletools.bluetooth.widget.dialog.SelectCharacterDialog;
import com.kaha.bletools.bluetooth.widget.dialog.SelectCommandDialog;
import com.kaha.bletools.framework.ui.activity.BaseActivity;
import com.kaha.bletools.framework.utils.PermissionHelper;
import com.kaha.bletools.framework.utils.ToastUtil;
import com.kaha.bletools.framework.widget.CommonTopView;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;

/**
 * 操作界面
 *
 * @author Darcy
 * @date 2019-01-23
 * @desciption
 */
public class DeviceControlActivity extends BaseActivity {

    @BindView(R.id.topView)
    CommonTopView topView;
    @BindView(R.id.tv_mac)
    TextView tvMac;
    @BindView(R.id.tv_statues)
    TextView tvStatues;

    @BindView(R.id.select_write_character)
    BleSelectView selectWriteView;
    @BindView(R.id.select_notify_character)
    BleSelectView selectNotifyView;
    @BindView(R.id.select_read_character)
    BleSelectView selectReadView;
    //输入命令的控件
    @BindView(R.id.tv_command)
    TextView tvCommand;
    @BindView(R.id.rv_output)
    RecyclerView rvOutput;

    @BindView(R.id.tv_total)
    TextView tvTotal;

    @BindView(R.id.activity_main_rfal)
    RapidFloatingActionLayout rfaLayout;
    @BindView(R.id.activity_main_rfab)
    RapidFloatingActionButton rfaBtn;
    private RapidFloatingActionHelper rfabHelper;
    private List<RFACLabelItem> items;//浮动button的数据源
    //rvOutput的适配器
    private NotifyOutputAdapter adapter;
    //蓝牙设备信息数据
    private BleGattProfile bleGattProfiledata;
    private String mac;
    private String name;
    private UUID writeServiceUuid;
    private UUID writeCharacterUuid;
    //通知
    private UUID notifyServiceUuid;
    private UUID notifyCharacterUuid;
    //权限帮助类
    private PermissionHelper permissionHelper;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_device_control;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getIntent().getStringExtra(AppConst.KEY_1);
        mac = getIntent().getStringExtra(AppConst.KEY_2);
        setUserData();
        connection();
        initView();
    }

    private void initView() {
        GridLayoutManager manager = new GridLayoutManager(context, 1);
        rvOutput.setLayoutManager(manager);
        adapter = new NotifyOutputAdapter(context, null);
        rvOutput.setAdapter(adapter);

        if (items != null) {
            items.clear();
        }
        //浮动按钮的初始化
        items = FloatingBtnData.getFloatingData(context);
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(context);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(labelListListener);
        rfaContent
                .setItems(items)
                .setIconShadowRadius(dip2px(5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(dip2px(5))
        ;
        rfabHelper = new RapidFloatingActionHelper(
                context,
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();
        //实例化权限帮助类
        permissionHelper = new PermissionHelper(activity);
    }

    /**
     * 点击事件处理
     */
    @OnClick({R.id.select_write_character, R.id.select_notify_character,
            R.id.tv_command, R.id.btn_send, R.id.select_read_character})
    public void onClick(View view) {
        switch (view.getId()) {
            //选择可写属性
            case R.id.select_write_character:
                showDialog();
                break;
            //选择可通知属性
            case R.id.select_notify_character:
                showDialog();
                break;
            //选择可读属性
            case R.id.select_read_character:
                showDialog();
                break;
            //点击输入命令
            case R.id.tv_command:
                showCommandDialog();
                break;
            //发送命令
            case R.id.btn_send:
                String s = tvCommand.getText().toString();
                if (s.equals(getResources().getString(R.string.input_command))) {
                    ToastUtil.show(context, R.string.no_input_command);
                    return;
                }
                writeCommand(s);
                break;
        }
    }

    /**
     * 展示命令的输入框
     *
     * @param ,
     * @return void
     * @date 2019-02-13
     */
    private void showCommandDialog() {
        String writeCharacter = selectWriteView.getContentString();
        String notifyCharacter = selectNotifyView.getContentString();
        //用户未选择对应的特征
        if (writeCharacter.equals(activity.getResources().getString(R.string.select_write_character))) {
            ToastUtil.show(activity, R.string.no_select_character);
            return;
        } else if (notifyCharacter.equals(activity.getResources().getString(R.string.select_notify_character))) {
            ToastUtil.show(activity, R.string.no_select_character);
            return;
        }
        new SelectCommandDialog(activity) {
            @Override
            public void sendCommand(int commandFlag, String command) {
                tvCommand.setText(command);
                writeCommand(command);
            }
        }.show();
    }

    //发送命令
    private void writeCommand(String command) {
        BluetoothManage.getInstance().writeCommand(mac, writeServiceUuid, writeCharacterUuid,
                command, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (code == REQUEST_SUCCESS) {
                            ToastUtil.show(context, R.string.write_success);
                        } else {
                            ToastUtil.show(context, R.string.write_fail);
                        }
                    }
                });
    }


    /**
     * 展示选择字段的弹框
     *
     * @param
     * @return void
     * @date 2019-02-13
     */
    private void showDialog() {
        Boolean connect = BluetoothManage.getInstance().isConnect(mac);
        if (!connect) {
            ToastUtil.show(context, R.string.ble_device_disconnect);
            return;
        }
        new SelectCharacterDialog(activity, bleGattProfiledata) {
            @Override
            public void clickWrite(UUID serviceUuid, UUID characterUuid) {
                selectWriteView.setTvContent(String.valueOf(characterUuid));
                writeServiceUuid = serviceUuid;
                writeCharacterUuid = characterUuid;
            }

            @Override
            public void clickRead(UUID serviceUuid, UUID characterUuid) {
                BluetoothManage.getInstance().readData(mac, serviceUuid,
                        characterUuid, new BleReadResponse() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse(int code, byte[] data) {
//                                String hexString = ByteAndStringUtil.bytesToHexString(data);
//                                String result = ByteAndStringUtil.hexStringToString(hexString);
                                adapter.notifyData(new OutputData(new String(data)));
                                tvTotal.setText("" + adapter.getDatasSize());
                            }
                        });
            }

            @Override
            public void clickNotify(UUID serviceUuid, UUID characterUuid) {
                //已经选择同一个通知就不再打开通知
                String contentString = selectNotifyView.getContentString();
                if (contentString.equals(String.valueOf(characterUuid))) {
                    //ToastUtil.show(context,"通知已选择");
                    return;
                }
                selectNotifyView.setTvContent(String.valueOf(characterUuid));
                //打开通知
                BluetoothManage.getInstance().openNotify(mac, serviceUuid, characterUuid, response);
            }
        }.show();
    }

    /**
     * 蓝牙接收通知的回调
     */
    private BleNotifyResponse response = new BleNotifyResponse() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {
            String hexString = ByteAndStringUtil.bytesToHexString(value);
            adapter.notifyData(new OutputData(hexString));
            tvTotal.setText("" + adapter.getDatasSize());
            rvOutput.smoothScrollToPosition(adapter.getDatasSize() - 1);
        }

        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {
                //打开通知成功
                ToastUtil.show(context, R.string.notify_opened);
            }
        }
    };


    /**
     * 设置设备的基本信息
     *
     * @param ,
     * @return void
     * @date 2019-01-23
     */
    private void setUserData() {
        if (!TextUtils.isEmpty(name)) {
            topView.setTitle(name);
        }
        if (!TextUtils.isEmpty(mac)) {
            tvMac.setText(mac);
        }
    }

    //连接设备
    private void connection() {
        BluetoothManage.getInstance().
                registerConnection(mac, mBleConnectStatusListener);
        showProgressDialog(getString(R.string.connecting));
        BluetoothManage.getInstance().connectionRequest(mac, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile data) {
                if (code == STATUS_CONNECTED) {
                }
                //连接成功
                bleGattProfiledata = data;
            }
        });
    }

    //蓝牙连接状态监听
    private final BleConnectStatusListener mBleConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == STATUS_CONNECTED) {
                hideProgressDialog();
                tvStatues.setText(getString(R.string.connection));
                tvStatues.setTextColor(Color.GREEN);

            } else if (status == STATUS_DISCONNECTED) {
                tvStatues.setText(getString(R.string.disconnection));
                tvStatues.setTextColor(Color.RED);

            }
        }
    };

    /**
     * 浮动按钮的点击事件
     */
    private RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener labelListListener =
            new RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener() {

                @Override
                public void onRFACItemLabelClick(int position, RFACLabelItem item) {
                    //Toast.makeText(context, "clicked label: " + position, Toast.LENGTH_SHORT).show();
                    //rfabHelper.toggleContent();
                }

                @Override
                public void onRFACItemIconClick(int position, RFACLabelItem item) {
                    switch (position) {
                        //清空数据
                        case 0:
                            adapter.clear();
                            break;
                        //导出文本操作
                        case 1:
                            saveToFile();
                            break;
                    }
                    rfabHelper.toggleContent();
                }
            };

    /**
     * 导出文本
     *
     * @param ,
     * @return void
     * @date 2019-02-15
     */
    private void saveToFile() {
        //没有输入数据的时候
        if (adapter.getDatasSize() == 0) {
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
                        String outputStr = null;
                        List<OutputData> datas = adapter.getDatas();
                        for (OutputData data : datas) {
                            outputStr = outputStr + data.getOutputString();
                        }
                        FileUtil.writeTextToFile(outputStr);
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
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //断开连接
        BluetoothManage.getInstance().disConnection(mac);
        //解除监听
        BluetoothManage.getInstance()
                .unRegistConnection(mac, mBleConnectStatusListener);
        //取消通知
        BluetoothManage.getInstance().closeNotify(mac, notifyServiceUuid, notifyCharacterUuid);
    }
}
