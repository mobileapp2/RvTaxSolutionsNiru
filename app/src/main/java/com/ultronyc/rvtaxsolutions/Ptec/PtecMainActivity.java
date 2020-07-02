package com.ultronyc.rvtaxsolutions.Ptec;

import android.app.DatePickerDialog;
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
import android.widget.LinearLayout;
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
import com.ultronyc.rvtaxsolutions.Gst_registration.DialogGstRegistrationDocument;
import com.ultronyc.rvtaxsolutions.Gst_registration.UploadGstRegistrationDocActivity;
import com.ultronyc.rvtaxsolutions.Itr_filling.BusinessReturnActivity;
import com.ultronyc.rvtaxsolutions.R;

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


public class PtecMainActivity extends AppCompatActivity {



    SearchableSpinner businessnatureSpinner, GSTTYPESpinner, itrpurposeSpinner;
    ProgressBar progress_BnatureSpinner_ptec,progress_ptecTYPESpinner_ptec,progress_itrpurposeSpinner_BR,progress_ptecmain;
    Button submit;
    EditText full_name,gst_number,pan_number,adhar_number, bank_ifsc_code,account_number, address,email,mobile_no;
    TextView birth_date,view_doc_list,GSTTYPEPLAN_AMT,GSTTYPEPLAN_DESC;
    ScrollView mail;
    LinearLayout GSTPLANDESC;

    String loginId,profileName;

    private int mYear, mMonth, mDay, type;

    String charges;

    String businessnatureStringName,GSTTYPEStringName,itrpurposeStringName;
    boolean verify = false,verify1 = false,verify2 = false;
    int position1,position2,position3;
    int businessnature_id,GSTTYPE_id,itrpurpose_id;


    List<String> businessnaturedata1 = new ArrayList<String>();
    List<String> businessnatureName = new ArrayList<String>();
    List<String> businessnatureIdLst = new ArrayList<String>();

    List<String> GSTTYPEdata1 = new ArrayList<String>();
    List<String> GSTTYPEName = new ArrayList<String>();
    List<String> GSTTYPEIdLst = new ArrayList<String>();



    String get_business_nature_list_url = "http://rvtaxs.com/android/business_nature.php";
    String get_ptecTYPE_list_url = "http://rvtaxs.com/android/gst_return_type.php";
    String URL_MAINSUBMIT = "http://rvtaxs.com/android/ptec_master.php";


    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptec_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = PreferenceManager.getDefaultSharedPreferences(PtecMainActivity.this);

        final ActionBar abar = getSupportActionBar();
        //abar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("PROFESSIONAL TAX REGISTRATION ");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setIcon(R.color.transparent);
        abar.setHomeButtonEnabled(true);


        loginId = pref.getString("LOGIN_ID", null);
        profileName = pref.getString("PROFILE_NAME", null);

        mail=(ScrollView)findViewById(R.id.ptec_mainScroll);
        progress_ptecmain = (ProgressBar) findViewById(R.id.progress_ptecmain);
        mail.setVisibility(View.GONE);
        progress_ptecmain.setVisibility(View.VISIBLE);

        businessnatureSpinner = (SearchableSpinner) findViewById(R.id.businessnaturespinner_ptec);
        progress_BnatureSpinner_ptec = (ProgressBar) findViewById(R.id.progress_BnatureSpinner_ptec);

        GSTTYPESpinner = (SearchableSpinner) findViewById(R.id.GSTTYPEspinner_ptec);
        progress_ptecTYPESpinner_ptec = (ProgressBar) findViewById(R.id.progress_ptecTYPESpinner_ptec);
        view_doc_list = (TextView) findViewById(R.id.GST_view_doc_list);
        GSTTYPEPLAN_AMT = (TextView) findViewById(R.id.GSTTYPEPLAN_AMT);
        GSTTYPEPLAN_DESC = (TextView) findViewById(R.id.GSTTYPEPLAN_DESC);


