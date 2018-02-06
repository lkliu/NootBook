package com.gankaowangxiao.xzl2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.gankaowangxiao.xzl2.adapter.TabPagerAdapter;
import com.gankaowangxiao.xzl2.fragment.DetermineFragment;
import com.gankaowangxiao.xzl2.fragment.ListsFrragment;
import com.gankaowangxiao.xzl2.fragment.WriterFrragment;
import com.gankaowangxiao.xzl2.popup.DialogPopup;
import com.gankaowangxiao.xzl2.popup.RecyclerViewPopup;
import com.gankaowangxiao.xzl2.utils.DimensUtils;
import com.gankaowangxiao.xzl2.utils.Util;

import java.util.ArrayList;
import java.util.List;

import static com.gankaowangxiao.xzl2.R.id.view;

/**
 * Created by Liko on 18/1/20.
 */

public class CardManagerActivity extends AppCompatActivity implements UIchangeInterface, onDisMissListener {
    private UIchangeInterface uIchangeInterface;
    private WriterFrragment frragmentWriter;
    private ListsFrragment frragmentLists;
    private DetermineFragment determineFragment;
    private WrapContentHeightViewPager viewPager;
    private View view1;
    private onDisMissListener disMissListener;

    private String showStr = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_popupwindow);
        showStr = getIntent().getExtras().getString("title");
        setDissMissListener(this);
        viewPager = (WrapContentHeightViewPager) findViewById(R.id.view_page);
        ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
        layoutParams.height = (int) (((Util.getScreenWidth(getApplicationContext()) - DimensUtils.dipToPx(getApplicationContext(), 20)) * 0.87) + DimensUtils.dipToPx(getApplicationContext(), 230));
        viewPager.setLayoutParams(layoutParams);
        view1 = findViewById(view);
        initDate();
    }

    private void initDate() {
        List fragments = getFragments();
        TabPagerAdapter viewpagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(viewpagerAdapter);
        viewPager.setOffscreenPageLimit(3);
    }

    public void setDissMissListener(onDisMissListener dissMissListener) {
        this.disMissListener = dissMissListener;
    }

    private List getFragments() {
        List fList = new ArrayList();
        uIchangeInterface = this;
        frragmentWriter = WriterFrragment.newInstance(showStr);
        frragmentWriter.setUpdataListener(uIchangeInterface);
        fList.add(frragmentWriter);
        frragmentLists = ListsFrragment.newInstance(new ArrayList<String>());
        frragmentLists.setUpdataListener(uIchangeInterface);
        fList.add(frragmentLists);
        determineFragment = DetermineFragment.newInstance("");
        determineFragment.setUpdataListener(uIchangeInterface);
        fList.add(determineFragment);
        return fList;
    }

    @Override
    public void UpdataLists(ArrayList<String> strings) {
        if (frragmentLists != null) {
            frragmentLists.ChangeData(strings);
        }
        viewPager.setCurrentItem(1);
    }

    @Override
    public void UpdataItem(int postion, String str) {
        if (determineFragment != null) {
            determineFragment.changeData(postion, str);
        }
        viewPager.setCurrentItem(2);
    }

    @Override
    public void Back(ArrayList<String> strings) {
        if (frragmentWriter != null) {
            frragmentWriter.ChangeData(strings);
        }
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onDetermine(int position, String str) {
        if (frragmentLists != null) {
            frragmentLists.onUpdataPosition(position, str);
        }
        viewPager.setCurrentItem(1);
    }

    @Override
    public void backTwo() {
        viewPager.setCurrentItem(1);
    }

    @Override
    public void dissMiss() {
        finish();
    }

    @Override
    public void onClose() {
        //提示是否关闭
        DialogPopup popup = new DialogPopup(CardManagerActivity.this);
        popup.setUpdataListener(uIchangeInterface);
        popup.showPopupWindow();
    }

    @Override
    public void onCommitData(ArrayList<String> datas) {
        RecyclerViewPopup recyclerViewPopup = new RecyclerViewPopup(CardManagerActivity.this, datas);
        recyclerViewPopup.setUpdataListener(uIchangeInterface);
        recyclerViewPopup.setCommitListener(disMissListener);
        recyclerViewPopup.showPopupWindow();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, R.anim.activity_close);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem > 0) {
                switch (currentItem) {
                    case 2:
                        viewPager.setCurrentItem(1);
                        break;
                    case 1:
                        viewPager.setCurrentItem(0);
                        break;
                    default:
                        break;
                }
                return true;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCommitDate(String dates) {
        Intent intent = new Intent();
        intent.putExtra("date", dates);
        CardManagerActivity.this.setResult(RESULT_OK, intent);
        finish();
    }
}
