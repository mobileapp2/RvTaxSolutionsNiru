package com.ultronyc.rvtaxsolutions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerviewAdapterSettings extends RecyclerView.Adapter<RecyclerviewAdapterSettings.MyViewHolder> {

    public Context mContext;
    public List<CategorySettings> mData;
    FragmentManager fragmentManager;

    public RecyclerviewAdapterSettings(Context mContext, List<CategorySettings> mData, FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.mData = mData;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public RecyclerviewAdapterSettings.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        view = mInflater.inflate(R.layout.cardview_category_settings, parent, false);

        return new RecyclerviewAdapterSettings.MyViewHolder(view);
    }


    public void onBindViewHolder(@NonNull RecyclerviewAdapterSettings.MyViewHolder holder, final int position) {

        holder.setting_icon.setImageResource(mData.get(position).getImageRes());

        holder.title.setText(mData.get(position).getName());

        String check = mData.get(position).getIsChecked();

        holder.setting_icon.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);

        if(check.matches("")){
            holder.aSwitch.setVisibility(View.GONE);
        }

        if(check.equals("true")) {
            holder.aSwitch.setChecked(true);
        } else {
            holder.aSwitch.setChecked(false);
        }

    }

    public void openDialog() {
    }

    public int getItemCount() {
        return this.mData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        RelativeLayout relativeLayout;

        ImageView setting_icon;
        TextView title;
        Switch aSwitch;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.cardview_main_id_settings);
            this.relativeLayout = (RelativeLayout) itemView.findViewById(R.id.category_relative_layout_settings);

            this.setting_icon = (ImageView) itemView.findViewById(R.id.image_settings);
            this.title = (TextView) itemView.findViewById(R.id.title_settings);
            this.aSwitch = (Switch) itemView.findViewById(R.id.switch_settings);


        }
    }

}

















