package com.example.aaron.contractwar;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Aaron on 2015/6/27.
 */
public class item {
    public String ID;
    public Bitmap logo;
    public int jobdemand;
    public int amount;
    public String description;
    public String name;

    item(String id, Resources res) {
        ID = id;
        amount = 1;
        initialize(res);
    }

    public void usage(bas_role user) {}
    public void unuse(bas_role user) {}
    private void initialize(Resources res) {
        switch (ID) {
            case "10001":
                logo = BitmapFactory.decodeResource(res, R.drawable.potion01);
                description = "恢复50%角色生命值";
                jobdemand = -1;
                name = "大还丹";
                break;
            case "22101":
                logo = BitmapFactory.decodeResource(res,R.drawable.mshoe);
                description = "角色移动速度提高100%";
                jobdemand = 1;
                name = "疾行靴";
                break;
            case "23001":
                logo = BitmapFactory.decodeResource(res,R.drawable.warmour01);
                description = "格挡时受到的伤害降低至10%";
                jobdemand = 0;
                name = "磐石";
                break;
        }
    }
}
