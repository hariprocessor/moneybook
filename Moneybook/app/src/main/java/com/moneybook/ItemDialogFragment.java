package com.moneybook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.util.Calendar;


public class ItemDialogFragment extends DialogFragment {
    private DBManager dbManager;
    private View rootView;
    private LayoutInflater inflater;
    private EditText contentsEditText;
    private TextView contentsTextView;
    private EditText amountEditText;
    private TextView dateTextView;
    private TextView timeTextView;
    private RadioButton plusRadioButton;
    private RadioButton minusRadioButton;
    private TextView categoryTextView;
    private String id;
    private String use;
    private String amount;
    private String day;
    private String time;
    private String category;
    private String method;
    private String status;
    private String income;

    private int[] currentDay;
    private int[] currentTime;
    private Calendar cal;

    public ItemDialogFragment(String id, String use, String amount, String day, String time, String category, String method, String status) {
        this.id = id;
        this.use = use;
        this.amount = amount;
        this.day=day;
        this.time = time;
        this.category = category;
        this.method = method;
        this.status = status;
    }
/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        rootView = inflater.inflate(R.layout.fragment_item_dialog, container, false);
        if(rootView == null) System.out.println("rootView is null");
        contentsTextView = (TextView) rootView.findViewById(R.id.contents);
        amountTextView = (TextView) rootView.findViewById(R.id.amount);
        dateTextView = (TextView) rootView.findViewById(R.id.date);
        timeTextView = (TextView) rootView.findViewById(R.id.time);
        statusTextView = (TextView) rootView.findViewById(R.id.status);
        categoryTextView = (TextView) rootView.findViewById(R.id.category);

        contentsTextView.setText(use);
        amountTextView.setText(amount);

        dateTextView.setText(day);
        timeTextView.setText(time);
        statusTextView.setText(status);
        categoryTextView.setText(category);
        return rootView;
    }
    */

    private void setDateTextView(Calendar cal){
        System.out.println(cal.get(Calendar.YEAR));
        dateTextView.setText(cal.get(Calendar.YEAR) + "년 " + (cal.get(Calendar.MONTH)+1) + "월 " + cal.get(Calendar.DAY_OF_MONTH) + "일");
    }

    private void setTimeTextView(Calendar cal){
        String ampm;
        if(cal.get(Calendar.AM_PM) == 0) ampm = "오전";
        else ampm = "오후";
        timeTextView.setText(ampm + " "+ (cal.get(Calendar.HOUR)) + "시 " + cal.get(Calendar.MINUTE) + "분");
    }

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
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        dbManager = new DBManager(getActivity().getApplicationContext(), "money_book.db", null, 1);
        rootView = inflater.inflate(R.layout.fragment_item_dialog, null);
        if(rootView == null) System.out.println("rootView is null");

        currentDay = new int[3];
        currentTime = new int[2];
        contentsTextView = (TextView) rootView.findViewById(R.id.contents_text_view);
        contentsEditText = (EditText) rootView.findViewById(R.id.contents_edit_text);
        amountEditText = (EditText) rootView.findViewById(R.id.amount);
        dateTextView = (TextView) rootView.findViewById(R.id.date);
        timeTextView = (TextView) rootView.findViewById(R.id.time);
        plusRadioButton = (RadioButton) rootView.findViewById(R.id.income);
        minusRadioButton = (RadioButton) rootView.findViewById(R.id.expense);

        categoryTextView = (TextView) rootView.findViewById(R.id.category);

        contentsEditText.setText(use);
        contentsTextView.setText(use);
        amountEditText.setText(amount);

        int tmpDay, tmpTime;

        tmpDay = Integer.parseInt(day);
        tmpTime = Integer.parseInt(time);
        int year = tmpDay/10000;
        int month = (tmpDay - year*10000) / 100;
        int days = (tmpDay - year*10000 - month*100);
        int hour = tmpTime/100;
        int minute = tmpTime - hour*100;

        cal = Calendar.getInstance();
        cal.set(year, month-1, days, hour, minute);
        setCurrentDay(cal);
        setCurrentTime(cal);
        setDateTextView(cal);
        setTimeTextView(cal);

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, monthOfYear, dayOfMonth);
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
                TimePickerDialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(1, 1, 1, hourOfDay, minute);
                        setTimeTextView(cal);
                        setCurrentTime(cal);
                    }
                }, currentTime[0], currentTime[1],false);
                dialog.show();
            }
        });

        income = status;
        if(status == "+") plusRadioButton.setChecked(true);
        else minusRadioButton.setChecked(true);
        categoryTextView.setText(category);
        builder.setView(rootView)
        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (plusRadioButton.isChecked()) income = "+";
                else income = "-";
                int date = currentDay[0]*10000 + currentDay[1]*100 + currentDay[2];
                int time = currentTime[0]*100 + currentTime[1];
                dbManager.updateMoneyBook(id, String.valueOf(contentsEditText.getText()), Integer.parseInt(String.valueOf(amountEditText.getText())),date, time, category, "현금", income);
            }
        })
        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("취소버튼");
            }
        });

        Dialog dialog = builder.create();

        return dialog;
    }
/*
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    */

}
