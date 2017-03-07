package com.example.aaron.contractwar;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Aaron on 2015/6/9.
 */
public class phonecalltask extends task {
    private static final int OUT = 0;
    private static final int IN = 1;

    public phonecalltask(int m, int p, String d, int t, int r, item ri) {
        super(m,p,d,t,r,ri);
    }

    @Override
    public void synchronize(Context mContext, ContactWar myApp, RoundProgressBar progressBar, Button getreward) {
        progress = 0;
        ArrayList<String> phonenumber = myApp.getPhonenumber();
        Cursor cursor = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, null);
        if(cursor.moveToLast()){
            do{
                SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
                Date _date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))));
                //呼叫时间
                String time = sfd.format(_date);
                if (time.compareTo(date) != 0)
                    break;
                boolean flag;
                String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                int calltype = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)));
                if (type == IN) flag = calltype == CallLog.Calls.INCOMING_TYPE;
                else if (type == OUT) flag = calltype == CallLog.Calls.OUTGOING_TYPE;
                else flag = calltype != CallLog.Calls.MISSED_TYPE;

                String duration = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION));
                if (time.compareTo(date) == 0 && flag && phonenumber.contains(number) && Integer.parseInt(duration) >= 90){
                    progress++;
                    if (progress >= max) {
                        progress = max;
                        accomplished = true;
                        break;
                    }
                }
            }while(cursor.moveToPrevious());
            cursor.close();
            progressBar.setProgress(progress);
            if (accomplished && !rewarded)
                getreward.setEnabled(true);
            else if (accomplished && rewarded) {
                getreward.setEnabled(false);
                getreward.setText("已领取");
            } else {
                getreward.setEnabled(false);
            }
        }
    }

    @Override
    public void draw(ImageView logo, TextView description, RoundProgressBar progressBar, TextView expdata, ImageView rewardlogo, Button getreward) {
        logo.setImageResource(R.drawable.phonecall);
        switch (type) {
            case 0:
                description.setText("向任意联系人拨打"+String.valueOf(max)+"通电话");
                break;
            case 1:
                description.setText("接听任意联系人的"+String.valueOf(max)+"通来电");
                break;
            case 2:
                description.setText("拨打或接听"+String.valueOf(max)+"通电话");
                break;
            default:
                description.setText("向任意联系人拨打"+String.valueOf(max)+"通电话");
                break;
        }
        if (reward != 1) {
            expdata.setText(String.valueOf(reward)+"经验值");
            rewardlogo.setImageResource(R.drawable.exp);
        } else {
            rewardlogo.setImageBitmap(rewarditem.logo);
            expdata.setText("");
        }
        getreward.setEnabled(false);
        progressBar.setMax(max);
        progressBar.setProgress(0);
    }
}
