package com.gankaowangxiao.xzl.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gankaowangxiao.xzl.R;

import java.util.List;

/**
 * Created by xiaotantan on 17/10/24.
 */

public class BottomAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public BottomAdapter(@Nullable List<String> data) {
        super(R.layout.item_number, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_number, item);
    }
}
