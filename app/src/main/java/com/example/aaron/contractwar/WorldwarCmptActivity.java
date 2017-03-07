package com.example.aaron.contractwar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Aaron on 2015/9/12.
 */
public class WorldwarCmptActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worldwarmain);
        String signed = getIntent().getStringExtra("signed");
        if (signed.equals("1")) {
            ((TextView)findViewById(R.id.sorry)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.chanceleft)).setText("10");
            ((TextView)findViewById(R.id.score)).setText("0");
        } else {
            ((Button)findViewById(R.id.fight_btn)).setVisibility(View.GONE);
        }
    }
}
