package com.example.aaron.contractwar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron on 2015/6/27.
 */
public class RegisterActivity extends Activity {

    private Button button_submit;
    private Button button_login;
    private EditText register_Nickname;
    private EditText register_rePassword;
    private EditText register_name;
    private EditText register_password;
    private TextView Welcome;
    private String inputTextName;
    private String inputTextPassword;
    private String inputTextNickname;
    private String inputTextRePassword;
    public Handler myhandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (((String)msg.obj).compareTo("1")==0) {
                        Log.d("register","succeed");
                        Toast.makeText(RegisterActivity.this, "注册成功" , Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "该手机已被注册", Toast.LENGTH_SHORT).show();
                        Log.d("register","failure");
                    }
                    /*Intent intent = new Intent();
                    intent.setClass(LoginActivity.this,MainActivity.class);
                    startActivity(intent);*/
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regisiter);
        button_login = (Button) findViewById(R.id.turn_to_login);
        button_submit = (Button) findViewById(R.id.register_submit);
        Welcome = (TextView) findViewById(R.id.Register_Welcome);
        register_name = (EditText) findViewById(R.id.register_name);
        register_password = (EditText) findViewById(R.id.register_password);
        register_rePassword = (EditText) findViewById(R.id.register_re_password);
        register_Nickname = (EditText) findViewById(R.id.register_nickname);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputTextName = register_name.getText().toString();
                inputTextPassword = register_password.getText().toString();
                inputTextNickname = register_Nickname.getText().toString();
                inputTextRePassword = register_rePassword.getText().toString();
                if (inputTextPassword.compareTo(inputTextRePassword) == 0) {
                    String response = HttpUtil.HttpClientGET("http://10.0.2.2:8008/reg?username=" + inputTextName + "&password=" + inputTextPassword + "&nickname=" + inputTextNickname, myhandler);
                    Log.d("Test", inputTextName);
                    Log.d("Test", inputTextPassword);
                    Log.d("Test", inputTextNickname);
                    Log.d("Test", inputTextRePassword);
                    Log.d("Test", "response" + response);

                } else if (inputTextPassword.length() == 0 || inputTextRePassword.length() == 0) {
                    Toast.makeText(RegisterActivity.this,"请正确输入您的密码",Toast.LENGTH_SHORT).show();
                } else if (inputTextNickname.length() == 0) {
                    Toast.makeText(RegisterActivity.this,"昵称不可为空",Toast.LENGTH_SHORT).show();
                } else if (inputTextPassword.compareTo(inputTextRePassword) != 0) {
                    Toast.makeText(RegisterActivity.this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
                }
            }
        });
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}
