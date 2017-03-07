package com.example.aaron.contractwar;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by Aaron on 2015/6/5.
 */
public class shockwave {
    public int type;           //0战士 1法师
    public int x,y;              //冲击波坐标 战士：起点 法师：中点
    public int dir;             //方向
    public int cnt = 0;         //计数器
    public boolean validity = false;
    public static Bitmap[] img,re_img;          //图片资源
    public static Bitmap mimg;

    public void logic() {
        if (cnt > 0) cnt--;
        else validity = false;
    }

    public shockwave(int t, int _x, int _y, int d) {
        x = _x;
        y = _y;
        dir = d;
        type = t;
    }

    public void draw(Canvas c, Paint p) {
        c.save();
        switch (type) {
            case 0:
                if (dir == 1) {
                    c.clipRect(x, y - img[(36-cnt)/6].getHeight(), x + img[(36-cnt)/6].getWidth(), y);
                    c.drawBitmap(img[(36-cnt)/6], x , y - img[(36-cnt)/6].getHeight(), p);
                } else {
                    c.clipRect(x - re_img[(36-cnt)/6].getWidth(), y - re_img[(36-cnt)/6].getHeight(), x , y);
                    c.drawBitmap(re_img[(36-cnt)/6], x - re_img[(36-cnt)/6].getWidth(), y - re_img[(36-cnt)/6].getHeight(), p);
                }
                break;
            case 1:
                c.clipRect(x - 185, y - 268, x + 185, y);
                c.drawBitmap(mimg, x - 370*(12 - cnt/4) - 185,y - 282 , p);
                break;
        }
        c.restore();
    }

    public boolean collidedwith(bas_role p) {
        switch (type) {
            case 0:
                if ( (p.p - x)*(p.p - (x+dir*395)) < 0 || (p.b - x)*(p.b - (x+dir*395)) < 0 ) {
                    return true;
                }
                break;
            case 1:
                if ( Math.abs(p.p - x) <= 185 ) {
                    return true;
                }
                break;
        }
        return false;
    }
}
