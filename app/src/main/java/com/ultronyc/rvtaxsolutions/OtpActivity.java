package com.ultronyc.rvtaxsolutions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.developer.kalert.KAlertDialog;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.ultronyc.rvtaxsolutions.FOROTP.OtpReceivedInterface;
import com.ultronyc.rvtaxsolutions.FOROTP.SmsBroadcastReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtpActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        OtpReceivedInterface, GoogleApiClient.OnConnectionFailedListener {


    private ImageView backImg;
    private EditText edt1Otp, edt2Otp, edt3Otp, edt4Otp;


    private static String URL_LOGIN = "http://rvtaxs.com/android/registration_confirm_master.php";
    private static String URL_RESEND = "http://rvtaxs.com/android/resend_otp_master.php";

    private Button submit;
    private ProgressBar progressBar;
    private String otpStr = null, id = null, mobileStr = null;
    private TextView mobNoTv, timerTv, resendButton;

    private SmsBroadcastReceiver smsBroadcastReceiver;
    GoogleApiClient googleApiClient;
    private String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);


        Intent intent = getIntent();
        id = intent.getStringExtra("ID");
        otpStr = intent.getStringExtra("OTP");
        mobileStr = intent.getStringExtra("MOBILE");

        backImg = (ImageView) findViewById(R.id.backImage);

        edt1Otp = (EditText) findViewById(R.id.otp_edt1);
        edt2Otp = (EditText) findViewById(R.id.otp_edt2);
        edt3Otp = (EditText) findViewById(R.id.otp_edt3);
        edt4Otp = (EditText) findViewById(R.id.otp_edt4);

        this.edt1Otp.addTextChangedListener(new GenericTextWatcher(edt1Otp));
        this.edt2Otp.addTextChangedListener(new GenericTextWatcher(edt2Otp));
        this.edt3Otp.addTextChangedListener(new GenericTextWatcher(edt3Otp));
        this.edt4Otp.addTextChangedListener(new GenericTextWatcher(edt4Otp));

        submit = (Button) findViewById(R.id.submitOtpBtn);
        progressBar = (ProgressBar) findViewById(R.id.submitProgress);
        mobNoTv = (TextView) findViewById(R.id.mobNoTv);
        timerTv = (TextView) findViewById(R.id.timerTv);
        resendButton = (TextView) findViewById(R.id.resendTv);

        mobNoTv.setText("Verification code sent to  +91" + " " + mobileStr);

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               resendotp();
            }
        });




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstDigit = edt1Otp.getText().toString();
                String secondDigit = edt2Otp.getText().toString();
                String thirdDigit = edt3Otp.getText().toString();
                String fourthDigit = edt4Otp.getText().toString();

                if(firstDigit.matches("")){
                    edt1Otp.setError("Enter OTP digit here!");
                } else if(secondDigit.matches("")){
                    edt2Otp.setError("Enter OTP digit here!");
                } else if(thirdDigit.matches("")){
                    edt3Otp.setError("Enter OTP digit here!");
                } else if(fourthDigit.matches("")){
                    edt4Otp.setError("Enter OTP digit here!");
                } else if(!(firstDigit + secondDigit + thirdDigit + fourthDigit).matches(otpStr)){

                    AlertDialog.Builder builder = new AlertDialog.Builder(OtpActivity.this);
                    builder.setMessage("OTP is Wrong.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    //Toast.makeText(ItemMaster.this, "Submit Error! " + e.toString(), Toast.LENGTH_SHORT).show();


                } else {
                    String otp = firstDigit + secondDigit + thirdDigit + fourthDigit;
                    submitOTP(otp);
                }

            }
        });


        smsBroadcastReceiver = new SmsBroadcastReceiver();

        smsBroadcastReceiver.setOnOtpListeners(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);

