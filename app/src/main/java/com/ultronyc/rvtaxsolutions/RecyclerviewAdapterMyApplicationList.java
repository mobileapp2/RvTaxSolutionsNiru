package com.ultronyc.rvtaxsolutions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerviewAdapterMyApplicationList extends RecyclerView.Adapter<RecyclerviewAdapterMyApplicationList.MyViewHolder>  {


    public Context mContext;
    private List<CategoryMyApplicationList> mData;

    public RecyclerviewAdapterMyApplicationList(Context mContext, List<CategoryMyApplicationList> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    public RecyclerviewAdapterMyApplicationList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        view = mInflater.inflate(R.layout.cardview_category_my_application, parent, false);

        return new RecyclerviewAdapterMyApplicationList.MyViewHolder(view);
    }


    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        
        holder.name.setText(mData.get(position).getName());
        holder.date.setText(mData.get(position).getDate());
        holder.amt.setText(mData.get(position).getAmt());
        holder.paymentid.setText(mData.get(position).getPaymentid());
        holder.transactionid.setText(mData.get(position).getTransactionid());
        holder.processid.setText(mData.get(position).getProcessid());
        holder.status.setText(mData.get(position).getStatus());
        holder.replay_msg.setText(mData.get(position).getReplayMsg());
        String filename=""+mData.get(position).getProcessid()+"."+mData.get(position).getReplayDocType();
        holder.replay_doc.setText(filename);


        holder.replay_doc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                    Log.d("aaaaaaaa","2");

              //  ((MyApplicationActivity)mContext).niru();
                ((MyApplicationActivity)mContext).downloadFiles(""+mData.get(position).getProcessid(),""+mData.get(position).getReplayDoc(),""+mData.get(position).getReplayDocType());

            }
        });



    }


    public int getItemCount() {
        return mData.size();
    }

    public void updateLista(List<CategoryMyApplicationList> list) {
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
        TextView replay_msg;
        TextView replay_doc;
        RelativeLayout relativeLayout;
        TextView updateButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name_userlist_myapllication);
            date = (TextView) itemView.findViewById(R.id.date_userlist_myapllication);
            amt = (TextView) itemView.findViewById(R.id.amt_userlist_myapllication);
            paymentid = (TextView) itemView.findViewById(R.id.paymentid_userlist_myapllication);
            transactionid = (TextView) itemView.findViewById(R.id.transactionid_userlist_myapllication);
            processid = (TextView) itemView.findViewById(R.id.processid_userlist_myapllication);
            status = (TextView) itemView.findViewById(R.id.status_userlist_myapllication);
            replay_msg = (TextView) itemView.findViewById(R.id.replay_msg_userlist_myapllication);
            replay_doc = (TextView) itemView.findViewById(R.id.replay_doc_userlist_myapllication);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.category_relative_layout_masters);
        }
    }


}
