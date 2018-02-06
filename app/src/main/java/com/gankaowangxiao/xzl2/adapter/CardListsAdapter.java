package com.gankaowangxiao.xzl2.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gankaowangxiao.xzl2.R;

import java.util.List;

/**
 * Created by xiaotantan on 17/10/24.
 */

public class CardListsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public CardListsAdapter(@Nullable List<String> data) {
        super(R.layout.item_lists, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_content, item);
        helper.addOnClickListener(R.id.iv_update).addOnClickListener(R.id.iv_remove);
    }
}
