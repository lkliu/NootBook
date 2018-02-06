package com.gankaowangxiao.xzl2.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.gankaowangxiao.xzl2.R;
import com.gankaowangxiao.xzl2.UIchangeInterface;
import com.gankaowangxiao.xzl2.adapter.CardListsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaotantan on 17/10/20.
 * 输入卡号页面
 */

public class ListsFrragment extends Fragment implements View.OnClickListener {

    private ArrayList<String> CardNumberList = new ArrayList<>();
    private CardListsAdapter cardListsAdapter;
    private RecyclerView recyList;
    private RelativeLayout rlBack;
    private TextView tvNum, tvContent;
    private static String KEY = "lists";
    private UIchangeInterface Interface;

    public static ListsFrragment newInstance(ArrayList<String> lists) {
        ListsFrragment fragment = new ListsFrragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(KEY, lists);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setUpdataListener(UIchangeInterface listener) {
        this.Interface = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(KEY)) {
            CardNumberList = getArguments().getStringArrayList(KEY);
        }
    }

    public void ChangeData(List<String> updateData) {
        CardNumberList.clear();
        CardNumberList.addAll(updateData);
        cardListsAdapter.setNewData(CardNumberList);
        changeUI(CardNumberList);
    }

    public void onUpdataPosition(int position, String str) {
        String string = getString(str);
        CardNumberList.set(position, string);
        cardListsAdapter.setNewData(CardNumberList);
        changeUI(CardNumberList);
    }

    public void changeUI(ArrayList<String> list) {
        if (list.size() > 0) {
            int size = 0;
            for (String str : CardNumberList) {
                size += getLong(str);
            }
            tvNum.setText("复核录入(" + size + ")");
            tvContent.setVisibility(View.GONE);
        } else {
            tvNum.setText("复核录入");
            tvContent.setVisibility(View.VISIBLE);
        }
    }

    public long getLong(String str) {
        if (str.contains("-")) {
            String str1 = str.trim().substring(0, str.indexOf("-"));
            String str2 = str.trim().substring(str.lastIndexOf("-") + 1, str.length());
            long strOne = Long.parseLong(str1.trim());
            long strTwo = Long.parseLong(str2.trim());
            long size = (strTwo - strOne) + 1;
            return size;
        } else {
            return 1;
        }
    }

    public String getString(String str) {
        if (str.contains("-")) {
            String[] splitStr = str.split("-");
            long strOne = Long.parseLong(splitStr[0]);
            long strTwo = Long.parseLong(splitStr[1]);
            long size = (strTwo - strOne) + 1;
            String sss = splitStr[0] + " - (" + size + "" + ") - " + splitStr[1];
            return sss;
        } else {
            return str;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_lists, container, false);
        recyList = (RecyclerView) view.findViewById(R.id.recy_list);
        rlBack = (RelativeLayout) view.findViewById(R.id.rl_back);
        tvNum = (TextView) view.findViewById(R.id.tv_num);
        tvContent = (TextView) view.findViewById(R.id.tv_content);
        initData();
        return view;
    }

    private void initData() {
        tvNum.setText("复核录入");
        rlBack.setOnClickListener(this);
        cardListsAdapter = new CardListsAdapter(CardNumberList);
        recyList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyList.setAdapter(cardListsAdapter);
        recyList.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String string = cardListsAdapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.iv_update:
                        if (Interface != null) {
                            String str;
                            if (string.contains("-")) {
                                String substring = string.trim().substring(0, string.trim().indexOf(" "));
                                String substring1 = string.trim().substring(string.trim().lastIndexOf(" ") + 1, string.trim().length());
                                str = substring.trim() + "-" + substring1.trim();
                                Interface.UpdataItem(position, str.trim());
                            } else {
                                Interface.UpdataItem(position, string);
                            }
                        }
                        break;
                    case R.id.iv_remove:
                        CardNumberList.remove(position);
                        cardListsAdapter.notifyItemRemoved(position);
                        changeUI(CardNumberList);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                if (Interface != null) {
                    Interface.Back(CardNumberList);
                }
                break;
            default:
                break;
        }
    }

}
