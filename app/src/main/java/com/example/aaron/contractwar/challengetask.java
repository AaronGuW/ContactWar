package com.example.aaron.contractwar;

import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Aaron on 2015/6/9.
 */
public class challengetask extends task {
    private static final int WARRIOR = 0;
    private static final int MAGICIAN = 1;
    private static final int DOCTOR = 2;

    public challengetask(int m, int p, String d, int t, int r, item ri) {
        super(m,p,d,t, r, ri);
    }

    @Override
    public void synchronize(Context mContext, ContactWar myApp, RoundProgressBar progressBar, Button getreward) {
        if (type == WARRIOR) progress = myApp.getfightrecord(WARRIOR) < max ? myApp.getfightrecord(WARRIOR):max;
        else if (type == MAGICIAN) progress = myApp.getfightrecord(MAGICIAN) < max ? myApp.getfightrecord(MAGICIAN):max;
        else if (type == DOCTOR) progress = myApp.getfightrecord(DOCTOR) < max ? myApp.getfightrecord(DOCTOR):max;
        else { int cnt = myApp.getfightrecord(0) + myApp.getfightrecord(1) + myApp.getfightrecord(2); progress = cnt < max ? cnt:max; }
        if ( progress == max) { accomplished = true; }
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
        switch (type) {
            case 0:
                logo.setImageResource(R.drawable.sword);
                description.setText("挑战任意战士"+String.valueOf(max)+"次");
                break;
            case 1:
                logo.setImageResource(R.drawable.wand);
                description.setText("挑战任意法师"+String.valueOf(max)+"次");
                break;
            case 2:
                logo.setImageResource(R.drawable.cross);
                description.setText("挑战任意圣骑士"+String.valueOf(max)+"次");
                break;
            default:
                logo.setImageResource(R.drawable.sword);
                description.setText("挑战任意战士"+String.valueOf(max)+"次");
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
