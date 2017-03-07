package com.example.aaron.contractwar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.AbstractList;
import java.util.ArrayList;

/**
 * Created by Aaron on 2015/6/6.
 */
public class MyteamActivity extends Activity {
    private ContactWar myApp;
    private bas_role user, n1, n2;
    private Button quit;
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            final String res = msg.obj.toString();
            if (res.equals("1")) {
                Toast.makeText(MyteamActivity.this, "已退出队伍" , Toast.LENGTH_SHORT).show();
                myApp.getUser().in_team = false;
                myApp.getUser().team_number.clear();
                MyteamActivity.this.finish();
            } else if (res.equals("0")) {
                Toast.makeText(MyteamActivity.this, "服务器出错" , Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myteam);
        quit = (Button)findViewById(R.id.quit);
        myApp = (ContactWar)getApplication();
        user = myApp.getUser();
        ImageView img = (ImageView)findViewById(R.id.user);
        ImageView bottom = (ImageView)findViewById(R.id.user_bottom);
        TextView hp = (TextView)findViewById(R.id.uhp_data);
        TextView atk = (TextView)findViewById(R.id.uatk_data);

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.HttpClientGET("http://10.0.2.2:8008/request?username=" + ContactWar.getUserID() + "&mode=quitteam",mhandler);
            }
        });

        switch (user.getjob()) {
            case 0:
                img.setImageResource(R.drawable.warrior);
                bottom.setImageResource(R.drawable.wbottom);
                break;
            case 1:
                img.setImageResource(R.drawable.magician);
                bottom.setImageResource(R.drawable.mbottom);
                break;
            case 2:
                img.setImageResource(R.drawable.doctor);
                bottom.setImageResource(R.drawable.dbottom);
                break;
            default:
                img.setImageResource(R.drawable.warrior);
                bottom.setImageResource(R.drawable.wbottom);
        }
        hp.setText(String.valueOf(user.getHP()));
        atk.setText(String.valueOf(user.getATK()));

        if (user.team_number.size() >= 1) {
            n1 = user.team_number.get(0);
            img = (ImageView)findViewById(R.id.n1);
            bottom = (ImageView)findViewById(R.id.n1_bottom);
            hp = (TextView)findViewById(R.id.n1hp_data);
            atk = (TextView)findViewById(R.id.n1atk_data);

            switch (n1.getjob()) {
                case 0:
                    img.setImageResource(R.drawable.warrior);
                    bottom.setImageResource(R.drawable.wbottom);
                    break;
                case 1:
                    img.setImageResource(R.drawable.magician);
                    bottom.setImageResource(R.drawable.mbottom);
                    break;
                case 2:
                    img.setImageResource(R.drawable.doctor);
                    bottom.setImageResource(R.drawable.dbottom);
                    break;
                default:
                    img.setImageResource(R.drawable.warrior);
                    bottom.setImageResource(R.drawable.wbottom);
            }
            hp.setText(String.valueOf(n1.getHP()));
            atk.setText(String.valueOf(n1.getATK()));
        }

        img = (ImageView)findViewById(R.id.n2);
        bottom = (ImageView)findViewById(R.id.n2_bottom);
        hp = (TextView)findViewById(R.id.n2hp_data);
        atk = (TextView)findViewById(R.id.n2atk_data);
        if (user.team_number.size() == 2) {
            n2 = user.team_number.get(1);
            switch (n2.getjob()) {
                case 0:
                    img.setImageResource(R.drawable.warrior);
                    bottom.setImageResource(R.drawable.wbottom);
                    break;
                case 1:
                    img.setImageResource(R.drawable.magician);
                    bottom.setImageResource(R.drawable.mbottom);
                    break;
                case 2:
                    img.setImageResource(R.drawable.doctor);
                    bottom.setImageResource(R.drawable.dbottom);
                    break;
                default:
                    img.setImageResource(R.drawable.warrior);
                    bottom.setImageResource(R.drawable.wbottom);
            }
            hp.setText(String.valueOf(n2.getHP()));
            atk.setText(String.valueOf(n2.getATK()));
        } else {
            img.setImageResource(R.drawable.unknown);
            bottom.setImageResource(R.drawable.mbottom);
            hp.setText("?????");
            atk.setText("????");
        }
    }
}
