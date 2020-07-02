package com.ultronyc.rvtaxsolutions.Project_report_for_loan;

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
import android.widget.RadioButton;
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
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.ultronyc.rvtaxsolutions.R;

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


public class ProjectReportLoanMainActivity extends AppCompatActivity {



    SearchableSpinner businessnatureSpinner, turnoverSpinner, itrpurposeSpinner;
    ProgressBar progress_BnatureSpinner_PROJECTREPORT,progress_businessturenoverSpinner_BR,progress_itrpurposeSpinner_BR,progress_PROJECTREPORTmain;
    Button submit;
    EditText business_name,loan_amount,self_contribution,current_loan;
    TextView birth_date,view_doc_list;
    ScrollView mail;
    private RadioGroup radioGroup;

    String current_loan1="Yes";

    String loginId,profileName;

    private int mYear, mMonth, mDay, type;

    String businessnatureStringName,turnoverStringName,itrpurposeStringName;
    boolean verify = false,verify1 = false,verify2 = false;
    int position1,position2,position3;
    int businessnature_id,turnover_id,itrpurpose_id;


    List<String> businessnaturedata1 = new ArrayList<String>();
    List<String> businessnatureName = new ArrayList<String>();
    List<String> businessnatureIdLst = new ArrayList<String>();



    String get_business_nature_list_url = "http://rvtaxs.com/android/business_nature.php";
    String URL_MAINSUBMIT = "http://rvtaxs.com/android/project_loan_master.php";


    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_report_loan_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = PreferenceManager.getDefaultSharedPreferences(ProjectReportLoanMainActivity.this);

        final ActionBar abar = getSupportActionBar();
        //abar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("PROJECT REPORT FOR LOAN");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setIcon(R.color.transparent);
        abar.setHomeButtonEnabled(true);


        loginId = pref.getString("LOGIN_ID", null);
        profileName = pref.getString("PROFILE_NAME", null);

        mail=(ScrollView)findViewById(R.id.PROJECTREPORT_mainScroll);
        progress_PROJECTREPORTmain = (ProgressBar) findViewById(R.id.progress_PROJECTREPORTmain);
        mail.setVisibility(View.GONE);
        progress_PROJECTREPORTmain.setVisibility(View.VISIBLE);

