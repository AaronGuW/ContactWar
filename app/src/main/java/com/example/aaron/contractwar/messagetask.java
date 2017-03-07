package com.example.aaron.contractwar;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
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
public class messagetask extends task {

    public messagetask(int m, int p, String d, int t, int r, item ri) {
        super(m,p,d,t,r,ri);
    }
    @Override
    public void synchronize(Context mContext, ContactWar myApp, RoundProgressBar progressBar, Button getreward){
        final String SMS_URI_ALL   = "content://sms/";
        progress = 0;
        try{
            ContentResolver cr = mContext.getContentResolver();
            String[] projection = new String[]{"_id", "address", "person",
                    "body", "date", "type"};
            Uri uri = Uri.parse(SMS_URI_ALL);
            Cursor cur = cr.query(uri, projection, null, null, null);
            if (cur.moveToFirst()) {
                String name;
                String _date;
                int typeId;
                int nameColumn = cur.getColumnIndex("person");
                int dateColumn = cur.getColumnIndex("date");
                int typeColumn = cur.getColumnIndex("type");
                int bodyIdx = cur.getColumnIndex("body");

                do{
                    name = cur.getString(nameColumn);
                    typeId = cur.getInt(typeColumn);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date d = new Date(Long.parseLong(cur.getString(dateColumn)));
                    _date = dateFormat.format(d);
                    Log.i("message date",_date);
                    if (_date.compareTo(date) != 0)
                        break;
                    else if (typeId == 2 || (typeId == 1 && name != null)) {
                        String body = cur.getString(bodyIdx);
                        Log.i("append",body);
                        if (type == 0 && typeId == 2) {progress++;}
                        else if (type == 1 && typeId == 1) progress++;
                        else if (type == 2) progress++;
                        if (progress == max) {
                            accomplished = true;
                            break;
                        }
                    }
                }while(cur.moveToNext());
            }
            cur.close();
        } catch(SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }
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

    @Override
    public void draw(ImageView logo, TextView description, RoundProgressBar progressBar, TextView expdata, ImageView rewardlogo, Button getreward) {
        logo.setImageResource(R.drawable.message);
        if (type == 0)
            description.setText("向任意联系人发送"+String.valueOf(max)+"条短信");
        else if (type == 1)
            description.setText("收到任意联系人的"+String.valueOf(max)+"条短信");
        else
            description.setText("发送或接收"+String.valueOf(max)+"条短信");

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
