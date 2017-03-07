package com.example.aaron.contractwar;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

/**
 * Created by Aaron on 2015/6/27.
 */
public class potion extends item {

    public potion(String id,Resources res) {
        super(id,res);
    }

    @Override
    public void usage(bas_role user) {
        switch (ID) {
            case "10001":
                user.HP = user.HP + user.UHP/2 <= user.UHP? user.HP + user.UHP/2 : user.HP;
                break;
        }
    }
}
