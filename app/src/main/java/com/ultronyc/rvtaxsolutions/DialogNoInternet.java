package com.ultronyc.rvtaxsolutions;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

public class DialogNoInternet extends AppCompatDialogFragment implements InternetConnectivityListener {


    Activity activity;

    InternetAvailabilityChecker mInternetAvailabilityChecker;

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.nointernet_dialog_layout, null);
        builder.setView(view);

        InternetAvailabilityChecker.init(activity);

        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        mInternetAvailabilityChecker.addInternetConnectivityListener(this);







        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            activity = (FragmentActivity) context;
        }
    }


    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        //Toast.makeText(activity, "Network Status = " + " " + isConnected, Toast.LENGTH_LONG).show();
        if(isConnected){
            dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mInternetAvailabilityChecker
                .removeInternetConnectivityChangeListener(this);
    }

}
