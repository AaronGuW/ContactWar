package com.example.aaron.contractwar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Aaron on 2015/5/27.
 */
public class CompeteActivity extends Activity implements View.OnTouchListener {

    int lastX, lastY;
    int screenWidth, screenHeight;
    TextView tester1, tester2, tester3, tester4, hole1, hole2, hole3, hole4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compete);
        tester1 = (TextView) findViewById(R.id.tester1);
        tester2 = (TextView) findViewById(R.id.tester2);
        tester3 = (TextView) findViewById(R.id.tester3);
        tester4 = (TextView) findViewById(R.id.tester4);
        hole1 = (TextView) findViewById(R.id.hole1);
        hole2 = (TextView) findViewById(R.id.hole2);
        hole3 = (TextView) findViewById(R.id.hole3);
        hole4 = (TextView) findViewById(R.id.hole4);
        tester1.setOnTouchListener(this);
        tester2.setOnTouchListener(this);
        tester3.setOnTouchListener(this);
        tester4.setOnTouchListener(this);
        Display dis=this.getWindowManager().getDefaultDisplay();
        screenWidth = dis.getWidth();
        screenHeight = dis.getHeight();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                lastX=(int)event.getRawX();
                lastY=(int)event.getRawY();

                break;

            case MotionEvent.ACTION_MOVE:
                int dx=(int)event.getRawX()-lastX;
                int dy=(int)event.getRawY()-lastY;

                int top=v.getTop()+dy;

                int left=v.getLeft()+dx;


                if(top<=0)
                {
                    top=0;
                }
                if(top>=screenHeight - tester1.getHeight())
                {
                    top=screenHeight - tester1.getHeight();
                }
                if(left>=screenWidth - tester1.getWidth())
                {
                    left=screenWidth - tester1.getWidth();
                }

                if(left <= 0)
                {
                    left = 0;
                }

                v.layout(left, top, left + tester1.getWidth(), top + tester1.getHeight());
                lastX=(int)event.getRawX();
                lastY=(int)event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(v.getLeft()-hole1.getLeft()) <= 100 && Math.abs(v.getTop()-hole1.getTop()) <= 50)
                    v.layout(hole1.getLeft() + 15, hole1.getTop() + 10, hole1.getLeft() + 15 + tester1.getWidth(), hole1.getTop() + 10 + tester1.getHeight());
                else if (Math.abs(v.getLeft()-hole2.getLeft()) <= 100 && Math.abs(v.getTop()-hole2.getTop()) <= 50)
                    v.layout(hole2.getLeft() + 15, hole2.getTop() + 10, hole2.getLeft() + 15 + tester1.getWidth(), hole2.getTop() + 10 + tester1.getHeight());
                else if (Math.abs(v.getLeft()-hole3.getLeft()) <= 100 && Math.abs(v.getTop()-hole3.getTop()) <= 50)
                    v.layout(hole3.getLeft() + 15, hole3.getTop() + 10, hole3.getLeft() + 15 + tester1.getWidth(), hole3.getTop() + 10 + tester1.getHeight());
                else if (Math.abs(v.getLeft()-hole4.getLeft()) <= 100 && Math.abs(v.getTop()-hole4.getTop()) <= 50)
                    v.layout(hole4.getLeft() + 15, hole4.getTop() + 10, hole4.getLeft() + 15 + tester1.getWidth(), hole4.getTop() + 10 + tester1.getHeight());
                break;
        }
        return false;
    }

}