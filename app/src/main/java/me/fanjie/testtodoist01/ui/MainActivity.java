package me.fanjie.testtodoist01.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;

import me.fanjie.testtodoist01.R;
import me.fanjie.testtodoist01.core.C;
import me.fanjie.testtodoist01.core.DataCenter;
import me.fanjie.testtodoist01.model.Plan;
import me.fanjie.testtodoist01.utils.L;
import me.fanjie.testtodoist01.utils.UiUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BaseFragment taskListFragment;
    private BaseFragment planListFragment;
    private BaseFragment completeFragment;

    private BaseFragment currentFragment;

    private FragmentManager fragmentManager;

    private DataCenter dataCenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(AVUser.getCurrentUser() == null){
            startActivity(new Intent(this,LoginActivity.class));
            finish();
            return;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataCenter = DataCenter.getInstance();

        SharedPreferences preferences = getSharedPreferences("count", MODE_WORLD_READABLE);
        int count = preferences.getInt("count", 0);
        L.e("启动次数" + count);
        if (count == 0) {
//            TODO 初始化一些默认计划
        }
        preferences.edit().putInt("count", ++count).commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DataCenter.plens == null || DataCenter.plens.size()<1){
                    UiUtil.toast("需要先创建计划");
                    addPlan();
                }else {
                    AddTaskActivity.startActivity(MainActivity.this, 10086);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_task_list);

        TextView tvUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_user_name);
        tvUserName.setText(AVUser.getCurrentUser().getUsername());


        fragmentManager = getSupportFragmentManager();
        taskListFragment = TaskListFragment.newInstance();
        switchFragment(taskListFragment);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10086) {
            if (resultCode == Activity.RESULT_OK) {
                currentFragment.onDataChanged(C.TAG_TASK);
                L.e("Mainb -- onActivityResult");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_plan) {
            addPlan();
        } else if (id == R.id.action_add_friend) {

        }
        return super.onOptionsItemSelected(item);
    }

    private void addPlan() {
        final EditText editText = new EditText(this);
        int dp = UiUtil.dip2px(16);
        editText.setPadding(dp, dp, dp, dp);
        editText.setHint("请输入计划名称");
        editText.setSingleLine(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(editText);
        builder.setTitle("添加计划");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = editText.getText().toString();
                if (!str.isEmpty()) {
                    dataCenter.addPlan(new Plan(str), new DataCenter.PutDoneCallback() {
                        @Override
                        public void done(String id) {
                            currentFragment.onDataChanged(C.TAG_PLAN);
                        }
                    });

                }
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_task_list) {
            if (taskListFragment == null) {
                taskListFragment = TaskListFragment.newInstance();
            }
            switchFragment(taskListFragment);
        } else if (id == R.id.nav_plan_list) {
            if (planListFragment == null) {
                planListFragment = PlanListFragment.newInstance();
            }
            switchFragment(planListFragment);
        } else if(id == R.id.nav_task_complete_list) {
            if (completeFragment == null) {
                completeFragment = CompleteListFragment.newInstance();
            }
            switchFragment(completeFragment);
        }else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_setting) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void switchFragment(@NonNull BaseFragment totla) {
        L.e(totla.toString());
        if (currentFragment != totla) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            if (!totla.isAdded()) {
                transaction.add(R.id.fl_content, totla);
            } else {
                transaction.show(totla);
            }
            transaction.commit();
            currentFragment = totla;
            setTitle(totla.getTitle());
        }
    }



}
