package com.kaha.bletools.bluetooth.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.kaha.bletools.R;
import com.kaha.bletools.bluetooth.ui.adapter.BluetoothAdapter;
import com.kaha.bletools.bluetooth.utils.bluetooth.BluetoothManage;
import com.kaha.bletools.bluetooth.utils.bluetooth.SortByRssi;
import com.kaha.bletools.framework.ui.activity.BaseActivity;
import com.kaha.bletools.framework.widget.CommonTopView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author : Darcy
 * @package com.kaha.bletools.bluetooth
 * @Date 2018-11-9 14:24
 * @Description 搜索蓝牙界面
 */
public class SearchActivity extends BaseActivity {
    @BindView(R.id.topView)
    CommonTopView topView;
    @BindView(R.id.recycleView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_bluetooth_num)
    TextView tvBluetoothNum;
    @BindView(R.id.pb)
    ProgressBar progressBar;

    //展示蓝牙的适配器
    private BluetoothAdapter adapter;
    //用户装设备的list
    private List<SearchResult> list = new ArrayList<>();

    @Override
    protected int setLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topView.setTitle(getString(R.string.search_bluetooth));
        initRecycleView();
        searchBluetooth();
    }

    @OnClick({})
    public void onClick(View view) {
        switch (view.getId()) {

        }
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
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                //device.device.
                if (TextUtils.isEmpty(device.getName()) || "NULL".equals(device.getName())) {
                    return;
                }
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getAddress().equals(device.getAddress())) {
                            return;
                        }
                    }
                    list.add(device);
                    //adapter.notifyDataSetChanged();
                } else {
                    list.add(device);
                    //adapter.notifyDataSetChanged();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvBluetoothNum.setText("" + list.size());
                        //排序
                        Collections.sort(list, new SortByRssi());
                        //adapter.clear();
                        // adapter.setListAll(list);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onSearchStopped() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onSearchCanceled() {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //停止搜索
        BluetoothManage.getInstance().getBluetoothClient().stopSearch();
        progressBar.setVisibility(View.GONE);
    }
}
