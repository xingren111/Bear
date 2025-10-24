package com.bear.DD.BoBo;

import java.util.HashMap;

public class Turn {
    public static int turn=0;
    public static void TurnOver(){
        turn++;
        for (int i=0;i<Interval.intervals.size();i++){
            if(Interval.intervals.get(i).check()){
                Interval.intervals.get(i).turns--;
            }
        }
        for(Ctrl ctrl:ConstantPool.ctrls){
            ctrl.updateLock();
        }
    }
    public static int getMaxDDinAllPlayers(Player player){
        int nowmaxdd=0;
        for(Player p:ConstantPool.players){
            if(p==player){
                continue;
            }
            if(p.ddnum>nowmaxdd){
                nowmaxdd=p.ddnum;
            }
        }
        return nowmaxdd;
    }
    public static boolean isSkillExist(Player player,Skill skill){
        if(getMaxDDinAllPlayers(player)>=skill.cost&&!skill.ismixture){//如果dd数大于所需被判定的技能花费且该技能不是组合技
            return true;
        }
        if(skill.ismixture){//组合技判定
            for(Player p:ConstantPool.players){
                if(p==player){
                    continue;
                }
                for(HashMap<Integer,Integer> possibleSkillcombo:skill.mixedof){//遍历全部可能组合
                    boolean isCombo=true;
                    for(int ids:possibleSkillcombo.keySet()){//遍历一个可能的组合
                        if(ConstantPool.ctrls[p.id].historyskills.containsKey(BoBoActivity.U_idToskill.get(ids))){//如果历史记录中包含全集中某个技能
                            if(possibleSkillcombo.get(ids)>=ConstantPool.ctrls[p.id].historyskills.get(BoBoActivity.U_idToskill.get(ids))){//如果历史记录中这个技能的数量小于等于组合技所需技能数
                                isCombo=false;
                            }
                        }else{
                            isCombo=false;
                        }
                    }
                    if(isCombo){//如果遍历结束后仍然全部符合要求
                        return true;//判定存在该技能
                    }
                }
            }
        }
        for(Player p:ConstantPool.players){
            if(p==player){
                continue;
            }
            if(ConstantPool.ctrls[p.id].getAarea().containsKey(skill)){
                if(ConstantPool.ctrls[p.id].getAarea().get(skill)>0){
                    return true;
                }
            }
            if(ConstantPool.ctrls[p.id].gettAarea().containsKey(skill)){
                if(ConstantPool.ctrls[p.id].gettAarea().get(skill)>0){
                    return true;
                }
            }
        }
        return false;
    }
    public static int livingPlayers(){
        int nowlivingplayernum=0;
        for(Player p:ConstantPool.players){
            if(p.HP>0){
                nowlivingplayernum++;
            }
        }
        return nowlivingplayernum;
    }
}
