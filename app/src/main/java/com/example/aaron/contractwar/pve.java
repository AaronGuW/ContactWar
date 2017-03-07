package com.example.aaron.contractwar;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Aaron on 2015/6/16.
 */
public class pve extends Activity {

    private tornado tornadearray[];
    private shockwave shockwavearray[];
    private ArrayList< ArrayList<bullet> > bullets;
    private ArrayList<bossbullet> bossbullets;
    private Resources res;
    private int sw, sh;
    private int HP[];
    private int BHP;
    private ArrayList<thunder> thunders;
    private int lastleftcnt = -1, lastrightcnt = -1;

    static private Bitmap p1skill[];
    static private Bitmap cdcover,background, reportbg;
    static private Bitmap left,right,dleft,dright,host;
    static private Bitmap poison[];
    static private Bitmap hpbar,rehpbar;
    static private Bitmap potionblock;

    private boolean isleftpressed = false, isrightpressed = false, ishosted = false;
    private boolean gameover = false, win = false, added = false;
    private int cd1[];
    private int nextround[];
    private static String IDlist[] = new String[]{"22101","23001"};
    private static int warrioratkrange[] = new int[]{80,40,280};
    private static int magicianatkrange[] = new int[]{0,300,400};
    private static int doctoratkrange[] = new int[]{100,0,120};
    private int safedis[] = new int[]{40,200,80};
    private int chance[] = new int[]{50,50,50};

    private ArrayList<bas_role> roles;
    private ContactWar myApp;
    private monster boss;
    private int posioncnt = 0, thundercnt = 0;
    private boolean posionactivated = false, thunderactivated = false;
    private Bitmap box, shine[];
    private int shinecnt = 0, dropspeed = -30, bx,by;
    private boolean land = false,boxget = false;

    private bas_role user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApp = (ContactWar)getApplication();
        roles = new ArrayList<>();
        res = myApp.getResources();
        sw = myApp.getScreenwidth();
        sh = myApp.getScreenheight();

        user = myApp.getUser();
        roles.add(user);
        if (user.in_team) {
            for (int i = 0 ; i != user.team_number.size() ; ++i) {
                roles.add(user.team_number.get(i));
            }
        }
        HP = new int[roles.size()];
        nextround = new int[roles.size()];
        tornadearray = new tornado[roles.size()];
        shockwavearray = new shockwave[roles.size()];
        bullets = new ArrayList<>();
        bossbullets = new ArrayList<>();
        ArrayList<bullet> b1 = new ArrayList<>();
        ArrayList<bullet> b2 = new ArrayList<>();
        ArrayList<bullet> b3 = new ArrayList<>();
        bullets.add(b1);
        bullets.add(b2);
        bullets.add(b3);
        for (int i = 0 ; i < roles.size() ; ++i) {
            HP[i] = roles.get(i).getHP();
        }

        for (int i = 1 ; i < roles.size() ; ++i) {
            int level = myApp.getIntimacyLevel(roles.get(i).getId());
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
            roles.get(0).HP += (int)((double)roles.get(0).UHP*calibration);
            HP[0] += (int)((double)roles.get(0).UHP*calibration);
            roles.get(i).HP += (int)((double)roles.get(i).UHP*calibration);
            HP[i] += (int)((double)roles.get(i).UHP*calibration);
        }

        switch (getIntent().getStringExtra("monster")) {
            case "1":
                boss = new nail(200*roles.size(),20000*roles.size());
                boss.X = sw - 250;
                thunders = new ArrayList<>();
                BHP = 20000*roles.size();
                break;
            case "2":
                boss = new rock();
                BHP = 50000;
                break;
            default:
                boss = new rock();
        }

