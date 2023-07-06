package com.bear.DD;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

interface ClassicCommonProb {
    //    ArrayList<Prob<ClassicSkill>> probs = new ArrayList<Prob<ClassicSkill>>() {
//        {
//            add(new Prob<ClassicSkill>() {
//                {
//                    addfreq(ClassicCommonSkills.DD, 3);
//                    addfreq(ClassicCommonSkills.B, 0);
//                    addfreq(ClassicCommonSkills.Prigen, 0);
//                    addfreq(ClassicCommonSkills.ThreeL, 0);
//                    addfreq(ClassicCommonSkills.Wow, 0);
//                    addfreq(ClassicCommonSkills.BigB, 0);
//                    addfreq(ClassicCommonSkills.Sb, 0);
//                    addfreq(ClassicCommonSkills.sDef, 0);
//                    addfreq(ClassicCommonSkills.lDef, 0);
//                    addfreq(ClassicCommonSkills.wDef, 0);
//                    addfreq(ClassicCommonSkills.BigDef, 0);
//                    addfreq(ClassicCommonSkills.Reflect, 0);
//                    updateprob(990000);
//                    addarea(new RollInt(990001, 100000), ClassicCommonSkills.SelfB);
//                }
//            });
//        }
//    };
    HashMap<ClassicSkill, ArrayList<Integer>> atkTofreq = new HashMap<ClassicSkill, ArrayList<Integer>>() {
        {
            put(ClassicCommonSkills.DD, new ArrayList<Integer>(Arrays.asList(3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 0)));
            put(ClassicCommonSkills.B, new ArrayList<Integer>(Arrays.asList(0, 5, 3, 2, 2, 2, 1, 1, 1, 1, 0)));
            put(ClassicCommonSkills.Prigen, new ArrayList<Integer>(Arrays.asList(0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 0)));
            put(ClassicCommonSkills.ThreeL, new ArrayList<Integer>(Arrays.asList(0, 0, 0, 3, 2, 1, 1, 1, 1, 1, 0)));
            put(ClassicCommonSkills.Wow, new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 3, 3, 2, 1, 1, 1, 0)));
            put(ClassicCommonSkills.BigB, new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 3, 3, 2, 2, 2, 0)));
            put(ClassicCommonSkills.Sb, new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1)));
            put(ClassicCommonSkills.sDef, new ArrayList<Integer>(Arrays.asList(0, 1, 3, 2, 1, 1, 1, 1, 1, 1, 0)));
            put(ClassicCommonSkills.lDef, new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)));
            put(ClassicCommonSkills.wDef, new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)));
            put(ClassicCommonSkills.BigDef, new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)));
            put(ClassicCommonSkills.Reflect, new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)));
        }
    };
    HashMap<ClassicSkill, ArrayList<Integer>> defTofreq = new HashMap<ClassicSkill, ArrayList<Integer>>() {
        {
            put(ClassicCommonSkills.DD, new ArrayList<Integer>(Arrays.asList(3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)));
            put(ClassicCommonSkills.B, new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)));
            put(ClassicCommonSkills.Prigen, new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)));
            put(ClassicCommonSkills.ThreeL, new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)));
            put(ClassicCommonSkills.Wow, new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)));
            put(ClassicCommonSkills.BigB, new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)));
            put(ClassicCommonSkills.Sb, new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)));
            put(ClassicCommonSkills.sDef, new ArrayList<Integer>(Arrays.asList(0, 3, 3, 2, 2, 1, 1, 1, 1, 1, 0)));
            put(ClassicCommonSkills.lDef, new ArrayList<Integer>(Arrays.asList(0, 0, 0, 2, 2, 1, 0, 0, 0, 0, 0)));
            put(ClassicCommonSkills.wDef, new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0)));
            put(ClassicCommonSkills.BigDef, new ArrayList<Integer>(Arrays.asList(0, 0, 0, 1, 1, 3, 3, 2, 1, 1, 0)));
            put(ClassicCommonSkills.Reflect, new ArrayList<Integer>(Arrays.asList(0, 3, 3, 2, 2, 2, 2, 1, 0, 0, 0)));
        }
    };
}

public class Bot implements ClassicCommonSkills, ClassicCommonProb,Runnable{
    Player p;
    ClassicPack cp;

