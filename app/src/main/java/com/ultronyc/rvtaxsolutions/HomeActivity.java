package com.ultronyc.rvtaxsolutions;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import com.ultronyc.rvtaxsolutions.Itr_filling.BusinessReturnActivity;
import com.ultronyc.rvtaxsolutions.Itr_filling.UploadBusinessReturnDocActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    List<Category> lstCategory = new ArrayList();
    RecyclerviewAdapter myAdapter;
    RecyclerView recyclerView;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String loginId,profileName;

    TextView settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        pref = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
         loginId = pref.getString("LOGIN_ID", null);
         profileName = pref.getString("PROFILE_NAME", null);

        settings = (TextView) findViewById(R.id.settings_TV);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_Home);
        
       lstCategory.add(new Category("", "ITR FILLING", R.drawable.masters, R.color.masters_blue, R.color.text_color1, R.color.text_color2, "com.ultronyc.rvtaxsolutions.Itr_filling.IteFillingMainActivity"));
        lstCategory.add(new Category("", "GST REGISTRATION", R.drawable.gst_registration, R.color.sales_pink, R.color.text_color1, R.color.text_color2, "com.ultronyc.rvtaxsolutions.Gst_registration.GstRegistrationMainActivity"));
        lstCategory.add(new Category("", "GST RETURN", R.drawable.gst_registration, R.color.sales_pink, R.color.text_color1, R.color.text_color2, "com.ultronyc.rvtaxsolutions.Gst_returns.GstReturnMainActivity"));
        lstCategory.add(new Category("", "SHOPACT REGISTRATION", R.drawable.shop_act, R.color.reports_pink, R.color.text_color1, R.color.text_color2, "com.ultronyc.rvtaxsolutions.Shopact_registration.ShopActRegistrationActivity"));
        lstCategory.add(new Category("", "UDYOG ADHAAR", R.drawable.udyog_adhar, R.color.graph_blue, R.color.text_color1, R.color.text_color2, "com.ultronyc.rvtaxsolutions.Udyog_adhar_registration.UdyogAdharRegistrationActivity"));
        lstCategory.add(new Category("", "STARTUP CONSULTATION", R.drawable.startup_consultation, R.color.masters_blue, R.color.text_color1, R.color.text_color2, "com.ultronyc.rvtaxsolutions.Stratup_consultation.StartUpConsultationActivity"));
        lstCategory.add(new Category("", "PROJECT REPORT FOR LOAN", R.drawable.loan, R.color.masters_blue, R.color.text_color1, R.color.text_color2, "com.ultronyc.rvtaxsolutions.Project_report_for_loan.ProjectReportLoanMainActivity"));
        lstCategory.add(new Category("", "PTEC", R.drawable.certificate, R.color.masters_blue, R.color.text_color1, R.color.text_color2, "com.ultronyc.rvtaxsolutions.Ptec.PtecMainActivity"));




        recyclerView.addItemDecoration(new GridSpacingDecoration(true, 15, 2));
        myAdapter = new RecyclerviewAdapter(HomeActivity.this, lstCategory, getSupportFragmentManager());
        recyclerView.setLayoutManager(new GridLayoutManager(HomeActivity.this, 2));
        recyclerView.setAdapter(myAdapter);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        drawer.animate();

        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        navigationView.setNavigationItemSelectedListener(this);


        updateNavHeader();


    }


    /*public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

         if (id == R.id.nav_update_profile) {
             Intent intent = new Intent(HomeActivity.this, UpdateProfileActivity.class);
             startActivity(intent);

        }
         else if (id == R.id.nav_billing) {
             Intent intent = new Intent(HomeActivity.this, MyPaymentStatusActivity.class);
             startActivity(intent);
             //Toast.makeText(getApplicationContext(), "Payment History", Toast.LENGTH_SHORT).show();

         }

         else if (id == R.id.nav_masters) {
            // Handle the camera action
             Intent intent = new Intent(HomeActivity.this, MyApplicationActivity.class);
             startActivity(intent);
        }
        else if (id == R.id.nav_settings) {


        }
        else if (id == R.id.nav_about) {
             Intent intent = new Intent(HomeActivity.this, AbountUsActivity.class);
             startActivity(intent);

        }else if (id == R.id.nav_share) {
             try {
                 Intent shareIntent = new Intent(Intent.ACTION_SEND);
                 shareIntent.setType("text/plain");
                 shareIntent.putExtra(Intent.EXTRA_SUBJECT, "RV TAXS SOLUTIONS");
                 String shareMessage= "\nWe Are Online Tax Consulting Service,We Provide GST, Income Tax, &amp; All Other Business Services\n" +
                         "Our Work Prepaid Under The Professionals\n\n";
                 shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                 shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                 startActivity(Intent.createChooser(shareIntent, "choose one"));
             } catch(Exception e) {
                 //e.toString();
             }

         }else if (id == R.id.nav_logout) {
           // Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
             editor = pref.edit();
             editor.putString("LOGIN_ID", null);
             editor.putString("PROFILE_NAME", null);
             editor.commit();
             editor.apply();

             Intent intent = new Intent(HomeActivity.this, MainActivity.class);
             intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             startActivity(intent);
             finish();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }




    public void updateNavHeader(){

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView compName = headerView.findViewById(R.id.drawerFirmTitleTextView);
        TextView userName = headerView.findViewById(R.id.drawerUserNameTextView);
        TextView mobile = headerView.findViewById(R.id.drawerUserMobileTextView);

        compName.setText(""+profileName);
        userName.setText("Registration No: "+loginId);
        mobile.setText("");


        //TextView navUsername = headerView.findViewById(R.id.nav_username);
        //TextView navUserMail = headerView.findViewById(R.id.nav_user_mail);
        //ImageView navuserPhoto = headerView.findViewById(R.id.nav_user_photo);

        //navUsername.setText("Shubham");
        //navUserMail.setText("shindesshubham@gmail.com");

        //navUsername.setTextColor(Color.parseColor("#000000"));
        //navUserMail.setTextColor(Color.parseColor("#000000"));

        //navuserPhoto.setImageResource(R.drawable.photo_male);

    }

}




















































