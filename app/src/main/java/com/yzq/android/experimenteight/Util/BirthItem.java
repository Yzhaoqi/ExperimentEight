package com.yzq.android.experimenteight.Util;

/**
 * Created by YZQ on 2016/11/21.
 */

public class BirthItem {
    private String name;
    private String birth;
    private String gift;

    public BirthItem(String name, String birth, String gift) {
        this.name = name;
        this.birth = birth;
        this.gift = gift;
    }

    public void setAll(String birth, String gift) {
        this.birth = birth;
        this.gift = gift;
    }

    public String getName() {
        return name;
    }

    public String getBirth() {
        return birth;
    }

    public String getGift() {
        return gift;
    }
}
