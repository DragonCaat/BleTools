package com.kaha.bletools.bluetooth.widget.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.kaha.bletools.R;
import com.kaha.bletools.bluetooth.common.onRecycleViewItemClickListener;
import com.kaha.bletools.bluetooth.ui.adapter.FileCommandAdapter;
import com.kaha.bletools.litepal.FileCommand;
import com.kaha.bletools.litepal.LitePalManage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 从文件中读取命令
 *
 * @author Darcy
 * @Date 2019/7/9
 * @package com.kaha.bletools.bluetooth.widget.dialog
 * @Desciption
 */
public abstract class FileCommandDialog extends Dialog {


    @BindView(R.id.rv_file_command)
    RecyclerView rvFileCommand;

    private AppCompatActivity activity;
    private FileCommandAdapter adapter;

    public FileCommandDialog(AppCompatActivity activity) {
        super(activity, R.style.defaultDialog);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_file_command);
        setCanceledOnTouchOutside(true);//外部点击取消
        ButterKnife.bind(this);

        initRecycleView();
    }

    //初始化recycleView
    private void initRecycleView() {
        GridLayoutManager manager = new GridLayoutManager(activity, 2);
        rvFileCommand.setLayoutManager(manager);
        adapter = new FileCommandAdapter(activity, null, new onRecycleViewItemClickListener() {
            @Override
            public void onItmeClick(String cmdValue) {
                ClickFileCommand(cmdValue);
                FileCommandDialog.this.dismiss();
            }
        });
        rvFileCommand.setAdapter(adapter);

        List<FileCommand> fileCommandList = LitePalManage.getInstance().getFileCommandList();
        adapter.updateData(fileCommandList);
    }

    /**
     * 点击单个命令
     */
    public abstract void ClickFileCommand(String command);

}
