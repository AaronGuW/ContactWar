package com.example.aaron.contractwar;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Aaron on 2015/6/19.
 */
public class thunder {
    public int x,y;
    public static Bitmap thunderimg[];
    public static Bitmap forcastimg[];
    public int cnt = 0;
    public boolean activated = true;
    public boolean valid = false;

    public thunder(int _x, int _y) {
        x = _x;
        y = _y;
    }

    public void logic() {
        if (cnt >= 154){
            valid = false;
            activated = false;
            return;
        }
        if (cnt == 100) {
            valid = true;
        }
        cnt++;
    }

    public void draw(Canvas c, Paint p) {
        if (cnt < 100) {
            c.save();
            c.clipRect(x - forcastimg[cnt/20].getWidth()/2, y - forcastimg[cnt/20].getHeight(), x + forcastimg[cnt/20].getWidth()/2, y);
            c.drawBitmap(forcastimg[cnt/20],x - forcastimg[cnt/20].getWidth()/2, y - forcastimg[cnt/20].getHeight() , p);
            c.restore();
        } else if (cnt >= 100) {
            c.save();
            c.clipRect(x - thunderimg[((cnt - 100)/6)%9].getWidth()/2, y - thunderimg[((cnt - 100)/6)%9].getHeight() - 20, x + thunderimg[((cnt - 100)/6)%9].getWidth()/2, y);
            c.drawBitmap(forcastimg[4],x - forcastimg[4].getWidth()/2, y - forcastimg[4].getHeight(), p);
            c.drawBitmap(thunderimg[((cnt - 100)/6)%9], x - thunderimg[((cnt - 100)/6)%9].getWidth()/2, y - thunderimg[((cnt - 100)/6)%9].getHeight() - 20, p);
            c.restore();
        }
    }
}
