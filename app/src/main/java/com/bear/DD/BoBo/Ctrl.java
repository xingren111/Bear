package com.bear.DD.BoBo;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bear.DD.R;
import com.google.android.flexbox.FlexboxLayout;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class Ctrl {
    HashMap<Skill, Integer> historyskills;//历史技能，决定commonskills中组合机
    int id;
    int nowdd;//dd数，决定commonskills中基础技能
    ConcurrentHashMap<Skill, Integer> tAarea = new ConcurrentHashMap<Skill, Integer>();//塔区技能
    ConcurrentHashMap<Skill, Integer> Aarea = new ConcurrentHashMap<Skill, Integer>();//吸收的技能
    HashSet<Skill> AbleCommonSkills = new HashSet<Skill>();//可用common技能
    HashSet<Skill> AbleAbsorbSkills = new HashSet<Skill>();//可用吸收/塔技能
//    CopyOnWriteArraySet<Skill> Locklist = new CopyOnWriteArraySet<Skill>();//被锁的技能
    ConcurrentHashMap<Interval, Skill> interSkill = new ConcurrentHashMap<Interval, Skill>();//通过计时器绑定interskill
    FlexboxLayout tALayout;//塔区布局
    FlexboxLayout ALayout;//吸收区布局
    HashMap<Skill, Button> A_skillTobutton = new HashMap<Skill, Button>();//吸收区技能->按钮映射
    HashMap<Skill, Button> T_skillTobutton = new HashMap<Skill, Button>();//塔区技能->按钮映射
    HashMap<Button, Skill> A_buttonToskill = new HashMap<Button, Skill>();
    HashMap<Button, Skill> T_buttonToskill = new HashMap<Button, Skill>();
    Enviroment enviroment;

    public void clear() {
        historyskills.clear();
        nowdd = 0;
        for(Skill skill:A_skillTobutton.keySet()){
            ALayout.removeView(A_skillTobutton.get(skill));
        }
        for(Skill skill:T_skillTobutton.keySet()){
            tALayout.removeView(T_skillTobutton.get(skill));
        }
        tAarea.clear();
        Aarea.clear();
        AbleCommonSkills.clear();
        AbleAbsorbSkills.clear();
//        Locklist.clear();
        interSkill.clear();
    }

    private <T> HashSet<T> intersect(Set<T> set1, Set<T> set2) {//交集
        HashSet<T> set3 = new HashSet<T>();
        for (T a : set1) {
            for (T b : set2) {
                if (a == b) {
                    set3.add(a);
                }
            }
        }
        return set3;
    }

    private <T> HashSet<T> union(Set<T> set1, Set<T> set2) {//并集
        HashSet<T> set3 = new HashSet<T>();
        for (T a : set1) {
            if (!set3.contains(a)) {
                set3.add(a);
            }
        }
        for (T b : set2) {
            if (!set3.contains(b)) {
                set3.add(b);
            }
        }
        return set3;
    }

    private <T> HashSet<T> minus(Set<T> set, Set<T> U) {//补集
        HashSet<T> set3 = new HashSet<T>();
        for (T a : U) {
            if (!set.contains(a)) {
                set3.add(a);
            }
        }
        return set3;
    }

    public Ctrl(HashMap<Skill, Integer> historyskills, int nowdd,
                ConcurrentHashMap<Skill, Integer> tAarea, ConcurrentHashMap<Skill, Integer> aarea,
                Enviroment enviroment, int id) {
        this.historyskills = historyskills;
        this.nowdd = nowdd;
        this.tAarea = tAarea;
        Aarea = aarea;
        this.enviroment = enviroment;
        this.id = id;
    }

    public void setContainer(FlexboxLayout tALayout, FlexboxLayout ALayout) {
        this.tALayout = tALayout;
        this.ALayout = ALayout;
    }

    public void updateCommon(int dd, Skill skill, int times) {//更新commonskills区域
        System.out.println(ConstantPool.players.get(id).name + "已进入updatecommon");
        nowdd = dd;
        AbleCommonSkills.clear();
        if (skill != null) {
            System.out.println(ConstantPool.players.get(id).name + "已进入if判断");
            if (historyskills.containsKey(skill)) {
                System.out.println(ConstantPool.players.get(id).name + "已进入if判断中的true");
                historyskills.put(skill, historyskills.get(skill) + times);
            } else {
                System.out.println(ConstantPool.players.get(id).name + "已进入if判断中的false");
                historyskills.put(skill, times);
            }
        }
        for (Skill s : BoBoActivity.U_idToskill.values()) {
            if (!s.ismixture && s.cost <= dd) {
                AbleCommonSkills.add(s);
            }
            if (s.ismixture) {//组合技判定
                for (HashMap<Integer, Integer> possibleSkillcombo : s.mixedof) {//遍历全部可能组合
                    boolean isCombo = true;
                    for (int ids : possibleSkillcombo.keySet()) {//遍历一个可能的组合
                        if (historyskills.containsKey(BoBoActivity.U_idToskill.get(ids))) {//如果历史记录中包含全集中某个技能
                            if (possibleSkillcombo.get(ids) > historyskills.get(BoBoActivity.U_idToskill.get(ids))) {//如果历史记录中这个技能的数量小于等于组合技所需技能数
                                isCombo = false;
                            }
                        } else {
                            isCombo = false;
                        }
                    }
                    if (isCombo) {//如果遍历结束后仍然全部符合要求
                        AbleCommonSkills.add(s);//添加此组合技
                    }
                }
            }
        }
        //先补后交，被锁技能的补集与able取交集
        AbleCommonSkills = intersect(AbleCommonSkills, minus(new HashSet<Skill>(interSkill.values()), new HashSet<Skill>(BoBoActivity.U_idToskill.values())));
        if (enviroment == Enviroment.HUMAN) {//如果环境是玩家
            for (Button b : BoBoActivity.U_idTobutton.values()) {//全部禁用
                b.setEnabled(false);
            }
            for (Skill s : AbleCommonSkills) {
                BoBoActivity.U_idTobutton.get(s.id).setEnabled(true);//部分开启
            }
        }
    }

    public void updateAbsorb() {
        //清理吸收区/塔区用完的技能
        for (Skill skill : Aarea.keySet()) {
            if (Aarea.get(skill) <= 0) {
                Aarea.remove(skill);
                if(A_skillTobutton.containsKey(skill)){
                    ALayout.removeView(A_skillTobutton.get(skill));
                    A_buttonToskill.remove(A_skillTobutton.get(skill));
                    A_skillTobutton.remove(skill);
                }


            }
        }
        for (Skill skill : tAarea.keySet()) {
            if (tAarea.get(skill) <= 0) {
                tAarea.remove(skill);
                if(T_skillTobutton.containsKey(skill)) {
                    tALayout.removeView(T_skillTobutton.get(skill));
                    T_buttonToskill.remove(T_skillTobutton.get(skill));
                    T_skillTobutton.remove(skill);
                }
            }
        }
        AbleAbsorbSkills = union(new HashSet<Skill>(tAarea.keySet()),
                intersect(new HashSet<Skill>(Aarea.keySet()),
                        minus(new HashSet<Skill>(interSkill.values()), new HashSet<Skill>(BoBoActivity.U_idToskill.values()))));
        if (enviroment == Enviroment.BOT) {
            return;
        }
        //判断新加入的技能（吸收区）
        if (!Aarea.keySet().equals(A_skillTobutton.keySet())) {
            //对新加入的技能（即C（全部技能）被映射技能）循环，添加新映射并加入至容器
            for (Skill skill : minus(new HashSet<Skill>(A_skillTobutton.keySet()), new HashSet<Skill>(Aarea.keySet()))) {
                Button b = new Button(BoBoActivity.context);
                b.setBackgroundResource(R.drawable.button_background);
                b.setText(skill.name);
                b.setVisibility(View.VISIBLE);
                b.setEnabled(true);
                ALayout.addView(b,
                        new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                A_skillTobutton.put(skill, b);
                A_buttonToskill.put(b, skill);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConstantPool.packs.get(id).skill = A_buttonToskill.get(b);
                        ConstantPool.packs.get(id).source = Source.A;
                        ConstantPool.packs.get(id).description+=ConstantPool.packs.get(id).skill.description;
                        ConstantPool.packs.get(id).description+="来自吸收区";
                        ConstantPool.packs.get(id).times=1;
                        Aarea.put(A_buttonToskill.get(b), Aarea.get(A_buttonToskill.get(b)) - 1);
                        ConstantPool.packs.get(id).isOK = true;

                    }
                });
            }
        }
        //判断新加入的技能（塔区）
        if (!tAarea.keySet().equals(T_skillTobutton.keySet())) {
            //对新加入的技能（即C（全部技能）被映射技能）循环，添加新映射并加入至容器
            for (Skill skill : minus(new HashSet<Skill>(T_skillTobutton.keySet()), new HashSet<Skill>(tAarea.keySet()))) {
                Button b = new Button(BoBoActivity.context);
                b.setBackgroundResource(R.drawable.button_background);
                b.setVisibility(View.VISIBLE);
                b.setText(skill.name);
                b.setEnabled(true);
                tALayout.addView(b,
                        new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                T_skillTobutton.put(skill, b);
                T_buttonToskill.put(b, skill);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(Button button:BoBoActivity.U_idTobutton.values()){
                            button.setEnabled(false);
                        }
                        for(Button button:T_skillTobutton.values()){
                            button.setEnabled(false);
                        }
                        for(Button button:A_skillTobutton.values()){
                            button.setEnabled(false);
                        }
                        ConstantPool.packs.get(id).skill = T_buttonToskill.get(b);
                        ConstantPool.packs.get(id).source = Source.T;
                        ConstantPool.packs.get(id).description+=ConstantPool.packs.get(id).skill.description;
                        ConstantPool.packs.get(id).isOK = true;
                    }
                });
            }
        }
        //判断被锁情况
        if (!intersect(new HashSet<Skill>(interSkill.values()), new HashSet<Skill>(Aarea.keySet())).isEmpty()) {
            HashSet<Skill> lockIntersectAarea = intersect(new HashSet<Skill>(interSkill.values()), new HashSet<Skill>(Aarea.keySet()));
            for (Skill skill : lockIntersectAarea) {
                A_skillTobutton.get(skill).setEnabled(false);
            }
        }
        if (!intersect(new HashSet<Skill>(interSkill.values()), new HashSet<Skill>(tAarea.keySet())).isEmpty()) {
            HashSet<Skill> lockIntersecttAarea = intersect(new HashSet<Skill>(interSkill.values()), new HashSet<Skill>(tAarea.keySet()));
            for (Skill skill : lockIntersecttAarea) {
                T_skillTobutton.get(skill).setEnabled(false);
            }
        }
    }

    public void updateLock() {
        for (Interval interval : interSkill.keySet()) {
            if (!interval.check()) {
//                Locklist.remove(interSkill.get(interval));
                interSkill.remove(interval);

            }
        }
    }

    public ConcurrentHashMap<Skill, Integer> gettAarea() {
        return tAarea;
    }

    public ConcurrentHashMap<Skill, Integer> getAarea() {
        return Aarea;
    }

    public HashSet<Skill> getAbleCommonSkills() {
        return AbleCommonSkills;
    }


}
