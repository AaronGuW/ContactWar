package com.example.aaron.contractwar;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Aaron on 2015/9/9.
 */
public class WorldWarActivity extends Activity {
    private CountDownTimer cdt;
    private TextView countdown,teamcnt;
    private Button signupbtn;
    private static final String WRONGSTAGE = "0", SIGNUPSUCCEED = "1", ERROR = "2";
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.obj.toString().substring(0,1)) {
                case WRONGSTAGE:
                    Toast.makeText(WorldWarActivity.this, "报名尚未开始或已经结束", Toast.LENGTH_SHORT).show();
                    break;
                case SIGNUPSUCCEED:
                    Toast.makeText(WorldWarActivity.this, "报名成功", Toast.LENGTH_SHORT).show();
                    signupbtn.setEnabled(false);
                    signupbtn.setText("已报名");
                    teamcnt.setText(msg.obj.toString().substring(1));
                    break;
                case ERROR:
                    Toast.makeText(WorldWarActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worldwar);
        countdown = (TextView)findViewById(R.id.countdowntimer);
        signupbtn = (Button)findViewById(R.id.signbtn);
        teamcnt = (TextView)findViewById(R.id.totalteam);
        switch (getIntent().getStringExtra("stage")) {
            case "0":
                ((TextView)findViewById(R.id.stagetitle)).setText("赛季伊始，诸君请积极备战！\n距离报名开始还有");
                break;
            case "1":
                ((TextView)findViewById(R.id.stagetitle)).setText("本赛季世界大战报名已开始！\n距离报名结束还有");
                teamcnt.setText(getIntent().getStringExtra("teamcnt"));
                break;
            case "2":
                ((TextView)findViewById(R.id.stagetitle)).setText("本赛季世界大战报名已结束！\n距离大赛开始还有");
                teamcnt.setText(getIntent().getStringExtra("teamcnt"));
                break;
            default:
                ((TextView)findViewById(R.id.stagetitle)).setText("赛季伊始，诸君请积极备战！\n距离报名开始还有");
        }
        switch (getIntent().getStringExtra("signed")) {
            case "1":
                ((Button)findViewById(R.id.signbtn)).setEnabled(false);
                ((Button)findViewById(R.id.signbtn)).setText("已报名");
                break;
            case "2":
                ((Button)findViewById(R.id.signbtn)).setEnabled(false);
                ((Button)findViewById(R.id.signbtn)).setText("您没有队伍");
                break;
        }
        String _date = getIntent().getStringExtra("start");
        Date date = new Date(Long.valueOf(_date)*1000+28800000);
        SimpleDateFormat format = new SimpleDateFormat("MM'月'dd'日'");
        ((TextView)findViewById(R.id.signdeadline)).setText(format.format(date));
        int left = Integer.valueOf(getIntent().getStringExtra("countdown"));
        cdt = new CountDownTimer(left*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long second = millisUntilFinished/1000;
                long hour = second/3600;
                long minute = second/60 - hour*60;
                second = second - minute*60 - hour*3600;
                String HH = hour >= 10?String.valueOf(hour):"0"+String.valueOf(hour);
                String mm = minute >= 10?String.valueOf(minute):"0"+String.valueOf(minute);
                String ss = second >= 10?String.valueOf(second):"0"+String.valueOf(second);
                countdown.setText(HH+":"+mm+":"+ss);
            }

            @Override
            public void onFinish() {

            }
        };
        cdt.start();

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.HttpClientGET("http://10.0.2.2:8008/w?username=" + ContactWar.getUserID()+"&mode=signup",mhandler);
            }
        });
    }
}
