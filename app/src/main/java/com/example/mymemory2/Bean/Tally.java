package com.example.mymemory2.Bean;

//封装记录记账条目信息实体类
public class Tally {
    public int id;
    public String tallyTime;        //记账时间
    public String tallyType;        //类型（收入/支出）
    public double tallyMoney;       //金额
    public String tallyState;       //描述

    public int getId() {return id;    }

    public void setId(int id) { this.id = id; }

    public String getTallyTime() { return tallyTime;  }

    public void setTallyTime(String tallyTime) { this.tallyTime = tallyTime; }

    public String getTallyType() { return tallyType;  }

    public void setTallyType(String tallyType) { this.tallyType = tallyType; }

    public double getTallyMoney() { return tallyMoney; }

    public void setTallyMoney(double tallyMoney) { this.tallyMoney = tallyMoney; }

    public String getTallyState() { return tallyState; }

    public void setTallyState(String tallyState) { this.tallyState = tallyState; }
}
