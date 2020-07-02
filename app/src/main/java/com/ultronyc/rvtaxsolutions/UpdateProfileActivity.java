package com.ultronyc.rvtaxsolutions;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.ultronyc.rvtaxsolutions.Itr_filling.BusinessReturnActivity;
import com.ultronyc.rvtaxsolutions.Itr_filling.CapitalGainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class UpdateProfileActivity extends AppCompatActivity {

    ProgressBar progress_Profilemain;
    Button submit;
    EditText full_name,mobile_number,pincode, email;
    ScrollView mail;
    TextView view_doc_list;

    String URL_MAINSUBMIT = "http://rvtaxs.com/android/update_profile_master.php";

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String loginId,profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = PreferenceManager.getDefaultSharedPreferences(UpdateProfileActivity.this);
        final ActionBar abar = getSupportActionBar();
        //abar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Update Profile");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setIcon(R.color.transparent);
        abar.setHomeButtonEnabled(true);

        loginId = pref.getString("LOGIN_ID", null);
        profileName = pref.getString("PROFILE_NAME", null);

        full_name = (EditText) findViewById(R.id.PROFILE_fullname);
        mobile_number = (EditText) findViewById(R.id.PROFILE_mobileno);

        email = (EditText) findViewById(R.id.PROFILE_email);
        pincode = (EditText) findViewById(R.id.PROFILE_pincode);


        submit = (Button) findViewById(R.id.PROFILE_submit);

        GetProfileData();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(SignUpActivity.this, OTP_Activity.class));

                String full_name1 = full_name.getText().toString();
                String email1 = email.getText().toString();
                String mobile_no1 = mobile_number.getText().toString();
                String pincode1 = pincode.getText().toString();

                if(full_name1.matches("")){
                    full_name.setError("Please Enter Full name!");
                } else if(email1.matches("")){
                    email.setError("Enter Email");
                }  else if(mobile_no1.length() != 10){
                    mobile_number.setError("Mobile Number must be 10 digit");
                }else {
                    doSubmit(full_name1,email1,mobile_no1,pincode1);
                }
            }
        });
    }


    //Get ITR Purpose List ro Spinner
    private void GetProfileData() {

        final String statement = "Select";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_MAINSUBMIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("checkss", " response = " + " " + response);
                        try {

                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;
                            for (int i = 0; i < ja.length(); i++) {
                                jo = ja.getJSONObject(i);

                                full_name.setText(jo.getString("full_name"));
                                mobile_number.setText(jo.getString("mobile_no"));
                                email.setText(jo.getString("email"));
                                pincode.setText(jo.getString("pincode"));

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(RegistrationActivity.this, "Submit Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("checkss", " response = " + " " + e.toString());

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(RegistrationActivity.this, "Submit Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("checkss", " response = " + " " + error.toString());

                        //scrollViewMainLayout.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("condition", "GET");
                params.put("login_id", ""+loginId);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(UpdateProfileActivity.this);
        requestQueue.add(stringRequest);
    }


    //Form Submit
    private void  doSubmit(String full_name,String email,String mobile_no,String pincode)
    {
        // progresssubmit.setVisibility(View.VISIBLE);
        submit.setEnabled(false);
        submit.setAlpha(0.5f);

        final String full_name1 = String.valueOf(full_name);
        final String email1 = String.valueOf(email);
        final String mobile_no1 = String.valueOf(mobile_no);
        final String pincode1 = String.valueOf(pincode);

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

                                    Toasty.success(UpdateProfileActivity.this, "Profile Update Successfully", Toast.LENGTH_SHORT, true).show();

                                }
                            }

                            //Toast.makeText(SignUpActivity.this, "Message : " + " " + custId, Toast.LENGTH_SHORT).show();

                            if(message.equals("1")){


                                    Intent intent = new Intent(UpdateProfileActivity.this, HomeActivity.class);
                                    startActivity(intent);


                            }

                            else {


                                new KAlertDialog(UpdateProfileActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error!")
                                        .setContentText("Your connection is poor try again!")
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

                            new KAlertDialog(UpdateProfileActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setContentText("Server Error!")
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

                        new KAlertDialog(UpdateProfileActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!")
                                .setContentText("Server Error! Please try again.")
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
                params.put("condition","CHANGE");
                params.put("login_id",loginId);
                params.put("full_name",full_name1);
                params.put("mobile_no",mobile_no1);
                params.put("email",email1);
                params.put("pincode",pincode1);
                params.put("status","pending");


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(UpdateProfileActivity.this);
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
