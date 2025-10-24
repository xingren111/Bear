package com.bear.DD.BoBo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.Layout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bear.DD.RollInt;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.*;
import com.bear.DD.R;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.concurrent.locks.Condition;

public class BoBoActivity extends AppCompatActivity {
    //技能全集，id->技能
    public static HashMap<Integer, Skill> U_idToskill = new HashMap<Integer, Skill>();
    //id->按钮映射
    public static HashMap<Integer, Button> U_idTobutton = new HashMap<Integer, Button>();
    //按钮->id映射
    public static HashMap<Button, Integer> U_buttonToid = new HashMap<Button, Integer>();
    //全局配置
    public static Config config = new Config();
    public static AssetManager am;
    public static Context context;
    public static BoBoActivity activity;
    TextView tvw_turns = null;
    TextView tvw_dd = null;
    TextView tvw_skill = null;
    TextView tvw_HP = null;
    Boolean isChanging = false;
    Boolean isTower = false;

    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            int what=msg.what;
            if(what==0) {//修改回合数
                tvw_turns.setText("当前回合数：" + msg.obj);
            }else if(what==1) {//修改skill显示
                tvw_skill.setText((String) msg.obj);
            }else if(what==2) {//修改dd显示
                tvw_dd.setText((String) msg.obj);
            }else if(what==3) {//修改出局显示
                tvw_HP.setText((String) msg.obj);
            }else if(what==4){//ctrl更新
                    Pack pack = (Pack) msg.obj;
                    ConstantPool.ctrls[0].updateCommon(pack.player.ddnum, pack.skill, pack.times);
                    ConstantPool.ctrls[0].updateAbsorb();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.res_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemid = item.getItemId();
        if (itemid == R.id.see_more_information) {//若点击查看更多信息选项
            //来了！
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            StringBuilder sb = new StringBuilder();
            for (Pack pack : ConstantPool.packs) {
                if (ConstantPool.outset.contains(pack)) {
                    continue;
                }
                sb.append("----" + pack.player.name + "----" + '\n');
                sb.append("历史技能如下（已扣除组合技）：\n");
                for (Skill skill : ConstantPool.ctrls[pack.player.id].historyskills.keySet()) {
                    if (ConstantPool.ctrls[pack.player.id].historyskills.get(skill) <= 0) {
                        continue;
                    }
                    sb.append(skill.name + "*" + ConstantPool.ctrls[pack.player.id].historyskills.get(skill) + '\n');
                }
                sb.append("吸收区技能如下（已扣除组合技）：\n");
                for (Skill skill : ConstantPool.ctrls[pack.player.id].Aarea.keySet()) {
                    if (ConstantPool.ctrls[pack.player.id].Aarea.get(skill) <= 0) {
                        continue;
                    }
                    sb.append(skill.name + "*" + ConstantPool.ctrls[pack.player.id].Aarea.get(skill) + '\n');
                }
                sb.append("塔区技能如下（已扣除组合技）：\n");
                for (Skill skill : ConstantPool.ctrls[pack.player.id].tAarea.keySet()) {
                    if (ConstantPool.ctrls[pack.player.id].tAarea.get(skill) <= 0) {
                        continue;
                    }
                    sb.append(skill.name + "*" + ConstantPool.ctrls[pack.player.id].tAarea.get(skill) + '\n');
                }
            }
            alert.setMessage(sb.toString());
            alert.setPositiveButton("确定", (dialog, which) -> {
            });
            alert.create().show();

        } else if (itemid == R.id.see_help) {


            AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.layout_see_help, null);
            alert1.setView(view);
            LinearLayout ll = view.findViewById(R.id.see_help);
            for (int id : U_idTobutton.keySet()) {
                Button b = new Button(view.getContext());
                b.setText(U_idTobutton.get(id).getText());
                b.setBackgroundResource(R.drawable.button_background);
                b.setVisibility(View.VISIBLE);
                b.setEnabled(true);
                TextView tv = new TextView(view.getContext());
                tv.setVisibility(View.GONE);

                StringBuilder sb1 = new StringBuilder();
                Skill skill = U_idToskill.get(id);
                sb1.append("技能名称：" + skill.name + '\n');
                sb1.append("技能花费：" + skill.cost + '\n');
                sb1.append("技能是否为组合技：" + (skill.ismixture == true ? "是" : "否") + '\n');

                if (skill.ismixture) {
                    sb1.append("技能的组成成分：\n");
                    for (int i = 0; i < skill.mixedof.size(); i++) {
                        for (int skillid : skill.mixedof.get(i).keySet()) {
                            Skill skill1 = U_idToskill.get(skillid);
                            sb1.append(skill1.name + '*' + skill.mixedof.get(i).get(skillid) + '\n');
                        }
                        if (i != skill.mixedof.size() - 1) {
                            sb1.append("或者\n");
                        }
                    }
                }

                if (!skill.Atk.isEmpty()) {
                    sb1.append("技能的攻击目标：\n");
                    for (int skillid : skill.Atk.keySet()) {
                        Skill skill1 = U_idToskill.get(skillid);
                        sb1.append("对" + skill1.name + "造成" + skill.Atk.get(skillid) + "点伤害" + '\n');
                    }
                }

                sb1.append("解锁此技能所需等级：" + skill.Lv + '\n');

                if (!skill.Absorb.isEmpty()) {
                    sb1.append("此技能可普通吸收的技能：\n");
                    for (int skillid : skill.Absorb) {
                        Skill skill1 = U_idToskill.get(skillid);
                        sb1.append(skill1.name + "，");
                    }
                    sb1.deleteCharAt(sb1.length() - 1);
                    sb1.append('\n');
                }


                if (!skill.tAbsorb.isEmpty()) {
                    sb1.append("此技能可多倍吸收的技能：\n");
                    for (int skillid : skill.tAbsorb) {
                        Skill skill1 = U_idToskill.get(skillid);
                        sb1.append(skill1.name + "，");
                    }
                    sb1.deleteCharAt(sb1.length() - 1);
                    sb1.append('\n');
                    sb1.append("吸收倍率：" + skill.tAbsorbTimes + '\n');
                }

                if (!skill.sAbsorb.isEmpty()) {
                    sb1.append("此技能可偷取的技能：\n");
                    for (int skillid : skill.sAbsorb) {
                        Skill skill1 = U_idToskill.get(skillid);
                        sb1.append(skill1.name + "，");
                    }
                    sb1.deleteCharAt(sb1.length() - 1);
                    sb1.append('\n');
                }

                if (skill.Heal != 0) {
                    sb1.append("治疗量：" + skill.Heal + '\n');
                }

                if (skill.Lockturns != 0) {
                    sb1.append("可被此技能锁住的技能：" + '\n');
                    for (int skillid : skill.Locklist) {
                        Skill skill1 = U_idToskill.get(skillid);
                        sb1.append(skill1.name + "，");
                    }
                    sb1.deleteCharAt(sb1.length() - 1);
                    sb1.append("\n锁技能回合数：" + skill.Lockturns + '\n');
                }

                if (skill.isChangeable) {
                    sb1.append("此技能可以变成的技能：\n");
                    for (int skillid : skill.changelist) {
                        Skill skill1 = U_idToskill.get(skillid);
                        sb1.append(skill1.name + "，");
                    }
                    sb1.deleteCharAt(sb1.length() - 1);
                    sb1.append('\n');
                }

                if (skill.gain != 0) {
                    sb1.append("技能加子数：" + skill.gain + "\n");
                }
                tv.setText(sb1.toString());
                b.setOnClickListener(new View.OnClickListener() {
                    boolean flag = true;

                    @Override
                    public void onClick(View v) {
                        if (flag) {
                            tv.setVisibility(View.VISIBLE);
                            flag = !flag;
                        } else {
                            tv.setVisibility(View.GONE);
                            flag = !flag;
                        }
                    }
                });
                if (ll == null) {
                    System.out.println("ll==null");
                }
                if (b == null) {
                    System.out.println("b==null");
                }
                ll.addView(b, new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                ll.addView(tv, new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
            }

            alert1.setPositiveButton("确定", (dialog, which) -> {
            });
            alert1.create().show();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
//        ConstantPool.isQuit=true;
//        for(Condition c:ConstantPool.conditions){
//            c.signalAll();
//        }
//        finish();

        ConstantPool.isQuit = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Turn.turn = 0;
                ConstantPool.clearAll();
            }
        },100);

