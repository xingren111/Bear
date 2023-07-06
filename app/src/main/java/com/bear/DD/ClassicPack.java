package com.bear.DD;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class ClassicPack extends Pack{
    ClassicSkill skill=null;
    int ddnum=0;
    boolean isout=false;
    HashSet<ClassicPack> targets=new HashSet<ClassicPack>();
    ClassicPack(Player p){
        player=p;
        targets=new HashSet<ClassicPack>();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassicPack that = (ClassicPack) o;
        return ddnum == that.ddnum && isout == that.isout && Objects.equals(skill, that.skill) && Objects.equals(targets, that.targets);
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(skill, ddnum, isout, targets);
//    }

    void clear(){
        ddnum=0;
        skill=null;
        targets=new HashSet<ClassicPack>();
        isOK=false;
    }
    void Allclear(){
        clear();
        isout=false;
    }
}
