package com.kaha.bletools.bluetooth.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaha.bletools.R;
import com.kaha.bletools.bluetooth.entity.RssiData;
import com.kaha.bletools.framework.ui.adapter.BaseRecycleViewHolder;
import com.kaha.bletools.framework.ui.adapter.BaseRecyclerAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * 信号值适配器
 *
 * @author Darcy
 * @Date 2019/8/27
 * @package com.kaha.bletools.bluetooth.ui.adapter
 * @Desciption
 */
public class RssiAdapter extends BaseRecyclerAdapter<RssiData, RssiAdapter.RssiHolder> {

    public RssiAdapter(Context context, List<RssiData> datas) {
        super(context, datas);
    }

    @Override
    protected void onBindViewHolder(RssiHolder holder, RssiData data, int position) {
        holder.tvRssi.setText(String.valueOf(data.getRssi()));
        holder.tvTime.setText(data.getTime());
        if (data.getConnectionFlag() == 0) {
            holder.tvRssi.setTextColor(context.getResources().getColor(R.color.red_ff4c4c));
            holder.tvTime.setTextColor(context.getResources().getColor(R.color.red_ff4c4c));
        } else {
            holder.tvTime.setTextColor(context.getResources().getColor(R.color.gray_888888));
            holder.tvRssi.setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    @NonNull
    @Override
    public RssiHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.item_rssi_layout, null);
        return new RssiAdapter.RssiHolder(view);
    }

    class RssiHolder extends BaseRecycleViewHolder {

        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_rssi)
        TextView tvRssi;

        public RssiHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    //断开
    public void notifyDisconnect(){
        RssiData rssiData = datas.get(datas.size() - 1);
        rssiData.setConnectionFlag(0);
        notifyItemChanged(datas.size() - 1);
    }

}
