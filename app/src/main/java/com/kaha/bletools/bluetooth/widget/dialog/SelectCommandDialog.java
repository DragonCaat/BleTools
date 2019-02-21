package com.kaha.bletools.bluetooth.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.kaha.bletools.R;
import com.kaha.bletools.bluetooth.base.AppConst;
import com.kaha.bletools.bluetooth.ui.adapter.GattServiceAdapter;
import com.kaha.bletools.bluetooth.ui.adapter.ShowCommandAdapter;
import com.kaha.bletools.bluetooth.utils.ByteAndStringUtil;
import com.kaha.bletools.bluetooth.utils.CommandFileUtil;
import com.kaha.bletools.bluetooth.utils.SPUtil;
import com.kaha.bletools.framework.utils.ToastUtil;
import com.kaha.bletools.litepal.Command;
import com.kaha.bletools.litepal.LitePalManage;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 输入命令的dialog
 *
 * @author Darcy
 * @Date 2019/2/12
 * @package com.kaha.bletools.bluetooth.widget.dialog
 * @Desciption
 */
public abstract class SelectCommandDialog extends Dialog {

    //文本命令
    public static final int TEXT_COMMAND = 1;
    //十六进制命令
    public static final int HEX_COMMAND = 0;
    @BindView(R.id.et_input_command)
    EditText etInputCommand;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    //展示已经保存的命令的ListView
    @BindView(R.id.lv_show_save_command)
    ListView lvShowSaveCommand;

    private Activity activity;
    //命令列表的适配器
    private ShowCommandAdapter adapter;
    private List<Command> list;

    private int commandFlag = 0; //0:代表16进制的命令   1:文本命令

    private String command;//已经输入的命令

    public SelectCommandDialog(Activity activity, String command) {
        super(activity, R.style.defaultDialog);
        this.activity = activity;
        this.command = command;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_command);
        setCanceledOnTouchOutside(true);//外部点击取消
        ButterKnife.bind(this);
        setDialogData();
        initView();
    }

    //初始化View
    private void initView() {
        list = LitePalManage.getInstance().getCommandList();
        //初始化命令的listView
        adapter = new ShowCommandAdapter(activity, list);
        lvShowSaveCommand.setAdapter(adapter);
        lvShowSaveCommand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String command = list.get(position).getCommand();
                etInputCommand.setText(command);
                etInputCommand.setSelection(command.length());
            }
        });

        //命令输入框的监听
        etInputCommand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() > 0) {
                    ivDelete.setVisibility(View.VISIBLE);
                } else {
                    ivDelete.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    ivDelete.setVisibility(View.VISIBLE);
                } else {
                    ivDelete.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (!activity.getResources().getString(R.string.input_command).equals(command))
            etInputCommand.setText(command);
    }

    @OnClick({R.id.btn_send, R.id.btn_save_command,
            R.id.iv_delete, R.id.btn_convert})
    public void onClick(View view) {
        switch (view.getId()) {
            //发送命令
            case R.id.btn_send:
                sendCommand();
                break;

            //保存命令
            case R.id.btn_save_command:
                saveCommand();
                break;

            //清空输入的命令
            case R.id.iv_delete:
                etInputCommand.setText("");
                break;
            //转换已经输入的命令
            case R.id.btn_convert:
                String commandStr = etInputCommand.getText().toString();
                String regex = "^[A-Fa-f0-9]+$";
                if (commandStr.matches(regex)) {
                    //是十六进制
                    String s1 = ByteAndStringUtil.hexStringToString(commandStr);
                    etInputCommand.setText(s1);
                    etInputCommand.setSelection(s1.length());
                } else {
                    //是文本
                    String s1 = ByteAndStringUtil.str2HexStr(commandStr);
                    etInputCommand.setText(s1);
                    etInputCommand.setSelection(s1.length());
                }
                break;

        }
    }

    /**
     * 保存命令
     *
     * @param ,
     * @return void
     * @date 2019-02-14
     */
    private void saveCommand() {
        String command = etInputCommand.getText().toString();
        if (TextUtils.isEmpty(command)) {
            ToastUtil.show(activity, R.string.no_command);
            return;
        }
        List<Command> list = DataSupport.findAll(Command.class);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getCommand().equals(command)) {
                    //数据库中已经保存相同mac的设备
                    ToastUtil.show(activity, R.string.command_exist);
                    return;
                }
            }
        }
        Boolean aBoolean = LitePalManage.getInstance().saveCommand(command);
        if (aBoolean) {
            Command command1 = new Command();
            command1.setCommand(command);
            adapter.addCommand(command1);
        } else {
            ToastUtil.show(activity, R.string.save_fail);
        }

        List<Command> commandData = adapter.getCommandData();
        if (commandData.size() > 0) {
            CommandFileUtil.saveCommand2SDCard(commandData,"bleCommand");
        }
    }

    /**
     * 发送命令
     *
     * @param ,
     * @return void
     * @date 2019-02-13
     */
    private void sendCommand() {
        String command = etInputCommand.getText().toString();
        if (TextUtils.isEmpty(command)) {
            ToastUtil.show(activity, R.string.no_command);
            return;
        }
        sendCommand(commandFlag, command);
        cancel();
    }


    /**
     * 设置弹框的相关属性
     *
     * @param ,
     * @return void
     * @date 2019-02-13
     */
    private void setDialogData() {
        Window window = this.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        //将属性设置
        onWindowAttributesChanged(layoutParams);
    }

    /**
     * 发送命令
     */
    public abstract void sendCommand(int commandFlag, String command);
}
