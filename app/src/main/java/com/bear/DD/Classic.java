package com.bear.DD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Classic extends AppCompatActivity implements ClassicCommonSkills {
    static boolean isover = false;
    static boolean isdone = false;
    int turns = 0;
    static TextView status = null;
    static ScrollView scr_status=null;
    TextView DDnum = null;
    static ClassicPack pack = ClassicSet.packs.get(0);
    static HashSet<ClassicPack> nobattlepack = new HashSet<ClassicPack>();
    Button BEGIN = null;
    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    status.append((String) msg.obj);
                    scr_status.post(new Runnable() {
                        @Override
                        public void run() {
                            scr_status.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                    break;
                case 1:
//                    AlertDialog.Builder buider = new AlertDialog.Builder(Classic.this);
//                    buider.setTitle("你被淘汰，是否重新开始？");
//                    buider.setPositiveButton("再来一局", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Classic.isover = true;
//                            Classic.isdone = true;
//                        }
//                    });
//                    buider.setNegativeButton("结束游戏", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Classic.isdone = true;
//                        }
//                    });
//                    buider.show();
                    BEGIN.setText("重新开始");
                    BEGIN.setEnabled(true);
                    break;
                case 2:
                    status.setText((String) msg.obj);
                    break;
                case 3:
                    scr_status.fullScroll(ScrollView.FOCUS_DOWN);
                    break;
            }

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        status = findViewById(R.id.status);
        scr_status=findViewById(R.id.scr_status);
        DDnum = findViewById(R.id.DDnum);
        for (ClassicPack cp : ClassicSet.packs) {
            cp.Allclear();
            status.setText("");
            turns = 0;
        }
        ClassicSet.Allclear();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic);
        status = findViewById(R.id.status);
//        status.setMovementMethod(new ScrollingMovementMethod());
        scr_status=findViewById(R.id.scr_status);
        DDnum = findViewById(R.id.DDnum);
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
                            turns++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    DDnum.setText("当前DD数：" + pack.ddnum);
                                }
                            });
                            ClassicSet.nowplayer++;
                            //等待玩家选择完毕
                            while (!pack.isOK) ;
                            //判定
                            HashSet<ClassicPack> reflectset = new HashSet<ClassicPack>();
                            HashSet<ClassicPack> selfbset = new HashSet<ClassicPack>();
                            System.out.println("开始判定");


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
                                Message msg2 = new Message();
                                msg2.what = 0;
                                msg2.obj = cp.player.name + "出了" + cp.skill.skillname ;
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
                                mHandler.sendMessage(msg2);
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
                            msg3.what=0;
                            msg3.obj = "第" + turns + "回合\n目前淘汰者：\n";
                            System.out.println((String) msg3.obj);
                            mHandler.sendMessage(msg3);
                            for (ClassicPack cp : ClassicSet.outset) {
                                Message msg4 = new Message();
                                msg4.obj = cp.player.name + "\n";
                                System.out.println((String) msg4.obj);
                                mHandler.sendMessage(msg4);
                            }
//                            Message msg4 = new Message();
//                            msg4.obj = "玩家DD数：" + pack.ddnum;
//                            mHandler.sendMessage(msg4);
//                            Message msg5 = new Message();
//                            msg5.obj = "\n人机1DD数：" + ClassicSet.packs.get(1).ddnum + "\n--------------\n";
//                            mHandler.sendMessage(msg5);
                            for (ClassicPack cp : ClassicSet.packs) {
                                if (ClassicSet.outset.contains(cp) || nobattlepack.contains(cp)) {
                                    continue;
                                }
                                Message msg4 = new Message();
                                msg4.what=0;
                                msg4.obj = cp.player.name + "DD数：" + cp.ddnum + '\n';
                                mHandler.sendMessage(msg4);
                            }
                            Message msg4 = new Message();
                            msg4.what=0;
                            msg4.obj = "--------------\n";
                            mHandler.sendMessage(msg4);

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
                                    turns = 0;
                                }
                                Message msg8 = new Message();
                                msg8.what = 0;
                                msg8.obj = "你赢了。";
                                mHandler.sendMessage(msg8);
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
                                    turns = 0;
                                }
                                Message msg8 = new Message();
                                msg8.what = 0;
                                msg8.obj = "你被淘汰。";
                                mHandler.sendMessage(msg8);
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
                            msg9.obj=null;
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