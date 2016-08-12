package me.fanjie.testtodoist01.ui;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.List;

import me.fanjie.testtodoist01.R;
import me.fanjie.testtodoist01.core.DataCenter;
import me.fanjie.testtodoist01.model.Plan;
import me.fanjie.testtodoist01.model.Task;

/**
 * Created by fanjie on 2016/5/19.
 */
public class ElvPlanTaskAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Plan> plens;
    private ExpandableListView elv;
    private boolean showComplete = false;

    public ElvPlanTaskAdapter(Context context, List<Plan> plens, ExpandableListView elv) {
        this.context = context;
        this.plens = plens;
        this.elv = elv;
    }

    public ElvPlanTaskAdapter(Context context, List<Plan> plens, ExpandableListView elv, boolean showComplete) {
        this(context, plens, elv);
        this.showComplete = showComplete;
    }

    public void completeTask(final int groupPosition, final int childPosition) {
        Task task = getChild(groupPosition, childPosition);
        task.setComplete(true);
        DataCenter.getInstance().addTask(task, new DataCenter.PutDoneCallback() {
            @Override
            public void done(String id) {
                getGroup(groupPosition).removeTask(childPosition);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getGroupCount() {
        if (plens != null) {
            return plens.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<Task> tasks;
        if (showComplete) {
            tasks = getGroup(groupPosition).getCompleteTasks();
        } else {
            tasks = getGroup(groupPosition).getTasks();
        }
        if (tasks != null) {
            return tasks.size();
        } else {
            return 0;
        }
    }

    @Override
    public Plan getGroup(int groupPosition) {
        if (plens != null) {
            return plens.get(groupPosition);
        } else {
            return null;
        }
    }

    @Override
    public Task getChild(int groupPosition, int childPosition) {
        Plan plan = getGroup(groupPosition);
        if (plan != null) {
            List<Task> tasks;
            if (showComplete) {
                tasks = plan.getCompleteTasks();
            } else {
                tasks = plan.getTasks();
            }
            if (tasks != null) {
                return tasks.get(childPosition);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        for (int i = 0; i < getGroupCount(); i++) {
            elv.expandGroup(i);
        }
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View v, ViewGroup parent) {
        Plan plan = getGroup(groupPosition);
        PlanViewHolder h;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.item_plan_list, null);
            h = new PlanViewHolder();
            h.tvAuthor = (TextView) v.findViewById(R.id.tv_author);
            h.tvPlanName = (TextView) v.findViewById(R.id.tv_plan_name);
            h.tvUserCount = (TextView) v.findViewById(R.id.tv_user_count);
            h.tvTaskCount = (TextView) v.findViewById(R.id.tv_task_count);
            v.setTag(h);
        } else {
            h = (PlanViewHolder) v.getTag();
        }
        h.tvPlanName.setText(plan.getPlanName());
        h.tvAuthor.setText(plan.getAuthorName()+"：");
        h.tvUserCount.setText("成员："+plan.getUserCount());
        h.tvTaskCount.setText("任务："+ plan.getTaskCount());
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View v, ViewGroup parent) {
        Task task = getChild(groupPosition, childPosition);
        TaskViewHolder h;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.item_task_list, null);
            h = new TaskViewHolder();
            h.tvTaskName = (TextView) v.findViewById(R.id.tv_task_name);
            h.tvTaskTime = (TextView) v.findViewById(R.id.tv_task_time);
            h.tvAuthor = (TextView) v.findViewById(R.id.tv_author);
            v.setTag(h);
        } else {
            h = (TaskViewHolder) v.getTag();
        }
        h.tvTaskName.setText(task.getTaskName());
        if(task.isComplete()){
            h.tvTaskName.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        h.tvTaskTime.setText(task.getTaskTimeString());
        h.tvAuthor.setText("来自 "+task.getAuthorName());
        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class PlanViewHolder {
        TextView tvAuthor;
        TextView tvPlanName;
        TextView tvUserCount;
        TextView tvTaskCount;
    }

    private class TaskViewHolder {
        TextView tvTaskName;
        TextView tvTaskTime;
        TextView tvAuthor;
    }
}
