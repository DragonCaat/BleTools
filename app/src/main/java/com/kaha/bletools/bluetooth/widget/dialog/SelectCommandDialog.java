package com.kaha.bletools.bluetooth.widget.dialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.kaha.bletools.R;
import com.kaha.bletools.bluetooth.ui.adapter.MinePagerAdapter;
import com.kaha.bletools.bluetooth.ui.adapter.ShowCommandAdapter;
import com.kaha.bletools.bluetooth.ui.fragment.ComCommandFragment;
import com.kaha.bletools.bluetooth.utils.ByteAndStringUtil;
import com.kaha.bletools.bluetooth.utils.FileUtil;
import com.kaha.bletools.bluetooth.utils.InputCapLowerToUpper;
import com.kaha.bletools.bluetooth.utils.TimeUtil;
import com.kaha.bletools.framework.utils.PermissionHelper;
import com.kaha.bletools.framework.utils.ToastUtil;
import com.kaha.bletools.litepal.Command;
import com.kaha.bletools.litepal.LitePalManage;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    //循环时间间隔
    @BindView(R.id.et_send_circle_time)
    EditText etCircleTime;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private AppCompatActivity activity;
    //命令列表的适配器
    private ShowCommandAdapter adapter;
    private List<Command> list;

    private int commandFlag = 0; //0:代表16进制的命令   1:文本命令

    private String command;//已经输入的命令

    private PermissionHelper permissionHelper;

    //tableLayout的标题源数据
    private String[] mTitles = {"公共", "个人"};
    private List<Fragment> fragmentList = new ArrayList<>();

    public SelectCommandDialog(AppCompatActivity activity, String command) {
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
        permissionHelper = new PermissionHelper(activity);
        initView();
        initData();
        setDialogParams();
    }

    private void initData() {
        String[] commonCommands = activity.getResources().getStringArray(R.array.command_common);
        List<Command> list = DataSupport.findAll(Command.class);
        if (list == null || list.size() == 0) {
            for (int i = 0; i < commonCommands.length; i++) {
                Command command1 = new Command();
                command1.setCommand(commonCommands[i]);
                adapter.addCommand(command1);
            }
        }
    }

    /**
     * 设置dialog位于屏幕中间
     *
     * @return void
     * @Date 2018-11-19
     */
    private void setDialogParams() {
        Window window = this.getWindow();
        if (window != null) {
            //int width = activity.getResources().getDimensionPixelSize(R.dimen.y300);
            int height = activity.getResources().getDimensionPixelSize(R.dimen.darcy_500_dp);
            window.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = window.getAttributes();
            // lp.width = width; //设置宽度
            lp.height = height;
            window.setAttributes(lp);
        }
    }


    /**
     * 初始化viewPager
     *
     * @param ,
     * @return void
     * @date 2019-01-09
     */
    private void initViewPager() {
        ComCommandFragment fragment0 = new ComCommandFragment();
        ComCommandFragment fragment1 = new ComCommandFragment();

        fragmentList.add(fragment0);
        fragmentList.add(fragment1);

        MinePagerAdapter pagerAdapter = new MinePagerAdapter(
                Objects.requireNonNull(activity).getSupportFragmentManager(), mTitles, fragmentList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);
    }

    //初始化View
    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        list = LitePalManage.getInstance().getCommandList();
        if (list == null || list.size() == 0) {
            //list = CommandFileUtil.getCommandList("bleCommand");
        }
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
        //强制小写变大写
        //etInputCommand.setTransformationMethod(new InputCapLowerToUpper());
        if (!activity.getResources().getString(R.string.input_command).equals(command))
            etInputCommand.setText(command);
    }

    @OnClick({R.id.btn_send, R.id.btn_save_command,
            R.id.iv_delete, R.id.btn_convert,
            R.id.btn_sen_circle, R.id.btn_capitalized,
            R.id.btn_time})
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

            //循环发送命令
            case R.id.btn_sen_circle:
                circleSendCommand();
                break;

            //大写
            case R.id.btn_capitalized:
                String command = etInputCommand.getText().toString();
                if (TextUtils.isEmpty(command)) {
                    return;
                }
                String commandUpper = command.toUpperCase();
                etInputCommand.setText(commandUpper);
                etInputCommand.setSelection(commandUpper.length());
                break;

            //时间
            case R.id.btn_time:
                String hexTime = TimeUtil.getHexTime();
                String hexTimeZone = TimeUtil.getHexTimeZone();
                String commandTime = "415453544D3D" + hexTime + hexTimeZone+"00000000";
                //"415453544D3D141106070A34152B051E";
                etInputCommand.setText(commandTime);
                etInputCommand.setSelection(commandTime.length());
                break;

        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 循环发送命令
     *
     * @param
     * @return void
     * @date 2019-03-18
     */
    private void circleSendCommand() {
        String circleTime = etCircleTime.getText().toString();
        if (TextUtils.isEmpty(circleTime)) {
            ToastUtil.show(activity, R.string.input_circle_time);
            return;
        }
        String commandStr = etInputCommand.getText().toString();//.toUpperCase();
        if (TextUtils.isEmpty(commandStr)) {
            ToastUtil.show(activity, R.string.no_command);
            return;
        }
        int circle = Integer.parseInt(circleTime);
        CircleSendCommand(commandStr, circle);
        this.dismiss();
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
        requestPermission();
    }

    //请求写入文件的权限
    private void requestPermission() {
        permissionHelper.requestPermission(new PermissionHelper.PermissionCallBack() {
            @Override
            public void onPermissionGrant(String... permission) {
                final List<Command> commandData = adapter.getCommandData();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (commandData.size() > 0) {
                            for (int i = 0; i < commandData.size(); i++) {
                                String command = "";
                                command = command + commandData.get(i).getCommand() + "\n";
                                FileUtil.writeCommandToFile(command);
                            }
                        }
                    }
                }).start();
            }

            @Override
            public void onPermissionDenied(String... permission) {

            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }


    /**
     * 发送命令
     *
     * @param ,
     * @return void
     * @date 2019-02-13
     */
    private void sendCommand() {
        String command = etInputCommand.getText().toString();//.toUpperCase();
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
     *
     * @param command     命令
     * @param commandFlag 命令类型，是十六进制还是文本类型的命令
     */
    public abstract void sendCommand(int commandFlag, String command);

    /**
     * 循环发送命令
     *
     * @param command    要循环发送的命令
     * @param circleTime 循环间隔
     */
    public abstract void CircleSendCommand(String command, int circleTime);
}
