package com.ultronyc.rvtaxsolutions;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ultronyc.rvtaxsolutions.Itr_filling.UploadSalaryReturnDocActivity;

import java.util.List;

public class RecyclerviewAdapterPaymentsList extends RecyclerView.Adapter<RecyclerviewAdapterPaymentsList.MyViewHolder>  {


    public Context mContext;
    private List<CategoryPaymentList> mData;

    public RecyclerviewAdapterPaymentsList(Context mContext, List<CategoryPaymentList> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    public RecyclerviewAdapterPaymentsList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        view = mInflater.inflate(R.layout.cardview_category_payment_list, parent, false);

        return new RecyclerviewAdapterPaymentsList.MyViewHolder(view);
    }


    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        
        holder.name.setText(mData.get(position).getName());
        holder.date.setText(mData.get(position).getDate());
        holder.amt.setText(mData.get(position).getAmt());
        holder.paymentid.setText(mData.get(position).getPaymentid());
        holder.transactionid.setText(mData.get(position).getTransactionid());
        holder.processid.setText(mData.get(position).getProcessid());
        holder.status.setText(mData.get(position).getStatus());

        if(holder.status.getText().toString().matches("Transaction Pending"))
        {
            holder.deleteButton.setEnabled(true);
        }else if(holder.status.getText().toString().matches("Transaction Fail/Cancel"))
        {
            holder.deleteButton.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.deleteButton.setVisibility(View.GONE);
        }
        holder.updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
       String status_temp="";
                if(holder.status.getText().toString().matches("Transaction Pending"))
                {
                    status_temp="fail";
                }
                else if(holder.status.getText().toString().matches("Transaction Fail/Cancel"))
                {
                    status_temp="fail";
                }
                else if(holder.status.getText().toString().matches("Transaction Completed"))
                {
                    status_temp="success";
                }
                Intent intent = new Intent(mContext, PaymentStatusActivity.class);
                intent.putExtra("tran_id", holder.processid.getText().toString());
                intent.putExtra("status",""+status_temp);
                intent.putExtra("payment_id",""+holder.paymentid.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });


        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
String amt=holder.amt.getText().toString();
amt=amt.replace("/-", "");
                Intent intent = new Intent(mContext, paymentMainactivity.class);
                intent.putExtra("EMAIL_ID", "");
                intent.putExtra("tran_id", holder.processid.getText().toString());
                intent.putExtra("CUSTOMER_NUMBER", "");
                intent.putExtra("AMOUNT", amt);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return mData.size();
    }

    public void updateList(List<CategoryPaymentList> list) {
        mData = list;
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView deleteButton;
        TextView date;
        TextView amt;
        TextView paymentid;
        TextView transactionid;
        TextView processid;
        TextView status;
        RelativeLayout relativeLayout;
        TextView updateButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name_userlist);
            date = (TextView) itemView.findViewById(R.id.date_userlist);
            amt = (TextView) itemView.findViewById(R.id.amt_userlist);
            paymentid = (TextView) itemView.findViewById(R.id.paymentid_userlist);
            transactionid = (TextView) itemView.findViewById(R.id.transactionid_userlist);
            processid = (TextView) itemView.findViewById(R.id.processid_userlist);
            status = (TextView) itemView.findViewById(R.id.status_userlist);
            updateButton = (TextView) itemView.findViewById(R.id.reciptButtonTextview_userlist);
            deleteButton = (TextView) itemView.findViewById(R.id.payButtonTextview_userlist);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.category_relative_layout_masters);
        }
    }


}
