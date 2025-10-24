package com.bear.DD.BoBo;

import java.util.*;

public class Skill {
    String name;
    int id;
    int cost;
    boolean ismixture;
    ArrayList<HashMap<Integer,Integer>> mixedof;
    HashMap<Integer,Integer> Atk;
    int Lv;
    ArrayList<Integer> Absorb;
    ArrayList<Integer> tAbsorb;
    int tAbsorbTimes;
    ArrayList<Integer> sAbsorb;
    int Heal;
    int Lockturns;
    ArrayList<Integer> Locklist;
    boolean isChangeable;
    ArrayList<Integer> changelist;
    String description;
    int gain;
    boolean isOnlyforbot;
    int power;
}
