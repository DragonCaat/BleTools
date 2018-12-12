package com.kaha.bletools.bluetooth.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.kaha.bletools.R;
import com.kaha.bletools.bluetooth.entity.BluetoothEntity;
import com.kaha.bletools.bluetooth.ui.adapter.BluetoothAdapter;
import com.kaha.bletools.bluetooth.utils.bluetooth.BleBluetoothHelper;
import com.kaha.bletools.bluetooth.utils.bluetooth.BluetoothManage;
import com.kaha.bletools.bluetooth.utils.bluetooth.SortByRssi;
import com.kaha.bletools.framework.ui.activity.BaseActivity;
import com.kaha.bletools.framework.widget.CommonTopView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * @author : Darcy
 * @package com.kaha.bletools.bluetooth
 * @Date 2018-11-9 14:24
 * @Description 搜索蓝牙界面
 */
public class SearchActivity extends BaseActivity {
    @BindView(R.id.toView)
    CommonTopView topView;

    @BindView(R.id.recycleView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_bluetooth_num)
    TextView tvBluetoothNum;

    //展示蓝牙的适配器
    private BluetoothAdapter adapter;
    //用户装设备的list
    private List<BluetoothEntity> list = new ArrayList<>();

    //蓝牙帮助类
    private BleBluetoothHelper bluetoothHelper;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topView.setTitle("正在搜索蓝牙");
        initRecycleView();
        searchBluetooth();
    }

    /**
     * 初始化recycleView
     *
     * @return void
     * @Date 2018-11-12
     */
    private void initRecycleView() {
        adapter = new BluetoothAdapter(list, context);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        recyclerView.setAdapter(adapter);
    }


    /**
     * 搜索蓝牙
     *
     * @return void
     * @Date 2018-11-09
     */
    private void searchBluetooth() {
        BluetoothManage.getInstance().scanBluetooth(new SearchResponse() {
            @Override
            public void onSearchStarted() {

            }

            @Override
            public void onDeviceFounded(SearchResult device) {
//                Beacon beacon = new Beacon(device.scanRecord);
//                Log.i("hello", "onDeviceFounded: " +
//                        String.format("beacon for %s\n%s", device.getAddress(), beacon.toString()));

                if (TextUtils.isEmpty(device.getName()) || "NULL".equals(device.getName())) {
                    return;
                }

                if (list.size() > 0) {
                    boolean same = bluetoothHelper.isSame(device, list);
                    if (same) {
                        return;
                    }else {
                        BluetoothEntity entity = bluetoothHelper.getEntity(device);
                        list.add(entity);
                    }
                }else {
                    BluetoothEntity entity = bluetoothHelper.getEntity(device);
                    list.add(entity);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvBluetoothNum.setText("" + list.size());
                        //排序
                        Collections.sort(list, new SortByRssi());
                        adapter.notifyDataSetChanged();
                    }
                });

            }

            @Override
            public void onSearchStopped() {

            }

            @Override
            public void onSearchCanceled() {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null == bluetoothHelper) {
            bluetoothHelper = new BleBluetoothHelper();
        }
    }
}
