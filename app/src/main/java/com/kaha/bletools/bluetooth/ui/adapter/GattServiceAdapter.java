package com.kaha.bletools.bluetooth.ui.adapter;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattService;
import com.kaha.bletools.R;
import com.kaha.bletools.bluetooth.utils.GattAttributeResolver;

import java.util.List;

/**
 * 蓝牙数据的适配器
 *
 * @author Darcy
 * @Date 2019/2/12
 * @package com.kaha.bletools.bluetooth.ui.adapter
 * @Desciption
 */
public class GattServiceAdapter extends BaseExpandableListAdapter {
    //获取到的蓝牙设备服务信息
    private List<BleGattService> gattServiceData;
    private Context context;

    public GattServiceAdapter(Context context, List<BleGattService> gattServiceData) {
        this.context = context;
        this.gattServiceData = gattServiceData;
    }

    @Override
    public int getGroupCount() {
        return gattServiceData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return gattServiceData.get(groupPosition).getCharacters().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return gattServiceData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return gattServiceData.get(groupPosition).getCharacters().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        BleGattService gattService = gattServiceData.get(groupPosition);
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_gatt_service_item, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvServiceName = convertView.findViewById(R.id.tv_service_name);
            groupViewHolder.tvUUid = convertView.findViewById(R.id.tv_service_uuid);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvServiceName.setText(GattAttributeResolver.getAttributeName(gattService.getUUID().toString(), context.getString(R.string.unknown_service)));
        groupViewHolder.tvUUid.setText(gattService.getUUID().toString());
        return convertView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        BleGattCharacter gattCharacter = gattServiceData.get(groupPosition).getCharacters().get(childPosition);
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_gatt_character_item, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tvCharacterName = convertView.findViewById(R.id.tv_service_name);
            childViewHolder.tvUUid = convertView.findViewById(R.id.tv_service_uuid);
            childViewHolder.tvProperties = convertView.findViewById(R.id.tv_properties);
            childViewHolder.ivWrite = convertView.findViewById(R.id.iv_write);
            childViewHolder.ivReceive = convertView.findViewById(R.id.iv_receive);
            childViewHolder.tvValue = convertView.findViewById(R.id.tv_value);
            childViewHolder.ivRead = convertView.findViewById(R.id.iv_read);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        childViewHolder.tvCharacterName.setText(GattAttributeResolver.getAttributeName(gattCharacter.getUuid().toString(), context.getString(R.string.unknown_characteristic)));
        childViewHolder.tvUUid.setText("" + gattCharacter.getUuid());
        //childViewHolder.tvValue.setText();
        //获取字段的读写权限
        int property = gattCharacter.getProperty();
        //可读属性
        if ((property & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
            resetView(childViewHolder);
            childViewHolder.tvProperties.setText("READ");
            childViewHolder.ivRead.setVisibility(View.VISIBLE);
        }

        //既可读又可写
        if ((property & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0 && (property & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
            resetView(childViewHolder);
            childViewHolder.tvProperties.setText("WRITE_NO_RESPONSE   NOTIFY");
            childViewHolder.ivWrite.setVisibility(View.VISIBLE);
        }
        //可写权限
        if ((property & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
            resetView(childViewHolder);
            childViewHolder.tvProperties.setText("WRITE");
            childViewHolder.ivWrite.setVisibility(View.VISIBLE);
        }
        //写入无响应权限
        if ((property & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0) {
            resetView(childViewHolder);
            childViewHolder.tvProperties.setText("WRITE_NO_RESPONSE");
            childViewHolder.ivWrite.setVisibility(View.VISIBLE);
        }
        //可通知权限
        if ((property & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
            resetView(childViewHolder);
            childViewHolder.tvProperties.setText("NOTIFY");
            childViewHolder.ivReceive.setVisibility(View.VISIBLE);
        }
        //
        if ((property & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
            resetView(childViewHolder);
            childViewHolder.tvProperties.setText("INDICATE");
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    //组的viewHolder
    static class GroupViewHolder {
        TextView tvServiceName;
        TextView tvUUid;
    }

    //组的子viewHolder
    static class ChildViewHolder {
        TextView tvCharacterName;
        TextView tvUUid;
        TextView tvProperties;
        ImageView ivWrite;
        ImageView ivReceive;
        TextView tvValue;
        ImageView ivRead;
    }

    /**
     * 隐藏权限图标
     *
     * @param childViewHolder 子
     * @return void
     * @date 2019-02-12
     */
    private void resetView(ChildViewHolder childViewHolder) {
        childViewHolder.ivRead.setVisibility(View.GONE);
        childViewHolder.ivReceive.setVisibility(View.GONE);
        childViewHolder.ivWrite.setVisibility(View.GONE);
    }

}
