package me.fanjie.testtodoist01.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import me.fanjie.testtodoist01.R;
import me.fanjie.testtodoist01.core.C;
import me.fanjie.testtodoist01.core.DataCenter;
import me.fanjie.testtodoist01.model.Plan;
import me.fanjie.testtodoist01.utils.UiUtil;

public class PlanDetailsActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private static Plan plan;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private List<BaseFragment> fragmentList = new ArrayList<>();

    public static void startActivity(Activity activity, int requestCode, Plan plan) {
        PlanDetailsActivity.plan = plan;
        Intent i = new Intent(activity, PlanDetailsActivity.class);
        activity.startActivityForResult(i, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTaskActivity.startActivity(PlanDetailsActivity.this, 10086, plan);
            }
        });

        setTitle(plan.getPlanName());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_plan_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_friend) {
            addFriend();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10086) {
            if (resultCode == RESULT_OK) {
                for (BaseFragment f : fragmentList) {
                    f.onDataChanged(C.TAG_TASK);
                }
            }
        }
    }

    private void addFriend() {
        final EditText editText = new EditText(this);
        int dp = UiUtil.dip2px(16);
        editText.setPadding(dp, dp, dp, dp);
        editText.setHint("请输入成员用户名");
        editText.setSingleLine(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(editText);
        builder.setTitle("添加成员");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = editText.getText().toString();
                DataCenter.getInstance().addFriend(plan, str);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                UiUtil.showInput(editText);
            }
        });
        dialog.show();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends BaseFragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final int TAB_TASK = 0;
        public static final int TAB_TASK_COMPLETE = 1;
        public static final int TAB_USER = 2;

        private static final String ARG_TAB_TYPE = "tab_type";

        private BaseAdapter adapter;
        private PullToRefreshListView listView;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int tabType) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_TAB_TYPE, tabType);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_plan_details, container, false);
            listView = (PullToRefreshListView) rootView.findViewById(R.id.lv_tab_content);
            adapter = null;
            final int type = getArguments().getInt(ARG_TAB_TYPE);
            if (type == TAB_TASK) {
                adapter = new SimpleTaskAdapter(getContext(), plan.getTasks());
            } else if (type == TAB_TASK_COMPLETE) {
                adapter = new SimpleTaskAdapter(getContext(), plan.getCompleteTasks());
            } else if (type == TAB_USER) {
                adapter = new SimpleUserAdapter(getContext(), plan.getUsers());
            }
            listView.getRefreshableView().setAdapter(adapter);
            listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                @Override
                public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                    DataCenter.getInstance().refreshPlanList(new DataCenter.FindTaskCallback() {
                        @Override
                        public void lastTaskDone(boolean havaData) {
                            if (havaData) {
                                onDataChanged(C.TAG_TASK);
                            }
                            listView.onRefreshComplete();
                        }
                    });
                }
            });
            listView.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    if (type == TAB_TASK) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("任务完成？");
                        builder.setNegativeButton("未完成", null);
                        builder.setPositiveButton("已完成", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((SimpleTaskAdapter) adapter).completeTask(plan, position-1);
                            }
                        });
                        builder.show();
                    }
                }


            });
            return rootView;
        }

        @Override
        public String getTitle() {
            return null;
        }

        @Override
        public void onDataChanged(int tag) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            fragmentList.add(position, PlaceholderFragment.newInstance(position));
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "进行中";
                case 1:
                    return "已完成";
                case 2:
                    return "成员列表";
            }
            return null;
        }
    }
}
