package com.memory.db;

/**
 * @Auther: cui.Memory
 * @Date: 2018/12/19 0019 13:28
 * @Description:
 */
public class Proxy {
    private String id;
    private String name;
    private Double money;
    private Double moneySum;
    private String parent;
    private String parentName;
    private Integer count;

    private Double moneyLs;
    private Double moneySumLs;

    @Override
    public String toString() {
        if(count>0){
            return name + "（"+count+"）";
        }else{
            return name;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Double getMoneySum() {
        return moneySum;
    }

    public void setMoneySum(Double moneySum) {
        this.moneySum = moneySum;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getMoneyLs() {
        return moneyLs;
    }

    public void setMoneyLs(Double moneyLs) {
        this.moneyLs = moneyLs;
    }

    public Double getMoneySumLs() {
        return moneySumLs;
    }

    public void setMoneySumLs(Double moneySumLs) {
        this.moneySumLs = moneySumLs;
    }
}
