package com.kaha.bletools.framework.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * recycleView的base viewholder
 *
 * @author Darcy
 * @Date 2019/2/14
 * @package com.kaha.bletools.framework.ui.adapter
 * @Desciption
 */
public class BaseRecycleViewHolder extends RecyclerView.ViewHolder {
    protected View itemView;

    public BaseRecycleViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        ButterKnife.bind(this, itemView);

    }

    public View getItemView() {
        return itemView;
    }
}
