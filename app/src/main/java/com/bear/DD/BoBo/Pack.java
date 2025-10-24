package com.bear.DD.BoBo;

public class Pack {
    Player player;
    Skill skill;
    volatile boolean isOK = false;
    int times;
    Skill changeskill;
    int maxdamaged;
    boolean isstolen;
    Source source;
    String description=new String();
    boolean isChanged=false;
    public Pack(Player player, Skill skill, boolean isOK, int times, Skill changeskill, int maxdamaged, boolean isstolen) {
        this.player = player;
        this.skill = skill;
        this.isOK = isOK;
        this.times = times;
        this.changeskill = changeskill;
        this.maxdamaged = maxdamaged;
        this.isstolen = isstolen;
    }
    public void clear(){
        TurnOver();
        player.clear();
    }
    public void TurnOver(){
        skill=null;
        isOK=false;
        times=0;
        changeskill=null;
        maxdamaged=0;
        isstolen=false;
        source=null;
        isChanged=false;
        description="";
        changeskill=null;
    }
    public int calcPower(){//能力值计算
        Config config=BoBoActivity.config;
        int nowpower= player.ddnum;
        int id=player.id;
        nowpower+=(player.HP-config.initialHP)*config.HPTodd;//血->dd
        for(Skill skill:ConstantPool.ctrls[id].historyskills.keySet()){
            nowpower+= skill.power*ConstantPool.ctrls[id].historyskills.get(skill);//加历史记录技能能力值
        }
        return nowpower;
    }
}
