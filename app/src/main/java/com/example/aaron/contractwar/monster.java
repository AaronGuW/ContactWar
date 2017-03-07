package com.example.aaron.contractwar;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Aaron on 2015/6/16.
 */
public class monster {
    public Bitmap stand[];
    public Bitmap natk[];
    public Bitmap skill1[];
    public Bitmap xuli[];
    public Bitmap skill2[];
    public Bitmap death[];
    public int atk, hp;
    public int status;         //0站立  1普攻  2技能1(需蓄力、可打断)  3技能2  4死亡  5蓄力
    public int ncnt;
    public int skcnt1,skcnt2,cnt = 0;
    public int X,Y;
    public boolean dead = false;
    public monster(int a, int h) {
        atk = a;
        hp = h;
        Y = 760;
    }

    public void logic() {}
    public void draw(Canvas c, Paint p) {}
    public void initialize(Resources res) {}
}
