package com.ultronyc.rvtaxsolutions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;
import static com.ultronyc.rvtaxsolutions.MyApplicationActivity.*;

public class MyApplicationActivity extends AppCompatActivity  {

    List<CategoryMyApplicationList> lstCategory = new ArrayList();
    RecyclerviewAdapterMyApplicationList myAdapter;
    RecyclerView recyclerView;
    EditText search;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String URL_MAINSUBMIT = "http://rvtaxs.com/android/application_history_master.php";
    String loginId,profileName;

    private final int READ_STORAGE_PER_CODE = 21;
    private final int WRITE_STORAGE_PER_CODE = 22;

    private Button checkReadPermissionBtn, checkWritePermissionBtn, downloadFilesBtn;

    private DownloadManager downloadManager;
    private ArrayList<Long> downloadReqList = new ArrayList<>();


    BroadcastReceiver onComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_application);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = PreferenceManager.getDefaultSharedPreferences(MyApplicationActivity.this);

        final ActionBar abar = getSupportActionBar();
        //abar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("My Applications");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setIcon(R.color.transparent);
        abar.setHomeButtonEnabled(true);

        downloadManager = (DownloadManager) this.getSystemService(DOWNLOAD_SERVICE);

        loginId = pref.getString("LOGIN_ID", null);
        profileName = pref.getString("PROFILE_NAME", null);


        search = (EditText) findViewById(R.id.searchEdt_userList_myapplication);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_userList_myapplication);
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

        //BroadcastReceiver onComplete = new BroadcastReceiver() {
        onComplete = new BroadcastReceiver() {

            public void onReceive(Context ctxt, Intent intent) {

                // get the refid from the download manager
                long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                // remove it from our list
                downloadReqList.remove(referenceId);

                // if list is empty means all downloads completed
                if (downloadReqList.isEmpty()) {

                    // show a notification
                    Log.e("INSIDE", "" + referenceId);

                    new KAlertDialog(MyApplicationActivity.this, KAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success!")
                            .setContentText("Files Have Been Downloaded.")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog kAlertDialog) {
                                    kAlertDialog.dismiss();
                                }
                            })
                            .confirmButtonColor(R.drawable.kalert_button_background)
                            .show();


                    //NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    //notificationManager.notify(455, mBuilder.build());
                }
            }
        };

        registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void filter(String text) {
        ArrayList<CategoryMyApplicationList> temp = new ArrayList<>();

        for (CategoryMyApplicationList d : lstCategory) {
            if (d.getName().toLowerCase().contains(text) || d.getPaymentid().toLowerCase().contains(text) || d.getProcessid().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        if(myAdapter==null)
        {}
        else {
            myAdapter.updateLista(temp);
        }
    }

    //Form GET DATA
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
                                String replay_msg=jo.getString("replay_msg");
                                String replay_doc=jo.getString("replay_doc");
                                String replay_doc_type=jo.getString("replay_doc_type");



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
                                else if(cata.equals("udyog_aadhar_regitrastion_master"))
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
                                    cata="Only TDS Refund";
                                }
                                lstCategory.add(new CategoryMyApplicationList(""+cata, ""+date, ""+amt+"/-", ""+payment_id,""+transactionid,""+process_id,""+status,""+replay_msg,""+replay_doc,""+replay_doc_type));


                            }


                            RecyclerviewAdapterMyApplicationList myAdapter = new RecyclerviewAdapterMyApplicationList(MyApplicationActivity.this, lstCategory);
                            recyclerView.setLayoutManager(new GridLayoutManager(MyApplicationActivity.this, 1));
                            recyclerView.setAdapter(myAdapter);



                        } catch (JSONException e){
                            e.printStackTrace();

                            //Toast.makeText(SignUpActivity.this, "JSON Exception : " + " " + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("EXCEPTION", "JSON Exception : " + " " + e.toString());

                            new KAlertDialog(MyApplicationActivity.this, KAlertDialog.ERROR_TYPE)
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

                        new KAlertDialog(MyApplicationActivity.this, KAlertDialog.ERROR_TYPE)
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
        RequestQueue requestQueue = Volley.newRequestQueue(MyApplicationActivity.this);
        requestQueue.add(stringRequest);
    }





    private boolean checkPermissionStatus(int perCode) {

        int status = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (status == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestForPermission(int perCode) {

        if (perCode == READ_STORAGE_PER_CODE) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, perCode);

        } else if (perCode == WRITE_STORAGE_PER_CODE) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, perCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Toast.makeText(this, "Request Code =" + " " + requestCode, LENGTH_SHORT).show();

        if (requestCode == READ_STORAGE_PER_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                Toast.makeText(this, "Read Storage Permission Granted", LENGTH_SHORT).show();

            } else {
                // Permission Denied
                Toast.makeText(this, "Read Storage Permission Denied", LENGTH_SHORT).show();
            }


        } else if (requestCode == WRITE_STORAGE_PER_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted

                Toast.makeText(this, "Write Storage Permission Granted", LENGTH_SHORT).show();

            } else {
                // Permission Denied

                Toast.makeText(this, "Write Storage Permission Denied", LENGTH_SHORT).show();
            }

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(onComplete);
    }


public void niru(){

        Toast.makeText(this, "hii",LENGTH_SHORT).show();
}

    public void downloadFiles(String reqId,String URLPATH,String FILETYPE) {

        Log.d("aaaaaaaa",""+URLPATH);

        if (checkPermissionStatus(READ_STORAGE_PER_CODE)) {

            if (checkPermissionStatus(WRITE_STORAGE_PER_CODE)) {

                Log.d("aaaaaaaa",""+URLPATH);

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(URLPATH));
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setAllowedOverRoaming(false);
                request.setDescription("Downloading Profile");
                request.setTitle("Abc App");
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+reqId+"."+FILETYPE+"");
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                final long downloadId = manager.enqueue(request);


            } else {
                requestForPermission(WRITE_STORAGE_PER_CODE);
            }

        } else {
            requestForPermission(READ_STORAGE_PER_CODE);
        }





    }


}
