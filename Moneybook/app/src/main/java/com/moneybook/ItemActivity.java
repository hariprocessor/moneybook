package com.moneybook;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;


public class ItemActivity extends ActionBarActivity {
    private TextView contentsTextView;
    private TextView amountTextView;
    private TextView dateTextView;
    private TextView timeTextView;
    private TextView incomeTextView;
    private TextView categoryTextView;

    private DBManager dbManager;

    private int[] currentDay;
    private int[] currentTime;

    private String id;
    private String use;
    private String amount;
    private String day;
    private String time;
    private String category;
    private String method;
    private String status;
    public void setCurrentDay(Calendar cal){
        currentDay = new int[3];
        currentDay[0] = cal.get(Calendar.YEAR);
        currentDay[1] = cal.get(Calendar.MONTH)+1;
        currentDay[2] = cal.get(Calendar.DAY_OF_MONTH);
    }

    public void setCurrentTime(Calendar cal){
        currentTime = new int[3];
        currentTime[0] = cal.get(Calendar.HOUR_OF_DAY);
        currentTime[1] = cal.get(Calendar.MINUTE);
    }

    private void setDateTextView(Calendar cal){
        dateTextView.setText(cal.get(Calendar.YEAR) + "년 " + (cal.get(Calendar.MONTH)+1) + "월 " + cal.get(Calendar.DAY_OF_MONTH) + "일");
    }

    private void setTimeTextView(Calendar cal){
        String ampm;
        if(cal.get(Calendar.AM_PM) == 0) ampm = "오전";
        else ampm = "오후";
        timeTextView.setText(ampm + " "+ (cal.get(Calendar.HOUR)) + "시 " + cal.get(Calendar.MINUTE) + "분");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        contentsTextView = (TextView) findViewById(R.id.contents_text_view);
        amountTextView = (TextView) findViewById(R.id.amount_text_view);
        dateTextView = (TextView) findViewById(R.id.date);
        timeTextView = (TextView) findViewById(R.id.time);
        incomeTextView = (TextView) findViewById(R.id.income_text_view);
        categoryTextView = (TextView) findViewById(R.id.category_text_view);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        use = intent.getExtras().getString("use");
        amount = intent.getExtras().getString("amount");
        day = intent.getExtras().getString("day");
        time = intent.getExtras().getString("time");
        category = intent.getExtras().getString("category");
        method = intent.getExtras().getString("method");
        status = intent.getExtras().getString("status");

        contentsTextView.setText(use);
        amountTextView.setText(amount);
        dateTextView.setText(day);
        timeTextView.setText(time);
        incomeTextView.setText(status);
        categoryTextView.setText(category);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
