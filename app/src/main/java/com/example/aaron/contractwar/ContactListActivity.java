package com.example.aaron.contractwar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class ContactListActivity extends Activity {

    private ArrayList<String> contactsname = new ArrayList<>();            /**联系人姓名用于提示**/
    private String[] namelist = {};                                         /**联系人姓名用于排序**/
    private String target;                                                  /**记录被选中的联系人**/
    private HashMap<String,bas_role> hashMap = new HashMap<>();            /**<联系人姓名, bas_role>表**/
    private Bitmap star[];                                                    /**亲密度等级**/
    private ImageView stars;                                                /**弹出窗口的星星view**/
    private TextView buff_des;                                               /**buff详述**/
    private ContactWar myApp;
    private bas_role user;
    private Boolean Selected = false;
    private AutoCompleteTextView searchbar;
    private TextView name;
    private ImageView friend_img;
    private TextView img_hint, hp, atk;
    private ImageView search_btn,hp_head, atk_head;
    private Button fight_btn, invite_btn, yes, no;
    private AlphaAnimation in_animation;
    private AlphaAnimation out_animation;
    private AlphaAnimation out_disappear_animation;
    private AlphaAnimation out_then_in_animation;
    private Bitmap defaultphoto;
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String res = msg.obj.toString();
            if (res.equals("1")) {
                Toast.makeText(ContactListActivity.this, "组队邀请已成功发送" , Toast.LENGTH_SHORT).show();
            } else if (res.equals("2")) {
                Toast.makeText(ContactListActivity.this, "对方已有队伍" , Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ContactListActivity.this, "请检查网络" , Toast.LENGTH_SHORT).show();
            }
        }
    };



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        name = (TextView)findViewById(R.id.name);
        searchbar = (AutoCompleteTextView)findViewById(R.id.search_bar);
        img_hint = (TextView)findViewById(R.id.img_hint);
        hp = (TextView)findViewById(R.id.hp);
        hp_head = (ImageView)findViewById(R.id.hp_head);
        atk = (TextView)findViewById(R.id.atk);
        atk_head = (ImageView)findViewById(R.id.atk_head);
        friend_img = (ImageView)findViewById(R.id.friend_img);
        search_btn = (ImageButton)findViewById(R.id.search_btn);
        fight_btn = (Button)findViewById(R.id.fight_btn);
        invite_btn = (Button)findViewById(R.id.invite_btn);
        myApp = (ContactWar)getApplication();
        user = myApp.getUser();
        contactsname = myApp.getContactsname();
        hashMap = myApp.getMemhashmap();
        initAnim();
        friend_img.setVisibility(View.GONE);
        defaultphoto = BitmapFactory.decodeResource(getResources(),R.drawable.contact_photo);

        fight_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactListActivity.this,fightsurfaceview.class);
                intent.putExtra("enemy",target);
                startActivity(intent);
            }
        });


        /**邀请组队按钮监听以及弹出窗口的加载**/
        final LayoutInflater inflater = LayoutInflater.from(this);
        View invite_hint = inflater.inflate(R.layout.invite_hint, null);
        final PopupWindow pop = new PopupWindow(invite_hint, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        pop.setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_bg));
        star = new Bitmap[6];
        star[0] = BitmapFactory.decodeResource(getResources(), R.drawable.star_0);
        star[1] = BitmapFactory.decodeResource(getResources(), R.drawable.star_1);
        star[2] = BitmapFactory.decodeResource(getResources(), R.drawable.star_2);
        star[3] = BitmapFactory.decodeResource(getResources(), R.drawable.star_3);
        star[4] = BitmapFactory.decodeResource(getResources(), R.drawable.star_4);
        star[5] = BitmapFactory.decodeResource(getResources(), R.drawable.star_5);
        buff_des = (TextView)invite_hint.findViewById(R.id.buff_description);
        yes = (Button)invite_hint.findViewById(R.id.yes);
        no = (Button)invite_hint.findViewById(R.id.no);
        stars = (ImageView)invite_hint.findViewById(R.id.stars);
        if (user.team_number.size() >= 2)
            yes.setEnabled(false);
        else {
            yes.setEnabled(true);
        }
        yes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                HttpUtil.HttpClientGET("http://10.0.2.2:8008/request?username=" + ContactWar.getUserID() + "&mode=invite&target=" + myApp.getMemhashmap().get(target).getPhone_number(),mhandler);
                if(pop.isShowing()){
                    pop.dismiss();
                }
            }
        });

        no.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (pop.isShowing()){
                    pop.dismiss();
                }
            }
        });

        invite_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("invite button","pressed");
                int level = myApp.getIntimacyLevel(target);
                double calibration;
                switch (level){
                    case 0: calibration = 0; break;
                    case 1: calibration = 0.02; break;
                    case 2: calibration = 0.05; break;
                    case 3: calibration = 0.1; break;
                    case 4: calibration = 0.15; break;
                    case 5: calibration = 0.2; break;
                    default: calibration = 0;
                }
                stars.setImageBitmap(star[level]);
                double hp_buff = (double)myApp.getUser().getHP()*calibration;
                double atk_buff = (double)myApp.getUser().getATK()*calibration;
                buff_des.setText("血量： +" + String.valueOf((int)hp_buff) + "\n攻击： +" + String.valueOf((int)atk_buff));
                boolean flag = true;
                for (int i = 0; i != user.team_number.size() ; ++i) {
                    if (target.compareTo(user.team_number.get(i).getId()) == 0) {
                        yes.setEnabled(false);
                        flag = false;
                        break;
                    }
                }
                if (user.team_number.size() >= 2) {
                    yes.setEnabled(false);
                    flag = false;
                }
                if (flag) yes.setEnabled(true);

                pop.setFocusable(true);
                pop.showAtLocation((View)v.getParent(), Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });


        /**设置搜索框的适配器**/
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.search_bar);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_item,contactsname);

        /**将联系人按姓名排序**/
        Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
        namelist = contactsname.toArray(new String[0]);
        Arrays.sort(namelist,cmp);
        autoCompleteTextView.setAdapter(adapter);

        /**设置通讯录Listview的适配器**/
        ListView lv = (ListView)findViewById(R.id.contact_list);
        BaseAdapter ba = new BaseAdapter() {
            @Override
            public int getCount() { return contactsname.size();}
            @Override
            public Object getItem(int position) { return null;}
            @Override
            public long getItemId(int position) { return 0;}
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                String cname = namelist[position];
                View view;
                if (convertView != null) {
                    view = convertView;
                } else {
                    view = inflater.inflate(R.layout.contactitem,null);
                }
                ImageView img = (ImageView)view.findViewById(R.id.contact_photo);
                if (hashMap.get(cname).getPhoto() != null)
                    img.setImageBitmap(hashMap.get(cname).getPhoto());
                else
                    img.setImageBitmap(defaultphoto);
                TextView name = (TextView)view.findViewById(R.id.contact_name);
                name.setText(hashMap.get(cname).getId());
                TextView level = (TextView)view.findViewById(R.id.contact_level);
                level.setText("等级： " + hashMap.get(cname).getLv());
                TextView job = (TextView)view.findViewById(R.id.job);
                int cjob = hashMap.get(cname).getjob();
                if (cjob == 0)
                    job.setText("职业： 战士");
                else if (cjob == 1)
                    job.setText("职业： 法师");
                else
                    job.setText("职业： 圣骑士");
                ImageView rank = (ImageView)view.findViewById(R.id.rank);
                switch (myApp.getIntimacyLevel(cname)) {
                    case 0:
                        rank.setImageDrawable(getResources().getDrawable(R.drawable.star_0));
                        break;
                    case 1:
                        rank.setImageDrawable(getResources().getDrawable(R.drawable.star_1));
                        break;
                    case 2:
                        rank.setImageDrawable(getResources().getDrawable(R.drawable.star_2));
                        break;
                    case 3:
                        rank.setImageDrawable(getResources().getDrawable(R.drawable.star_3));
                        break;
                    case 4:
                        rank.setImageDrawable(getResources().getDrawable(R.drawable.star_4));
                        break;
                    case 5:
                        rank.setImageDrawable(getResources().getDrawable(R.drawable.star_5));
                        break;
                }

                return view;
            }
        };
        lv.setAdapter(ba);

        lv.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    target = namelist[position];
                    bas_role targ = hashMap.get(target);
                    Drawable img;
                    int job = targ.getjob(), ihp = targ.getHP(), iatk = targ.getATK();
                    switch (job){
                        case 0: img = getResources().getDrawable(R.drawable.warrior); break;
                        case 1: img = getResources().getDrawable(R.drawable.magician); break;
                        case 2: img = getResources().getDrawable(R.drawable.doctor); break;
                        default: img = getResources().getDrawable(R.drawable.warrior);
                    }
                    name.setText(target);
                    hp.setText(String.valueOf(ihp));
                    atk.setText(String.valueOf(iatk));

                    if (Selected) {
                        friend_img.clearAnimation();
                        out_then_in_animation.setAnimationListener(new outtheninListener(img));
                        friend_img.setAnimation(out_then_in_animation);
                        out_then_in_animation.start();
                    }else{
                        Selected = true;
                        friend_img.clearAnimation();
                        img_hint.setVisibility(View.GONE);
                        friend_img.setVisibility(View.VISIBLE);
                        friend_img.setImageDrawable(img);
                        friend_img.setAnimation(in_animation);
                        in_animation.start();
                        fight_btn.setVisibility(View.VISIBLE);
                        invite_btn.setVisibility(View.VISIBLE);
                        hp.setVisibility(View.VISIBLE);
                        hp_head.setVisibility(View.VISIBLE);
                        atk.setVisibility(View.VISIBLE);
                        atk_head.setVisibility(View.VISIBLE);
                    }
                }
            }
        );

        search_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                String target = searchbar.getText().toString();
                if (hashMap.containsKey(target)){
                    bas_role targ = hashMap.get(target);
                    name.setText(target);
                    Drawable img;
                    int job = targ.getjob(), ihp = targ.getHP(), iatk = targ.getATK();
                    atk.setText(String.valueOf(iatk));
                    hp.setText(String.valueOf(ihp));
                    switch (job){
                        case 0: img = getResources().getDrawable(R.drawable.warrior); break;
                        case 1: img = getResources().getDrawable(R.drawable.magician); break;
                        case 2: img = getResources().getDrawable(R.drawable.doctor); break;
                        default: img = getResources().getDrawable(R.drawable.warrior);
                    }
                    if (Selected) {
                        out_then_in_animation.setAnimationListener(new outtheninListener(img));
                        friend_img.clearAnimation();
                        friend_img.setAnimation(out_then_in_animation);
                        out_then_in_animation.start();
                    }else{
                        Selected = true;
                        friend_img.clearAnimation();
                        img_hint.setVisibility(View.GONE);
                        friend_img.setVisibility(View.VISIBLE);
                        friend_img.setImageDrawable(img);
                        friend_img.setAnimation(in_animation);
                        in_animation.start();
                        fight_btn.setVisibility(View.VISIBLE);
                        invite_btn.setVisibility(View.VISIBLE);
                        hp.setVisibility(View.VISIBLE);
                        hp_head.setVisibility(View.VISIBLE);
                        atk.setVisibility(View.VISIBLE);
                        atk_head.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    name.setText(target + " 是谁？");
                    if (Selected) {
                        Selected = false;
                        friend_img.clearAnimation();
                        friend_img.setAnimation(out_disappear_animation);
                        out_disappear_animation.start();
                        fight_btn.setVisibility(View.GONE);
                        invite_btn.setVisibility(View.GONE);
                        hp.setVisibility(View.GONE);
                        hp_head.setVisibility(View.GONE);
                        atk.setVisibility(View.GONE);
                        atk_head.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void initAnim(){
        in_animation = new AlphaAnimation(0,1);
        in_animation.setDuration(500);
        in_animation.setFillAfter(true);
        out_animation = new AlphaAnimation(1,0);
        out_animation.setDuration(500);
        out_animation.setFillAfter(true);
        out_then_in_animation = new AlphaAnimation(1,0);
        out_then_in_animation.setDuration(500);
        out_then_in_animation.setFillAfter(true);
        out_disappear_animation = new AlphaAnimation(1,0);
        out_disappear_animation.setDuration(500);
        out_disappear_animation.setFillAfter(true);
        out_disappear_animation.setAnimationListener(new disappearlistener());
    }

    private class outtheninListener implements Animation.AnimationListener {
        Drawable img;

        public outtheninListener(Drawable img){
            this.img = img;
        }

        public void onAnimationEnd(Animation animation) {
            friend_img.setImageDrawable(img);
            friend_img.setAnimation(in_animation);
            in_animation.start();
        }

        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }

        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

        }
    }

    private class disappearlistener implements Animation.AnimationListener {

        public void onAnimationEnd(Animation animation) {
            img_hint.setVisibility(View.VISIBLE);
            friend_img.setVisibility(View.GONE);
        }

        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }

        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }
}
