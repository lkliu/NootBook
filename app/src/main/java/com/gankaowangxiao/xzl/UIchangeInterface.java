package com.gankaowangxiao.xzl;

import java.util.ArrayList;

/**
 * Created by xiaotantan on 17/10/24.
 */

public interface UIchangeInterface {
    void UpdataLists(ArrayList<String> strings);

    void UpdataItem(int position, String str);

    void Back(ArrayList<String> strings);

    void onDetermine(int postion, String str);

    void backTwo();

    void dissMiss();

    void onClose();

    void onCommitData(ArrayList<String> datas);
}
