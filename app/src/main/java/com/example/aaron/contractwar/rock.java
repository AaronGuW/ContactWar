package com.example.aaron.contractwar;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Aaron on 2015/6/16.
 */
public class rock extends monster {

    public rock() {
        super(250,50000);
    }

    @Override
    public void logic() {
        ncnt++;
        skcnt1++;
        skcnt2++;
        cnt++;
        if (status == 0) {
            if (skcnt2 > 600) {
                skcnt2 = 0;
                status = 3;
                cnt = 0;
            } else if (skcnt1 > 450) {
                skcnt1 = 0;
                status = 2;
                cnt = 0;
            } else if (ncnt > 200) {
                ncnt = 0;
                status = 1;
                cnt = 0;
            }
        } else if (status == 1) {
            if (cnt == 72) {
                status = 0;
                cnt = 0;
            }
        } else if (status == 2) {
            if (cnt == 54) {
                status = 0;
                cnt = 0;
            }
        } else if (status == 3) {
            if (cnt == 102) {
                status = 0;
                cnt = 0;
            }
        }
    }

    @Override
    public void draw(Canvas c, Paint p) {
        switch (status) {
            case 0:

        }
    }

}
