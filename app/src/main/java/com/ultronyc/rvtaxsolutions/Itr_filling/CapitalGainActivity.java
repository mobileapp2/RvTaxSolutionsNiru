package com.ultronyc.rvtaxsolutions.Itr_filling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.developer.kalert.KAlertDialog;
import com.ultronyc.rvtaxsolutions.R;
import com.ultronyc.rvtaxsolutions.paymentMainactivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CapitalGainActivity extends AppCompatActivity {

    ProgressBar progress_BRmain;
    Button submit;
    EditText full_name,mobile_number,address, email,pan_number, addhar_number,bank_account_number,bank_ifsc_code;
    ScrollView mail;
    TextView view_doc_list;

    String URL_MAINSUBMIT = "http://rvtaxs.com/android/capital_gain_master.php";

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String loginId,profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capital_gain);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = PreferenceManager.getDefaultSharedPreferences(CapitalGainActivity.this);
        final ActionBar abar = getSupportActionBar();
        //abar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("ITR Capital Gain");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setIcon(R.color.transparent);
        abar.setHomeButtonEnabled(true);


        loginId = pref.getString("LOGIN_ID", null);
        profileName = pref.getString("PROFILE_NAME", null);

        full_name = (EditText) findViewById(R.id.CG_fullname);
        mobile_number = (EditText) findViewById(R.id.CG_mobileno);

        email = (EditText) findViewById(R.id.CG_email);


        submit = (Button) findViewById(R.id.CG_submit);


        view_doc_list = (TextView) findViewById(R.id.CG_view_doc_list);

        view_doc_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createdDialog(0).show();
                DialogPensionReturnDocument dialog = new DialogPensionReturnDocument();
                dialog.show(getSupportFragmentManager(), "ITR House Property Documents");

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(SignUpActivity.this, OTP_Activity.class));

                String full_name1 = full_name.getText().toString();
                String email1 = email.getText().toString();
                String mobile_no1 = mobile_number.getText().toString();

                if(full_name1.matches("")){
                    full_name.setError("Please Enter Full name!");
                } else if(email1.matches("")){
                    email.setError("Enter Email");
                }  else if(mobile_no1.length() != 10){
                    mobile_number.setError("Mobile Number must be 10 digit");
                }else {
                    doSubmit(full_name1,email1,mobile_no1);
                }
            }
        });

    }

    //Form Submit
    private void  doSubmit(String full_name,String email,String mobile_no)
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

                            String cg_registration_no = null;
                            String amt = null;
                            String email = null;
                            String number = null;
                            String message = null;
                            for(int i = 0; i<ja.length(); i++){
                                jo = ja.getJSONObject(i);
                                message = jo.getString("Message");
                                if(message.equals("1")) {
                                    cg_registration_no = jo.getString("cg_registration_no");
                                    amt = jo.getString("payment_rs");
                                    email = jo.getString("EMAIL_ID");
                                    number = jo.getString("CUSTOMER_NUMBER");

                                }
                            }

                            //Toast.makeText(SignUpActivity.this, "Message : " + " " + custId, Toast.LENGTH_SHORT).show();

                            if(message.equals("1")){

                                if(cg_registration_no != null){
                                    Intent intent = new Intent(CapitalGainActivity.this, paymentMainactivity.class);
                                    intent.putExtra("tran_id", cg_registration_no);
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


                                new KAlertDialog(CapitalGainActivity.this, KAlertDialog.ERROR_TYPE)
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

                            new KAlertDialog(CapitalGainActivity.this, KAlertDialog.ERROR_TYPE)
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

                        new KAlertDialog(CapitalGainActivity.this, KAlertDialog.ERROR_TYPE)
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
                params.put("mobile_no",mobile_no1);
                params.put("email",email1);
                params.put("status","pending");


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(CapitalGainActivity.this);
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
