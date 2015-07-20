package com.moneybook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {

    public DBManager(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("1");
        db.execSQL("create table moneybook (_id integer primary key AUTOINCREMENT, use text, amount integer, day integer, time integer, category text, method text, status text)");
        System.out.println("2");
        db.execSQL("create table category (_id integer primary key AUTOINCREMENT, name text)");
        System.out.println("3");
        db.execSQL("create table method (_id integer primary key AUTOINCREMENT, name text)");
        System.out.println("4");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertMoneyBook(String use, int amount, int day, int time, String category, String method, String status){
        SQLiteDatabase db = getWritableDatabase();
        String _query = "insert into moneybook (use, amount, day, time, category, method, status) values ('"+use+"', "+amount+", "+day+", "+time+", '"+category+"', '"+method+"', '"+status+"');";
        db.execSQL(_query);
        System.out.println(_query);
        db.close();
    }
    public void updateMoneyBook(String id, String use, int amount, int day, int time, String category, String method, String status){
        SQLiteDatabase db = getWritableDatabase();
        String _query = "update moneybook set use='"+use+"', amount="+amount+", day="+day+", time="+time+", category='"+category+"', method='"+method+"', status='"+status+"' where _id="+id+";";
        db.execSQL(_query);
        System.out.println(_query);
        db.close();
    }

    public void insertCategory(String name){
        SQLiteDatabase db = getWritableDatabase();
        String _query = "insert into category (name) values ('"+name+"');";
        db.execSQL(_query);
        System.out.println(_query);
        db.close();
    }

    public void insertMethod(String name){
        SQLiteDatabase db = getWritableDatabase();
        String _query = "insert into method (name) values ('"+name+"');";
        db.execSQL(_query);
        System.out.println(_query);
        db.close();
    }



    public String[][] findMoneyBook(String use, int min_amount, int max_amount, int min_day, int max_day, int min_time, int max_time, String category, String method, String status){
        int count = 0;
        if(use != null) count++;
        if(min_amount != -1) count++;
        if(max_amount != -1) count++;
        if(min_day != -1) count++;
        if(max_day != -1) count++;
        if(min_time != -1) count++;
        if(max_time != -1) count++;
        if(category != null)count++;
        if(method != null) count++;
        if(status != null) count++;

        String _query = "select * from moneybook";
        if(count != 0) _query += " where ";
        if(use != null) {
            _query += "use='%" + use + "%'";
            if(count != 1) _query += " and ";
            count--;
        }
        if(min_amount != -1) {
            _query += "amount>=" + min_amount + "";
            if(count != 1) _query += " and ";
            count--;
        }
        if(max_amount != -1) {
            _query += "amount<=" + max_amount + "";
            if(count != 1) _query += " and ";
            count--;
        }
        if(min_day != -1) {
            _query += "day>=" + min_day + "";
            if(count != 1) _query += " and ";
            count--;
        }
        if(max_day != -1) {
            _query += "day<=" + max_day + "";
            if(count != 1) _query += " and ";
            count--;
        }
        if(min_time != -1) {
            _query += "time>=" + min_time + "";
            if(count != 1) _query += " and ";
            count--;
        }
        if(max_time != -1) {
            _query += "time<=" + max_time + "";
            if(count != 1) _query += " and ";
            count--;
        }

        if(category != null) {
            _query += "category='" + category + "'";
            if(count != 1) _query += " and ";
            count--;
        }
        if(method != null) {
            _query += "method='" + method + "'";
            if(count != 1) _query += " and ";
            count--;
        }
        if(status != null) {
            _query += "status='" + status + "'";
            if(count != 1) _query += " and ";
            count--;
        }
        _query += ";";
        System.out.println(_query);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(_query, null);


        String[][] str = new String[cursor.getCount()][8];
        int i = 0;
        while(cursor.moveToNext()) {
            str[i][0] = cursor.getString(0);
            str[i][1] = cursor.getString(1);
            str[i][2] = cursor.getString(2);
            str[i][3] = cursor.getString(3);
            str[i][4] = cursor.getString(4);
            str[i][5] = cursor.getString(5);
            str[i][6] = cursor.getString(6);
            str[i][7] = cursor.getString(7);
            i++;
        }

        return str;
    }

    public void delete(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        System.out.println(_query);
        db.close();
    }

    public void deleteMoneyBook(String id){
        SQLiteDatabase db = getWritableDatabase();
        String _query = "delete from moneybook where _id="+id+";";
        db.execSQL(_query);
        System.out.println(_query);
        db.close();
    }

    public String[] findCategory(String name){
        SQLiteDatabase db = getReadableDatabase();
        String _query = "select * from category";
        if(name != null) _query += " where name='"+name+"'";
        _query += ";";

        Cursor cursor = db.rawQuery(_query, null);
        System.out.println(_query);

        String[] str = new String[cursor.getCount()];
        System.out.println("get count : "+cursor.getCount());

        int i = 0;
        while(cursor.moveToNext()) {
            str[i] = cursor.getString(1);
            System.out.println("cursor.getString(1) : "+cursor.getString(1));
            i++;
        }
        return str;
    }
}