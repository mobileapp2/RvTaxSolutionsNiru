package com.ultronyc.rvtaxsolutions;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.developer.kalert.KAlertDialog;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class RegistrationActivity extends AppCompatActivity {


    SearchableSpinner contrySpinner, stateSpinner, citySpinner, pincodeSpinner;
    ProgressBar progresscontry,progressstate,progresscity,progresssubmit,progressmain;
    Button submit;
    EditText fname,lname, email, mobileno, pincode, password,cpassword;
    ScrollView mail;

    String contryStringName,stateStringName,cityStringName;
    boolean verify = false,verify1 = false,verify2 = false;
    int position1,position2,position3;
    int contry_id,state_id,city_id;
    int mobile_ok=0,email_ok=0;
    ArrayList<String> registered_mobile_no;
    ArrayList<String> registered_email_id;


    List<String> contrydata1 = new ArrayList<String>();
    List<String> contryName = new ArrayList<String>();
    List<String> contryIdLst = new ArrayList<String>();

    List<String> statedata1 = new ArrayList<String>();
    List<String> stateName = new ArrayList<String>();
    List<String> stateIdLst = new ArrayList<String>();


    List<String> citydata1 = new ArrayList<String>();
    List<String> cityName = new ArrayList<String>();
    List<String> cityIdLst = new ArrayList<String>();


    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String custRegistId = null;
    boolean OTPState = false;

    String get_contry_list_url = "http://rvtaxs.com/android/contry_master.php";
    String get_state_list_url = "http://rvtaxs.com/android/state_master.php";
    String get_city_list_url = "http://rvtaxs.com/android/city_master.php";
    String URL_REGISTRATION = "http://rvtaxs.com/android/registration_master.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

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
        textviewTitle.setText("Registration");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setIcon(R.color.transparent);
        abar.setHomeButtonEnabled(true);


        mail=(ScrollView)findViewById(R.id.scrollView);
        progressmain = (ProgressBar) findViewById(R.id.progress_main);
        mail.setVisibility(View.GONE);
        progressmain.setVisibility(View.VISIBLE);

        contrySpinner = (SearchableSpinner) findViewById(R.id.contryspinner_regitsration);
        progresscontry = (ProgressBar) findViewById(R.id.progress_contrySpinnerRegistration);

        stateSpinner = (SearchableSpinner) findViewById(R.id.statespinner_regitsration);
        progressstate = (ProgressBar) findViewById(R.id.progress_stateSpinnerRegistration);

        citySpinner = (SearchableSpinner) findViewById(R.id.cityspinner_regitsration);
        progresscity = (ProgressBar) findViewById(R.id.progress_citySpinnerRegistration);

        fname = (EditText) findViewById(R.id.firstNameEditText);
        lname = (EditText) findViewById(R.id.lastNameEditText);
        email = (EditText) findViewById(R.id.emailEditText);
        mobileno = (EditText) findViewById(R.id.mobileEditText);
        pincode = (EditText) findViewById(R.id.pincodeEditText);

        password = (EditText) findViewById(R.id.passwordEditText);
        cpassword = (EditText) findViewById(R.id.confirmPasswordEditText);
        submit = (Button) findViewById(R.id.submit_registration);
        progresssubmit = (ProgressBar) findViewById(R.id.progress_submit);

        registered_mobile_no=new ArrayList<>();
        registered_email_id=new ArrayList<>();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(SignUpActivity.this, OTP_Activity.class));

                String name = fname.getText().toString()+" "+lname.getText().toString();
                String email1 = email.getText().toString();
                String mobileNo = mobileno.getText().toString();
                String password1 = password.getText().toString();
                String cpassword1 = cpassword.getText().toString();
                String pincode1 = pincode.getText().toString();

                if(name == null){
                    Toasty.error(RegistrationActivity.this, "Please Enter Name", Toasty.LENGTH_LONG).show();
                } else if(name.matches("")){
                    fname.setError("Please Enter name!");
                } else if(email1.matches("")){
                    email.setError("Enter Email");
                } else if(mobileNo.matches("")){
                    mobileno.setError("Enter Mobile Number");
                } else if(contryStringName.matches("")){
                    Toasty.error(RegistrationActivity.this, "Country Name can not be empty!", Toasty.LENGTH_LONG).show();
                }else if(stateStringName.matches("")){
                    Toasty.error(RegistrationActivity.this, "State Name can not be empty!", Toasty.LENGTH_LONG).show();
                } else if(cityStringName.matches("")){
                    Toasty.error(RegistrationActivity.this, "City Name can not be empty!", Toasty.LENGTH_LONG).show();
                } else if(mobileNo.length() != 10){
                    mobileno.setError("Mobile Number must be 10 digit");
                } else if(password1.matches("")){
                    password.setError("Password can not be empty");
                }else if(cpassword1.matches("")){
                    password.setError("Confirm Password can not be empty");
                }
                else if(mobile_ok==0){
                    mobileno.setError("Mobile Number can not be empty");
                }
                else if(email_ok==0){
                    email.setError("Email ID can not be empty");
                }
                else {
                    if(password1.matches(cpassword1)){
                        doRegitsration(name, email1, mobileNo, password1, cpassword1,pincode1);

                    }
                    else{
                        password.setError("Password Not Match!");
                    }

                }
            }
        });

        mobileno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                   // Toast.makeText(getApplicationContext(), "Got the focus", Toast.LENGTH_LONG).show();
                } else {
                   // Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();
                    String mobileNo = mobileno.getText().toString();
                    if(registered_mobile_no.contains(mobileNo))
                    {
                        mobileno.setError("Mobile Number Existed!");
                        mobileno.setFocusable(true);
                        new KAlertDialog(RegistrationActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Warrning!")
                                .setContentText("Mobile Number Existed, Please try again!")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog kAlertDialog) {
                                        kAlertDialog.dismiss();
                                    }
                                })
                                .confirmButtonColor(R.drawable.kalert_button_background)
                                .show();
                    }
                    else
                    {
                        mobile_ok=1;
                    }
                }
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    // Toast.makeText(getApplicationContext(), "Got the focus", Toast.LENGTH_LONG).show();
                } else {
                    // Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();
                    String mobileNo = email.getText().toString();
                    if(registered_email_id.contains(mobileNo))
                    {
                        email.setError("Email ID Existed!");
                        new KAlertDialog(RegistrationActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Warrning!")
                                .setContentText("Email ID Existed, Please try again!")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog kAlertDialog) {
                                        kAlertDialog.dismiss();
                                    }
                                })
                                .confirmButtonColor(R.drawable.kalert_button_background)
                                .show();
                    }
                    else
                    {
                        email_ok=1;
                    }
                }
            }
        });


        GetContryList();
        getAllRegisteredInfo();
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


    //get al registered info
    private void getAllRegisteredInfo() {

        final String statement = "Select";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTRATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //   Toast.makeText(getApplicationContext(), "Response = "+" "+response, Toast.LENGTH_LONG).show();
                        mail.setVisibility(View.VISIBLE);
                        progressmain.setVisibility(View.GONE);

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
                                registered_mobile_no.add(""+jo.getString("mobile_no"));
                                registered_email_id.add(""+jo.getString("email"));
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
                 return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);
        requestQueue.add(stringRequest);
    }


    /**
     *
     */
    //Get Country List ro Spinner
    private void GetContryList() {
        progresscontry.setVisibility(View.VISIBLE);

        contrydata1.clear();
        contryName.clear();
        contryIdLst.clear();
        contrySpinner.setAdapter(null);
        contryStringName = null;

       // Log.d("checkss", " response = " + " " + get_contry_list_url);

        final String statement = "Select";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_contry_list_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("checkss", " response = " + " " + response);
                        try {

                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;
                            for (int i = 0; i < ja.length(); i++) {
                                jo = ja.getJSONObject(i);
                                contrydata1.add(jo.getString("contry_name") + jo.getString("id"));
                                contryIdLst.add(jo.getString("id"));
                               }

                            try {
                                for (int i = 0; i < contrydata1.size(); i++) {
                                    String temp;
                                    temp = contrydata1.get(i);
                                    temp = temp.replaceAll("[0-9]", "");
                                    contryName.add(temp);
                                }

                                if (contryName.isEmpty()) {
                                    Toasty.warning(getApplicationContext(), "Contry Name is not available", Toast.LENGTH_SHORT).show();
                                } else {
                                    progresscontry.setVisibility(View.GONE);//.setVisibility(View.GONE);
                                    final ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, contryName); //selected item will look like a spinner set from XML
                                    spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    contrySpinner.setAdapter(spinnerArrayAdapter1);

                                    contrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                            position1 = position;
                                            try {
                                                String s = contrydata1.get(position1);
                                                s = s.replaceAll("[^\\d]", "");
                                                contry_id = Integer.decode(s);
                                                GetStateList(contry_id);
                                            } catch (Exception e) {}


                                            contryStringName = contrySpinner.getSelectedItem().toString().trim();

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
                            progresscontry.setVisibility(View.GONE);
                          }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(RegistrationActivity.this, "Submit Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("checkss", " response = " + " " + error.toString());
                        progresscontry.setVisibility(View.GONE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);
        requestQueue.add(stringRequest);
    }


    //Get State List to Spinner
    private void GetStateList(int contry_id) {
        progressstate.setVisibility(View.VISIBLE);

        statedata1.clear();
        stateName.clear();
        stateIdLst.clear();
        stateSpinner.setAdapter(null);
        stateStringName = null;

        //Log.d("checkss", " response = " + " " + get_contry_list_url);

        final String statement = "Select";
        final String contry_id1 = ""+contry_id;

        Log.d("checkss", " response = " + " " + contry_id1);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_state_list_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("checkss", " response = " + " " + response);
                        try {

                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;
                            for (int i = 0; i < ja.length(); i++) {
                                jo = ja.getJSONObject(i);
                                statedata1.add(jo.getString("state_name") + jo.getString("state_id"));
                                stateIdLst.add(jo.getString("state_id"));
                            }

                            try {
                                for (int i = 0; i < statedata1.size(); i++) {
                                    String temp;
                                    temp = statedata1.get(i);
                                    temp = temp.replaceAll("[0-9]", "");
                                    stateName.add(temp);
                                }

                                if (stateName.isEmpty()) {
                                    Toasty.warning(getApplicationContext(), "State Name is not available", Toast.LENGTH_SHORT).show();
                                } else {
                                    progressstate.setVisibility(View.GONE);//.setVisibility(View.GONE);
                                    final ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, stateName); //selected item will look like a spinner set from XML
                                    spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    stateSpinner.setAdapter(spinnerArrayAdapter1);

                                    stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                            position2 = position;
                                            try {
                                                String s = statedata1.get(position2);
                                                s = s.replaceAll("[^\\d]", "");
                                                state_id = Integer.decode(s);
                                                GetCityList(state_id);
                                            } catch (Exception e) {}


                                            stateStringName = stateSpinner.getSelectedItem().toString().trim();

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
                            progressstate.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(RegistrationActivity.this, "Submit Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("checkss", " response = " + " " + error.toString());
                        progressstate.setVisibility(View.GONE);
                        //scrollViewMainLayout.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("condition", "GET");
                params.put("contry_id", contry_id1);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);
        requestQueue.add(stringRequest);
    }

    //Get City List To Spinner
    private void GetCityList(int state_id) {
        progresscity.setVisibility(View.VISIBLE);

        citydata1.clear();
        cityName.clear();
        cityIdLst.clear();
        citySpinner.setAdapter(null);
        cityStringName = null;

        //Log.d("checkss", " response = " + " " + get_contry_list_url);

        final String statement = "Select";
        final String state_id1 = ""+state_id;

   //     Log.d("checkss", " response = " + " " + contry_id1);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_city_list_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("checkss", " response = " + " " + response);
                        try {

                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;
                            for (int i = 0; i < ja.length(); i++) {
                                jo = ja.getJSONObject(i);
                                citydata1.add(jo.getString("city_name") + jo.getString("city_id"));
                                cityIdLst.add(jo.getString("city_id"));
                            }

                            try {
                                for (int i = 0; i < citydata1.size(); i++) {
                                    String temp;
                                    temp = citydata1.get(i);
                                    temp = temp.replaceAll("[0-9]", "");
                                    cityName.add(temp);
                                }

                                if (cityName.isEmpty()) {
                                    Toasty.warning(getApplicationContext(), "City Name is not available", Toast.LENGTH_SHORT).show();
                                } else {
                                    progresscity.setVisibility(View.GONE);//.setVisibility(View.GONE);
                                    final ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, cityName); //selected item will look like a spinner set from XML
                                    spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    citySpinner.setAdapter(spinnerArrayAdapter1);

                                    citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                            position3 = position;
                                            try {
                                                String s = citydata1.get(position3);
                                                s = s.replaceAll("[^\\d]", "");
                                                city_id = Integer.decode(s);
                                                //GetCirtyList(state_id);
                                            } catch (Exception e) {}


                                            cityStringName = citySpinner.getSelectedItem().toString().trim();

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                }

                                verify2 = true;

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(RegistrationActivity.this, "Submit Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("checkss", " response = " + " " + e.toString());
                            progresscity.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(RegistrationActivity.this, "Submit Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("checkss", " response = " + " " + error.toString());
                        progresscity.setVisibility(View.GONE);
                        //scrollViewMainLayout.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("condition", "GET");
                params.put("state_id", state_id1);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);
        requestQueue.add(stringRequest);
    }




    private void doRegitsration(String username, String email, String mobile, String password,String cpassword, String pincode){

        progresssubmit.setVisibility(View.VISIBLE);
        submit.setEnabled(false);
        submit.setAlpha(0.5f);

        final String contryid = String.valueOf(contry_id);
        final String stateid = String.valueOf(state_id);
        final String cityid = String.valueOf(city_id);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTRATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(SignUpActivity.this, "Response : " + " " + response, Toast.LENGTH_LONG).show();
                        try{
                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;
                            String custId = null;
                            String otp = null;
                            String message = null;

                            for(int i = 0; i<ja.length(); i++){
                                jo = ja.getJSONObject(i);

                                message = jo.getString("Message");

                                if(message.equals("1")) {
                                    custId = jo.getString("id");
                                    otp = jo.getString("otp");
                                }

                            }

                            //Toast.makeText(SignUpActivity.this, "Message : " + " " + custId, Toast.LENGTH_SHORT).show();

                            if(message.equals("1")){

                                if(custId != null){
                                    Intent intent = new Intent(RegistrationActivity.this, OtpActivity.class);
                                    intent.putExtra("ID", custId);
                                    intent.putExtra("MOBILE", mobile);
                                    intent.putExtra("OTP", otp);
                                    startActivity(intent);
                                }

                            }

                            else {


                                new KAlertDialog(RegistrationActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error!")
                                        .setContentText("Your Connection is too poor try again!")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog kAlertDialog) {
                                                kAlertDialog.dismiss();
                                            }
                                        })
                                        .confirmButtonColor(R.drawable.kalert_button_background)
                                        .show();
                            }

                            progresssubmit.setVisibility(View.GONE);
                            submit.setEnabled(true);
                            submit.setAlpha(1);

                        } catch (JSONException e){
                            e.printStackTrace();

                            //Toast.makeText(SignUpActivity.this, "JSON Exception : " + " " + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("EXCEPTION", "JSON Exception : " + " " + e.toString());

                            new KAlertDialog(RegistrationActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setContentText("Server error!")
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog kAlertDialog) {
                                            kAlertDialog.dismiss();
                                        }
                                    })
                                    .confirmButtonColor(R.drawable.kalert_button_background)
                                    .show();

                            progresssubmit.setVisibility(View.GONE);
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

                        new KAlertDialog(RegistrationActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!")
                                .setContentText("Server Error, Please try again.")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog kAlertDialog) {
                                        kAlertDialog.dismiss();
                                    }
                                })
                                .confirmButtonColor(R.drawable.kalert_button_background)
                                .show();


                        //Toast.makeText(ItemMaster.this, "Submit Error! " + e.toString(), Toast.LENGTH_SHORT).show();

                        progresssubmit.setVisibility(View.GONE);
                        submit.setEnabled(true);
                        submit.setAlpha(1);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("condition","IN");
                params.put("customer_name",username);
                params.put("customer_email",email);
                params.put("customer_mobile",mobile);
                params.put("country_id",contryid);
                params.put("state_id",stateid);
                params.put("city_id",cityid);
                params.put("pincode",pincode);
                params.put("customer_password",password);


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);
        requestQueue.add(stringRequest);
    }



}
