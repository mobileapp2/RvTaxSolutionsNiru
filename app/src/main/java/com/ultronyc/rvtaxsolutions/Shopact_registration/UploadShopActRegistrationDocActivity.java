package com.ultronyc.rvtaxsolutions.Shopact_registration;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.developer.kalert.KAlertDialog;
import com.squareup.picasso.Picasso;
import com.ultronyc.rvtaxsolutions.R;
import com.ultronyc.rvtaxsolutions.paymentMainactivity;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class UploadShopActRegistrationDocActivity extends AppCompatActivity {

    private Button selectImageBtn_pancard,selectImageBtn_aadharcard,selectImageBtn_photo,selectImageBtn_lightbill,selectImageBtn_declaration,selectImageBtn_rent, submitBtn;
    private ImageView selectedImage_pancard,selectedImage_aadharcard,selectedImage_photo,selectedImage_lightbill,selectedImage_declaration,selectedImage_rent;

    private final int READ_STORAGE_PER_CODE = 21;
    private final int WRITE_STORAGE_PER_CODE = 22;
    private final int CAMERA_CAPTURE_PERMISSION = 23;
    public static boolean isReadPermissionGranted = false;
    public static boolean isWritePermissionGranted = false;
    public static boolean isCameraCapturePermissionGranted = false;

    private static final int FILE_SELECT_CODE = 111;
    private static final int FILE_CAPTURE_CODE = 112;

    private int doc_type=0;

    private ArrayList<String> supportedFileFormats = new ArrayList<>();

    private File capturedImageFile;
    private Uri capturedImageUri;
    private File selftGeneratedImageFile = null;

    private String imagePath_pancard = null, imageName_pancard = null, imageExtension_pancard = null;
    private boolean imageUploadStatus_pancard = false;
    private TextView imageNameTv_pancard;

    private String imagePath_aadharcard = null, imageName_aadharcard = null, imageExtension_aadharcard = null;
    private boolean imageUploadStatus_aadharcard = false;
    private TextView imageNameTv_aadharcard;

    private String imagePath_photo = null, imageName_photo = null, imageExtension_photo = null;
    private boolean imageUploadStatus_photo = false;
    private TextView imageNameTv_photo;

    private String imagePath_lightbill = null, imageName_lightbill = null, imageExtension_lightbill = null;
    private boolean imageUploadStatus_lightbill = false;
    private TextView imageNameTv_lightbill;

    private String imagePath_declaration = null, imageName_declaration = null, imageExtension_declaration = null;
    private boolean imageUploadStatus_declaration = false;
    private TextView imageNameTv_declaration;

    

    private String IP_Address = "";
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    String successActivityName,transactionId,EMAIL_ID,CUSTOMER_NUMBER,AMOUNT;

    private ProgressBar wp7ProgressBar;
    private TextView uploadStatusTv;
    private Button retryUploadButton, skipUploadAndSubmitFormBtn;
    private CardView imageDisplayCard_pancard,imageDisplayCard_aadharcard,imageDisplayCard_photo,imageDisplayCard_lightbill,imageDisplayCard_declaration;

    String loginId,profileName;

    String checking_Address = "http://rvtaxs.com/android/file_checking.php";
    String upload_Address = "http://rvtaxs.com/android/file_upload.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopact_registration_doc);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = PreferenceManager.getDefaultSharedPreferences(UploadShopActRegistrationDocActivity.this);

        final ActionBar abar = getSupportActionBar();
        //abar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("SHOPACT Registration Document Upload");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setIcon(R.color.transparent);
        abar.setHomeButtonEnabled(true);


        loginId = pref.getString("LOGIN_ID", null);
        profileName = pref.getString("PROFILE_NAME", null);

        Intent intent = getIntent();
        successActivityName = intent.getStringExtra("ACTIVITY_NAME");
        transactionId = intent.getStringExtra("shopact_registration_no");
        EMAIL_ID = intent.getStringExtra("EMAIL_ID");
        CUSTOMER_NUMBER = intent.getStringExtra("CUSTOMER_NUMBER");
        AMOUNT = intent.getStringExtra("AMOUNT");


        supportedFileFormats.add("jpeg");
        supportedFileFormats.add("jpg");
        supportedFileFormats.add("png");
        supportedFileFormats.add("pdf");

        selectImageBtn_pancard = (Button) findViewById(R.id.SHOPACT_selectImageBtn_pancard);
        imageNameTv_pancard = (TextView) findViewById(R.id.SHOPACT_imageNameTv_pancard);
        selectedImage_pancard = (ImageView) findViewById(R.id.SHOPACT_selectedImage_pancard);
        imageDisplayCard_pancard = (CardView) findViewById(R.id.SHOPACT_imageCard_pancard);

        selectImageBtn_aadharcard = (Button) findViewById(R.id.SHOPACT_selectImageBtn_aadharcard);
        imageNameTv_aadharcard = (TextView) findViewById(R.id.SHOPACT_imageNameTv_aadharcard);
        selectedImage_aadharcard = (ImageView) findViewById(R.id.SHOPACT_selectedImage_aadharcard);
        imageDisplayCard_aadharcard = (CardView) findViewById(R.id.SHOPACT_imageCard_aadharcard);

        selectImageBtn_photo = (Button) findViewById(R.id.SHOPACT_selectImageBtn_photo);
        imageNameTv_photo = (TextView) findViewById(R.id.SHOPACT_imageNameTv_photo);
        selectedImage_photo = (ImageView) findViewById(R.id.SHOPACT_selectedImage_photo);
        imageDisplayCard_photo = (CardView) findViewById(R.id.SHOPACT_imageCard_photo);

        selectImageBtn_lightbill = (Button) findViewById(R.id.SHOPACT_selectImageBtn_lightbill);
        imageNameTv_lightbill = (TextView) findViewById(R.id.SHOPACT_imageNameTv_lightbill);
        selectedImage_lightbill = (ImageView) findViewById(R.id.SHOPACT_selectedImage_lightbill);
        imageDisplayCard_lightbill = (CardView) findViewById(R.id.SHOPACT_imageCard_lightbill);

        selectImageBtn_declaration = (Button) findViewById(R.id.SHOPACT_selectImageBtn_declaration);
        imageNameTv_declaration = (TextView) findViewById(R.id.SHOPACT_imageNameTv_declaration);
        selectedImage_declaration = (ImageView) findViewById(R.id.SHOPACT_selectedImage_declaration);
        imageDisplayCard_declaration = (CardView) findViewById(R.id.SHOPACT_imageCard_declaration);



        submitBtn = (Button) findViewById(R.id.SHOPACTsubmitBtn);

        wp7ProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        uploadStatusTv = (TextView) findViewById(R.id.submitUploadStatusTv);



        // wp7ProgressBar.showProgressBar();

        submitBtn = (Button) findViewById(R.id.SHOPACTsubmitBtn);

        selectImageBtn_pancard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doc_type=1;
                if (checkPermissionSts(READ_STORAGE_PER_CODE)) {

                    isReadPermissionGranted = true;

                    if (checkPermissionSts(WRITE_STORAGE_PER_CODE)) {

                        isWritePermissionGranted = true;
                        showFileChooser();
                        //selectImage(UploadImageActivity.this);

                    } else {
                        isWritePermissionGranted = false;
                        requestForPermission(WRITE_STORAGE_PER_CODE);
                    }

                } else {
                    isReadPermissionGranted = false;
                    requestForPermission(READ_STORAGE_PER_CODE);
                }


            }
        });


        selectImageBtn_aadharcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doc_type=2;
                if (checkPermissionSts(READ_STORAGE_PER_CODE)) {

                    isReadPermissionGranted = true;

                    if (checkPermissionSts(WRITE_STORAGE_PER_CODE)) {

                        isWritePermissionGranted = true;
                        showFileChooser();
                        //selectImage(UploadImageActivity.this);

                    } else {
                        isWritePermissionGranted = false;
                        requestForPermission(WRITE_STORAGE_PER_CODE);
                    }

                } else {
                    isReadPermissionGranted = false;
                    requestForPermission(READ_STORAGE_PER_CODE);
                }


            }
        });


        selectImageBtn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doc_type=3;
                if (checkPermissionSts(READ_STORAGE_PER_CODE)) {

                    isReadPermissionGranted = true;

                    if (checkPermissionSts(WRITE_STORAGE_PER_CODE)) {

                        isWritePermissionGranted = true;
                        showFileChooser();
                        //selectImage(UploadImageActivity.this);

                    } else {
                        isWritePermissionGranted = false;
                        requestForPermission(WRITE_STORAGE_PER_CODE);
                    }

                } else {
                    isReadPermissionGranted = false;
                    requestForPermission(READ_STORAGE_PER_CODE);
                }


            }
        });

        selectImageBtn_lightbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doc_type=4;
                if (checkPermissionSts(READ_STORAGE_PER_CODE)) {

                    isReadPermissionGranted = true;

                    if (checkPermissionSts(WRITE_STORAGE_PER_CODE)) {

                        isWritePermissionGranted = true;
                        showFileChooser();
                        //selectImage(UploadImageActivity.this);

                    } else {
                        isWritePermissionGranted = false;
                        requestForPermission(WRITE_STORAGE_PER_CODE);
                    }

                } else {
                    isReadPermissionGranted = false;
                    requestForPermission(READ_STORAGE_PER_CODE);
                }


            }
        });


        selectImageBtn_declaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doc_type=5;
                if (checkPermissionSts(READ_STORAGE_PER_CODE)) {

                    isReadPermissionGranted = true;

                    if (checkPermissionSts(WRITE_STORAGE_PER_CODE)) {

                        isWritePermissionGranted = true;
                        showFileChooser();
                        //selectImage(UploadImageActivity.this);

                    } else {
                        isWritePermissionGranted = false;
                        requestForPermission(WRITE_STORAGE_PER_CODE);
                    }

                } else {
                    isReadPermissionGranted = false;
                    requestForPermission(READ_STORAGE_PER_CODE);
                }


            }
        });




        imageNameTv_pancard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkPermissionSts(READ_STORAGE_PER_CODE)) {

                    isReadPermissionGranted = true;

                    if (checkPermissionSts(WRITE_STORAGE_PER_CODE)) {

                        isWritePermissionGranted = true;
                        showFileChooser();
                        //selectImage(UploadImageActivity.this);

                    } else {
                        isWritePermissionGranted = false;
                        requestForPermission(WRITE_STORAGE_PER_CODE);
                    }

                } else {
                    isReadPermissionGranted = false;
                    requestForPermission(READ_STORAGE_PER_CODE);
                }

            }
        });


        imageNameTv_aadharcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkPermissionSts(READ_STORAGE_PER_CODE)) {

                    isReadPermissionGranted = true;

                    if (checkPermissionSts(WRITE_STORAGE_PER_CODE)) {

                        isWritePermissionGranted = true;
                        showFileChooser();
                        //selectImage(UploadImageActivity.this);

                    } else {
                        isWritePermissionGranted = false;
                        requestForPermission(WRITE_STORAGE_PER_CODE);
                    }

                } else {
                    isReadPermissionGranted = false;
                    requestForPermission(READ_STORAGE_PER_CODE);
                }

            }
        });


        imageNameTv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkPermissionSts(READ_STORAGE_PER_CODE)) {

                    isReadPermissionGranted = true;

                    if (checkPermissionSts(WRITE_STORAGE_PER_CODE)) {

                        isWritePermissionGranted = true;
                        showFileChooser();
                        //selectImage(UploadImageActivity.this);

                    } else {
                        isWritePermissionGranted = false;
                        requestForPermission(WRITE_STORAGE_PER_CODE);
                    }

                } else {
                    isReadPermissionGranted = false;
                    requestForPermission(READ_STORAGE_PER_CODE);
                }

            }
        });

        imageNameTv_lightbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkPermissionSts(READ_STORAGE_PER_CODE)) {

                    isReadPermissionGranted = true;

                    if (checkPermissionSts(WRITE_STORAGE_PER_CODE)) {

                        isWritePermissionGranted = true;
                        showFileChooser();
                        //selectImage(UploadImageActivity.this);

                    } else {
                        isWritePermissionGranted = false;
                        requestForPermission(WRITE_STORAGE_PER_CODE);
                    }

                } else {
                    isReadPermissionGranted = false;
                    requestForPermission(READ_STORAGE_PER_CODE);
                }

            }
        });


        imageNameTv_declaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkPermissionSts(READ_STORAGE_PER_CODE)) {

                    isReadPermissionGranted = true;

                    if (checkPermissionSts(WRITE_STORAGE_PER_CODE)) {

                        isWritePermissionGranted = true;
                        showFileChooser();
                        //selectImage(UploadImageActivity.this);

                    } else {
                        isWritePermissionGranted = false;
                        requestForPermission(WRITE_STORAGE_PER_CODE);
                    }

                } else {
                    isReadPermissionGranted = false;
                    requestForPermission(READ_STORAGE_PER_CODE);
                }

            }
        });



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    if (imagePath_pancard == null) {
                    } else {
                        uploadAttachedFilesNew_pancard();
                    }

                    if (imagePath_aadharcard == null) {
                    } else {
                        uploadAttachedFilesNew_aadharcard();
                    }

                    if (imagePath_photo == null) {
                    } else {
                        uploadAttachedFilesNew_photo();
                    }

                    if (imagePath_lightbill == null) {
                    } else {
                        uploadAttachedFilesNew_lightbill();
                    }

                    if (imagePath_declaration == null) {
                    } else {
                        uploadAttachedFilesNew_declaration();
                    }

                    if(imageName_pancard==null)
                    {}
                    else {
                        getFileUploadStatus_pancard(imageName_pancard, false);
                    }



            }
        });





    }




    private void uploadAttachedFilesNew_pancard() {

        selectImageBtn_pancard.setEnabled(false);
        submitBtn.setEnabled(false);
        selectImageBtn_pancard.setAlpha(0.5f);
        submitBtn.setAlpha(0.5f);
        uploadStatusTv.setVisibility(View.VISIBLE);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                File files = new File(imagePath_pancard);

                if (files.exists()) {

                    if (!imageUploadStatus_pancard) {


                        File file = new File(imagePath_pancard);
                        String content_type = getMimeType(imagePath_pancard);

                        OkHttpClient client = new OkHttpClient();

                        RequestBody fileBody = RequestBody.create(file, MediaType.parse(content_type));
                        String filePath = file.getAbsolutePath();

                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("tran_id", transactionId)
                                .addFormDataPart("type", content_type)
                                .addFormDataPart("file", filePath.substring(filePath.lastIndexOf("/") + 1), fileBody)
                                .build();

                        okhttp3.Request request = new okhttp3.Request.Builder()

                                .url(upload_Address)
                                .post(requestBody)
                                .build();

                        try {
                            okhttp3.Response response = client.newCall(request).execute();

                            if (!response.isSuccessful()) {

                                Log.d("SERVER_RESPONSE", "Error : " + " " + response);

                                //throw new IOException("Error : " + response);
                                //uploadFailureList.add(fileInfo);
                                //pojoUploadFilesList.get(fileIndexNumber).setUploadStatus(false);
                                //getFileUploadStatus(fileInfo.getFileName(), fileIndexNumber);

                                imageUploadStatus_pancard = false;
                                //getFileUploadStatus_pancard(imageName_pancard, false);


                            } else {

                                Log.d("SERVER_RESPONSE", "Response : " + " " + response);
                                //pojoUploadFilesList.get(fileIndexNumber).setUploadStatus(true);
                                imageUploadStatus_pancard = true;
                               // getFileUploadStatus_pancard(imageName_pancard, false);

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("SERVER_RESPONSE", "Exception : " + " " + e);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        Log.d("ISEXISTCHECK", "IsExist? = " + " " + file.exists());


                    } else {

                       // getFileUploadStatus_pancard(imageName_pancard, true);
                    }

                } else {
                    //Toasty.error(GenerateRequestActivity.this, "", Toasty.LENGTH_LONG).show();
                    if (imageNameTv_pancard.length() > 15) {

                      //  showMessage(imageName.substring(1, 15) + " " + "does not exist anymore!", "error");

                    } else {

                       // showMessage(imageName + " " + "does not exist anymore!", "error");
                    }


                }


            }
        });

        thread.start();


    }





    private void uploadAttachedFilesNew_aadharcard() {

        selectImageBtn_aadharcard.setEnabled(false);
        submitBtn.setEnabled(false);
//        skipUploadAndSubmitFormBtn.setEnabled(false);
        selectImageBtn_aadharcard.setAlpha(0.5f);
        submitBtn.setAlpha(0.5f);
        // skipUploadAndSubmitFormBtn.setAlpha(0.5f);

        uploadStatusTv.setVisibility(View.VISIBLE);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                File files = new File(imagePath_aadharcard);

                if (files.exists()) {

                    if (!imageUploadStatus_aadharcard) {

                        // http://www.kpssatara.com/pages/inverse/demo_file_upload.php

//                    File file = new File("/storage/sdcard1/FileUpload.xls");
//                    String content_type = getMimeType("/storage/sdcard1/FileUpload.xls");

                        File file = new File(imagePath_aadharcard);
                        String content_type = getMimeType(imagePath_aadharcard);

                        OkHttpClient client = new OkHttpClient();

                        //RequestBody fileBody = RequestBody.create(MediaType.parse(content_type), file);
                        RequestBody fileBody = RequestBody.create(file, MediaType.parse(content_type));

                        String filePath = file.getAbsolutePath();

                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("tran_id", transactionId)
                                .addFormDataPart("type", content_type)
                                .addFormDataPart("file", filePath.substring(filePath.lastIndexOf("/") + 1), fileBody)
                                .build();

                        okhttp3.Request request = new okhttp3.Request.Builder()

                                .url(upload_Address)
                                .post(requestBody)
                                .build();

                        try {
                            okhttp3.Response response = client.newCall(request).execute();

                            //                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    int filecount = fileIndexNumber;
//
//                                    generateStatusTv.setText("Uploading file" + " " + filecount);
//
//                                    Log.d("FILEINDEXNUMBER", "FileIndexNumber = " + " " + filecount);
//                                }
//                            });

                            if (!response.isSuccessful()) {

                                Log.d("SERVER_RESPONSE", "Error : " + " " + response);

                                //throw new IOException("Error : " + response);
                                //uploadFailureList.add(fileInfo);
                                //pojoUploadFilesList.get(fileIndexNumber).setUploadStatus(false);
                                //getFileUploadStatus(fileInfo.getFileName(), fileIndexNumber);

                                imageUploadStatus_aadharcard = false;
                              //  getFileUploadStatus_aadharcard(imageName_aadharcard, false);


                            } else {

                                Log.d("SERVER_RESPONSE", "Response : " + " " + response);
                                //pojoUploadFilesList.get(fileIndexNumber).setUploadStatus(true);
                                imageUploadStatus_aadharcard = true;
                               // getFileUploadStatus_aadharcard(imageName_aadharcard, false);

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("SERVER_RESPONSE", "Exception : " + " " + e);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        Log.d("ISEXISTCHECK", "IsExist? = " + " " + file.exists());


                    } else {

                       // getFileUploadStatus_aadharcard(imageName_aadharcard, true);

                    }

                } else {
                    //Toasty.error(GenerateRequestActivity.this, "", Toasty.LENGTH_LONG).show();
                    if (imageNameTv_aadharcard.length() > 15) {

                        //  showMessage(imageName.substring(1, 15) + " " + "does not exist anymore!", "error");

                    } else {

                        // showMessage(imageName + " " + "does not exist anymore!", "error");
                    }


                }


            }
        });

        thread.start();


    }



    private void uploadAttachedFilesNew_photo() {

        selectImageBtn_photo.setEnabled(false);
        submitBtn.setEnabled(false);
//        skipUploadAndSubmitFormBtn.setEnabled(false);
        selectImageBtn_photo.setAlpha(0.5f);
        submitBtn.setAlpha(0.5f);
        // skipUploadAndSubmitFormBtn.setAlpha(0.5f);

        uploadStatusTv.setVisibility(View.VISIBLE);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                File files = new File(imagePath_photo);

                if (files.exists()) {

                    if (!imageUploadStatus_photo) {

                        // http://www.kpssatara.com/pages/inverse/demo_file_upload.php

//                    File file = new File("/storage/sdcard1/FileUpload.xls");
//                    String content_type = getMimeType("/storage/sdcard1/FileUpload.xls");

                        File file = new File(imagePath_photo);
                        String content_type = getMimeType(imagePath_photo);

                        OkHttpClient client = new OkHttpClient();

                        //RequestBody fileBody = RequestBody.create(MediaType.parse(content_type), file);
                        RequestBody fileBody = RequestBody.create(file, MediaType.parse(content_type));

                        String filePath = file.getAbsolutePath();

                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("tran_id", transactionId)
                                .addFormDataPart("type", content_type)
                                .addFormDataPart("file", filePath.substring(filePath.lastIndexOf("/") + 1), fileBody)
                                .build();

                        okhttp3.Request request = new okhttp3.Request.Builder()

                                .url(upload_Address)
                                .post(requestBody)
                                .build();

                        try {
                            okhttp3.Response response = client.newCall(request).execute();

                            //                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    int filecount = fileIndexNumber;
//
//                                    generateStatusTv.setText("Uploading file" + " " + filecount);
//
//                                    Log.d("FILEINDEXNUMBER", "FileIndexNumber = " + " " + filecount);
//                                }
//                            });

                            if (!response.isSuccessful()) {

                                Log.d("SERVER_RESPONSE", "Error : " + " " + response);

                                //throw new IOException("Error : " + response);
                                //uploadFailureList.add(fileInfo);
                                //pojoUploadFilesList.get(fileIndexNumber).setUploadStatus(false);
                                //getFileUploadStatus(fileInfo.getFileName(), fileIndexNumber);

                                imageUploadStatus_photo = false;
                              // getFileUploadStatus_photo(imageName_photo, false);


                            } else {

                                Log.d("SERVER_RESPONSE", "Response : " + " " + response);
                                //pojoUploadFilesList.get(fileIndexNumber).setUploadStatus(true);
                                imageUploadStatus_photo = true;
                               // getFileUploadStatus_photo(imageName_photo, false);

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("SERVER_RESPONSE", "Exception : " + " " + e);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        Log.d("ISEXISTCHECK", "IsExist? = " + " " + file.exists());


                    } else {

                       // getFileUploadStatus_photo(imageName_photo, true);

                    }

                } else {
                    //Toasty.error(GenerateRequestActivity.this, "", Toasty.LENGTH_LONG).show();
                    if (imageNameTv_photo.length() > 15) {

                        //  showMessage(imageName.substring(1, 15) + " " + "does not exist anymore!", "error");

                    } else {

                        // showMessage(imageName + " " + "does not exist anymore!", "error");
                    }


                }


            }
        });

        thread.start();


    }



    private void uploadAttachedFilesNew_lightbill() {

        selectImageBtn_lightbill.setEnabled(false);
        submitBtn.setEnabled(false);
//        skipUploadAndSubmitFormBtn.setEnabled(false);
        selectImageBtn_lightbill.setAlpha(0.5f);
        submitBtn.setAlpha(0.5f);
        // skipUploadAndSubmitFormBtn.setAlpha(0.5f);

        uploadStatusTv.setVisibility(View.VISIBLE);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                File files = new File(imagePath_lightbill);

                if (files.exists()) {

                    if (!imageUploadStatus_lightbill) {

                        // http://www.kpssatara.com/pages/inverse/demo_file_upload.php

//                    File file = new File("/storage/sdcard1/FileUpload.xls");
//                    String content_type = getMimeType("/storage/sdcard1/FileUpload.xls");

                        File file = new File(imagePath_lightbill);
                        String content_type = getMimeType(imagePath_lightbill);

                        OkHttpClient client = new OkHttpClient();

                        //RequestBody fileBody = RequestBody.create(MediaType.parse(content_type), file);
                        RequestBody fileBody = RequestBody.create(file, MediaType.parse(content_type));

                        String filePath = file.getAbsolutePath();

                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("tran_id", transactionId)
                                .addFormDataPart("type", content_type)
                                .addFormDataPart("file", filePath.substring(filePath.lastIndexOf("/") + 1), fileBody)
                                .build();

                        okhttp3.Request request = new okhttp3.Request.Builder()

                                .url(upload_Address)
                                .post(requestBody)
                                .build();

                        try {
                            okhttp3.Response response = client.newCall(request).execute();

                            //                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    int filecount = fileIndexNumber;
//
//                                    generateStatusTv.setText("Uploading file" + " " + filecount);
//
//                                    Log.d("FILEINDEXNUMBER", "FileIndexNumber = " + " " + filecount);
//                                }
//                            });

                            if (!response.isSuccessful()) {

                                Log.d("SERVER_RESPONSE", "Error : " + " " + response);

                                //throw new IOException("Error : " + response);
                                //uploadFailureList.add(fileInfo);
                                //pojoUploadFilesList.get(fileIndexNumber).setUploadStatus(false);
                                //getFileUploadStatus(fileInfo.getFileName(), fileIndexNumber);

                                imageUploadStatus_lightbill = false;
                               // getFileUploadStatus_lightbill(imageName_lightbill, false);


                            } else {

                                Log.d("SERVER_RESPONSE", "Response : " + " " + response);
                                //pojoUploadFilesList.get(fileIndexNumber).setUploadStatus(true);
                                imageUploadStatus_lightbill = true;
                               // getFileUploadStatus_lightbill(imageName_lightbill, false);

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("SERVER_RESPONSE", "Exception : " + " " + e);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        Log.d("ISEXISTCHECK", "IsExist? = " + " " + file.exists());


                    } else {

                        //getFileUploadStatus_lightbill(imageName_lightbill, true);

                    }

                } else {
                    //Toasty.error(GenerateRequestActivity.this, "", Toasty.LENGTH_LONG).show();
                    if (imageNameTv_lightbill.length() > 15) {

                        //  showMessage(imageName.substring(1, 15) + " " + "does not exist anymore!", "error");

                    } else {

                        // showMessage(imageName + " " + "does not exist anymore!", "error");
                    }


                }


            }
        });

        thread.start();


    }



    private void uploadAttachedFilesNew_declaration() {

        selectImageBtn_declaration.setEnabled(false);
        submitBtn.setEnabled(false);
//        skipUploadAndSubmitFormBtn.setEnabled(false);
        selectImageBtn_declaration.setAlpha(0.5f);
        submitBtn.setAlpha(0.5f);
        // skipUploadAndSubmitFormBtn.setAlpha(0.5f);

        uploadStatusTv.setVisibility(View.VISIBLE);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                File files = new File(imagePath_declaration);

                if (files.exists()) {

                    if (!imageUploadStatus_declaration) {

                        // http://www.kpssatara.com/pages/inverse/demo_file_upload.php

//                    File file = new File("/storage/sdcard1/FileUpload.xls");
//                    String content_type = getMimeType("/storage/sdcard1/FileUpload.xls");

                        File file = new File(imagePath_declaration);
                        String content_type = getMimeType(imagePath_declaration);

                        OkHttpClient client = new OkHttpClient();

                        //RequestBody fileBody = RequestBody.create(MediaType.parse(content_type), file);
                        RequestBody fileBody = RequestBody.create(file, MediaType.parse(content_type));

                        String filePath = file.getAbsolutePath();

                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("tran_id", transactionId)
                                .addFormDataPart("type", content_type)
                                .addFormDataPart("file", filePath.substring(filePath.lastIndexOf("/") + 1), fileBody)
                                .build();

                        okhttp3.Request request = new okhttp3.Request.Builder()

                                .url(upload_Address)
                                .post(requestBody)
                                .build();

                        try {
                            okhttp3.Response response = client.newCall(request).execute();

                            //                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    int filecount = fileIndexNumber;
//
//                                    generateStatusTv.setText("Uploading file" + " " + filecount);
//
//                                    Log.d("FILEINDEXNUMBER", "FileIndexNumber = " + " " + filecount);
//                                }
//                            });

                            if (!response.isSuccessful()) {

                                Log.d("SERVER_RESPONSE", "Error : " + " " + response);

                                //throw new IOException("Error : " + response);
                                //uploadFailureList.add(fileInfo);
                                //pojoUploadFilesList.get(fileIndexNumber).setUploadStatus(false);
                                //getFileUploadStatus(fileInfo.getFileName(), fileIndexNumber);

                                imageUploadStatus_declaration = false;
                               // getFileUploadStatus_declaration(imageName_declaration, false);
                              //  getFileUploadStatus_pancard(imageName_pancard, false);


                            } else {

                                Log.d("SERVER_RESPONSE", "Response : " + " " + response);
                                //pojoUploadFilesList.get(fileIndexNumber).setUploadStatus(true);
                                imageUploadStatus_declaration = true;
                               // getFileUploadStatus_declaration(imageName_declaration, false);
                                //getFileUploadStatus_pancard(imageName_pancard, false);

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("SERVER_RESPONSE", "Exception : " + " " + e);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        Log.d("ISEXISTCHECK", "IsExist? = " + " " + file.exists());


                    } else {

                       // getFileUploadStatus_declaration(imageName_declaration, true);
                       // getFileUploadStatus_pancard(imageName_pancard, true);

                    }

                } else {
                    //Toasty.error(GenerateRequestActivity.this, "", Toasty.LENGTH_LONG).show();
                    if (imageNameTv_declaration.length() > 15) {

                        //  showMessage(imageName.substring(1, 15) + " " + "does not exist anymore!", "error");

                    } else {

                        // showMessage(imageName + " " + "does not exist anymore!", "error");
                    }


                }


            }
        });

        thread.start();


    }




    private void getFileUploadStatus_pancard(String filename, boolean isUploaded) {

        //progressBar.setVisibility(View.VISIBLE);
        //dataLoadFailureLayout.setVisibility(View.GONE);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                uploadStatusTv.setText("Uploading Confirmed, Please Wait!");

            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST, checking_Address ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("UPLOAD_STATUS_LOG", "Response : " + " " + response);

                        try {

                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;

                            String message = null;

                            for (int i = 0; i < ja.length(); i++) {
                                jo = ja.getJSONObject(i);

                                message = jo.getString("Message");

                                if (message.equals("1")) {

                                    imageUploadStatus_pancard = true;


                                } else if (message.equals("0")) {

                                    imageUploadStatus_pancard = true;

                                }


                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                if (!imageUploadStatus_pancard) {


                                                    uploadStatusTv.setText("File Uploading Failed!");

                                                    uploadStatusTv.setVisibility(View.GONE);

                                                    selectImageBtn_pancard.setEnabled(true);
                                                    submitBtn.setEnabled(true);
                                                 //   skipUploadAndSubmitFormBtn.setEnabled(true);
                                                    selectImageBtn_pancard.setAlpha(1);
                                                    submitBtn.setAlpha(1);
                                                  //  skipUploadAndSubmitFormBtn.setAlpha(1);



                                                } else {

                                                    /*uploadStatusTv.setText("All files Uploaded successfully");
                                                    retryBtn.setVisibility(View.GONE);
                                                    wp7ProgressBar.setVisibility(View.GONE);
                                                    submitPropBtn.setEnabled(true);
                                                    submitPropBtn.setAlpha(1);

                                                    //callSubmitForm();

                                                    Intent intent = new Intent(UploadImageActivity.this, MainActivity.class);
                                                    intent.putExtra("FRAGMENT_TYPE", fragmentType);
                                                    intent.putExtra("RESTART_TYPE", restartType);
                                                    intent.putExtra("RESTART_PROP_TYPE", restartPropType);
                                                    intent.putExtra("IP_ADDRESS", IP_Address);
                                                    intent.putExtra("ORDER_ID", orderId);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);*/

                                                    Intent intent = new Intent(UploadShopActRegistrationDocActivity.this, paymentMainactivity.class);
                                                    intent.putExtra("EMAIL_ID", EMAIL_ID);
                                                    intent.putExtra("tran_id", transactionId);
                                                    intent.putExtra("CUSTOMER_NUMBER", CUSTOMER_NUMBER);
                                                    intent.putExtra("AMOUNT", AMOUNT);

                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);

                                                }

                                            }
                                        });
                                    }
                                }, 10000);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(GenerateRequestActivity.this, "JSON Exception : " + " " + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("UPLOAD_STATUS_LOG", "JSON Exception : " + " " + e.toString());

                            //pojoUploadFilesList.get(fileIndexNumber).setUploadStatus(false);

                            /*new KAlertDialog(GeneralApprvMenuActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setContentText("Ops, Something went wrong!")
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog kAlertDialog) {
                                            kAlertDialog.dismiss();
                                        }
                                    })
                                    .confirmButtonColor(R.drawable.kalert_button_background)
                                    .show();*/

                            // progressBar.setVisibility(View.GONE);
                            //dataLoadFailureLayout.setVisibility(View.VISIBLE);
                            //errorTv.setText("Ops, Something went wrong!");





                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(GenerateRequestActivity.this, "Volley Error : " + " " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("UPLOAD_STATUS_LOG", "Volley Error : " + " " + error.toString());

                        //pojoUploadFilesList.get(fileIndexNumber).setUploadStatus(false);

                        /*new KAlertDialog(GeneralApprvMenuActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!")
                                .setContentText("Can't communicate with server, Please try again.")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog kAlertDialog) {
                                        kAlertDialog.dismiss();
                                    }
                                })
                                .confirmButtonColor(R.drawable.kalert_button_background)
                                .show();*/

                        // progressBar.setVisibility(View.GONE);
                       /* dataLoadFailureLayout.setVisibility(View.VISIBLE);
                        errorTv.setText("Can't communicate with server, Please try again.");*/




                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("name", filename);
                params.put("tran_id", transactionId);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(UploadShopActRegistrationDocActivity.this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,           //  5000
                0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }



    // -----------  FILE MANAGEMENT CODE STARTS  ------------------


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == FILE_SELECT_CODE) {

            if (resultCode == RESULT_OK) {

                Uri uri = data.getData();
                Log.d("FILE_LOG_DATAURI", "File Uri: " + uri.toString());
                // Get the path
                try {

                    String path = null;


                    Log.d("FILECAPTURELOG", "Image URI in activity result:" + " " + uri);

                    path = getRealPathFromURI(this, uri);

                    Log.d("FILEURILOG", "URI :" + " " + uri);

                    //String path = getRealPathFromURI(this, uri);

                    Log.d("FILE", "File Path: " + path);
                    int index = path.lastIndexOf('/');
                    Log.d("FILE", "File Path: " + path.substring(index + 1, path.length()));
                    Log.d("FILE", "Index: " + index);
                    String fileName = path.substring(index + 1, path.length());
                    String fileNameFull = path.substring(index + 1, path.length());

                    //Toast.makeText(this, "Mimetype : " + " " + getMimeType(fileNameFull), Toast.LENGTH_SHORT).show();

                    int extIndex = fileNameFull.lastIndexOf(".");
                    String extension = path.substring(extIndex + 1, fileNameFull.length());

                    //Toast.makeText(this, "Extension : " + " " + getFileExtension(fileNameFull), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(this, "Extension:" + getFileExtensio n(fileNameFull) + "--", Toast.LENGTH_SHORT).show();

                    Pattern pattern = Pattern.compile("[^A-Za-z0-9]");
                    Matcher matcher = pattern.matcher(String.valueOf(fileName.charAt(0)));

                    //Matcher matcherEnd = pattern.matcher(String.valueOf(fileName.charAt(fileName.length() - 1)));
                    Matcher matcherEnd = pattern.matcher(String.valueOf(fileName.charAt(fileName.lastIndexOf(".") - 1)));

                            /*if(matcherEnd.find()){
                                Toast.makeText(this, "File contains special case at end", Toast.LENGTH_SHORT).show();
                            }*/

                    if (matcher.find() || matcherEnd.find()) {

                        new KAlertDialog(UploadShopActRegistrationDocActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!")
                                .setContentText("Image must not contain special case character at the start and the end of its name")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog kAlertDialog) {
                                        kAlertDialog.dismiss();
                                    }
                                })
                                .confirmButtonColor(R.drawable.kalert_button_background)
                                .show();



                        // Toast.makeText(this, "File" + " " + " " + fileName.substring(0, 10) + " " + " " + "must not contain special case character at the start and the end of its name", Toast.LENGTH_LONG).show();
                    } else {

                        if (supportedFileFormats.contains(getFileExtension(fileNameFull))) {

                            //Toast.makeText(this, "Extension :" + " " + getFileExtension(fileNameFull), Toast.LENGTH_LONG).show();

                            if (fileName.length() > 23) {
                                fileName = fileName.substring(0, 23);
                                fileName = fileName + "...";
                            }


                            File imgFile = new File(path);

                            if (imgFile.exists()) {

                                //image exist do selection and upload code here
                                if(doc_type==1) {
                                    imageName_pancard = fileName;
                                    imagePath_pancard = path;
                                    imageExtension_pancard = extension;
                                    imageUploadStatus_pancard = false;
                                    imageDisplayCard_pancard.setVisibility(View.VISIBLE);
                                    imageNameTv_pancard.setText(fileNameFull);
                                    Picasso.get().load(imgFile).into(selectedImage_pancard);
                                    selectedImage_pancard.invalidate();
                                }
                                else if(doc_type==2)
                                {
                                    imageName_aadharcard = fileName;
                                    imagePath_aadharcard = path;
                                    imageExtension_aadharcard = extension;
                                    imageUploadStatus_aadharcard = false;
                                    imageDisplayCard_aadharcard.setVisibility(View.VISIBLE);
                                    imageNameTv_aadharcard.setText(fileNameFull);
                                    Picasso.get().load(imgFile).into(selectedImage_aadharcard);
                                    selectedImage_aadharcard.invalidate();
                                }
                                else if(doc_type==3)
                                {
                                    imageName_photo = fileName;
                                    imagePath_photo = path;
                                    imageExtension_photo = extension;
                                    imageUploadStatus_photo = false;
                                    imageDisplayCard_photo.setVisibility(View.VISIBLE);
                                    imageNameTv_photo.setText(fileNameFull);
                                    Picasso.get().load(imgFile).into(selectedImage_photo);
                                    selectedImage_photo.invalidate();
                                }
                                else if(doc_type==4)
                                {
                                    imageName_lightbill = fileName;
                                    imagePath_lightbill = path;
                                    imageExtension_lightbill = extension;
                                    imageUploadStatus_lightbill = false;
                                    imageDisplayCard_lightbill.setVisibility(View.VISIBLE);
                                    imageNameTv_lightbill.setText(fileNameFull);
                                    Picasso.get().load(imgFile).into(selectedImage_lightbill);
                                    selectedImage_lightbill.invalidate();
                                }
                                else if(doc_type==5)
                                {
                                    imageName_declaration = fileName;
                                    imagePath_declaration = path;
                                    imageExtension_declaration = extension;
                                    imageUploadStatus_declaration = false;
                                    imageDisplayCard_declaration.setVisibility(View.VISIBLE);
                                    imageNameTv_declaration.setText(fileNameFull);
                                    Picasso.get().load(imgFile).into(selectedImage_declaration);
                                    selectedImage_declaration.invalidate();
                                }


                            } else {

                                if (fileName.length() > 15) {
                                    new KAlertDialog(UploadShopActRegistrationDocActivity.this, KAlertDialog.ERROR_TYPE)
                                            .setTitleText("Error!")
                                            .setContentText("File" + " " + fileNameFull.substring(0, 15) + " " + "does not exists!")
                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                @Override
                                                public void onClick(KAlertDialog kAlertDialog) {
                                                    kAlertDialog.dismiss();
                                                }
                                            })
                                            .confirmButtonColor(R.drawable.kalert_button_background)
                                            .show();


                                } else {
                                    new KAlertDialog(UploadShopActRegistrationDocActivity.this, KAlertDialog.ERROR_TYPE)
                                            .setTitleText("Error!")
                                            .setContentText("File" + " " + fileNameFull + " " + "does not exists!")
                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                @Override
                                                public void onClick(KAlertDialog kAlertDialog) {
                                                    kAlertDialog.dismiss();
                                                }
                                            })
                                            .confirmButtonColor(R.drawable.kalert_button_background)
                                            .show();

                                }
                            }


                        } else {

                            //Toast.makeText(this, "Not Supported", Toast.LENGTH_SHORT).show();
                            new KAlertDialog(UploadShopActRegistrationDocActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setContentText("Image format of this image is not supported!")
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog kAlertDialog) {
                                            kAlertDialog.dismiss();
                                        }
                                    })
                                    .confirmButtonColor(R.drawable.kalert_button_background)
                                    .show();

                        }
                    }
                } catch (Exception use) {
                    use.printStackTrace();
                    Log.d("FILE", "URI Syntax Exception : " + "  " + use);
                }

                // }


            }

        } else if (requestCode == FILE_CAPTURE_CODE) {

            //Toast.makeText(this, "Inside File Capture Code", Toast.LENGTH_SHORT).show();

            if (resultCode == RESULT_OK) {

                try {

                    String path = null;

                            /*if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1){
                                path = getPath(this, uri);
                            } else {
                                path = getRealPathFromURI(this, uri);
                            }*/

                    Log.d("FILECAPTURELOG", "Image absolute path:" + " " + selftGeneratedImageFile.getAbsolutePath());

                    //path = getRealPathFromURI(this, capturedImageUri);
                    path =  selftGeneratedImageFile.getAbsolutePath();

                    //String path = getRealPathFromURI(this, uri);

                    Log.d("FILE", "File Path: " + path);
                    int index = path.lastIndexOf('/');
                    Log.d("FILE", "File Path: " + path.substring(index + 1, path.length()));
                    Log.d("FILE", "Index: " + index);
                    String fileName = path.substring(index + 1, path.length());
                    String fileNameFull = path.substring(index + 1, path.length());

                    int extIndex = fileNameFull.lastIndexOf(".");
                    String extension = path.substring(extIndex + 1, fileNameFull.length());

                    Pattern pattern = Pattern.compile("[^A-Za-z0-9]");
                    Matcher matcher = pattern.matcher(String.valueOf(fileName.charAt(0)));

                    //Matcher matcherEnd = pattern.matcher(String.valueOf(fileName.charAt(fileName.length() - 1)));
                    Matcher matcherEnd = pattern.matcher(String.valueOf(fileName.charAt(fileName.lastIndexOf(".") - 1)));

                    if (matcher.find() || matcherEnd.find()) {

                         new KAlertDialog(UploadShopActRegistrationDocActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!")
                                .setContentText("Image must not contain special case character at the start and the end of its name")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog kAlertDialog) {
                                        kAlertDialog.dismiss();
                                    }
                                })
                                .confirmButtonColor(R.drawable.kalert_button_background)
                                .show();


                    } else {

                        if (supportedFileFormats.contains(getFileExtension(fileNameFull))) {

                            if (fileName.length() > 23) {
                                fileName = fileName.substring(0, 23);
                                fileName = fileName + "...";
                            }

                            File imgFile = new File(path);

                            if (imgFile.exists()) {
                                if(doc_type==1) {
                                    imageName_pancard = fileName;
                                    imagePath_pancard = path;
                                    imageExtension_pancard = extension;
                                    imageUploadStatus_pancard = false;
                                    imageDisplayCard_pancard.setVisibility(View.VISIBLE);
                                    imageNameTv_pancard.setText(fileName);
                                    Picasso.get().load(imgFile).into(selectedImage_pancard);
                                    selectedImage_pancard.invalidate();
                                }
                                else if(doc_type==2)
                                {
                                    imageName_aadharcard = fileName;
                                    imagePath_aadharcard = path;
                                    imageExtension_aadharcard = extension;
                                    imageUploadStatus_aadharcard = false;
                                    imageDisplayCard_aadharcard.setVisibility(View.VISIBLE);
                                    imageNameTv_aadharcard.setText(fileName);
                                    Picasso.get().load(imgFile).into(selectedImage_aadharcard);
                                    selectedImage_aadharcard.invalidate();
                                }

                                else if(doc_type==3)
                                {
                                    imageName_photo = fileName;
                                    imagePath_photo = path;
                                    imageExtension_photo = extension;
                                    imageUploadStatus_photo = false;
                                    imageDisplayCard_photo.setVisibility(View.VISIBLE);
                                    imageNameTv_photo.setText(fileName);
                                    Picasso.get().load(imgFile).into(selectedImage_photo);
                                    selectedImage_photo.invalidate();
                                }
                                else if(doc_type==4)
                                {
                                    imageName_lightbill = fileName;
                                    imagePath_lightbill = path;
                                    imageExtension_lightbill = extension;
                                    imageUploadStatus_lightbill = false;
                                    imageDisplayCard_lightbill.setVisibility(View.VISIBLE);
                                    imageNameTv_lightbill.setText(fileName);
                                    Picasso.get().load(imgFile).into(selectedImage_lightbill);
                                    selectedImage_lightbill.invalidate();
                                }
                                else if(doc_type==5)
                                {
                                    imageName_declaration = fileName;
                                    imagePath_declaration = path;
                                    imageExtension_declaration = extension;
                                    imageUploadStatus_declaration = false;
                                    imageDisplayCard_declaration.setVisibility(View.VISIBLE);
                                    imageNameTv_declaration.setText(fileName);
                                    Picasso.get().load(imgFile).into(selectedImage_declaration);
                                    selectedImage_declaration.invalidate();
                                }





                            } else {

                                if (fileName.length() > 15) {
                                    new KAlertDialog(UploadShopActRegistrationDocActivity.this, KAlertDialog.ERROR_TYPE)
                                            .setTitleText("Error!")
                                            .setContentText("File\" + \" \" + fileNameFull.substring(0, 15) + \" \" + \"does not exists!")
                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                @Override
                                                public void onClick(KAlertDialog kAlertDialog) {
                                                    kAlertDialog.dismiss();
                                                }
                                            })
                                            .confirmButtonColor(R.drawable.kalert_button_background)
                                            .show();


                                } else {
                                    new KAlertDialog(UploadShopActRegistrationDocActivity.this, KAlertDialog.ERROR_TYPE)
                                            .setTitleText("Error!")
                                            .setContentText("File\" + \" \" + fileNameFull + \" \" + \"does not exists!")
                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                @Override
                                                public void onClick(KAlertDialog kAlertDialog) {
                                                    kAlertDialog.dismiss();
                                                }
                                            })
                                            .confirmButtonColor(R.drawable.kalert_button_background)
                                            .show();

                                }
                            }


                        } else {
                            new KAlertDialog(UploadShopActRegistrationDocActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setContentText("Image format of this image is not supported!")
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog kAlertDialog) {
                                            kAlertDialog.dismiss();
                                        }
                                    })
                                    .confirmButtonColor(R.drawable.kalert_button_background)
                                    .show();

                        }
                    }
                } catch (Exception use) {
                    use.printStackTrace();
                    Log.d("FILE", "URI Syntax Exception : " + "  " + use);
                }

            } else {

                capturedImageFile = null;
                capturedImageUri = null;
            }

        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    public static String getRealPathFromURI(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public String getFileExtension(String path) {

        String extension = "null";

        extension = FilenameUtils.getExtension(path);

        return extension;
    }

    public String getMimeType(String path) {

        //String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        String extension = getFileExtension(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase().trim());
    }

    private void requestForPermission(int perCode) {

        if (perCode == READ_STORAGE_PER_CODE) {

            ActivityCompat.requestPermissions(UploadShopActRegistrationDocActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, perCode);

        } else if (perCode == WRITE_STORAGE_PER_CODE) {

            ActivityCompat.requestPermissions(UploadShopActRegistrationDocActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, perCode);

        } else if(perCode == CAMERA_CAPTURE_PERMISSION){

            ActivityCompat.requestPermissions(UploadShopActRegistrationDocActivity.this, new String[]{Manifest.permission.CAMERA}, perCode);

        }
    }

    private boolean checkPermissionSts(int perCode) {

        int status = ContextCompat.checkSelfPermission(UploadShopActRegistrationDocActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (status == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkCameraPermission(){
        //CAMERA_CAPTURE_PERMISSION
        int status = ContextCompat.checkSelfPermission(UploadShopActRegistrationDocActivity.this, Manifest.permission.CAMERA);

        if (status == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void showFileChooser() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");

        try {

            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {

            new KAlertDialog(UploadShopActRegistrationDocActivity.this, KAlertDialog.ERROR_TYPE)
                    .setTitleText("Error!")
                    .setContentText("Please install a File Manager!")
                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                        @Override
                        public void onClick(KAlertDialog kAlertDialog) {
                            kAlertDialog.dismiss();
                        }
                    })
                    .confirmButtonColor(R.drawable.kalert_button_background)
                    .show();


        }

    }


    private String getPathFromProviderUri(Uri contentUri){

        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
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
