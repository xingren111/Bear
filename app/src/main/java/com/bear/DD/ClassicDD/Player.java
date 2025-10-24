package com.bear.DD.ClassicDD;

import java.util.Objects;

public class Player {
    int playerid;
    String name;

    public void setPlayerid(int playerid) {
        this.playerid = playerid;
    }

    public int getPlayerid() {
        return playerid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    Player(int playerid,String name){
        this.playerid=playerid;
        this.name=name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return playerid == player.playerid && Objects.equals(name, player.name);
    }
}
