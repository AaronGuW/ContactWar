package com.example.aaron.contractwar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Aaron on 2015/4/1.
 */
public class SplashScreenActivity extends Activity {
    Context mContext = null;
    private static final String[] PHONES_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID };
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;           /**联系人显示名称**/
    private static final int PHONES_NUMBER_INDEX = 1;                  /**电话号码**/
    private static final int PHONES_PHOTO_ID_INDEX = 2;               /**头像ID**/
    private static final int PHONES_CONTACT_ID_INDEX = 3;             /**联系人的ID**/
    private static final String GET_FAILUER = "0", NOT_INTEAM = "1";
    private ArrayList<String> phonenumber;           /**联系人号码用于去除重复**/
    private static boolean initOK = false;

    private static List<String> Yidong = Arrays.asList("134", "135", "136", "137", "138", "139", "150", "151", "152", "157", "158", "159", "182", "183", "188", "187");
    private static List<String> Liantong = Arrays.asList("130","131","132","155","156","186","185");
    private static List<String> Dianxin = Arrays.asList("133","153","180","181","189");

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj.equals(GET_FAILUER)) {
                initOK = false;
            } else if (msg.obj.equals(NOT_INTEAM)) {
                initOK = true;
            } else {
                initOK = true;
                String[] members = msg.obj.toString().split("&");
                myApp.getUser().in_team = true;
                for (int i = 0 ; i != members.length ; ++i) {
                    myApp.getUser().team_number.add(myApp.getMemhashmap().get(ContactWar.getnamebynumber(members[i])));
                }
            }
        }
    };

    private ContactWar myApp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        myApp = (ContactWar)getApplication();
        phonenumber = new ArrayList<>();
        setContentView(R.layout.splashscreen);

        getPhoneContacts();
        inti_user();

        inti_intimacy();
        setScreenAttr();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                /* Create an Intent that will start the Main WordPress Activity. */
                /*if (initOK)*/if (true) {
                    Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                } else {
                    Toast.makeText(SplashScreenActivity.this,"网络状况不佳，请检查网络后重新登录",Toast.LENGTH_SHORT).show();
                }
                SplashScreenActivity.this.finish();
            }
        }, 2000); //2000 for release

    }

    private void setScreenAttr(){
        WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        myApp.setScreenAttr(display.getWidth(),display.getHeight());
        ContactWar.scale = getResources().getDisplayMetrics().density;
    }

    static private int getHp(int job, int num){
        int bas;
        switch (job){
            case 0: bas = 10000; break;
            case 1: bas = 6000; break;
            case 2: bas = 8000; break;
            default: bas = 8000;
        }
        int sgn = (num/1000)%2 == 1?1:-1;
        return bas+sgn*(num%1000);
    }

    static private int getATK(int job, int num){
        int bas, calibration;
        switch (job){
            case 0: bas = 800; calibration = 1; break;
            case 1: bas = 1200; calibration = 2; break;
            case 2: bas = 900; calibration = 1; break;
            default: bas = 900; calibration = 1;
        }
        int sgn = (num/1000)%2 == 1?1:-1;
        return bas+sgn*calibration*((num%1000)/10);
    }

    private void getPhoneContacts() {
        ContentResolver resolver = mContext.getContentResolver();
        //获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);


        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                //得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);

                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;

                //调试用
                phoneNumber = phoneNumber.replaceAll(" ","");
                phoneNumber = phoneNumber.replaceAll("-","");
                if (phoneNumber.compareTo(ContactWar.getUserID()) == 0) {
                    continue;
                }
                Log.i("phonenumber",phoneNumber);

                //得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                Log.i("contactname",contactName);

                //得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

                //得到联系人头像ID
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

                //得到联系人头像Bitamp
                Bitmap contactPhoto = null;

                //随机一个职业
                Random random = new Random();
                int score = Math.abs(random.nextInt())%20;
                int team_score = Math.abs(random.nextInt())%30;
                //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                if(photoid > 0 ) {
                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
                    contactPhoto = BitmapFactory.decodeStream(input);
                }else {
                    contactPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.contact_photo);
                }
                if (!phonenumber.contains(phoneNumber) && phoneNumber.length() == 11){
                    phonenumber.add(phoneNumber);
                    bas_role tmp = new bas_role();
                    int job = 0;
                    if (Yidong.contains(phoneNumber.substring(0,3))){
                        job = 0;
                        tmp.basspeed = tmp.speed = 5;
                    } else if (Liantong.contains(phoneNumber.substring(0,3))){
                        job = 1;
                        tmp.basspeed = tmp.speed = 3;
                    } else {
                        job = 2;
                        tmp.basspeed = tmp.speed = 4;
                    }
                    tmp.initialize(phoneNumber,contactPhoto,contactName);
                    tmp.setjob(job);
                    tmp.setHP(getHp(job,Integer.parseInt(phoneNumber.substring(3,7))));
                    tmp.UHP = tmp.HP;
                    tmp.setATK(getATK(job,Integer.parseInt(phoneNumber.substring(7,11))));
                    tmp.add_score(score*10);
                    tmp.add_teamscore(team_score*10);

                    myApp.setContactsname(contactName);
                    myApp.setMemhashmap(contactName,tmp);
                    myApp.setmContact(tmp);
                }
            }
            phoneCursor.close();
            myApp.setPhonenumber(phonenumber);
        }
        ArrayList<String> names = myApp.getContactsname();
        Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
        String[] namelist = names.toArray(new String[0]);
        Arrays.sort(namelist,cmp);
        names.clear();
        for (int i = 0 ; i != namelist.length ; ++i) {
            names.add(new String(namelist[i]));
        }
    }

    private void inti_user(){
        //String number = ContactWar.getUserID();
        String number = "18918261855";
        HttpUtil.HttpClientGET("http://10.0.2.2:8008/request?username=" + ContactWar.getUserID() + "&mode=getteammember",mhandler);
        Bitmap contactPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.contact_photo);
        bas_role me = new bas_role();
        me.initialize(number,contactPhoto,"我");
        int job = 0;
        if (Yidong.contains(number.substring(0,3))){
            job = 0;
            me.basspeed = me.speed = 5;
        } else if (Liantong.contains(number.substring(0,3))){
            job = 1;
            me.basspeed = me.speed = 3;
        } else {
            job = 2;
            me.basspeed = me.speed = 4;
        }
        me.setjob(job);
        me.setHP(getHp(job,Integer.parseInt(number.substring(3,7))));
        me.UHP = me.HP;
        me.setATK(getATK(job,Integer.parseInt(number.substring(7,11))));
        myApp.setUser(me);
        HttpUtil.HttpClientGET("http://10.0.2.2:8008/request?username=" + ContactWar.getUserID() +"&mode=getteammember",null);
    }

    private void inti_intimacy(){
        myApp.initInitimacy();
        Cursor cursor = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                boolean flag;
                String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                Log.i("number",number);
                if (number.compareTo(ContactWar.getUserID()) == 0)
                    continue;
                switch (Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)))) {
                    case CallLog.Calls.INCOMING_TYPE:
                    case CallLog.Calls.OUTGOING_TYPE:
                        flag = true;
                        break;
                    default:
                        flag = false;
                }
                SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))));
                //呼叫时间
                String time = sfd.format(date);
                //联系人
                String name = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
                //通话时间,单位:s
                String duration = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION));
                if (flag && phonenumber.contains(number)){
                    if (name == null){
                        name = myApp.getnamebynumber(number);
                    }
                    if (name == null)
                        continue;
                    myApp.setIntimacy(name,Integer.valueOf(duration));
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
    }

}
