package com.ultronyc.rvtaxsolutions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.ultronyc.rvtaxsolutions.FOROTP.AppSignatureHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    TextView login, signup, forgetPassword, buyOurPlan;
    EditText username, password;
    ProgressBar progresslogin;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private String LOGIN_ID_STR = null, profileName = null;
    String URL_LOGIN = "http://rvtaxs.com/android/login_master.php";


    Typeface typeface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        login = (TextView) findViewById(R.id.loginTextView);
        signup = (TextView) findViewById(R.id.signupTextView);
        forgetPassword = (TextView) findViewById(R.id.forgetPasswordTextView);
        buyOurPlan = (TextView) findViewById(R.id.buyPlanTextView);

        username = (EditText) findViewById(R.id.usernameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        progresslogin = (ProgressBar) findViewById(R.id.progress_login);

        typeface = Typeface.createFromAsset(getAssets(), "font/SourceSansPro-SemiBold.ttf");


        AppSignatureHelper g=new AppSignatureHelper(this);
        g.getAppSignatures();
        //Toast.makeText(this, "lOGIN Id from Main Activity : " + " " + LOGIN_ID_STR, Toast.LENGTH_SHORT).show();
        Log.d("LOGIN_ID_LOG", "lOGIN Id from Main Activity : " + " " + typeface);

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, HomeActivity.class));
              //  startActivity(new Intent(MainActivity.this, paymentMainactivity.class));

                String name = username.getText().toString();
                String password1 = password.getText().toString();

                if(name == null){
                    Toasty.error(MainActivity.this, "Please Enter UserName", Toasty.LENGTH_LONG).show();
                } else if(name.matches("")){
                    username.setError("Please Enter Username!");
                } else if(password1.matches("")){
                    password.setError("Enter Password");
                }
                else {

                    doLogin(name, password1);


                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


        String loginId = pref.getString("LOGIN_ID", null);
        String profileName = pref.getString("PROFILE_NAME", null);

        if(loginId != null && profileName != null){

            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("LOGIN_ID", loginId);
            intent.putExtra("PROFILE_NAME", profileName);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    public void openDialog() {
        DialogForgetPassword dialog = new DialogForgetPassword();
        //dialog.setArguments(new Bundle());
        dialog.show(getSupportFragmentManager(), "Forget Password");
    }

    private void doLogin(String username, String password) {

        progresslogin.setVisibility(View.VISIBLE);
        login.setEnabled(false);
        login.setAlpha(0.5f);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("EXCEPTION", "LOGINResponse : " + " " + response);
                        try {
                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;
                            String loginId = null;
                            String profileName = null;

                            for (int i = 0; i < ja.length(); i++) {
                                jo = ja.getJSONObject(i);

                                loginId = jo.getString("login_id");

                                if(loginId.matches("0")){

                                } else {
                                    profileName = jo.getString("user_name");
                                }

                            }

                            //Toast.makeText(LoginActivity.this, "Message : " + " " + custId, Toast.LENGTH_SHORT).show();

                            if (loginId != null) {

                                if (loginId.equals("0")) {

                                    new KAlertDialog(MainActivity.this, KAlertDialog.ERROR_TYPE)
                                            .setTitleText("Error!")
                                            .setContentText("The Username or Password that you've entered is incorrect or the account does not exist!")
                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                @Override
                                                public void onClick(KAlertDialog kAlertDialog) {
                                                    kAlertDialog.dismiss();
                                                }
                                            })
                                            .confirmButtonColor(R.drawable.kalert_button_background)
                                            .show();

                                } else {

                                    editor = pref.edit();
                                    editor.putString("LOGIN_ID", loginId);
                                    editor.putString("PROFILE_NAME", profileName);
                                    editor.commit();
                                    editor.apply();

                                    loginId = null;

                                    loginId = pref.getString("LOGIN_ID", null);

                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                    intent.putExtra("LOGIN_ID", loginId);
                                    intent.putExtra("PROFILE_NAME", profileName);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();

                                }

                            } else {
                                new KAlertDialog(MainActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error!")
                                        .setContentText("Some unexpected error occurred, Please try again later or contact service provider!")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog kAlertDialog) {
                                                kAlertDialog.dismiss();
                                            }
                                        })
                                        .confirmButtonColor(R.drawable.kalert_button_background)
                                        .show();
                            }

                            progresslogin.setVisibility(View.INVISIBLE);
                            login.setEnabled(true);
                            login.setAlpha(1);

                        } catch (JSONException e) {
                            e.printStackTrace();

                            //Toast.makeText(LoginActivity.this, "JSON Exception : " + " " + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("EXCEPTION", "JSON Exception : " + " " + e.toString());

                            new KAlertDialog(MainActivity.this, KAlertDialog.ERROR_TYPE)
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

                            progresslogin.setVisibility(View.INVISIBLE);
                            login.setEnabled(true);
                            login.setAlpha(1);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //Toast.makeText(LoginActivity.this, "Volley Error : " + " " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("EXCEPTION", "Volley Error : " + " " + error.toString());

                        new KAlertDialog(MainActivity.this, KAlertDialog.ERROR_TYPE)
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

                        progresslogin.setVisibility(View.INVISIBLE);
                        login.setEnabled(true);
                        login.setAlpha(1);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("user_name", username);
                params.put("user_pass", password);
                params.put("condition", "GET");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }


}
