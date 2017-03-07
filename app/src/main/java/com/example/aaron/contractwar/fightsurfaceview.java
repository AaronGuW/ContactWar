package com.example.aaron.contractwar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Aaron on 2015/4/13.
 */
public class fightsurfaceview extends Activity {
    private int nextround[];                    //0什么都不干 1移动 2普通攻击 3技能1 4技能2 5格挡
    private tornado tornadearray[];
    private shockwave shockwavearray[];
    public ArrayList<bullet> bullets1, bullets2;
    private Resources res;
    private int sw, sh;
    private int HP1  ,HP2;
    static private Bitmap p1skill[];
    static private Bitmap cdcover,background, returnbutton,victory, firework;
    static private Bitmap left,right,dleft,dright,host;
    private int lastleftcnt = -1, lastrightcnt = -1;

    private boolean isleftpressed = false, isrightpressed = false, ishosted = false;
    private boolean gameover = false, added = false;
    private int cd1[];

    /**4.16 添加**/
    ArrayList<bas_role> roles = new ArrayList<>();
    private ContactWar myApp;
    /** only for test ___ added on 4.27 **/
    private String me,enemy;

    /**4.16 添加**/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        enemy = intent.getStringExtra("enemy");
        res = this.getResources();

        ishosted = false;
        bullets1 = new ArrayList<>();
        bullets2 = new ArrayList<>();
        myApp =  (ContactWar)getApplication();
        sw = myApp.getScreenwidth();
        sh = myApp.getScreenheight();

        roles.add(myApp.getUser());
        roles.add(myApp.getMemhashmap().get(enemy));
        HP1 = roles.get(0).getHP();
        HP2 = roles.get(1).getHP();
        myApp.fighted(roles.get(1).getjob());
        roles.get(0).ishosted = false;
        roles.get(1).ishosted = true;
        roles.get(0).lastingAI = false;
        roles.get(1).lastingAI = true;

        me = myApp.getUser().getId();
        init_status();

        InputStream is = res.openRawResource(R.raw.tornade);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 1;

        //初始化地图龙卷风
        tornado.img = BitmapFactory.decodeStream(is,null,options);
        tornadearray = new tornado[2];
        tornadearray[0] = new tornado(0,760);
        tornadearray[1] = new tornado(0,760);

        //初始化地图冲击波
        shockwavearray = new shockwave[2];
        if (roles.get(0).getjob() == 0) {
            shockwavearray[0] = new shockwave(0,0,760,0);
            if (shockwave.img == null) {
                shockwave.img = new Bitmap[6];
                is = res.openRawResource(R.raw.swordkee0);
                shockwave.img[0] = BitmapFactory.decodeStream(is, null, options);
                is = res.openRawResource(R.raw.swordkee1);
                shockwave.img[1] = BitmapFactory.decodeStream(is, null, options);
                is = res.openRawResource(R.raw.swordkee2);
                shockwave.img[2] = BitmapFactory.decodeStream(is, null, options);
                is = res.openRawResource(R.raw.swordkee3);
                shockwave.img[3] = BitmapFactory.decodeStream(is, null, options);
                is = res.openRawResource(R.raw.swordkee4);
                shockwave.img[4] = BitmapFactory.decodeStream(is, null, options);
                is = res.openRawResource(R.raw.swordkee5);
                shockwave.img[5] = BitmapFactory.decodeStream(is, null, options);
                shockwave.re_img = new Bitmap[6];
                is = res.openRawResource(R.raw.reswordkee0);
                shockwave.re_img[0] = BitmapFactory.decodeStream(is, null, options);
                is = res.openRawResource(R.raw.reswordkee1);
                shockwave.re_img[1] = BitmapFactory.decodeStream(is, null, options);
                is = res.openRawResource(R.raw.reswordkee2);
                shockwave.re_img[2] = BitmapFactory.decodeStream(is, null, options);
                is = res.openRawResource(R.raw.reswordkee3);
                shockwave.re_img[3] = BitmapFactory.decodeStream(is, null, options);
                is = res.openRawResource(R.raw.reswordkee4);
                shockwave.re_img[4] = BitmapFactory.decodeStream(is, null, options);
                is = res.openRawResource(R.raw.reswordkee5);
                shockwave.re_img[5] = BitmapFactory.decodeStream(is, null, options);
            }
        }
        else if (roles.get(0).getjob() == 1) {
            shockwavearray[0] = new shockwave(1, 0, 760, 0);
            is = res.openRawResource(R.raw.mshockwave);
            shockwavearray[0].mimg = BitmapFactory.decodeStream(is, null, options);
        }
        if (roles.get(1).getjob() == 0) {
            shockwavearray[1] = new shockwave(0,0,760,0);
            if (shockwave.img == null) {
                shockwave.img = new Bitmap[6];
                is = res.openRawResource(R.raw.swordkee0);
                shockwave.img[0] = BitmapFactory.decodeStream(is, null, options);
                is = res.openRawResource(R.raw.swordkee1);
                shockwave.img[1] = BitmapFactory.decodeStream(is, null, options);
                is = res.openRawResource(R.raw.swordkee2);
                shockwave.img[2] = BitmapFactory.decodeStream(is, null, options);
                is = res.openRawResource(R.raw.swordkee3);
                shockwave.img[3] = BitmapFactory.decodeStream(is, null, options);
                is = res.openRawResource(R.raw.swordkee4);
                shockwave.img[4] = BitmapFactory.decodeStream(is, null, options);
                is = res.openRawResource(R.raw.swordkee5);
                shockwave.img[5] = BitmapFactory.decodeStream(is, null, options);
                shockwave.re_img = new Bitmap[6];
                is = res.openRawResource(R.raw.reswordkee0);
                shockwave.re_img[0] = BitmapFactory.decodeStream(is, null, options);
                is = res.openRawResource(R.raw.reswordkee1);
                shockwave.re_img[1] = BitmapFactory.decodeStream(is, null, options);
                is = res.openRawResource(R.raw.reswordkee2);
                shockwave.re_img[2] = BitmapFactory.decodeStream(is, null, options);
                is = res.openRawResource(R.raw.reswordkee3);
                shockwave.re_img[3] = BitmapFactory.decodeStream(is, null, options);
                is = res.openRawResource(R.raw.reswordkee4);
                shockwave.re_img[4] = BitmapFactory.decodeStream(is, null, options);
                is = res.openRawResource(R.raw.reswordkee5);
                shockwave.re_img[5] = BitmapFactory.decodeStream(is, null, options);
            }
        }
        else if (roles.get(1).getjob() == 1) {
            shockwavearray[1] = new shockwave(1, 0, 760, 0);
            is = res.openRawResource(R.raw.mshockwave);
            shockwavearray[1].mimg = BitmapFactory.decodeStream(is, null, options);
        }

