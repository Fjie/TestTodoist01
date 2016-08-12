package me.fanjie.testtodoist01.ui;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;

import java.util.List;

import me.fanjie.testtodoist01.R;
import me.fanjie.testtodoist01.core.DataCenter;
import me.fanjie.testtodoist01.model.Plan;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskListFragment extends BaseFragment implements ExpandableListView.OnChildClickListener, PullToRefreshBase.OnRefreshListener, ExpandableListView.OnGroupClickListener {

    private PullToRefreshExpandableListView elvPlanTaskList;
    private ElvPlanTaskAdapter adapter;

    public static TaskListFragment newInstance() {
        TaskListFragment fragment = new TaskListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task_list, container, false);
        elvPlanTaskList = (PullToRefreshExpandableListView) root.findViewById(R.id.elv_plan_task_list);
        elvPlanTaskList.getRefreshableView().setOnChildClickListener(this);
        elvPlanTaskList.getRefreshableView().setOnGroupClickListener(this);
        elvPlanTaskList.setOnRefreshListener(this);
        dataCenter.getPlanList(new DataCenter.FindPlanCallback() {
            @Override
            public void done(List<Plan> plans) {
                adapter = new ElvPlanTaskAdapter(getContext(), plans, elvPlanTaskList.getRefreshableView());
            }
        }, new DataCenter.FindTaskCallback() {
            @Override
            public void lastTaskDone(boolean havaData) {
                    elvPlanTaskList.getRefreshableView().setAdapter(adapter);
                if(havaData) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
        return root;
    }


    @Override
    public String getTitle() {
        return "正在进行";
    }

    @Override
    public void onDataChanged(int tag) {
        if(DataCenter.plens!=null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, final int childPosition, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("任务完成？");
        builder.setNegativeButton("未完成", null);
        builder.setPositiveButton("已完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.completeTask(groupPosition, childPosition);
            }
        });
        builder.show();
        return true;
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

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        PlanDetailsActivity.startActivity(getActivity(), 10086, adapter.getGroup(groupPosition));
        return true;
    }
}
