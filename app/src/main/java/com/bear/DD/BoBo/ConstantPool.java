package com.bear.DD.BoBo;

import com.bear.DD.RollInt;
import com.google.gson.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConstantPool {
    public static ArrayList<Player> players = new ArrayList<Player>();//玩家集合
    public static ArrayList<Pack> packs = new ArrayList<Pack>();//pack集合
    public static Lock lock = new ReentrantLock();
    public static Condition[] conditions;
    public static Bot[] bots;
    public static Ctrl[] ctrls;
    public static RollInt ri;
    public static int botnum;
    public static HashSet<Pack> outset=new HashSet<Pack>();
    public static boolean isQuit=false;

    public static void init() {
        //添加玩家
        Config c = BoBoActivity.config;
        System.out.println("已进入");
        players.add(new Player("玩家", 0, c.initialHP, 0, c.initialLv, false));
        for (int i = 1; i <= botnum; i++) {
            Player p = new Player("人机" + String.valueOf(i), i, c.initialHP, 0, c.initialLv, false);
            System.out.println("已经进入for循环");
            players.add(p);
            System.out.println("添加了一个player");
        }
        //添加pack
        packs.add(new Pack(players.get(0), null, false, 0, null, 0, false));
        for (int i = 1; i <= botnum; i++) {
            packs.add(new Pack(players.get(i), null, false, 0, null, 0, false));
        }
        //CTRL
        ctrls = new Ctrl[botnum + 1];
        ctrls[0] = new Ctrl(new HashMap<Skill, Integer>(), 0, new ConcurrentHashMap<>(), new ConcurrentHashMap<>(), Enviroment.HUMAN,0);
        for (int i = 1; i <= botnum; i++) {
            ctrls[i] = new Ctrl(new HashMap<Skill, Integer>(), 0, new ConcurrentHashMap<>(), new ConcurrentHashMap<>(), Enviroment.BOT,i);
        }
        //人机
        bots = new Bot[botnum];
        for (int i = 0; i < botnum; i++) {//注意此处的i不是bot的id，而是bot的id减一，即bot[0].id=1
            bots[i] = new Bot(i);
        }
        ri = new RollInt(0, botnum);
        ri.setp(1);
    }

    /**
     * 模拟
     * <p>顾名思义。模拟判定。
     * 人机可以通过模拟机智地选择自己想要变的技能，通过score（分数）来判断哪个技能更好。
     *
     * @param simulater     模拟者，主持模拟的Pack。
     * @param simulatepacks 被模拟对象，配合模拟者进行模拟。
     * @return 返回最佳的Skill
     */
    public static Skill Simulate(Pack simulater, ArrayList<Pack> simulatepacks) {
        ConcurrentHashMap<Skill, Integer> skilltoscore = new ConcurrentHashMap<Skill, Integer>();
        ConcurrentHashMap<Skill, Integer> skilltoatkscore = new ConcurrentHashMap<Skill, Integer>();
        for (Integer waitchange : simulater.skill.changelist) {
            Skill atker = BoBoActivity.U_idToskill.get(waitchange);
            int score = 0;
            int atkscore = 0;
            for (Pack pack : simulatepacks) {
                Skill defender = pack.skill;
                //1.判断是否为击败关系
                if (atker.Atk.containsKey(defender.id)) {
                    score++;
                    atkscore++;
                }
                //2.判断是否为被击败关系
                if (defender.Atk.containsKey(atker.id)) {
                    score -= ConstantPool.botnum;
                }
                //3.判断是否为吸收关系
                if (atker.Absorb.contains(defender.id) ||
                        atker.tAbsorb.contains(defender.id) ||
                        atker.sAbsorb.contains(defender.id)) {
                    score++;
                }
            }
            skilltoatkscore.put(atker, atkscore);
            skilltoscore.put(atker, score);
        }
        //选出最大的一项
        int maxscore = Integer.MIN_VALUE;
        for (Skill skill : skilltoscore.keySet()) {
            if (skilltoscore.get(skill) < maxscore) {
                skilltoscore.remove(skill);
                skilltoatkscore.remove(skill);
            } else {
                maxscore = skilltoscore.get(skill);
            }
        }
        for (Skill skill : skilltoscore.keySet()) {
            if (skilltoscore.get(skill) < maxscore) {
                skilltoscore.remove(skill);
                skilltoatkscore.remove(skill);
            }
        }
        if (skilltoscore.size() == 1) {
            for (Skill skill : skilltoscore.keySet()) {
                return skill;
            }
        } else {
            int maxatkscore = Integer.MIN_VALUE;
            //第一次循环找到最大值
            for (Skill atkskill : skilltoatkscore.keySet()) {
                if (skilltoatkscore.get(atkskill) < maxatkscore) {
                    skilltoatkscore.remove(atkskill);
                }else{
                    maxatkscore=skilltoatkscore.get(atkskill);
                }
            }
            //第二次循环移除小于最大值的
            for (Skill atkskill : skilltoatkscore.keySet()) {
                if (skilltoatkscore.get(atkskill) < maxatkscore) {
                    skilltoatkscore.remove(atkskill);
                }
            }
            if(skilltoatkscore.size()==1){
                for(Skill skill:skilltoatkscore.keySet()){
                    return skill;
                }
            }else{
                Random r=new Random(System.currentTimeMillis());
                ArrayList<Skill> waits=new ArrayList<Skill>(skilltoatkscore.keySet());
                return waits.get(r.nextInt(waits.size()));
            }
        }
        return null;
    }
    public static void clearAll(){
        players=new ArrayList<Player>();
        packs=new ArrayList<Pack>();
        lock=null;
        for(Bot bot:bots){
            bot.clearAll();
        }
        bots=null;
        ctrls=null;
        ri=null;
        botnum=0;
        outset=new HashSet<Pack>();

    }
}
