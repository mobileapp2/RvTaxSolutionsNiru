package com.ultronyc.rvtaxsolutions.Itr_filling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.ultronyc.rvtaxsolutions.CategoryItemList;
import com.ultronyc.rvtaxsolutions.DialogAddItem;
import com.ultronyc.rvtaxsolutions.DialogForgetPassword;
import com.ultronyc.rvtaxsolutions.MainActivity;
import com.ultronyc.rvtaxsolutions.OtpActivity;
import com.ultronyc.rvtaxsolutions.R;
import com.ultronyc.rvtaxsolutions.RegistrationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class BusinessReturnActivity extends AppCompatActivity {


    SearchableSpinner businessnatureSpinner, turnoverSpinner, itrpurposeSpinner;
    ProgressBar progress_BnatureSpinner_BR,progress_businessturenoverSpinner_BR,progress_itrpurposeSpinner_BR,progress_BRmain;
    Button submit;
    EditText trading_name,pan_number,adhar_number, bank_ifsc_code,account_number, address,email,mobile_no;
    TextView birth_date,view_doc_list;
    ScrollView mail;

    String loginId,profileName;

    private int mYear, mMonth, mDay, type;

    String businessnatureStringName,turnoverStringName,itrpurposeStringName;
    boolean verify = false,verify1 = false,verify2 = false;
    int position1,position2,position3;
    int businessnature_id,turnover_id,itrpurpose_id;


    List<String> businessnaturedata1 = new ArrayList<String>();
    List<String> businessnatureName = new ArrayList<String>();
    List<String> businessnatureIdLst = new ArrayList<String>();

    List<String> turnoverdata1 = new ArrayList<String>();
    List<String> turnoverName = new ArrayList<String>();
    List<String> turnoverIdLst = new ArrayList<String>();

    List<String> itrpurposedata1 = new ArrayList<String>();
    List<String> itrpurposeName = new ArrayList<String>();
    List<String> itrpurposeIdLst = new ArrayList<String>();


    String get_business_nature_list_url = "http://rvtaxs.com/android/business_nature.php";
    String get_turn_over_list_br_url = "http://rvtaxs.com/android/turn_over.php";
    String get_itr_purpose_list_br_url = "http://rvtaxs.com/android/itr_purpose.php";
    String URL_MAINSUBMIT = "http://rvtaxs.com/android/business_returns_master.php";


    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_return);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = PreferenceManager.getDefaultSharedPreferences(BusinessReturnActivity.this);

        final ActionBar abar = getSupportActionBar();
        //abar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("ITR Business Return");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setIcon(R.color.transparent);
        abar.setHomeButtonEnabled(true);


         loginId = pref.getString("LOGIN_ID", null);
         profileName = pref.getString("PROFILE_NAME", null);

        mail=(ScrollView)findViewById(R.id.BR_mailScrollview);
        progress_BRmain = (ProgressBar) findViewById(R.id.progress_BRmain);
        mail.setVisibility(View.GONE);
        progress_BRmain.setVisibility(View.VISIBLE);

        businessnatureSpinner = (SearchableSpinner) findViewById(R.id.businessnaturespinner_BR);
        progress_BnatureSpinner_BR = (ProgressBar) findViewById(R.id.progress_BnatureSpinner_BR);

        turnoverSpinner = (SearchableSpinner) findViewById(R.id.businessturenoverspinner_BR);
        progress_businessturenoverSpinner_BR = (ProgressBar) findViewById(R.id.progress_businessturenoverSpinner_BR);

        itrpurposeSpinner = (SearchableSpinner) findViewById(R.id.itrpurposespinner_BR);
        progress_itrpurposeSpinner_BR = (ProgressBar) findViewById(R.id.progress_itrpurposeSpinner_BR);

        trading_name = (EditText) findViewById(R.id.BR_trading_name);
        pan_number = (EditText) findViewById(R.id.BR_pan_number);
        adhar_number = (EditText) findViewById(R.id.BR_aadhar_no);
        bank_ifsc_code = (EditText) findViewById(R.id.BR_bank_ifsc);
        account_number = (EditText) findViewById(R.id.BR_account_no);
        birth_date = (TextView) findViewById(R.id.BR_bdate);
        view_doc_list = (TextView) findViewById(R.id.BR_view_doc_list);
        //birth_date.setEnabled(false);
        address = (EditText) findViewById(R.id.BR_address);
        email = (EditText) findViewById(R.id.BR_email);
        mobile_no = (EditText) findViewById(R.id.BR_mobileno);

        submit = (Button) findViewById(R.id.BR_submit);

        GetBusinessNatureList_BR();
        GetTurnOverList_BR();
        GetItrPurposeList_BR();

        birth_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createdDialog(0).show();
                type = 1;
                pickdate(type);

            }
        });

        view_doc_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createdDialog(0).show();
                DialogBusinessReturnDocument dialog = new DialogBusinessReturnDocument();
                //dialog.setArguments(new Bundle());
                dialog.show(getSupportFragmentManager(), "ITR Business Return Documents");

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

                String trading_name1 = trading_name.getText().toString();
                String pan_number1 = pan_number.getText().toString();
                String adhar_number1 = adhar_number.getText().toString();
                String bank_ifsc_code1 = bank_ifsc_code.getText().toString();
                String account_number1 = account_number.getText().toString();
                String birth_date1 = birth_date.getText().toString();
                String address1 = address.getText().toString();
                String email1 = email.getText().toString();
                String mobile_no1 = mobile_no.getText().toString();

                 if(trading_name1.matches("")){
                     Toasty.error(BusinessReturnActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                     trading_name.setError("Please Trading name!");
                } else if(email1.matches("")){
                     Toasty.error(BusinessReturnActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    email.setError("Enter Email");
                } else if(pan_number1.matches("")){
                     Toasty.error(BusinessReturnActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                     pan_number.setError("Enter Pan Number");
                } else if(businessnatureStringName.matches("")){

                    Toasty.error(BusinessReturnActivity.this, "Select Business Nature!", Toasty.LENGTH_LONG).show();
                }else if(turnoverStringName.matches("")){
                    Toasty.error(BusinessReturnActivity.this, "Select Bysuness Turn Over!", Toasty.LENGTH_LONG).show();
                } else if(itrpurposeStringName.matches("")){
                    Toasty.error(BusinessReturnActivity.this, "Select ITR Purpose!", Toasty.LENGTH_LONG).show();
                } else if(mobile_no1.length() != 10){
                     Toasty.error(BusinessReturnActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                     mobile_no.setError("Mobile Number must be 10 digit");
                }else if(pan_number1.length() != 10){
                     Toasty.error(BusinessReturnActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                     pan_number.setError("Enter valied Pan Number");
                }else if(adhar_number1.length() != 12){
                     Toasty.error(BusinessReturnActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                     adhar_number.setError("Enter valied Aadhar Number");
                 }else if(adhar_number1.matches("")){
                     Toasty.error(BusinessReturnActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                     adhar_number.setError("Enter Aadhar Number");
                }else if(bank_ifsc_code1.matches("")){
                     Toasty.error(BusinessReturnActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                     bank_ifsc_code.setError("Enter Bank IFSC Code");
                }else if(account_number1.matches("")){
                     Toasty.error(BusinessReturnActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                     account_number.setError("Enter Bank Account Number");
                 }else if(birth_date1.matches("")){
                     Toasty.error(BusinessReturnActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                     birth_date.setError("Select Birth Date");
                 }else if(address1.matches("")){
                     Toasty.error(BusinessReturnActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                     address.setError("Enter full Address");
                 }else {
                     // get your editext value here

                     Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

                     Matcher matcher = pattern.matcher(pan_number1);
// Check if pattern matches
                     if (matcher.matches()) {
                         doSubmit(trading_name1, email1, pan_number1, mobile_no1, adhar_number1,bank_ifsc_code1,account_number1,birth_date1,address1);
                     }
                     else
                     {
                         Toasty.error(BusinessReturnActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                         pan_number.setError("Enter valied Pan Number");
                     }

                 }
            }
        });


    }


    //Get Business Nature List ro Spinner
    private void GetBusinessNatureList_BR() {
        progress_BnatureSpinner_BR.setVisibility(View.VISIBLE);

        businessnaturedata1.clear();
        businessnatureName.clear();
        businessnatureIdLst.clear();
        businessnatureSpinner.setAdapter(null);
        businessnatureStringName = null;

        // Log.d("checkss", " response = " + " " + get_contry_list_url);

        final String statement = "Select";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_business_nature_list_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("checkss", " response = " + " " + response);
                        try {

                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;
                            for (int i = 0; i < ja.length(); i++) {
                                jo = ja.getJSONObject(i);
                                businessnaturedata1.add(jo.getString("nature_name") + jo.getString("id"));
                                businessnatureIdLst.add(jo.getString("id"));
                            }

                            try {
                                for (int i = 0; i <  businessnaturedata1.size(); i++) {
                                    String temp;
                                    temp =  businessnaturedata1.get(i);
                                    temp = temp.replaceAll("[0-9]", "");
                                    businessnatureName.add(temp);
                                }

                                if ( businessnatureName.isEmpty()) {
                                    Toasty.warning(getApplicationContext(), "Business Nature is empty", Toast.LENGTH_SHORT).show();
                                } else {
                                    progress_BnatureSpinner_BR.setVisibility(View.GONE);//.setVisibility(View.GONE);
                                    final ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(BusinessReturnActivity.this, android.R.layout.simple_spinner_item,  businessnatureName); //selected item will look like a spinner set from XML
                                    spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    businessnatureSpinner.setAdapter(spinnerArrayAdapter1);

                                    businessnatureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                            position1 = position;
                                            try {
                                                String s =  businessnaturedata1.get(position1);
                                                s = s.replaceAll("[^\\d]", "");
                                                businessnature_id = Integer.decode(s);
                                            } catch (Exception e) {}


                                            businessnatureStringName =  businessnatureSpinner.getSelectedItem().toString().trim();

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                }

                                verify = true;

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(RegistrationActivity.this, "Submit Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("checkss", " response = " + " " + e.toString());
                            progress_BnatureSpinner_BR.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(RegistrationActivity.this, "Submit Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("checkss", " response = " + " " + error.toString());
                        progress_BnatureSpinner_BR.setVisibility(View.GONE);
                        //scrollViewMainLayout.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("condition", "GET");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(BusinessReturnActivity.this);
        requestQueue.add(stringRequest);
    }


    //Get Turn Over List ro Spinner
    private void GetTurnOverList_BR() {
        progress_businessturenoverSpinner_BR.setVisibility(View.VISIBLE);

        turnoverdata1.clear();
        turnoverName.clear();
        turnoverIdLst.clear();
        turnoverSpinner.setAdapter(null);
        turnoverStringName = null;

        // Log.d("checkss", " response = " + " " + get_contry_list_url);

        final String statement = "Select";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_turn_over_list_br_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("checkss", " response = " + " " + response);
                        try {

                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;
                            for (int i = 0; i < ja.length(); i++) {
                                jo = ja.getJSONObject(i);
                                turnoverdata1.add(jo.getString("turn_over"));
                                turnoverIdLst.add(jo.getString("id"));
                            }

                            try {
                                for (int i = 0; i <  turnoverdata1.size(); i++) {
                                    String temp;
                                    temp =  turnoverdata1.get(i);
                                  //  temp = temp.replaceAll("[0-9]", "");
                                    turnoverName.add(temp);
                                }

                                if ( turnoverName.isEmpty()) {
                                    Toasty.warning(getApplicationContext(), "Contry Name is empty", Toast.LENGTH_SHORT).show();
                                } else {
                                    progress_businessturenoverSpinner_BR.setVisibility(View.GONE);//.setVisibility(View.GONE);
                                    final ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(BusinessReturnActivity.this, android.R.layout.simple_spinner_item,  turnoverdata1); //selected item will look like a spinner set from XML
                                    spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    turnoverSpinner.setAdapter(spinnerArrayAdapter1);

                                    turnoverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                            position2 = position;
                                            try {
                                               // Log.d("Turn_over",""+position2);
                                                String s =  turnoverIdLst.get(position2);
                                               // Log.d("Turn_over",""+s);
                                               // s = s.replaceAll("[^\\d]", "");
                                                turnover_id = Integer.decode(s);
                                               // Log.d("Turn_over",""+turnover_id);
                                            } catch (Exception e) {}


                                            turnoverStringName =  turnoverSpinner.getSelectedItem().toString().trim();

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                }

                                verify1 = true;

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(RegistrationActivity.this, "Submit Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("checkss", " response = " + " " + e.toString());
                            progress_businessturenoverSpinner_BR.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(RegistrationActivity.this, "Submit Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("checkss", " response = " + " " + error.toString());
                        progress_businessturenoverSpinner_BR.setVisibility(View.GONE);
                        //scrollViewMainLayout.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("condition", "GET");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(BusinessReturnActivity.this);
        requestQueue.add(stringRequest);
    }


    //Get ITR Purpose List ro Spinner
    private void GetItrPurposeList_BR() {
        progress_itrpurposeSpinner_BR.setVisibility(View.VISIBLE);

        itrpurposedata1.clear();
        itrpurposeName.clear();
        itrpurposeIdLst.clear();
        itrpurposeSpinner.setAdapter(null);
        itrpurposeStringName = null;

        // Log.d("checkss", " response = " + " " + get_contry_list_url);

        final String statement = "Select";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_itr_purpose_list_br_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("checkss", " response = " + " " + response);
                        try {

                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;
                            for (int i = 0; i < ja.length(); i++) {
                                jo = ja.getJSONObject(i);
                                itrpurposedata1.add(jo.getString("itr_purpose") + jo.getString("id"));
                                itrpurposeIdLst.add(jo.getString("id"));
                            }

                            try {
                                for (int i = 0; i <  itrpurposedata1.size(); i++) {
                                    String temp;
                                    temp =  itrpurposedata1.get(i);
                                    temp = temp.replaceAll("[0-9]", "");
                                    itrpurposeName.add(temp);
                                }

                                if ( itrpurposeName.isEmpty()) {
                                    Toasty.warning(getApplicationContext(), "ITR Purpose is empty", Toast.LENGTH_SHORT).show();
                                } else {
                                    progress_itrpurposeSpinner_BR.setVisibility(View.GONE);//.setVisibility(View.GONE);
                                    final ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(BusinessReturnActivity.this, android.R.layout.simple_spinner_item,  itrpurposeName); //selected item will look like a spinner set from XML
                                    spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    itrpurposeSpinner.setAdapter(spinnerArrayAdapter1);

                                    itrpurposeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                            position3 = position;
                                            try {
                                                String s =  itrpurposedata1.get(position3);
                                                s = s.replaceAll("[^\\d]", "");
                                                itrpurpose_id = Integer.decode(s);
                                            } catch (Exception e) {}


                                            itrpurposeStringName =  itrpurposeSpinner.getSelectedItem().toString().trim();

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                }

                                verify2 = true;
                                mail.setVisibility(View.VISIBLE);
                                progress_BRmain.setVisibility(View.GONE);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(RegistrationActivity.this, "Submit Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("checkss", " response = " + " " + e.toString());
                            progress_itrpurposeSpinner_BR.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(RegistrationActivity.this, "Submit Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("checkss", " response = " + " " + error.toString());
                        progress_itrpurposeSpinner_BR.setVisibility(View.GONE);
                        //scrollViewMainLayout.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("condition", "GET");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(BusinessReturnActivity.this);
        requestQueue.add(stringRequest);
    }


    //Form Submit
    private void  doSubmit(String trading_name, String email, String pan_number, String mobile_no, String adhar_number,String bank_ifsc_code,String account_number,String birth_date,String address)
    {
       // progresssubmit.setVisibility(View.VISIBLE);
        submit.setEnabled(false);
        submit.setAlpha(0.5f);

        final String trading_name1 = String.valueOf(trading_name);
        final String email1 = String.valueOf(email);
        final String pan_number1 = String.valueOf(pan_number);
        final String mobile_no1 = String.valueOf(mobile_no);
        final String adhar_number1 = String.valueOf(adhar_number);
        final String bank_ifsc_code1 = String.valueOf(bank_ifsc_code);
        final String account_number1 = String.valueOf(account_number);
        final String birth_date1 = String.valueOf(birth_date);
        final String address1 = String.valueOf(address);

        final String businessnatureid = String.valueOf(businessnature_id);
        final String turnoverid = String.valueOf(turnover_id);
        final String itrpurposeid = String.valueOf(itrpurpose_id);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_MAINSUBMIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("demo",""+response);
                        //Toast.makeText(SignUpActivity.this, "Response : " + " " + response, Toast.LENGTH_LONG).show();

                        try{
                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;

                            String br_registration_no = null;
                            String amt = null;
                            String email = null;
                            String number = null;
                            String surl = null;
                            String message = null;
                            for(int i = 0; i<ja.length(); i++){
                                jo = ja.getJSONObject(i);
                                message = jo.getString("Message");
                                if(message.equals("1")) {
                                    Toasty.success(BusinessReturnActivity.this, "Data Saved.", Toast.LENGTH_SHORT, true).show();
                                    br_registration_no = jo.getString("br_registration_no");
                                    amt = jo.getString("payment_rs");
                                    email = jo.getString("EMAIL_ID");
                                    number = jo.getString("CUSTOMER_NUMBER");

                                }
                            }

                            //Toast.makeText(SignUpActivity.this, "Message : " + " " + custId, Toast.LENGTH_SHORT).show();

                            if(message.equals("1")){

                                if(br_registration_no != null){
                                    Intent intent = new Intent(BusinessReturnActivity.this, UploadBusinessReturnDocActivity.class);
                                    intent.putExtra("br_registration_no", br_registration_no);
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


                                new KAlertDialog(BusinessReturnActivity.this, KAlertDialog.ERROR_TYPE)
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

                            new KAlertDialog(BusinessReturnActivity.this, KAlertDialog.ERROR_TYPE)
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

                        new KAlertDialog(BusinessReturnActivity.this, KAlertDialog.ERROR_TYPE)
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
                params.put("business_nature_id",businessnatureid);
                params.put("trading_name",trading_name1);
                params.put("turn_over_id",turnoverid);
                params.put("itr_purpose_id",itrpurposeid);
                params.put("pan_no",pan_number1);
                params.put("aadhar_no",adhar_number1);
                params.put("bank_ifsc",bank_ifsc_code1);
                params.put("bank_account_np",account_number1);
                params.put("bdate",birth_date1);
                params.put("full_address",address1);
                params.put("email",email1);
                params.put("mobile_no",mobile_no1);
                params.put("status","pending");


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(BusinessReturnActivity.this);
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
    public void pickdate(final int type){

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int mm=monthOfYear + 1;
                        int dd=dayOfMonth;
                        String month,date1;
                        if(mm>9)
                        {
                            month=""+mm;

                        }
                        else
                        {
                            month="0"+mm;
                        }
                        if(dd>9)
                        {
                            date1=""+dd;

                        }
                        else
                        {
                            date1="0"+dd;
                        }
                        String date = date1+"-"+month+"-"+year ;

                        if(type == 1){
                            birth_date.setText(date);
                        }


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

}
