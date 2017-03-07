package com.example.aaron.contractwar;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.InputStream;

/**
 * Created by Aaron on 2015/6/18.
 */
public class monsterlistactivity extends Activity {
    private Resources res;
    private Button fm1;
    private Button fm2;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monsterlist);

        fm1 = (Button)findViewById(R.id.m1_fight);
        fm2 = (Button)findViewById(R.id.m2_fight);
        res = getResources();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(monsterlistactivity.this,pve.class);
                if (v == fm1) {
                    intent.putExtra("monster","1");
                } else if (v == fm2) {
                    intent.putExtra("monster","2");
                }
                startActivity(intent);
            }
        };
        fm1.setOnClickListener(listener);
        fm2.setOnClickListener(listener);
    }

}
