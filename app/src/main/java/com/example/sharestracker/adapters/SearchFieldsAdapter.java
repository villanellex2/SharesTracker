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

import com.example.sharestracker.File.FavouriteStorage;
import com.example.sharestracker.R;
import com.example.sharestracker.File.FileHandler;

import java.util.List;

public class SearchFieldsAdapter extends RecyclerView.Adapter<SearchFieldsAdapter.ViewHolder> {
    Context context;
    List<String> responseList;
    FileHandler handler;

    public SearchFieldsAdapter(Context context, List<String> responseList) {
        this.context = context;
        this.responseList = responseList;
        handler = new FileHandler(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.text_to_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String request = responseList.get(position);
        holder.textView.setText(request);
    }


    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView textView;

        ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.searchTextView);
        }

    }
}