        is = res.openRawResource(R.raw.victory);
        victory = BitmapFactory.decodeStream(is,null,options);
        is = res.openRawResource(R.raw.firework);
        firework = BitmapFactory.decodeStream(is,null,options);

        returnbutton = BitmapFactory.decodeResource(res,R.drawable.back);
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
            bitmaprecycle(roles.get(0));
            bitmaprecycle(roles.get(1));
        }
    }

    class FightThread extends Thread {
        public boolean isrun;
        private SurfaceHolder holder;

        public FightThread(SurfaceHolder holder) {
            this.holder = holder;
            isrun = true;
        }

        @Override
        public void run()
        {
            float hp1 = HP1, hp2 = HP2;
            bas_role p1 = roles.get(0),p2 = roles.get(1);
            String winner = "";
            while(isrun)
            {
                Canvas c = null;
                try
                {
                    synchronized (holder){
                        boolean shock1 = false,shock2 = false, dizzy1 = false, dizzy2 = false, blind1 = false, blind2 = false;

                        c = holder.lockCanvas();
                        Paint p = new Paint(); //创建画笔
                        p.setColor(Color.BLACK);
                        p.setAntiAlias(true);//去除锯齿
                        p.setFilterBitmap(true);//对位图进行滤波处理
                        p.setTextSize(40);

                        c.drawBitmap(background,0,0,p);
                        drawskilllogo(c,p,p1,p2);
                        drawpad(c,p);

                        if (!gameover) {
                            if (hp1 <= 0) {
                                hp1 = 0;
                                gameover = true;
                                winner = p2.getId();
                            }
                            if (hp2 <= 0) {
                                hp2 = 0;
                                gameover = true;
                                winner = p1.getId();
                            }
                        }

                        Paint realtimehp = new Paint();
                        realtimehp.setColor(Color.RED);
                        realtimehp.setAntiAlias(true);
                        realtimehp.setFilterBitmap(true);
                        realtimehp.setTextSize(60);
                        realtimehp.setTextAlign(Paint.Align.LEFT);
                        c.drawText(String.valueOf((int)hp1)+"/"+String.valueOf(HP1),10,100,realtimehp);
                        realtimehp.setTextAlign(Paint.Align.RIGHT);
                        c.drawText(String.valueOf((int)hp2)+"/"+String.valueOf(HP2),sw-10,100,realtimehp);

                        //简易血条的绘制
                        Paint Hp = new Paint();
                        Hp.setColor(Color.RED);
                        Hp.setAntiAlias(true);
                        Hp.setStyle(Paint.Style.FILL);
                        Rect hprect1 = new Rect(10, 10, sw/2 - 50, 50);
                        Rect rect1 = new Rect(10,10,10+(int)((hp1/HP1)*(float)(sw/2 - 60)),50);
                        Rect hprect2 = new Rect(sw/2 + 50, 10 , sw-10, 50);
                        Rect rect2 = new Rect(sw/2 + 50 + (int)((1-hp2/HP2)*(float)(sw/2 - 60)),10,sw-10,50);

                        Paint Hpboarder = new Paint();
                        Hpboarder.setColor(Color.BLACK);
                        Hpboarder.setAntiAlias(true);
                        Hpboarder.setStyle(Paint.Style.STROKE);
                        Hpboarder.setStrokeWidth((float)3.0);

                        c.drawRect(rect1, Hp);
                        c.drawRect(hprect1, Hpboarder);
                        c.drawRect(rect2, Hp);
                        c.drawRect(hprect2, Hpboarder);

                        if (!gameover) {

                            doubleclickcnt();   //法师双击瞬移计时

                            //两玩家转身判断
                            if ((p1.b + p1.x) / 2 > (p2.b + p2.x) / 2 && p1.headto == 1) {
                                p1.headto = -1;
                                p2.headto = 1;
                                p1.p = p1.x;
                                p2.p = p2.b;
                                if (Math.abs(p1.status) == 1) {
                                    p1.status = 0 - p1.status;
                                }
                                if (Math.abs(p2.status) == 1) {
                                    p2.status = 0 - p2.status;
                                }
                            } else if ((p1.b + p1.x) / 2 < (p2.b + p2.x) / 2 && p1.headto == -1) {
                                p1.headto = 1;
                                p2.headto = -1;
                                p1.p = p1.b;
                                p2.p = p2.x;
                                if (Math.abs(p1.status) == 1) {
                                    p1.status = 0 - p1.status;
                                }
                                if (Math.abs(p2.status) == 1) {
                                    p2.status = 0 - p2.status;
                                }
                            }

                            //法师飞弹伤害判定
                            for (int i = 0; i < bullets1.size(); ++i) {
                                if (bullets1.get(i).isdead) {
                                    bullets1.remove(i);
                                } else {
                                    if (Math.abs((p2.x + p2.b) / 2 - (bullets1.get(i).p + bullets1.get(i).dir * 30)) < 30) {
                                        bullets1.get(i).isdead = true;
                                        if (p2.shieldcnt == 0) {
                                            if (p2.getjob() != 0 || (p2.getjob() == 0 && p2.status != 2)) {
                                                hp2 -= (int) ((float) roles.get(0).getATK() * 0.6);
                                                p2.setHP((int) hp2);
                                                refresh_status(p2);
                                                p2.status = 6;
                                            } else {
                                                hp2 -= (int) ((float) p1.getATK() * 0.6 * 0.6);
                                                p2.setHP((int) hp2);
                                            }
                                        }
                                    }
                                    bullets1.get(i).draw(c, p);
                                    bullets1.get(i).logic();
                                }

                            }
                            for (int i = 0; i != bullets2.size(); ++i) {
                                if (bullets2.get(i).isdead)
                                    bullets2.remove(i);
                                else {
                                    if (Math.abs((p1.x + p1.b) / 2 - (bullets2.get(i).p + bullets2.get(i).dir * 30)) < 30) {
                                        bullets2.get(i).isdead = true;
                                        if (p1.shieldcnt == 0) {
                                            if (p1.getjob() != 0 || (p1.getjob() == 0 && p1.status != 2)) {
                                                hp1 -= (int) ((float) p2.getATK() * 0.6);
                                                p1.setHP((int) hp1);
                                                refresh_status(p1);
                                                p1.status = 6;
                                            } else {
                                                hp1 -= (int) ((float) p2.getATK() * 0.6 * 0.6);
                                                p1.setHP((int) hp1);
                                            }
                                        }
                                    }
                                    bullets2.get(i).draw(c, p);
                                    bullets2.get(i).logic();
                                }
                            }

                            //奶妈闪电护盾伤害判定
                            if (p1.shieldcnt > 0 && Math.abs(p1.p - p2.p) <= 103) {
                                hp2 -= HP2*0.0005;
                                p2.setHP((int)hp2);
                            }
                            if (p2.shieldcnt > 0 && Math.abs(p1.p - p2.p) <= 103) {
                                hp1 -= HP1*0.0005;
                                p1.setHP((int)hp1);
                            }

                            //龙卷风伤害判定，逻辑与绘制
                            if (tornadearray[0].valid) {
                                tornadearray[0].draw(c, p);
                                if (p2.shieldcnt == 0 && Math.abs((p2.p + p2.b) / 2 - tornadearray[0].center) < 360) {
                                    int dis = (p2.p + p2.b) / 2 - tornadearray[0].center;
                                    int offset;
                                    if (dis != 0) {
                                        offset = 7 * Math.abs(dis) / dis;
                                    } else {
                                        offset = 0;
                                    }
                                    p2.x -= offset;
                                    p2.p -= offset;
                                    p2.b -= offset;
                                }
                                if (Math.abs((p2.p + p2.b) / 2 - tornadearray[0].center) < 180) {
                                    int dmg = (int) ((float) p1.getATK() * 0.05);
                                    hp2 -= dmg;
                                    p2.setHP((int) hp2);
                                }
                                tornadearray[0].logic();
                            }
                            if (tornadearray[1].valid) {
                                tornadearray[1].draw(c, p);
                                if (p1.shieldcnt == 0 && Math.abs((p1.p + p1.b) / 2 - tornadearray[1].center) < 360) {
                                    int offset;
                                    int dis = (p1.p + p1.b) / 2 - tornadearray[1].center;
                                    if (dis != 0) {
                                        offset = 7 * Math.abs(dis) / dis;
                                    } else {
                                        offset = 0;
                                    }
                                    p1.x -= offset;
                                    p1.b -= offset;
                                    p1.p -= offset;
                                }
                                if (Math.abs((p1.p + p1.b) / 2 - tornadearray[1].center) < 180) {
                                    int dmg = (int) ((float) p2.getATK() * 0.05);
                                    hp1 -= dmg;
                                    p1.setHP((int) hp1);
                                }
                                tornadearray[1].logic();
                            }

                            //冲击波伤害判定、逻辑与绘制
                            if (p1.getjob() != 2 && shockwavearray[0].cnt != 0) {
                                shockwavearray[0].draw(c,p);
                                if (shockwavearray[0].validity && shockwavearray[0].collidedwith(p2)) {
                                    shockwavearray[0].validity = false;
                                    refresh_status(p2);
                                    if (p1.getjob() == 1) hp2 -= p1.getATK()*2;
                                    else hp2 -= ((float)p1.getATK())*2.5;
                                    p2.setHP((int)hp2);
                                    if (p1.getjob() == 0)
                                        p2.status = 6;
                                    else {
                                        p2.status = 7;
                                        p2.shock_dis = 40;
                                    }
                                }
                                shockwavearray[0].logic();
                            }
                            if (p2.getjob() != 2 && shockwavearray[1].cnt != 0) {
                                shockwavearray[1].draw(c,p);
                                if (shockwavearray[1].validity && shockwavearray[1].collidedwith(p1)) {
                                    shockwavearray[1].validity = false;
                                    refresh_status(p1);
                                    if (p2.getjob() == 1) hp1 -= p2.getATK()*2;
                                    else hp1 -= ((float)p2.getATK())*2.5;
                                    p1.setHP((int)hp1);
                                    if (p2.getjob() == 0)
                                        p1.status = 6;
                                    else {
                                        p1.status = 7;
                                        p1.shock_dis = 40;
                                    }
                                }
                                shockwavearray[1].logic();
                            }

                            //有源伤害判定
                            if (p1.atk_validity && p2.atk_validity && p1.atk_activated && p2.atk_activated  && Math.abs(p1.p - p2.p) < 50) {
                                if (p1.shieldcnt == 0) {
                                    switch (p2.status) {
                                        case 3:
                                            hp1 -= p2.getATK();
                                            break;
                                        case 4:
                                            hp1 -= p2.getATK() * 2;
                                            shock1 = p2.getjob() == 1;
                                            dizzy1 = p2.getjob() == 0;
                                            break;
                                        case 5:
                                            hp1 -= (int) ((float) (p2.getATK()) * 2.5);
                                            blind1 = p2.getjob() == 2;
                                            break;
                                        default:
                                            hp1 -= p2.getATK();
                                    }
                                    if (shock1) {
                                        p1.status = 7;
                                        p1.shock_dis = 40;
                                    } else if (dizzy1) p1.status = 8;
                                    else if (blind1) p1.blind_cnt = 180;
                                    else p1.status = 6;
                                    refresh_status(p1);
                                    p1.setHP((int) hp1);
                                }
                                if (p2.shieldcnt == 0) {
                                    switch (p1.status) {
                                        case 3:
                                            hp2 -= p1.getATK();
                                            break;
                                        case 4:
                                            hp2 -= p1.getATK() * 2;
                                            shock2 = p1.getjob() == 1;
                                            dizzy2 = p1.getjob() == 0;
                                            break;
                                        case 5:
                                            hp2 -= (int) ((float) (p1.getATK()) * 2.5);
                                            blind2 = p1.getjob() == 2;
                                            break;
                                        default:
                                            hp2 -= p1.getATK();
                                            p2.natk_cnt = 0;
                                    }
                                    if (shock2) {
                                        p2.status = 7;
                                        p2.shock_dis = 40;
                                    } else if (dizzy2) p2.status = 8;
                                    else if (blind2) p2.blind_cnt = 180;
                                    else p2.status = 6;
                                    refresh_status(p2);
                                    p2.setHP((int) hp2);
                                }
                            } else if (p1.atk_validity && p1.atk_activated && p2.shieldcnt == 0 && Math.abs(p1.p - p2.p) < 50) {
                                int dmg;
                                switch (p1.status) {
                                    case 3:
                                        dmg = p1.getATK();
                                        break;
                                    case 4:
                                        dmg = p1.getATK() * 2;
                                        shock2 = p1.getjob() == 1;
                                        dizzy2 = p1.getjob() == 0;
                                        break;
                                    case 5:
                                        dmg = (int) ((float) (p1.getATK()) * 2.5);
                                        blind2 = p1.getjob() == 2;
                                        break;
                                    default:
                                        dmg = p1.getATK();
                                }
                                if (p2.status == 2 && p2.getjob() == 0) {
                                    hp2 -= (int) ((float) dmg * 0.6);
                                    p2.setHP((int) hp2);
                                } else {
                                    hp2 -= dmg;
                                    p2.setHP((int) hp2);
                                    refresh_status(p2);
                                    if (shock2) {
                                        p2.status = 7;
                                        p2.shock_dis = 40;
                                    } else if (dizzy2) {
                                        p2.status = 8;
                                    } else if (blind2) {
                                        p2.blind_cnt = 180;
                                    } else {
                                        p2.status = 6;
                                    }
                                }
                                p1.atk_validity = false;
                            } else if (p2.atk_validity && p2.atk_activated && p1.shieldcnt == 0 && Math.abs(p1.p - p2.p) < 50) {
                                int dmg;
                                switch (p2.status) {
                                    case 3:
                                        dmg = p2.getATK();
                                        break;
                                    case 4:
                                        dmg = p2.getATK() * 2;
                                        shock1 = p2.getjob() == 1;
                                        dizzy1 = p2.getjob() == 0;
                                        break;
                                    case 5:
                                        dmg = (int) ((float) (p2.getATK()) * 2.5);
                                        blind1 = p2.getjob() == 2;
                                        break;
                                    default:
                                        dmg = p2.getATK();
                                }
                                if (p1.status == 2 && p1.getjob() == 0) {
                                    hp1 -= (int) ((float) dmg * 0.6);
                                    p1.setHP((int) hp1);
                                } else {
                                    hp1 -= dmg;
                                    p1.setHP((int) hp1);
                                    refresh_status(p1);
                                    if (shock1) {
                                        p1.status = 7;
                                        p1.shock_dis = 40;
                                    } else if (dizzy1) {
                                        p1.status = 8;
                                    } else if (blind1) {
                                        p1.blind_cnt = 180;
                                    } else {
                                        p1.status = 6;
                                    }
                                }
                                p2.atk_validity = false;
                            }
                            /*Log.i("nextround of p1", String.valueOf(nextround[0]));
                            Log.i("status of p1", String.valueOf(p1.status));*/

                            //法师普攻的能量球加入
                            if (p1.getjob() == 1 && p1.status == 3 && p1.natk_cnt == 37) {
                                bullets1.add(new bullet(p1.headto == 1?p1.p + 15:p1.p - 15, p1.y - 240 + 30, p1.headto == 1?p1.p+15+120:p1.p-15-120, p1.headto));
                            }
                            if (p2.getjob() == 1 && p2.status == 3 && p2.natk_cnt == 37) {
                                bullets2.add(new bullet(p2.headto == 1?p2.p + 15:p2.p - 15, p2.y - 240 + 30, p2.headto == 1?p2.p+15+120:p2.p-15-120, p2.headto));
                            }

                            //奶妈回血
                            if (p1.getjob() == 2 && p1.status == 2 && p1.mcnt == 8) {
                                hp1 += (float)HP1*0.05;
                                p1.setHP((int)hp1);
                            }
                            if (p2.getjob() == 2 && p2.status == 2 && p2.mcnt == 8) {
                                hp2 += (float)HP2*0.05;
                                p2.setHP((int)hp2);
                            }

                            //法师技能二的龙卷风加入
                            if (p1.getjob() == 1 && p1.status == 5 && p1.sk_cnt[1] == 28) {
                                tornadearray[0].valid = true;
                                tornadearray[0].x = p1.p + 480 * p1.headto;
                                tornadearray[0].center = tornadearray[0].x + 150;
                                p1.SCD[1] = 480;
                            }
                            if (p2.getjob() == 1 && p2.status == 5 && p2.sk_cnt[1] == 28) {
                                tornadearray[1].valid = true;
                                tornadearray[1].x = p2.p + 480 * p2.headto;
                                tornadearray[1].center = tornadearray[1].x + 150;
                                p2.SCD[1] = 480;
                            }

                            //法师技能一的冲击波加入
                            if (p1.getjob() == 1 && p1.status == 4 && p1.sk_cnt[0] == 24) {
                                shockwavearray[0].validity = true;
                                shockwavearray[0].x = p1.p + 88*p1.headto;
                                shockwavearray[0].cnt = 52;
                                p1.SCD[0] = 300;
                            }
                            if (p2.getjob() == 1 && p2.status == 4 && p2.sk_cnt[0] == 24) {
                                shockwavearray[1].validity = true;
                                shockwavearray[1].x = p2.p + 88*p2.headto;
                                shockwavearray[1].cnt = 52;
                                p2.SCD[0] = 300;
                            }

                            //战士技能二的冲击波加入
                            if (p1.getjob() == 0 && p1.status == 5 && p1.sk_cnt[1] == 8) {
                                shockwavearray[0].validity = true;
                                shockwavearray[0].x = p1.p + 50*p1.headto;
                                shockwavearray[0].cnt = 36;
                                shockwavearray[0].dir = p1.headto;
                                p1.SCD[1] = 360;
                            }
                            if (p2.getjob() == 0 && p2.status == 5 && p2.sk_cnt[1] == 8) {
                                shockwavearray[1].validity = true;
                                shockwavearray[1].x = p2.p + 50*p2.headto;
                                shockwavearray[1].cnt = 36;
                                shockwavearray[1].dir = p2.headto;
                                p2.SCD[1] = 360;
                            }


                        } else {
                            Paint resp = new Paint();
                            resp.setTextAlign(Paint.Align.CENTER);
                            resp.setColor(Color.RED);
                            resp.setTextSize(80);
                            //c.drawText("恭喜 "+ winner +" 获胜！",sw/2,200,resp);
                            c.drawBitmap(returnbutton, 1500, 760, resp);
                            if (winner == me) {
                                c.drawBitmap(victory, sw / 2 - victory.getWidth() / 2, sh / 2 - victory.getHeight() - 90, resp);
                                c.drawBitmap(firework, sw/2 - 2*victory.getWidth(), sh/2 - 100, resp);
                                c.drawBitmap(firework, sw/2 + victory.getWidth(), sh/2 + 100, resp);
                            }
                            if (!added) {
                                roles.get(0).addexp(winner == me ? 10 : 2);
                                roles.get(0).add_score(winner == me ? 10 : 2);
                                added = true;
                            }
                        }
                        roles.get(0).draw(c,p);
                        roles.get(1).draw(c,p);

                        if (!gameover) {
                            takeaction();
                            if (nextround[0] != 0) {
                                roles.get(0).status = nextround[0];
                                roles.get(0).lastingAI = Math.abs(nextround[0]) == 1;
                            } else {
                                roles.get(0).logic();
                            }
                            if (nextround[1] != 0) {
                                roles.get(1).status = nextround[1];
                            } else {
                                roles.get(1).logic();
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

    private void init_status() {
        nextround = new int[2];
        nextround[0] = nextround[1] = 0;
        background = BitmapFactory.decodeResource(res,R.drawable.bg);
        dleft = BitmapFactory.decodeResource(res,R.drawable.dleft);
        left = BitmapFactory.decodeResource(res,R.drawable.left);
        dright = BitmapFactory.decodeResource(res,R.drawable.dright);
        right = BitmapFactory.decodeResource(res,R.drawable.right);
        host = BitmapFactory.decodeResource(res,R.drawable.host);
        p1skill = new Bitmap[4];
        cd1 = new int[4];
        InputStream is1, is2;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 1;
        switch (roles.get(0).getjob()) {
            case 0:
                is1 = res.openRawResource(R.raw.warrioractions);
                is2 = res.openRawResource(R.raw.re_warrioraction);
                p1skill[0] = BitmapFactory.decodeResource(res,R.drawable.wnatklogo);
                p1skill[1] = BitmapFactory.decodeResource(res,R.drawable.kicklogo);
                p1skill[2] = BitmapFactory.decodeResource(res,R.drawable.shenglonglogo);
                p1skill[3] = BitmapFactory.decodeResource(res,R.drawable.blocklogo);
                break;
            case 1:
                is1 = res.openRawResource(R.raw.magicianactions);
                is2 = res.openRawResource(R.raw.re_magicianactions);
                p1skill[0] = BitmapFactory.decodeResource(res,R.drawable.mnatklogo);
                p1skill[1] = BitmapFactory.decodeResource(res,R.drawable.baologo);
                p1skill[2] = BitmapFactory.decodeResource(res,R.drawable.tornadologo);
                p1skill[3] = BitmapFactory.decodeResource(res,R.drawable.flashlogo);
                break;
            case 2:
                is1 = res.openRawResource(R.raw.doctoraction);
                is2 = res.openRawResource(R.raw.re_doctoraction);
                p1skill[0] = BitmapFactory.decodeResource(res,R.drawable.dnatklogo);
                p1skill[1] = BitmapFactory.decodeResource(res,R.drawable.shieldlogo);
                p1skill[2] = BitmapFactory.decodeResource(res,R.drawable.blindlogo);
                p1skill[3] = BitmapFactory.decodeResource(res,R.drawable.srecoverlogo);
                break;
            default:
                is1 = res.openRawResource(R.raw.warrioractions);
                is2 = res.openRawResource(R.raw.re_warrioraction);
                p1skill[0] = BitmapFactory.decodeResource(res,R.drawable.wnatklogo);
                p1skill[1] = BitmapFactory.decodeResource(res,R.drawable.kicklogo);
                p1skill[2] = BitmapFactory.decodeResource(res,R.drawable.shenglonglogo);
                p1skill[3] = BitmapFactory.decodeResource(res,R.drawable.blocklogo);
        }
        roles.get(0).character_img = BitmapFactory.decodeStream(is1, null, options);
        roles.get(0).re_img = BitmapFactory.decodeStream(is2, null, options);

        switch (roles.get(1).getjob()) {
            case 0:
                is2 = res.openRawResource(R.raw.re_warrioraction);
                is1 = res.openRawResource(R.raw.warrioractions);
                break;
            case 1:
                is2 = res.openRawResource(R.raw.re_magicianactions);
                is1 = res.openRawResource(R.raw.magicianactions);
                break;
            case 2:
                is2 = res.openRawResource(R.raw.re_doctoraction);
                is1 = res.openRawResource(R.raw.doctoraction);
                break;
            default:
                is2 = res.openRawResource(R.raw.re_warrioraction);
                is1 = res.openRawResource(R.raw.warrioractions);
        }
        roles.get(1).character_img = BitmapFactory.decodeStream(is1, null, options);
        roles.get(1).re_img = BitmapFactory.decodeStream(is2,null,options);
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

        roles.get(0).x = 100;
        roles.get(0).y = 760;
        roles.get(0).headto = 1;
        roles.get(0).status = 0;
        switch (roles.get(0).getjob()) {
            case 0:
                roles.get(0).b = 100 + 170;
                roles.get(0).p = 100 + 170;
                break;
            case 1:
                roles.get(0).b = 100 + 160;
                roles.get(0).p = 100 + 160;
                break;
            case 2:
                roles.get(0).b = 100 + 110;
                roles.get(0).p = 100 + 110;
                break;
        }

        roles.get(1).headto = -1;
        roles.get(1).b = sw - 100;
        roles.get(1).status = 0;
        roles.get(1).y = 760;
        switch (roles.get(1).getjob()) {
            case 0:
                roles.get(1).x = sw - 100 - 170;
                roles.get(1).p = sw - 100 - 170;
                break;
            case 1:
                roles.get(1).x = sw - 100 - 160;
                roles.get(1).p = sw - 100 - 160;
                break;
            case 2:
                roles.get(1).x = sw - 100 - 110;
                roles.get(1).p = sw - 100 - 110;
                break;
        }

        bullet.img = BitmapFactory.decodeResource(res,R.drawable.bullet);
        bullet.sw = sw;

        if (roles.get(0).getjob() == 0 || roles.get(1).getjob() == 0) {
            //is1 = res.openRawResource(R.raw.shockwave);
            //bas_role.shockwave = BitmapFactory.decodeStream(is1, null, options);
            is1 = res.openRawResource(R.raw.block);
            bas_role.block = BitmapFactory.decodeStream(is1, null, options);
        }
        if (roles.get(0).getjob() == 1 || roles.get(1).getjob() == 1) {
            is1 = res.openRawResource(R.raw.flash);
            bas_role.flash = BitmapFactory.decodeStream(is1, null, options);
            //is1 = res.openRawResource(R.raw.mshockwave);
            //bas_role.mshockwave = BitmapFactory.decodeStream(is1, null, options);
        }
        if (roles.get(0).getjob() == 2 || roles.get(1).getjob() == 2) {
            is1 = res.openRawResource(R.raw.shield);
            bas_role.shield = BitmapFactory.decodeStream(is1, null, options);
        }
        is1 = res.openRawResource(R.raw.dizzy);
        bas_role.dizzy = BitmapFactory.decodeStream(is1, null, options);
        bas_role.sw = sw;
    }

    private void bitmaprecycle(bas_role player){
        player.character_img.recycle();
        player.character_img = null;
        player.re_img.recycle();
        player.re_img = null;
        if (player.flash != null) {
            player.flash.recycle();
            player.flash = null;
        }
        if (player.dizzy != null) {
            player.dizzy.recycle();
            player.dizzy = null;
        }
        /*if (player.shockwave != null) {
            player.shockwave.recycle();
            player.shockwave = null;
        }
        if (player.mshockwave != null) {
            player.mshockwave.recycle();
            player.mshockwave = null;
        }*/
        if (player.shield != null) {
            player.shield.recycle();
            player.shield = null;
        }
        if (player.block != null) {
            player.block.recycle();
            player.block = null;
        }
        System.gc();
    }

    private void drawskilllogo(Canvas c, Paint p, bas_role p1, bas_role p2) {
        int head = 300,interval = 30,width = 150;
        int offset1[] = new int[4]; //offset2[] = new int[4];

        //半透明遮盖的偏移量
        offset1[0] = 132 - (int)((float)p1.NCD/(float)cd1[0]*132.0);
        offset1[1] = 132 - (int)((float)p1.SCD[0]/(float)cd1[1]*132.0);
        offset1[2] = 132 - (int)((float)p1.SCD[1]/(float)cd1[2]*132.0);
        offset1[3] = 132 - (int)((float)p1.MCD/(float)cd1[3]*132.0);
        /*offset2[0] = 132 - (int)((float)p2.NCD/(float)cd2[0]*132.0);
        offset2[1] = 132 - (int)((float)p2.SCD[0]/(float)cd2[1]*132.0);
        offset2[2] = 132 - (int)((float)p2.SCD[1]/(float)cd2[2]*132.0);
        offset2[3] = 132 - (int)((float)p2.MCD/(float)cd2[3]*132.0);*/

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
    }

    private void reset_status() {
        bas_role p1 = roles.get(0), p2 = roles.get(1);
        p1.setHP(HP1);
        p2.setHP(HP2);
        p1.status = p2.status = 0;
        p1.NCD = p2.NCD = 0;
        p1.SCD[0] = p1.SCD[1] = p2.SCD[0] = p2.SCD[1] = 0;
        p1.MCD = p2.MCD = 0;
        p1.shieldcnt = p2.shieldcnt = 0;
        refresh_status(p1);
        refresh_status(p2);
    }

    private void takeaction() {
        bas_role p1 = roles.get(0), p2 = roles.get(1);
        Random random = new Random();
        boolean flag1 = true, flag2 = true;
        if (ishosted) {
            if (p1.blind_cnt == 0) {
                switch (p1.getjob()) {
                    //战士AI
                    case 0:
                        if (p1.status == 0) {
                            if (bullets2.size() != 0) {
                                for (int i = 0; i != bullets2.size(); ++i) {
                                    if (Math.abs(p1.p - bullets2.get(i).p) <= 120) {
                                        int r = Math.abs(random.nextInt()) % 10;
                                        if (r < 8) {
                                            nextround[0] = 2;
                                            flag1 = false;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (flag1 && Math.abs(p1.p - p2.p) <= 41) {
                                if (p2.status == 8 || p2.status == 6) {
                                    if (p1.SCD[1] == 0) nextround[0] = 5;
                                    else if (p1.SCD[0] == 0) nextround[0] = 4;
                                    else if (p1.NCD == 0) nextround[0] = 3;
                                    else nextround[0] = 1;
                                    flag1 = false;
                                } else {
                                    int aors = Math.abs(random.nextInt()) % 10;
                                    if (aors < 2) {
                                        nextround[0] = 2;
                                        flag1 = false;
                                    } else if (aors < 4) {
                                        nextround[0] = -1;
                                        flag1 = false;
                                    } else if (aors < 7) {
                                        if (p1.NCD == 0) {
                                            nextround[0] = 3;
                                            flag1 = false;
                                        }
                                    } else {
                                        if (p1.SCD[0] != 0 && p1.SCD[1] == 0) {
                                            nextround[0] = 5;
                                            flag1 = false;
                                        } else if (p1.SCD[1] != 0 && p1.SCD[0] == 0) {
                                            nextround[0] = 4;
                                            flag1 = false;
                                        } else if (p1.SCD[0] == 0 && p1.SCD[1] == 0) {
                                            int a = Math.abs(random.nextInt()) % 2;
                                            if (a == 0)
                                                nextround[0] = 4;
                                            else
                                                nextround[0] = 5;
                                            flag1 = false;
                                        }
                                    }
                                }
                            }
                            if (flag1)
                                nextround[0] = 1;
                        } else if (Math.abs(p1.status) == 1) {
                            if (bullets2.size() != 0) {
                                for (int i = 0; i != bullets2.size(); ++i) {
                                    if (Math.abs(p1.p - bullets2.get(i).p) <= 120) {
                                        int r = Math.abs(random.nextInt()) % 10;
                                        if (r < 8) {
                                            nextround[0] = 2;
                                            refresh_status(p1);
                                            p1.status = 0;
                                            flag1 = false;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (flag1 && Math.abs(p1.p - p2.p) <= 41 && p2.status != 8 && p2.status != 6) {
                                int aors = Math.abs(random.nextInt()) % 10;
                                if (aors < 2) {
                                    nextround[0] = 2;
                                    flag1 = false;
                                } else if (aors < 4) {
                                    nextround[0] = -1;
                                    flag1 = false;
                                } else if (aors < 7) {
                                    if (p1.NCD == 0) {
                                        nextround[0] = 3;
                                        flag1 = false;
                                    }
                                } else {
                                    if (p1.SCD[0] != 0 && p1.SCD[1] == 0) {
                                        nextround[0] = 5;
                                        flag1 = false;
                                    } else if (p1.SCD[1] != 0 && p1.SCD[0] == 0) {
                                        nextround[0] = 4;
                                        flag1 = false;
                                    } else if (p1.SCD[0] == 0 && p1.SCD[1] == 0) {
                                        int a = Math.abs(random.nextInt()) % 2;
                                        if (a == 0)
                                            nextround[0] = 4;
                                        else
                                            nextround[0] = 5;
                                        flag1 = false;
                                    }
                                }
                            }
                            if (flag1)
                                nextround[0] = 0;
                            else
                                p1.mv_cnt = 0;
                        } else {
                            nextround[0] = 0;
                        }
                        break;

                    //法师AI
                    case 1:
                        if (p1.status == 0) {
                            if (bullets2.size() != 0) {
                                for (int i = 0; i != bullets2.size(); ++i) {
                                    if (p1.headto != bullets2.get(i).dir && Math.abs(p1.p - bullets2.get(i).p) <= 105) {
                                        if (p1.MCD == 0) {
                                            nextround[0] = 2;
                                            flag1 = false;
                                            break;
                                        }
                                    }
                                }
                            }
                            //龙卷风释放判定
                            if (flag1) {
                                if ((p1.headto == 1 && p2.p - p1.p > 120 && p2.p - p1.p < 500) || (p1.headto == -1 && p1.p - p2.p > 120 && p1.p - p2.p < 500)) {
                                    if (p1.SCD[1] == 0) {
                                        nextround[0] = 5;
                                        flag1 = false;
                                    }
                                }
                            }
                            //判定爆
                            if (flag1) {
                                if (((p1.headto == 1 && p2.p - p1.p < 105) || (p1.headto == -1 && p1.p - p2.p < 105)) && p1.SCD[0] == 0) {
                                    nextround[0] = 4;
                                    flag1 = false;
                                }
                            }
                            //判定瞬移
                            if (flag1) {
                                if ((p1.headto == 1 && p2.p - p1.p < 55) || (p1.headto == -1 && p1.p - p2.p < 105)) {
                                    if (p1.MCD == 0) {
                                        int r = Math.abs(random.nextInt()) % 2;
                                        nextround[0] = r == 0 ? 2 : -2;
                                        flag1 = false;
                                    }
                                }
                            }
                            //判定普攻或移动
                            if (flag1) {
                                if (p1.NCD != 0) {
                                    if ((p1.headto == 1 && p2.p - p1.p < 200) || (p1.headto == -1 && p1.p - p2.p < 200)) {
                                        nextround[0] = -1;
                                    } else {
                                        nextround[0] = 1;
                                    }
                                } else {
                                    nextround[0] = 3;
                                }
                            }
                        } else {
                            nextround[0] = 0;
                        }
                        break;

                    //奶妈AI
                    case 2:
                        boolean threatened = false;
                        if (p1.status == 0) {
                            if (bullets2.size() != 0) {
                                for (int i = 0; i != bullets2.size(); ++i) {
                                    if (Math.abs(bullets2.get(i).p - p1.p) < 360) {
                                        threatened = true;
                                        break;
                                    }
                                }
                            }
                            if (flag1 && Math.abs(p2.p - p1.p) < 240 && p1.SCD[1] == 0) {
                                nextround[0] = 5;
                                flag1 = false;
                            }
                            if (flag1 && (Math.abs(p2.p - p1.p) < 480 || threatened) && p1.SCD[0] == 0) {
                                nextround[0] = 4;
                                flag1 = false;
                            }
                            if (flag1 && (float) p1.getHP() / (float) HP1 < 0.95 && p1.MCD == 0) {
                                nextround[0] = 2;
                                flag1 = false;
                            }
                            if (flag1 && Math.abs(p1.p - p2.p) < 120) {
                                if (p1.NCD == 0) {
                                    nextround[0] = 3;
                                } else {
                                    nextround[0] = -1;
                                }
                                flag1 = false;
                            }
                            if (flag1 && Math.abs(p1.p - p2.p) > 360) {

                                nextround[0] = 1;
                                flag1 = false;
                            }
                            if (flag1) {
                                nextround[0] = 1;

                            }
                        } else if (Math.abs(p1.status) == 1) {
                            if (p2.status == 6 || p2.status == 8) {
                                if (p1.SCD[0] == 0 && Math.abs(p2.p - p1.p) <= 480) {
                                    nextround[0] = 4;
                                } else if (p1.NCD == 0 && Math.abs(p2.p - p1.p) <= 120) {
                                    nextround[0] = 3;
                                }
                            } else if (p1.NCD == 0 && Math.abs(p2.p - p1.p) <= 120) {
                                nextround[0] = 3;
                            } else {
                                nextround[0] = 0;
                            }
                        } else {
                            nextround[0] = 0;
                        }
                        break;
                }
            } else if (p1.blind_cnt % 20 == 1) {
                int action = Math.abs(random.nextInt()) % 3;
                int dir = Math.abs(random.nextInt()) % 2;
                refresh_status(p1);
                switch (action) {
                    case 0:
                        nextround[0] = 0;
                        break;
                    case 1:
                        nextround[0] = dir == 0 ? 1 : -1;
                        break;
                    case 2:
                        switch (p1.getjob()) {
                            case 0:
                                nextround[0] = 2;
                                break;
                            case 1:

                                if (p1.MCD == 0)
                                    nextround[0] = dir == 0 ? 2 : -2;
                                else
                                    nextround[0] = dir == 0 ? 1 : -1;
                                break;
                            case 2:
                                if (p1.MCD == 0)
                                    nextround[0] = 2;
                                else
                                    nextround[0] = dir == 0 ? 1 : -1;
                                break;
                        }
                }
            }
        } else {
            nextround[0] = 0;
        }

        if (p2.blind_cnt == 0) {
            switch (p2.getjob()) {
                case 0:
                    if (p2.status == 0) {
                        if (bullets1.size() != 0) {
                            for (int i = 0; i != bullets1.size(); ++i) {
                                if (Math.abs(p2.p - bullets1.get(i).p) <= 120) {
                                    int r = Math.abs(random.nextInt()) % 10;
                                    if (r < 8) {
                                        nextround[1] = 2;
                                        flag2 = false;
                                        break;
                                    }
                                }
                            }
                        }
                        if (flag2 && Math.abs(p1.p - p2.p) <= 41) {
                            if (p1.status == 8 || p1.status == 6) {
                                if (p2.SCD[1] == 0) nextround[1] = 5;
                                else if (p2.SCD[0] == 0) nextround[1] = 4;
                                else if (p2.NCD == 0) nextround[1] = 3;
                                else nextround[1] = 1;
                                flag2 = false;
                            } else {
                                int aors = Math.abs(random.nextInt()) % 10;
                                if (aors < 2) {
                                    nextround[1] = 2;
                                    flag2 = false;
                                } else if (aors < 4) {
                                    nextround[1] = -1;
                                    flag2 = false;
                                } else if (aors < 7) {
                                    if (p2.NCD == 0) {
                                        nextround[1] = 3;
                                        flag2 = false;
                                    }
                                } else {
                                    if (p2.SCD[0] != 0 && p2.SCD[1] == 0) {
                                        nextround[1] = 5;
                                        flag2 = false;
                                    } else if (p2.SCD[1] != 0 && p2.SCD[0] == 0) {
                                        nextround[1] = 4;
                                        flag2 = false;
                                    } else if (p2.SCD[0] == 0 && p2.SCD[1] == 0) {
                                        int a = Math.abs(random.nextInt()) % 2;
                                        if (a == 0)
                                            nextround[1] = 4;
                                        else
                                            nextround[1] = 5;
                                        flag2 = false;
                                    }
                                }
                            }
                        }
                        if (flag2)
                            nextround[1] = 1;
                    } else if (Math.abs(p2.status) == 1) {
                        if (bullets1.size() != 0) {
                            for (int i = 0; i != bullets1.size(); ++i) {
                                if (Math.abs(p2.p - bullets1.get(i).p) <= 120) {
                                    int r = Math.abs(random.nextInt()) % 10;
                                    if (r < 8) {
                                        nextround[1] = 2;
                                        refresh_status(p2);
                                        p2.status = 0;
                                        flag2 = false;
                                        break;
                                    }
                                }
                            }
                        }
                        if (flag2 && Math.abs(p1.p - p2.p) <= 41 && p1.status != 8 && p1.status != 6) {
                            int aors = Math.abs(random.nextInt()) % 10;
                            if (aors < 2) {
                                nextround[1] = 2;
                                flag2 = false;
                            } else if (aors < 4) {
                                nextround[1] = -1;
                                flag2 = false;
                            } else if (aors < 7) {
                                if (p2.NCD == 0) {
                                    nextround[1] = 3;
                                    flag2 = false;
                                }
                            } else {
                                if (p2.SCD[0] != 0 && p2.SCD[1] == 0) {
                                    nextround[1] = 5;
                                    flag2 = false;
                                } else if (p2.SCD[1] != 0 && p2.SCD[0] == 0) {
                                    nextround[1] = 4;
                                    flag2 = false;
                                } else if (p2.SCD[0] == 0 && p2.SCD[1] == 0) {
                                    int a = Math.abs(random.nextInt()) % 2;
                                    if (a == 0)
                                        nextround[1] = 4;
                                    else
                                        nextround[1] = 5;
                                    flag2 = false;
                                }
                            }
                        }
                        if (flag2)
                            nextround[1] = 0;
                        else
                            p2.mv_cnt = 0;
                    } else {
                        nextround[1] = 0;
                    }
                    break;
                case 1:
                    if (p2.status == 0) {
                        if (bullets1.size() != 0) {
                            for (int i = 0; i != bullets1.size(); ++i) {
                                if (p2.headto != bullets1.get(i).dir && Math.abs(p2.p - bullets1.get(i).p) <= 105) {
                                    if (p2.MCD == 0) {
                                        nextround[1] = 2;
                                        flag2 = false;
                                        break;
                                    }
                                }
                            }
                        }
                        //龙卷风释放判定
                        if (flag2) {
                            if ((p2.headto == 1 && p1.p - p2.p > 120 && p1.p - p2.p < 500) || (p2.headto == -1 && p2.p - p1.p > 120 && p2.p - p1.p < 500)) {
                                if (p2.SCD[1] == 0) {
                                    nextround[1] = 5;
                                    flag2 = false;
                                }
                            }
                        }
                        //判定爆
                        if (flag2) {
                            if (((p2.headto == 1 && p1.p - p2.p < 105) || (p2.headto == -1 && p2.p - p1.p < 105)) && p2.SCD[0] == 0) {
                                nextround[1] = 4;
                                flag2 = false;
                            }
                        }
                        //判定瞬移
                        if (flag2) {
                            if ((p2.headto == 1 && p1.p - p2.p < 55) || (p2.headto == -1 && p2.p - p1.p < 105)) {
                                if (p2.MCD == 0) {
                                    int r = Math.abs(random.nextInt()) % 2;
                                    nextround[1] = r == 0 ? 2 : -2;
                                    flag2 = false;
                                }
                            }
                        }
                        //判定普攻或移动
                        if (flag2) {
                            if (p2.NCD != 0) {
                                if ((p2.headto == 1 && p1.p - p2.p < 200) || (p2.headto == -1 && p2.p - p1.p < 200)) {
                                    nextround[1] = -1;
                                } else {
                                    nextround[1] = 1;
                                }
                            } else {
                                nextround[1] = 3;
                            }
                        }
                    } else {
                        nextround[1] = 0;
                    }
                    break;

                //奶妈AI
                case 2:
                    boolean threatened = false;
                    if (p2.status == 0) {
                        if (bullets1.size() != 0) {
                            for (int i = 0; i != bullets1.size(); ++i) {
                                if (Math.abs(bullets1.get(i).p - p2.p) < 360) {
                                    threatened = true;
                                    break;
                                }
                            }
                        }
                        if (flag2 && Math.abs(p2.p - p1.p) < 240 && p2.SCD[1] == 0) {
                            nextround[1] = 5;
                            flag2 = false;
                        }
                        if (flag2 && (Math.abs(p2.p - p1.p) < 480 || threatened) && p2.SCD[0] == 0) {
                            nextround[1] = 4;
                            flag2 = false;
                        }
                        if (flag2 && (float) p2.getHP() / (float) HP2 < 0.95 && p2.MCD == 0) {
                            nextround[1] = 2;
                            flag2 = false;
                        }
                        if (flag2 && Math.abs(p1.p - p2.p) < 120) {
                            if (p2.NCD == 0) {
                                nextround[1] = 3;
                            } else {
                                nextround[1] = -1;
                            }
                            flag2 = false;
                        }
                        if (flag2 && Math.abs(p1.p - p2.p) > 360) {
                            nextround[1] = 1;
                        }
                        if (flag2)
                            nextround[1] = 1;
                    } else if (Math.abs(p2.status) == 1) {
                        if (p1.status == 6 || p1.status == 8) {
                            if (p2.SCD[0] == 0 && Math.abs(p2.p - p1.p) <= 480) {
                                nextround[1] = 4;
                            } else if (p2.NCD == 0 && Math.abs(p2.p - p1.p) <= 120) {
                                nextround[1] = 3;
                            }
                        } else if (p2.NCD == 0 && Math.abs(p2.p - p1.p) <= 120) {
                            nextround[1] = 3;
                        } else {
                            nextround[1] = 0;
                        }
                    } else {
                        nextround[1] = 0;
                    }
                    break;
            }
        } else if (p2.blind_cnt%20 == 1){
            int action = Math.abs(random.nextInt())%3;
            int dir = Math.abs(random.nextInt())%2;
            refresh_status(p2);
            switch (action) {
                case 0:
                    nextround[1] = 0;
                    break;
                case 1:
                    nextround[1] = dir == 0 ? 1 : -1;
                    break;
                case 2:
                    switch (p2.getjob()) {
                        case 0:
                            nextround[1] = 2;
                            break;
                        case 1:
                            if (p2.MCD == 0)
                                nextround[1] = dir == 0 ? 2 : -2;
                            else
                                nextround[1] = dir == 0 ? 1 : -1;
                            break;
                        case 2:
                            if (p2.MCD == 0)
                                nextround[1] = 2;
                            else
                                nextround[1] = dir == 0 ? 1 : -1;
                            break;
                    }
            }
        }
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
        c.drawBitmap(host, 1900 - 108, 300,p);
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
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (pointX > 1792 && pointX < 1900) {
                    if (pointY > 300 && pointY < 408) {
                        ishosted = !ishosted;
                        roles.get(0).ishosted = ishosted;
                        if (Math.abs(roles.get(0).status) != 1 && roles.get(0).lastingAI) {
                            roles.get(0).lastingAI = false;
                        }
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

    private void doubleclickcnt() {
        if (lastleftcnt >= 0) {
            lastleftcnt++;
        }
        if (lastrightcnt >= 0) {
            lastrightcnt++;
        }
    }
}
