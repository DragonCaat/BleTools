package com.kaha.bletools.bluetooth.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaha.bletools.R;
import com.kaha.bletools.litepal.Command;

import java.util.List;

/**
 * 展示公共的的命令的适配器
 *
 * @author Darcy
 * @Date 2019/2/13
 * @package com.kaha.bletools.bluetooth.ui.adapter
 * @Desciption
 */
public class ComCommandAdapter extends BaseAdapter {
    private String[] commonCommands;
    private Context context;

    public ComCommandAdapter(Context context, String[] commonCommands) {
        this.context = context;
        this.commonCommands = commonCommands;
    }

    @Override
    public int getCount() {
        return commonCommands.length;
    }

    @Override
    public Object getItem(int position) {
        return commonCommands[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(context, R.layout.layout_common_command_item, null);
        } else {
            view = convertView;
        }
        TextView tvContent = view.findViewById(R.id.tv_content);
        tvContent.setText(commonCommands[position]);
        return view;
    }
}
