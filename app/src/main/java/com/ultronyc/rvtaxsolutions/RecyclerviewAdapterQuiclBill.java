package com.ultronyc.rvtaxsolutions;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerviewAdapterQuiclBill extends RecyclerView.Adapter<RecyclerviewAdapterQuiclBill.MyViewHolder>  {

    public Context mContext;

    public List<CategoryQuickBill> mData;


    public RecyclerviewAdapterQuiclBill(Context mContext, List<CategoryQuickBill> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    public RecyclerviewAdapterQuiclBill.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        view = mInflater.inflate(R.layout.cardview_quickbill_category, parent, false);

        return new RecyclerviewAdapterQuiclBill.MyViewHolder(view);
    }


    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.itemType.setText(mData.get(position).getItemType());

        String ti = "Total Items :  " +mData.get(position).getCount();

        holder.count.setText(ti);

        //
        // Picasso.with(mContext).load(mData.get(position).getImageUrl()).into(holder.itemImage);

        Picasso.get().load(mData.get(position).getImageUrl()).into(holder.itemImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // Intent intent = new Intent(mContext, ItemQtyActivity.class);
               // intent.putExtra("itemName", mData.get(position).getItemName());
               // mContext.startActivity(intent);
            }
        });

    }


    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView count;
        ImageView itemImage;
        TextView itemType;
        RelativeLayout relativeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.itemType = (TextView) itemView.findViewById(R.id.itemType_quickbill);
            this.count = (TextView) itemView.findViewById(R.id.countTextView_quickBill);
            this.itemImage = (ImageView) itemView.findViewById(R.id.itemImage_quickBill);
            this.cardView = (CardView) itemView.findViewById(R.id.cardview_quickbill_main_id);
            this.relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_main_quickBill);
        }
    }


}
























