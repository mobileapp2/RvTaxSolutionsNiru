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
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.developer.kalert.KAlertDialog;
import com.ultronyc.rvtaxsolutions.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class OnlyTdsRefundActivity extends AppCompatActivity {


    ProgressBar progress_BRmain;
    Button submit;
    EditText full_name,mobile_number,address, email,pan_number, addhar_number,bank_account_number,bank_ifsc_code;
    ScrollView mail;
    TextView view_doc_list;
    private RadioGroup radioGroup;

    String army_flag;

    String URL_MAINSUBMIT = "http://rvtaxs.com/android/only_tds_refund_master.php";

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String loginId,profileName;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_tds_refund);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = PreferenceManager.getDefaultSharedPreferences(OnlyTdsRefundActivity.this);
        final ActionBar abar = getSupportActionBar();
        //abar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("ITR Only TDS Refund");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setIcon(R.color.transparent);
        abar.setHomeButtonEnabled(true);

        loginId = pref.getString("LOGIN_ID", null);
        profileName = pref.getString("PROFILE_NAME", null);

        full_name = (EditText) findViewById(R.id.OTR_full_name);
        pan_number = (EditText) findViewById(R.id.OTR_pan_number);
        addhar_number = (EditText) findViewById(R.id.OTR_aadhar_number);
        bank_ifsc_code = (EditText) findViewById(R.id.OTR_ifsc_code);
        bank_account_number = (EditText) findViewById(R.id.OTR_account_number);
        address = (EditText) findViewById(R.id.OTR_address);
        email = (EditText) findViewById(R.id.OTR_email);
        mobile_number = (EditText) findViewById(R.id.OTR_mobile_number);


        submit = (Button) findViewById(R.id.OTR_submit);


        view_doc_list = (TextView) findViewById(R.id.OTR_view_doc_list);

        view_doc_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createdDialog(0).show();
                DialogSalaryReturnDocument dialog = new DialogSalaryReturnDocument();
                //dialog.setArguments(new Bundle());
                dialog.show(getSupportFragmentManager(), "ITR Only TDS Refund Documents");

            }
        });





        pan_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    // Toast.makeText(getApplicationContext(), "Got the focus", Toast.LENGTH_LONG).show();
                } else {
                    // Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();
                    String panno = pan_number.getText().toString();

                    pan_number.setText(panno.toUpperCase());
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(SignUpActivity.this, OTP_Activity.class));

                String full_name1 = full_name.getText().toString();
                String pan_number1 = pan_number.getText().toString();
                String addhar_number1 = addhar_number.getText().toString();
                String bank_ifsc_code1 = bank_ifsc_code.getText().toString();
                String bank_account_number1 = bank_account_number.getText().toString();
                String address1 = address.getText().toString();
                String email1 = email.getText().toString();
                String mobile_no1 = mobile_number.getText().toString();

                if(full_name1.matches("")){
                    Toasty.error(OnlyTdsRefundActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    full_name.setError("Please Enter Full name!");
                } else if(email1.matches("")){
                    Toasty.error(OnlyTdsRefundActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    email.setError("Enter Email");
                } else if(pan_number1.matches("")){
                    Toasty.error(OnlyTdsRefundActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    pan_number.setError("Enter Pan Number");
                } else if(mobile_no1.length() != 10){
                    mobile_number.setError("Mobile Number must be 10 digit");
                }else if(pan_number1.length() != 10){
                    Toasty.error(OnlyTdsRefundActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    pan_number.setError("Enter valied Pan Number");
                }else if(addhar_number1.length() != 12){
                    Toasty.error(OnlyTdsRefundActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    addhar_number.setError("Enter valied Aadhar Number");
                } else if(addhar_number1.matches("")){
                    Toasty.error(OnlyTdsRefundActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    addhar_number.setError("Enter Aadhar Number");
                }else if(bank_ifsc_code1.matches("")){
                    Toasty.error(OnlyTdsRefundActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    bank_ifsc_code.setError("Enter Bank IFSC Code");
                }else if(bank_account_number1.matches("")){
                    Toasty.error(OnlyTdsRefundActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    bank_account_number.setError("Enter Bank Account Number");
                }else if(address1.matches("")){
                    Toasty.error(OnlyTdsRefundActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    address.setError("Enter full Address");
                }else {
                    Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

                    Matcher matcher = pattern.matcher(pan_number1);
// Check if pattern matches
                    if (matcher.matches()) {
                        doSubmit(full_name1, pan_number1, addhar_number1, bank_ifsc_code1, bank_account_number1,address1,email1,mobile_no1);
                    }
                    else
                    {
                        Toasty.error(OnlyTdsRefundActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                        pan_number.setError("Enter valied Pan Number");
                    }

                }
            }
        });

    }

    //Form Submit
    private void  doSubmit(String full_name,String pan_number,String addhar_number,String bank_ifsc_code,String bank_account_number,String address,String email,String mobile_no)
    {
        // progresssubmit.setVisibility(View.VISIBLE);
        submit.setEnabled(false);
        submit.setAlpha(0.5f);

        final String full_name1 = String.valueOf(full_name);
        final String email1 = String.valueOf(email);
        final String pan_number1 = String.valueOf(pan_number);
        final String mobile_no1 = String.valueOf(mobile_no);
        final String addhar_number1 = String.valueOf(addhar_number);
        final String bank_ifsc_code1 = String.valueOf(bank_ifsc_code);
        final String bank_account_number1 = String.valueOf(bank_account_number);
        final String address1 = String.valueOf(address);
        Log.d("demoNiru",""+full_name1);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_MAINSUBMIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("demoNiru",""+response);
                        //Toast.makeText(SignUpActivity.this, "Response : " + " " + response, Toast.LENGTH_LONG).show();

                        try{
                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;

                            String otr_registration_no = null;
                            String message = null;
                            String amt = null;
                            String email = null;
                            String number = null;
                            String surl = null;
                            for(int i = 0; i<ja.length(); i++){
                                jo = ja.getJSONObject(i);
                                message = jo.getString("Message");
                                if(message.equals("1")) {
                                    Toasty.success(OnlyTdsRefundActivity.this, "Data Saved!", Toast.LENGTH_SHORT, true).show();
                                    otr_registration_no = jo.getString("otr_registration_no");
                                    amt = jo.getString("payment_rs");
                                    email = jo.getString("EMAIL_ID");
                                    number = jo.getString("CUSTOMER_NUMBER");

                                }
                            }

                            //Toast.makeText(SignUpActivity.this, "Message : " + " " + custId, Toast.LENGTH_SHORT).show();

                            if(message.equals("1")){

                                if(otr_registration_no != null){
                                    Intent intent = new Intent(OnlyTdsRefundActivity.this, UploadOnlyTDSRefundActivity.class);
                                    intent.putExtra("otr_registration_no", otr_registration_no);
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


                                new KAlertDialog(OnlyTdsRefundActivity.this, KAlertDialog.ERROR_TYPE)
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

                            new KAlertDialog(OnlyTdsRefundActivity.this, KAlertDialog.ERROR_TYPE)
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

                        new KAlertDialog(OnlyTdsRefundActivity.this, KAlertDialog.ERROR_TYPE)
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
                params.put("address",address1);
                params.put("pan_number",pan_number1);
                params.put("adhar_number",addhar_number1);
                params.put("bank_ifsc",bank_ifsc_code1);
                params.put("bank_account_number",bank_account_number1);
                params.put("email",email1);
                params.put("status","pending");


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(OnlyTdsRefundActivity.this);
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