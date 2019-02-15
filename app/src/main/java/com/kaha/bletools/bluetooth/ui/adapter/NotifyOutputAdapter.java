package com.kaha.bletools.bluetooth.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaha.bletools.R;
import com.kaha.bletools.bluetooth.entity.OutputData;
import com.kaha.bletools.framework.ui.adapter.BaseRecycleViewHolder;
import com.kaha.bletools.framework.ui.adapter.BaseRecyclerAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * 展示通知数据的适配器
 *
 * @author Darcy
 * @Date 2019/2/14
 * @package com.kaha.bletools.bluetooth.ui.adapter
 * @Desciption
 */
public class NotifyOutputAdapter extends BaseRecyclerAdapter<OutputData, NotifyOutputAdapter.OutputHolder> {

    public NotifyOutputAdapter(Context context, List<OutputData> datas) {
        super(context, datas);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(OutputHolder holder, OutputData data, int position) {
        holder.getItemView().setTag(holder.getItemView().getId(), position);
        holder.tvOutputItem.setText(data.getOutputString());
        if (!TextUtils.isEmpty(data.getOutputString())) {
            holder.tvCount.setText("" + data.getOutputString().length());
        }
        //最新的一条数据颜色为绿色
        if (position == datas.size() - 1) {
            holder.tvOutputItem.setTextColor(context.getResources().getColor(R.color.green_47ba5c));
        } else {
            holder.tvOutputItem.setTextColor(context.getResources().getColor(R.color.gray_888888));
        }
    }

    @NonNull
    @Override
    public OutputHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_output_item, viewGroup, false);
        return new OutputHolder(view);
    }

    static class OutputHolder extends BaseRecycleViewHolder {
        @BindView(R.id.tv_output_item)
        TextView tvOutputItem;
        @BindView(R.id.tv_textCount)
        TextView tvCount;

        public OutputHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public int getDatasSize() {
        return datas.size();
    }
}