        init_status();
        setContentView(new FightView(this));
    }


    class FightView extends SurfaceView implements SurfaceHolder.Callback {

        private SurfaceHolder holder;
        private FightThread fight;

        public FightView(Context context) {
            super(context);
            holder = this.getHolder();
            holder.addCallback(this);
            fight = new FightThread(holder);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            fight.isrun = true;
            fight.start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            fight.isrun = false;
            reset_status();
        }
    }

    class FightThread extends Thread {
        public boolean isrun;
        private SurfaceHolder holder;
        private Paint resp, gainp, rewardp;

        public FightThread(SurfaceHolder holder) {
            this.holder = holder;
            isrun = true;
        }

        @Override
        public void run()
        {
            resp = new Paint();
            gainp = new Paint();
            rewardp = new Paint();
            resp.setTextAlign(Paint.Align.CENTER);
            resp.setTextSize(80);
            resp.setAntiAlias(true);//去除锯齿
            resp.setFilterBitmap(true);//对位图进行滤波处理
            gainp.setTextAlign(Paint.Align.LEFT);
            gainp.setTextSize(40);
            gainp.setAntiAlias(true);//去除锯齿
            gainp.setFilterBitmap(true);//对位图进行滤波处理
            rewardp.setTextAlign(Paint.Align.CENTER);
            rewardp.setTextSize(40);
            rewardp.setColor(Color.RED);
            rewardp.setAntiAlias(true);
            rewardp.setFilterBitmap(true);

            while(isrun)
            {
                Canvas c = null;
                try
                {
                    synchronized (holder){
                        c = holder.lockCanvas();
                        Paint p = new Paint(); //创建画笔
                        p.setColor(Color.BLACK);
                        p.setAntiAlias(true);//去除锯齿
                        p.setFilterBitmap(true);//对位图进行滤波处理
                        p.setTextSize(40);
                        p.setTextAlign(Paint.Align.CENTER);

                        c.drawBitmap(background,0,0,p);
                        drawskilllogo(c,p);
                        drawpad(c,p);

                        Paint realtimehp = new Paint();
                        realtimehp.setColor(Color.BLACK);
                        realtimehp.setAntiAlias(true);
                        realtimehp.setFilterBitmap(true);
                        realtimehp.setTextSize(20);

                        realtimehp.setTextAlign(Paint.Align.RIGHT);

                        //简易血条的绘制
                        Paint Hp = new Paint();
                        Hp.setColor(Color.RED);
                        Hp.setAntiAlias(true);
                        Hp.setStyle(Paint.Style.FILL);
                        Rect rect2 = new Rect(sw/2 + 252 + (int)((1.0-(float)boss.hp/(float)BHP)*488.0),50,sw - 60 - 168 ,80);

                        Paint Hpboarder = new Paint();
                        Hpboarder.setColor(Color.BLACK);
                        Hpboarder.setAntiAlias(true);
                        Hpboarder.setStyle(Paint.Style.STROKE);
                        Hpboarder.setStrokeWidth((float)3.0);

                        c.drawRect(rect2, Hp);
                        c.drawText(String.valueOf(boss.hp)+"/"+String.valueOf(BHP),sw-410,77,realtimehp);
                        c.drawBitmap(rehpbar, sw - 60 - rehpbar.getWidth(), 10, p);
                        for (int i = 0 ; i < roles.size(); ++i) {
                            Rect rec = new Rect(60 + 168,50 + hpbar.getHeight()*i + 10*i,60 + 168 + (int)(((float)(roles.get(i).HP)/(float)HP[i])*488.0),80 + hpbar.getHeight()*i + 10*i);
                            realtimehp.setTextAlign(Paint.Align.CENTER);
                            c.drawRect(rec,Hp);
                            c.drawText(String.valueOf(roles.get(i).HP)+"/"+String.valueOf(HP[i]),466,77 + hpbar.getHeight()*i + 10*i,realtimehp);
                            c.drawBitmap(hpbar,60,10 + hpbar.getHeight()*i + 10*i,p);
                        }

                        //判断游戏是否结束
                        if (boss.hp <= 0 && !gameover) {
                            boss.hp = 0;
                            gameover = true;
                            win = true;
                            Random random = new Random();
                            int luck = Math.abs(random.nextInt())%100;
                            if (luck >= 60) {
                                boxget = true;
                                InputStream is;
                                shine = new Bitmap[3];
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inJustDecodeBounds = false;
                                options.inSampleSize = 1;
                                is = getResources().openRawResource(R.raw.box);
                                box = BitmapFactory.decodeStream(is,null,options);
                                is = getResources().openRawResource(R.raw.shining01);
                                shine[0] = BitmapFactory.decodeStream(is,null,options);
                                is = getResources().openRawResource(R.raw.shining02);
                                shine[1] = BitmapFactory.decodeStream(is,null,options);
                                is = getResources().openRawResource(R.raw.shining03);
                                shine[2] = BitmapFactory.decodeStream(is,null,options);
                                bx = boss.X;
                                by = 580;
                                int ord = Math.abs(random.nextInt())%2;
                                myApp.addtobag(new equipment(IDlist[ord],getResources()));
                            }
                        }
                        if (!gameover) {
                            boolean flag = true;
                            for (int i = 0; i != roles.size() ; i++) {
                                if (roles.get(i).HP > 0) flag = false;
                                else roles.get(i).HP = 0;
                            }
                            if (flag) {
                                gameover = true;
                                win = false;
                            }
                        }

                        roles.get(0).draw(c,p);
                        for (int i = 1 ; i < roles.size() ; ++i) {
                            roles.get(i).draw(c,p);
                        }
                        if (!gameover) {
                            doubleclickcnt();
                            addskill();
                            bosslogic(c,p);
                            bulletlogic(c,p);
                            playerskilldmg(c,p);
                            playerdmg();
                            generateaction();
                        }


                        if (gameover && win) {boss.draw(c,p); boss.logic(); }
                        else if (gameover) boss.draw(c,p);
                        if (!gameover)
                            roles.get(0).logic();
                        for (int i = 1 ; i < roles.size() ; ++i) {
                            if (nextround[i] != 0)
                                roles.get(i).status = nextround[i];
                            if (!gameover)
                                roles.get(i).logic();
                        }
                        drawpotion(c,p);

                        if (boxget) {
                            drawbox(c,p);
                        }

                        if (gameover && (!boxget || (boxget && land))) {
                            c.drawBitmap(reportbg,sw/2 - reportbg.getWidth()/2, sh/2 - reportbg.getHeight()/2,p);
                            if (win) {
                                resp.setColor(Color.RED);
                                c.drawText("胜利", sw/2 , sh/2 - reportbg.getHeight()/2 +140, resp);
                                c.drawText("经验值： +10", sw/2 - 60 , sh/2 - 20, gainp);
                                c.drawText("积分： +10", sw/2 - 60, sh/2 + 20, gainp);
                                if (boxget) {
                                    c.drawText("您获得了一件神秘装备，快去物品栏查看吧！",sw/2, sh/2 + 200, rewardp);
                                }
                            } else {
                                resp.setColor(Color.BLACK);
                                c.drawText("失败", sw/2 , sh/2 - reportbg.getHeight()/2 + 140, resp);
                                c.drawText("经验值： +2", sw/2 - 80 , sh/2 - 20, gainp);
                                c.drawText("积分： +2", sw/2 - 80, sh/2 + 20, gainp);
                            }
                            if (!added) {
                                user.addexp(win?10:2);
                                user.add_score(win?10:2);
                                added = true;
                            }

                        }

                        //Thread.sleep(5);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(c!= null){
                        holder.unlockCanvasAndPost(c);//结束锁定画图，并提交改变。
                    }
                }
            }
        }
    }

    private void generateaction() {
        boolean flag;
        Random random = new Random();
        for (int i = 1; i < roles.size() ; ++i) {
            bas_role p = roles.get(i);
            nextround[i] = 0;
            flag = true;
            switch (roles.get(i).getjob()) {
                case 0:
                    //遇到落雷的逻辑
                    if (Math.abs(p.status) == 1 || p.status == 0) {
                        for (int j = 0 ; j != thunders.size(); ++j) {
                            if (!thunders.get(j).valid) {
                                if ( (100-thunders.get(j).cnt)*p.speed > 90 - Math.abs(thunders.get(j).x - (p.x+p.b)/2) ) {
                                    if (nextround[i] != 2)
                                        nextround[i] = thunders.get(j).x - (p.x+p.b)/2 > 0 ? -1: 1;
                                } else {
                                    if (thunders.get(j).cnt == 28) {
                                        nextround[i] = 2;
                                    }
                                }
                            } else {
                                if (Math.abs(thunders.get(j).x - (p.x+p.b)/2) < 90) {
                                    nextround[i] = 2;
                                }
                            }
                        }
                    }
                    flag = nextround[i] == 0;
                    //看到Boss蓄力的逻辑
                    if (flag && (p.status == 0 || Math.abs(p.status) == 1)) {
                        if (boss.status == 5 && p.SCD[0] == 0 && boss.X - p.p < warrioratkrange[1] && boss.X - p.p > 0) {
                            nextround[i] = 4;
                            flag = false;
                        } else if ( boss.status == 5 && p.SCD[0] < 100 - boss.cnt && boss.X - p.p > warrioratkrange[1] ) {
                            nextround[i] = 1;
                            flag = false;
                        }
                    }
                    //一般逻辑 有招数可放
                    if (flag && p.status == 0) {
                        if (boss.X - p.p < warrioratkrange[2] && p.SCD[1] == 0) {
                            nextround[i] = 5;
                            flag = false;
                        } else if (boss.X - p.p < warrioratkrange[0] && p.NCD == 0) {
                            nextround[i] = 3;
                            flag = false;
                        }
                    }
                    //无招数可放
                    if (flag && p.status == 0) {
                        if (boss.X - p.p < safedis[0]) {
                            nextround[i] = -1;
                            flag = false;
                        } else {
                            int r = Math.abs(random.nextInt())%100;
                            if (r < chance[i-1]) {
                                nextround[i] = 2;
                            } else {
                                nextround[i] = 1;
                            }
                            flag = false;
                        }
                    }
                    if (flag && p.status == 1) {
                        if (boss.X - p.p < safedis[0]) {
                            nextround[i] = -1;
                            flag = false;
                        }
                    }
                    if (flag && p.status == 0) {
                        int r = Math.abs(random.nextInt())%100;
                        if (r < chance[i-1]) {
                            nextround[i] = 2;
                        } else {
                            nextround[i] = 1;
                        }
                    } else if (flag) {
                        nextround[i] = 0;
                    }
                   break;
                case 1:
                    //遇到落雷的逻辑
                    if (Math.abs(p.status) == 1 || p.status == 0) {
                        for (int j = 0 ; j != thunders.size(); ++j) {
                            if (!thunders.get(j).valid) {
                                if ( (100-thunders.get(j).cnt)*p.speed > 90 - Math.abs(thunders.get(j).x - (p.x+p.b)/2) ) {
                                    if (nextround[i] != 2)
                                        nextround[i] = thunders.get(j).x - (p.x+p.b)/2 > 0 ? -1: 1;
                                } else {
                                    if (thunders.get(j).cnt == 20 && p.MCD == 0) {
                                        nextround[i] = -2;
                                    }
                                }
                            } else {
                                if (Math.abs(thunders.get(j).x - (p.x+p.b)/2) < 90) {
                                    if (p.MCD == 0)
                                        nextround[i] = -2;
                                    else {
                                        nextround[i] = -1;
                                    }
                                }
                            }
                        }
                    }
                    flag = nextround[i] == 0;

                    //看到Boss蓄力的逻辑
                    if (flag && (p.status == 0 || Math.abs(p.status) == 1)) {
                        if (boss.status == 5 && p.SCD[0] == 0 && boss.X - p.p < magicianatkrange[1] && boss.X - p.p > 0) {
                            nextround[i] = 4;
                            flag = false;
                        } else if (boss.status == 5) {
                            if (p.SCD[0] < 100 - boss.cnt && boss.X - p.p > 360 && p.MCD == 0) {
                                nextround[i] = 2;
                                flag = false;
                            } else if (p.SCD[0] < 100 - boss.cnt && boss.X - p.p > magicianatkrange[1]) {
                                nextround[i] = 1;
                                flag = false;
                            }
                        }
                    }
                    //技能
                    if (flag && p.status == 0) {
                        if (boss.X - p.p > 300 && boss.X - p.p < 500 && p.SCD[1] == 0) {
                            nextround[i] = 5;
                            flag = false;
                        }
                    }

                    //普攻
                    if (flag && p.status == 0 && p.NCD == 0) {
                        nextround[i] = 3;
                        flag = false;
                    }

                    //保持安全距离
                    if (flag && (p.status == 0 || Math.abs(p.status) == 1) ) {
                        if (boss.X - p.p < safedis[1]) {
                            nextround[i] = -1;
                            flag = false;
                        }
                    }

                    if (flag && p.status == 0) {
                        int r = Math.abs(random.nextInt())%100;
                        if (r < chance[i-1]) {
                            nextround[i] = 1;
                        } else {
                            nextround[i] = -1;
                        }
                    } else if (flag) {
                        nextround[i] = 0;
                    }
                    break;

                case 2:
                    if (Math.abs(p.status) == 1 || p.status == 0) {
                        for (int j = 0 ; j != thunders.size(); ++j) {
                            if (!thunders.get(j).valid) {
                                if ( (100-thunders.get(j).cnt)*p.speed > 90 - Math.abs(thunders.get(j).x - (p.x+p.b)/2) ) {
                                    if (nextround[i] != 4)
                                        nextround[i] = thunders.get(j).x - (p.x+p.b)/2 > 0 ? -1: 1;
                                } else {
                                    if (thunders.get(j).cnt == 4 && p.SCD[0] == 0) {
                                        nextround[i] = 4;
                                    }
                                }
                            } else {
                                if (Math.abs(thunders.get(j).x - (p.x+p.b)/2) < 90) {
                                    nextround[i] = 4;
                                }
                            }
                        }
                    }
                    flag = nextround[i] == 0;
                    if (flag && (p.status == 0 || Math.abs(p.status) == 1)) {
                        if (boss.status == 5 && p.SCD[1] == 0 && boss.X - p.p < doctoratkrange[2] && boss.X - p.p > 0) {
                            nextround[i] = 5;
                            flag = false;
                        } else if (boss.status == 5 && p.SCD[0] < 100 - boss.cnt && boss.X - p.p > doctoratkrange[2]) {
                            nextround[i] = 1;
                            flag = false;
                        }
                        if (flag && p.MCD == 0) {
                            for (int j = 0; j != roles.size(); ++j) {
                                if ((float) roles.get(j).HP / (float) HP[j] < 0.95) {
                                    nextround[i] = 2;
                                    flag = false;
                                    break;
                                }
                            }
                        }
                        if (flag && p.NCD == 0 && boss.X - p.p < doctoratkrange[0]) {
                            nextround[i] = 3;
                            flag = false;
                        }
                    }
                    if (p.status == 0) {
                        if (flag && p.p > sw - 330) {
                            nextround[i] = -1;
                            flag = false;
                        }
                        if (flag && boss.X - p.p < safedis[2]) {
                            int dir = Math.abs(random.nextInt()) % 2;
                            nextround[i] = dir == 0 ? 1 : -1;
                            flag = false;
                        } else if (flag && boss.X - p.p >= safedis[2]) {
                            int dir = Math.abs(random.nextInt()) % 10;
                            nextround[i] = dir > 7 ? -1 : 1;
                            flag = false;
                        } else if (flag) {
                            nextround[i] = 0;
                        }
                    }
                    break;
            }
        }
    }




    private void drawskilllogo(Canvas c, Paint p) {
        int head = 300,interval = 30,width = 150;
        int offset1[] = new int[4];

        bas_role p1 = roles.get(0);
        //半透明遮盖的偏移量
        offset1[0] = 132 - (int)((float)p1.NCD/(float)cd1[0]*132.0);
        offset1[1] = 132 - (int)((float)p1.SCD[0]/(float)cd1[1]*132.0);
        offset1[2] = 132 - (int)((float)p1.SCD[1]/(float)cd1[2]*132.0);
        offset1[3] = 132 - (int)((float)p1.MCD/(float)cd1[3]*132.0);

        //p1的技能图标绘制
        c.save();
        c.clipRect(sw/2 + head - interval - width, sh - 180, sw/2 + head - interval, sh - 180 + width);
        c.drawBitmap(p1skill[0],sw/2 + head - interval - width,sh - 180, p);
        c.restore();
        c.save();
        c.clipRect(sw/2 + head - interval - width + 9,sh - 180 + 9, sw/2 + head - interval - 9, sh - 180 + width - 9);
        c.drawBitmap(cdcover, sw/2 + head - interval - width + 9, sh - 180 - offset1[0] + 9, p);
        c.restore();

        c.save();
        c.clipRect(sw/2 + head,sh - 180, sw/2 + head + width, sh - 180 + width);
        c.drawBitmap(p1skill[3],sw/2 + head,sh - 180, p);
        c.restore();
        c.save();
        c.clipRect(sw/2 + head + 9,sh - 180 + 9, sw/2 + head + width - 9, sh - 180 + width - 9);
        c.drawBitmap(cdcover, sw/2 + head + 9, sh - 180 - offset1[3] + 9, p);
        c.restore();

        c.save();
        c.clipRect(sw/2 + head + interval + width, sh - 180, sw/2 + head + interval + 2*width, sh - 180 + width);
        c.drawBitmap(p1skill[1], sw/2 + head + interval + width, sh - 180, p);
        c.restore();
        c.save();
        c.clipRect(sw/2 + head + interval + width + 9, sh - 180 + 9, sw/2 + head + interval + 2*width - 9, sh - 180 + width - 9);
        c.drawBitmap(cdcover, sw/2 + head + interval + width + 9, sh - 180 - offset1[1] + 9, p);
        c.restore();

        c.save();
        c.clipRect(sw/2 + head + 2*width + 2*interval, sh - 180, sw/2 + head + 3*width + 2*interval, sh - 180 + width);
        c.drawBitmap(p1skill[2], sw/2 + head + 2*width + 2*interval, sh - 180, p);
        c.restore();
        c.save();
        c.clipRect(sw/2 + head + 2*width + 2*interval + 9, sh - 180 + 9, sw/2 + head + 3*width + 2*interval - 9, sh - 180 + width - 9);
        c.drawBitmap(cdcover, sw/2 + head + 2*width + 2*interval + 9, sh - 180 - offset1[2] + 9, p);
        c.restore();
    }

    private void refresh_status(bas_role player) {
        player.natk_cnt = 0;
        player.mv_cnt = 0;
        player.mcnt = 0;
        player.sk_cnt[0] = player.sk_cnt[1] = 0;
        player.atk_validity = true;
        player.atk_activated = false;
        player.shock_dis = 40;
        player.dizzy_cnt = 0;
        player.blind_cnt = 0;
        player.poisoned = false;
        player.poisoncnt = 0;
    }

    public boolean onTouchEvent(MotionEvent event) {
        int pointX = (int) event.getX();
        int pointY = (int) event.getY();

        if (!gameover) {
            if (!ishosted && !roles.get(0).lastingAI) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                    //判定用户是否点击了按钮
                    if (pointX > 20 && pointX < 20 + 192) {
                        if (pointY > 860 && pointY < 860 + 192) {
                            isleftpressed = true;
                            if (roles.get(0).status == 0 || Math.abs(roles.get(0).status) == 1)
                                roles.get(0).status = roles.get(0).headto == -1 ? 1 : -1;
                        } else {
                            isleftpressed = false;
                            if (Math.abs(roles.get(0).status) == 1) roles.get(0).status = 0;
                        }
                    } else {
                        isleftpressed = false;
                        if (Math.abs(roles.get(0).status) == 1) roles.get(0).status = 0;
                    }
                    if (pointX > 232 && pointX < 424) {
                        if (pointY > 860 && pointY < 860 + 192) {
                            isrightpressed = true;
                            if (roles.get(0).status == 0 || Math.abs(roles.get(0).status) == 1)
                                roles.get(0).status = roles.get(0).headto == 1 ? 1 : -1;
                        } else {
                            isrightpressed = false;
                            if (Math.abs(roles.get(0).status) == 1) roles.get(0).status = 0;
                        }
                    } else {
                        isrightpressed = false;
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP && (isrightpressed || isleftpressed)) {
                    if (roles.get(0).getjob() == 1) {
                        if (isleftpressed) {
                            lastrightcnt = -1;
                            if (lastleftcnt < 0) {
                                lastleftcnt = 0;
                            } else if (lastleftcnt < 30 && roles.get(0).MCD == 0) {
                                roles.get(0).status = -2;
                                lastleftcnt = -1;
                            } else
                                lastleftcnt = -1;
                        } else if (isrightpressed) {
                            lastleftcnt = -1;
                            if (lastrightcnt < 0) {
                                lastrightcnt = 0;
                            } else if (lastrightcnt < 30 && roles.get(0).MCD == 0) {
                                roles.get(0).status = 2;
                                lastrightcnt = -1;
                            } else
                                lastrightcnt = -1;
                        }
                    }
                    isrightpressed = isleftpressed = false;
                    if (Math.abs(roles.get(0).status) == 1)
                        roles.get(0).status = 0; //松开左右键时，停止移动，但是松开托管键的时候不需要，点击屏幕中任何区域时也不需要
                }

                //技能释放判断
                int head = 300, interval = 30, width = 150;
                if (event.getAction() == MotionEvent.ACTION_UP && (Math.abs(roles.get(0).status) == 1 || roles.get(0).status == 0)) {
                    if (pointY >= 900 && pointY <= 1050) {
                        if (pointX >= sw / 2 + head - interval - width && pointX <= sw / 2 + head - interval && roles.get(0).NCD == 0) {
                            roles.get(0).status = 3;
                        } else if (pointX >= sw / 2 + head && pointX <= sw / 2 + head + width && roles.get(0).MCD == 0) {
                            roles.get(0).status = 2;
                        } else if (pointX >= sw / 2 + head + interval + width && pointX <= sw / 2 + head + interval + 2 * width && roles.get(0).SCD[0] == 0) {
                            roles.get(0).status = 4;
                        } else if (pointX >= sw / 2 + head + 2 * interval + 2 * width && pointX <= sw / 2 + head + 2 * interval + 3 * width && roles.get(0).SCD[1] == 0) {
                            roles.get(0).status = 5;
                        }
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN && (pointX > 84 - potionblock.getWidth()/2 && pointX < 84 + potionblock.getWidth()/2) && (pointY < sh - 350 + potionblock.getHeight()/2 && pointY > sh - 350 - potionblock.getHeight()/2)) {
                    if (user.bottle != null) {
                        user.bottle.usage(user);
                        user.bottle = null;
                    }
                }
            }
        }
        if (gameover && event.getAction()== MotionEvent.ACTION_DOWN) {
            if (pointX > 1500 && pointX < 1837 && pointY > 760 && pointY < 910) {
                this.finish();
            }
        }

        return false;
    }

    private void drawpad(Canvas c, Paint p) {
        if (isleftpressed)
            c.drawBitmap(left,20,860,p);
        else
            c.drawBitmap(dleft,20,860,p);
        if (isrightpressed)
            c.drawBitmap(right,40 + 192, 860, p);
        else
            c.drawBitmap(dright,40 + 192, 860, p);
    }

    private void init_status() {
        InputStream is1;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 1;

        boss.initialize(res);
        initlocalskillimg();
        is1 = res.openRawResource(R.raw.hpbar);
        hpbar = BitmapFactory.decodeStream(is1,null,options);
        is1 = res.openRawResource(R.raw.rehpbar);
        rehpbar = BitmapFactory.decodeStream(is1,null,options);
        is1 = res.openRawResource(R.raw.potionblock);
        potionblock = BitmapFactory.decodeStream(is1,null,options);
        is1 = res.openRawResource(R.raw.reportbg);
        reportbg = BitmapFactory.decodeStream(is1,null, options);

        shockwavearray = new shockwave[roles.size()];
        tornadearray = new tornado[roles.size()];
        for (int i = 0 ; i != roles.size() ; ++i) {
            switch (roles.get(i).getjob()) {
                case 0:
                    shockwavearray[i] = new shockwave(0,0,760,1);
                    if (shockwave.img == null) {
                        shockwave.img = new Bitmap[6];
                        is1 = res.openRawResource(R.raw.swordkee0);
                        shockwave.img[0] = BitmapFactory.decodeStream(is1,null,options);
                        is1 = res.openRawResource(R.raw.swordkee1);
                        shockwave.img[1] = BitmapFactory.decodeStream(is1,null,options);
                        is1 = res.openRawResource(R.raw.swordkee2);
                        shockwave.img[2] = BitmapFactory.decodeStream(is1,null,options);
                        is1 = res.openRawResource(R.raw.swordkee3);
                        shockwave.img[3] = BitmapFactory.decodeStream(is1,null,options);
                        is1 = res.openRawResource(R.raw.swordkee4);
                        shockwave.img[4] = BitmapFactory.decodeStream(is1,null,options);
                        is1 = res.openRawResource(R.raw.swordkee5);
                        shockwave.img[5] = BitmapFactory.decodeStream(is1,null,options);
                        shockwave.re_img = new Bitmap[6];
                        is1 = res.openRawResource(R.raw.reswordkee0);
                        shockwave.re_img[0] = BitmapFactory.decodeStream(is1,null,options);
                        is1 = res.openRawResource(R.raw.reswordkee1);
                        shockwave.re_img[1] = BitmapFactory.decodeStream(is1,null,options);
                        is1 = res.openRawResource(R.raw.reswordkee2);
                        shockwave.re_img[2] = BitmapFactory.decodeStream(is1,null,options);
                        is1 = res.openRawResource(R.raw.reswordkee3);
                        shockwave.re_img[3] = BitmapFactory.decodeStream(is1,null,options);
                        is1 = res.openRawResource(R.raw.reswordkee4);
                        shockwave.re_img[4] = BitmapFactory.decodeStream(is1,null,options);
                        is1 = res.openRawResource(R.raw.reswordkee5);
                        shockwave.re_img[5] = BitmapFactory.decodeStream(is1,null,options);
                    }
                    break;
                case 1:
                    shockwavearray[i] = new shockwave(1,0,760,1);
                    tornadearray[i] = new tornado(0,760);
                    if (shockwave.mimg == null) {
                        is1 = res.openRawResource(R.raw.mshockwave);
                        shockwave.mimg = BitmapFactory.decodeStream(is1,null,options);
                    }
                    if (tornado.img == null) {
                        is1 = res.openRawResource(R.raw.tornade);
                        tornado.img = BitmapFactory.decodeStream(is1, null, options);
                    }
                    break;
            }
        }

        background = BitmapFactory.decodeResource(res, R.drawable.bg);
        dleft = BitmapFactory.decodeResource(res,R.drawable.dleft);
        left = BitmapFactory.decodeResource(res,R.drawable.left);
        dright = BitmapFactory.decodeResource(res,R.drawable.dright);
        right = BitmapFactory.decodeResource(res,R.drawable.right);
        host = BitmapFactory.decodeResource(res,R.drawable.host);
        p1skill = new Bitmap[4];
        cd1 = new int[4];

        switch (roles.get(0).getjob()) {
            case 0:
                is1 = res.openRawResource(R.raw.warrioractions);
                p1skill[0] = BitmapFactory.decodeResource(res,R.drawable.wnatklogo);
                p1skill[1] = BitmapFactory.decodeResource(res,R.drawable.kicklogo);
                p1skill[2] = BitmapFactory.decodeResource(res,R.drawable.shenglonglogo);
                p1skill[3] = BitmapFactory.decodeResource(res,R.drawable.blocklogo);
                break;
            case 1:
                is1 = res.openRawResource(R.raw.magicianactions);
                p1skill[0] = BitmapFactory.decodeResource(res,R.drawable.mnatklogo);
                p1skill[1] = BitmapFactory.decodeResource(res,R.drawable.baologo);
                p1skill[2] = BitmapFactory.decodeResource(res,R.drawable.tornadologo);
                p1skill[3] = BitmapFactory.decodeResource(res,R.drawable.flashlogo);
                break;
            case 2:
                is1 = res.openRawResource(R.raw.doctoraction);
                p1skill[0] = BitmapFactory.decodeResource(res,R.drawable.dnatklogo);
                p1skill[1] = BitmapFactory.decodeResource(res,R.drawable.shieldlogo);
                p1skill[2] = BitmapFactory.decodeResource(res,R.drawable.blindlogo);
                p1skill[3] = BitmapFactory.decodeResource(res,R.drawable.srecoverlogo);
                break;
            default:
                is1 = res.openRawResource(R.raw.warrioractions);
                p1skill[0] = BitmapFactory.decodeResource(res,R.drawable.wnatklogo);
                p1skill[1] = BitmapFactory.decodeResource(res,R.drawable.kicklogo);
                p1skill[2] = BitmapFactory.decodeResource(res,R.drawable.shenglonglogo);
                p1skill[3] = BitmapFactory.decodeResource(res,R.drawable.blocklogo);
        }
        roles.get(0).character_img = BitmapFactory.decodeStream(is1, null, options);

        for (int i = 1 ; i < roles.size() ; ++i) {
            switch (roles.get(i).getjob()) {
                case 0:
                    is1 = res.openRawResource(R.raw.warrioractions);
                    break;
                case 1:
                    is1 = res.openRawResource(R.raw.magicianactions);
                    break;
                case 2:
                    is1 = res.openRawResource(R.raw.doctoraction);
                    break;
                default:
                    is1 = res.openRawResource(R.raw.warrioractions);
            }
            roles.get(i).character_img = BitmapFactory.decodeStream(is1, null, options);
        }


        cdcover = BitmapFactory.decodeResource(res,R.drawable.cd_cover);
        switch (roles.get(0).getjob()) {
            case 0:
                cd1[0] = 80;
                cd1[1] = 180;
                cd1[2] = 360;
                cd1[3] = 1;
                break;
            case 1:
                cd1[0] = 120;
                cd1[1] = 300;
                cd1[2] = 480;
                cd1[3] = 120;
                break;
            case 2:
                cd1[0] = 80;
                cd1[1] = 480;
                cd1[2] = 480;
                cd1[3] = 180;
                break;
            default:
                cd1[0] = 80;
                cd1[1] = 180;
                cd1[2] = 360;
                cd1[3] = 1;
        }

        int inix = 100;
        for (int i = 0 ; i != roles.size() ; ++i) {
            roles.get(i).x = inix;
            roles.get(i).y = 760;
            roles.get(i).headto = 1;
            roles.get(i).status = 0;
            switch (roles.get(i).getjob()) {
                case 0:
                    roles.get(i).b = inix + 170;
                    roles.get(i).p = inix + 170;
                    break;
                case 1:
                    roles.get(i).b = inix + 160;
                    roles.get(i).p = inix + 160;
                    break;
                case 2:
                    roles.get(i).b = inix + 110;
                    roles.get(i).p = inix + 110;
                    break;
            }
            inix += 200;
        }

        bullet.img = BitmapFactory.decodeResource(res,R.drawable.bullet);
        bullet.sw = sw;
        bossbullet.sw = sw;
        bossbullet.img = new Bitmap[3];
        is1 = res.openRawResource(R.raw.bossbullet1);
        bossbullet.img[0] = BitmapFactory.decodeStream(is1,null,options);
        is1 = res.openRawResource(R.raw.bossbullet2);
        bossbullet.img[1] = BitmapFactory.decodeStream(is1,null,options);
        is1 = res.openRawResource(R.raw.bossbullet3);
        bossbullet.img[2] = BitmapFactory.decodeStream(is1,null,options);


        for (int i = 0 ; i != roles.size() ; ++i) {
            switch (roles.get(i).getjob()) {
                case 0:
                    if (bas_role.block == null) {
                        is1 = res.openRawResource(R.raw.block);
                        bas_role.block = BitmapFactory.decodeStream(is1, null, options);
                    }
                    break;
                case 1:
                    if (bas_role.flash == null) {
                        is1 = res.openRawResource(R.raw.flash);
                        bas_role.flash = BitmapFactory.decodeStream(is1, null, options);
                    }

                    break;
                case 2:
                    if (bas_role.shield == null) {
                        is1 = res.openRawResource(R.raw.shield);
                        bas_role.shield = BitmapFactory.decodeStream(is1, null, options);
                    }
                    break;
            }
        }

        is1 = res.openRawResource(R.raw.dizzy);
        bas_role.dizzy = BitmapFactory.decodeStream(is1, null, options);
        bas_role.sw = sw;
        is1 = res.openRawResource(R.raw.toxic);
        bas_role.poison = BitmapFactory.decodeStream(is1, null, options);
    }

    private void addskill() {
        bas_role p;
        for (int i = 0 ; i != roles.size() ; ++i) {
            p = roles.get(i);
            if (p.getjob() == 1 && p.status == 3 && p.natk_cnt == 37) {
                bullets.get(i).add(new bullet(p.p + 15, p.y - 240 + 30, p.p+15+120, 1));
            }
            if (p.getjob() == 1 && p.status == 5 && p.sk_cnt[1] == 28) {
                tornadearray[i].valid = true;
                tornadearray[i].x = p.p + 480 * p.headto;
                tornadearray[i].center = tornadearray[i].x + 150;
            }
            if (p.getjob() == 1 && p.status == 4 && p.sk_cnt[0] == 24) {
                shockwavearray[i].validity = true;
                shockwavearray[i].x = p.p + 88*p.headto;
                shockwavearray[i].cnt = 52;
            }
            if (p.getjob() == 0 && p.status == 5 && p.sk_cnt[1] == 8) {
                shockwavearray[i].validity = true;
                shockwavearray[i].x = p.p + 50*p.headto;
                shockwavearray[i].cnt = 36;
                shockwavearray[i].dir = p.headto;
            }
        }
    }

    private void bulletlogic(Canvas c, Paint p) {
        for (int i = 0 ; i < roles.size() ; ++i) {
            ArrayList<bullet> tmp = bullets.get(i);
            for (int j = 0 ; j < tmp.size(); ++j) {
                if (tmp.get(j).isdead) {
                    tmp.remove(j);
                } else {
                    bullet b = tmp.get(j);
                    if (b.p > boss.X - 40) {
                        b.isdead = true;
                        boss.hp -= (int)((float)roles.get(i).getATK()*0.6);
                    }
                    b.draw(c,p);
                    b.logic();
                }
            }
        }
    }

    private boolean atkreach(int position, int bossposition,int range) {
        return (bossposition - position < range && bossposition - position > 0);
    }

    private void playerdmg() {
        for (int i = 0 ; i != roles.size() ; ++i) {
            bas_role player = roles.get(i);
            switch (player.getjob()) {
                case 0:
                    switch (player.status) {
                        case 3:
                            if (player.atk_validity && player.atk_activated && atkreach(player.p,boss.X,warrioratkrange[0])) {
                                boss.hp -= player.getATK();
                                player.atk_validity = false;
                            }
                            break;
                        case 4:
                            if (player.atk_validity && player.atk_activated && atkreach(player.p, boss.X, warrioratkrange[1])) {
                                boss.hp -= (int)((float)player.getATK()*2.5);
                                player.atk_validity = false;
                                if (boss.status == 5) {
                                    boss.status = 0;
                                }
                            }
                            break;
                    }
                    break;
                case 2:
                    switch (player.status) {
                        case 5:
                            if (player.atk_activated && player.atk_validity && atkreach(player.p, boss.X, doctoratkrange[2])) {
                                boss.hp -= (int) ((float) player.getATK() * 3.0);
                                player.atk_validity = false;
                                if (boss.status == 5) {
                                    boss.status = 0;
                                }
                            }
                            break;
                        case 3:
                            if (player.atk_validity && player.atk_validity && atkreach(player.p, boss.X, doctoratkrange[0])) {
                                boss.hp -= player.getATK();
                                player.atk_validity = false;
                            }
                            break;
                        case 2:
                            if (player.mcnt == 8) {
                                for (int j = 0; j != roles.size(); ++j) {
                                    roles.get(j).HP = roles.get(j).HP + (int) (0.05 * (float) HP[j]) <= HP[j] ? roles.get(j).HP + (int) (0.05 * (float) HP[j]) : HP[j];
                                }
                            }
                            if (shinecnt != 0 && Math.abs(boss.X - player.p) < 180) {
                                boss.hp -= 20;
                            }
                            break;
                    }
                    break;
            }
        }
    }

    private void playerskilldmg(Canvas c, Paint p) {
        for (int i = 0 ; i != roles.size(); ++i) {
            //冲击波判定
            if (roles.get(i).getjob() == 0 || roles.get(i).getjob() == 1) {
                if (shockwavearray[i].cnt != 0) {
                    shockwavearray[i].draw(c, p);
                    if (shockwavearray[i].validity) {
                        switch (shockwavearray[i].type) {
                            case 0:
                                if (boss.X - shockwavearray[i].x < 395 && boss.X - shockwavearray[i].x > -100) {
                                    shockwavearray[i].validity = false;
                                    boss.hp -= roles.get(i).getATK() * 3;
                                }
                                break;
                            case 1:
                                if (boss.X - shockwavearray[i].x < 185 && boss.X - shockwavearray[i].x > -185) {
                                    shockwavearray[i].validity = false;
                                    boss.hp -= (int) ((float) roles.get(i).getATK() * 2.5);
                                    if (boss.status == 5) boss.status = 0;
                                }
                                break;
                        }
                    }
                    shockwavearray[i].logic();
                }
            }

            //龙卷风判定
            if (roles.get(i).getjob() == 1) {
                if (tornadearray[i].valid) {
                    tornadearray[i].draw(c, p);
                    if (Math.abs(boss.X - tornadearray[i].center) <= 180) {
                        boss.hp -= (int) ((float) roles.get(i).getATK() * 0.05);
                    }
                    tornadearray[i].logic();
                }
            }
        }
    }

    private void bosslogic(Canvas c, Paint p) {
        //普攻子弹加入
        if (boss.status == 1 && boss.cnt == 36) {
            bossbullets.add(new bossbullet(boss.X - 220, boss.Y - 200));
        }
        //子弹逻辑
        for (int i = 0 ; i < bossbullets.size(); ++i) {
            bossbullet b = bossbullets.get(i);
            if (b.isdead) {
                bossbullets.remove(i);
            } else {
                bas_role tar = closestone(b.x + 220);
                if (tar != null) {
                    if (b.x - tar.p < 0 && b.x - tar.p > - 210) {
                        if (tar.getjob() == 0 && tar.status == 2) {
                            tar.HP -= (int)((float)boss.atk*0.4);
                            b.isdead = true;
                        } else if (!(tar.getjob() == 2 && tar.status == 4) && !(tar.getjob() == 1 && Math.abs(tar.status) == 2)) {
                            tar.HP -= boss.atk;
                            refresh_status(tar);
                            tar.status = 6;
                            b.isdead = true;
                        }
                    }
                }
                b.draw(c,p);
                b.logic();
            }
        }
        //技能2毒气绘制
        if (boss.status == 2 && !posionactivated) posionactivated = true;
        if (posionactivated) {
            for (int i = 1 ; i != 6 ; ++i) {
                c.drawBitmap(poison[posioncnt/4],boss.X - 280*i - poison[posioncnt/4].getWidth()/2,boss.Y - 120, p);
            }
            for (int i = 0 ; i != roles.size() ; ++i) {
                roles.get(i).poisoned = true;
            }
            posioncnt++;
            if (posioncnt == 48) {
                posioncnt = 0;
                posionactivated = false;
            }
        }
        //闪电的加入和绘制
        Random random = new Random();
        if (boss.status == 3 && !thunderactivated ) thunderactivated = true;
        if (thunderactivated) {
            if (thundercnt%80 == 0) {
                int target = Math.abs(random.nextInt())%roles.size();
                int range = 10 - Math.abs(random.nextInt())%20;
                thunders.add(new thunder((roles.get(target).x+roles.get(target).p)/2 + range, 760));
            }
            thundercnt++;
            if (thundercnt == 410) {
                thunderactivated = false;
            }
        }
        for (int i = 0 ; i != thunders.size(); ++i) {
            if (!thunders.get(i).activated)
                thunders.remove(i);
            else {
                thunder t = thunders.get(i);
                if (t.valid) {
                    for (int j = 0 ; j != roles.size() ; ++j) {
                        bas_role r = roles.get(j);
                        if (Math.abs((r.x+r.b)/2 - t.x) < 90) {
                            if (r.getjob() == 0 && r.status == 2) {
                                r.HP -= (int)((float)boss.atk*0.6);
                            } else if (r.getjob() != 2 && r.status != 4) {
                                r.HP -= (int)((float)boss.atk*1.5);
                                int drate = Math.abs(random.nextInt())%100;
                                if (drate < 20) {
                                    r.status = 8;
                                    refresh_status(r);
                                }
                            }
                            t.valid = false;
                        }
                    }
                }
                t.draw(c,p);
                t.logic();
            }
        }
        boss.draw(c,p);
        boss.logic();
    }

    private bas_role closestone(int x) {
        bas_role one = roles.get(0);
        for (int i = 1 ; i < roles.size() ; ++i) {
            if (roles.get(i).p > one.p && roles.get(i).p < x)
                one = roles.get(i);
        }
        if (one.p < x) return one;
        else return null;
    }

    private void initlocalskillimg() {
        InputStream is;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 1;
        poison = new Bitmap[12];

        thunder.thunderimg = new Bitmap[9];
        thunder.forcastimg = new Bitmap[5];
        is = res.openRawResource(R.raw.forecast1);
        thunder.forcastimg[0] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.forecast2);
        thunder.forcastimg[1] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.forecast3);
        thunder.forcastimg[2] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.forecast4);
        thunder.forcastimg[3] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.forecast5);
        thunder.forcastimg[4] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.thunder0);
        thunder.thunderimg[0] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.thunder1);
        thunder.thunderimg[1] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.thunder2);
        thunder.thunderimg[2] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.thunder3);
        thunder.thunderimg[3] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.thunder4);
        thunder.thunderimg[4] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.thunder5);
        thunder.thunderimg[5] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.thunder6);
        thunder.thunderimg[6] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.thunder7);
        thunder.thunderimg[7] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.thunder8);
        thunder.thunderimg[8] = BitmapFactory.decodeStream(is);

        is = res.openRawResource(R.raw.poison0);
        poison[0] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.poison1);
        poison[1] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.poison2);
        poison[2] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.poison3);
        poison[3] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.poison4);
        poison[4] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.poison5);
        poison[5] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.poison6);
        poison[6] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.poison7);
        poison[7] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.poison8);
        poison[8] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.poison9);
        poison[9] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.poison10);
        poison[10] = BitmapFactory.decodeStream(is);
        is = res.openRawResource(R.raw.poison11);
        poison[11] = BitmapFactory.decodeStream(is);
    }

    private void reset_status() {
        for (int i = 0 ; i != roles.size() ; ++i) {
            roles.get(i).HP = roles.get(i).UHP;
            roles.get(i).NCD = 0;
            roles.get(i).MCD = 0;
            roles.get(i).SCD[0] = roles.get(i).SCD[1] = 0;
            refresh_status(roles.get(i));
        }
        boss.hp = BHP;
        boss.cnt = 0;
        boss.status = 0;
    }

    private void doubleclickcnt() {
        if (lastleftcnt >= 0) {
            lastleftcnt++;
        }
        if (lastrightcnt >= 0) {
            lastrightcnt++;
        }
    }

    private void drawpotion(Canvas c, Paint p) {
        c.drawBitmap(potionblock, 84 - potionblock.getWidth()/2, sh - 350 - potionblock.getHeight()/2, p);
        if (user.bottle != null) {
            Bitmap logo = user.bottle.logo;
            c.drawBitmap(logo, 84 - logo.getWidth()/2, sh - 350 - logo.getHeight()/2, p);
        }
    }

    private void drawbox(Canvas c, Paint p) {
        c.drawBitmap(shine[(shinecnt/4)%3], bx - shine[(shinecnt/4)%3].getWidth()/2 , by - shine[(shinecnt/4)%3].getHeight()/2 ,p);
        c.drawBitmap(box,bx - box.getWidth()/2,by - box.getHeight()/2,p);
        shinecnt++;
        if (!land) {
            bx -= 4;
            dropspeed += 2;
            by += dropspeed;
            if (Math.abs(by - (760 - box.getHeight() / 2)) < 20 && dropspeed > 0) {
                dropspeed = -dropspeed + 4;
            }
            if (Math.abs(dropspeed) < 4 && Math.abs(by - (760 - box.getHeight() / 2)) < 3) {
                dropspeed = 0;
                by = 760 - box.getHeight() / 2;
                land = true;
            }
        }
    }
}
