package com.example.aaron.contractwar;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ContactWar extends Application{
    private bas_role me = new bas_role();
    static public float scale;
    static private String userID = new String();
    static public HashMap<String,bas_role> memhashmap = new HashMap<>();
    private ArrayList<String> contactsname = new ArrayList<>();
    private ArrayList<String> phonenumber = new ArrayList<>();
    private ArrayList<bas_role> mContact = new ArrayList<>();
    private HashMap<String, intimacy_element> intimacy = new HashMap<>();
    private int screenwidth, screenheight;
    private int fightrecord[] = new int[]{0,0,0};
    private ArrayList<task> tasklist = new ArrayList<>();
    private ArrayList<item> bag = new ArrayList<>();

    private class intimacy_element{
        public int call_count;
        public int total_call_time;
        public intimacy_element(){
            call_count = 0;
            total_call_time = 0;
        }
        public intimacy_element(int c, int t){
            call_count = c;
            total_call_time = t;
        }
    }

    public void onCreate(){
        super.onCreate();
    }

    public void startbgm(Context context) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.bgm01);
        mediaPlayer.start();
    }

    public void setUser(bas_role user) { me = user; }

    public bas_role getUser() { return me; }
    public HashMap<String,bas_role> getMemhashmap(){
        return memhashmap;
    }

    public void setMemhashmap(String name, bas_role mem){
        memhashmap.put(name,mem);
    }

    public ArrayList<String> getContactsname(){
        return contactsname;
    }

    public void setPhonenumber(ArrayList<String> pn) { phonenumber = pn; }

    public ArrayList<String> getPhonenumber() { return phonenumber; }

    public void setContactsname(String name){
        contactsname.add(name);
    }

    public ArrayList<bas_role> getmContact(){
        return mContact;
    }

    public void setmContact(bas_role mem){
        mContact.add(mem);
    }

    public void setScreenAttr(int w, int h) { screenwidth = w; screenheight = h; }

    public int getScreenwidth(){ return screenwidth; }

    public int getScreenheight(){ return screenheight; }

    public static float dp2pix(float dpvalue) { return dpvalue*scale; }

    public void initInitimacy(){
        for (int i = 0 ; i != contactsname.size(); ++i){
            intimacy.put(contactsname.get(i),new intimacy_element());
        }
    }

    public void setIntimacy(String name, int t){
        Log.i("name",name);
        intimacy.get(name).call_count += 1;
        intimacy.get(name).total_call_time += t;
    }

    static public String getnamebynumber(String number){
        Iterator iter = memhashmap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object val = entry.getValue();
            if ( number.compareTo(((bas_role)val).getPhone_number()) == 0 ){
                return (String)entry.getKey();
            }
        }
        return null;
    }

    public int getIntimacyLevel(String name){
        int cnt = intimacy.get(name).call_count;
        int total = intimacy.get(name).total_call_time;
        if (total >= 7200 && cnt >= 15) {
            return 5;
        } else if (total >= 3600 && cnt >= 10 ) {
            return 4;
        } else if (total >= 1800 && cnt >= 5) {
            return 3;
        } else if (cnt >= 5) {
            return 2;
        } else if (cnt >= 1) {
            return 1;
        } else
            return 0;
    }

    public void fighted(int type) {
        fightrecord[type]++;
    }

    public ArrayList<task> getTasklist() {
        return tasklist;
    }

    public int getfightrecord(int order) {
        return fightrecord[order];
    }

    public ArrayList<item> getBag() { return bag; }

    public boolean addtobag(item item) {
        if (bag.size() < 16) {
            bag.add(item);
            return true;
        } else {
            return false;
        }
    }

    static public void setUserID(String id) { userID = id; }
    static public String getUserID() { return userID; }
}
