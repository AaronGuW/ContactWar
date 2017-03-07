package com.example.aaron.contractwar;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.LogRecord;

/**
 * Created by Aaron on 2015/6/27.
 */
public class LoginActivity extends Activity {

    private Button button_submit;
    private Button button_register;
    private ContactWar contactWar;
    private EditText login_name;
    private EditText login_password;
    private String inputTextName;
    private String inputTextPassword;
    public Handler myhandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (((String)msg.obj).compareTo("1")==0) {
                        Log.d("login", "succeed");
                        Toast.makeText(LoginActivity.this, "登录成功" , Toast.LENGTH_SHORT).show();
                        contactWar.setUserID(inputTextName);
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, SplashScreenActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "用户名或密码错误" , Toast.LENGTH_SHORT).show();
                        Log.d("login","failure");
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        contactWar = (ContactWar)getApplication();
        button_submit = (Button)findViewById(R.id.login_submit);
        button_register = (Button)findViewById(R.id.turn_to_register);
        login_name = (EditText) findViewById(R.id.login_name);
        login_password = (EditText) findViewById(R.id.login_password);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                inputTextName = login_name.getText().toString();
                inputTextPassword = login_password.getText().toString();
                ArrayList<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("username",inputTextName));
                params.add(new BasicNameValuePair("password",inputTextPassword));
                contactWar.setUserID("18918261855");
                Toast.makeText(LoginActivity.this, "登录成功" , Toast.LENGTH_SHORT).show();
                contactWar.setUserID(inputTextName);
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, SplashScreenActivity.class);
                startActivity(intent);
                //String response = HttpUtil.HttpClientGET("http://10.0.2.2:8008/log?username="+inputTextName+"&password="+inputTextPassword,myhandler);
                Log.d("Test", inputTextName);
                Log.d("Test",inputTextPassword);
            }
        });
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}
