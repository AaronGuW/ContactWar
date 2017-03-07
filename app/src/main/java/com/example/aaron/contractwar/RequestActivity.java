package com.example.aaron.contractwar;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Aaron on 2015/8/18.
 */
public class RequestActivity extends Activity {
    private ListView reqlist;
    private ArrayList<String> dylist,originlist = new ArrayList<>();
    private ContactWar myApp;
    private Resources resources;
    private bas_role candidate = new bas_role();
    private BaseAdapter ba;
    private int req2del;
    static private final String FT_SUCCEED = "1", FT_ERROR = "0", T_NUM_FULLUP = "2", FT_REFUSE_SUCCEED = "3", ALREADY_IN_TEAM = "4", REPEATED_INVITATION = "5";
    public Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String res = msg.obj.toString().substring(0,1), rawres = msg.obj.toString();
            if (originlist.get(req2del).substring(1,2).compareTo("1") == 0) {
                if (res.equals(FT_SUCCEED)) {
                    Toast.makeText(RequestActivity.this, "组队成功", Toast.LENGTH_SHORT).show();
                    myApp.getUser().in_team = true;
                    myApp.getUser().team_number.add(candidate);
                    if (rawres.length() > 1) {
                        myApp.getUser().team_number.add(myApp.getMemhashmap().get(ContactWar.getnamebynumber(rawres.substring(1))));
                    }
                    MainActivity.requests.remove(req2del);
                    originlist.remove(req2del);
                    ba.notifyDataSetChanged();
                } else if (res.equals(FT_ERROR)) {
                    Toast.makeText(RequestActivity.this, "组队出错", Toast.LENGTH_SHORT).show();
                } else if (res.equals(T_NUM_FULLUP)) {
                    Toast.makeText(RequestActivity.this, "对方队伍人员已满", Toast.LENGTH_SHORT).show();
                    MainActivity.requests.remove(req2del);
                    originlist.remove(req2del);
                    ba.notifyDataSetChanged();
                } else if (res.equals(FT_REFUSE_SUCCEED)) {
                    Toast.makeText(RequestActivity.this, "已拒绝对方的组队邀请", Toast.LENGTH_SHORT).show();
                    MainActivity.requests.remove(req2del);
                    originlist.remove(req2del);
                    ba.notifyDataSetChanged();
                } else if (res.equals(ALREADY_IN_TEAM)) {
                    Toast.makeText(RequestActivity.this, "您已有队伍" , Toast.LENGTH_SHORT).show();
                    MainActivity.requests.remove(req2del);
                    originlist.remove(req2del);
                    ba.notifyDataSetChanged();
                } else if (res.equals(REPEATED_INVITATION)) {
                    Toast.makeText(RequestActivity.this, "来自队员的邀请" , Toast.LENGTH_SHORT).show();
                    MainActivity.requests.remove(req2del);
                    originlist.remove(req2del);
                    ba.notifyDataSetChanged();
                }
            } else if (originlist.get(req2del).substring(1,2).compareTo("3") == 0) {
                if (res.equals("1")) {
                    String req = new String(originlist.get(req2del));
                    if (req.substring(req.length() - 5, req.length() - 4).compareTo("1") == 0) {
                        myApp.addtobag(new potion(req.substring(req.length() - 5), resources));
                    } else {
                        myApp.addtobag(new equipment(req.substring(req.length() - 5), resources));
                    }
                    MainActivity.requests.remove(req2del);
                    originlist.remove(req2del);
                    ba.notifyDataSetChanged();
                    Toast.makeText(RequestActivity.this,"成功接受物品",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RequestActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
                }
            } else {
                if (res.equals("1")) {
                     MainActivity.requests.remove(req2del);
                    originlist.remove(req2del);
                    ba.notifyDataSetChanged();
                } else {
                    Toast.makeText(RequestActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request);
        reqlist = (ListView)findViewById(R.id.reqlist);
        ((Button)findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestActivity.this.finish();
            }
        });
        originlist.addAll(MainActivity.requests);
        myApp = (ContactWar)getApplication();
        resources = getResources();
        ini_list();
    }

