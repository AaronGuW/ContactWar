package com.example.aaron.contractwar;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;

public class bas_role {
    /**战斗所需的属性**/
    public int x,y,b,p;
    public int headto;                 //面朝方向
    public int status;                  //0站立 1前进 -1后退 3普通攻击 4技能1 5技能2 2 战士：格挡/法师：闪现（+2 -2） 6僵直 7击退 8眩晕 9致盲
    public int NCD = 0;                 //普攻CD
    public int SCD[] = {0,0};           //技能CD
    public int MCD = 0;                 //特殊技CD
    public int mcnt = 0;                //特殊技计时
    public int natk_cnt = 0;            //普攻帧数
    public int sk_cnt[] = {0,0};       //技能释放时间 计算帧数
    public int mv_cnt = 0;             //移动时间 计算帧数
    public int rigor_cnt = 0;           //僵直计时
    public int shock_dis = 0;
    public int dizzy_cnt = 0;           //眩晕计时
    public int blind_cnt = 0;           //致盲计时
    public int shieldcnt = 0;
    public boolean atk_validity = true;     //攻击有效判定，防止一次技能造成多次伤害
    public boolean atk_activated = false; //攻击有效判定
    public boolean poisoned = false;
    public int poisoncnt = 0;
    public Bitmap character_img,re_img;
    public static Bitmap flash;
    public static Bitmap dizzy;
    public static Bitmap shield,block;
    public static Bitmap poison;
    public boolean ishosted, lastingAI;

    /**角色基本信息**/
    private int job;                   //职业
    private int lv = 1;                     //角色信息
    private int exp = 0, nextlvexp = 50;        //用户当前等级经验，下一级所需经验
    private int attrpoint = 0;              //剩余未分配属性点
    private int ATK = 0;                    //攻击
    public int HP = 0, UHP = 0;                     //血量及血量上限
    private int score = 0;                  //个人积分
    private int team_score = 0;             //团队积分
    public boolean in_team = false;        //是否已组队，数据库里应该存的是队员的手机号
    public ArrayList<bas_role> team_number = new ArrayList<bas_role>();
    public int skill[] = new int[]{0,0};            //技能列表
    public potion bottle;
    public equipment[] equipments = new equipment[2];
    public int basspeed;
    public int speed;

    /**用户信息**/
    private String phone_number;        //电话号码
    private Bitmap photo;
    private String id;

    public static int sw, sh;
    public void initialize(String _phone_number,Bitmap _photo,String _id)
    {
        photo = _photo;
        id = _id;
        phone_number = _phone_number;
    }
    public void add_score(int addition){ score += addition; }
    public int getScore(){ return score; }
    public void add_teamscore(int addition){ team_score += addition; }
    public int getTeam_score(){ return team_score; }
    public void setjob(int _type){
        job = _type;
    }

    public int getjob() { return job; }
    public String getPhone_number(){ return phone_number; }
    public String getId(){ return id; }
    public Bitmap getPhoto(){ return photo; }
    public boolean isIn_team(){ return in_team; }
    public void setHP(int hp) { HP = hp; }
    public int getHP() { return HP; }
    public  void setATK(int atk) { ATK = atk; }
    public int getATK() { return ATK; }
    public int getLv() { return lv; }
    public int getAttrpoint() { return attrpoint; }
    public void setAttrpoint(int attr) { attrpoint = attr; }
    public int getExp() { return exp; }
    public int getNextlvexp() { return nextlvexp; }
    public boolean addexp(int e) {
        if (lv != 10) {
            exp += e;
            if (exp >= nextlvexp) {
                lv++;
                if (lv != 10) {
                    exp = exp - nextlvexp;
                    nextlvexp = (int) ((double) nextlvexp * 1.2);
                    attrpoint += 3;
                } else {
                    exp = nextlvexp;
                    attrpoint += 3;
                }
                return true;
            }
        }
        return false;
    }

