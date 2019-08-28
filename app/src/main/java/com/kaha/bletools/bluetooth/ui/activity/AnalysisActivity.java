package com.kaha.bletools.bluetooth.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kaha.bletools.R;
import com.kaha.bletools.bluetooth.base.AppConst;
import com.kaha.bletools.bluetooth.entity.OutputData;
import com.kaha.bletools.bluetooth.utils.bluetooth.ParseUtil;
import com.kaha.bletools.framework.ui.activity.BaseActivity;
import com.kaha.bletools.framework.widget.CommonTopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 分析 数据的activity
 *
 * @author Darcy
 * @Date 2019/6/21
 * @package com.kaha.bletools.bluetooth.ui.activity
 * @Desciption
 */
public class AnalysisActivity extends BaseActivity {

    @BindView(R.id.topView)
    CommonTopView topView;

    private List<OutputData> datas;

    //用于存放有效数据数组的数组
    private List<List<String>> lists = new ArrayList<>();

    @Override
    protected int setLayoutId() {
        return R.layout.activity_analysis;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topView.setTitle("分析数据");
        datas = (List<OutputData>) getIntent().getSerializableExtra(AppConst.KEY_5);

        Log.i("hello", "onCreate: " + datas.size());

        analysisData();
    }


    List<String> usefulData;//有用的数据的数组

    int circleIndex = 0;//遍历的初始角标

    boolean firstDataFunc = false;//第一个数据是不是有用的数据

    //分析数据
    private void analysisData() {
        if (datas == null || datas.size() == 0) {
            return;
        }
        for (int i = 0; i < datas.size(); i++) {
            if (i == 0) {
                String firstString = ParseUtil.parseFirstData(datas.get(i).getOutputString());
                //如果包含1或者是0，就记录为有效数据的开始
                if (firstString.contains("0") || firstString.contains("1")) {
                    usefulData = new ArrayList<>();
                    usefulData.add(firstString);
                    firstDataFunc = true;
                } else {
                    firstDataFunc = false;
                }
            } else {
                String contentString = ParseUtil.parseContentData(datas.get(i).getOutputString());
                if (contentString.contains("0") || contentString.contains("1")) {
                    if (firstDataFunc) {
                        usefulData.add(contentString);
                    } else {
                        firstDataFunc = true;
                        usefulData = new ArrayList<>();
                        usefulData.add(contentString);
                    }
                } else {
                    //遍历到不含1或者0的时候
                    circleIndex = i;
                    lists.add(usefulData);
                    break;
                }
            }
        }
       // Log.i("hello", "analysisData: " + lists.size());
    }
}
