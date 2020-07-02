package com.ultronyc.rvtaxsolutions;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerviewAdapterItemList extends RecyclerView.Adapter<RecyclerviewAdapterItemList.MyViewHolder> {


    Context mContext;
    List<CategoryItemList> mData;
    FragmentManager fragmentManager;

    public RecyclerviewAdapterItemList(Context mContext, List<CategoryItemList> mData, FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.mData = mData;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public RecyclerviewAdapterItemList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        view = mInflater.inflate(R.layout.cardview_itemlist_category, parent, false);

        return new RecyclerviewAdapterItemList.MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    public int getItemCount() {
        return this.mData.size();
    }

    public void openDialog() {
        DialogAddItem dialog = new DialogAddItem();
        dialog.setArguments(new Bundle());
        dialog.show(this.fragmentManager, "Add New Item");
    }

    public void updateList(List<CategoryItemList> list) {
        mData = list;
        notifyDataSetChanged();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {
        
        TextView brand;
        CardView cardView;
        Button delete;
        TextView group;
        TextView itemName;
        TextView mrp;
        TextView rate;
        RelativeLayout relativeLayout;
        TextView tax;
        TextView type;
        TextView unit;
        Button update;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.cardView = (CardView) itemView.findViewById(R.id.all_cardview_main_id);
            this.relativeLayout = (RelativeLayout) itemView.findViewById(R.id.category_relative_layout);
            this.itemName = (TextView) itemView.findViewById(R.id.ittemNameValue_itemlist);
            this.brand = (TextView) itemView.findViewById(R.id.brandValue_itemlist);
            this.unit = (TextView) itemView.findViewById(R.id.unitValue_itemlist);
            this.tax = (TextView) itemView.findViewById(R.id.taxValue_itemlist);
            this.type = (TextView) itemView.findViewById(R.id.typeValue_itemlist);
            this.group = (TextView) itemView.findViewById(R.id.groupValue_itemlist);
            this.rate = (TextView) itemView.findViewById(R.id.rateValue_itemlist);
            this.mrp = (TextView) itemView.findViewById(R.id.mrpValue_itemlist);
            this.update = (Button) itemView.findViewById(R.id.updateButton_itemList);
            this.delete = (Button) itemView.findViewById(R.id.deleteButton_itemList);
        }
    }

    
}





