    Bot(Player p, ClassicPack cp) {
        this.p = p;
        this.cp = cp;
    }
    @Override
    public void run() {
//        Classic.status.append(p.name+"开始选择技能");
        Prob<ClassicSkill> prob = new Prob<ClassicSkill>();
        ClassicCtrl csc = new ClassicCtrl();
        csc.setEnviroment(Enviroment.BOT);
        Iterator<ClassicSkill> it = skills.iterator();
        while (it.hasNext()) {
            csc.add(it.next(), null);
        }
        prob.setAL(skills);
        while (true) {
            ClassicSet.lock1.lock();
            try {

                while (ClassicSet.nowplayer != cp.player.getPlayerid()) {
                    try {
                        ClassicSet.conditions.get(p.playerid).await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                while(ClassicSet.nowplayer==cp.player.getPlayerid()&&ClassicSet.outset.contains(cp)){
                    try {
                        RollInt ri = new RollInt(0, ClassicSet.maxplayernum-1);
                        ri.setp(cp.player.getPlayerid());
                        ri.add();
                        ClassicSet.nowplayer = ri.getp();
                        ri = null;
                        ClassicSet.conditions.get(ClassicSet.nowplayer).signal();
                        ClassicSet.conditions.get(p.playerid).await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                cp.skill=null;
                cp.isOK=false;
                cp.targets=new HashSet<ClassicPack>();
                RollInt ri = new RollInt(0, ClassicSet.maxplayernum-1);
                ri.setp(cp.player.getPlayerid());
                ri.add();
                ClassicSet.nowplayer = ri.getp();
                ri = null;
                csc.setnowdd(cp.ddnum);
                csc.update();
                int max = ClassicSet.getMaxdd(cp);
                for (ClassicSkill cs : skills) {
                    if(cs==SelfB){
                        continue;
                    }
                    if (!csc.getBannedset().contains(cs)) {
                        System.out.println(cp.player.getName()+"DD数："+cp.ddnum+" 当前最大DD数："+max);
                        int fr=0;
                        if(max<=10){
                            fr = atkTofreq.get(cs).get(cp.ddnum) + defTofreq.get(cs).get(max);
                            System.out.println(cs.skillname+"权重1："+atkTofreq.get(cs).get(cp.ddnum));
                            System.out.println(cs.skillname+"权重2："+defTofreq.get(cs).get(max));
                        }else{
                            fr = atkTofreq.get(cs).get(cp.ddnum) + defTofreq.get(cs).get(10);
                            System.out.println(cs.skillname+"权重1："+atkTofreq.get(cs).get(cp.ddnum));
                            System.out.println(cs.skillname+"权重2："+defTofreq.get(cs).get(10));
                        }

                        prob.addfreq(cs, fr);


                    } else {
                        prob.addfreq(cs, 0);
                    }
                }
                int min = prob.updateprob(990000);
                int xmax=0;
                if(cp.ddnum>0){
                    prob.addarea(new RollInt(min , 1000000), SelfB);
                    xmax=1000000;
                }else{
                    xmax=990000;
                }

                for(ClassicSkill s:prob.area.keySet()){
                    System.out.println(s.skillname+"所对应的闭区间：["+prob.area.get(s).min+","+prob.area.get(s).max+"]");
                }
                cp.skill = prob.getNeed(xmax);
                System.out.println(p.name+"使用"+cp.skill.skillname);
                cp.ddnum -= cp.skill.cost;
                cp.isOK = true;
                if (cp.skill.maxTargets != 0) {
                    if (cp.skill.maxTargets == -1 || ClassicSet.maxplayernum - ClassicSet.outset.size()-1 <= cp.skill.maxTargets) {
                        for (ClassicPack p : ClassicSet.packs) {
                            if (!p.equals(cp) && !ClassicSet.outset.contains(p) && !Classic.nobattlepack.contains(p)) {
                                cp.targets.add(p);
                            }
                        }
                    } else {
                        Random r = new Random(System.currentTimeMillis());
                        while (cp.targets.size() != cp.skill.maxTargets) {
                            int id = r.nextInt(ClassicSet.maxplayernum);
                            ClassicPack p = ClassicSet.packs.get(id);
                            if (!p.equals(cp) && !ClassicSet.outset.contains(p) && !cp.targets.contains(p) &&!Classic.nobattlepack.contains(p)) {
                                cp.targets.add(p);
                            }
                        }
                    }
                    for(ClassicPack classicPack: cp.targets){
                        System.out.println(classicPack.player.name);
                    }
                }
                ClassicSet.conditions.get(ClassicSet.nowplayer).signal();
            }finally {
                ClassicSet.lock1.unlock();
            }
//            Classic.status.append(p.name+"完成技能选择");
        }

    }
}