        full_name = (EditText) findViewById(R.id.full_name_ptec);
        email = (EditText) findViewById(R.id.email_ptec);
        mobile_no = (EditText) findViewById(R.id.mobile_no_ptec);
        address = (EditText) findViewById(R.id.address_ptec);
        pan_number = (EditText) findViewById(R.id.pancard_ptec);
        adhar_number = (EditText) findViewById(R.id.aadharcard_ptec);
        birth_date = (TextView) findViewById(R.id.establishment_date);

        bank_ifsc_code = (EditText) findViewById(R.id.ptec_bank_ifsc);
        account_number = (EditText) findViewById(R.id.ptec_account_no);


        gst_number = (EditText) findViewById(R.id.gst_number_ptec);
        submit = (Button) findViewById(R.id.ptec_submit);

        GetBusinessNatureList_BR();
        GetGSTYPEList_ptec();


        birth_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createdDialog(0).show();
                type = 1;
                pickdate(type);

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

        view_doc_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createdDialog(0).show();
                DialogPtecDocument dialog = new DialogPtecDocument();
                //dialog.setArguments(new Bundle());
                dialog.show(getSupportFragmentManager(), "ITR Business Return Documents");

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(SignUpActivity.this, OTP_Activity.class));

                String gst_number1 = gst_number.getText().toString();
                String pan_number1 = pan_number.getText().toString();
                String adhar_number1 = adhar_number.getText().toString();
                String address1 = address.getText().toString();
                String full_name1 = full_name.getText().toString();
                String email1 = email.getText().toString();
                String mobile_no1 = mobile_no.getText().toString();
                String bank_ifsc_code1 = bank_ifsc_code.getText().toString();
                String account_number1 = account_number.getText().toString();
                String birth_date1 = birth_date.getText().toString();


                if(gst_number1.matches("")){
                    Toasty.error(PtecMainActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    gst_number.setError("Please Trading name!");
                }else if(full_name1.matches("")){
                    Toasty.error(PtecMainActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    full_name.setError("Please Enter Full name!");
                }  else if(email1.matches("")){
                    Toasty.error(PtecMainActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    email.setError("Enter Email");
                } else if(bank_ifsc_code1.matches("")){
                    Toasty.error(PtecMainActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    bank_ifsc_code.setError("Enter Bank IFSC Code");
                }else if(account_number1.matches("")){
                    Toasty.error(PtecMainActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    account_number.setError("Enter Bank Account Number");
                }else if(birth_date1.matches("")){
                    Toasty.error(PtecMainActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    birth_date.setError("Select Birth Date");
                }else if(pan_number1.matches("")){
                    Toasty.error(PtecMainActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    pan_number.setError("Enter Pan Number");
                } else if(businessnatureStringName.matches("")){

                    Toasty.error(PtecMainActivity.this, "Select Business Nature!", Toasty.LENGTH_LONG).show();
                } else if(mobile_no1.length() != 10){
                    Toasty.error(PtecMainActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    mobile_no.setError("Mobile Number must be 10 digit");
                }else if(pan_number1.length() != 10){
                    Toasty.error(PtecMainActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    pan_number.setError("Enter valied Pan Number");
                }else if(adhar_number1.length() != 12){
                    Toasty.error(PtecMainActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    adhar_number.setError("Enter valied Aadhar Number");
                }else if(adhar_number1.matches("")){
                    Toasty.error(PtecMainActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    adhar_number.setError("Enter Aadhar Number");
                }else if(address1.matches("")){
                    Toasty.error(PtecMainActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    address.setError("Enter full Address");
                }else {
                    // get your editext value here

                    Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

                    Matcher matcher = pattern.matcher(pan_number1);
// Check if pattern matches
                    if (matcher.matches()) {
                        doSubmit(gst_number1, email1, pan_number1, mobile_no1, adhar_number1,address1,full_name1,bank_ifsc_code1,account_number1,birth_date1);
                    }
                    else
                    {
                        Toasty.error(PtecMainActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                        pan_number.setError("Enter valied Pan Number");
                    }

                }
            }
        });

    }



    //Get Business Nature List ro Spinner
    private void GetBusinessNatureList_BR() {
        progress_BnatureSpinner_ptec.setVisibility(View.VISIBLE);

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
                                    progress_BnatureSpinner_ptec.setVisibility(View.GONE);//.setVisibility(View.GONE);
                                    final ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(PtecMainActivity.this, android.R.layout.simple_spinner_item,  businessnatureName); //selected item will look like a spinner set from XML
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
                                mail.setVisibility(View.VISIBLE);
                                progress_ptecmain.setVisibility(View.GONE);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(RegistrationActivity.this, "Submit Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("checkss", " response = " + " " + e.toString());
                            progress_BnatureSpinner_ptec.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(RegistrationActivity.this, "Submit Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("checkss", " response = " + " " + error.toString());
                        progress_BnatureSpinner_ptec.setVisibility(View.GONE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(PtecMainActivity.this);
        requestQueue.add(stringRequest);
    }



    //Get Business Nature List ro Spinner
    private void GetGSTYPEList_ptec() {
        progress_ptecTYPESpinner_ptec.setVisibility(View.VISIBLE);

        GSTTYPEdata1.clear();
        GSTTYPEName.clear();
        GSTTYPEIdLst.clear();
        GSTTYPESpinner.setAdapter(null);
        GSTTYPEStringName = null;

        // Log.d("checkss", " response = " + " " + get_contry_list_url);

        final String statement = "Select";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_ptecTYPE_list_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("checkss", " response = " + " " + response);
                        try {

                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;
                            for (int i = 0; i < ja.length(); i++) {
                                jo = ja.getJSONObject(i);
                                GSTTYPEdata1.add(jo.getString("type_name") );
                                GSTTYPEIdLst.add(jo.getString("id"));
                            }

                            try {
                                for (int i = 0; i <  GSTTYPEdata1.size(); i++) {
                                    String temp;
                                    temp =  GSTTYPEdata1.get(i);
                                   // temp = temp.replaceAll("[0-9]", "");
                                    GSTTYPEName.add(temp);
                                }

                                if ( GSTTYPEName.isEmpty()) {
                                    Toasty.warning(getApplicationContext(), "Business Nature is empty", Toast.LENGTH_SHORT).show();
                                } else {
                                    progress_ptecTYPESpinner_ptec.setVisibility(View.GONE);//.setVisibility(View.GONE);
                                    final ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(PtecMainActivity.this, android.R.layout.simple_spinner_item,  GSTTYPEName); //selected item will look like a spinner set from XML
                                    spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    GSTTYPESpinner.setAdapter(spinnerArrayAdapter1);

                                    GSTTYPESpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                            position2 = position;
                                            try {
                                                String s =  GSTTYPEIdLst.get(position2);
                                                s = s.replaceAll("[^\\d]", "");
                                                GSTTYPE_id = Integer.decode(s);

                                            } catch (Exception e) {}

                                            GetGSTTYPEPLAN_CHART(GSTTYPE_id);
                                            GSTTYPEStringName =  GSTTYPESpinner.getSelectedItem().toString().trim();

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                }

                                verify1 = true;
                                mail.setVisibility(View.VISIBLE);
                                progress_ptecmain.setVisibility(View.GONE);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(RegistrationActivity.this, "Submit Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("checkss", " response = " + " " + e.toString());
                            progress_ptecTYPESpinner_ptec.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(RegistrationActivity.this, "Submit Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("checkss", " response = " + " " + error.toString());
                        progress_ptecTYPESpinner_ptec.setVisibility(View.GONE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(PtecMainActivity.this);
        requestQueue.add(stringRequest);
    }


    //get al registered info
    private void GetGSTTYPEPLAN_CHART(int typeid) {

        final String statement = "Select";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_ptecTYPE_list_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //   Toast.makeText(getApplicationContext(), "Response = "+" "+response, Toast.LENGTH_LONG).show();

                        String res = null;

                        try {
                            //JSONArray ja = new JSONArray(result);
                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;

                            //data = new String[ja.length()];

                            for (int i = 0; i < ja.length(); i++) {
                                jo = ja.getJSONObject(i);
                                //tire_compnay_id.add(jo.getString("tire_id"));
                                //tire_company_name.add(jo.getString("tire_compnay"));
                                //categoryid.add(jo.getString("cid"));

                                //res = jo.getString("Message");
                                charges=jo.getString("gst_rate");
                                GSTTYPEPLAN_AMT.setText("Rs.: "+jo.getString("gst_rate")+".00/-");
                                GSTTYPEPLAN_DESC.setText("Facility :"+jo.getString("type_desc"));

                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(assign_tire_tag.this, "Submit Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                            //   progressMainLay.setVisibility(View.GONE);
                            //scrollViewMainLayout.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(assign_tire_tag.this, "Submit Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                        //  progressMainLay.setVisibility(View.GONE);
                        // scrollViewMainLayout.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("condition", "GET");
                params.put("id", ""+typeid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PtecMainActivity.this);
        requestQueue.add(stringRequest);
    }



    //Form Submit
    private void   doSubmit(String gst_number,String  email,String  pan_number,String  mobile_no,String  adhar_number,String address,String full_name,String bank_ifsc_code,String account_number,String birth_date)
    {
        // progresssubmit.setVisibility(View.VISIBLE);
        submit.setEnabled(false);
        submit.setAlpha(0.5f);
        final String full_name1 = String.valueOf(full_name);

        final String gst_number1 = String.valueOf(gst_number);
        final String email1 = String.valueOf(email);
        final String pan_number1 = String.valueOf(pan_number);
        final String mobile_no1 = String.valueOf(mobile_no);
        final String adhar_number1 = String.valueOf(adhar_number);
        final String address1 = String.valueOf(address);
        final String bank_ifsc_code1 = String.valueOf(bank_ifsc_code);
        final String account_number1 = String.valueOf(account_number);
        final String birth_date1 = String.valueOf(birth_date);


        final String businessnatureid = String.valueOf(businessnature_id);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_MAINSUBMIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("demo",""+response);
                        //Toast.makeText(SignUpActivity.this, "Response : " + " " + response, Toast.LENGTH_LONG).show();

                        try{
                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;

                            String ptec_no = null;
                            String amt = null;
                            String email = null;
                            String number = null;
                            String surl = null;
                            String message = null;
                            for(int i = 0; i<ja.length(); i++){
                                jo = ja.getJSONObject(i);
                                message = jo.getString("Message");
                                if(message.equals("1")) {
                                    Toasty.success(PtecMainActivity.this, "Data Saved.", Toast.LENGTH_SHORT, true).show();
                                    ptec_no = jo.getString("ptec_no");
                                    amt = jo.getString("payment_rs");
                                    email = jo.getString("EMAIL_ID");
                                    number = jo.getString("CUSTOMER_NUMBER");

                                }
                            }

                            //Toast.makeText(SignUpActivity.this, "Message : " + " " + custId, Toast.LENGTH_SHORT).show();

                            if(message.equals("1")){

                                if(ptec_no != null){
                                    Intent intent = new Intent(PtecMainActivity.this, UploadPtecDocActivity.class);
                                    intent.putExtra("ptec_no", ptec_no);
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


                                new KAlertDialog(PtecMainActivity.this, KAlertDialog.ERROR_TYPE)
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

                            new KAlertDialog(PtecMainActivity.this, KAlertDialog.ERROR_TYPE)
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

                        new KAlertDialog(PtecMainActivity.this, KAlertDialog.ERROR_TYPE)
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
                params.put("gst_number",gst_number1);
                params.put("full_name",full_name1);
                params.put("pan_no",pan_number1);
                params.put("bank_ifsc",bank_ifsc_code1);
                params.put("bank_account_np",account_number1);
                params.put("establish_date",birth_date1);
                params.put("aadhar_no",adhar_number1);
                params.put("full_address",address1);
                params.put("email",email1);
                params.put("mobile_no",mobile_no1);
                params.put("status","pending");


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PtecMainActivity.this);
        requestQueue.add(stringRequest);
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
