package com.ultronyc.rvtaxsolutions;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerviewAdapterCustList  extends RecyclerView.Adapter<RecyclerviewAdapterCustList.MyViewHolder>  {

    public Context mContext;
    private List<CategoryCustList> mData;

    public RecyclerviewAdapterCustList(Context mContext, List<CategoryCustList> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    public RecyclerviewAdapterCustList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        view = mInflater.inflate(R.layout.cardview_category_custlist, parent, false);

        return new RecyclerviewAdapterCustList.MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.custFullName.setText(mData.get(position).getCustFullName());
        holder.custMobile.setText(mData.get(position).getCustMobile());
        holder.custEmail.setText(mData.get(position).getCustEmail());
        holder.custAddress.setText(mData.get(position).getCustAddress());
        holder.custBirthDate.setText(mData.get(position).getCustBirthDate());
        holder.custAnniversaryDate.setText(mData.get(position).getCustAnniversaryDate());
        
        holder.update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // Intent intent = new Intent(mContext, AddCustomerActivity.class);
               // String str = "";
              //  intent.putExtra(str, str);
             //   mContext.startActivity(intent);
            }
        });
        
        
        holder.delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });
        
        
    }

    public int getItemCount() {
        return mData.size();
    }

    public void updateList(List<CategoryCustList> list) {
        mData = list;
        notifyDataSetChanged();
    }
    

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        
        CardView cardView;
        TextView custAddress;
        TextView custAnniversaryDate;
        TextView custBirthDate;
        TextView custEmail;
        TextView custFullName;
        TextView custMobile;
        TextView delete;
        LinearLayout linearLayout;
        TextView update;

        public MyViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardview_custlist_main_id);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.mainLinearLayout_custlist);
            custFullName = (TextView) itemView.findViewById(R.id.fullNameTextView_custlist);
            custMobile = (TextView) itemView.findViewById(R.id.mobileNoTextView_custlist);
            custEmail = (TextView) itemView.findViewById(R.id.emailTextView_custlist);
            custAddress = (TextView) itemView.findViewById(R.id.addressTextView_custlist);
            custBirthDate = (TextView) itemView.findViewById(R.id.dateOfBirthTextView_custlist);
            custAnniversaryDate = (TextView) itemView.findViewById(R.id.dateOfAnniversaryTextView_custlist);
            update = (TextView) itemView.findViewById(R.id.updateCustomerInfoTextView_custlist);
            delete = (TextView) itemView.findViewById(R.id.deleteCustomerInfoTextView_custlist);
        }
    }



}
