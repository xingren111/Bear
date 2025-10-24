package com.bear.DD.BoBo;

public class Player {
    String name;
    int id;
    int HP;
    int ddnum;
    int Lv;
    boolean hasrestarted;
    public Player(String name, int id, int HP, int ddnum, int lv, boolean hasrestarted) {
        this.name = name;
        this.id = id;
        this.HP = HP;
        this.ddnum = ddnum;
        Lv = lv;
        this.hasrestarted = hasrestarted;
    }
    public void clear(){
        name=null;
        id=0;
        HP=0;
        ddnum=0;
        Lv=0;
        hasrestarted=false;
    }
}
