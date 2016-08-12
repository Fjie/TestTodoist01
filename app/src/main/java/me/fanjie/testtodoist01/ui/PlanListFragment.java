package me.fanjie.testtodoist01.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import me.fanjie.testtodoist01.R;
import me.fanjie.testtodoist01.core.DataCenter;


public class PlanListFragment extends BaseFragment implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener {

    private PullToRefreshListView listView;
    private SimplePlanAdapter adapter;

    public PlanListFragment() {
        // Required empty public constructor
    }


    public static PlanListFragment newInstance() {
        PlanListFragment fragment = new PlanListFragment();

        return fragment;
    }

    @Override
    public String getTitle() {
        return "计划列表";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_plan_list, container, false);
        listView = (PullToRefreshListView) root.findViewById(R.id.lv_plan_list);
        listView.getRefreshableView().setOnItemClickListener(this);
        listView.setOnRefreshListener(this);
        adapter = new SimplePlanAdapter(getContext(), DataCenter.plens);
        listView.getRefreshableView().setAdapter(adapter);
        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PlanDetailsActivity.startActivity(getActivity(), 10086, adapter.getItem(position-1));
    }

    @Override
    public void onDataChanged(int tag) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        DataCenter.getInstance().refreshPlanList(new DataCenter.FindTaskCallback() {
            @Override
            public void lastTaskDone(boolean havaData) {
                if(havaData) {
                    onDataChanged(10086);
                }
                listView.onRefreshComplete();
            }
        });
    }
}
