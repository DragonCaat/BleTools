package com.kaha.bletools.bluetooth.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaha.bletools.R;
import com.kaha.bletools.bluetooth.entity.BluetoothEntity;

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


    private List<BluetoothEntity> list;
    private Context context;

    public BluetoothAdapter(List<BluetoothEntity> list, Context context) {
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
        BluetoothEntity entity = list.get(i);
        bluetoothHolder.deviceName.setText(entity.getDeviceName());
        bluetoothHolder.deviceAddress.setText(entity.getDeviceAddress());
        bluetoothHolder.deviceRSSI.setText(""+entity.getDeviceRSSI());
    }

    @Override
    public int getItemCount() {
        return list.size();
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

        public BluetoothHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
