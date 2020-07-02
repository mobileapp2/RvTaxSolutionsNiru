package com.ultronyc.rvtaxsolutions;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    RecyclerviewAdapterSettings myAdapter;
    RecyclerView recyclerView;

    ArrayList<CategorySettings> settingLst = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ActionBar abar = getSupportActionBar();
        //abar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Settings");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setIcon(R.color.transparent);
        abar.setHomeButtonEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.settingRecyclerView);

        settingLst.add(new CategorySettings(R.drawable.export_recy_icon, "Export Data", ""));
        settingLst.add(new CategorySettings(R.drawable.import_recy_icon, "Import Data", ""));
        settingLst.add(new CategorySettings(R.drawable.sale, "Delete All Sales", ""));
        settingLst.add(new CategorySettings(R.drawable.all_item_detail, "Delete All Items", ""));
        settingLst.add(new CategorySettings(R.drawable.share_recy_icon, "Share Backup", ""));
        settingLst.add(new CategorySettings(R.drawable.mail_enable, "Enable Mail", "true"));
        settingLst.add(new CategorySettings(R.drawable.daily_sms_icon, "Daily SMS", "true"));
        settingLst.add(new CategorySettings(R.drawable.printer, "Enable KOT", "true"));
        settingLst.add(new CategorySettings(R.drawable.settings_icon, "Import All Items", ""));
        settingLst.add(new CategorySettings(R.drawable.settings_icon, "Xprinter IP Address Set", ""));
        settingLst.add(new CategorySettings(R.drawable.settings_icon, "Send SMS Via", ""));

        //recyclerView.addItemDecoration(new GridSpacingDecoration(true, 22, 1));
        myAdapter = new RecyclerviewAdapterSettings(this, settingLst, getSupportFragmentManager());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