        businessnatureSpinner = (SearchableSpinner) findViewById(R.id.businessnaturespinner_PROJECTREPORT);
        progress_BnatureSpinner_PROJECTREPORT = (ProgressBar) findViewById(R.id.progress_BnatureSpinner_PROJECTREPORT);
        view_doc_list = (TextView) findViewById(R.id.PROJECTREPORT_view_doc_list);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);


        business_name = (EditText) findViewById(R.id.business_name_PROJECTREPORTregitration);
        self_contribution = (EditText) findViewById(R.id.self_contribution_PROJECTREPORTregitration);
        loan_amount = (EditText) findViewById(R.id.loan_amt_PROJECTREPORTregitration);
        submit = (Button) findViewById(R.id.PROJECTREPORT_submit);

        GetBusinessNatureList_PROJECTLOAN();



        view_doc_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createdDialog(0).show();
                DialogProjectReportLoanDocument dialog = new DialogProjectReportLoanDocument();
                //dialog.setArguments(new Bundle());
                dialog.show(getSupportFragmentManager(), "ITR Business Return Documents");

            }
        });



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch(i) {
                    case R.id.yes:
                            // Pirates are the best
                       // Toast.makeText(ProjectReportLoanMainActivity.this, "Yes", Toast.LENGTH_SHORT).show();
                        current_loan1="Yes";

                        break;
                    case R.id.no:
                            // Ninjas rule
                       // Toast.makeText(ProjectReportLoanMainActivity.this, "Noo", Toast.LENGTH_SHORT).show();
                        current_loan1="No";

                        break;
                }
            }
        });




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(SignUpActivity.this, OTP_Activity.class));

                String business_name1 = business_name.getText().toString();
                String loan_amount1 = loan_amount.getText().toString();
                String self_contribution1 = self_contribution.getText().toString();


                if(business_name1.matches("")){
                    Toasty.error(ProjectReportLoanMainActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    business_name.setError("Please Trading name!");
                }else if(loan_amount1.matches("")){
                    Toasty.error(ProjectReportLoanMainActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    loan_amount.setError("Please Enter Full name!");
                }  else if(self_contribution1.matches("")){
                    Toasty.error(ProjectReportLoanMainActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    self_contribution.setError("Enter Email");
                } else if(businessnatureStringName.matches("")){

                    Toasty.error(ProjectReportLoanMainActivity.this, "Select Business Nature!", Toasty.LENGTH_LONG).show();
                } else {
                        doSubmit(business_name1, loan_amount1, self_contribution1);

                }
            }
        });

    }



    //Get Business Nature List ro Spinner
    private void GetBusinessNatureList_PROJECTLOAN() {
        progress_BnatureSpinner_PROJECTREPORT.setVisibility(View.VISIBLE);

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
                                    progress_BnatureSpinner_PROJECTREPORT.setVisibility(View.GONE);//.setVisibility(View.GONE);
                                    final ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(ProjectReportLoanMainActivity.this, android.R.layout.simple_spinner_item,  businessnatureName); //selected item will look like a spinner set from XML
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
                                progress_PROJECTREPORTmain.setVisibility(View.GONE);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(RegistrationActivity.this, "Submit Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("checkss", " response = " + " " + e.toString());
                            progress_BnatureSpinner_PROJECTREPORT.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(RegistrationActivity.this, "Submit Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("checkss", " response = " + " " + error.toString());
                        progress_BnatureSpinner_PROJECTREPORT.setVisibility(View.GONE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(ProjectReportLoanMainActivity.this);
        requestQueue.add(stringRequest);
    }


    //Form Submit
    private void   doSubmit(String business_name,String  loan_amount,String  self_contribution)
    {
        // progresssubmit.setVisibility(View.VISIBLE);
        submit.setEnabled(false);
        submit.setAlpha(0.5f);
        final String business_name1 = String.valueOf(business_name);

        final String loan_amount1 = String.valueOf(loan_amount);
        final String self_contribution1 = String.valueOf(self_contribution);


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

                            String project_loan_registration_no = null;
                            String amt = null;
                            String email = null;
                            String number = null;
                            String surl = null;
                            String message = null;
                            for(int i = 0; i<ja.length(); i++){
                                jo = ja.getJSONObject(i);
                                message = jo.getString("Message");
                                if(message.equals("1")) {
                                    Toasty.success(ProjectReportLoanMainActivity.this, "Data Saved.", Toast.LENGTH_SHORT, true).show();
                                    project_loan_registration_no = jo.getString("project_loan_registration_no");
                                    amt = jo.getString("payment_rs");
                                    email = jo.getString("EMAIL_ID");
                                    number = jo.getString("CUSTOMER_NUMBER");

                                }
                            }

                            //Toast.makeText(SignUpActivity.this, "Message : " + " " + custId, Toast.LENGTH_SHORT).show();

                            if(message.equals("1")){

                                if(project_loan_registration_no != null){
                                    Intent intent = new Intent(ProjectReportLoanMainActivity.this, UploadProjectReportLoanDocActivity.class);
                                    intent.putExtra("project_loan_registration_no", project_loan_registration_no);
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


                                new KAlertDialog(ProjectReportLoanMainActivity.this, KAlertDialog.ERROR_TYPE)
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

                            new KAlertDialog(ProjectReportLoanMainActivity.this, KAlertDialog.ERROR_TYPE)
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

                        new KAlertDialog(ProjectReportLoanMainActivity.this, KAlertDialog.ERROR_TYPE)
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
                params.put("loan_req_amt",loan_amount1);
                params.put("business_name",business_name1);
                params.put("self_contibution",self_contribution1);
                params.put("loan_amount",loan_amount1);
                params.put("currently_loan", current_loan1);
                params.put("status","pending");


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ProjectReportLoanMainActivity.this);
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