        super.onBackPressed();
    }

    private String ReadAssetFile(String path) {
        AssetManager am = getAssets();
        StringBuilder sb = new StringBuilder();
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(am.open(path)));

            String line = new String();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bo_bo);

        Gson gson = new Gson();
        am = getAssets();
        context = this;
        activity = this;
        System.out.println("成功进入UI加载进程");
        //设置配置
        config = gson.fromJson(ReadAssetFile("Config.json"), Config.class);
        ConstantPool.init();
        //加载控件
        tvw_dd = findViewById(R.id.bostatus_DD);
        tvw_HP = findViewById(R.id.bostatus_HP);
        tvw_skill = findViewById(R.id.bostatus_skill);
        tvw_turns = findViewById(R.id.boturnnum);

        Player myplayer = ConstantPool.players.get(0);
        Pack mypack = ConstantPool.packs.get(0);
        Ctrl myctrl = ConstantPool.ctrls[0];
        //设置容器
        myctrl.setContainer(findViewById(R.id.towerskills), findViewById(R.id.absorbskills));
        //读技能
        AssetManager am = getAssets();
        String[] skillpaths = new String[]{};
        try {
            skillpaths = am.list("commonskills");
            for (String path : skillpaths) {
                BufferedReader br = new BufferedReader(new InputStreamReader(am.open("commonskills/" + path)));
                StringBuilder sb = new StringBuilder();
                String line = new String();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                System.out.println(path);
                Skill skill = gson.fromJson(sb.toString(), Skill.class);
                U_idToskill.put(skill.id, skill);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //读完

        //下面为添加按钮
        FlexboxLayout commonskillslayout = findViewById(R.id.commonskills);
        for (int id : U_idToskill.keySet()) {
            Button b = new Button(this);
            b.setText(U_idToskill.get(id).name);
            b.setBackgroundResource(R.drawable.button_background);
            b.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            b.setEnabled(false);
            b.setVisibility(View.VISIBLE);
            commonskillslayout.addView(b,
                    new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT,
                            FlexboxLayout.LayoutParams.WRAP_CONTENT));
            U_idTobutton.put(id, b);
            U_buttonToid.put(b, id);
            //技能按钮点击事件
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("点击了" + U_idToskill.get(U_buttonToid.get(b)).name);
                    if (!isChanging && !isTower) {//如果不处在第二阶段
                        mypack.skill = U_idToskill.get(U_buttonToid.get(b));
                        mypack.times = 1;
                        mypack.isOK = true;
                        System.out.println("isOK=true");
                        mypack.source = Source.C;
                        mypack.description += mypack.skill.description;
                        if (!mypack.skill.ismixture) {
                            myplayer.ddnum -= mypack.skill.cost;
                        } else {
                            for (HashMap<Integer, Integer> possiblecombo : mypack.skill.mixedof) {
                                boolean isCombo = true;
                                for (int ids : possiblecombo.keySet()) {//检查是哪个组合
                                    if (myctrl.historyskills.containsKey(BoBoActivity.U_idToskill.get(ids))) {
                                        if (myctrl.historyskills.get(BoBoActivity.U_idToskill.get(ids)) < possiblecombo.get(ids)) {//如果组合中有一个技能项不符合
                                            isCombo = false;
                                        }
                                    } else {
                                        isCombo = false;
                                    }
                                }
                                //如果isCombo仍然为true
                                if (isCombo) {
                                    //进行相应技能的扣除
                                    for (int ids : possiblecombo.keySet()) {
                                        myctrl.historyskills.put(BoBoActivity.U_idToskill.get(ids), myctrl.historyskills.get(BoBoActivity.U_idToskill.get(ids)) - possiblecombo.get(ids));//扣除
                                    }
                                    break;
                                }
                            }
                        }
                    } else if (isChanging) {
                        for (Button b : myctrl.A_skillTobutton.values()) {
                            b.setEnabled(true);
                        }
                        for (Button b : myctrl.T_skillTobutton.values()) {
                            b.setEnabled(true);
                        }
                        mypack.changeskill = mypack.skill;
                        mypack.skill = U_idToskill.get(U_buttonToid.get(b));
                        mypack.times = 1;
                        mypack.source = Source.C;
                        mypack.isChanged = true;
                        mypack.isOK = true;
                        System.out.println("isOK=true");
                    } else if (isTower) {//自己出塔，吸收技能时
                        mypack.description = mypack.description + "吸收" + U_idToskill.get(U_buttonToid.get(b)).name + " ";
                        for (Button b : myctrl.A_skillTobutton.values()) {
                            b.setEnabled(true);
                        }
                        for (Button b : myctrl.T_skillTobutton.values()) {
                            b.setEnabled(true);
                        }
                        int absorbtimes = 0;
                        for (Pack pack : ConstantPool.packs) {
                            if (ConstantPool.outset.contains(pack)) {
                                continue;
                            }

                            if (pack.skill.id == U_buttonToid.get(b)) {
                                for (int i = 0; i < mypack.times * pack.times; i++) {
                                    absorbtimes += mypack.skill.tAbsorbTimes;
                                }
                            }
                        }
                        if (myctrl.tAarea.containsKey(U_idToskill.get(U_buttonToid.get(b)))) {
                            myctrl.tAarea.put(U_idToskill.get(U_buttonToid.get(b)), myctrl.tAarea.get(U_idToskill.get(U_buttonToid.get(b))) + absorbtimes);
                        } else {
                            myctrl.tAarea.put(U_idToskill.get(U_buttonToid.get(b)), absorbtimes);
                        }

                    }
                    mypack.isOK = true;
                    System.out.println("isOK=true");
                }
            });
        }
        commonskillslayout.requestLayout();
        commonskillslayout.invalidate();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myctrl.updateCommon(0, null, 0);//初始化
                        myctrl.updateAbsorb();//初始化
                    }
                });
                while (true) {
                    for (Bot bot : ConstantPool.bots) {
                        if (ConstantPool.outset.contains(bot.mypack)) {
                            continue;
                        }
                        bot.run(Bot.CHOOSE_SKILL);
                    }
                    mypack.isOK = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvw_turns.setText("当前回合数：" + Turn.turn);
                        }
                    });
                    System.out.println("等待isOK");
                    while (!mypack.isOK) ;//等待玩家做出操作
                    System.out.println("等待isOK完毕");
                    //做出操作之后，更新UI，skill
                    StringBuilder sb = new StringBuilder();
                    for (Pack pack : ConstantPool.packs) {
                        if (ConstantPool.outset.contains(pack)) {
                            continue;
                        }
                        sb.append(pack.player.name + "使用了" + pack.skill.name);
                        if (!pack.description.equals("")) {
                            sb.append("(");
                            sb.append(pack.description);
                            sb.append(")");
                        }
                        sb.append('\n');
                    }
                    Message msg1 = Message.obtain();
                    msg1.what = 1;
                    msg1.obj = sb.toString();
                    mHandler.sendMessage(msg1);
                    //更新dd
                    StringBuilder sb2 = new StringBuilder();
                    for (Pack pack : ConstantPool.packs) {
                        if (ConstantPool.outset.contains(pack)) {
                            continue;
                        }
                        sb2.append(pack.player.name + "的dd数：" + pack.player.ddnum + '\n');
                    }
                    Message msg2 = Message.obtain();
                    msg2.what = 2;
                    msg2.obj = sb2.toString();
                    mHandler.sendMessage(msg2);
                    //更新HP
                    StringBuilder sb3 = new StringBuilder();
                    for (Pack pack : ConstantPool.packs) {
                        if (ConstantPool.outset.contains(pack)) {
                            continue;
                        }
                        sb3.append(pack.player.name + "剩余HP：" + pack.player.HP + '\n');
                    }
                    Message msg3 = Message.obtain();
                    msg3.what = 3;
                    msg3.obj = sb3.toString();
                    mHandler.sendMessage(msg3);
                    //第一次循环结束，开始第二次
                    for (Bot bot : ConstantPool.bots) {
                        if (ConstantPool.outset.contains(bot.mypack)) {
                            continue;
                        }
                        bot.run(Bot.BEFORE_CHANGE);
                    }
                    {
                        StringBuilder sb11 = new StringBuilder();
                        for (Pack pack : ConstantPool.packs) {
                            if (ConstantPool.outset.contains(pack)) {
                                continue;
                            }
                            sb11.append(pack.player.name + "使用了" + pack.skill.name);
                            if (!pack.description.equals("")) {
                                sb11.append("(");
                                sb11.append(pack.description);
                                sb11.append(")");
                            }
                            sb11.append('\n');
                        }
                        Message msg11 = Message.obtain();
                        msg11.what = 1;
                        msg11.obj = sb.toString();
                        mHandler.sendMessage(msg11);
                    }
                    //第二次循环
                    if (mypack.skill.isChangeable) {
                        mypack.isOK = false;
                        isChanging = true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int ids : U_idTobutton.keySet()) {
                                    U_idTobutton.get(ids).setEnabled(false);
                                }
                                for (Button b : myctrl.A_skillTobutton.values()) {
                                    b.setEnabled(false);
                                }
                                for (Button b : myctrl.T_skillTobutton.values()) {
                                    b.setEnabled(false);
                                }
                                for (int ids : mypack.skill.changelist) {
                                    U_idTobutton.get(ids).setEnabled(true);
                                }
                            }
                        });

                        while (!mypack.isOK) ;//等待做出决策
                        isChanging = false;
                    }

                    for (Bot bot : ConstantPool.bots) {
                        if (ConstantPool.outset.contains(bot.mypack)) {
                            continue;
                        }
                        bot.run(Bot.CHANGE_TIMES);
                    }
                    {
                        StringBuilder sb11 = new StringBuilder();
                        for (Pack pack : ConstantPool.packs) {
                            if (ConstantPool.outset.contains(pack)) {
                                continue;
                            }
                            sb11.append(pack.player.name + "使用了" + pack.skill.name);
                            if (!pack.description.equals("")) {
                                sb11.append("(");
                                sb11.append(pack.description);
                                sb11.append(")");
                            }
                            sb11.append('\n');
                        }
                        Message msg11 = Message.obtain();
                        msg11.what = 1;
                        msg11.obj = sb.toString();
                        mHandler.sendMessage(msg11);
                    }
                    if (mypack.source == Source.T) {//如果是塔来源
                        mypack.isOK = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                LinearLayout ll = findViewById(R.id.towerchoose);
                                ll.setVisibility(View.VISIBLE);
                                RollInt ri = new RollInt(1, myctrl.tAarea.get(mypack.skill));
                                ri.setp(1);
                                TextView tvw = findViewById(R.id.toweramount);
                                Button sub = findViewById(R.id.towersub);
                                sub.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ri.sub();
                                        tvw.setText(String.valueOf(ri.getp()));
                                    }
                                });
                                Button add = findViewById(R.id.toweradd);
                                add.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ri.add();
                                        tvw.setText(String.valueOf(ri.getp()));
                                    }
                                });
                                tvw.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        for (Button button : myctrl.T_skillTobutton.values()) {
                                            button.setEnabled(true);
                                        }
                                        for (Button button : myctrl.A_skillTobutton.values()) {
                                            button.setEnabled(true);
                                        }
                                        mypack.times = ri.getp();
                                        mypack.description = mypack.description + "*" + mypack.times + " ";
                                        myctrl.tAarea.put(mypack.skill, myctrl.tAarea.get(mypack.skill) - mypack.times);
                                        tvw.setText("1");
                                        mypack.isOK = true;
                                        ll.setVisibility(View.GONE);
                                    }
                                });
                            }
                        });
                        while (!mypack.isOK) ;

                    }
                    for (Bot bot : ConstantPool.bots) {
                        if (ConstantPool.outset.contains(bot.mypack)) {
                            continue;
                        }
                        bot.run(Bot.CHOOSE_T_ABSORB);
                    }
                    {
                        StringBuilder sb11 = new StringBuilder();
                        for (Pack pack : ConstantPool.packs) {
                            if (ConstantPool.outset.contains(pack)) {
                                continue;
                            }
                            sb11.append(pack.player.name + "使用了" + pack.skill.name);
                            if (!pack.description.equals("")) {
                                sb11.append("(");
                                sb11.append(pack.description);
                                sb11.append(")");
                            }
                            sb11.append('\n');
                        }
                        Message msg11 = Message.obtain();
                        msg11.what = 1;
                        msg11.obj = sb.toString();
                        mHandler.sendMessage(msg11);
                    }
                    if (!mypack.skill.tAbsorb.isEmpty()) {//如果可以t吸收技能
                        mypack.isOK = false;
                        isTower = true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (Button b : U_idTobutton.values()) {
                                    b.setEnabled(false);
                                }
                                for (Button b : myctrl.A_skillTobutton.values()) {
                                    b.setEnabled(false);
                                }
                                for (Button b : myctrl.T_skillTobutton.values()) {
                                    b.setEnabled(false);
                                }
                                boolean hasTower = false;
                                for (int ids : mypack.skill.tAbsorb) {
                                    for (Pack pack : ConstantPool.packs) {
                                        if (ConstantPool.outset.contains(pack)) {
                                            continue;
                                        }
                                        if (pack.skill.id == ids) {
                                            hasTower = true;//是否有可以吸收的技能
                                            U_idTobutton.get(ids).setEnabled(true);
                                        }
                                    }
                                }
                                if (!hasTower) {
                                    for (Button b : myctrl.A_skillTobutton.values()) {
                                        b.setEnabled(true);
                                    }
                                    for (Button b : myctrl.T_skillTobutton.values()) {
                                        b.setEnabled(true);
                                    }
                                    mypack.isOK = true;
                                }
                            }
                        });
                        while (!mypack.isOK) ;
                        isTower = false;
                    }
                    //第二轮结束，进入判定阶段
                    for (Pack atker : ConstantPool.packs) {//攻击者（heal，gain等都在此处判定）
                        if (ConstantPool.outset.contains(atker)) {
                            continue;
                        }
                        for (int i = 0; i < atker.times; i++) {
                            atker.player.HP += atker.skill.Heal;
                            atker.player.ddnum += atker.skill.gain;
                        }
                        for (Pack defender : ConstantPool.packs) {//防御者
                            if (ConstantPool.outset.contains(defender)) {
                                continue;
                            }
                            boolean nodie = false;
                            if (atker.isChanged) {
                                if (atker.changeskill != null && defender.skill.Absorb.contains(atker.changeskill.id)) {
                                    nodie = true;
                                }
                                if (defender.changeskill != null) {
                                    nodie = true;
                                }
                            }
                            if (!nodie && atker.skill.Atk.containsKey(defender.skill.id)) {//判定攻防
                                if (atker.skill.Atk.get(defender.skill.id) < config.maxDamagedOnce) {
                                    defender.player.HP -= atker.skill.Atk.get(defender.skill.id);
                                    defender.maxdamaged = atker.skill.Atk.get(defender.skill.id);
                                } else {
                                    if (atker.skill.Atk.get(defender.skill.id) > defender.maxdamaged) {
                                        defender.player.HP = defender.player.HP - atker.skill.Atk.get(defender.skill.id) + defender.maxdamaged;
                                        defender.maxdamaged = atker.skill.Atk.get(defender.skill.id);
                                    }
                                }

                            }
                            if (atker.skill.Absorb.contains(defender.skill.id)) {//判断A吸收
                                if (ConstantPool.ctrls[atker.player.id].Aarea.containsKey(defender.skill)) {
                                    ConstantPool.ctrls[atker.player.id].Aarea.put(defender.skill, ConstantPool.ctrls[atker.player.id].Aarea.get(defender.skill) + defender.times);
                                } else {
                                    ConstantPool.ctrls[atker.player.id].Aarea.put(defender.skill, 1);
                                }
                            }
                            if (defender.changeskill != null && atker.skill.Absorb.contains(defender.changeskill.id)) {//判断A吸收（变）
                                if (ConstantPool.ctrls[atker.player.id].Aarea.containsKey(defender.changeskill)) {
                                    ConstantPool.ctrls[atker.player.id].Aarea.put(defender.changeskill, ConstantPool.ctrls[atker.player.id].Aarea.get(defender.changeskill) + defender.times);
                                } else {
                                    ConstantPool.ctrls[atker.player.id].Aarea.put(defender.changeskill, defender.times);
                                }
                            }
                            if (atker.skill.sAbsorb.contains(defender.skill.id)) {//判定s吸收
                                if (!defender.isstolen) {
                                    for (int i = 0; i < defender.times; i++) {
                                        defender.player.HP -= defender.skill.Heal;
                                        defender.player.ddnum -= defender.skill.gain;
                                        defender.isstolen = true;
                                    }
                                }
                                for (int i = 0; i < defender.times; i++) {
                                    atker.player.HP += defender.skill.Heal;
                                    atker.player.HP += defender.skill.gain;
                                }
                            }
                            if (atker.skill.Locklist.contains(defender.skill.id)) {
                                Interval interval = new Interval(atker.skill.Lockturns + 1);
                                ConstantPool.ctrls[defender.player.id].interSkill.put(interval, defender.skill);
//                                ConstantPool.ctrls[defender.player.id].Locklist.add(defender.skill);
                            }
                        }
                    }
                    //做出操作之后，更新UI，skill
                    StringBuilder sb4 = new StringBuilder();
                    for (Pack pack : ConstantPool.packs) {
                        if (ConstantPool.outset.contains(pack)) {
                            continue;
                        }
                        sb4.append(pack.player.name + "使用了" + pack.skill.name);
                        if (!pack.description.equals("")) {
                            sb4.append("(");
                            sb4.append(pack.description);
                            sb4.append(")");
                        }
                        sb4.append('\n');
                    }
                    Message msg4 = Message.obtain();
                    msg4.what = 1;
                    msg4.obj = sb4.toString();
                    mHandler.sendMessage(msg4);
                    //更新dd
                    StringBuilder sb5 = new StringBuilder();
                    for (Pack pack : ConstantPool.packs) {
                        if (ConstantPool.outset.contains(pack)) {
                            continue;
                        }
                        sb5.append(pack.player.name + "的dd数：" + pack.player.ddnum + '\n');
                    }
                    Message msg5 = Message.obtain();
                    msg5.what = 2;
                    msg5.obj = sb5.toString();
                    mHandler.sendMessage(msg5);
                    //更新HP
                    StringBuilder sb6 = new StringBuilder();
                    for (Pack pack : ConstantPool.packs) {
                        if (ConstantPool.outset.contains(pack)) {
                            continue;
                        }
                        sb6.append(pack.player.name + "剩余HP：" + pack.player.HP + '\n');
                    }
                    Message msg6 = Message.obtain();
                    msg6.what = 3;
                    msg6.obj = sb6.toString();
                    mHandler.sendMessage(msg6);
                    for (Pack pack : ConstantPool.packs) {
                        if (pack.player.HP <= 0) {
                            ConstantPool.outset.add(pack);
                        }
                    }
                    //判定结束，进入三阶段
                    Skill waitskill = mypack.skill;//sb延时，只能这么玩了
                    int waittimes = mypack.times;//sb延时，只能这么玩了
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mypack.skill == null) {
                                System.out.println("null!!!!!!!!!!!");
                            } else {
                                System.out.println(mypack.skill.name);
                            }
                            myctrl.updateCommon(myplayer.ddnum, waitskill, waittimes);
                            myctrl.updateAbsorb();
                        }
                    });

                    if (myplayer.HP <= 0) {
                        mypack.isOK = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder alert = new AlertDialog.Builder(BoBoActivity.this);
                                alert.setTitle("您血量已归零，是否重来？");
                                alert.setPositiveButton("重来", (dialog, which) -> {
                                    ConstantPool.outset.clear();

                                    for (Pack pack : ConstantPool.packs) {

                                        pack.TurnOver();
                                        pack.player.hasrestarted = false;
                                        pack.player.ddnum = 0;
                                        pack.player.HP = config.initialHP;


                                    }
                                    Turn.turn = -1;
                                    for (Ctrl ctrl : ConstantPool.ctrls) {
                                        for (Skill skill : ctrl.Aarea.keySet()) {
                                            ctrl.Aarea.put(skill, 0);
                                        }
                                        for (Skill skill : ctrl.tAarea.keySet()) {
                                            ctrl.tAarea.put(skill, 0);
                                        }
                                        ctrl.updateAbsorb();
                                        ctrl.clear();
                                        ctrl.updateCommon(0, null, 0);
                                        ctrl.updateAbsorb();
                                    }
                                    mypack.isOK = true;


                                });
                                alert.setNegativeButton("退出", (dialog, which) -> {
                                    ConstantPool.isQuit = true;
                                    Turn.turn = 0;
                                    ConstantPool.clearAll();
                                    finish();
                                });
                                alert.create().show();

                            }
                        });
                        while (!mypack.isOK) ;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvw_turns.setText("当前回合数：" + Turn.turn);
                                tvw_skill.setText("");
                                tvw_dd.setText("");
                                tvw_HP.setText("");
                            }
                        });

                    }


                    if (ConstantPool.outset.size() == ConstantPool.botnum && !ConstantPool.outset.contains(mypack)) {
                        mypack.isOK = false;
                        System.out.println("结束！！！！！！！！！！！！");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder alert = new AlertDialog.Builder(BoBoActivity.this);
                                alert.setTitle("您已胜利，是否重来？");
                                alert.setPositiveButton("重来", (dialog, which) -> {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            for (Pack pack : ConstantPool.packs) {
                                                ConstantPool.outset.clear();
                                                pack.TurnOver();
                                                pack.player.hasrestarted = false;
                                                pack.player.ddnum = 0;
                                                pack.player.HP = config.initialHP;
                                                Turn.turn = -1;
                                            }
                                            for (Ctrl ctrl : ConstantPool.ctrls) {
                                                for (Skill skill : ctrl.Aarea.keySet()) {
                                                    ctrl.Aarea.put(skill, 0);
                                                }
                                                for (Skill skill : ctrl.tAarea.keySet()) {
                                                    ctrl.tAarea.put(skill, 0);
                                                }
                                                ctrl.updateAbsorb();
                                                ctrl.clear();
                                                ctrl.updateCommon(0, null, 0);
                                                ctrl.updateAbsorb();
                                            }
                                            mypack.isOK = true;
//                                            tvw_turns.setText("当前回合数：" + Turn.turn);
                                            tvw_skill.setText("");
                                            tvw_dd.setText("");
                                            tvw_HP.setText("");
                                        }
                                    });

                                });
                                alert.setNegativeButton("退出", (dialog, which) -> {
                                    ConstantPool.isQuit = true;
                                    Turn.turn = 0;
                                    ConstantPool.clearAll();
                                    finish();
                                });
                                alert.create().show();
                            }
                        });
                        while (!mypack.isOK) ;
                    }
                    //第三阶段开始
                    for (Bot bot : ConstantPool.bots) {
                        if (ConstantPool.outset.contains(bot.mypack)) {
                            continue;
                        }
                        bot.run(Bot.TURN_END);
                    }
                    if (myplayer.HP > 0 && myplayer.HP <= config.restartHP && !myplayer.hasrestarted) {
                        mypack.isOK = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder alert = new AlertDialog.Builder(BoBoActivity.this);
                                alert.setTitle("您血量仅剩不多，是否重来？");
                                alert.setPositiveButton("重来", (dialog, which) -> {
                                    mypack.player.hasrestarted = true;
                                    for (Pack pack : ConstantPool.packs) {
                                        if (ConstantPool.outset.contains(pack)) {
                                            continue;
                                        }
                                        pack.TurnOver();
                                        pack.player.ddnum = 0;
                                    }
                                    for (Ctrl ctrl : ConstantPool.ctrls) {
                                        for (Skill skill : ctrl.Aarea.keySet()) {
                                            ctrl.Aarea.put(skill, 0);
                                        }
                                        for (Skill skill : ctrl.tAarea.keySet()) {
                                            ctrl.tAarea.put(skill, 0);
                                        }
                                        ctrl.updateAbsorb();
                                        ctrl.clear();
                                        ctrl.updateCommon(0, null, 0);
                                        ctrl.updateAbsorb();
                                    }
                                    Turn.turn = -1;
                                    mypack.isOK = true;
                                    tvw_dd.setText("");

                                });
                                alert.setNegativeButton("继续", (dialog, which) -> {
                                    mypack.player.hasrestarted = true;
                                    mypack.isOK = true;
                                });
                                alert.create().show();
                            }
                        });

                        while (!mypack.isOK) ;
                    }
                    System.out.println("myplayer.HP=" + myplayer.HP);
                    for (Pack pack : ConstantPool.packs) {
                        pack.TurnOver();
                    }
                    Turn.TurnOver();
                }
            }
        });
        Button BEGIN = findViewById(R.id.BEGINbo);
        BEGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BEGIN.setEnabled(false);
                for (Bot bot : ConstantPool.bots) {
                    bot.initAll();
                }
                t.start();
            }
        });
    }


}