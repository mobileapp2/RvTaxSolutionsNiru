package com.ultronyc.rvtaxsolutions.Udyog_adhar_registration;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.ultronyc.rvtaxsolutions.R;

public class DialogUdyogAdharDocument extends AppCompatDialogFragment {

    ImageView close;
    String title;
    TextView titleTextView,close1;

    FragmentActivity activity;
    TextView submit;

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_udyogadhardocument, null);
        builder.setView(view);

        close = (ImageView) view.findViewById(R.id.UDYOGcloseImageView);
       close1 = (TextView) view.findViewById(R.id.UDYOGclose1);

        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });
        close1.setOnClickListener(new View.OnClickListener() {
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



        return builder.create();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            activity = (FragmentActivity) context;
        }
    }










}
































