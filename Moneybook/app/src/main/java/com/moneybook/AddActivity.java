package com.moneybook;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class AddActivity extends ActionBarActivity {
    private EditText contentsEditText;
    private EditText amountEditText;
    private Button registerButton;
    private DBManager dbManager;
    private long now;

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private String[] array;
    private String income;
    private int type;
    private View tempView;

    private Calendar cal;
    private int date;
    private int time;
    private TextView dateTextView;
    private TextView timeTextView;
    private int[] currentDay;
    private int[] currentTime;
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
        setContentView(R.layout.activity_add);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_add);
        cal = Calendar.getInstance();
        date = cal.get(Calendar.YEAR)*10000 + (cal.get(Calendar.MONTH)+1)*100 + cal.get(Calendar.DAY_OF_MONTH);
        time = cal.get(Calendar.HOUR_OF_DAY)*100 + cal.get(Calendar.MINUTE);
        contentsEditText = (EditText) findViewById(R.id.contents_edit_text);
        amountEditText = (EditText) findViewById(R.id.amount_edit_text);
        registerButton = (Button) findViewById(R.id.register_button);
        dateTextView = (TextView) findViewById(R.id.date);
        timeTextView = (TextView) findViewById(R.id.time);

        setDateTextView(cal);
        setTimeTextView(cal);
        setCurrentDay(cal);
        setCurrentTime(cal);
        dbManager = new DBManager(getApplicationContext(), "money_book.db", null, 1);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbManager.insertMoneyBook(String.valueOf(contentsEditText.getText()), Integer.parseInt(String.valueOf(amountEditText.getText())),date, time, arrayAdapter.getItem(type), "현금", income);
                finish();
            }
        });

        array = dbManager.findCategory(null);
        Arrays.sort(array);
        listView = (ListView) findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
        listView.setAdapter(arrayAdapter);
        tempView = null;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(tempView != null) tempView.setBackgroundColor(Color.WHITE);
                view.setBackgroundColor(Color.YELLOW);
                tempView = view;
                type = position;
            }
        });

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, monthOfYear, dayOfMonth);
                        date = year*10000+(monthOfYear+1)*100+dayOfMonth;
                        setDateTextView(cal);
                        setCurrentDay(cal);
                    }
                },currentDay[0], currentDay[1]-1, currentDay[2]);
                dialog.show();
            }
        });

        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(AddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(1, 1, 1, hourOfDay, minute);
                        time = hourOfDay*100 + minute;
                        setTimeTextView(cal);
                        setCurrentTime(cal);
                    }
                }, currentTime[0], currentTime[1],false);
                dialog.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if(id == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            case R.id.expense:
                income = "-";
                break;
            case R.id.income:
                income = "+";
                break;
        }

    }


}
