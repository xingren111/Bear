package com.bear.DD.ClassicDD;

import java.util.ArrayList;
import java.util.HashSet;

public interface ClassicCommonSkills {
    ClassicSkill DD = new ClassicSkill(2, 0, 0, 0x20000001, null, 0, new ClassicBeen() {
        @Override
        public void doBeen(ClassicPack src, ClassicPack dst) {
            dst.isout = true;
            ClassicSet.isnewout = true;
        }

        @Override
        public void getsth(ClassicPack src) {
            src.ddnum++;
        }
    },"DD");
    ClassicSkill B = new ClassicSkill(0, 1, 1, 0x00100001, null, 1, new ClassicBeen() {
        @Override
        public void doBeen(ClassicPack src, ClassicPack dst) {
            if (src.skill.damage > dst.skill.damage) {
                dst.isout = true;
                ClassicSet.isnewout = true;
            }
        }

        @Override
        public void getsth(ClassicPack src) {
        }
    },"B");
    ClassicSkill Prigen = new ClassicSkill(0, 2, 2, 0x00200002, null, -1, new ClassicBeen() {
        @Override
        public void doBeen(ClassicPack src, ClassicPack dst) {
            if (src.skill.damage > dst.skill.damage) {
                dst.isout = true;
                ClassicSet.isnewout = true;
            }
        }

        @Override
        public void getsth(ClassicPack src) {
        }
    },"Prigen");
    ClassicSkill ThreeL = new ClassicSkill(0, 3, 3, 0x00300003, null, 1, new ClassicBeen() {
        @Override
        public void doBeen(ClassicPack src, ClassicPack dst) {
            if (src.skill.damage > dst.skill.damage) {
                dst.isout = true;
                ClassicSet.isnewout = true;
            }
        }

        @Override
        public void getsth(ClassicPack src) {
        }
    },"3L");
    ClassicSkill Wow = new ClassicSkill(0, 4, 4, 0x00400004, null, -1, new ClassicBeen() {
        @Override
        public void doBeen(ClassicPack src, ClassicPack dst) {
            if (src.skill.damage > dst.skill.damage) {
                dst.isout = true;
                ClassicSet.isnewout = true;
            }
        }

        @Override
        public void getsth(ClassicPack src) {
        }
    },"Wow");
    ClassicSkill BigB = new ClassicSkill(0, 5, 5, 0x00500005, null, 1, new ClassicBeen() {
        @Override
        public void doBeen(ClassicPack src, ClassicPack dst) {
            if (src.skill.damage > dst.skill.damage) {
                dst.isout = true;
                ClassicSet.isnewout = true;
            }
        }

        @Override
        public void getsth(ClassicPack src) {
        }
    },"BigB");
    ClassicSkill Sb = new ClassicSkill(0, 10, 10, 0x00A00006, null, -1, new ClassicBeen() {
        @Override
        public void doBeen(ClassicPack src, ClassicPack dst) {
            if (src.skill.damage > dst.skill.damage) {
                dst.isout = true;
                ClassicSet.isnewout = true;
            }
        }

        @Override
        public void getsth(ClassicPack src) {
        }
    },"扇贝");
    ClassicSkill sDef = new ClassicSkill(1, 0, 0, 0x10000001, new HashSet<Integer>() {
        {
            add(0x00100001);
            add(0x00200002);
        }
    }, 0, new ClassicBeen() {
        @Override
        public void doBeen(ClassicPack src, ClassicPack dst) {
            if (!dst.skill.OnlyDef.contains(src.skill.OnlyCode)) {
                dst.isout = true;
                ClassicSet.isnewout = true;
            }
        }

        @Override
        public void getsth(ClassicPack src) {

        }
    },"防");
    ClassicSkill lDef = new ClassicSkill(1, 0, 0, 0x10000002, new HashSet<Integer>() {
        {
            add(0x00300003);
        }
    }, 0, new ClassicBeen() {
        @Override
        public void doBeen(ClassicPack src, ClassicPack dst) {
            if (!dst.skill.OnlyDef.contains(src.skill.OnlyCode)) {
                dst.isout = true;
                ClassicSet.isnewout = true;
            }
        }

        @Override
        public void getsth(ClassicPack src) {

        }
    },"防3L");
    ClassicSkill wDef = new ClassicSkill(1, 0, 0, 0x10000003, new HashSet<Integer>() {
        {
            add(0x00400004);
        }
    }, 0, new ClassicBeen() {
        @Override
        public void doBeen(ClassicPack src, ClassicPack dst) {
            if (!dst.skill.OnlyDef.contains(src.skill.OnlyCode)) {
                dst.isout = true;
                ClassicSet.isnewout = true;
            }
        }

        @Override
        public void getsth(ClassicPack src) {

        }
    },"防Wow");
    ClassicSkill BigDef = new ClassicSkill(1, 1, 0, 0x10000004, new HashSet<Integer>() {
        {
            add(0x00100001);
            add(0x00200002);
            add(0x00300003);
            add(0x00400004);
            add(0x00500005);
        }
    }, 0, new ClassicBeen() {
        @Override
        public void doBeen(ClassicPack src, ClassicPack dst) {
            if (!dst.skill.OnlyDef.contains(src.skill.OnlyCode)) {
                dst.isout = true;
                ClassicSet.isnewout = true;
            }
        }

        @Override
        public void getsth(ClassicPack src) {

        }
    },"大防");
    ClassicSkill Reflect = new ClassicSkill(1, 1, 0, 0x10000005, null, 0, new ClassicBeen() {
        @Override
        public void doBeen(ClassicPack src, ClassicPack dst) {
            if (src.skill.damage >= 5) {
                dst.isout = true;
                ClassicSet.isnewout = true;
            } else {
                src.isout = true;
                ClassicSet.isnewout = true;
            }
        }

        @Override
        public void getsth(ClassicPack src) {
        }
    },"反弹");
    ClassicSkill SelfB = new ClassicSkill(3, 1, 1, 0x30100003, null, 0, new ClassicBeen() {
        @Override
        public void doBeen(ClassicPack src, ClassicPack dst) {
            if (src.skill.damage > dst.skill.damage) {
                dst.isout = true;
                ClassicSet.isnewout = true;
            }
        }

        @Override
        public void getsth(ClassicPack src) {
        }
    },"自B");
    Player Bot1 = new Player(1, "人机1");
    Player Bot2 = new Player(2, "人机2");
    Player Bot3 = new Player(3, "人机3");
    Player Bot4 = new Player(4, "人机4");
    Player Bot5 = new Player(5, "人机5");
    ArrayList<Player> Players = new ArrayList<Player>(){
        {
            add(ClassicSet.Playerself);
            add(Bot1);
            add(Bot2);
            add(Bot3);
            add(Bot4);
            add(Bot5);
        }
    };

    ArrayList<ClassicSkill> skills = new ArrayList<ClassicSkill>() {
        {
            add(DD);
            add(B);
            add(Prigen);
            add(ThreeL);
            add(Wow);
            add(BigB);
            add(Sb);
            add(sDef);
            add(lDef);
            add(wDef);
            add(BigDef);
            add(Reflect);
            add(SelfB);
        }
    };

}
