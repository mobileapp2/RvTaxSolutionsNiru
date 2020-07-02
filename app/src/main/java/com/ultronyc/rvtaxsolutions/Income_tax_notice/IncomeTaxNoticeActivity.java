package com.ultronyc.rvtaxsolutions.Income_tax_notice;

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
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.ultronyc.rvtaxsolutions.HomeActivity;
import com.ultronyc.rvtaxsolutions.R;
import com.ultronyc.rvtaxsolutions.Udyog_adhar_registration.UdyogAdharRegistrationActivity;
import com.ultronyc.rvtaxsolutions.Udyog_adhar_registration.UploadUhyodAdharRegistrationDocActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class IncomeTaxNoticeActivity extends AppCompatActivity {

    ProgressBar progress_incometaxmain;
    Button submit;
    EditText full_name,email,mobile_no;
    ScrollView mail;

    String loginId,profileName;


    String get_business_nature_list_url = "http://rvtaxs.com/android/business_nature.php";
    String URL_MAINSUBMIT = "http://rvtaxs.com/android/income_tax_notice_master.php";


    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_tax_notice);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = PreferenceManager.getDefaultSharedPreferences(IncomeTaxNoticeActivity.this);

        final ActionBar abar = getSupportActionBar();
        //abar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("INCOME TAX NOTICE");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setIcon(R.color.transparent);
        abar.setHomeButtonEnabled(true);


        loginId = pref.getString("LOGIN_ID", null);
        profileName = pref.getString("PROFILE_NAME", null);

       // mail=(ScrollView)findViewById(R.id.incometax_scrollmain);


        full_name = (EditText) findViewById(R.id.incometax_fullname);
        email = (EditText) findViewById(R.id.incometax_email);
        mobile_no = (EditText) findViewById(R.id.incometax_mobileno);
        submit = (Button) findViewById(R.id.incometax_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(SignUpActivity.this, OTP_Activity.class));

                String full_name1 = full_name.getText().toString();
                String email1 = email.getText().toString();
                String mobile_no1 = mobile_no.getText().toString();


                if(full_name1.matches("")){
                    Toasty.error(IncomeTaxNoticeActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    full_name.setError("Please Enter Full name!");
                }  else if(email1.matches("")){
                    Toasty.error(IncomeTaxNoticeActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    email.setError("Enter Email");
                } else if(mobile_no1.length() != 10){
                    Toasty.error(IncomeTaxNoticeActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    mobile_no.setError("Mobile Number must be 10 digit");
                }else {
                    // get your editext value here
       doSubmit(email1, mobile_no1,full_name1);

                }
            }
        });

    }


    //Form Submit
    private void   doSubmit(String  email,String  mobile_no,String full_name)
    {
        // progresssubmit.setVisibility(View.VISIBLE);
        submit.setEnabled(false);
        submit.setAlpha(0.5f);
        final String full_name1 = String.valueOf(full_name);
        final String email1 = String.valueOf(email);
        final String mobile_no1 = String.valueOf(mobile_no);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_MAINSUBMIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("demo",""+response);
                        //Toast.makeText(SignUpActivity.this, "Response : " + " " + response, Toast.LENGTH_LONG).show();

                        try{
                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;

                            String income_tax_notice_registration_no = null;
                            String amt = null;
                            String email = null;
                            String number = null;
                            String surl = null;
                            String message = null;
                            for(int i = 0; i<ja.length(); i++){
                                jo = ja.getJSONObject(i);
                                message = jo.getString("Message");
                                if(message.equals("1")) {
                                    Toasty.success(IncomeTaxNoticeActivity.this, "Data Saved.", Toast.LENGTH_SHORT, true).show();
                                    income_tax_notice_registration_no = jo.getString("income_tax_notice_registration_no");
                                    amt = jo.getString("payment_rs");
                                    email = jo.getString("EMAIL_ID");
                                    number = jo.getString("CUSTOMER_NUMBER");

                                }
                            }

                            //Toast.makeText(SignUpActivity.this, "Message : " + " " + custId, Toast.LENGTH_SHORT).show();

                            if(message.equals("1")){

                                if(income_tax_notice_registration_no != null){
                                    Intent intent = new Intent(IncomeTaxNoticeActivity.this, UploadIncomeTaxNoriceDocActivity.class);
                                    intent.putExtra("INCOMETAX_registration_no", income_tax_notice_registration_no);
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


                                new KAlertDialog(IncomeTaxNoticeActivity.this, KAlertDialog.ERROR_TYPE)
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

                            new KAlertDialog(IncomeTaxNoticeActivity.this, KAlertDialog.ERROR_TYPE)
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

                        new KAlertDialog(IncomeTaxNoticeActivity.this, KAlertDialog.ERROR_TYPE)
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
                params.put("status","pending");


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(IncomeTaxNoticeActivity.this);
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
