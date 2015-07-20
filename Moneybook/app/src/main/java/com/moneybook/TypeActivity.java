package com.moneybook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;


public class TypeActivity extends ActionBarActivity{
    private DBManager dbManager;
    private String[] type;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private EditText addEditText;
    private Button addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_type);
        dbManager = new DBManager(getApplicationContext(), "money_book.db", null, 1);
        addEditText = (EditText) findViewById(R.id.add_edit_text);
        addButton = (Button) findViewById(R.id.add_button);
        refresh();
        addEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    return true;
                }
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String newText = String.valueOf(addEditText.getText());
                    if(dbManager.findCategory(newText).length != 0) {
                        Toast.makeText(TypeActivity.this, "'"+newText+"' 은/는 이미 존재하는 타입입니다.", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    else {
                        dbManager.insertCategory(newText);
                        refresh();
                        Toast.makeText(TypeActivity.this, "'" + newText + "' 이/가 성공적으로 추가되었습니다.", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = String.valueOf(addEditText.getText());
                if(dbManager.findCategory(newText).length != 0)
                    Toast.makeText(TypeActivity.this, "'"+newText+"' 은/는 이미 존재하는 타입입니다.", Toast.LENGTH_SHORT).show();
                else {
                    dbManager.insertCategory(newText);
                    refresh();
                    Toast.makeText(TypeActivity.this, "'" + newText + "' 이/가 성공적으로 추가되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void refresh(){
        type = dbManager.findCategory(null);
        Arrays.sort(type);
        listView = (ListView) findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, type);
        listView.setAdapter(arrayAdapter);
        addEditText.setText("");

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DeleteConfirmDialogFragment dialogFragment = new DeleteConfirmDialogFragment(arrayAdapter.getItem(position));
                dialogFragment.show(getSupportFragmentManager(), "dialog");
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_type, menu);
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
    private class DeleteConfirmDialogFragment extends DialogFragment {
        private AlertDialog.Builder builder;
        private DBManager dbManager;
        private String type;
        public DeleteConfirmDialogFragment(String type){
            this.type = type;
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            dbManager = new DBManager(getActivity().getApplicationContext(), "money_book.db", null, 1);
            builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.fragment_delete_confirm_dialog_message)
                    .setPositiveButton(R.string.fragment_delete_confirm_dialog_delete, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dbManager.delete("delete from type where type='"+type+"';");
                            refresh();
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(R.string.fragment_delete_confirm_dialog_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            return builder.create();
        }
    }

}
