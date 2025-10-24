package com.bear.DD.BoBo;

import java.util.ArrayList;

public class Interval {
    int id;
    int turns;
    public static ArrayList<Interval> intervals = new ArrayList<Interval>();

    public Interval(int turns) {
        id = intervals.size();
        this.turns = turns;
        intervals.add(this);
    }

    public boolean check() {
        if (turns <= 0) {
            return false;
        }
        return true;
    }
}
