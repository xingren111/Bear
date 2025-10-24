package com.bear.DD;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Prob<T>{

    public HashMap<T,Integer> freq=new HashMap<T, Integer>();
    public HashMap<T,RollInt> area=new HashMap<T,RollInt>();
//    ArrayList<T> al=new ArrayList<T>();
    public void addfreq(T t,Integer fr){
        freq.put(t,fr);
    }
//    void setAL(ArrayList<T> al){
//        this.al=al;
//    }
    public void removefreq(T t){
        freq.remove(t);
    }
    public void setfreq(T t,Integer fr){
        freq.put(t,fr);
    }
    public void addarea(RollInt ri,T t){
        area.put(t,ri);
    }
    public int updateprob(int max){

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
    public int updateprob(){
        int sum=0;
        for(int length:freq.values()){
            sum+=length;
        }
        return updateprob(sum);
    }
    public T getNeed(int max){
        Random r= ThreadLocalRandom.current();
        int i=r.nextInt(max)+1;
        System.out.println("随机数指向"+i);
        for(T t:area.keySet()){
            RollInt ri=area.get(t);
            if(ri.isLegal(i)){
                return t;
            }
        }
        return null;
    }
    public T getNeed(){
        int sum=0;
        for(int length:freq.values()){
            sum+=length;
        }
        if(freq.size()==0) {
            return null;
        }
        if(sum==0){
            ArrayList<T>  keylist=new ArrayList<T>(freq.keySet());
            Random r=new Random();

            return  keylist.get( r.nextInt(freq.size()));
        }
        return getNeed(sum);
    }
}
