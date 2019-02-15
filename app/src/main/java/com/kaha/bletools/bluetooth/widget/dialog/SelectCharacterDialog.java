package com.kaha.bletools.bluetooth.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.kaha.bletools.R;
import com.kaha.bletools.bluetooth.ui.adapter.GattServiceAdapter;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 选择蓝牙服务的dialog
 *
 * @author Darcy
 * @Date 2019/2/12
 * @package com.kaha.bletools.bluetooth.widget.dialog
 * @Desciption
 */
public abstract class SelectCharacterDialog extends Dialog {


    @BindView(R.id.expand_services_list)
    ExpandableListView expandableListView;

    private Activity activity;
    private BleGattProfile data;

    public SelectCharacterDialog(Activity activity, BleGattProfile data) {
        super(activity, R.style.defaultDialog);
        this.activity = activity;
        this.data = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_characteristic);
        setCanceledOnTouchOutside(true);//外部点击取消
        ButterKnife.bind(this);

        initExpandableList();
    }

    /**
     * 初始化view
     *
     * @param ,
     * @return void
     * @date 2019-02-12
     */
    private void initExpandableList() {
        GattServiceAdapter adapter = new GattServiceAdapter(activity, data.getServices());
        expandableListView.setAdapter(adapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                dismiss();
                //group的service值
                BleGattService service = data.getServices().get(groupPosition);
                BleGattCharacter character = service.getCharacters().get(childPosition);
                int property = character.getProperty();
                //点击的是可写的字段
                if ((property & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                    clickWrite(service.getUUID(), character.getUuid());
                }
                //点击的是可读的字段
                if ((property & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                    clickRead(service.getUUID(), character.getUuid());
                }
                //点击的是通知的字段
                if ((property & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    clickNotify(service.getUUID(), character.getUuid());
                }
                return true;
            }
        });
    }

    /**
     * 点击的是可写的characteristic
     */
    public abstract void clickWrite(UUID serviceUuid, UUID characterUuid);

    /**
     * 点击的是可读的characteristic
     */
    public abstract void clickRead(UUID serviceUuid, UUID characterUuid);

    /**
     * 点击的是可通知的characteristic
     */
    public abstract void clickNotify(UUID serviceUuid, UUID characterUuid);
}