//        googleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.CREDENTIALS_API)
//                .build();


        startSMSListener();

        startTimer(300000);
        resendButton.setVisibility(View.GONE);


    }



    private void submitOTP(String otp){

        progressBar.setVisibility(View.VISIBLE);
        submit.setEnabled(false);
        submit.setAlpha(0.5f);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("EXCEPTION", "Response : " + " " + response);
                        //Toast.makeText(OTP_Activity.this, "Response : " + " " + response, Toast.LENGTH_SHORT).show();

                        try{
                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;

                            String message = null;

                            /*JSONObject jsonObject = new JSONObject(response);
                            custId = jsonObject.getString("Message");*/

                            for(int i = 0; i<ja.length(); i++){
                                jo = ja.getJSONObject(i);

                                message = jo.getString("Message");

                            }

                            if(message.equals("1")){


                                new KAlertDialog(OtpActivity.this, KAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success")
                                        .setContentText("Registration successful.")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog kAlertDialog) {
                                                kAlertDialog.dismiss();
                                                //onBackPressed();
                                                Intent intent = new Intent(OtpActivity.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .confirmButtonColor(R.drawable.kalert_button_background)
                                        .show();

                            } else {

                                new KAlertDialog(OtpActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error!")
                                        .setContentText("Please try again!")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog kAlertDialog) {
                                                kAlertDialog.dismiss();
                                            }
                                        })
                                        .confirmButtonColor(R.drawable.kalert_button_background)
                                        .show();
                            }

                            progressBar.setVisibility(View.GONE);
                            submit.setEnabled(true);
                            submit.setAlpha(1);

                        } catch (JSONException e){
                            e.printStackTrace();

                            //Toast.makeText(OTP_Activity.this, "JSON Exception : " + " " + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("EXCEPTION", "JSON Exception : " + " " + e.toString());

                            new KAlertDialog(OtpActivity.this, KAlertDialog.ERROR_TYPE)
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

                            progressBar.setVisibility(View.GONE);
                            submit.setEnabled(true);
                            submit.setAlpha(1);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //Toast.makeText(OTP_Activity.this, "Volley Error : " + " " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("EXCEPTION", "Volley Error : " + " " + error.toString());

                        new KAlertDialog(OtpActivity.this, KAlertDialog.ERROR_TYPE)
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

                        progressBar.setVisibility(View.GONE);
                        submit.setEnabled(true);
                        submit.setAlpha(1);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("condition","IN");
                params.put("registration_id",id);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(OtpActivity.this);
        requestQueue.add(stringRequest);
    }




    private void resendotp() {

        progressBar.setVisibility(View.VISIBLE);
        submit.setEnabled(false);
        submit.setAlpha(0.5f);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RESEND,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                       // Log.d("EXCEPTION", "Response : " + " " + response);
                        //Toast.makeText(OTP_Activity.this, "Response : " + " " + response, Toast.LENGTH_SHORT).show();

                        try{
                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;

                            String message = null;

                            /*JSONObject jsonObject = new JSONObject(response);
                            custId = jsonObject.getString("Message");*/

                            for(int i = 0; i<ja.length(); i++){
                                jo = ja.getJSONObject(i);

                                message = jo.getString("Message");

                            }

                            if(message.equals("1")){




                            } else {

                                new KAlertDialog(OtpActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error!")
                                        .setContentText("Please try again!")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog kAlertDialog) {
                                                kAlertDialog.dismiss();
                                            }
                                        })
                                        .confirmButtonColor(R.drawable.kalert_button_background)
                                        .show();
                            }

                            progressBar.setVisibility(View.GONE);
                            submit.setEnabled(true);
                            submit.setAlpha(1);

                        } catch (JSONException e){
                            e.printStackTrace();

                            //Toast.makeText(OTP_Activity.this, "JSON Exception : " + " " + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("EXCEPTION", "JSON Exception : " + " " + e.toString());

                            new KAlertDialog(OtpActivity.this, KAlertDialog.ERROR_TYPE)
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

                            progressBar.setVisibility(View.GONE);
                            submit.setEnabled(true);
                            submit.setAlpha(1);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //Toast.makeText(OTP_Activity.this, "Volley Error : " + " " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("EXCEPTION", "Volley Error : " + " " + error.toString());

                        new KAlertDialog(OtpActivity.this, KAlertDialog.ERROR_TYPE)
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

                        progressBar.setVisibility(View.GONE);
                        submit.setEnabled(true);
                        submit.setAlpha(1);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("condition","IN");
                params.put("mobile_no",mobileStr);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(OtpActivity.this);
        requestQueue.add(stringRequest);
    }








    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onOtpReceived(String otp) {

        //Toast.makeText(this, "OTP : " + " " + otp, Toast.LENGTH_SHORT).show();
        Log.d("OTP_STR", "OTP : " + " " + otp);

        Pattern pattern = Pattern.compile("(\\d{4})");

        //   \d is for a digit
        //   {} is the number of digits here 4.

        Matcher matcher = pattern.matcher(otp);
        String val = "";
        if (matcher.find()) {
            val = matcher.group(0);  // 4 digit number
        }

        if(val.length() == 4) {
            int p1 = Character.digit(val.charAt(0), 10);
            int p2 = Character.digit(val.charAt(1), 10);
            int p3 = Character.digit(val.charAt(2), 10);
            int p4 = Character.digit(val.charAt(3), 10);

            edt1Otp.setText(String.valueOf(p1));
            edt2Otp.setText(String.valueOf(p2));
            edt3Otp.setText(String.valueOf(p3));
            edt4Otp.setText(String.valueOf(p4));
        }

        String e1 = edt1Otp.getText().toString();
        String e2 = edt2Otp.getText().toString();
        String e3 = edt3Otp.getText().toString();
        String e4 = edt4Otp.getText().toString();

        if(e1.matches("")){

        } else if(e2.matches("")){

        } else if(e3.matches("")){

        } else if(e4.matches("")){

        } else {
            String otpNum = e1 + e2 + e3 + e4;

            if(otpStr.matches(otpNum)){
                submitOTP(otpNum);
            }
        }


    }

    @Override
    public void onOtpTimeout() {

    }


    public void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(this);
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override public void onSuccess(Void aVoid) {

            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception e) {
                //Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startTimer(long noOfMillies) {
        CountDownTimer mCountDownTimer;
        mCountDownTimer = new CountDownTimer(noOfMillies, 1000) {  //300000
            public void onTick(long millisUntilFinished) {
                String day = TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)) + "";
                if (day.equalsIgnoreCase("0")) {
                    day = "";
                } else if (day.equalsIgnoreCase("1")) {
                    day = day + "day ";
                } else {
                    day = day + "days ";
                }
                String hr = (TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished))) + "";
                String min = (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished))) + "";
                String ss = (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))) + "";
                timerTv.setText("" + min + ":" + ss);
            }

            public void onFinish() {
                timerTv.setText("Times up!");
                resendButton.setVisibility(View.VISIBLE);

            }
        }.start();

    }

    public class GenericTextWatcher implements TextWatcher
    {
        private View view;
        private GenericTextWatcher(View view)
        {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {

            String text = editable.toString();
            switch(view.getId())
            {

                case R.id.otp_edt1:
                    if(text.length()==1)
                        edt2Otp.requestFocus();
                    break;
                case R.id.otp_edt2:
                    if(text.length()==1)
                        edt3Otp.requestFocus();
                    else if(text.length()==0)
                        edt1Otp.requestFocus();
                    break;
                case R.id.otp_edt3:
                    if(text.length()==1)
                        edt4Otp.requestFocus();
                    else if(text.length()==0)
                        edt2Otp.requestFocus();
                    break;
                case R.id.otp_edt4:
                    if(text.length()==0)
                        edt3Otp.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }
    }




}
