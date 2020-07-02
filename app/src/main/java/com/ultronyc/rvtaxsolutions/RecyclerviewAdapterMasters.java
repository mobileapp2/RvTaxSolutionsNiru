package com.ultronyc.rvtaxsolutions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerviewAdapterMasters extends RecyclerView.Adapter<RecyclerviewAdapterMasters.MyViewHolder> {

    
    public Context mContext;

    public List<Category> mData;
    FragmentManager fragmentManager;

    public RecyclerviewAdapterMasters(Context mContext, List<Category> mData, FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.mData = mData;
        this.fragmentManager = fragmentManager;
    }


    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        view = mInflater.inflate(R.layout.cardview_category_masters, parent, false);

        return new RecyclerviewAdapterMasters.MyViewHolder(view);
    }



    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        
        holder.img_category_thumbnail1.setImageResource(mData.get(position).getThumbnail());
        
        holder.tv_category_title.setText(this.mData.get(position).getTitle());
        
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                
                int i = position;

                    try{

//                        Intent mIntent=new Intent(mContext,HomeActivity.class);
//                        mContext.startActivity(mIntent);
//                        ((Activity) mContext).finish();
                       // Toast.makeText(mContext, "Work In Progress", Toast.LENGTH_SHORT).show();
                    String activityName = mData.get(position).getActivityName();
                    Class<?> c = Class.forName(activityName);
                    Intent intent = new Intent(mContext, c);
                    mContext.startActivity(intent);

                    }catch (Exception e){
                        e.printStackTrace();
                        //Toast.makeText(mContext, "startActivity Error"+e, Toast.LENGTH_SHORT).show();
                    }




                /*else if (i == 1) {
                    //RecyclerviewAdapterMasters.this.mContext.startActivity(new Intent(RecyclerviewAdapterMasters.this.mContext, ItemListActivity.class));
                    Intent intent = new Intent(mContext, ItemListActivity.class);
                    mContext.startActivity(intent);
                } else if (i == 2) {
                    //RecyclerviewAdapterMasters.this.mContext.startActivity(new Intent(RecyclerviewAdapterMasters.this.mContext, AddCustomerActivity.class));
                    Intent intent = new Intent(mContext, AddCustomerActivity.class);
                    mContext.startActivity(intent);
                } else if (i == 3) {
                    //RecyclerviewAdapterMasters.this.mContext.startActivity(new Intent(RecyclerviewAdapterMasters.this.mContext, CustomerListActivity.class));
                    Intent intent = new Intent(mContext, CustomerListActivity.class);
                    mContext.startActivity(intent);
                } else if (i == 4) {
                    //RecyclerviewAdapterMasters.this.mContext.startActivity(new Intent(RecyclerviewAdapterMasters.this.mContext, AddUserActivity.class));
                    Intent intent = new Intent(mContext, AddUserActivity.class);
                    mContext.startActivity(intent);
                } else if (i == 5) {
                    //RecyclerviewA/dapterMasters.this.mContext.startActivity(new Intent(RecyclerviewAdapterMasters.this.mContext, UserListActivity.class));
                    Intent intent = new Intent(mContext, UserListActivity.class);
                    mContext.startActivity(intent);
                }*/


            }
        });
    }


    public int getItemCount() {
        return this.mData.size();
    }

    public void openDialog(String title) {
        DialogAddItem dialog = new DialogAddItem();
        Bundle bundle = new Bundle();
        bundle.putString("TITLE", title);
        dialog.setArguments(bundle);
        dialog.show(this.fragmentManager, "Add New Item");
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView img_category_thumbnail1;
        RelativeLayout relativeLayout;
        ImageView rightArrow;
        TextView tv_category_title;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.tv_category_title = (TextView) itemView.findViewById(R.id.all_cat_title_masters);
            this.img_category_thumbnail1 = (ImageView) itemView.findViewById(R.id.all_cat_image_masters);
            this.rightArrow = (ImageView) itemView.findViewById(R.id.rightArrow_icon_image_masters);
            this.cardView = (CardView) itemView.findViewById(R.id.cardview_masters_main_id);
            this.relativeLayout = (RelativeLayout) itemView.findViewById(R.id.category_relative_layout_masters);
        }
    }
}
