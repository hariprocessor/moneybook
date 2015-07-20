package com.moneybook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class TodayFragment extends Fragment {
    private View rootView;
    private TextView todayTextView;
    private ListView listView;
    private ListViewAdapter listViewAdapter;
    private DBManager dbManager;
    private FloatingActionButton fab;
    private Intent addIntent;
    private Calendar cal;
    private int[] currentDay;
    public TodayFragment() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setTodayTextView(){
        String temp = currentDay[0]+"년 "+currentDay[1]+"월 "+currentDay[2]+"일";
        todayTextView.setText(temp);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbManager = new DBManager(getActivity().getApplicationContext(), "money_book.db", null, 1);
        init();
        /* layout 초기화 */
        rootView = inflater.inflate(R.layout.fragment_today, container, false);
        todayTextView = (TextView) rootView.findViewById(R.id.today);
        listView = (ListView) rootView.findViewById(R.id.list_view);
        listViewAdapter = new ListViewAdapter(getActivity());
        listView.setAdapter(listViewAdapter);

        /* day 초기화 */
        currentDay = new int[3];
        cal = Calendar.getInstance();
        setCurrentDay(cal);
        setTodayTextView();

        /* refresh 화면 초기화 */
        refresh(cal);

        /* listview 에 클릭이벤트
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListData listData = listViewAdapter.listData.get(position);
                Toast.makeText(getActivity(), listData.contents, Toast.LENGTH_LONG).show();
            }
        });
*/
        /* FAB */
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.attachToListView(listView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIntent = new Intent(getActivity(), AddActivity.class);
                startActivity(addIntent);
                getActivity().overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
            }
        });

        /* 오늘날짜에 클릭이벤트 */
        todayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int day = year*10000+monthOfYear*100+dayOfMonth;
                        System.out.println("In DatePicker Listener : "+day);
                        Calendar cal = Calendar.getInstance();
                        System.out.println(year);
                        cal.set(year, monthOfYear, dayOfMonth);
                        setCurrentDay(cal);
                        setTodayTextView();
                        String[][] str = dbManager.findMoneyBook(null, -1, -1, day, day, -1, -1, null, null, null);
                        refresh(cal);
                        for(int i = 0; i < str.length; i++){
                            for(int j = 0; j < str[i].length; j++)
                                System.out.print(str[i][j] + " ");
                            System.out.print("\n");
                        }
                    }
                }, currentDay[0], currentDay[1]-1, currentDay[2]);
                dialog.show();
            }
        });
        init();
        return rootView;
    }

    public  int getDay(Calendar cal){
        return cal.get(Calendar.YEAR)*10000+(cal.get(Calendar.MONTH)+1)*100+cal.get(Calendar.DAY_OF_MONTH);
    }

    public int getTime(Calendar cal){
        return cal.get(Calendar.HOUR_OF_DAY)*100+cal.get(Calendar.MINUTE);
    }

    public void setCurrentDay(Calendar cal){
        currentDay[0] = cal.get(Calendar.YEAR);
        currentDay[1] = cal.get(Calendar.MONTH)+1;
        currentDay[2] = cal.get(Calendar.DAY_OF_MONTH);
    }
    public void init(){
        if(dbManager.findCategory(null).length == 0) {
            System.out.println("기타 생성");
            dbManager.insertCategory("기타");
        }
    }

    public void refresh(Calendar cal){
        String[][] moneyBook;
        moneyBook = dbManager.findMoneyBook(null, -1, -1, getDay(cal), getDay(cal), -1, -1, null, null, null);
        listViewAdapter = new ListViewAdapter(getActivity());
        listView.setAdapter(listViewAdapter);
        if(moneyBook.length != 0) {
            for (int i = 0; i < moneyBook.length; i++) {
                listViewAdapter.addItem(moneyBook[i][0], moneyBook[i][1], moneyBook[i][2], moneyBook[i][3], moneyBook[i][4], moneyBook[i][5], moneyBook[i][6], moneyBook[i][7]);
            }
        }
/*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        */
        listView.invalidateViews();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    private class ViewHolder{
        public TextView contentsTextView;
        public TextView amountTextView;
    }

    private class ListViewAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<ListData> listData = new ArrayList<ListData>();

        public ListViewAdapter(Context context){
            super();
            this.context = context;
        }
        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.today_list, null);

                holder.contentsTextView = (TextView) convertView.findViewById(R.id.contents);
                holder.amountTextView = (TextView) convertView.findViewById(R.id.amount);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            final ListData mData = listData.get(position);

            holder.contentsTextView.setText(mData.use);
            holder.amountTextView.setText(mData.amount);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    ItemDialogFragment itemDialogFragment = new ItemDialogFragment(mData.id, mData.use, mData.amount, mData.day, mData.time, mData.category, mData.method, mData.status);
                    itemDialogFragment.show(getActivity().getSupportFragmentManager(), "hi?");
                    */
                    Intent intent = new Intent(getActivity().getApplicationContext(), ItemActivity.class);
                    intent.putExtra("id", mData.id);
                    intent.putExtra("use", mData.use);
                    intent.putExtra("amount", mData.amount);
                    intent.putExtra("day", mData.day);
                    intent.putExtra("time", mData.time);
                    intent.putExtra("category", mData.category);
                    intent.putExtra("method", mData.method);
                    intent.putExtra("status", mData.status);

                    startActivity(intent);

                }
            });
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("삭제할까요?")
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    remove(position);
                                    dbManager.deleteMoneyBook(mData.id);
                                }
                            });
                    builder.show();
                    return false;
                }
            });
            return convertView;
        }

        public void addItem(String id, String use, String amount, String day, String time, String category, String method, String status){
            ListData addInfo = new ListData(id, use, amount, day, time, category, method, status);
            listData.add(addInfo);
        }

        public void remove(int position){
            listData.remove(position);
            dataChange();
        }

        public void dataChange(){
            listViewAdapter.notifyDataSetChanged();
        }
    }
}
