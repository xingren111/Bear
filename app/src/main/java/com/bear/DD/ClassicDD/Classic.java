package com.bear.DD.ClassicDD;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bear.DD.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Classic extends AppCompatActivity implements ClassicCommonSkills {
    static boolean isover = false;
    static boolean isdone = false;
    int turns = 0;
    static TextView status_skill = null;
    static TextView status_DD = null;
    static TextView status_out = null;
    static ScrollView scr_status_skill=null;
    static ScrollView scr_status_DD=null;
    static ScrollView scr_status_out=null;
    TextView turnnum = null;
    static ClassicPack pack = ClassicSet.packs.get(0);
    static HashSet<ClassicPack> nobattlepack = new HashSet<ClassicPack>();
    Button BEGIN = null;
    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    turnnum.setText((String) msg.obj);
                    break;
                case 1:
                    BEGIN.setText("重新开始");
                    BEGIN.setEnabled(true);
                    break;
                case 2:
                    status_skill.setText((String) msg.obj);
                    status_DD.setText((String)msg.obj);
                    status_out.setText((String)msg.obj);
                    break;
                case 3:
                    scr_status_skill.fullScroll(ScrollView.FOCUS_DOWN);
                    scr_status_DD.fullScroll(ScrollView.FOCUS_DOWN);
                    scr_status_out.fullScroll(ScrollView.FOCUS_DOWN);
                    break;
                case 4:
                    status_skill.setText((String) msg.obj);
                    scr_status_skill.post(new Runnable() {
                        @Override
                        public void run() {
                            scr_status_skill.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                    break;
                case 5:
                    status_DD.setText((String)msg.obj);
                    scr_status_DD.post(new Runnable() {
                        @Override
                        public void run() {
                            scr_status_DD.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                    break;
                case 6:
                    status_out.setText((String)msg.obj);
                    scr_status_out.post(new Runnable() {
                        @Override
                        public void run() {
                            scr_status_out.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                    break;
            }

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        status_skill = findViewById(R.id.status_skill);
        scr_status_skill=findViewById(R.id.scr_status_skill);
        status_DD = findViewById(R.id.status_DD);
        scr_status_DD=findViewById(R.id.scr_status_DD);
        status_out = findViewById(R.id.status_out);
        scr_status_out=findViewById(R.id.scr_status_out);
        turnnum = findViewById(R.id.turnnum);
        for (ClassicPack cp : ClassicSet.packs) {
            cp.Allclear();
            status_skill.setText("");
            status_DD.setText("");
            status_out.setText("");
            turns = 0;
        }
        ClassicSet.Allclear();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic);
        status_skill = findViewById(R.id.status_skill);
        scr_status_skill=findViewById(R.id.scr_status_skill);
        status_DD = findViewById(R.id.status_DD);
        scr_status_DD=findViewById(R.id.scr_status_DD);
        status_out = findViewById(R.id.status_out);
        scr_status_out=findViewById(R.id.scr_status_out);
        turnnum = findViewById(R.id.turnnum);
        BEGIN = findViewById(R.id.BEGIN);

        final ArrayList<Button> al_button = new ArrayList<Button>() {
            {
                add(findViewById(R.id.DD));
                add(findViewById(R.id.B));
                add(findViewById(R.id.Prigen));
                add(findViewById(R.id.ThreeL));
                add(findViewById(R.id.Wow));
                add(findViewById(R.id.BigB));
                add(findViewById(R.id.SB));
                add(findViewById(R.id.sDef));
                add(findViewById(R.id.lDef));
                add(findViewById(R.id.wDef));
                add(findViewById(R.id.BigDef));
                add(findViewById(R.id.Reflect));
                add(findViewById(R.id.SelfB));
            }
        };
        HashMap<ClassicPack, Button> packTobutton = new HashMap<ClassicPack, Button>() {
            {
                Button bBot1 = findViewById(R.id.Bot1);
                Button bBot2 = findViewById(R.id.Bot2);
                Button bBot3 = findViewById(R.id.Bot3);
                Button bBot4 = findViewById(R.id.Bot4);
                Button bBot5 = findViewById(R.id.Bot5);
                put(ClassicSet.packs.get(1), bBot1);
                System.out.println(ClassicSet.packs.get(1).player.name + "->" + bBot1.getText());
                put(ClassicSet.packs.get(2), bBot2);
                System.out.println(ClassicSet.packs.get(2).player.name + "->" + bBot2.getText());
                put(ClassicSet.packs.get(3), bBot3);
                System.out.println(ClassicSet.packs.get(3).player.name + "->" + bBot3.getText());
                put(ClassicSet.packs.get(4), bBot4);
                System.out.println(ClassicSet.packs.get(4).player.name + "->" + bBot4.getText());
                put(ClassicSet.packs.get(5), bBot5);
                System.out.println(ClassicSet.packs.get(5).player.name + "->" + bBot5.getText());
            }
        };
        for (Button b : packTobutton.values()) {
            b.setEnabled(false);
        }
        for (int i = 0; i < al_button.size(); i++) {
            int finalI = i;
            al_button.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pack.skill = skills.get(finalI);
                    pack.ddnum -= pack.skill.cost;
                    if (skills.get(finalI) == null) {
                        System.out.println("NULL!!!");
                    }
                    System.out.println(String.valueOf("玩家使用" + skills.get(finalI).skillname));
                    //设置目标
                    if (pack.skill.maxTargets == -1) {
                        for (ClassicPack cp : packTobutton.keySet()) {
                            if (!nobattlepack.contains(cp) && !ClassicSet.outset.contains(cp) && cp != pack) {
                                pack.targets.add(cp);
                            }
                        }
                        pack.isOK = true;
                    } else if (pack.skill.maxTargets == 0) {
                        pack.isOK = true;
                    } else {
                        if (ClassicSet.maxplayernum - ClassicSet.outset.size() - 1 <= pack.skill.maxTargets) {
                            for (ClassicPack cp : packTobutton.keySet()) {
                                if (!nobattlepack.contains(cp) && !ClassicSet.outset.contains(cp) && cp != pack) {
                                    pack.targets.add(cp);
                                }
                            }
                            pack.isOK = true;
                        } else {
                            //1.技能键全部设置禁用
                            for (int j = 0; j < al_button.size(); j++) {
                                al_button.get(j).setEnabled(false);
                            }
                            //2.设置全部人机为启用
                            for (Button b : packTobutton.values()) {
                                b.setEnabled(true);
                            }
//                            3.设置不参与对战的人机为禁用
                            for (int j = 5; j >= ClassicSet.maxplayernum; j--) {
                                packTobutton.get(ClassicSet.packs.get(j)).setEnabled(false);
                            }
                            for (ClassicPack cp : nobattlepack) {
                                packTobutton.get(cp).setEnabled(false);
                            }
//                            4.设置出局人机为禁用
                            if (!ClassicSet.outset.isEmpty()) {
                                for (ClassicPack cp : ClassicSet.outset) {
                                    packTobutton.get(cp).setEnabled(false);
                                }
                            }
                        }
                    }
                }
            });
        }
        for (ClassicPack cp : packTobutton.keySet()) {
            packTobutton.get(cp).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pack.targets.add(cp);
                    System.out.println("将"+cp.player.getName()+"加入目标");
                    packTobutton.get(cp).setEnabled(false);
                    System.out.println("当前目标数："+pack.targets.size());
                    if (pack.targets.size() == pack.skill.maxTargets) {
                        //设置全部人机为禁用
                        for (int j = 1; j <= packTobutton.size(); j++) {
                            packTobutton.get(ClassicSet.packs.get(j)).setEnabled(false);
                        }
                        pack.isOK = true;
                    }
                }
            });
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ClassicSet.lock1.lock();
                try {
                    while (true) {
                        ClassicSet.isquit=false;
                        isover = false;
                        ClassicCtrl ctrl = new ClassicCtrl();
                        ctrl.setEnviroment(Enviroment.PLAYER);
                        HashMap<Button, ClassicSkill> buttonToskill = new HashMap<Button, ClassicSkill>();
                        for (int i = 0; i < skills.size(); i++) {
                            ctrl.add(skills.get(i), al_button.get(i));
                            buttonToskill.put(al_button.get(i), skills.get(i));
                        }
                        ctrl.setnowdd(0);
//                    ArrayList<Bot> bots = new ArrayList<Bot>();
                        for (int i = 1; i <= ClassicSet.maxplayernum - 1; i++) {
                            Bot b = new Bot(Players.get(i), ClassicSet.packs.get(i));
//                        bots.add(b);
                            new Thread(b).start();

                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ctrl.update();
                            }
                        });
                        for (int i = 5; i >= ClassicSet.maxplayernum; i--) {
                            nobattlepack.add(ClassicSet.packs.get(i));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                for (int i = 1; i <= 5; i++) {
//                                    packTobutton.get(ClassicSet.packs.get(i)).setEnabled(false);
//                                }
                                if (packTobutton == null) {
                                    System.out.println("packTobutton为NULL!");
                                } else if (packTobutton.isEmpty()) {
                                    System.out.println("packTobutton为空！");
                                } else {
                                    System.out.println("packTobutton不为空！,内容如下：");
                                    for (ClassicPack cp : ClassicSet.packs) {
                                        if (cp == pack) {
                                            continue;
                                        }
                                        System.out.println(cp.player.name + "->" + packTobutton.get(cp).getText());
                                    }
                                }
                                for (ClassicPack cp : ClassicSet.packs) {
                                    if (cp == pack) {
                                        continue;
                                    }

                                    System.out.println("是否找到cp（" + cp.player.name + "）？" + packTobutton.containsKey(cp));
                                    Button b = packTobutton.get(cp);
                                    if (b == null) {
                                        System.out.println("NULL，cp=" + cp.player.name);
                                    }
                                    b.setEnabled(false);
                                }
                            }
                        });


                        while (true) {
                            ctrl.setnowdd(pack.ddnum);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ctrl.update();
                                }
                            });


                            while (ClassicSet.nowplayer != 0) {
                                try {
                                    ClassicSet.conditions.get(0).await();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            Message msg=new Message();
                            msg.what=0;
                            msg.obj="当前回合数："+turns;
                            mHandler.sendMessage(msg);
                            turns++;

                            ClassicSet.nowplayer++;
                            //等待玩家选择完毕
                            while (!pack.isOK) ;
                            //判定
                            HashSet<ClassicPack> reflectset = new HashSet<ClassicPack>();
                            HashSet<ClassicPack> selfbset = new HashSet<ClassicPack>();
                            System.out.println("开始判定");

                            Message msg2 = new Message();
                            msg2.what = 4;
                            msg2.obj="";
                            for (ClassicPack cp : ClassicSet.packs) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        status.append("现在判定"+cp.player.name);
//                                        System.out.println("现在判定"+cp.player.name);
//                                    }
//                                });
                                if (nobattlepack.contains(cp) || ClassicSet.outset.contains(cp)) {
                                    continue;
                                }
                                ClassicSkill skill = cp.skill;
//                                status.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        status.append(cp.player.name+"出了"+cp.skill.skillname+'\n');
//                                        System.out.println(cp.player.name+"出了"+cp.skill.skillname);
//
//                                    }
//                                });

                                msg2.obj += (cp.player.name + "出了" + cp.skill.skillname );
                                if(skill.maxTargets==-1){
                                    msg2.obj+="，攻击全体";
                                }else if(skill.maxTargets>0){
                                    msg2.obj+="，攻击：";
                                    for(ClassicPack cp1:cp.targets){
                                        msg2.obj=msg2.obj+"\n"+cp1.player.getName();
                                    }
                                }
                                msg2.obj+="\n";
                                System.out.println((String) msg2.obj);

                                if (skill.type == 2) {
                                    skill.classicbeen.getsth(cp);
                                } else if (skill.type == 0) {
                                    for (ClassicPack tar : cp.targets) {
                                        tar.skill.classicbeen.doBeen(cp, tar);
                                    }
                                }
                                if (skill.equals(Reflect)) {
                                    reflectset.add(cp);
                                }
                                if (skill.equals(SelfB)) {
                                    selfbset.add(cp);
                                }
                                cp.isOK = false;
                            }
                            mHandler.sendMessage(msg2);
                            if (reflectset.isEmpty() && !selfbset.isEmpty()) {
                                for (ClassicPack cp : selfbset) {
                                    cp.isout = true;
                                    ClassicSet.isnewout = true;
                                }
                            } else if (!reflectset.isEmpty() && !selfbset.isEmpty()) {
                                for (ClassicPack cp : reflectset) {
                                    cp.isout = true;
                                    ClassicSet.isnewout = true;
                                }
                            }
                            for (ClassicPack cp : ClassicSet.packs) {
                                if (cp.isout) {
                                    ClassicSet.outset.add(cp);
                                }
                            }

                            Message msg3 = new Message();
                            msg3.what=6;
                            msg3.obj = "目前淘汰者：\n";
                            System.out.println((String) msg3.obj);
//                            mHandler.sendMessage(msg3);
                            for (ClassicPack cp : ClassicSet.outset) {
//                                Message msg4 = new Message();
                                msg3.obj += (cp.player.name + "\n");
                                System.out.println((String) msg3.obj);

                            }
                            mHandler.sendMessage(msg3);
//                            Message msg4 = new Message();
//                            msg4.obj = "玩家DD数：" + pack.ddnum;
//                            mHandler.sendMessage(msg4);
//                            Message msg5 = new Message();
//                            msg5.obj = "\n人机1DD数：" + ClassicSet.packs.get(1).ddnum + "\n--------------\n";
//                            mHandler.sendMessage(msg5);
                            Message msg4 = new Message();
                            msg4.what=5;
                            msg4.obj="";
                            for (ClassicPack cp : ClassicSet.packs) {
                                if (ClassicSet.outset.contains(cp) || nobattlepack.contains(cp)) {
                                    continue;
                                }

                                msg4.obj += (cp.player.name + "DD数：" + cp.ddnum + '\n');

                            }
                            mHandler.sendMessage(msg4);
//                            Message msg4 = new Message();
//                            msg4.what=5;
//                            msg4.obj = "--------------\n";
//                            mHandler.sendMessage(msg4);

                            if(ClassicSet.isnewout){
                                System.out.println("人机死亡。");
                            }
                            if (ClassicSet.isroundback && ClassicSet.isnewout) {
                                System.out.println("人机死亡，重新开始。");
                                ClassicSet.isnewout = false;
                                for (ClassicPack cp : ClassicSet.packs) {
                                    cp.clear();
                                    Message msg6 = new Message();
                                    msg6.what = 2;
                                    msg6.obj = "";
                                    mHandler.sendMessage(msg6);
                                    turns = 0;
                                }
                            }
                            if (ClassicSet.isAllout()) {
                                System.out.println("玩家获胜");
                                ClassicSet.nowplayer = 1;
                                for (ClassicPack cp : ClassicSet.packs) {
                                    cp.Allclear();
//                                    Message msg7=new Message();
//                                    msg7.what=2;
//                                    msg7.obj="";
//                                    mHandler.sendMessage(msg7);

                                }
                                Message msg8 = new Message();
                                msg8.what = 0;
                                msg8.obj = "第"+turns+"回合，"+"你赢了。";
                                ClassicSet.isquit=true;
                                mHandler.sendMessage(msg8);
                                ClassicSet.conditions.get(1).signalAll();
                                turns = 0;
//                                Message msg9=new Message();
//                                msg9.what=1;
//                                mHandler.sendMessage(msg9);
//                                while (!isdone);
                                break;
                            }
                            if (pack.isout) {
                                ClassicSet.nowplayer = 1;
                                for (ClassicPack cp : ClassicSet.packs) {
                                    cp.Allclear();
//                                    Message msg7=new Message();
//                                    msg7.what=2;
//                                    msg7.obj="";
//                                    mHandler.sendMessage(msg7);

                                }
                                Message msg8 = new Message();
                                msg8.what = 0;
                                msg8.obj = "第"+turns+"回合，"+"你被淘汰。";
                                ClassicSet.isquit=true;
                                mHandler.sendMessage(msg8);
                                ClassicSet.conditions.get(1).signalAll();
                                turns = 0;
//                                Message msg9=new Message();
//                                msg9.what=1;
//                                mHandler.sendMessage(msg9);
//                                while (!isdone);
                                break;
                            }
                            for(ClassicPack cp:ClassicSet.packs){
                                cp.targets.clear();
                                cp.skill=null;
                                cp.isOK=false;
                            }
                            Message msg9=new Message();
                            msg9.what=3;
                            msg9.obj="";
                            mHandler.sendMessage(msg9);
                            ClassicSet.conditions.get(ClassicSet.nowplayer).signal();
                        }
//                        if (Classic.isover) {
//                            continue;
//                        }
                        break;
                    }
                }finally {
                    ClassicSet.lock1.unlock();
                }

            }
        });
        BEGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    BEGIN.setEnabled(false);
                    t.start();
                    ClassicSet.nowplayer = 1;
                    ClassicSet.lock1.lock();
                    try{
                        ClassicSet.conditions.get(1).signal();
                    }finally {
                        ClassicSet.lock1.unlock();
                    }
            }
        });
    }
}