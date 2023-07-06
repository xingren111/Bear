package com.bear.DD;

import java.util.ArrayList;
import java.util.HashSet;

public class ClassicSkill extends Skill{
    //type:主动=0;被动=1;收益=2;特殊=3
    int damage;
    HashSet<Integer> OnlyDef;
    int maxTargets;
    ClassicBeen classicbeen;
    String skillname=new String();
    ClassicSkill(int type, int cost, int damage, int OnlyCode, HashSet<Integer> OnlyDef, int maxTargets, ClassicBeen classicbeen,String skillname){
        this.type=type;
        this.cost=cost;
        this.damage=damage;
        this.OnlyCode=OnlyCode;
        this.OnlyDef=OnlyDef;
        this.maxTargets=maxTargets;
        this.classicbeen =classicbeen;
        this.skillname=skillname;
    }
}
