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
public class task {
    protected int max;                    //任务要求的数量
    protected int progress;               //当前进度
    protected String date;                  //任务生成的日期，年/月/日；
    protected int type;                     //任务的小分类
    protected int reward;                   //奖励类型
    protected item rewarditem;              //奖励物品
    protected boolean accomplished = false, rewarded = false;
    public task(int m , int p, String d, int t, int r, item ri) { max = m; progress = p; date = d; type = t; reward = r; rewarditem = ri; }
    public void synchronize(Context mContext,ContactWar myApp, RoundProgressBar progressBar, Button getreward) {}
    public boolean isAccomplished() { return accomplished; }
    public int getMax() { return max; }
    public int getProgress() { return progress; }
    public void draw(ImageView logo, TextView description, RoundProgressBar progressBar, TextView expdata, ImageView rewardlogo, Button getreward) {}
    public void setRewarded() { rewarded = true; }
    public item getRewarditem() { return rewarditem; }
}
