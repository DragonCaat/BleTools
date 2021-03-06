package com.kaha.bletools.bluetooth.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
public class SearchActivity extends BaseActivity implements OnRefreshListener {
    @BindView(R.id.recycleView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_bluetooth_num)
    TextView tvBluetoothNum;
    @BindView(R.id.pb)
    ProgressBar progressBar;
    //搜索
    @BindView(R.id.tv_search)
    TextView tvSearch;

    @BindView(R.id.swipeRefresh)
    SmartRefreshLayout mSwipeRefreshLayout;

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
        initRecycleView();
        searchBluetooth();
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @OnClick({R.id.rl_back, R.id.tv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            //搜索
            case R.id.tv_search:
                list.clear();
                tvBluetoothNum.setText("0");
                adapter.notifyDataSetChanged();
                searchBluetooth();
                break;
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
            public void
            onSearchStarted() {
                progressBar.setVisibility(View.VISIBLE);
                tvSearch.setVisibility(View.GONE);

                isRefresh = true;
            }

            @Override
            public void onDeviceFounded(SearchResult device) {
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
                } else {
                    list.add(device);
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
                if (iaActivityAlive) {
                    progressBar.setVisibility(View.GONE);
                    tvSearch.setVisibility(View.VISIBLE);
                    isRefresh = false;
                }
            }

            @Override
            public void onSearchCanceled() {
                if (iaActivityAlive)
                    tvSearch.setVisibility(View.VISIBLE);
                isRefresh = false;
            }
        });
    }

    private boolean iaActivityAlive = true;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        iaActivityAlive = false;
        isRefresh = false;
        //停止搜索
        BluetoothManage.getInstance().getBluetoothClient().stopSearch();
        progressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.finishRefresh();
    }

    private boolean isRefresh = true;//是否正在刷新

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (isRefresh) {
            mSwipeRefreshLayout.finishRefresh();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        tvSearch.setVisibility(View.GONE);
        list.clear();
        tvBluetoothNum.setText("0");
        adapter.notifyDataSetChanged();
        searchBluetooth();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.finishRefresh();
            }
        }, 500);
    }

    Handler handler = new Handler();
}
