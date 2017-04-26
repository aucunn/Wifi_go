package com.mysterlee.www;

import java.io.Serializable;

/**
 * Created by aucun on 2017-04-25.
 */

public class Person implements Serializable {

    private String name;
    private String lv;
    private String cash;
    private String num;
    private String point;

    public Person(String num, String name, String lv, String cash, String point){

        this.num = num;
        this.name = name;
        this.lv = lv;
        this.cash = cash;
        this.point = point;
    }

    public String getNum(){
        return num;
    }

    public String getName(){
        return name;
    }

    public String getLv(){
        return lv;
    }

    public String getCash(){
        return cash;
    }

    public String getPoint(){
        return point;
    }
}
