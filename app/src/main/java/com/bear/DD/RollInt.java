package com.bear.DD;

//左右闭区间
public class RollInt {
    int max;
    int min;
    int p;

    RollInt(int min, int max) {
        this.max = max;
        this.min = min;
        p = min;
    }

    void add() {
        if (p >= max) {
            p = min;
        } else {
            p++;
        }
    }

    void sub() {
        if (p <= min) {
            p = max;
        } else {
            p--;
        }
    }

    void setp(int p) {
        this.p = p;
        if (p > max) {
            p = max;
        } else if (p < min) {
            p = min;
        }
    }

    int getp() {
        return p;
    }

    boolean isLegal(int num) {
        if (num < min || num > max) {
            return false;
        }
        return true;
    }
}
