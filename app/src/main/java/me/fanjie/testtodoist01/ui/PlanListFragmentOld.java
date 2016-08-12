package me.fanjie.testtodoist01.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.fanjie.testtodoist01.R;
import me.fanjie.testtodoist01.core.DataCenter;
import me.fanjie.testtodoist01.model.Plan;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class PlanListFragmentOld extends BaseFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private RecyclerView recyclerView;
    private PlanListRecyclerViewAdapter adapter;

    public PlanListFragmentOld() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PlanListFragmentOld newInstance(int columnCount) {
        PlanListFragmentOld fragment = new PlanListFragmentOld();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_list_old, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            dataCenter.getPlanList(new DataCenter.FindPlanCallback() {
                @Override
                public void done(List<Plan> plans) {
                    adapter = new PlanListRecyclerViewAdapter(plans);
                    recyclerView.setAdapter(adapter);
                }
            });

        }
        return view;
    }


    @Override
    public void onDataChanged(int tag) {
        adapter.notifyDataSetChanged();
    }
}
