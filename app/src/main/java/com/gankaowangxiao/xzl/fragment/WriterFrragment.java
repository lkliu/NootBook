package com.gankaowangxiao.xzl.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gankaowangxiao.xzl.R;
import com.gankaowangxiao.xzl.UIchangeInterface;
import com.gankaowangxiao.xzl.adapter.BottomAdapter;
import com.gankaowangxiao.xzl.utils.Constance;
import com.gankaowangxiao.xzl.utils.DimensUtils;
import com.gankaowangxiao.xzl.utils.SoundPlayUtils;
import com.gankaowangxiao.xzl.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaotantan on 17/10/20.
 * 输入卡号页面
 */

public class WriterFrragment extends Fragment implements View.OnClickListener {
    private LinearLayout llCommit, llLeft, llRight, llNum;
    private RelativeLayout llClose;
    private RecyclerView recyNum;
    private ImageView ivCheck, ivClear, ivCommit;
    private TextView tvShow, tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv0, tvGone, tvTitle;
    private StringBuilder showText;

    private ArrayList<String> CardNumberList;
    private BottomAdapter bottomAdapter;
    private UIchangeInterface Interface;

    public static WriterFrragment newInstance() {
        WriterFrragment fragment = new WriterFrragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setUpdataListener(UIchangeInterface listener) {
        this.Interface = listener;
    }

    public void ChangeData(List<String> updateData) {
        CardNumberList.clear();
        CardNumberList.addAll(updateData);
        bottomAdapter.setNewData(CardNumberList);
        changeUI(CardNumberList);
    }

    public void changeUI(ArrayList<String> list) {
        if (list.size() > 0) {
            int size = 0;
            for (String str : CardNumberList) {
                size += getLong(str);
            }
            tvTitle.setText("批量关卡(" + size + ")");
        } else {
            tvTitle.setText("批量关卡");
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_writer, container, false);
        recyNum = (RecyclerView) view.findViewById(R.id.recy_num);
        ivCheck = (ImageView) view.findViewById(R.id.iv_check);
        ivCommit = (ImageView) view.findViewById(R.id.iv_commit);
        ivClear = (ImageView) view.findViewById(R.id.iv_clear);
        tvShow = (TextView) view.findViewById(R.id.tv_show);
        tvGone = (TextView) view.findViewById(R.id.tv_gone);
        tv0 = (TextView) view.findViewById(R.id.tv_0);
        tv1 = (TextView) view.findViewById(R.id.tv_1);
        tv2 = (TextView) view.findViewById(R.id.tv_2);
        tv3 = (TextView) view.findViewById(R.id.tv_3);
        tv4 = (TextView) view.findViewById(R.id.tv_4);
        tv5 = (TextView) view.findViewById(R.id.tv_5);
        tv6 = (TextView) view.findViewById(R.id.tv_6);
        tv7 = (TextView) view.findViewById(R.id.tv_7);
        tv8 = (TextView) view.findViewById(R.id.tv_8);
        tv9 = (TextView) view.findViewById(R.id.tv_9);
        tvTitle = (TextView) view.findViewById(R.id.tv_num);
        llCommit = (LinearLayout) view.findViewById(R.id.ll_commit);
        llLeft = (LinearLayout) view.findViewById(R.id.ll_left);
        llRight = (LinearLayout) view.findViewById(R.id.ll_right);
        llNum = (LinearLayout) view.findViewById(R.id.ll_num);
        llClose = (RelativeLayout) view.findViewById(R.id.ll_close);
        ViewGroup.LayoutParams layoutParams = llNum.getLayoutParams();
        layoutParams.height = (int) ((Util.getScreenWidth(getContext()) - DimensUtils.dipToPx(getContext(), 20)) * 0.87);
        llNum.setLayoutParams(layoutParams);
        setOnClick();
        initShowText();
        return view;
    }

    private void setOnClick() {
        tv0.setOnClickListener(this);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);
        tv6.setOnClickListener(this);
        tv7.setOnClickListener(this);
        tv8.setOnClickListener(this);
        tv9.setOnClickListener(this);
        llLeft.setOnClickListener(this);
        llRight.setOnClickListener(this);
        llCommit.setOnClickListener(this);
        ivCheck.setOnClickListener(this);
        ivClear.setOnClickListener(this);
        llClose.setOnClickListener(this);
        ivCommit.setOnClickListener(this);

        tvShow.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    tvGone.setVisibility(View.VISIBLE);
                } else {
                    tvGone.setVisibility(View.GONE);
                }
                if (s.toString().contains("-")) {
                    llLeft.setEnabled(false);
                } else {
                    llLeft.setEnabled(true);
                }
            }
        });
    }

    //初始化
    private void initShowText() {
        CardNumberList = new ArrayList<>();
        bottomAdapter = new BottomAdapter(CardNumberList);
        showText = new StringBuilder();
        tvShow.setText("");
        //初始化底部数字列表RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyNum.setLayoutManager(layoutManager);
        recyNum.setAdapter(bottomAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_0:
                SoundPlayUtils.play(Constance.ZERO);
                ChangeShowText(0);
                break;
            case R.id.tv_1:
                SoundPlayUtils.play(Constance.ONE);
                ChangeShowText(1);
                break;
            case R.id.tv_2:
                SoundPlayUtils.play(Constance.TWO);
                ChangeShowText(2);
                break;
            case R.id.tv_3:
                SoundPlayUtils.play(Constance.THREE);
                ChangeShowText(3);
                break;
            case R.id.tv_4:
                SoundPlayUtils.play(Constance.FOUR);
                ChangeShowText(4);
                break;
            case R.id.tv_5:
                SoundPlayUtils.play(Constance.FIVE);
                ChangeShowText(5);
                break;
            case R.id.tv_6:
                SoundPlayUtils.play(Constance.SIX);
                ChangeShowText(6);
                break;
            case R.id.tv_7:
                SoundPlayUtils.play(Constance.SEVEN);
                ChangeShowText(7);
                break;
            case R.id.tv_8:
                SoundPlayUtils.play(Constance.EIGHT);
                ChangeShowText(8);
                break;
            case R.id.tv_9:
                SoundPlayUtils.play(Constance.NINE);
                ChangeShowText(9);
                break;
            //提交
            case R.id.ll_commit:
                SoundPlayUtils.play(Constance.ENTER);
                String showStr = tvShow.getText().toString();
                if (isTrue(showStr)) {
                    String string = getString(showStr);
                    CardNumberList.add(string);
                    changeUI(CardNumberList);
                    bottomAdapter.setData(bottomAdapter.getData().size() > 0 ? bottomAdapter.getData().size() - 1 : 0, string);
                    recyNum.smoothScrollToPosition(bottomAdapter.getItemCount() - 1);
                    Clean();
                }
                break;
            //-
            case R.id.ll_left:
                SoundPlayUtils.play(Constance.SPACE);
                ChangeShowText(10);
                break;
            //删除
            case R.id.ll_right:
                SoundPlayUtils.play(Constance.BACK);
                ChangeShowText(11);
                break;
            //复核
            case R.id.iv_check:
                if (CardNumberList.size() == 0) {
                    Util.makeText(getContext(), "请先录入卡号");
                    return;
                }
                if (Interface != null) {
                    Interface.UpdataLists(CardNumberList);
                }
                break;
            case R.id.iv_clear:
                Clean();
                break;
            case R.id.ll_close:
                //关闭
                if (CardNumberList.size() > 0) {
                    if (Interface != null) {
                        Interface.onClose();
                    }
                } else {
                    if (Interface != null) {
                        Interface.dissMiss();
                    }
                }
                break;
            case R.id.iv_commit:
                if (CardNumberList.size() > 0) {
                    //展示提交数据
                    if (Interface != null) {
                        Interface.onCommitData(CardNumberList);
                    }
                } else {
                    if (Interface != null) {
                        Interface.dissMiss();
                    }
                }
                break;
            default:
                break;
        }
    }

    //清空
    private void Clean() {
        llLeft.setEnabled(true);
        showText.delete(0, showText.length());
        tvShow.setText("");
    }

    private boolean isTrue(String str) {
        if (str.contains("-")) {
            String[] splitStr = str.split("-");
            if (splitStr.length < 2) {
                Util.makeText(getContext(), "请输入正确的卡号码段");
                return false;
            }
            if (splitStr[0].equals("") || splitStr[1].equals("")) {
                Util.makeText(getContext(), "我知道了-----卡号不能为空");
                return false;
            }
            long strOne = Long.parseLong(splitStr[0]);
            long strTwo = Long.parseLong(splitStr[1]);
            if (strTwo > strOne) {
                return true;
            } else {
                Util.makeText(getContext(), "卡段号码输入不正确");
                return false;
            }
        } else {
            if (str.trim().length() == 0) {
                Util.makeText(getContext(), "请先输入卡号");
                return false;
            }
            return true;
        }
    }

    //更改显示TextView
    private void ChangeShowText(int i) {
        switch (i) {
            case 10:
                showText.append("-");
                llLeft.setEnabled(false);
                break;
            case 11:
                if (showText.length() == 0) {
                    return;
                }
                showText.deleteCharAt(showText.length() - 1);
                break;
            default:
                showText.append(i + "");
                break;
        }
        tvShow.setText(showText.toString());
    }
}
