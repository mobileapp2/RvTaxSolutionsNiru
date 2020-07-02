package com.ultronyc.rvtaxsolutions;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentActivity;

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

import java.util.HashMap;
import java.util.Map;

public class DialogAddItem extends AppCompatDialogFragment {

    ImageView close;
    String title;
    TextView titleTextView;

    TextView name, type, brand, group, unit, rate, tax, mrp;

    FragmentActivity activity;
    Context mContext;

    private static String URL_SUBMIT = "http://www.intellyserve.in/android/im_erp/php_files/tire_maintanance/tire_repair_store.php";

    ProgressBar progressBar;
    TextView submit;


    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_additem, null);
        builder.setView(view);

        titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        close = (ImageView) view.findViewById(R.id.closeImageView);

        name = (TextView) view.findViewById(R.id.itemNameEditText);
        type = (TextView) view.findViewById(R.id.itemTypeEditText);
        brand = (TextView) view.findViewById(R.id.itemBrandEditText);
        group = (TextView) view.findViewById(R.id.itemGroupEditText);
        unit = (TextView) view.findViewById(R.id.itemUnitEditText);
        rate = (TextView) view.findViewById(R.id.itemRateEditText);
        tax = (TextView) view.findViewById(R.id.itemTaxEditText);
        mrp = (TextView) view.findViewById(R.id.itemMrpEditText);

        progressBar = (ProgressBar) view.findViewById(R.id.progressSubmit_addItemDialog);
        submit = (TextView) view.findViewById(R.id.submit_addItemTextView);

        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

        try {
            title = getArguments().getString("TITLE");
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

        String str = title;

        if (str != null) {
            titleTextView.setText(str);
        }


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameS = name.getText().toString();
                String typeS = type.getText().toString();
                String brandS = brand.getText().toString();
                String groupS = group.getText().toString();
                String unitS = unit.getText().toString();
                String rateS = rate.getText().toString();
                String taxS = tax.getText().toString();
                String mrpS = mrp.getText().toString();

                if (nameS.matches("")) {
                    name.setError("Enter Name");
                } else if (typeS.matches("")) {
                    type.setError("Enter Type");
                } else if (brandS.matches("")) {
                    brand.setError("Enter Brand");
                } else if (groupS.matches("")) {
                    group.setError("Enter Group");
                } else if (unitS.matches("")) {
                    unit.setError("Enter Unit");
                } else if (rateS.matches("")) {
                    rate.setError("Enter Rate");
                } else if (taxS.matches("")) {
                    tax.setError("Enter Tax");
                } else if (mrpS.matches("")) {
                    mrp.setError("Enter MRP");
                } else {
                    //submit();
                    //Toast.makeText(activity, "Success",Toast.LENGTH_LONG).show();
                    dismiss();
                    new KAlertDialog(activity, KAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success!")
                            .setContentText("Item has been added successfully!")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog kAlertDialog) {
                                    kAlertDialog.dismiss();

                                }
                            })
                            .confirmButtonColor(R.drawable.kalertbutton_background)
                            .show();

                }


            }
        });


        return builder.create();
    }


    private void submit(String nameP, String typeP, String brandP, String groupP, String unitP, String rateP, String taxP, String mrpP) {

        progressBar.setVisibility(View.VISIBLE);
        submit.setVisibility(View.GONE);

        //  subcatdata2.clear();
        //final String compid1 = String.valueOf(comid);

        final String nameStr = nameP;
        final String typeStr = typeP;
        final String brandStr = brandP;
        final String groupStr = groupP;
        final String unitStr = unitP;
        final String rateStr = rateP;
        final String taxStr = taxP;
        final String mrpStr = mrpP;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SUBMIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //    Toast.makeText(getApplicationContext(), "Response12: "+response, Toast.LENGTH_LONG).show();
                        //String j="0";
                        int j = 1;
                        try {
                            //JSONArray ja = new JSONArray(result);
                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;
                            //data = new String[ja.length()];

                            for (int i = 0; i < ja.length(); i++) {
                                jo = ja.getJSONObject(i);
                                j++;

                                //purchaseOrderCode.setText(jo.getString("po_no"));
                                //max_po_cnt = "" + jo.getString("po_max_key");


                            }
                            //max_number=j;
                            //itemNumber=String.valueOf(j);
                            // Toast.makeText(getApplicationContext(), "----------------: "+max_number, Toast.LENGTH_LONG).show();

                            try {
                                //if(!verify) {


                                if (response.isEmpty()) {
                                    // Toast.makeText(getApplicationContext(), "SubCategory arraylist is empty", Toast.LENGTH_LONG).show();
                                } else {
                                    //   Toast.makeText(getApplicationContext(), ""+j, Toast.LENGTH_LONG).show();

                                }
                                //verify = true;
                                //}
                            } catch (Exception e) {
                                Toast.makeText(activity, "" + e, Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                            progressBar.setVisibility(View.GONE);
                            submit.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(activity, "Submit Error! " + e.toString(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            submit.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity, "Submit Error! " + error.toString(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        submit.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Toast.makeText(ItemMaster.this, "niru" + cattid1, Toast.LENGTH_LONG).show();

                params.put("name", nameStr);
                params.put("type", typeStr);
                params.put("brand", brandStr);
                params.put("group", typeStr);
                params.put("unit", unitStr);
                params.put("rate", rateStr);
                params.put("tax", taxStr);
                params.put("mrp", mrpStr);

//                params.put("comp_id", type);
//                params.put("statement", "Select");
//                params.put("fiscyr", "2019");
//                params.put("statement1", "Max");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            activity = (FragmentActivity) context;
        }
    }


}
