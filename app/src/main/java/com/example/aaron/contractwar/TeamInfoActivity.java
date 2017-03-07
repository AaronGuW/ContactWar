package com.example.aaron.contractwar;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Aaron on 2015/5/27.
 */
public class TeamInfoActivity extends Activity {

    private Button holdcomp, tocontact,ok,cancel,chooseok;
    private LayoutInflater inflater;
    private View back;
    private Spinner typeS,inviteS,dayS,hourS;
    private ListView contactlist;
    private ArrayList<String> contactnames;
    private HashMap<String, bas_role> hashMap;
    private ContactWar myApp;
    private Bitmap defaultphoto;
    private ArrayList<String> targets = new ArrayList<>();
    private LinearLayout listtab,shadow;
    private RelativeLayout optiontab;
    private AlphaAnimation options_in,options_out,list_in,list_out;
    private ScaleAnimation options_small, options_large, list_small, list_large;
    private AnimationSet options_in_set,options_out_set,list_out_set, list_in_set;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_info);
        defaultphoto = BitmapFactory.decodeResource(getResources(),R.drawable.contact_photo);
        myApp = (ContactWar)getApplication();
        contactnames = myApp.getContactsname();
        hashMap = myApp.getMemhashmap();
        holdcomp = (Button)findViewById(R.id.hold);
        tocontact = (Button)findViewById(R.id.invite);
        back = findViewById(R.id.background);
        inflater = LayoutInflater.from(this);
        final View options = inflater.inflate(R.layout.cptmenu,null);
        final PopupWindow pop = new PopupWindow(options, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        pop.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectanglealpha));
        typeS = (Spinner)(options.findViewById(R.id.typeselector));
        inviteS = (Spinner)(options.findViewById(R.id.inviteselector));
        dayS = (Spinner)(options.findViewById(R.id.day));
        hourS = (Spinner)(options.findViewById(R.id.hour));
        contactlist = (ListView)(findViewById(R.id.contactlist));
        ok = (Button)(options.findViewById(R.id.ok));
        cancel = (Button)(options.findViewById(R.id.cancel));
        chooseok = (Button)(findViewById(R.id.chooseok));
        optiontab = (RelativeLayout)findViewById(R.id.optionstab);
        listtab = (LinearLayout)findViewById(R.id.contactlisttab);
        shadow = (LinearLayout)findViewById(R.id.shadow);
        inviteS = (Spinner)(findViewById(R.id.inviteselector));
        init_animation();

        BaseAdapter ba = new BaseAdapter() {
            @Override
            public int getCount() {
                return contactnames.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                String cname = contactnames.get(position);
                View view;
                if (convertView != null) {
                    view = convertView;
                } else {
                    view = inflater.inflate(R.layout.contactpickeritem,null);
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
                TextView job = (TextView)view.findViewById(R.id.contact_job);
                int cjob = hashMap.get(cname).getjob();
                if (cjob == 0)
                    job.setText("职业： 战士");
                else if (cjob == 1)
                    job.setText("职业： 法师");
                else
                    job.setText("职业： 圣骑士");
                return view;
            }
        };
        contactlist.setAdapter(ba);
        contactlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox selected = (CheckBox)view.findViewById(R.id.selected);
                if (selected.isSelected()) {
                    selected.setSelected(false);
                    targets.remove(hashMap.get(contactnames.get(position)).getPhone_number());
                } else {
                    selected.setSelected(true);
                    targets.add(hashMap.get(contactnames.get(position)).getPhone_number());
                }
            }
        });

        holdcomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*pop.setFocusable(true);
                pop.showAtLocation(back, Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);*/
                ObjectAnimator.ofFloat(optiontab,"translationY",ContactWar.dp2pix(45-400)).setDuration(800).start();
                ObjectAnimator.ofFloat(shadow,"alpha",0.0f,0.9f).setDuration(800).start();
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pop.isShowing()) {
                            pop.dismiss();
                        }
                    }
                });
                chooseok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //ObjectAnimator.ofFloat(options,"translationY",0-ContactWar.dp2pix(270)).setDuration(200).start();
                        //listtab.startAnimation(list_out_set);
                        Log.i("chooseok","clicked!");
                        ObjectAnimator.ofFloat(optiontab,"translationX",0).setDuration(800).start();
                        PropertyValuesHolder pvha = PropertyValuesHolder.ofFloat("alpha",1f,0f);
                        PropertyValuesHolder pvhx = PropertyValuesHolder.ofFloat("translationX",0,ContactWar.dp2pix(-45));
                        ObjectAnimator oa = ObjectAnimator.ofPropertyValuesHolder(listtab, pvha, pvhx);
                        oa.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                listtab.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        oa.setDuration(800);
                        oa.start();
                    }
                });
            }
        });
        tocontact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeamInfoActivity.this, ContactListActivity.class));
                TeamInfoActivity.this.finish();
            }
        });

        inviteS.setSelection(0,true);
        inviteS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    //ObjectAnimator.ofFloat(options,"translationY",ContactWar.dp2pix(270)).setDuration(2000).start();
                    //optiontab.startAnimation(options_out_set);
                    ObjectAnimator.ofFloat(optiontab,"translationX",ContactWar.dp2pix(-130)).setDuration(800).start();
                    PropertyValuesHolder pvha = PropertyValuesHolder.ofFloat("alpha",0f,1f);
                    PropertyValuesHolder pvhx = PropertyValuesHolder.ofFloat("translationX",0,ContactWar.dp2pix(130));
                    listtab.setVisibility(View.VISIBLE);
                    ObjectAnimator.ofPropertyValuesHolder(listtab,pvha,pvhx).setDuration(800).start();
                    //ObjectAnimator.ofFloat(listtab,"translationX",ContactWar.dp2pix(305-630)).setDuration(800).start();
                    Log.i("option","2");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void init_animation() {
        options_out = new AlphaAnimation(1.0f,0.0f);
        options_out.setDuration(1000);
        options_out.setFillAfter(true);
        options_small = new ScaleAnimation(1.0f,0.0f,1.0f,0.0f, Animation.RELATIVE_TO_PARENT,0.5f,Animation.RELATIVE_TO_PARENT,0.5f);
        options_small.setDuration(1000);
        options_small.setFillAfter(true);
        options_in = new AlphaAnimation(0.0f,1.0f);
        options_in.setFillAfter(true);
        options_in.setDuration(1000);
        options_large = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f, Animation.RELATIVE_TO_PARENT,0.5f,Animation.RELATIVE_TO_PARENT,0.5f);
        options_large.setDuration(1000);
        options_large.setFillAfter(true);
        list_in = new AlphaAnimation(0.0f,1.0f);
        list_in.setDuration(1000);
        list_in.setFillAfter(true);
        list_large = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT,0.5f);
        list_large.setDuration(1000);
        list_large.setFillAfter(true);
        list_out = new AlphaAnimation(1.0f,0.0f);
        list_out.setDuration(1000);
        list_out.setFillAfter(true);
        list_small = new ScaleAnimation(1.0f,0.0f,1.0f,0.0f, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT,0.5f);
        list_small.setDuration(1000);
        list_small.setFillAfter(true);
        options_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                optiontab.setFocusable(false);
                optiontab.setVisibility(View.GONE);
                listtab.setVisibility(View.VISIBLE);
                listtab.setFocusable(true);
                listtab.startAnimation(list_in_set);
                Log.i("aend","called");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        list_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                listtab.setFocusable(false);
                listtab.setVisibility(View.GONE);
                optiontab.setVisibility(View.VISIBLE);
                optiontab.setFocusable(true);
                optiontab.startAnimation(options_in_set);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        options_out_set = new AnimationSet(true);
        options_out_set.addAnimation(options_out);
        options_out_set.addAnimation(options_small);
        options_out_set.setFillAfter(true);
        options_out_set.setDuration(1000);
        options_in_set = new AnimationSet(true);
        options_in_set.addAnimation(options_in);
        options_in_set.addAnimation(options_large);
        options_in_set.setFillAfter(true);
        options_in_set.setDuration(1000);
        list_in_set = new AnimationSet(true);
        list_in_set.addAnimation(list_in);
        list_in_set.addAnimation(list_large);
        list_in_set.setDuration(1000);
        list_in_set.setFillAfter(true);
        list_out_set = new AnimationSet(true);
        list_out_set.addAnimation(list_out);
        list_out_set.addAnimation(list_small);
        list_out_set.setDuration(1000);
        list_out_set.setFillAfter(true);
    }
}
