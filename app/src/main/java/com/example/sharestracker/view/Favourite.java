package com.example.sharestracker.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharestracker.connection.FileHandler;
import com.example.sharestracker.R;
import com.example.sharestracker.adapters.FieldsAdapter;
import com.example.sharestracker.adapters.ShareData;
import com.example.sharestracker.connection.SharesInitializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Favourite extends Fragment {
    private List<ShareData> list;
    private FieldsAdapter mAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View curr = inflater.inflate(R.layout.fragment_favourite, container, false);
        mAdapter = ((MainActivity)getContext()).mAdapter;
        list = ((MainActivity)getContext()).dataList;
        list.clear();
        buildRecyclerView(curr);
        return curr;
    }

    private void buildRecyclerView(View view) {
        FileHandler handler = new FileHandler(getContext());
        List<String> sharesNames = handler.getFavoritesList();
        SharesInitializer initializer = new SharesInitializer(getContext(), sharesNames,
                getResources(), list, mAdapter);
        initializer.fillSharesFiled();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.stock2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Favourite.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }
}