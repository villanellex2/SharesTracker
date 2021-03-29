package com.example.sharestracker.adapters;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharestracker.JSON.FileHandler;
import com.example.sharestracker.R;

import java.util.List;


public class FieldsAdapter extends RecyclerView.Adapter<FieldsAdapter.ViewHolder> {
    Context context;
    List<ShareData> responseList;
    FileHandler handler;

    public FieldsAdapter(Context context, List<ShareData> responseList) {
        this.context = context;
        this.responseList = responseList;
        handler = new FileHandler(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.example_field, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ShareData ticketData = responseList.get(position);
        if (position % 2 == 1) {
            holder.layout.setBackgroundColor(Color.parseColor("#F0F4F7"));
        }
        else {
            holder.layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        holder.picture.setImageDrawable(ticketData.getLogo());
        holder.companyName.setText(ticketData.getCompanyName());
        holder.ticketName.setText(ticketData.getName());
        holder.dayDelta.setText(ticketData.getDayDelta());
        boolean isFavourite = handler.isFavourite(ticketData.getName());
        if (isFavourite) {
            holder.star.setImageDrawable(context.getResources().getDrawable(R.drawable.star));
        }
        else{
            holder.star.setImageDrawable(context.getResources().getDrawable(R.drawable.grey_star));
        }
        if (ticketData.getDayDelta().startsWith("-")) {
            holder.dayDelta.setTextColor(Color.parseColor("#B22424"));
        } else {
            holder.dayDelta.setTextColor(Color.parseColor("#24B25D"));
        }
        holder.currentPrice.setText(ticketData.getCurrentPrice());
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.currentPrice.getLayoutParams();
        params.setMarginEnd(12);
    }


    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView ticketName, companyName, currentPrice, dayDelta;
        final ImageView picture, star;
        final ConstraintLayout layout;

        ViewHolder(View view) {
            super(view);
            picture = view.findViewById(R.id.picture);
            star = view.findViewById(R.id.star);
            ticketName = view.findViewById(R.id.ticketName);
            companyName = view.findViewById(R.id.companyName);
            dayDelta = view.findViewById(R.id.dayDelta);
            layout = view.findViewById(R.id.example_field);
            currentPrice = view.findViewById(R.id.currentPrice);
        }

    }
}