package com.ultronyc.rvtaxsolutions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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
import com.ultronyc.rvtaxsolutions.Income_tax_notice.IncomeTaxNoticeActivity;
import com.ultronyc.rvtaxsolutions.Income_tax_notice.UploadIncomeTaxNoriceDocActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class MyPaymentStatusActivity extends AppCompatActivity {

    List<CategoryPaymentList> lstCategory = new ArrayList();
    RecyclerviewAdapterPaymentsList myAdapter;
    RecyclerView recyclerView;
    EditText search;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String URL_MAINSUBMIT = "http://rvtaxs.com/android/payment_history_master.php";
    String loginId,profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_payment_status);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = PreferenceManager.getDefaultSharedPreferences(MyPaymentStatusActivity.this);

        final ActionBar abar = getSupportActionBar();
        //abar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Payment History");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setIcon(R.color.transparent);
        abar.setHomeButtonEnabled(true);

        loginId = pref.getString("LOGIN_ID", null);
        profileName = pref.getString("PROFILE_NAME", null);


        search = (EditText) findViewById(R.id.searchEdt_userList);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_userList);
        lstCategory = new ArrayList<>();


        String str = "satara";
        String str2 = "4569875685";

        GetHistory();



        search.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    public void filter(String text) {
        ArrayList<CategoryPaymentList> temp = new ArrayList<>();

        for (CategoryPaymentList d : lstCategory) {
            if (d.getName().toLowerCase().contains(text) || d.getPaymentid().toLowerCase().contains(text) || d.getProcessid().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        if(myAdapter==null)
        {}
        else {
            myAdapter.updateList(temp);
        }
    }

    //Form Submit
    private void   GetHistory()
    {
        // progresssubmit.setVisibility(View.VISIBLE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_MAINSUBMIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("demo",""+response);
                        //Toast.makeText(SignUpActivity.this, "Response : " + " " + response, Toast.LENGTH_LONG).show();

                        try{
                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;

                            for (int i = 0; i < ja.length(); i++) {
                                jo = ja.getJSONObject(i);
                                String cata=jo.getString("category");
                                String transactionid=jo.getString("transaction_id");
                                String payment_id=jo.getString("payment_id");
                                String status=jo.getString("transaction_flag");
                                String process_id=jo.getString("process_id");
                                String date=jo.getString("date");
                                String amt=jo.getString("amt");



                                if(transactionid.equals("") || transactionid==null)
                                {
                                    transactionid="-";
                                }
                                if(payment_id.equals("") || payment_id==null)
                                {
                                    payment_id="-";
                                } if(status.equals("pending"))
                                {
                                    status="Transaction Pending";
                                }
                                else if(status.equals("done"))
                                {
                                    status="Transaction Completed";
                                }
                                else if(status.equals("failed"))
                                {
                                    status="Transaction Fail/Cancel";
                                }

                                if(cata.equals("business_returns_master"))
                                {
                                    cata="Business Returns ITR";
                                }else if(cata.equals("salary_returns_master"))
                                {
                                    cata="Salary Returns ITR";
                                }else if(cata.equals("pension_returns_master"))
                                {
                                    cata="Pension Returns ITR";
                                }else if(cata.equals("house_property_master"))
                                {
                                    cata="House Property ITR";
                                }else if(cata.equals("capital_gain_master"))
                                {
                                    cata="Capital Gain ITR";
                                }else if(cata.equals("income_tax_notice_master"))
                                {
                                    cata="Income Tax Notice";
                                }
                                else if(cata.equals("gst_registration_master"))
                                {
                                    cata="GST Registration";
                                }else if(cata.equals("shopact_registration_master"))
                                {
                                    cata="Shop Act Registration";
                                }
                                else if(cata.equals("udyog_aadhar_registration_master"))
                                {
                                    cata="Udhyog Aadhar Registration";
                                }else if(cata.equals("startup_consultation_master"))
                                {
                                    cata="StartUp Consultation";
                                }else if(cata.equals("project_report_for_loan_master"))
                                {
                                    cata="Project Report For Loan";
                                }else if(cata.equals("gst_returns_master"))
                                {
                                    cata="GST Returns";
                                }else if(cata.equals("ptec_master"))
                                {
                                    cata="PROFESSIONAL TAX CERTIFICATE";
                                }else if(cata.equals("only_tds_refund_master"))
                                {
                                    cata="Only TDS Refund.";
                                }
                                lstCategory.add(new CategoryPaymentList(""+cata, ""+date, ""+amt+"/-", ""+payment_id,""+transactionid,""+process_id,""+status));


                            }


                            RecyclerviewAdapterPaymentsList myAdapter = new RecyclerviewAdapterPaymentsList(getApplicationContext(), lstCategory);
                            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                            recyclerView.setAdapter(myAdapter);



                        } catch (JSONException e){
                            e.printStackTrace();

                            //Toast.makeText(SignUpActivity.this, "JSON Exception : " + " " + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("EXCEPTION", "JSON Exception : " + " " + e.toString());

                            new KAlertDialog(MyPaymentStatusActivity.this, KAlertDialog.ERROR_TYPE)
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

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //Toast.makeText(SignUpActivity.this, "Volley Error : " + " " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("EXCEPTION", "Volley Error : " + " " + error.toString());

                        new KAlertDialog(MyPaymentStatusActivity.this, KAlertDialog.ERROR_TYPE)
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

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("condition","GET");
                params.put("login_regi_no",loginId);



                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MyPaymentStatusActivity.this);
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