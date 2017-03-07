package com.example.aaron.contractwar;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.io.InputStream;

/**
 * Created by Aaron on 2015/6/18.
 */
public class nail extends monster {
    public nail(int atk, int hp) {
        super(500,hp);
    }

    @Override
    public void logic() {
        if (!dead) {
            ncnt++;
            skcnt1++;
            skcnt2++;
        }
        cnt++;
        if (!dead && hp == 0) {
            status = 4;
            cnt = 0;
            dead = true;
        }
        if (!dead) {
            if (status == 0) {
                if (skcnt2 > 1200) {
                    skcnt2 = 0;
                    status = 3;
                    cnt = 0;
                } else if (skcnt1 > 750) {
                    skcnt1 = 0;
                    status = 5;
                    cnt = 0;
                } else if (ncnt > 200) {
                    ncnt = 0;
                    status = 1;
                    cnt = 0;
                }
            } else if (status == 1) {
                if (cnt == 78) {
                    status = 0;
                    cnt = 0;
                }
            } else if (status == 2) {
                if (cnt == 48) {
                    status = 0;
                    cnt = 0;
                }
            } else if (status == 3) {
                if (cnt == 96) {
                    status = 0;
                    cnt = 0;
                }
            } else if (status == 5) {
                if (cnt == 100) {
                    status = 2;
                    cnt = 0;
                }
            }
        }
    }

    @Override
    public void draw(Canvas c, Paint p) {
        switch (status) {
            case 0:
                c.drawBitmap(stand[(cnt/12)%4],X-stand[(cnt/12)%4].getWidth()/2,Y-stand[(cnt/12)%4].getHeight(),p);
                break;
            case 1:
                c.drawBitmap(natk[(cnt/6)%13], X-natk[(cnt/6)%13].getWidth()/2, Y-natk[(cnt/6)%13].getHeight(),p);
                break;
            case 2:
                c.drawBitmap(skill1[(cnt/6)%13], X-skill1[(cnt/6)%13].getWidth()/2, Y-skill1[(cnt/6)%13].getHeight(),p);
                break;
            case 3:
                c.drawBitmap(skill2[(cnt/6)%16], X-skill2[(cnt/6)%16].getWidth()/2, Y-skill2[(cnt/6)%16].getHeight(),p);
                break;
            case 4:
                if (cnt < 78)
                    c.drawBitmap(death[(cnt/6)%13], X-death[(cnt/6)%13].getWidth()/2, Y-death[(cnt/6)%13].getHeight(),p);
                else
                    c.drawBitmap(death[12], X-death[12].getWidth()/2, Y-death[12].getHeight(),p);
                break;
            case 5:
                c.drawBitmap(skill1[cnt/20], X-skill1[cnt/20].getWidth()/2, Y-skill1[cnt/20].getHeight(),p);
                c.drawBitmap(xuli[(cnt/6)%6], X-xuli[(cnt/6)%6].getWidth()/2, Y-xuli[(cnt/6)%6].getHeight(),p);
                break;
            default:
                c.drawBitmap(stand[(cnt/6)%4], X-stand[(cnt/6)%4].getWidth()/2,Y-stand[(cnt/6)%4].getHeight(),p);
        }
    }

    @Override
    public void initialize(Resources res) {
        InputStream is;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 1;
        stand = new Bitmap[4];
        natk = new Bitmap[13];
        skill1 = new Bitmap[13];
        skill2 = new Bitmap[16];
        death = new Bitmap[13];
        xuli = new Bitmap[6];
        is = res.openRawResource(R.raw.naildead1);
        death[0] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.naildead2);
        death[1] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.naildead3);
        death[2] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.naildead4);
        death[3] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.naildead5);
        death[4] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.naildead6);
        death[5] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.naildead7);
        death[6] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.naildead8);
        death[7] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.naildead9);
        death[8] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.naildead10);
        death[9] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.naildead11);
        death[10] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.naildead12);
        death[11] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.naildead13);
        death[12] = BitmapFactory.decodeStream(is);

        is = res.openRawResource(R.raw.nailstand1);
        stand[0] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailstand2);
        stand[1] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailstand3);
        stand[2] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailstand4);
        stand[3] = BitmapFactory.decodeStream(is);

        is = res.openRawResource(R.raw.nailskill1);
        skill1[0] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskill2);
        skill1[1] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskill3);
        skill1[2] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskill4);
        skill1[3] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskill5);
        skill1[4] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskill6);
        skill1[5] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskill7);
        skill1[6] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskill8);
        skill1[7] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskill9);
        skill1[8] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskill10);
        skill1[9] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskill11);
        skill1[10] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskill12);
        skill1[11] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskill13);
        skill1[12] = BitmapFactory.decodeStream(is);

        is = res.openRawResource(R.raw.nailskillbig1);
        skill2[0] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskillbig2);
        skill2[1] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskillbig3);
        skill2[2] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskillbig4);
        skill2[3] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskillbig5);
        skill2[4] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskillbig6);
        skill2[5] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskillbig7);
        skill2[6] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskillbig8);
        skill2[7] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskillbig9);
        skill2[8] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskillbig10);
        skill2[9] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskillbig11);
        skill2[10] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskillbig12);
        skill2[11] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskillbig13);
        skill2[12] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskillbig14);
        skill2[13] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskillbig15);
        skill2[14] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailskillbig16);
        skill2[15] = BitmapFactory.decodeStream(is);

        is = res.openRawResource(R.raw.nailnatk1);
        natk[0] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailnatk2);
        natk[1] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailnatk3);
        natk[2] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailnatk4);
        natk[3] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailnatk5);
        natk[4] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailnatk6);
        natk[5] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailnatk7);
        natk[6] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailnatk8);
        natk[7] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailnatk9);
        natk[8] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailnatk10);
        natk[9] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailnatk11);
        natk[10] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailnatk12);
        natk[11] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.nailnatk13);
        natk[12] = BitmapFactory.decodeStream(is);

        is = res.openRawResource(R.raw.xuli0);
        xuli[0] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.xuli1);
        xuli[1] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.xuli2);
        xuli[2] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.xuli3);
        xuli[3] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.xuli4);
        xuli[4] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.xuli5);
        xuli[5] = BitmapFactory.decodeStream(is);
    }
}
