package com.example.mymemory2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.mymemory2.Utils.ToastUtil;
import com.example.mymemory2.Database.UserTB;
import com.example.mymemory2.Bean.User;

import java.util.List;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_reg_account;
    private EditText et_reg_pwd;
    private UserTB mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_reg_account = findViewById(R.id.et_reg_account);
        et_reg_pwd = findViewById(R.id.et_reg_pwd);

        findViewById(R.id.btn_register).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHelper = new UserTB(this);
        mHelper.open();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHelper.close();
    }
    @Override
    public void onClick(View view) {
        String account = et_reg_account.getText().toString();
        String pwd = et_reg_pwd.getText().toString();
        User user = new User();
        if(TextUtils.isEmpty(account)){
            ToastUtil.show(this, "账号不能为空");
        }

        List<User> userList = mHelper.selectByAccount(account);
        if(!userList.isEmpty()){  //说明已经被注册过了
            ToastUtil.show(this, "该账号已存在");
            return;
        }
        if(TextUtils.isEmpty(pwd)){
            ToastUtil.show(this, "密码不能为空");
            return;
        }
        user.setAccount(account);
        user.setPassword(pwd);
        long rowId = mHelper.insert(user);
        if(rowId != -1){
            ToastUtil.show(this, "注册成功");
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }else {
            ToastUtil.show(this, "注册失败");
        }
    }
}