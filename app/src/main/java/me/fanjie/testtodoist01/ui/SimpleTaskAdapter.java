package me.fanjie.testtodoist01.ui;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import me.fanjie.testtodoist01.R;
import me.fanjie.testtodoist01.core.DataCenter;
import me.fanjie.testtodoist01.model.Plan;
import me.fanjie.testtodoist01.model.Task;

/**
 * Created by fanjie on 2016/6/1.
 */
public class SimpleTaskAdapter extends BaseAdapter {

    private Context context;
    private List<Task> tasks;

    public SimpleTaskAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    public void completeTask(final Plan plan, final int positon){
        Task task = getItem(positon);
        task.setComplete(true);
        DataCenter.getInstance().addTask(task, new DataCenter.PutDoneCallback() {
            @Override
            public void done(String id) {
                plan.removeTask(positon);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        if(tasks == null){
            return 0;
        }else {
            return tasks.size();
        }
    }

    @Override
    public Task getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        Task task = getItem(position);
        TaskViewHolder h;
        if(v == null){
            v = LayoutInflater.from(context).inflate(R.layout.item_simple_task_list,null);
            h = new TaskViewHolder();
            h.tvTaskName = (TextView) v.findViewById(R.id.tv_task_name);
            h.tvTaskTime = (TextView) v.findViewById(R.id.tv_task_time);
            v.setTag(h);
        }else {
            h = (TaskViewHolder) v.getTag();
        }
        h.tvTaskName.setText(task.getTaskName());
        if(task.isComplete()){
            h.tvTaskName.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        h.tvTaskTime.setText(task.getTaskTimeString());
        return v;
    }

    private class TaskViewHolder{
        TextView tvTaskName;
        TextView tvTaskTime;
    }
}
