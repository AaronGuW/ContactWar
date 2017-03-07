package com.example.aaron.contractwar;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Aaron on 2015/8/12.
 */
public class Request {

    static public String getRequestContent(String req) {
        String content = new String();
        int index = req.indexOf("*");
        String number = req.substring(index+1,index+12);
        String name = ContactWar.getnamebynumber(number);
        if (name != null) {
            content += name + " ";
        } else {
            content += number + " ";
        }
        if (String.valueOf(req.charAt(1)).compareTo("1") == 0) {
            content += "邀请您加入队伍!";
        } else if (String.valueOf(req.charAt(1)).compareTo("2") == 0) {
            String type = String.valueOf(req.charAt(req.length()-1));
            if (type.equals("0")) {
                content += "拒绝了您的组队邀请";
            } else if (type.equals("1")) {
                content += "接受了您的组队邀请";
            } else if (type.equals("2")) {
                content += "退出了队伍";
            } else if (type.equals("3")) {
                content += "退出了队伍，您的队伍解散了";
            }
        } else if (String.valueOf(req.charAt(1)).compareTo("3") == 0) {
            content += "赠送给您一件物品，请前往包裹查看";
        }
        return content;
    }

    static public String getRequestType(String req) {
        String type = req.substring(1,2);
        if (type.compareTo("1") == 0) {
            return "组队邀请";
        } else if (type.compareTo("2") == 0) {
            return "组队通知";
        } else if (type.compareTo("3") == 0) {
            return "物品赠送";
        } else {
            return null;
        }
    }

    static public Bitmap getphoto(String req,Resources res) {
        int index = req.indexOf("*");
        String number = req.substring(index+1,index+12);
        String name = ContactWar.getnamebynumber(number);
        if (name != null) {
            return ContactWar.memhashmap.get(name).getPhoto();
        } else {
            return BitmapFactory.decodeResource(res,R.drawable.contact_photo);
        }
    }

    static public class reqreceiver extends Service {

        private String userid = new String();
        private IBinder binder = new reqreceiver.LocalBinder();
        private boolean paused = false,active = true;
        private Thread maintask = new Thread(new Runnable(){
            @Override
            public void run(){
                Looper.prepare();
                try {
                    String res = new String();
                    while (active) {
                        userid = ContactWar.getUserID();
                        res = HttpUtil.HttpClientGET("http://10.0.2.2:8008/request?username=" + userid + "&mode=get", MainActivity.reqhandler);
                        Thread.sleep(5000);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        @Override
        public IBinder onBind(Intent intent) {
            return binder;
        }

        public class LocalBinder extends Binder {
            //返回本地服务
            reqreceiver getService(){
                return reqreceiver.this;
            }
        }

        @Override
        public void onStart(Intent intent, int startId) {
            Log.i("service", "onStart");
            super.onStart(intent, startId);
        }

        @Override
        public void onCreate() {
            Log.i("service","start!");
            super.onCreate();
            maintask.start();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            active = false;
        }

    }

}
