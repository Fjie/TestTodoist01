package me.fanjie.testtodoist01.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.avos.avoscloud.AVUser;

import me.fanjie.testtodoist01.R;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        if(AVUser.getCurrentUser() == null){
            startActivity(new Intent(this,LoginActivity.class));
        }else {
            startActivity(new Intent(this,MainActivity.class));
        }
        finish();
    }
}
