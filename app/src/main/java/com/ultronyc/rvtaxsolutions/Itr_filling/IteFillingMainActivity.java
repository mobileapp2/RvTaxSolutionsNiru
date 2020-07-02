package com.ultronyc.rvtaxsolutions.Itr_filling;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ultronyc.rvtaxsolutions.Category;
import com.ultronyc.rvtaxsolutions.GridSpacingDecoration;
import com.ultronyc.rvtaxsolutions.R;
import com.ultronyc.rvtaxsolutions.RecyclerviewAdapterMasters;

import java.util.ArrayList;
import java.util.List;

public class IteFillingMainActivity extends AppCompatActivity {

    List<Category> lstCategory = new ArrayList();
    RecyclerviewAdapterMasters myAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itrfillingmain);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ActionBar abar = getSupportActionBar();
        //abar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("ITR FILLING");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setIcon(R.color.transparent);
        abar.setHomeButtonEnabled(true);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_masters);
        //List<Category> list = lstCategory;
        lstCategory = new ArrayList<>();

        lstCategory.add(new Category("Business Return", "", R.drawable.business_return, R.color.white, R.color.text_color1, R.color.text_color2, "com.ultronyc.rvtaxsolutions.Itr_filling.BusinessReturnActivity"));
        lstCategory.add(new Category("Salary Return", "", R.drawable.salary_return, R.color.white, R.color.text_color1, R.color.text_color2, "com.ultronyc.rvtaxsolutions.Itr_filling.SalaryReturnActivity"));
        lstCategory.add(new Category("Pension", "", R.drawable.pension, R.color.white, R.color.text_color1, R.color.text_color2, "com.ultronyc.rvtaxsolutions.Itr_filling.PensionReturnActivity"));
        lstCategory.add(new Category("House Property", "", R.drawable.house_property, R.color.white, R.color.text_color1, R.color.text_color2, "com.ultronyc.rvtaxsolutions.Itr_filling.HousePropertyActivity"));
        lstCategory.add(new Category("Income Tax Notice", "", R.drawable.income_tax, R.color.white, R.color.text_color1, R.color.text_color2, "com.ultronyc.rvtaxsolutions.Income_tax_notice.IncomeTaxNoticeActivity"));
        lstCategory.add(new Category("Capital Gain", "", R.drawable.capital_gain, R.color.white, R.color.text_color1, R.color.text_color2, "com.ultronyc.rvtaxsolutions.Itr_filling.CapitalGainActivity"));
        lstCategory.add(new Category("Only TDS Refund", "", R.drawable.tds_refund, R.color.white, R.color.text_color1, R.color.text_color2, "com.ultronyc.rvtaxsolutions.Itr_filling.OnlyTdsRefundActivity"));
        //lstCategory.add(new Category("Add User", "", R.drawable.salesman, R.color.white, R.color.text_color1, R.color.text_color2, "com.ultronyc.rvtaxs.AddUserActivity"));
        //lstCategory.add(new Category("User List", "", R.drawable.salesman_list, R.color.white, R.color.text_color1, R.color.text_color2, "com.ultronyc.rvtaxs.UserListActivity"));


//        Category category = new Category("Add Item", "", R.drawable.add_item_neww, R.color.white, R.color.text_color1, R.color.text_color2, "");
//        list.add(category);
//        List<Category> list2 = lstCategory;
//        Category category2 = new Category("Item List", "", R.drawable.item_list_new, R.color.white, R.color.text_color1, R.color.text_color2, "");
//        list2.add(category2);
//        List<Category> list3 = lstCategory;
//        Category category3 = new Category("Add Customer", "", R.drawable.add_customer, R.color.white, R.color.text_color1, R.color.text_color2, "");
//        list3.add(category3);
//        List<Category> list4 = lstCategory;
//        Category category4 = new Category("Customer List", "", R.drawable.customer_list, R.color.white, R.color.text_color1, R.color.text_color2, "");
//        list4.add(category4);
//        List<Category> list5 = lstCategory;
//        Category category5 = new Category("Add User", "", R.drawable.salesman, R.color.white, R.color.text_color1, R.color.text_color2, "");
//        list5.add(category5);
//        List<Category> list6 = lstCategory;
//        Category category6 = new Category("User List", "", R.drawable.salesman_list, R.color.white, R.color.text_color1, R.color.text_color2, "");
//        list6.add(category6);
        
        
        recyclerView.addItemDecoration(new GridSpacingDecoration(true, 25, 1));
        myAdapter = new RecyclerviewAdapterMasters(this, lstCategory, getSupportFragmentManager());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
        
        

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






}


























