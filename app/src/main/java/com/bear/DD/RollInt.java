package com.bear.DD;

//左右闭区间
public class RollInt {
    public int max;
    public int min;
    public int p;

    public RollInt(int min, int max) {
        this.max = max;
        this.min = min;
        p = min;
    }

    public void add() {
        if (p >= max) {
            p = min;
        } else {
            p++;
        }
    }

    public void sub() {
        if (p <= min) {
            p = max;
        } else {
            p--;
        }
    }

    public void setp(int p) {
        this.p = p;
        if (p > max) {
            p = max;
        } else if (p < min) {
            p = min;
        }
    }

    public int getp() {
        return p;
    }

    public boolean isLegal(int num) {
        if (num < min || num > max) {
            return false;
        }
        return true;
    }
}