    public void logic() {
        if (poisoned) {
            poisoncnt++;
            HP -= 20;
            if (poisoncnt >= 150) {
                poisoncnt = 0;
                poisoned = false;
            }
        }
        if (status != 4 && SCD[0] > 0) {
            SCD[0]--;
        }
        if (status != 5 && SCD[1] > 0) {
            SCD[1]--;
        }
        if (NCD > 0 && status != 3) {
            NCD--;
        }
        if (shieldcnt > 0) shieldcnt--;
        if (blind_cnt > 0) blind_cnt--;
        if (status != 2) {
            if (MCD > 0)
                MCD--;
        }
        switch (job) {
            case 0:
                if (Math.abs(status) == 1) {
                    p = headto == 1? b:x;
                    mv_cnt++;
                    if ( x < 100 ) {
                        if (headto*status == 1) {
                            x = x + headto * status * speed;
                            b = b + headto * status * speed;
                        }
                    } else if ( x > sw - 300) {
                        if (headto*status == -1) {
                            x = x + headto * status * speed;
                            b = b + headto * status * speed;
                        }
                    } else {
                        x = x + headto * status * speed;
                        b = b + headto * status * speed;
                    }
                    if (mv_cnt == 32) {
                        mv_cnt = 0;
                        status = 0;
                    }
                } else if (status == 2) {
                    mcnt++;
                    if (mcnt == 40) {
                        mcnt = 0;
                        status = 0;
                    }
                } else if (status == 3) {
                    natk_cnt++;
                    atk_activated = natk_cnt >= 4;
                    if (natk_cnt == 24) {
                        natk_cnt = 0;
                        status = 0;
                        NCD = 80;
                        atk_validity = true;
                        atk_activated = false;
                    }
                } else if (status == 4) {
                    sk_cnt[0]++;
                    atk_activated = sk_cnt[0] >= 12 && sk_cnt[0] <= 23;
                    if (sk_cnt[0] == 28) {
                        sk_cnt[0] = 0;
                        status = 0;
                        SCD[0] = 180;
                        NCD = 40;
                        atk_validity = true;
                        atk_activated = false;
                    }
                } else if (status == 5) {
                    sk_cnt[1]++;
                    if (sk_cnt[1] == 24) {
                        sk_cnt[1] = 0;
                        status = 0;
                        if (SCD[1] == 0) SCD[1] = 360;
                        NCD = 40;
                    }
                }
                break;
            case 1:
                if (Math.abs(status) == 1) {
                    if ( x < 100 ) {
                        if (headto*status == 1) {
                            x = x + headto * status * speed;
                            b = b + headto * status * speed;
                        }
                    } else if ( x > sw - 300) {
                        if (headto*status == -1) {
                            x = x + headto * status * speed;
                            b = b + headto * status * speed;
                        }
                    } else {
                        x = x + headto * status * speed;
                        b = b + headto * status * speed;
                    }
                    p = headto == 1? b:x;
                    mv_cnt++;
                    if (lastingAI) {
                        if (mv_cnt%32 == 0) {
                            mv_cnt = 0;
                            status = 0;
                            if (!ishosted)
                                lastingAI = false;
                        }
                    }
                } else if (Math.abs(status) == 2) {
                    mcnt++;
                    if ( x < 100 ) {
                        status = 2;
                    } else if ( x > sw - 100 - 160) {
                        status = -2;
                    }
                    if (mcnt == 10) {
                        x += 180 * status;
                        b += 180 * status;
                        x = x < 0 ? 0:x;
                        x = x > sw - 160 ? sw - 160:x;
                        p = headto == 1 ? b : x;
                    }
                    if (mcnt == 20) {
                        status = 0;
                        mcnt = 0;
                        MCD = 120;
                    }
                } else if (status == 3) {
                    natk_cnt++;
                    if (natk_cnt == 44) {
                        natk_cnt = 0;
                        status = 0;
                        NCD = 120;
                    }
                } else if (status == 4) {
                    sk_cnt[0]++;
                    if (sk_cnt[0] == 44) {
                        sk_cnt[0] = 0;
                        status = 0;
                        if (SCD[0] == 0) SCD[0] = 300;
                        NCD = 60;
                    }
                } else if (status == 5) {
                    sk_cnt[1]++;
                    if (sk_cnt[1] == 44) {
                        sk_cnt[1] = 0;
                        status = 0;
                        if (SCD[1] == 0) SCD[1] = 480;
                        NCD = 60;
                    }
                }
                break;
            case 2:
                if (Math.abs(status) == 1) {
                    if ( x < 100 ) {
                        if (headto*status == 1) {
                            x = x + headto * status * speed;
                            b = b + headto * status * speed;
                        }
                    } else if ( x > sw - 300) {
                        if (headto*status == -1) {
                            x = x + headto * status * speed;
                            b = b + headto * status * speed;
                        }
                    } else {
                        x = x + headto * status * speed;
                        b = b + headto * status * speed;
                    }
                    p = headto == 1? b:x;
                    mv_cnt++;
                    if (mv_cnt == 32) {
                        mv_cnt = 0;
                        status = 0;
                    }
                } else if (Math.abs(status) == 2) {
                    mcnt++;
                    if (mcnt == 40) {
                        status = 0;
                        mcnt = 0;
                        MCD = 180;
                    }
                } else if (status == 3) {
                    natk_cnt++;
                    atk_activated = natk_cnt >= 20;
                    if (natk_cnt == 40) {
                        natk_cnt = 0;
                        status = 0;
                        NCD = 80;
                        atk_activated = false;
                        atk_validity = true;
                    }
                } else if (status == 4) {
                    sk_cnt[0]++;
                    if (sk_cnt[0] == 8) {
                        shieldcnt = 160;
                    }
                    if (sk_cnt[0] == 40) {
                        sk_cnt[0] = 0;
                        status = 0;
                        SCD[0] = 480;
                    }
                } else if (status == 5) {
                    sk_cnt[1]++;
                    atk_activated = sk_cnt[1] >= 4;
                    if (sk_cnt[1] == 48) {
                        sk_cnt[1] = 0;
                        status = 0;
                        SCD[1] = 480;
                        atk_activated = false;
                        atk_validity = true;
                    }
                }
                break;
        }
        if (status == 6) {
            rigor_cnt++;
            if (rigor_cnt == 24) {
                rigor_cnt = 0;
                status = 0;
            }
        } else if (status == 7) {
            x -= shock_dis*headto;
            p -= shock_dis*headto;
            b -= shock_dis*headto;
            shock_dis-= 2;
            if (shock_dis < 5) {
                status = 0;
                shock_dis = 0;
            }
        } else if (status == 8) {
            dizzy_cnt++;
            if (dizzy_cnt == 120) {
                dizzy_cnt = 0 ;
                status = 0;
            }
        }
    }

