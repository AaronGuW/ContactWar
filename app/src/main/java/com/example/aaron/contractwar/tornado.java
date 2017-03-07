package com.example.aaron.contractwar;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Aaron on 2015/6/3.
 */
public class tornado {
    public static Bitmap img;
    public int cnt;
    public int x,y,center;
    public boolean valid = false;

    public tornado(int _x, int _y) {
        x = _x;
        y = _y;
        center = x + 75;
    }

    public void logic() {
        cnt++;
        if (cnt == 40) {
            valid = false;
            cnt = 0;
        }
    }

    public void draw(Canvas c, Paint p) {
        c.save();
        c.clipRect(x, y - 300, x + 310, y);
        c.drawBitmap(img, x - (cnt/4)%3*420, y - 300, p);
        c.restore();
    }
}
