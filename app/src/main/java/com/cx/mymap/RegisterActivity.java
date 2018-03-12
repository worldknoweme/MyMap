package com.cx.mymap;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cx.mymap.model.Operator;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends Activity {

    //获取用户名控件
    private EditText usernameInput;
    //获取密码控件
    private EditText pwdInput;
    //获取再次获取密码控件
    private EditText pwdSecInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //初始化bmob
        Bmob.initialize(this, "16e5e78189321279e7f550b11c1c3f6c");
        usernameInput = (EditText) this.findViewById(R.id.username_edit);
        pwdInput = (EditText)this.findViewById(R.id.password_edit);
        pwdSecInput = (EditText)findViewById(R.id.pwdsec_edit);
    }

    /**
     * 弹框方法
     */
    public void toast(String message){
        Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    /**
     * 进行用户注册
     * @param view
     */
    public void register(View view){
        //获取用户名
        String username = usernameInput.getText().toString();
        //获取密码
        String password = pwdInput.getText().toString();
        //获取二次输入密码
        String passwordSec = pwdSecInput.getText().toString();
        if(username.equals("")){
            toast("用户名不能为空");
            return;
        }
        if(password.equals("")){
            toast("密码不能为空");
            return;
        }
        if(!password.equals(passwordSec)){
            toast("两次密码输入不一致，请重新输入");
            return;
        }

            Operator operator = new Operator();
            operator.setUsername(username);
            operator.setPassword(password);
            operator.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    if(e==null){
                        toast("恭喜你，注册成功！");
                    }else{
                        toast("注册失败，请联系管理员");
                    }
                }
            });

    }
}
