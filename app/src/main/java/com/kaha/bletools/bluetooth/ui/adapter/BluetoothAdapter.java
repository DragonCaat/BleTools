package com.kaha.bletools.bluetooth.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inuker.bluetooth.library.search.SearchResult;
import com.kaha.bletools.R;
import com.kaha.bletools.bluetooth.base.AppConst;
import com.kaha.bletools.bluetooth.entity.BluetoothEntity;
import com.kaha.bletools.bluetooth.ui.activity.DeviceControlActivity;
import com.kaha.bletools.bluetooth.ui.activity.RssiTestActivity;
import com.kaha.bletools.bluetooth.utils.ByteAndStringUtil;
import com.kaha.bletools.bluetooth.utils.SPUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author : Darcy
 * @package com.kaha.bletools.bluetooth.ui.adapter
 * @Date 2018-11-12 15:42
 * @Description 展示设备蓝牙的适配器
 */
public class BluetoothAdapter extends RecyclerView.Adapter<BluetoothAdapter.BluetoothHolder> {


    private List<SearchResult> list;
    private Context context;

    public BluetoothAdapter(List<SearchResult> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public BluetoothHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.item_bluetooth_layout, null);
        return new BluetoothHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothHolder bluetoothHolder, int i) {
        final SearchResult entity = list.get(i);
        bluetoothHolder.deviceName.setText(entity.getName());
        bluetoothHolder.deviceAddress.setText(entity.getAddress());
        bluetoothHolder.deviceRSSI.setText("" + entity.rssi);
        //bluetoothHolder.tvBroadcastPack.setText(""+ByteAndStringUtil.bytesToHexString(entity.scanRecord));
        bluetoothHolder.llBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                int anInt = SPUtil.getInstance().getInt(AppConst.RSSI_MODEL, 0);
                if (anInt == 0) {
                    intent = new Intent(context, DeviceControlActivity.class);
                } else {
                    intent = new Intent(context, RssiTestActivity.class);
                }
                intent.putExtra(AppConst.KEY_1, entity.getName());
                intent.putExtra(AppConst.KEY_2, entity.getAddress());
                context.startActivity(intent);
            }
        });

        final int rssiPercent = (int) (100.0f * (127.0f + entity.rssi) / (127.0f + 20.0f));

        // Log.i("hello", "onBindViewHolder: "+rssiPercent);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    /**
     * 清空数据
     *
     * @param ,
     * @return void
     * @date 2019-01-23
     */
    public void clear() {
        if (list != null) {
            list.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 设置所有数据
     *
     * @param dataList 数据源
     * @return void
     * @date 2019-01-23
     */
    public void setListAll(List<SearchResult> dataList) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.clear();
        list.addAll(dataList);
        notifyDataSetChanged();
    }


    static class BluetoothHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.device_name)
        TextView deviceName;
        @BindView(R.id.device_mac)
        TextView deviceAddress;
        @BindView(R.id.device_rssi)
        TextView deviceRSSI;
        @BindView(R.id.device_distance)
        TextView deviceDistance;
        @BindView(R.id.tv_broadcastPack)
        TextView tvBroadcastPack;

        @BindView(R.id.ll_show_bluetooth)
        LinearLayout llBluetooth;

        public BluetoothHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
