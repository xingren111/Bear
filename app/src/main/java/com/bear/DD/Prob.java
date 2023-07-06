package com.bear.DD;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class Prob<T>{

    HashMap<T,Integer> freq=new HashMap<T, Integer>();
    HashMap<T,RollInt> area=new HashMap<T,RollInt>();
    ArrayList<T> al=new ArrayList<T>();
    void addfreq(T t,Integer fr){
        freq.put(t,fr);
    }
    void setAL(ArrayList<T> al){
        this.al=al;
    }
    void removefreq(T t){
        freq.remove(t);
    }
    void setfreq(T t,Integer fr){
        freq.put(t,fr);
    }
    void addarea(RollInt ri,T t){
        area.put(t,ri);
    }
    int updateprob(int max){

        int allfreq=0;
        Collection<Integer> al_freq=freq.values();
        Iterator<Integer> it=al_freq.iterator();
        while(it.hasNext()){
            allfreq+=(int)it.next();
        }
        it=null;
        int eacharea=max/allfreq;
        Iterator<T> it2=freq.keySet().iterator();
        int min=1;
        while(it2.hasNext()){
            T t=it2.next();
            int eachs=freq.get(t);
            int range=eachs*eacharea;
            RollInt ri=new RollInt(min,min+range-1);
            min=min+range;
            area.put(t,ri);
        }
        it2=null;
        return min;
    }
    T getNeed(int max){
        Random r=new Random(System.currentTimeMillis());
        int i=r.nextInt(max)+1;
        System.out.println("随机数指向"+i);
        for(T t:al){
            RollInt ri=area.get(t);
            if(ri.isLegal(i)){
                return t;
            }
        }
        return null;
    }
}
