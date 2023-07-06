package com.bear.DD;

import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

enum Enviroment{
    PLAYER,BOT;
}

/**
 * @author xingren
 * 用于控制技能按钮，通过ddnum
 */
public class ClassicCtrl implements ClassicCommonSkills {
    int nowdd;
    HashMap<ClassicSkill,Button> skillTobutton=new HashMap<ClassicSkill, Button>();
//    HashMap<Pack,Button> playerTobutton;
    Enviroment enviroment;
    HashSet<ClassicSkill> bannedset=new HashSet<ClassicSkill>(){
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

    public HashSet<ClassicSkill> getBannedset() {
        return bannedset;
    }

    void setnowdd(int nowdd){
        this.nowdd=nowdd;
    }

    public void setEnviroment(Enviroment enviroment) {
        this.enviroment = enviroment;
    }

    /**
     * 添加映射
     * @param classicskill 技能，key
     * @param button 按钮，value
     */
    void add(ClassicSkill classicskill, Button button){
        skillTobutton.put(classicskill,button);
    }

    /**
     * 更新结果
     */
    void update(){
        Iterator<ClassicSkill> it=skills.iterator();
        while(it.hasNext()){
            ClassicSkill cs=it.next();
            if(nowdd>=cs.cost){
                if(enviroment.equals(Enviroment.PLAYER)) {
                    skillTobutton.get(cs).setEnabled(true);
                }else if(enviroment.equals(Enviroment.BOT)){
                    bannedset.remove(cs);
                }
            }else{
                if(enviroment.equals(Enviroment.PLAYER)) {
                    skillTobutton.get(cs).setEnabled(false);
                }else if(enviroment.equals(Enviroment.BOT)){
                    bannedset.add(cs);
                }
            }
        }
        it=null;
//        Iterator<ClassicPack> itp=ClassicSet.outset.iterator();
//        while(itp.hasNext()){
//            ClassicPack p=itp.next();
//            playerTobutton.get(p).setEnabled(false);
//        }
//        itp=null;
    }
}
