package com.ultronyc.rvtaxsolutions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StartupScreenActivity extends AppCompatActivity {

    private ImageView companyLogoImg;
    private TextView companyNameTv;
    private TextView companyNameTv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_screen);

        companyLogoImg = (ImageView) findViewById(R.id.companyLogoTv);
        companyNameTv = (TextView) findViewById(R.id.companyNameTv);
        companyNameTv1 = (TextView) findViewById(R.id.companyNameTv1);


        showFadeInImage();

    }


    public void showFadeInImage(){

        Animation a = new AlphaAnimation(0.00f, 1);

        a.setDuration(2000);
        a.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            public void onAnimationEnd(Animation animation) {
                companyLogoImg.setVisibility(View.VISIBLE);
                //companyNameTv.setVisibility(View.VISIBLE);
                showFadeInText();

            }
        });

        companyLogoImg.startAnimation(a);
    }



    public void showFadeInText(){

        Animation a = new AlphaAnimation(0.00f, 1);

        a.setDuration(1500);
        a.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            public void onAnimationEnd(Animation animation) {
                companyNameTv.setVisibility(View.VISIBLE);
             //   companyNameTv1.setVisibility(View.VISIBLE);
               // Toast.makeText(getApplicationContext(), "ITR/Refund Status", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StartupScreenActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        companyNameTv.startAnimation(a);

    }


}
