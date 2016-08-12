package me.fanjie.testtodoist01.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;

import me.fanjie.testtodoist01.R;
import me.fanjie.testtodoist01.core.DataCenter;


public class CompleteListFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener {

    private PullToRefreshExpandableListView elvPlanTaskList;
    private ElvPlanTaskAdapter adapter;

    public CompleteListFragment() {
        // Required empty public constructor
    }

    public static CompleteListFragment newInstance() {
        CompleteListFragment fragment = new CompleteListFragment();
        return fragment;
    }

    @Override
    public String getTitle() {
        return "已完成";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDataChanged(int tag) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task_list, container, false);
        elvPlanTaskList = (PullToRefreshExpandableListView) root.findViewById(R.id.elv_plan_task_list);
        elvPlanTaskList.setOnRefreshListener(this);
        adapter = new ElvPlanTaskAdapter(getContext(), DataCenter.plens,elvPlanTaskList.getRefreshableView(),true);
        elvPlanTaskList.getRefreshableView().setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return root;
    }

    @Override
    public void onRefresh(final PullToRefreshBase refreshView) {
        DataCenter.getInstance().refreshPlanList(new DataCenter.FindTaskCallback() {
            @Override
            public void lastTaskDone(boolean havaData) {
                if(havaData) {
                    onDataChanged(10086);
                }
                refreshView.onRefreshComplete();
            }
        });
    }


}
