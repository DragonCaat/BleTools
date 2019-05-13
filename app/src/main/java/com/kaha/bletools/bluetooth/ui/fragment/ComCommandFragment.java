package com.kaha.bletools.bluetooth.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.kaha.bletools.R;
import com.kaha.bletools.bluetooth.ui.adapter.ComCommandAdapter;
import com.kaha.bletools.framework.ui.fragment.BaseFragment;

import butterknife.BindView;

/**
 * 公共的fragment
 *
 * @author Darcy
 * @Date 2019/5/11
 * @package com.kaha.bletools.bluetooth.ui.fragment
 * @Desciption
 */
public class ComCommandFragment extends BaseFragment {

    @BindView(R.id.lv_common_command)
    ListView lvCommonCommand;

    //公共命令适配器
    private ComCommandAdapter adapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_common_command;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] commonCommands = getResources().getStringArray(R.array.command_common);
        adapter = new ComCommandAdapter(context, commonCommands);
        lvCommonCommand.setAdapter(adapter);
    }
}
