package com.example.aaron.contractwar;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by Aaron on 2015/6/24.
 */
public class bossbullet {
    static public Bitmap img[];
    //static public void initimg(Bitmap bmp) { img = bmp; }

    public int x,y;
    public boolean isdead = false;
    private int cnt = 0;
    private int life = 0;
    public static int sw;
    public bossbullet(int _x, int _y) {
        x = _x;
        y = _y;
    }

    public void logic() {
        x -= 8;
        life += 8;
        cnt++;
        if (x > sw || x < 0 || life > 1000) {
            isdead = true;
        }
    }

    public void draw(Canvas c, Paint p) {
        c.save();
        c.clipRect(x, y, x + img[(cnt/4)%3].getWidth(), y + img[(cnt/4)%3].getHeight());
        c.drawBitmap(img[(cnt/4)%3], x, y , p);
        c.restore();
    }
}
