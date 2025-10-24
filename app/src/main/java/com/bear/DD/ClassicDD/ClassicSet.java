package com.bear.DD.ClassicDD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClassicSet implements ClassicCommonSkills {
    static int maxplayernum;
    static boolean isroundback;
    static HashSet<ClassicPack> outset = new HashSet<ClassicPack>();
    static int nowplayer = -1;
    static final byte[] lock = new byte[0];
    static Lock lock1 = new ReentrantLock();
    static ArrayList<Condition> conditions = new ArrayList<Condition>() {
        {
            for (int i = 0; i <= 5; i++) {
                add(lock1.newCondition());
            }
        }
    };
    static Player Playerself = new Player(0, "玩家");
    static boolean isnewout = false;
    static boolean isquit=false;
    //    static ArrayList<ClassicPack> packs=new ArrayList<ClassicPack>(){
//        {
//            add(new ClassicPack(ClassicSet.Playerself));
//            add(new ClassicPack(Bot1));
//            add(new ClassicPack(Bot2));
//            add(new ClassicPack(Bot3));
//            add(new ClassicPack(Bot4));
//            add(new ClassicPack(Bot5));
//        }
//    };
    static ArrayList<ClassicPack> packs = new ArrayList<ClassicPack>
            (Arrays.asList(new ClassicPack(ClassicSet.Playerself),
                    new ClassicPack(Bot1),
                    new ClassicPack(Bot2),
                    new ClassicPack(Bot3),
                    new ClassicPack(Bot4),
                    new ClassicPack(Bot5)));

    static int getMaxdd(ClassicPack p) {
        int max = 0;
        for (ClassicPack cp : packs) {
            if (cp == p || outset.contains(cp) || Classic.nobattlepack.contains(cp)) {
                continue;
            }
            if (cp.ddnum > max) {
                max = cp.ddnum;
            }
        }
        return max;
    }

    static ClassicPack hasDD(ClassicPack self, int ddnum) {
        for (ClassicPack cp : packs) {
            if (cp != self && cp.ddnum == ddnum && !outset.contains(cp) && !Classic.nobattlepack.contains(cp)) {
                return cp;
            }
        }
        return null;
    }

    static boolean isAllout() {
        if (outset.size() == maxplayernum - 1 && !outset.contains(packs.get(0))) {
            return true;
        }
        return false;
    }

    static void Allclear() {
        maxplayernum = 0;
        outset.clear();
        nowplayer = -1;
        isnewout = false;

    }
}
