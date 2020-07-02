package com.ultronyc.rvtaxsolutions;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomerListActivity extends AppCompatActivity {

    List<CategoryCustList> lstCategory = new ArrayList();
    RecyclerviewAdapterCustList myAdapter;
    RecyclerView recyclerView;
    EditText searchEdtx;
    TextView totalSavedCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ActionBar abar = getSupportActionBar();
        //abar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("All Customers Information");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setIcon(R.color.transparent);
        abar.setHomeButtonEnabled(true);


        searchEdtx = (EditText) findViewById(R.id.searchEditText);
        totalSavedCustomers = (TextView) findViewById(R.id.totalSavedCustomerTextView);
        recyclerView = (RecyclerView) findViewById(R.id.customerListRecyclerView);
        
        lstCategory = new ArrayList<>();

        lstCategory.add(new CategoryCustList("shubham shinde", "9503696428", "shindesshubham96@gmail.com", "satara", "13-05-1996", "None"));
        lstCategory.add(new CategoryCustList("niranjan kakatkar", "9503696428", "niranjankakatkar95@gmail.com", "satara", "20-07-1995", "None"));


        //CategoryCustList categoryCustList = new CategoryCustList("shubham shinde", "9503696428", "shindesshubham96@gmail.com", "satara", "13-05-1996", "None");
        //list.add(categoryCustList);


        recyclerView.addItemDecoration(new GridSpacingDecoration(true, 25, 1));
        myAdapter = new RecyclerviewAdapterCustList(this, lstCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);


        int size = lstCategory.size();

        if(size > 0){
            totalSavedCustomers.setText(String.valueOf(size));
        }

        searchEdtx.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void filter(String text) {
        ArrayList<CategoryCustList> temp = new ArrayList<>();

        for (CategoryCustList d : lstCategory) {
            if (d.getCustFullName().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        myAdapter.updateList(temp);
    }





}














