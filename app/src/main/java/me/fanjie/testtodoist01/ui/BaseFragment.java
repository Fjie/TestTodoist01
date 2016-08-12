package me.fanjie.testtodoist01.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import me.fanjie.testtodoist01.core.DataCenter;

/**
 * Created by fanjie on 2016/5/26.
 */
public abstract class BaseFragment extends Fragment {

    protected DataCenter dataCenter;
    public abstract String getTitle();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataCenter = DataCenter.getInstance();
    }

    public abstract void onDataChanged(int tag);

}
