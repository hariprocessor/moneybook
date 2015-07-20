package com.moneybook;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

public class MainActivity extends ActionBarActivity {

    private ActionBar actionBar;
    private ActionBar.Tab tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);

        tab = actionBar.newTab()
                .setText(R.string.today_tab)
                .setTabListener(new TabListener<TodayFragment>(
                        this, "today", TodayFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(R.string.find_tab)
                .setTabListener(new TabListener<FindFragment>(
                        this, "setting", FindFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(R.string.setting_tab)
                .setTabListener(new TabListener<SettingFragment>(
                        this, "setting", SettingFragment.class));
        actionBar.addTab(tab);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();


    }
}


