package com.cx.mymap;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cx.mymap.model.Operator;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    private EditText usernameEdit,passwordEdit;
    private String usernameString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEdit =findViewById(R.id.username_edit);
        passwordEdit = findViewById(R.id.password_edit);
        //初始化bmob
        Bmob.initialize(this, "16e5e78189321279e7f550b11c1c3f6c");

    }
    /**
     * 弹框方法
     */
    public void toast(String message){
        Toast.makeText(LoginActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    public void login(View view){
        BmobQuery<Operator> bmobQuery = new BmobQuery<Operator>();
        usernameString = usernameEdit.getText().toString();
        bmobQuery.addWhereEqualTo("username",usernameString).addWhereEqualTo("password",passwordEdit.getText().toString());
        bmobQuery.count(Operator.class, new CountListener() {
            @Override
            public void done(Integer count, BmobException e) {
                if(e==null){
                   if(count>=1){
                       toast("登录成功！");
                       MyMapApplication.username = usernameString;
                       //进行页面跳转
                       Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                       startActivity(intent);
                   }
                }else{
                    toast("bmob失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    /**
     * 跳转到注册界面
     * @param view
     */
    public void link(View view){
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }


}

