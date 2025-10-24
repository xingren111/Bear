package com.bear.DD.BoBo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Looper;

import com.bear.DD.Prob;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.Condition;

public class Bot {

    public static final int CHOOSE_SKILL = 1;
    public static final int BEFORE_CHANGE = 2;
    public static final int CHANGE_TIMES = 3;
    public static final int CHOOSE_T_ABSORB = 4;
    public static final int TURN_END = 5;
    ArrayList<BotSkill> U_Botskill = new ArrayList<BotSkill>();
    Condition condition;
    Player myplayer;
    Pack mypack;
    Ctrl myctrl;
    int id;

    public Bot(int idsubone) {
        System.out.println("idsubone:" + idsubone);
        myplayer = ConstantPool.players.get(idsubone + 1);
        mypack = ConstantPool.packs.get(idsubone + 1);
        myctrl = ConstantPool.ctrls[idsubone + 1];
        this.id = idsubone + 1;
    }
    public void initAll(){
        myctrl.updateCommon(0, null, 0);//初始化
        myctrl.updateAbsorb();//初始化
        Gson gson = new Gson();
        //读技能

        String[] skillpaths = new String[]{};
        try {
            skillpaths = BoBoActivity.am.list("botskills");
            for (String path : skillpaths) {
                BufferedReader br = new BufferedReader(new InputStreamReader(BoBoActivity.am.open("botskills/" + path)));
                StringBuilder sb = new StringBuilder();
                String line = new String();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                BotSkill botskill = gson.fromJson(sb.toString(), BotSkill.class);
                U_Botskill.add(botskill);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void clearAll(){
        U_Botskill.clear();
        myctrl.clear();
        mypack=null;
        myplayer=null;
    }
    public void run(int stage) {



        switch (stage) {
            case CHOOSE_SKILL:
                //开始决策
                Source source;
                Prob<Skill> prob = new Prob<Skill>();//概率模型
                System.out.println(myctrl.AbleCommonSkills.isEmpty());
                for (BotSkill botSkill : U_Botskill) {
                    if (!myctrl.AbleCommonSkills.contains(BoBoActivity.U_idToskill.get(botSkill.id))//如果可用技能列表不存在就跳过
                            && !myctrl.AbleAbsorbSkills.contains(BoBoActivity.U_idToskill.get(botSkill.id))) {
                        continue;
                    }
                    int weight = botSkill.basicweight;
                    for (ArrayList<Integer> weightcondition : botSkill.spcweight) {
                        if (weightcondition.size() == 0) {
                            break;
                        }
                        //开始进行权重增减
                        switch (weightcondition.get(0)) {
                            case 1:
                                if (1 + Turn.turn == weightcondition.get(1)) {//加一很抽象但是是这样的
                                    weight += weightcondition.get(2);
                                }
                                break;
                            case 2:
                                if (1 + Turn.turn >= weightcondition.get(1)) {
                                    weight += weightcondition.get(2);
                                }
                                break;
                            case 11:
                                if (Turn.getMaxDDinAllPlayers(myplayer) >= weightcondition.get(1)) {
                                    weight += weightcondition.get(2);
                                }
                                break;
                            case 12:
                                if (Turn.getMaxDDinAllPlayers(myplayer) <= weightcondition.get(1)) {
                                    weight += weightcondition.get(2);
                                }
                                break;
                            case 13:
                                if (myplayer.ddnum >= weightcondition.get(1)) {
                                    weight += weightcondition.get(2);
                                }
                                break;
                            case 14:
                                if (myplayer.ddnum <= weightcondition.get(1)) {
                                    weight += weightcondition.get(2);
                                }
                                break;
                            case 21:
                                if (Turn.isSkillExist(myplayer, BoBoActivity.U_idToskill.get(weightcondition.get(1)))) {
                                    weight += weightcondition.get(2);
                                }
                                break;
                            case 22:
                                if (!Turn.isSkillExist(myplayer, BoBoActivity.U_idToskill.get(weightcondition.get(1)))) {
                                    weight += weightcondition.get(2);
                                }
                                break;
                            case 23:
                                Skill skill = BoBoActivity.U_idToskill.get(weightcondition.get(1));
                                if (myplayer.ddnum >= skill.cost && !skill.ismixture) {
                                    weight += weightcondition.get(2);
                                    break;
                                }
                                if (skill.ismixture) {
                                    for (HashMap<Integer, Integer> possibleSkillcombo : skill.mixedof) {//遍历全部可能组合
                                        boolean isCombo = true;
                                        for (int ids : possibleSkillcombo.keySet()) {//遍历一个可能的组合
                                            if (myctrl.historyskills.containsKey(BoBoActivity.U_idToskill.get(ids))) {//如果历史记录中包含全集中某个技能
                                                if (possibleSkillcombo.get(ids) >= myctrl.historyskills.get(BoBoActivity.U_idToskill.get(ids))) {//如果历史记录中这个技能的数量小于等于组合技所需技能数
                                                    isCombo = false;
                                                }
                                            }
                                        }
                                        if (isCombo) {//如果遍历结束后仍然全部符合要求
                                            weight += weightcondition.get(2);
                                            break;
                                        }
                                    }
                                }
                                if ((myctrl.getAarea().containsKey(skill) && myctrl.getAarea().get(skill) > 0) || (myctrl.gettAarea().containsKey(skill) && myctrl.gettAarea().get(skill) > 0)) {
                                    weight += weightcondition.get(2);
                                    break;
                                }
                                break;
                            case 24:
                                Skill skill1 = BoBoActivity.U_idToskill.get(weightcondition.get(1));
                                if (myplayer.ddnum >= skill1.cost && !skill1.ismixture) {
                                    break;
                                }
                                if (skill1.ismixture) {
                                    for (HashMap<Integer, Integer> possibleSkillcombo : skill1.mixedof) {//遍历全部可能组合
                                        boolean isCombo = true;
                                        for (int ids : possibleSkillcombo.keySet()) {//遍历一个可能的组合
                                            if (myctrl.historyskills.containsKey(BoBoActivity.U_idToskill.get(ids))) {//如果历史记录中包含全集中某个技能
                                                if (possibleSkillcombo.get(ids) >= myctrl.historyskills.get(BoBoActivity.U_idToskill.get(ids))) {//如果历史记录中这个技能的数量小于等于组合技所需技能数
                                                    isCombo = false;
                                                }
                                            }
                                        }
                                        if (isCombo) {//如果遍历结束后仍然全部符合要求
                                            break;
                                        }
                                    }
                                }
                                if ((myctrl.getAarea().containsKey(skill1) && myctrl.getAarea().get(skill1) > 0) || (myctrl.gettAarea().containsKey(skill1) && myctrl.gettAarea().get(skill1) > 0)) {
                                    break;
                                }
                                weight += weightcondition.get(2);
                                break;
                            case 31:
                                ArrayList<Pack> powerspack = new ArrayList<Pack>();
                                ArrayList<Integer> powersint = new ArrayList<Integer>();
                                for (int i = 0; i <= ConstantPool.botnum; i++) {
                                    powerspack.add(ConstantPool.packs.get(i));
                                    powersint.add(powerspack.get(i).calcPower());
                                }
                                //排序
                                for (int i = 0; i < ConstantPool.botnum; i++) {
                                    for (int j = 0; j < ConstantPool.botnum - i; j++) {
                                        if (powersint.get(j) < powersint.get(j + 1)) {
                                            Pack p = powerspack.get(j + 1);
                                            powerspack.set(j + 1, powerspack.get(j));
                                            powerspack.set(j, p);
                                            int a = powersint.get(j + 1);
                                            powersint.set(j + 1, powersint.get(j));
                                            powersint.set(j, a);
                                        }

                                    }
                                }
                                for (int i = 0; i <= ConstantPool.botnum; i++) {
                                    if (powerspack.get(i) == mypack) {
                                        if (Math.round((double) 1.0 * (i + 1) / (ConstantPool.botnum + 1)) >= ((double) 1.0 * weightcondition.get(1) / 100)) {
                                            weight += weightcondition.get(2);
                                            break;
                                        }
                                    }
                                }
                            case 32:
                                ArrayList<Pack> powerspack1 = new ArrayList<Pack>();
                                ArrayList<Integer> powersint1 = new ArrayList<Integer>();
                                for (int i = 0; i <= ConstantPool.botnum; i++) {
                                    powerspack1.add(ConstantPool.packs.get(i));
                                    powersint1.add(powerspack1.get(i).calcPower());
                                }
                                //排序
                                for (int i = 0; i < ConstantPool.botnum; i++) {
                                    for (int j = 0; j < ConstantPool.botnum - i; j++) {
                                        if (powersint1.get(j) < powersint1.get(j + 1)) {
                                            Pack p = powerspack1.get(j + 1);
                                            powerspack1.set(j + 1, powerspack1.get(j));
                                            powerspack1.set(j, p);
                                            int a = powersint1.get(j + 1);
                                            powersint1.set(j + 1, powersint1.get(j));
                                            powersint1.set(j, a);
                                        }

                                    }
                                }
                                for (int i = 0; i <= ConstantPool.botnum; i++) {
                                    if (powerspack1.get(i) == mypack) {
                                        if (Math.round((double) 1.0 * (i + 1) / (ConstantPool.botnum + 1)) <= ((double) 1.0 * weightcondition.get(1) / 100)) {
                                            weight += weightcondition.get(2);
                                            break;
                                        }
                                    }
                                }
                            case 41:
                                if (Turn.livingPlayers() >= weightcondition.get(1)) {
                                    weight += weightcondition.get(2);
                                    break;
                                }
                            case 42:
                                if (Turn.livingPlayers() <= weightcondition.get(2)) {
                                    weight += weightcondition.get(2);
                                    break;
                                }
                        }

                    }
                    if (weight < 0) {
                        weight = 0;
                    }
                    System.out.printf("%s的权重是%d\n", BoBoActivity.U_idToskill.get(botSkill.id).name, weight);
                    prob.addfreq(BoBoActivity.U_idToskill.get(botSkill.id), weight);//添加权重至概率模型
                }
                try {
                    prob.updateprob();
                } catch (ArithmeticException e) {
                    myplayer.HP = 0;
                    ConstantPool.outset.add(mypack);
                }
                Skill myskill = prob.getNeed();//获取将出技能
                mypack.description += myskill.description;
                //判定来源
                if (myctrl.Aarea.containsKey(myskill) && myctrl.Aarea.get(myskill) > 0) {
                    myctrl.Aarea.put(myskill, myctrl.Aarea.get(myskill) - 1);
                    source = Source.A;
                    mypack.skill = myskill;
                    mypack.description += "来自吸收区";
                    mypack.times = 1;
                    mypack.isOK = true;
                    mypack.source = source;
                } else if (myctrl.tAarea.containsKey(myskill) && myctrl.tAarea.get(myskill) > 0) {
                    source = Source.T;
                    mypack.skill = myskill;
                    mypack.isOK = true;
                    mypack.source = source;
                } else {
                    source = Source.C;
                    mypack.skill = myskill;
                    mypack.times = 1;
                    mypack.source = source;
                    if (!myskill.ismixture) {
                        myplayer.ddnum -= myskill.cost;
                    } else {//如果是组合技
                        for (HashMap<Integer, Integer> possiblecombo : myskill.mixedof) {
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
                            }
                        }
                    }
                    mypack.description = myskill.description;
                    mypack.isOK = true;
                }
                break;
            case BEFORE_CHANGE:
                //第一轮结束，开始第二轮
                //判断change
                if (mypack.skill.isChangeable) {
                    mypack.isOK = false;
                    mypack.isChanged = true;
                    //模拟
                    ArrayList<Pack> waitforsimulate = new ArrayList<Pack>();
                    for (Pack pack : ConstantPool.packs) {
                        if (ConstantPool.outset.contains(pack)) {
                            continue;
                        }
                        if (!pack.skill.isChangeable &&
                                !pack.skill.Absorb.contains(pack.skill.id)) {
                            waitforsimulate.add(pack);
                        }
                    }
                    mypack.changeskill = mypack.skill;
                    mypack.skill = ConstantPool.Simulate(mypack, waitforsimulate);
                    mypack.isOK = true;
                }
                break;
            case CHANGE_TIMES:
                //调倍率
                if (mypack.source == Source.T) {
                    mypack.isOK = false;
                    int maxtimes = 0;
                    for (Pack pack : ConstantPool.packs) {
                        if (ConstantPool.outset.contains(pack)) {
                            continue;
                        }
                        if (mypack.skill.Atk.containsKey(pack.skill.id)) {
                            if (mypack.skill.Atk.get(pack.skill.id) < BoBoActivity.config.maxDamagedOnce) {
                                if (myctrl.historyskills.get(mypack.skill) >= pack.player.HP) {
                                    int times = (int) Math.ceil(1.0 * pack.player.HP / mypack.skill.Atk.get(pack.skill.id));
                                    maxtimes = Math.max(times, maxtimes);
                                } else {
                                    int times = myctrl.historyskills.get(mypack.skill);
                                    maxtimes = Math.max(times, maxtimes);
                                }
                            } else {
                                int times = 1;
                                maxtimes = Math.max(times, maxtimes);
                            }
                        }
                    }
                    if (maxtimes == 0) {
                        Random r = new Random(System.currentTimeMillis());
                        maxtimes = r.nextInt(myctrl.tAarea.get(mypack.skill)) + 1;
                    }
                    mypack.times = maxtimes;
                    mypack.description += "*" + mypack.times;
                    myctrl.tAarea.put(mypack.skill, myctrl.tAarea.get(mypack.skill) - mypack.times);
                    mypack.isOK = true;
                }
                break;
            case CHOOSE_T_ABSORB:
                //判断塔
                if (!mypack.skill.tAbsorb.isEmpty()) {
                    mypack.isOK = false;
                    Prob<Skill> p = new Prob<Skill>();
                    for (Pack pack : ConstantPool.packs) {
                        if (ConstantPool.outset.contains(pack)) {
                            continue;
                        }
                        if (mypack.skill.tAbsorb.contains(pack.skill.id)) {
                            if (p.freq.containsKey(pack.skill)) {
                                p.freq.put(pack.skill, p.freq.get(pack.skill) + pack.skill.power);
                            } else {
                                p.freq.put(pack.skill, pack.skill.power);
                            }
                        }
                    }
                    Skill tabsorbskill = null;
                    boolean isNull = true;
                    for (int length : p.freq.values()) {
                        if (length != 0) {
                            isNull = false;
                        }
                    }
                    if (!isNull) {
                        p.updateprob();//what can I say?
                        tabsorbskill = p.getNeed();
                    }
                    if (isNull && !p.freq.isEmpty()) {
                        Random r = new Random(System.currentTimeMillis());
                        tabsorbskill = new ArrayList<Skill>(p.freq.keySet()).get(r.nextInt(p.freq.values().size()));
                    }
                    if (tabsorbskill != null) {
                        mypack.description += "吸收" + tabsorbskill.name;
                    }

                    for (Pack pack : ConstantPool.packs) {
                        if (isNull && p.freq.isEmpty()) {
                            break;
                        }
                        if (pack.skill == tabsorbskill) {
                            if (myctrl.gettAarea().containsKey(tabsorbskill)) {
                                myctrl.gettAarea().put(tabsorbskill, myctrl.gettAarea().get(tabsorbskill) + mypack.skill.tAbsorbTimes);
                            } else {
                                myctrl.gettAarea().put(tabsorbskill, mypack.skill.tAbsorbTimes);
                            }
                        }
                    }
                    mypack.isOK = true;
                }
                break;
            case TURN_END:
                //第二轮循环结束，判定由主线程进行，线程锁后直接第三轮循环
                myctrl.updateCommon(myplayer.ddnum, mypack.skill, mypack.times);//更新刚刚进行的操作
                myctrl.updateAbsorb();//更新吸收技能列表
                //第三轮循环
                if (mypack.player.HP > 0 && mypack.player.HP <= BoBoActivity.config.restartHP && !ConstantPool.outset.contains(mypack) && !mypack.player.hasrestarted) {
                    mypack.isOK = false;
                    mypack.player.hasrestarted = true;
                    ArrayList<Pack> powerspack = new ArrayList<Pack>();
                    ArrayList<Integer> powersint = new ArrayList<Integer>();
                    for (int i = 0; i <= ConstantPool.botnum; i++) {
                        powerspack.add(ConstantPool.packs.get(i));
                        powersint.add(powerspack.get(i).calcPower());
                    }
                    //排序
                    for (int i = 0; i < ConstantPool.botnum; i++) {
                        for (int j = 0; j < ConstantPool.botnum - i; j++) {
                            if (powersint.get(j) < powersint.get(j + 1)) {
                                Pack p = powerspack.get(j + 1);
                                powerspack.set(j + 1, powerspack.get(j));
                                powerspack.set(j, p);
                                int a = powersint.get(j + 1);
                                powersint.set(j + 1, powersint.get(j));
                                powersint.set(j, a);
                            }

                        }
                    }
                    for (int i = 0; i <= ConstantPool.botnum; i++) {
                        if (powerspack.get(i) == mypack) {
                            if (Math.round((double) 1.0 * (i + 1) / (ConstantPool.botnum + 1)) >= 0.8) {//如果能力值排名在80%后
                                BoBoActivity.activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
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
                                        mypack.isOK = true;
                                        Turn.turn = -1;
                                        AlertDialog.Builder alert = new AlertDialog.Builder(BoBoActivity.context);
                                        alert.setTitle("提示");
                                        alert.setMessage(myplayer.name + "选择了重来");
                                        alert.setPositiveButton("确定", (dialog, which) -> {
                                        });
                                        alert.create().show();

                                        BoBoActivity.activity.tvw_dd.setText("");
                                    }
                                });
                                while (!mypack.isOK) ;
                            }
                        }
                    }
                }
                break;
        }
    }
}
