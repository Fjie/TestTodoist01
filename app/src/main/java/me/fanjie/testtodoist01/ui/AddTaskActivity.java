package me.fanjie.testtodoist01.ui;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import me.fanjie.testtodoist01.R;
import me.fanjie.testtodoist01.core.C;
import me.fanjie.testtodoist01.core.DataCenter;
import me.fanjie.testtodoist01.model.Plan;
import me.fanjie.testtodoist01.model.Task;
import me.fanjie.testtodoist01.utils.L;
import me.fanjie.testtodoist01.utils.TimeUtil;

public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etInputTaskName;
    private TextView tvSelectedPlan;
    private TextView tvSelectedDate;
    private SimplePlanAdapter planAdapter;

    private Calendar calendar;
    private Plan currentPlan;

    private DataCenter dataCenter;

    private boolean canNotSelectPlan;

    public static void startActivity(AppCompatActivity activity,int requestCode,Plan plan){
        Intent i = new Intent(activity,AddTaskActivity.class);
        if(plan!=null){
            i.putExtra(C.Plan.CLASS_NAME,plan);
        }
        activity.startActivityForResult(i,requestCode);
    }

    public static void startActivity(AppCompatActivity activity,int requestCode){
        startActivity(activity,requestCode,null);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Object o = getIntent().getSerializableExtra(C.Plan.CLASS_NAME);
        if(o!=null){
            currentPlan = (Plan) o;
            canNotSelectPlan = true;
        }else {
            currentPlan = DataCenter.plens.get(0);
            canNotSelectPlan = false;
        }

        etInputTaskName = (EditText) findViewById(R.id.et_input_task_name);
        tvSelectedPlan = (TextView) findViewById(R.id.tv_seleted_plan);
        tvSelectedDate = (TextView) findViewById(R.id.tv_selected_date);

        findViewById(R.id.fab).setOnClickListener(this);
        findViewById(R.id.ll_selecet_plan).setOnClickListener(this);
        findViewById(R.id.ll_select_date).setOnClickListener(this);

        tvSelectedPlan.setText(currentPlan.getPlanName());
        tvSelectedDate.setText(TimeUtil.getSimpleDateToShow(calendar));

        dataCenter = DataCenter.getInstance();

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.fab){
            addTask();
        }else if(id == R.id.ll_selecet_plan){
            if(!canNotSelectPlan) {
                selectPlan();
            }
        }else if(id == R.id.ll_select_date){
            selectData();
        }
    }



    private void addTask(){
        String str = etInputTaskName.getText().toString();
        if(str.isEmpty()){
            Toast.makeText(AddTaskActivity.this,"请输入任务",Toast.LENGTH_SHORT).show();
        }else {
            final Task task = new Task();
            task.setTaskName(str);
            task.setTime(calendar);
            task.setPlanObjectId(currentPlan.getObjectId());
            L.e(currentPlan.toString());
            dataCenter.addTask(task,currentPlan.getUserIds(), new DataCenter.PutDoneCallback() {
                @Override
                public void done(String id) {
                    currentPlan.getTasks().add(task);
                    setResult(RESULT_OK);
                    finish();
                }
            });

        }
    }

    private void selectPlan() {

        ListView listView = new ListView(this);
        planAdapter = new SimplePlanAdapter(this,DataCenter.plens);
        listView.setAdapter(planAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择任务");
        builder.setView(listView);
        builder.setNegativeButton("取消", null);
        final AlertDialog dialog = builder.create();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPlan = planAdapter.getItem(position);
                tvSelectedPlan.setText(currentPlan.getPlanName());
                dialog.dismiss();
                L.e("onItemClick");
            }
        });
        dialog.show();

    }

    private void selectData() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year,monthOfYear,dayOfMonth);
                tvSelectedDate.setText(TimeUtil.getSimpleDateToShow(calendar));
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();


    }
}
