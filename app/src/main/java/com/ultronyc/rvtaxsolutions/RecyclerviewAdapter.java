package com.ultronyc.rvtaxsolutions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.MyViewHolder> {

    FragmentManager fragmentManager;

    public Context mContext;

    public List<Category> mData;

    public RecyclerviewAdapter(Context mContext, List<Category> mData, FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.mData = mData;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        view = mInflater.inflate(R.layout.cardview_allitem_category, parent, false);

        return new MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {


        holder.tv_category_title.setText(mData.get(position).getTitle());
        holder.img_category_thumbnail.setImageResource(mData.get(position).getThumbnail());
        holder.subtitle.setText(mData.get(position).getCategoryId());


         if (position == 0) {
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#412258"));
            holder.subtitle.setTypeface(null, Typeface.BOLD);
            holder.img_category_thumbnail.setColorFilter(ContextCompat.getColor(mContext, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            //holder.subtitle.setTypeface(null, 1);
        } else if (position == 1) {
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#f28381"));
            holder.subtitle.setTypeface(null, Typeface.BOLD);
            holder.img_category_thumbnail.setColorFilter(ContextCompat.getColor(mContext, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            //holder.subtitle.setTypeface(null, 1);
        } else if (position == 2) {
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#f0649e"));
            holder.subtitle.setTypeface(null, Typeface.BOLD);
            holder.img_category_thumbnail.setColorFilter(ContextCompat.getColor(mContext, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            //holder.subtitle.setTypeface(null, 1);
        } else if (position == 3) {
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#7f6baf"));
            holder.subtitle.setTypeface(null, Typeface.BOLD);
            holder.img_category_thumbnail.setColorFilter(ContextCompat.getColor(mContext, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            //holder.subtitle.setTypeface(null, 1);
        } else if (position == 4) {
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#1287A5"));
            holder.subtitle.setTypeface(null, Typeface.BOLD);
            holder.img_category_thumbnail.setColorFilter(ContextCompat.getColor(mContext, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            //holder.subtitle.setTypeface(null, 1);
        }else if (position == 5) {
             holder.relativeLayout.setBackgroundColor(Color.parseColor("#7ddb8a"));
             holder.subtitle.setTypeface(null, Typeface.BOLD);
             holder.img_category_thumbnail.setColorFilter(ContextCompat.getColor(mContext, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
             //holder.subtitle.setTypeface(null, 1);
         }else if (position == 6) {
             holder.relativeLayout.setBackgroundColor(Color.parseColor("#ad91fa"));
             holder.subtitle.setTypeface(null, Typeface.BOLD);
             holder.img_category_thumbnail.setColorFilter(ContextCompat.getColor(mContext, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
             //holder.subtitle.setTypeface(null, 1);
         }else if (position == 7) {
             holder.relativeLayout.setBackgroundColor(Color.parseColor("#faa057"));
             holder.subtitle.setTypeface(null, Typeface.BOLD);
             holder.img_category_thumbnail.setColorFilter(ContextCompat.getColor(mContext, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
             //holder.subtitle.setTypeface(null, 1);
         }else if (position == 8) {
             holder.relativeLayout.setBackgroundColor(Color.parseColor("#aaa057"));
             holder.subtitle.setTypeface(null, Typeface.BOLD);
             holder.img_category_thumbnail.setColorFilter(ContextCompat.getColor(mContext, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
             //holder.subtitle.setTypeface(null, 1);
         }





        if (mData.get(position).getTitle().matches("")) {
            holder.tv_category_title.setVisibility(View.GONE);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    //Intent intent = new Intent(mContext, )
                    //mContext.startActivity(new Intent(mContext, mData.get(position)).getActivityName())));

                    String activityName = mData.get(position).getActivityName();
                    Class<?> c = Class.forName(activityName);
                    Intent intent = new Intent(mContext, c);

                    //String module = mData.get(position).getModuleId();
                    //String acttname = mData.get(position).getActivityNM();

                    //intent.putExtra("MODULE_ID", module);
                    //intent.putExtra("TITLE", acttname);
                    mContext.startActivity(intent);

                } catch (ClassNotFoundException cnfe) {

                    Toast.makeText(mContext, "Exception"+ " "+cnfe, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void openDialog() {
    }

    public int getItemCount() {
        return this.mData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView img_category_thumbnail;
        RelativeLayout relativeLayout;
        TextView subtitle;
        TextView tv_category_title;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.tv_category_title = (TextView) itemView.findViewById(R.id.all_cat_title);
            this.img_category_thumbnail = (ImageView) itemView.findViewById(R.id.all_cat_image);
            this.subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            this.cardView = (CardView) itemView.findViewById(R.id.all_cardview_main_id);
            this.relativeLayout = (RelativeLayout) itemView.findViewById(R.id.category_relative_layout);
        }
    }


}
