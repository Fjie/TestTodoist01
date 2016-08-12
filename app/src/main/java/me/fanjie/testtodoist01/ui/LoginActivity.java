package me.fanjie.testtodoist01.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;

import me.fanjie.testtodoist01.R;
import me.fanjie.testtodoist01.utils.L;
import me.fanjie.testtodoist01.utils.UiUtil;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);

        findViewById(R.id.btn_sign_in).setOnClickListener(this);
        findViewById(R.id.btn_sign_up).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btn_sign_in){
            signInOrUp(false);
        }else if(id == R.id.btn_sign_up){
            signInOrUp(true);
        }
    }

    private void signInOrUp(boolean isSignUp){
        String userName = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if(userName.isEmpty()){
            UiUtil.toast("请输入用户名");
        }else if(password.isEmpty()){
            UiUtil.toast("请输入密码");
        }else {
            if(isSignUp){
                AVUser user = new AVUser();
                user.setUsername(userName);
                user.setPassword(password);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e == null){
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            UiUtil.toast("注册成功！");
                            finish();
                        }else {
                            UiUtil.toast("用户名被占用");
                            L.e(e.getMessage());
                        }
                    }
                });
            }else {
                AVUser.logInInBackground(userName, password, new LogInCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                        if(e == null){
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            UiUtil.toast("登陆成功");
                            finish();
                        }else {
                            L.e(e.getMessage());
                            UiUtil.toast("用户名或密码错误");
                        }
                    }
                });
            }
        }
    }
}
