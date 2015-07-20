package com.moneybook;
public class ListData {
    public String id;
    public String use;
    public String amount;
    public String day;
    public String time;
    public String category;
    public String method;
    public String status;
    public ListData(String id, String use, String amount, String day, String time, String category, String method, String status){
        this.id = id;
        this.use = use;
        this.amount = amount;
        this.day=day;
        this.time = time;
        this.category = category;
        this.method = method;
        this.status = status;
    }
}