package com.kaha.bletools.bluetooth.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kaha.bletools.R;
import com.kaha.bletools.bluetooth.common.onRecycleViewItemClickListener;
import com.kaha.bletools.framework.ui.adapter.BaseRecycleViewHolder;
import com.kaha.bletools.framework.ui.adapter.BaseRecyclerAdapter;
import com.kaha.bletools.litepal.FileCommand;

import java.util.List;

import butterknife.BindView;

/**
 * @author Darcy
 * @Date 2019/7/9
 * @package com.kaha.bletools.bluetooth.ui.adapter
 * @Desciption
 */
public class FileCommandAdapter extends BaseRecyclerAdapter<FileCommand, FileCommandAdapter.FileCommandHolder> {

    private onRecycleViewItemClickListener listener;

    public FileCommandAdapter(Context context, List<FileCommand> datas, onRecycleViewItemClickListener listener) {
        super(context, datas);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(FileCommandHolder holder, final FileCommand data, int position) {
        holder.btnFileCommand.setText(data.getCommandName());
        holder.btnFileCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItmeClick(data.getCommandValue());
            }
        });
    }

    @NonNull
    @Override
    public FileCommandHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.layout_file_command_item, null);
        return new FileCommandHolder(view);
    }

    class FileCommandHolder extends BaseRecycleViewHolder {

        @BindView(R.id.btn_file_command_item)
        TextView btnFileCommand;

        public FileCommandHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
