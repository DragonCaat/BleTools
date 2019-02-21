package com.kaha.bletools.bluetooth.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaha.bletools.R;
import com.kaha.bletools.litepal.Command;
import com.kaha.bletools.litepal.LitePalManage;

import java.util.List;

/**
 * 展示已经保存的命令的listview适配器
 *
 * @author Darcy
 * @Date 2019/2/13
 * @package com.kaha.bletools.bluetooth.ui.adapter
 * @Desciption
 */
public class ShowCommandAdapter extends BaseAdapter {
    private List<Command> list;
    private Context context;

    public ShowCommandAdapter(Context context, List<Command> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(context, R.layout.layout_saved_command_item, null);
        } else {
            view = convertView;
        }
        TextView tvContent = view.findViewById(R.id.tv_content);
        tvContent.setText(list.get(position).getCommand());
        //删除命令
        ImageView ivRemoveCommand = view.findViewById(R.id.iv_remove_command);
        ivRemoveCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Command command = list.get(position);
                command.delete();
                list.remove(position);
                notifyDataSetChanged();
            }
        });
        return view;
    }

    /**
     * @param command 要添加保存的命令
     * @return void
     * @date 2019-02-14
     */
    public void addCommand(Command command) {
        list.add(command);
        notifyDataSetChanged();
    }

    public List<Command> getCommandData() {
        return list;
    }
}