    private void ini_list() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        ba = new BaseAdapter() {
            @Override
            public int getCount() {
                return originlist.size();
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
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = inflater.inflate(R.layout.reqitem,null);
                ((ImageView)view.findViewById(R.id.contact_photo)).setImageBitmap(Request.getphoto(originlist.get(position),getResources()));
                ((TextView)view.findViewById(R.id.reqtype)).setText(Request.getRequestType(originlist.get(position)));
                ((TextView)view.findViewById(R.id.reqcontent)).setText(Request.getRequestContent(originlist.get(position)));
                final int index = originlist.get(position).indexOf("*");
                String _date = originlist.get(position).substring(2, index);
                Date date = new Date(Long.valueOf(_date)*1000);
                Date current = new Date();
                long dif = current.getTime() - date.getTime();
                dif /= 1000;
                String time;
                if (dif <= 5) {
                    time = "刚刚";
                } else if (dif < 60) {
                    time = String.valueOf(dif)+"秒前";
                } else if (dif < 3600) {
                    time = String.valueOf(dif/60)+"分钟前";
                } else if (dif < 86400) {
                    time = String.valueOf(dif/3600)+"小时前";
                } else {
                    time = String.valueOf(dif/86400)+"天前";
                }
                ((TextView)view.findViewById(R.id.time)).setText(time);

                ((Button)view.findViewById(R.id.ac)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            req2del = position;
                            candidate = myApp.getMemhashmap().get(ContactWar.getnamebynumber(originlist.get(position).substring(index+1,index+12)));
                            if (originlist.get(position).substring(1, 2).compareTo("1") == 0) {
                                HttpUtil.HttpClientGET("http://10.0.2.2:8008/request?username=" + ContactWar.getUserID() + "&mode=accept&req=" + originlist.get(position).substring(1), mhandler);
                            } else if (originlist.get(position).substring(1, 2).compareTo("2") == 0) {
                                HttpUtil.HttpClientGET("http://10.0.2.2:8008/request?username=" + ContactWar.getUserID() + "&mode=OK&req=" + originlist.get(position).substring(1), mhandler);
                            } else if (originlist.get(position).substring(1, 2).compareTo("3") == 0) {
                                HttpUtil.HttpClientGET("http://10.0.2.2:8008/request?username=" + ContactWar.getUserID() + "&mode=OK&req=" + originlist.get(position).substring(1), mhandler);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                ((Button)view.findViewById(R.id.delreq)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            req2del = position;
                            String res = HttpUtil.HttpClientGET("http://10.0.2.2:8008/request?username=" + ContactWar.getUserID() + "&mode=refuse&req="+originlist.get(position).substring(1), mhandler);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                return view;
            }
        };
        reqlist.setAdapter(ba);
        reqlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout options = (LinearLayout)view.findViewById(R.id.options);
                if (originlist.get(position).substring(0,1).compareTo("0") == 0) {
                    if (originlist.get(position).substring(1,2).compareTo("1") == 0) {
                        ObjectAnimator.ofFloat(options,"translationX",0 - ContactWar.dp2pix(85)).setDuration(200).start();
                        Log.i("options","moved_left");
                    } else {
                        ObjectAnimator.ofFloat(options,"translationX",0 - ContactWar.dp2pix(45)).setDuration(200).start();
                    }
                    Log.i("req",originlist.get(position));
                    originlist.set(position,originlist.get(position).replaceFirst("0","1"));
                    Log.i("_req", originlist.get(position));
                } else {
                    if (originlist.get(position).substring(1,2).compareTo("1") == 0) {
                        ObjectAnimator.ofFloat(options, "translationX", ContactWar.dp2pix(85)).setDuration(200).start();
                        Log.i("options","move_right");
                    } else {
                        ObjectAnimator.ofFloat(options, "translationX", ContactWar.dp2pix(45)).setDuration(200).start();
                    }
                    originlist.set(position,originlist.get(position).replaceFirst("1","0"));
                }
                Log.i("note","clicked");
            }
        });

    }
}
