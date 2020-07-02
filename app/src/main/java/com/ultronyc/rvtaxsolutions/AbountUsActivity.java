package com.ultronyc.rvtaxsolutions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ultronyc.rvtaxsolutions.Project_report_for_loan.DialogProjectReportLoanDocument;
import com.ultronyc.rvtaxsolutions.Project_report_for_loan.ProjectReportLoanMainActivity;

public class AbountUsActivity extends AppCompatActivity {

    ImageView whatsapp,facebook,youtube,twitter,gmail,instagram;
    TextView text_email,text_mobile;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abount_us);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = PreferenceManager.getDefaultSharedPreferences(AbountUsActivity.this);

        final ActionBar abar = getSupportActionBar();
        //abar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("ABOUT US");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setIcon(R.color.transparent);
        abar.setHomeButtonEnabled(true);

        whatsapp = (ImageView) findViewById(R.id.whatsapp);
        facebook = (ImageView) findViewById(R.id.facebook);
        youtube = (ImageView) findViewById(R.id.youtube);
        gmail = (ImageView) findViewById(R.id.gmail);
        instagram = (ImageView) findViewById(R.id.instagram);
        text_email = (TextView) findViewById(R.id.text_email);
        text_mobile = (TextView) findViewById(R.id.text_mobile);


        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createdDialog(0).show();

                String number="+919075523183";
                String url = "https://api.whatsapp.com/send?phone="+number+"&text=Hi";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createdDialog(0).show();

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/cambridgesaicollection"));
                    startActivity(intent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,    Uri.parse("http://www.facebook.com/cambridgesaicollection")));
                }

            }
        });



        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createdDialog(0).show();

                Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse("https://www.youtube.com/channel/UCPz-9blciUUkt5XprdEO7NA?view_as=subscriber"));
                startActivity(intent);

            }
        });

        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createdDialog(0).show();

                try{
                    Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "rvtax1616@gmail.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "About App");
                    intent.putExtra(Intent.EXTRA_TEXT, "your_text");
                    startActivity(intent);
                }catch(Exception e){
                    //TODO smth
                }

            }
        });

        text_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createdDialog(0).show();

                try{
                    Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "rvtax1616@gmail.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "About App");
                    intent.putExtra(Intent.EXTRA_TEXT, "your_text");
                    startActivity(intent);
                }catch(Exception e){
                    //TODO smth
                }

            }
        });

        text_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createdDialog(0).show();
               String posted_by = "907-552-318-3";

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+919075523183"));
                startActivity(intent);

            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createdDialog(0).show();

                Uri uri = Uri.parse("http://instagram.com/_u/rv.taxsolutins.2020");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/xxx")));
                }

            }
        });

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
