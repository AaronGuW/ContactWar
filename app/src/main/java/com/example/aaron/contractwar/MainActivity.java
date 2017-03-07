package com.example.aaron.contractwar;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.UserDictionary;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {

    Context mContext = null;
    private ContactWar myApp;                                      /**ContactWar类用于Activity间的数据共享**/
    private ViewPager mPager;                                      /**首页的排行榜view **/
    private ArrayList<View> listViews;
    private TextView t1,t2;
    private ImageView cursor;
    private Button To_contact, To_team;
    private int currIndex = 0;
    private int bmpW;
    private int offset;
    private ListView perrank,teamrank;
    private ArrayList<bas_role> mContactper,mContactteam;
    private ArrayList<task> taskArrayList;
    private ImageView logo[], rewardlogo[];
    private TextView description[], expdata[];
    private Button getreward[];
    private View.OnClickListener getrewardlistener;
    private RoundProgressBar progressbar[];
    private bas_role user;
    private Bitmap defaultphoto;
    private Intent sintent;

    private int lastY;
    private LinearLayout tasklist;
    private ImageView circle;
    private boolean down = false;
    private String today;
    static private ImageView reddot;

    static public ArrayList<String> requests = new ArrayList<>();
    static private String LatestRequestDate = new String();
    static String LatestHandledRequestDate = new String("0");

    static public Handler reqhandler = new Handler() {
        public void handleMessage(Message msg) {
            String res = (String)msg.obj;
            String[] req = res.split("&");
            if (req.length != 0) {
                //Log.i("req[0]",req[0]);
                //Log.i("*location",String.valueOf(req[0].indexOf("*")));
                if (req[0].indexOf("*") != -1) {
                    if (req[0].substring(2, req[0].indexOf("*")).compareTo(LatestRequestDate) > 0) {
                        LatestRequestDate = req[0].substring(2, req[0].indexOf("*"));
                        requests.clear();
                        for (int i = 0; i != req.length; ++i) {
                            if (req[i].length() != 0) {
                                requests.add(new String("0" + req[i]));
                            }
                        }
                        Message message = new Message();
                        message.what = 1;
                        message.obj = "1";
                        reddot.setVisibility(View.VISIBLE);
                    }
                }
            }
            super.handleMessage(msg);
        }
    };

    private Handler wwhandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent();
            String res = msg.obj.toString();
            if (res.substring(0,1).compareTo("2") > 0) {
                intent.setClass(MainActivity.this, WorldwarCmptActivity.class);
            } else {
                intent.setClass(MainActivity.this, WorldWarActivity.class);
            }
            intent.putExtra("stage",res.substring(0,1));
            intent.putExtra("signed",res.substring(1,2));
            intent.putExtra("teamcnt",res.substring(2,res.indexOf("+")));
            intent.putExtra("countdown",res.substring(res.indexOf("+")+1,res.indexOf("*")));
            intent.putExtra("start",res.substring(res.indexOf("*")+1,res.length()));
            startActivity(intent);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        defaultphoto = BitmapFactory.decodeResource(getResources(), R.drawable.contact_photo);

        reddot = (ImageView)findViewById(R.id.red_dot);

        final Request.reqreceiver reqrec = new Request.reqreceiver();
        sintent = new Intent(this,reqrec.getClass());
        startService(sintent);

        Button worldwar = (Button)findViewById(R.id.warbtn);
        worldwar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                HttpUtil.HttpClientGET("http://10.0.2.2:8008/w?username=" + ContactWar.getUserID() + "&mode=get",wwhandler);
            }
        });

        Button note = (Button)findViewById(R.id.notebtn);
        note.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((ImageView)findViewById(R.id.red_dot)).setVisibility(View.GONE);
                startActivity(new Intent(MainActivity.this,RequestActivity.class));
            }
        });

        Button jump = (Button)findViewById(R.id.jump);
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,character.class);
                startActivity(intent);
            }
        });

        myApp = (ContactWar)getApplication();
        user = myApp.getUser();
        taskArrayList = myApp.getTasklist();

        //初始化每日任务下拉窗
        tasklist = (LinearLayout)findViewById(R.id.task_list);
        circle = (ImageView)findViewById(R.id.circle);
        circle.setClickable(true);

        progressbar = new RoundProgressBar[3];
        progressbar[0] = (RoundProgressBar) findViewById(R.id.roundProgressBar1);
        progressbar[1] = (RoundProgressBar) findViewById(R.id.roundProgressBar2);
        progressbar[2] = (RoundProgressBar) findViewById(R.id.roundProgressBar3);

        description = new TextView[3];
        description[0] = (TextView) findViewById(R.id.task1description);
        description[1] = (TextView) findViewById(R.id.task2description);
        description[2] = (TextView) findViewById(R.id.task3description);

        logo = new ImageView[3];
        logo[0] = (ImageView) findViewById(R.id.task1logo);
        logo[1] = (ImageView) findViewById(R.id.task2logo);
        logo[2] = (ImageView) findViewById(R.id.task3logo);

        getreward = new Button[3];
        getreward[0] = (Button) findViewById(R.id.task1rewardget);
        getreward[1] = (Button) findViewById(R.id.task2rewardget);
        getreward[2] = (Button) findViewById(R.id.task3rewardget);

        rewardlogo = new ImageView[3];
        rewardlogo[0] = (ImageView) findViewById(R.id.task1rewardlogo);
        rewardlogo[1] = (ImageView) findViewById(R.id.task2rewardlogo);
        rewardlogo[2] = (ImageView) findViewById(R.id.task3rewardlogo);

        expdata = new TextView[3];
        expdata[0] = (TextView) findViewById(R.id.task1expdata);
        expdata[1] = (TextView) findViewById(R.id.task2expdata);
        expdata[2] = (TextView) findViewById(R.id.task3expdata);

        getrewardlistener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                for (int i = 0 ; i != 3 ; ++i) {
                    if (v == getreward[i]) {
                        if (taskArrayList.get(i).reward != 1) {
                            taskArrayList.get(i).setRewarded();
                            getreward[i].setEnabled(false);
                            getreward[i].setText("已领取");
                            user.addexp(taskArrayList.get(i).reward);
                        } else {
                            if (myApp.addtobag(taskArrayList.get(i).getRewarditem())) {
                                taskArrayList.get(i).setRewarded();
                                getreward[i].setEnabled(false);
                                getreward[i].setText("已领取");
                            } else {
                                Toast mtoast = new Toast(MainActivity.this);
                                mtoast.makeText(MainActivity.this,"您的包裹满了，请整理后再行领取~",Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    }
                }
            }
        };
        for (int i = 0 ; i != 3 ; ++i) {
            getreward[i].setOnClickListener(getrewardlistener);
        }

        getSysDate();
        loaddailytask();

        /**以下都是初始化排行榜**/
        initperrank();
        initteamrank();
        InitImageView();
        InitTextView();
        init_pager();

        Button pve = (Button)findViewById(R.id.pve);
        pve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,monsterlistactivity.class);
                startActivity(intent);
           }
        });

        /**设置my_contact按钮点击后页面跳转监听**/
        To_contact = (Button)findViewById(R.id.my_contact);
        To_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ContactListActivity.class);
                startActivity(intent);
            }
        });

        To_team = (Button)findViewById(R.id.my_team);
        To_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (myApp.getUser().in_team) {
                    intent.setClass(MainActivity.this, MyteamActivity.class);
                }
                else
                    intent.setClass(MainActivity.this, TeamInfoActivity.class);
                startActivity(intent);
            }
        });

        circle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v == circle) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            lastY = (int) event.getRawY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            int dy = (int) event.getRawY() - lastY;
                            down = dy > 0;
                            int top = tasklist.getTop() + dy;
                            int left = tasklist.getLeft();
                            if (top <= 0 - tasklist.getHeight()) {
                                top = 0 - tasklist.getHeight();
                            }
                            if (top >= 0) {
                                top = 0;
                            }
                            tasklist.layout(left, top, left + tasklist.getWidth(), top + tasklist.getHeight());
                            lastY = (int) event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:
                            int height = tasklist.getHeight();
                            if (down) {
                                ObjectAnimator.ofFloat(tasklist, "translationY", 0 - tasklist.getTop()).setDuration(200).start();
                            } else {
                                ObjectAnimator.ofFloat(tasklist, "translationY", -(height - circle.getHeight()) - tasklist.getTop()).setDuration(200).start();
                            }
                            ArrayList<task> tasklist = myApp.getTasklist();
                            for (int i = 0; i != tasklist.size(); ++i) {
                                tasklist.get(i).synchronize(mContext, myApp, progressbar[i],getreward[i]);
                            }
                            break;
                    }
                }
                return false;
            }
        });
    }



    /**
     * ViewPager适配器
     */
    public class MyPagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

    private void init_pager()
    {
        mPager = (ViewPager) findViewById(R.id.per_team_rank);
        listViews = new ArrayList<>();
        listViews.add(perrank);
        listViews.add(teamrank);
        mPager.setAdapter(new MyPagerAdapter(listViews));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    private void InitTextView() {
        t1 = (TextView) findViewById(R.id.per_rank);
        t2 = (TextView) findViewById(R.id.team_rank);

        t1.setOnClickListener(new title_onClickListener(0));
        t2.setOnClickListener(new title_onClickListener(1));
    }

    private void InitImageView() {
        cursor = (ImageView) findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.cursor)
                .getWidth();// 获取图片宽度
        RelativeLayout wrap = (RelativeLayout)findViewById(R.id.wrap);
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        wrap.measure(w,h);
        int screenW = wrap.getMeasuredWidth();
        offset = (screenW / 2 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    //初始化个人排行榜
    private void initperrank(){
        final LayoutInflater inflater = LayoutInflater.from(this);
        perrank = new ListView(MainActivity.this);
        mContactper = myApp.getmContact();
        bas_role[] mc = mContactper.toArray(new bas_role[mContactper.size()]);
        MyComparator cmp = new MyComparator();
        Arrays.sort(mc, cmp);
        mContactper = new ArrayList<>(Arrays.asList(mc));
        BaseAdapter pr = new BaseAdapter() {
            @Override
            public int getCount() {
                return mContactper.size();
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
                View view;
                if (convertView != null) {
                    view = convertView;
                } else {
                    view = inflater.inflate(R.layout.rankitem,null);
                }
                ImageView img = (ImageView)view.findViewById(R.id.contact_photo);
                if (mContactper.get(position).getPhoto() != null)
                    img.setImageBitmap(mContactper.get(position).getPhoto());
                else
                    img.setImageBitmap(defaultphoto);
                TextView name = (TextView)view.findViewById(R.id.contact_name);
                name.setText(mContactper.get(position).getId());
                TextView level = (TextView)view.findViewById(R.id.contact_level);
                level.setText("等级： "+mContactper.get(position).getLv());
                TextView score = (TextView)view.findViewById(R.id.contact_score);
                score.setText("积分： "+mContactper.get(position).getScore());
                TextView rank = (TextView)view.findViewById(R.id.rank);
                rank.setText(String.valueOf(position+1));
                switch (position) {
                    case 0:
                        rank.setBackground(getResources().getDrawable(R.drawable.goldmedal));
                        break;
                    case 1:
                        rank.setBackground(getResources().getDrawable(R.drawable.silvermedal));
                        break;
                    case 2:
                        rank.setBackground(getResources().getDrawable(R.drawable.bronzemedal));
                        break;
                    default:
                        rank.setBackground(getResources().getDrawable(R.drawable.normalmedal));
                }
                return view;
            }
        };
        perrank.setAdapter(pr);
    }

    private void initteamrank(){
        final LayoutInflater inflater = LayoutInflater.from(this);
        teamrank = new ListView(MainActivity.this);
        mContactteam = myApp.getmContact();
        bas_role[] mc = mContactteam.toArray(new bas_role[mContactteam.size()]);
        MyComparator_team cmp = new MyComparator_team();
        Arrays.sort(mc,cmp);
        mContactteam = new ArrayList<>(Arrays.asList(mc));
        BaseAdapter tr = new BaseAdapter() {
            @Override
            public int getCount() {
                return mContactteam.size();
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
                View view;
                if (convertView != null) {
                    view = convertView;
                } else {
                    view = inflater.inflate(R.layout.rankitem,null);
                }
                ImageView img = (ImageView)view.findViewById(R.id.contact_photo);
                if (mContactteam.get(position).getPhoto() != null)
                    img.setImageBitmap(mContactteam.get(position).getPhoto());
                else
                    img.setImageBitmap(defaultphoto);
                TextView name = (TextView)view.findViewById(R.id.contact_name);
                name.setText(mContactteam.get(position).getId());
                TextView level = (TextView)view.findViewById(R.id.contact_level);
                level.setText("等级： "+mContactteam.get(position).getLv());
                TextView score = (TextView)view.findViewById(R.id.contact_score);
                score.setText("积分： "+mContactteam.get(position).getTeam_score());
                TextView rank = (TextView)view.findViewById(R.id.rank);
                rank.setText(String.valueOf(position+1));
                switch (position) {
                    case 0:
                        rank.setBackground(getResources().getDrawable(R.drawable.goldmedal));
                        break;
                    case 1:
                        rank.setBackground(getResources().getDrawable(R.drawable.silvermedal));
                        break;
                    case 2:
                        rank.setBackground(getResources().getDrawable(R.drawable.bronzemedal));
                        break;
                    default:
                        rank.setBackground(getResources().getDrawable(R.drawable.normalmedal));
                }
                return view;
            }
        };
        teamrank.setAdapter(tr);
    }

    public class title_onClickListener implements View.OnClickListener {
        private int index = 0;

        public title_onClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset * 2 + bmpW;

        @Override
        public void onPageSelected(int arg0) {
            Animation animation;
            animation = new TranslateAnimation(one*currIndex, one*arg0, 0, 0);
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    public class MyComparator implements Comparator {
        @Override
        public int compare(Object lhs, Object rhs) {
            bas_role r1 = (bas_role)lhs;
            bas_role r2 = (bas_role)rhs;
            if (r1.getScore()>r2.getScore()){
                return -1;
            }
            else if (r1.getScore() < r2.getScore()){
                return 1;
            }
            else{
                return r1.getId().compareTo(r2.getId());
            }
        }
    }

    public class MyComparator_team implements Comparator {
        @Override
        public  int compare(Object lhs, Object rhs) {
            bas_role r1 = (bas_role)lhs;
            bas_role r2 = (bas_role)rhs;
            if (r1.getTeam_score()>r2.getTeam_score()){
                return -1;
            }
            else if (r1.getTeam_score()<r2.getTeam_score()){
                return 1;
            }
            else
                return r1.getId().compareTo(r2.getId());
        }
    }

    private void getSysDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        today = formatter.format(curDate);
        Log.i("Date",today);
    }

    private void loaddailytask(){
        ArrayList<task> tasklist = myApp.getTasklist();
        ArrayList<Integer> typeset = new ArrayList<>();
        if (tasklist.size() == 0) {
            Random random = new Random();
            int ttype,otype,amount;
            for (int i = 0; i != 3 ; ++i) {
                while (true) {
                    ttype = Math.abs(random.nextInt())%3;
                    otype = Math.abs(random.nextInt())%3;
                    if (ttype < 2 && !typeset.contains(ttype)) {
                        typeset.add(ttype);
                        break;
                    } else if (ttype == 2 && !typeset.contains(ttype+otype+2)) {
                        typeset.add(ttype+otype+2);
                        break;
                    }
                }
                if (ttype < 2) {
                    amount = Math.abs(random.nextInt())%5;
                    amount = amount < 3? 3:4;
                } else {
                    amount = Math.abs(random.nextInt())%3 + 6;
                }
                int reward = Math.abs(random.nextInt())%100;
                item rewarditem;
                reward = reward < 40?0:1;
                if (reward == 1) {
                    rewarditem = new potion("10001",getResources());
                } else {
                    rewarditem = null;
                    reward = amount*10;
                }
                switch (ttype) {
                    case 0:
                        tasklist.add(new messagetask(amount,0,today,otype,reward,rewarditem));
                        break;
                    case 1:
                        tasklist.add(new phonecalltask(amount,0,today,otype,reward,rewarditem));
                        break;
                    case 2:
                        tasklist.add(new challengetask(amount,0,today,otype,reward,rewarditem));
                        break;
                    default:
                        tasklist.add(new challengetask(amount,0,today,otype,reward,rewarditem));
                }
            }
        }
        for (int i = 0 ; i != 3 ; ++i) {
            tasklist.get(i).draw(logo[i],description[i],progressbar[i],expdata[i],rewardlogo[i],getreward[i]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
