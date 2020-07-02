package com.ultronyc.rvtaxsolutions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerviewAdapterItemType extends RecyclerView.Adapter<RecyclerviewAdapterItemType.MyViewHolder>   {

    int count = 0;

    public Context mContext;

    public List<CategoryQuickBill> mData;

    float price = 0.0f;
    float total = 0.0f;


    public RecyclerviewAdapterItemType(Context mContext, List<CategoryQuickBill> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        view = mInflater.inflate(R.layout.category_item_type, parent, false);

        return new MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        
        final MyViewHolder viewHolder = holder;
        
        viewHolder.itemType.setText(mData.get(position).getItemName());
        viewHolder.price.setText(mData.get(position).getItemMRP());

        viewHolder.plus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                count = 0;
                total = 0.0f;
                count = Integer.parseInt(mData.get(position).getCount());
                count++;

                mData.get(position).setCount(String.valueOf(count));
                viewHolder.count.setText(mData.get(position).getCount());

                price = Float.parseFloat(mData.get(position).getItemMRP());


                total = Float.parseFloat(mData.get(position).getAmount());
                total += price;
                mData.get(position).setAmount(String.valueOf(total));


                String amount = "Total :  " + mData.get(position).getAmount();

                Toast.makeText(mContext, "Amount = " + amount, Toast.LENGTH_LONG).show();
                viewHolder.total.setText(amount);
            }
        });

        viewHolder.minus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                
                count = 0;
                total = 0.0f;
                count = Integer.parseInt(mData.get(position).getCount());
                
                total = Float.parseFloat(mData.get(position).getItemMRP());
                
                total = Float.parseFloat(mData.get(position).getItemMRP());
                viewHolder.price.setText(String.valueOf(total));
                count--;
                mData.get(position).setCount(String.valueOf(count));
                holder.count.setText(mData.get(position).getCount());
                mData.get(position).setAmount(String.valueOf(total));
            }
        });
    }

    public int getItemCount() {
        return mData.size();
    }

    public int getItemViewType(int position) {
        return position;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        
        CardView cardView;
        CheckBox checkBox;
        TextView count;
        ImageView itemImage;
        TextView itemType;
        TextView minus;
        TextView plus;
        TextView price;
        RelativeLayout relativeLayout;
        TextView total;

        public MyViewHolder(View itemView) {
            super(itemView);

            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox_itemQty);
            itemType = (TextView) itemView.findViewById(R.id.itemType_name);
            count = (TextView) itemView.findViewById(R.id.quantityCount_itemQty);
            total = (TextView) itemView.findViewById(R.id.total_itemQty);
            price = (TextView) itemView.findViewById(R.id.price_itemQty);
            plus = (TextView) itemView.findViewById(R.id.plus_itemQty);
            minus = (TextView) itemView.findViewById(R.id.minus_itemQty);
            itemImage = (ImageView) itemView.findViewById(R.id.itemImage_quickBill);
            cardView = (CardView) itemView.findViewById(R.id.cardview_quickbill_main_id);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_main_quickBill);
        }
    }
    
    
    
    
    
}






















