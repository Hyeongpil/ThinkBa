package com.example.kwan.thinkba.Daum_search;

import java.util.List;

/**
 * Created by pixeleye03 on 2015-10-13.
 */
public interface OnFinishSearchListener {
    public void onSuccess(List<Item> itemList);
    public void onFail();
}
