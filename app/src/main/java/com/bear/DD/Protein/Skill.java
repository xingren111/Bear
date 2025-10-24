package com.bear.DD.Protein;

public class Skill {
    public enum Quality{
        White,Green,Blue,Gold;
    }
    String name;
    int id;
    int cost;
    Quality quality;
    Damage damage;
    Effect effect;

    public Skill(String name, int id, int cost, Quality quality, Damage damage, Effect effect) {
        this.name = name;
        this.id = id;
        this.cost = cost;
        this.quality = quality;
        this.damage = damage;
        this.effect = effect;
    }
}
