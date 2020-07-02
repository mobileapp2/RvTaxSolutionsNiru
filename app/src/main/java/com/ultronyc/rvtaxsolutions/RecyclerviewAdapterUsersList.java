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

import java.util.List;

public class RecyclerviewAdapterUsersList extends RecyclerView.Adapter<RecyclerviewAdapterUsersList.MyViewHolder>  {


    public Context mContext;
    private List<CategoryUserList> mData;

    public RecyclerviewAdapterUsersList(Context mContext, List<CategoryUserList> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    public RecyclerviewAdapterUsersList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        view = mInflater.inflate(R.layout.cardview_category_user_list, parent, false);

        return new RecyclerviewAdapterUsersList.MyViewHolder(view);
    }


    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        
        holder.name.setText(mData.get(position).getName());
        holder.email.setText(mData.get(position).getEmail());
        holder.mobile.setText(mData.get(position).getMobile());
        holder.address.setText(mData.get(position).getAddress());
        
        
        holder.updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

               // Intent intent = new Intent(mContext, UpdateUserProfileActivity.class);
              //  mContext.startActivity(intent);
            }
        });


        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
    }

    public int getItemCount() {
        return mData.size();
    }

    public void updateList(List<CategoryUserList> list) {
        mData = list;
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView address;
        TextView deleteButton;
        TextView email;
        TextView mobile;
        TextView name;
        RelativeLayout relativeLayout;
        TextView updateButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name_userlist);
            email = (TextView) itemView.findViewById(R.id.email_usrlist);
            mobile = (TextView) itemView.findViewById(R.id.mobile_userlist);
            address = (TextView) itemView.findViewById(R.id.address_userlist);
            updateButton = (TextView) itemView.findViewById(R.id.updateButtonTextview_userlist);
            deleteButton = (TextView) itemView.findViewById(R.id.deleteButtonTextview_userlist);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.category_relative_layout_masters);
        }
    }


}
