package com.example.aaron.contractwar;

import android.content.res.Resources;

/**
 * Created by Aaron on 2015/6/27.
 */
public class equipment extends item {

    public equipment(String id, Resources res) {
        super(id,res);
    }

    @Override
    public void usage(bas_role user) {
        switch (ID) {
            case "22101":
                user.speed = user.basspeed*2;
                break;
        }
    }

    @Override
    public void unuse(bas_role user) {
        switch (ID) {
            case "22101":
                user.speed = user.basspeed;
                break;
        }
    }
}
