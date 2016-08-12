package me.fanjie.testtodoist01.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.fanjie.testtodoist01.R;
import me.fanjie.testtodoist01.model.Plan;


/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class PlanListRecyclerViewAdapter extends RecyclerView.Adapter<PlanListRecyclerViewAdapter.ViewHolder> {

//    private final List<DummyItem> mValues;
//
//    public PlanListRecyclerViewAdapter(List<DummyItem> items) {
//        mValues = items;
//    }

    private final List<Plan> plans;
    public PlanListRecyclerViewAdapter(List<Plan> plans) {
        this.plans = plans;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fragment_plan_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = plans.get(position);
        holder.mContentView.setText(plans.get(position).getPlanName());
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public Plan mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
