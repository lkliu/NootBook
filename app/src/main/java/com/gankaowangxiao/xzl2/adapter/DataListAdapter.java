package com.gankaowangxiao.xzl2.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gankaowangxiao.xzl2.R;

import java.util.List;

/**
 * Created by xiaotantan on 17/10/24.
 */

public class DataListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public DataListAdapter(@Nullable List<String> data) {
        super(R.layout.item_data, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_content, item);
    }
}
