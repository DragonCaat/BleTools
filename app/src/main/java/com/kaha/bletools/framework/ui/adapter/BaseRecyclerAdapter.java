package com.kaha.bletools.framework.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kaha.bletools.R;
import com.kaha.bletools.framework.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * recycleView的base适配器
 *
 * @author Darcy
 * @Date 2019/2/14
 * @package com.kaha.bletools.framework.ui.adapter
 * @Desciption
 */
public abstract class BaseRecyclerAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    protected Context context;
    protected List<T> datas;

    public BaseRecyclerAdapter(Context context, List<T> datas) {
        this.context = context;
        if (null == datas || datas.size() == 0) {
            this.datas = new ArrayList<>();
        } else {
            this.datas = new ArrayList<>(datas.size());
        }
    }

    public void updateData(List<T> list) {
        this.datas.clear();
        if (list != null && list.size() > 0) {
            this.datas.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void notifyData(T value) {
        datas.add(value);
        notifyDataSetChanged();
    }

    public void clear() {
        if (datas.size() == 0) {
            ToastUtil.show(context, context.getResources().getString(R.string.no_output_data));
            return;
        }
        datas.clear();
        notifyDataSetChanged();
    }

    public List<T>  getDatas(){
        return datas;
    }


    @Override
    public void onBindViewHolder(@NonNull V v, int i) {
        T data = datas.get(i);
        onBindViewHolder(v, data, i);
    }

    protected abstract void onBindViewHolder(V holder, T data, int position);

    @Override
    public int getItemCount() {
        return this.datas.size();
    }
}
