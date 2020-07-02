package com.ultronyc.rvtaxsolutions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.ultronyc.rvtaxsolutions.Udyog_adhar_registration.UploadUhyodAdharRegistrationDocActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class PaymentStatusActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar progressBar;
    String tran_id,status,payment_id;
    private Button printBtn, backBtn, realUpdateBtn;
    private boolean isUpdateToggleEnabled = false;
    private LinearLayout updateToggleContainer;


    private String IP_Address = "", registrationId = "";
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private RelativeLayout dataLoadFailureLayout;
    private Button retryLoadDataBtn;
    private TextView errorTv;

    private ConstraintLayout loadingLayout;
    private ProgressBar progressBarWp10;

    private ArrayList<String> countList = new ArrayList<>();
    private ArrayList<String> idList = new ArrayList<>();

    private String selectedKramankId = "";

    private static String GETLIST_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = PreferenceManager.getDefaultSharedPreferences(PaymentStatusActivity.this);

        final ActionBar abar = getSupportActionBar();
        //abar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Receipt Print");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setIcon(R.color.transparent);
        abar.setHomeButtonEnabled(true);

        Intent intent = getIntent();
        GETLIST_URL = intent.getStringExtra("MODULE_ID");

        tran_id = intent.getStringExtra("tran_id");
        payment_id = intent.getStringExtra("payment_id");
        status = intent.getStringExtra("status");


        pref = PaymentStatusActivity.this.getSharedPreferences("LOGIN_SESSION_DATA", MODE_PRIVATE);
        IP_Address = pref.getString("IP_ADDRESS", "");
        registrationId = pref.getString("REGISTRATION_ID", "");

        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        printBtn = (Button) findViewById(R.id.printBtn);
        backBtn = (Button) findViewById(R.id.backBtn);


        progressBar.setVisibility(View.VISIBLE);

        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.reload();

        webView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view,String url)
            {
                progressBar.setVisibility(View.GONE);

            }

        });


       // Log.d("demoooo",""+status);

        if(status.equals("success"))
        {
            webView.loadUrl("http://rvtaxs.com/android/sucess_payment_print.php?paymentid="+payment_id+"&udf1="+tran_id);
        }
        else
        {
            webView.loadUrl("http://rvtaxs.com/android/fail_payment_print.php?paymentid="+payment_id+"&udf1="+tran_id);
        }



        printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createWebPrintJob(webView);

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PaymentStatusActivity.this, MyPaymentStatusActivity.class);
                startActivity(intent);

            }
        });






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


    private void createWebPrintJob(WebView webView) {

        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String capturedImageName = date + " " + currentTime + " " + " " + System.currentTimeMillis();

        PrintDocumentAdapter printAdapter =
                webView.createPrintDocumentAdapter("Report Print [ Timestamp :" + " " + capturedImageName + " " + "]");


        String jobName = getString(R.string.app_name) + " Print Report";

        if (printManager != null) {
            printManager.print(jobName, printAdapter,
                    new PrintAttributes.Builder().build());
        }

//        if (printManager != null) {
//            printManager.print(jobName, printAdapter,
//                    new PrintAttributes.Builder().setMediaSize(PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE).build());
//        }
    }




}































