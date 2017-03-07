package com.example.aaron.contractwar;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Aaron on 2015/5/31.
 */
public class bullet {
    static public Bitmap img = null;
    //static public void initimg(Bitmap bmp) { img = bmp; }

    public int x,y,p;
    public int dir;
    public boolean isdead = false;
    private int cnt = 0;
    private int life = 0;
    public static int sw;
    public bullet(int _x, int _y,int _p,int _dir) {
        x = _x;
        y = _y;
        p = _p;
        dir = _dir;
    }

    public void logic() {
        x = x + 8*dir;
        p = p + 8*dir;
        life += 8;
        cnt++;
        if (x > sw || x < 0 || life > 800)
            isdead = true;
    }

    public void draw(Canvas c, Paint p) {
        c.save();
        c.clipRect(x, y, x+90, y+90);
        c.drawBitmap(img, x - 90*((cnt/4)%4), y, p);
        c.restore();
    }
}
