package com.example.aaron.contractwar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Aaron on 2015/6/16.
 */
public class character extends Activity {

    private ContactWar myApp;
    private ImageView user_img, basic_attack, spec_cap, skill1, skill2, del_skill1_1, del_skill1_2, del_skill2_1, del_skill2_2;
    private ImageView lock1, lock2, lock3, lock4;
    private TextView hp_data, atk_data, remainpoint;
    private SpringProgressView expbar;
    private bas_role user;
    private TextView lv,hpupdata,atkupdata,itemname,jobrequirement,itemdescription;
    private Button surebutton,hpup,hpdown,atkup,atkdown;
    private int hppoint = 0,atkpoint = 0;
    private int remainpoints = 0;
    private ImageView blocks[],itemimg,equippedpotion,equipment1,equipment2;
    private Bitmap defaultphoto;
    private ArrayList<item> bag;
    private ArrayList<String> contactnames;
    private HashMap<String,bas_role> hashMap;
    private LayoutInflater inflater;
    private View menu,info,contact_list;
    private Button equip,give,eqinfo,abandon;
    private int focuseditem;
    private PopupWindow popmenu,popinfo,poplist;
    private String to = new String();
    private View.OnClickListener itemonclicklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0 ; i != 16 ; ++i) {
                if (v == blocks[i] && i < bag.size()) {
                    focuseditem = i;
                    int location[] = new int[2];
                    v.getLocationOnScreen(location);
                    popmenu.setFocusable(true);
                    popmenu.setOutsideTouchable(true);
                    popmenu.showAtLocation(v, Gravity.NO_GRAVITY, location[0] - (int)(1.55*v.getWidth()), location[1] - (int)(1.48*v.getHeight()));
                }
            }
        }
    };
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj.toString().compareTo("1")==0){
                bag.remove(focuseditem);
                redrawbag();
                Toast.makeText(character.this,"赠送成功",Toast.LENGTH_SHORT).show();
                if (poplist.isShowing())
                    poplist.dismiss();
            } else {
                Toast.makeText(character.this,"请检查网络",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        lv.setText("Lv."+user.getLv());
        expbar.setCurrentCount(user.getExp());
        expbar.setMaxCount(user.getNextlvexp());
        remainpoints = user.getAttrpoint();
        remainpoint.setText(String.valueOf(remainpoints));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.characherinfo);

        defaultphoto = BitmapFactory.decodeResource(getResources(),R.drawable.contact_photo);
        atk_data = (TextView)findViewById(R.id.atk_data);
        hp_data = (TextView)findViewById(R.id.hp_data);
        user_img = (ImageView)findViewById(R.id.uer_img);
        lv = (TextView)findViewById(R.id.lv);
        expbar = (SpringProgressView)findViewById(R.id.exp_bar);
        surebutton = (Button)findViewById(R.id.surebutton);
        hpup = (Button)findViewById(R.id.hpup);
        hpdown = (Button)findViewById(R.id.hpdown);
        atkup = (Button)findViewById(R.id.atkup);
        atkdown = (Button)findViewById(R.id.atkdown);
        atkupdata = (TextView)findViewById(R.id.atkupdata);
        hpupdata = (TextView)findViewById(R.id.hpupdata);
        remainpoint = (TextView)findViewById(R.id.number_remain);

        basic_attack = (ImageView)findViewById(R.id.basic_attack);
        spec_cap = (ImageView)findViewById(R.id.spec_cap);
        skill1 = (ImageView)findViewById(R.id.skill1);
        skill2 = (ImageView)findViewById(R.id.skill2);
        del_skill1_1 = (ImageView)findViewById(R.id.del_skill1_1);
        del_skill1_2 = (ImageView)findViewById(R.id.del_skill1_2);
        del_skill2_1 = (ImageView)findViewById(R.id.del_skill2_1);
        del_skill2_2 = (ImageView)findViewById(R.id.del_skill2_2);
        lock1 = (ImageView)findViewById(R.id.lock1);
        lock2 = (ImageView)findViewById(R.id.lock2);
        lock3 = (ImageView)findViewById(R.id.lock3);
        lock4 = (ImageView)findViewById(R.id.lock4);

        blocks = new ImageView[16];
        blocks[0] = (ImageView)findViewById(R.id.block1);
        blocks[1] = (ImageView)findViewById(R.id.block2);
        blocks[2] = (ImageView)findViewById(R.id.block3);
        blocks[3] = (ImageView)findViewById(R.id.block4);
        blocks[4] = (ImageView)findViewById(R.id.block5);
        blocks[5] = (ImageView)findViewById(R.id.block6);
        blocks[6] = (ImageView)findViewById(R.id.block7);
        blocks[7] = (ImageView)findViewById(R.id.block8);
        blocks[8] = (ImageView)findViewById(R.id.block9);
        blocks[9] = (ImageView)findViewById(R.id.block10);
        blocks[10] = (ImageView)findViewById(R.id.block11);
        blocks[11] = (ImageView)findViewById(R.id.block12);
        blocks[12] = (ImageView)findViewById(R.id.block13);
        blocks[13] = (ImageView)findViewById(R.id.block14);
        blocks[14] = (ImageView)findViewById(R.id.block15);
        blocks[15] = (ImageView)findViewById(R.id.block16);
        equipment1 = (ImageView)findViewById(R.id.equipment1);
        equipment2 = (ImageView)findViewById(R.id.equipment2);
        equippedpotion = (ImageView)findViewById(R.id.equippedpotion);

        myApp = (ContactWar)getApplication();
        contactnames = myApp.getContactsname();
        hashMap = myApp.getMemhashmap();
        user = myApp.getUser();
        if (user.equipments[0] != null) {
            equipment1.setImageBitmap(user.equipments[0].logo);
        }
        if (user.equipments[1] != null) {
            equipment2.setImageBitmap(user.equipments[1].logo);
        }
        if (user.bottle != null) {
            equippedpotion.setImageBitmap(user.bottle.logo);
        }

        setUser();
        bag = new ArrayList<>();
        bag = myApp.getBag();

        if (bag.size() == 0) {
            myApp.addtobag(new potion("10001", getResources()));
            myApp.addtobag(new equipment("22101", getResources()));
            myApp.addtobag(new equipment("23001", getResources()));
        }

        inflater = LayoutInflater.from(this);
        menu = inflater.inflate(R.layout.itemmenu,null);
        popmenu = new PopupWindow(menu, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        popmenu.setBackgroundDrawable(getResources().getDrawable(R.drawable.itemmenubg));
        info = inflater.inflate(R.layout.iteminfo,null);
        popinfo = new PopupWindow(info,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        popinfo.setBackgroundDrawable(getResources().getDrawable(R.drawable.itemmenubg));

        itemimg = (ImageView) info.findViewById(R.id.itemimg);
        itemname = (TextView) info.findViewById(R.id.itemname);
        itemdescription = (TextView) info.findViewById(R.id.itemdescription);
        jobrequirement = (TextView) info.findViewById(R.id.jobrequirement);

        equip = (Button)menu.findViewById(R.id.equip);
        give = (Button)menu.findViewById(R.id.give);
        eqinfo = (Button)menu.findViewById(R.id.eqinfo);
        abandon = (Button)menu.findViewById(R.id.abandon);

        //初始化联系人列表，用于赠送对象选择
        contact_list = inflater.inflate(R.layout.contactlist,null);
        ListView cl = (ListView)contact_list.findViewById(R.id.contact_list);
        BaseAdapter ba = new BaseAdapter() {
            @Override
            public int getCount() { return contactnames.size();}
            @Override
            public Object getItem(int position) { return null;}
            @Override
            public long getItemId(int position) { return 0;}
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                String cname = contactnames.get(position);
                View view;
                if (convertView != null) {
                    view = convertView;
                } else {
                    view = inflater.inflate(R.layout.contactitem_s,null);
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
        cl.setAdapter(ba);
        poplist = new PopupWindow(contact_list,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        poplist.setBackgroundDrawable(getResources().getDrawable(R.drawable.contactlistbg));
        cl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                to = hashMap.get(contactnames.get(position)).getPhone_number();
                HttpUtil.HttpClientGET("http://10.0.2.2:8008/request?username=" + ContactWar.getUserID() + "&mode=give&to=" + to + "&item=" + bag.get(focuseditem).ID, mhandler);
            }
        });

        abandon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bag.remove(focuseditem);
                redrawbag();
                if (popmenu.isShowing()) {
                    popmenu.dismiss();
                }
            }
        });

        give.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poplist.setFocusable(true);
                poplist.showAtLocation((RelativeLayout)findViewById(R.id.bg), Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                if (popmenu.isShowing()) {
                    popmenu.dismiss();
                }
            }
        });

        equip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (bag.get(focuseditem).ID.substring(0,1)) {
                    case "1":
                        equippedpotion.setImageBitmap(bag.get(focuseditem).logo);
                        if (user.bottle != null) {
                            bag.add(user.bottle);
                        }
                        user.bottle = (potion) bag.get(focuseditem);
                        bag.remove(focuseditem);
                        if (popmenu.isShowing()) {
                            popmenu.dismiss();
                        }
                        if (popinfo.isShowing()) {
                            popinfo.dismiss();
                        }
                        redrawbag();
                        break;
                    case "2":
                        if (bag.get(focuseditem).jobdemand == -1 || bag.get(focuseditem).jobdemand == user.getjob() ) {
                            switch (bag.get(focuseditem).ID.substring(1, 2)) {
                                case "0":
                                    equipment1.setImageBitmap(bag.get(focuseditem).logo);
                                    if (user.equipments[0] != null) {
                                        bag.add(user.equipments[0]);
                                        user.equipments[0].unuse(user);
                                    }
                                    user.equipments[0] = (equipment) bag.get(focuseditem);
                                    user.equipments[0].usage(user);
                                    bag.remove(focuseditem);
                                    break;
                                default:
                                    equipment2.setImageBitmap(bag.get(focuseditem).logo);
                                    if (user.equipments[1] != null) {
                                        bag.add(user.equipments[1]);
                                        user.equipments[1].unuse(user);
                                    }
                                    user.equipments[1] = (equipment) bag.get(focuseditem);
                                    user.equipments[1].usage(user);
                                    bag.remove(focuseditem);
                            }
                            if (popmenu.isShowing()) {
                                popmenu.dismiss();
                            }
                            if (popinfo.isShowing()) {
                                popinfo.dismiss();
                            }
                            redrawbag();
                        } else {
                            Toast mtoast = new Toast(character.this);
                            mtoast.makeText(character.this,"装备职业不符，送人吧~",Toast.LENGTH_SHORT).show();
                        }
                }
            }
        });

        eqinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (bag.get(focuseditem).jobdemand) {
                    case -1:
                        jobrequirement.setText("通用");
                        break;
                    case 0:
                        jobrequirement.setText("战士");
                        break;
                    case 1:
                        jobrequirement.setText("法师");
                        break;
                    case 2:
                        jobrequirement.setText("圣骑士");
                        break;
                }
                itemname.setText(bag.get(focuseditem).name);
                itemdescription.setText(bag.get(focuseditem).description);
                itemimg.setImageBitmap(bag.get(focuseditem).logo);
                int location[] = new int[2];
                blocks[focuseditem].getLocationOnScreen(location);
                popinfo.setFocusable(true);
                popinfo.setOutsideTouchable(true);
                popinfo.showAtLocation((View)blocks[focuseditem], Gravity.NO_GRAVITY, location[0] - (int)(4.4*blocks[focuseditem].getWidth()), location[1] - (int)(3.05*blocks[focuseditem].getHeight()));
            }
        });

        for (int i = 0 ; i != 16 ; ++i) {
            blocks[i].setOnClickListener(itemonclicklistener);
        }

        for (int i = 0 ; i != bag.size(); ++i) {
            blocks[i].setImageBitmap(bag.get(i).logo);
        }

        remainpoints = user.getAttrpoint();
        remainpoint.setText(String.valueOf(remainpoints));
        if (remainpoints == 0) {
            atkup.setVisibility(View.GONE);
            atkdown.setVisibility(View.GONE);
            hpup.setVisibility(View.GONE);
            hpdown.setVisibility(View.GONE);
            surebutton.setVisibility(View.GONE);
        } else {
            atkdown.setVisibility(View.GONE);
            hpdown.setVisibility(View.GONE);
        }


        hpup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hppoint++;
                remainpoints--;
                hpupdata.setText("+" + String.valueOf(hppoint*100));
                remainpoint.setText(String.valueOf(remainpoints));
                if (remainpoints == 0) {
                    hpup.setVisibility(View.GONE);
                    atkup.setVisibility(View.GONE);
                }
                hpdown.setVisibility(View.VISIBLE);
                if (hppoint+atkpoint == 0) {
                    surebutton.setVisibility(View.GONE);
                } else {
                    surebutton.setVisibility(View.VISIBLE);
                }
            }
        });

        hpdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hppoint--;
                remainpoints++;
                hpupdata.setText(hppoint == 0?"":"+" + String.valueOf(hppoint*100));
                remainpoint.setText(String.valueOf(remainpoints));
                if (hppoint == 0) {
                    hpdown.setVisibility(View.GONE);
                }
                hpup.setVisibility(View.VISIBLE);
                atkup.setVisibility(View.VISIBLE);
                if (hppoint+atkpoint == 0) {
                    surebutton.setVisibility(View.GONE);
                } else {
                    surebutton.setVisibility(View.VISIBLE);
                }
            }
        });

        atkup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atkpoint++;
                remainpoints--;
                atkupdata.setText("+" + String.valueOf(atkpoint*10));
                remainpoint.setText(String.valueOf(remainpoints));
                if (remainpoints == 0) {
                    hpup.setVisibility(View.GONE);
                    atkup.setVisibility(View.GONE);
                }
                atkdown.setVisibility(View.VISIBLE);
                if (hppoint+atkpoint == 0) {
                    surebutton.setVisibility(View.GONE);
                } else {
                    surebutton.setVisibility(View.VISIBLE);
                }
            }
        });

        atkdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atkpoint--;
                remainpoints++;
                atkupdata.setText(atkpoint == 0?"":"+" + String.valueOf(atkpoint*100));
                remainpoint.setText(String.valueOf(remainpoints));
                if (atkpoint == 0) {
                    atkdown.setVisibility(View.GONE);
                }
                hpup.setVisibility(View.VISIBLE);
                atkup.setVisibility(View.VISIBLE);
                if (hppoint+atkpoint == 0) {
                    surebutton.setVisibility(View.GONE);
                } else {
                    surebutton.setVisibility(View.VISIBLE);
                }
            }
        });

        surebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remainpoint.setText(String.valueOf(remainpoints));
                user.setHP(user.getHP()+hppoint*100);
                user.UHP = user.UHP + hppoint*100;
                user.setATK(user.getATK()+atkpoint*10);
                user.setAttrpoint(remainpoints);
                hp_data.setText(String.valueOf(user.getHP()));
                atk_data.setText(String.valueOf(user.getATK()));
                hpupdata.setText("");
                atkupdata.setText("");
                hppoint = atkpoint = 0;
                hpdown.setVisibility(View.GONE);
                atkdown.setVisibility(View.GONE);
                if (remainpoints == 0) {
                    surebutton.setVisibility(View.GONE);
                }
            }
        });

        //初始化技能栏
        switch (user.getjob()) {
            case 0:
                basic_attack.setImageResource(R.drawable.wnatklogo);
                spec_cap.setImageResource(R.drawable.blocklogo);
                del_skill1_1.setImageResource(R.drawable.kicklogo);
                del_skill1_2.setImageResource(R.drawable.rushlogo);
                del_skill2_1.setImageResource(R.drawable.shockwavelogo);
                del_skill2_2.setImageResource(R.drawable.shenglonglogo);
                break;
            case 1:
                basic_attack.setImageResource(R.drawable.mnatklogo);
                spec_cap.setImageResource(R.drawable.flashlogo);
                del_skill1_1.setImageResource(R.drawable.baologo);
                del_skill1_2.setImageResource(R.drawable.icelogo);
                del_skill2_1.setImageResource(R.drawable.tornadologo);
                del_skill2_2.setImageResource(R.drawable.firelogo);
                break;
            case 2:
                basic_attack.setImageResource(R.drawable.dnatklogo);
                spec_cap.setImageResource(R.drawable.srecoverlogo);
                del_skill1_1.setImageResource(R.drawable.shieldlogo);
                del_skill1_2.setImageResource(R.drawable.rainbladeslogo);
                del_skill2_1.setImageResource(R.drawable.blindlogo);
                del_skill2_2.setImageResource(R.drawable.curelogo);
                break;
            default:
                basic_attack.setImageResource(R.drawable.wnatklogo);
                spec_cap.setImageResource(R.drawable.blocklogo);
                del_skill1_1.setImageResource(R.drawable.kicklogo);
                del_skill1_2.setImageResource(R.drawable.rushlogo);
                del_skill2_1.setImageResource(R.drawable.shockwavelogo);
                del_skill2_2.setImageResource(R.drawable.shenglonglogo);
        }
        int lv = user.getLv();
        if (lv >= 2) {
            switch (user.getjob()) {
                case 0:
                    switch (user.skill[0]) {
                        case 1:
                            skill1.setImageResource(R.drawable.kicklogo);
                            break;
                        case 2:
                            skill1.setImageResource(R.drawable.rushlogo);
                            break;
                        default:
                            skill1.setImageResource(R.drawable.empty);
                    }
                    break;
                case 1:
                    switch (user.skill[0]) {
                        case 1:
                            skill1.setImageResource(R.drawable.baologo);
                            break;
                        case 2:
                            skill1.setImageResource(R.drawable.icelogo);
                            break;
                        default:
                            skill1.setImageResource(R.drawable.empty);
                    }
                    break;
                case 2:
                    switch (user.skill[0]) {
                        case 1:
                            skill1.setImageResource(R.drawable.shieldlogo);
                            break;
                        case 2:
                            skill1.setImageResource(R.drawable.rainbladeslogo);
                            break;
                        default:
                            skill1.setImageResource(R.drawable.empty);
                    }
                    break;
            }
        }
        if (lv >= 3) {
            switch (user.getjob()) {
                case 0:
                    switch (user.skill[1]) {
                        case 1:
                            skill2.setImageResource(R.drawable.shockwavelogo);
                            break;
                        case 2:
                            skill2.setImageResource(R.drawable.shenglonglogo);
                            break;
                        default:
                            skill2.setImageResource(R.drawable.empty);
                    }
                    break;
                case 1:
                    switch (user.skill[1]) {
                        case 1:
                            skill2.setImageResource(R.drawable.tornadologo);
                            break;
                        case 2:
                            skill2.setImageResource(R.drawable.firelogo);
                            break;
                        default:
                            skill2.setImageResource(R.drawable.empty);
                    }
                    break;
                case 2:
                    switch (user.skill[1]) {
                        case 1:
                            skill2.setImageResource(R.drawable.blindlogo);
                            break;
                        case 2:
                            skill2.setImageResource(R.drawable.curelogo);
                            break;
                        default:
                            skill2.setImageResource(R.drawable.empty);
                    }
                    break;

            }
        }
        lock1.setVisibility(lv>=2?View.GONE:View.VISIBLE);
        lock2.setVisibility(lv>=4?View.GONE:View.VISIBLE);
        lock3.setVisibility(lv>=3?View.GONE:View.VISIBLE);
        lock4.setVisibility(lv>=5?View.GONE:View.VISIBLE);


        //备选技能图标监听
        del_skill1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getLv() >= 2) {
                    Bitmap bitmap=((BitmapDrawable) del_skill1_1.getDrawable()).getBitmap();
                    skill1.setImageBitmap(bitmap);
                    user.skill[0] = 1;
                }
            }
        });

        del_skill1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getLv() >= 4) {
                    Bitmap bitmap=((BitmapDrawable) del_skill1_2.getDrawable()).getBitmap();
                    skill1.setImageBitmap(bitmap);
                    user.skill[0] = 2;
                }
            }
        });

        del_skill2_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getLv() >= 3) {
                    Bitmap bitmap=((BitmapDrawable) del_skill2_1.getDrawable()).getBitmap();
                    skill2.setImageBitmap(bitmap);
                    user.skill[1] = 1;
                }
            }
        });

        del_skill2_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getLv() >= 5) {
                    Bitmap bitmap=((BitmapDrawable) del_skill2_2.getDrawable()).getBitmap();
                    skill2.setImageBitmap(bitmap);
                    user.skill[1] = 2;
                }
            }
        });
    }

    private void setUser() {
        atk_data.setText(String.valueOf(user.getATK()));
        hp_data.setText(String.valueOf(user.getHP()));
        switch (user.getjob()) {
            case 0:
                user_img.setImageResource(R.drawable.warrior);
                break;
            case 1:
                user_img.setImageResource(R.drawable.magician);
                break;
            case 2:
                user_img.setImageResource(R.drawable.doctor);
                break;
            default:
                user_img.setImageResource(R.drawable.warrior);
        }
        lv.setText("Lv."+user.getLv());
        expbar.setMaxCount(user.getNextlvexp());
        expbar.setCurrentCount(user.getExp());
    }

    private void redrawbag() {
        int i;
        for (i = 0 ; i != bag.size(); ++i) {
            blocks[i].setImageBitmap(bag.get(i).logo);
        }
        for (;i < 16 ; ++i) {
            blocks[i].setImageBitmap(null);
        }
    }
}
