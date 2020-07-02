package com.ultronyc.rvtaxsolutions.Stratup_consultation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.developer.kalert.KAlertDialog;
import com.ultronyc.rvtaxsolutions.HomeActivity;
import com.ultronyc.rvtaxsolutions.Income_tax_notice.IncomeTaxNoticeActivity;
import com.ultronyc.rvtaxsolutions.R;
import com.ultronyc.rvtaxsolutions.paymentMainactivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class StartUpConsultationActivity extends AppCompatActivity {

    ProgressBar progress_incometaxmain;
    Button submit;
    EditText full_name,email,mobile_no,calling_time;
    TextView basic,advance;
    ScrollView mail;

    private RadioGroup radioGroup;

    String plan="Basic";


    String loginId,profileName;


    String get_business_nature_list_url = "http://rvtaxs.com/android/business_nature.php";
    String URL_MAINSUBMIT = "http://rvtaxs.com/android/startup_consultation_master.php";


    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up_consultation);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = PreferenceManager.getDefaultSharedPreferences(StartUpConsultationActivity.this);


        final ActionBar abar = getSupportActionBar();
        //abar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("STARTUP CONSULTATION");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setIcon(R.color.transparent);
        abar.setHomeButtonEnabled(true);

        loginId = pref.getString("LOGIN_ID", null);
        profileName = pref.getString("PROFILE_NAME", null);

        // mail=(ScrollView)findViewById(R.id.incometax_scrollmain);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup_startup);




        full_name = (EditText) findViewById(R.id.startup_fullname);
        email = (EditText) findViewById(R.id.startup_email);
        mobile_no = (EditText) findViewById(R.id.startup_mobileno);
        calling_time = (EditText) findViewById(R.id.startup_calling_time);

        basic = (TextView) findViewById(R.id.basic_feture);
        advance = (TextView) findViewById(R.id.advance_feture);
        submit = (Button) findViewById(R.id.startup_submit);
        basic.setVisibility(View.VISIBLE);
        advance.setVisibility(View.GONE);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch(i) {
                    case R.id.yes:
                        // Pirates are the best
                        // Toast.makeText(ProjectReportLoanMainActivity.this, "Yes", Toast.LENGTH_SHORT).show();
                        basic.setVisibility(View.VISIBLE);
                        advance.setVisibility(View.GONE);
                        plan="Basic";

                        break;
                    case R.id.no:
                        // Ninjas rule
                        // Toast.makeText(ProjectReportLoanMainActivity.this, "Noo", Toast.LENGTH_SHORT).show();
                        basic.setVisibility(View.GONE);

                        advance.setVisibility(View.VISIBLE);

                        plan="Advance";

                        break;
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(SignUpActivity.this, OTP_Activity.class));

                String full_name1 = full_name.getText().toString();
                String email1 = email.getText().toString();
                String mobile_no1 = mobile_no.getText().toString();
                String calling_time1 = calling_time.getText().toString();


                if(full_name1.matches("")){
                    Toasty.error(StartUpConsultationActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    full_name.setError("Please Enter Full name!");
                }  else if(email1.matches("")){
                    Toasty.error(StartUpConsultationActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    email.setError("Enter Email");
                } else if(calling_time1.matches("")){
                    Toasty.error(StartUpConsultationActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    calling_time.setError("Enter Calling Time");
                } else if(mobile_no1.length() != 10){
                    Toasty.error(StartUpConsultationActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    mobile_no.setError("Mobile Number must be 10 digit");
                }else {
                    // get your editext value here
                    doSubmit(email1, mobile_no1,full_name1,calling_time1);

                }
            }
        });
    }


    //Form Submit
    private void   doSubmit(String  email,String  mobile_no,String full_name,String calling_time)
    {
        // progresssubmit.setVisibility(View.VISIBLE);
        submit.setEnabled(false);
        submit.setAlpha(0.5f);
        final String full_name1 = String.valueOf(full_name);
        final String email1 = String.valueOf(email);
        final String mobile_no1 = String.valueOf(mobile_no);
        final String calling_time1 = String.valueOf(calling_time);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_MAINSUBMIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("demo",""+response);
                        //Toast.makeText(SignUpActivity.this, "Response : " + " " + response, Toast.LENGTH_LONG).show();

                        try{
                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;

                            String startup_consultation = null;
                            String amt = null;
                            String email = null;
                            String number = null;
                            String surl = null;
                            String message = null;
                            for(int i = 0; i<ja.length(); i++){
                                jo = ja.getJSONObject(i);
                                message = jo.getString("Message");
                                if(message.equals("1")) {
                                    Toasty.success(StartUpConsultationActivity.this, "Data Saved,We will contact you soon when your payment is done.", Toast.LENGTH_LONG, true).show();
                                    startup_consultation = jo.getString("startup_consultation");
                                    amt = jo.getString("payment_rs");
                                    email = jo.getString("EMAIL_ID");
                                    number = jo.getString("CUSTOMER_NUMBER");

                                }
                            }

                            //Toast.makeText(SignUpActivity.this, "Message : " + " " + custId, Toast.LENGTH_SHORT).show();

                            if(message.equals("1")){

                                if(startup_consultation != null){
                                    Intent intent = new Intent(StartUpConsultationActivity.this, paymentMainactivity.class);
                                    intent.putExtra("tran_id", startup_consultation);
                                    intent.putExtra("AMOUNT", amt);
                                    intent.putExtra("EMAIL_ID", email);
                                    intent.putExtra("CUSTOMER_NUMBER", number);
                                    intent.putExtra("SURL", amt);
                                    intent.putExtra("FURL", amt);
                                    intent.putExtra("CURL", amt);
                                    startActivity(intent);
                                }

                            }

                            else {


                                new KAlertDialog(StartUpConsultationActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error!")
                                        .setContentText("There was an unexpected error, Please try again!")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog kAlertDialog) {
                                                kAlertDialog.dismiss();
                                            }
                                        })
                                        .confirmButtonColor(R.drawable.kalert_button_background)
                                        .show();
                            }

                            // progresssubmit.setVisibility(View.GONE);
                            submit.setEnabled(true);
                            submit.setAlpha(1);

                        } catch (JSONException e){
                            e.printStackTrace();

                            //Toast.makeText(SignUpActivity.this, "JSON Exception : " + " " + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("EXCEPTION", "JSON Exception : " + " " + e.toString());

                            new KAlertDialog(StartUpConsultationActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setContentText("Ops, Something went wrong!")
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog kAlertDialog) {
                                            kAlertDialog.dismiss();
                                        }
                                    })
                                    .confirmButtonColor(R.drawable.kalert_button_background)
                                    .show();

                            // progresssubmit.setVisibility(View.GONE);
                            submit.setEnabled(true);
                            submit.setAlpha(1);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //Toast.makeText(SignUpActivity.this, "Volley Error : " + " " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("EXCEPTION", "Volley Error : " + " " + error.toString());

                        new KAlertDialog(StartUpConsultationActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!")
                                .setContentText("Can't communicate with server, Please try again.")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog kAlertDialog) {
                                        kAlertDialog.dismiss();
                                    }
                                })
                                .confirmButtonColor(R.drawable.kalert_button_background)
                                .show();
                        submit.setEnabled(true);
                        submit.setAlpha(1);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("condition","IN");
                params.put("login_regi_no",loginId);
                params.put("full_name",full_name1);
                params.put("email",email1);
                params.put("mobile_no",mobile_no1);
                params.put("calling_time",calling_time1);
                params.put("plan",plan);
                params.put("status","pending");


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(StartUpConsultationActivity.this);
        requestQueue.add(stringRequest);
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