    public void draw(Canvas canvas, Paint paint){
        switch (job) {
            case 0:
                switch (Math.abs(status)) {
                    case 0:
                        canvas.save();
                        if (headto == 1) {
                            canvas.clipRect(x, y - 214, x + 202, y);
                            canvas.drawBitmap(character_img, x - 436, y - 400, paint);
                        }else {
                            canvas.clipRect(x - 56, y - 236, x + 200 - 56, y);
                            canvas.drawBitmap(re_img, x - 420 - 56,y - 402, paint);
                        }
                        canvas.restore();
                        break;
                    case 1:
                        canvas.save();
                        if (headto == 1) {
                            canvas.clipRect(x, y - 234, x + 196, y);
                            canvas.drawBitmap(character_img, x - 22 - (mv_cnt / 4 + 1) * 420, y - 410, paint);
                        } else {
                            canvas.clipRect(x - 56, y - 236, x + 200 - 56, y);
                            canvas.drawBitmap(re_img, x - 420 - 56 - (mv_cnt / 4) * 420, y - 402, paint);
                        }

                        canvas.restore();
                        break;
                    case 2:
                        canvas.save();
                        if (headto == 1) {
                            canvas.clipRect(x, y - 248, x + 203, y);
                            canvas.drawBitmap(character_img, x - 2536, y - 824, paint);
                        } else {
                            canvas.clipRect(x - 58, y - 248, x + 203 - 58, y);
                            canvas.drawBitmap(re_img, x - 2598, y - 824, paint);
                        }
                        canvas.restore();
                        canvas.save();
                        if (headto == 1) {
                            canvas.clipRect(x - 63, y - 267, x - 63 + 300, y + 33);
                            canvas.drawBitmap(block, x - 63  - 300*((mcnt/4)%8) ,y - 267, paint);
                        }
                        else {
                            canvas.clipRect(x - 99, y - 267, x - 99 + 300, y + 33);
                            canvas.drawBitmap(block, x - 99 - 300 * ((mcnt / 4) % 8), y - 267, paint);
                        }
                        canvas.restore();
                        break;
                    case 3:
                        canvas.save();
                        if (headto == 1) {
                            canvas.clipRect(x, y - 284, x + 400, y);
                            canvas.drawBitmap(character_img, x - 10 - (natk_cnt / 4) * 420, y - 813, paint);
                        } else {
                            canvas.clipRect(x - 210, y - 263, x + 420 - 210, y);
                            canvas.drawBitmap(re_img, x - 210 - (natk_cnt/4)*420, y - 813 , paint);
                        }
                        canvas.restore();
                        break;
                    case 4:
                        canvas.save();
                        if (headto == 1) {
                            canvas.clipRect(x, y - 228, x + 300, y);
                            canvas.drawBitmap(character_img, x - 12 - sk_cnt[0] / 4 * 420, y - 1241, paint);
                        } else {
                            canvas.clipRect(x - 155, y - 231, x - 155 + 333, y);
                            canvas.drawBitmap(re_img, x - 150 - 420*(sk_cnt[0]/4), y - 1241, paint);
                        }
                        canvas.restore();
                        break;
                    case 5:
                        canvas.save();
                        if (headto == 1) {
                            canvas.clipRect(x, y - 393, x + 368, y);
                            canvas.drawBitmap(character_img, x - sk_cnt[1] / 4 * 420, y - 2497, paint);
                        } else {
                            canvas.clipRect(x - 166 , y - 393, x + 425 - 166, y);
                            canvas.drawBitmap(re_img, x - 178 - sk_cnt[1]/4*540, y - 2497, paint);
                        }
                        canvas.restore();
                        break;
                    case 6:
                        canvas.save();
                        if (headto == 1) {
                            canvas.clipRect(x, y - 257, x + 176, y);
                            canvas.drawBitmap(character_img, x - 1696 - (rigor_cnt / 8) % 2 * 420, y - 1657, paint);
                        } else {
                            canvas.clipRect(x, y - 257, x + 176, y);
                            canvas.drawBitmap(re_img, x - 1700 - (rigor_cnt/8)%2 * 420, y - 1657, paint);
                        }
                        canvas.restore();
                        break;
                    case 7:
                        canvas.save();
                        if (headto == 1) {
                            canvas.clipRect(x, y - 257, x + 176, y);
                            canvas.drawBitmap(character_img, x - 1696, y - 1657, paint);
                        } else {
                            canvas.clipRect(x ,y - 257, x + 176, y);
                            canvas.drawBitmap(re_img, x - 1700, y - 1657, paint);
                        }
                        canvas.restore();
                        break;
                    case 8:
                        canvas.save();
                        if (headto == 1) {
                            canvas.clipRect(x, y - 232, x + 176, y);
                            canvas.drawBitmap(character_img, x - 2139, y - 1657, paint);
                        } else {
                            canvas.clipRect(x, y - 232, x + 170, y);
                            canvas.drawBitmap(re_img, x - 2120, y - 1657, paint);
                        }
                        canvas.restore();
                        canvas.save();
                        int xx = (x+b)/2;
                        canvas.clipRect(xx - 128, y - 232 - 94, xx + 128 ,y - 232 - 10);
                        canvas.drawBitmap(dizzy, xx - 128 - 256 * ((dizzy_cnt / 4) % 6), y - 232 - 94, paint);
                        canvas.restore();
                        break;
                    default:
                        canvas.save();
                        canvas.clipRect(x, y - 214, x + 171, y);
                        canvas.drawBitmap(character_img, x - 22, y - 600, paint);
                        canvas.restore();
                        break;
                }
                break;
            case 1:
                switch (Math.abs(status)) {
                    case 0:
                        canvas.save();
                        canvas.clipRect(x, y - 247, x + 163, y);
                        if (headto == 1) {
                            canvas.drawBitmap(character_img, x - 357, y - 256, paint);
                        } else {
                            canvas.drawBitmap(re_img, x - 465, y - 256, paint);
                        }
                        canvas.restore();
                        break;
                    case 1:
                        canvas.save();
                        canvas.clipRect(x, y - 247, x + 163, y);
                        if (headto == 1) {
                            canvas.drawBitmap(character_img, x - 357, y - 256, paint);
                        } else {
                            canvas.drawBitmap(re_img, x - 465, y - 256, paint);
                        }
                        canvas.restore();
                        break;
                    case 2:
                        canvas.save();
                        canvas.clipRect(x, y - 250, x + 200, y);
                        canvas.drawBitmap(flash, x - 200*(mcnt/2) , y - 250, paint);
                        canvas.restore();
                        break;
                    case 3:
                        canvas.save();
                        if (headto == 1) {
                            canvas.clipRect(x, y - 247, x + 327, y);
                            canvas.drawBitmap(character_img, x - 355 - natk_cnt / 4 * 340, y - 256, paint);
                        }
                        else {
                            canvas.clipRect(x - 114, y - 247, x + 327 - 114, y);
                            canvas.drawBitmap(re_img, x - 345 - natk_cnt / 4 * 340 - 114, y - 256, paint);
                        }
                        canvas.restore();
                        break;
                    case 4:
                        canvas.save();
                        if (headto == 1) {
                            canvas.clipRect(x, y - 247, x + 327, y);
                            canvas.drawBitmap(character_img, x - 355 - sk_cnt[0] / 4 * 340, y - 256, paint);
                        } else {
                            canvas.clipRect(x - 114, y - 247, x + 327 - 114, y);
                            canvas.drawBitmap(re_img, x - 345 - sk_cnt[0] / 4 * 340 - 114, y - 256, paint);
                        }
                        canvas.restore();
                        break;
                    case 5:
                        canvas.save();
                        if (headto == 1) {
                            canvas.clipRect(x, y - 247, x + 327, y);
                            canvas.drawBitmap(character_img, x - 355 - sk_cnt[1] / 4 * 340, y - 256, paint);
                        } else {
                            canvas.clipRect(x - 114, y - 247, x + 327 - 114, y);
                            canvas.drawBitmap(re_img, x - 345 - sk_cnt[1] / 4 * 340 - 114, y - 256, paint);
                        }
                        canvas.restore();
                        break;
                    case 6:
                        canvas.save();
                        canvas.clipRect(x, y - 247, x + 163, y);
                        if (headto == 1) {
                            canvas.drawBitmap(character_img, x - 357, y - 256, paint);
                        } else {
                            canvas.drawBitmap(re_img, x - 465, y - 256, paint);
                        }
                        canvas.restore();
                        break;
                    case 7:
                        canvas.save();
                        canvas.clipRect(x, y - 247, x + 163, y);
                        if (headto == 1) {
                            canvas.drawBitmap(character_img, x - 357, y - 256, paint);
                        } else {
                            canvas.drawBitmap(re_img, x - 465, y - 256, paint);
                        }
                        canvas.restore();
                        break;
                    case 8:
                        canvas.save();
                        canvas.clipRect(x, y - 247, x + 163, y);
                        if (headto == 1) {
                            canvas.drawBitmap(character_img, x - 357, y - 256, paint);
                        } else {
                            canvas.drawBitmap(re_img, x - 465, y - 256, paint);
                        }
                        canvas.restore();
                        canvas.save();
                        int xx = (x+b)/2;
                        canvas.clipRect(xx - 128, y - 247 - 94, xx + 128 ,y - 247 - 10);
                        canvas.drawBitmap(dizzy, xx - 128 - 256 * ((dizzy_cnt / 4) % 6), y-247-94, paint);
                        canvas.restore();
                        break;
                }
                break;
            case 2:
                switch (Math.abs(status)) {
                    case 0:
                        canvas.save();
                        canvas.clipRect(x, y - 210, x + 127, y);
                        if (headto == -1) canvas.drawBitmap(re_img, x - 1046, y - 1248, paint);
                        else canvas.drawBitmap(character_img, x - 1059, y - 1248, paint );
                        canvas.restore();
                        break;
                    case 1:
                        canvas.save();
                        canvas.clipRect(x, y - 210, x + 150, y);
                        if (headto == -1) canvas.drawBitmap(re_img, x - 718 - 320*((mv_cnt/4)%3) , y -1248, paint);
                        else canvas.drawBitmap(character_img, x - 729 - 320*((mv_cnt/4)%3), y - 1248, paint);
                        canvas.restore();
                        break;
                    case 2:
                        canvas.save();
                        canvas.clipRect(x, y - 315, x + 160, y);
                        if (headto == -1) canvas.drawBitmap(re_img, x - 74 - 320*(mcnt/4), y - 1914, paint);
                        else canvas.drawBitmap(character_img, x - 74 - 320*(mcnt/4), y - 1914, paint);
                        canvas.restore();
                        break;
                    case 3:
                        canvas.save();
                        if (headto == -1) {
                            canvas.clipRect(x - 77, y - 312, x + 282 - 77, y);
                            canvas.drawBitmap(re_img, x - 12 - (natk_cnt / 4) * 320 - 77, y - 1568, paint);
                        }
                        else {
                            canvas.clipRect(x - 88, y - 312, x + 292 - 88, y);
                            canvas.drawBitmap(character_img, x - 4 - (natk_cnt / 4) * 320 - 88, y - 1568, paint);
                        }
                        canvas.restore();
                        break;
                    case 4:
                        canvas.save();
                        canvas.clipRect(x, y - 314, x + 160, y);
                        if (headto == -1) canvas.drawBitmap(re_img, x - 74 - 320*(sk_cnt[0]/4), y - 1914, paint);
                        else canvas.drawBitmap(character_img, x - 74 - 320*(sk_cnt[0]/4), y - 1914, paint );
                        canvas.restore();
                        break;
                    case 5:
                        canvas.save();
                        if (headto == -1) {
                            canvas.clipRect(x - 94, y - 258, x + 224 - 94, y);
                            canvas.drawBitmap(re_img, x - 4 - (sk_cnt[1] / 4) * 320 - 94, y - 286, paint);
                        }
                        else {
                            canvas.clipRect(x - 20, y - 258, x + 238 - 20, y);
                            canvas.drawBitmap(character_img, x - 76 - (sk_cnt[1] / 4) * 320 - 20, y - 286, paint );
                        }
                        canvas.restore();
                        break;
                    case 6:
                        canvas.save();
                        if (headto == -1) {
                            canvas.clipRect(x, y - 210, x + 150, y);
                            canvas.drawBitmap(re_img, x - 100 - 320 * ((rigor_cnt / 4) % 2), y - 1248, paint);
                        }
                        else {
                            canvas.clipRect(x - 25, y - 210, x + 150 - 25, y);
                            canvas.drawBitmap(character_img, x - 71 - 320 * ((rigor_cnt / 4) % 2) - 25, y - 1248, paint);
                        }
                        canvas.restore();
                        break;
                    case 7:
                        canvas.save();
                        canvas.clipRect(x, y - 210, x + 150, y);
                        if (headto == -1) {
                            canvas.clipRect(x, y - 210, x + 150, y);
                            canvas.drawBitmap(re_img, x - 100, y - 1248, paint);
                        }
                        else {
                            canvas.clipRect(x - 25, y - 210, x + 150 -25, y);
                            canvas.drawBitmap(character_img, x - 71, y - 1248, paint);
                        }
                        canvas.restore();
                        break;
                    case 8:
                        canvas.save();
                        if (headto == -1) {
                            canvas.clipRect(x, y - 210, x + 150, y);
                            canvas.drawBitmap(re_img, x - 100 - 320, y - 1248, paint);
                        }
                        else {
                            canvas.clipRect(x - 19, y - 210, x + 150 - 19, y);
                            canvas.drawBitmap(character_img, x - 397, y - 1248, paint);
                        }
                        canvas.restore();
                        canvas.save();
                        int xx = (x+b)/2;
                        canvas.clipRect(xx - 128, y - 315 - 94, xx + 128 ,y - 315 - 10);
                        canvas.drawBitmap(dizzy, xx - 128 - 256 * ((dizzy_cnt / 4) % 6), y - 315 - 94, paint);
                        canvas.restore();
                        break;

                }
                if (shieldcnt > 0) {
                    canvas.save();
                    int xx = (x+b)/2;
                    canvas.clipRect(xx - 206 + 50,y - 323, xx + 206 + 50, y + 10);
                    canvas.drawBitmap(shield,(float)(xx - 206 + 50 - 412.5*((shieldcnt/4)%6)),y - 323 , paint);
                    canvas.restore();
                }
        }
        if (poisoned) {
            canvas.save();
            int xx = (x+b)/2;
            canvas.clipRect(xx - poison.getWidth()/2, y - 220 - poison.getHeight() - 10,xx + poison.getWidth()/2, y - 10 - 220 );
            canvas.drawBitmap(poison, xx - poison.getWidth()/2, y - 220 - poison.getHeight(),paint);
            canvas.restore();
        }
    }

}
