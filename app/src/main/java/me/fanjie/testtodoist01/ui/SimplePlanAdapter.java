package me.fanjie.testtodoist01.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import me.fanjie.testtodoist01.R;
import me.fanjie.testtodoist01.model.Plan;

/**
 * Created by fanjie on 2016/5/21.
 */
public class SimplePlanAdapter extends BaseAdapter {

    private Context context;
    private List<Plan> plens;

    public SimplePlanAdapter(Context context, List<Plan> plens) {
        this.context = context;
        this.plens = plens;
    }

    @Override
    public int getCount() {
        if(plens == null) {
            return 0;
        }else {
            return plens.size();
        }
    }

    @Override
    public Plan getItem(int position) {
        return plens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        Plan plan = getItem(position);
        ViewHolder h;
        if(v == null){
            v = LayoutInflater.from(context).inflate(R.layout.item_simple_plan_list,null);
            h = new ViewHolder();
            h.tvPlanName = (TextView) v.findViewById(R.id.tv_plan_name);
            v.setTag(h);
        }else {
            h = (ViewHolder) v.getTag();
        }
        h.tvPlanName.setText(plan.getPlanName());
        return v;
    }

    private class ViewHolder {
        TextView tvPlanName;
    }
}
