package com.example.sharestracker.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharestracker.File.FavouriteStorage;
import com.example.sharestracker.R;
import com.example.sharestracker.adapters.ShareFieldsAdapter;
import com.example.sharestracker.adapters.ShareData;
import com.example.sharestracker.connection.SharesInitializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Favourite extends Fragment {
    private final List<ShareData> list = Collections.synchronizedList(new ArrayList<>());
    private RecyclerView mRecyclerView;
    private ShareFieldsAdapter mAdapter;
    private View animation;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View curr = inflater.inflate(R.layout.fragment_favourite, container, false);
        mRecyclerView = curr.findViewById(R.id.recyclerView);
        animation =curr.findViewById(R.id.loading_animation);
        list.clear();
        mAdapter = new ShareFieldsAdapter(getContext(), list);
        ((MainActivity)getContext()).mAdapter = mAdapter;
        buildRecyclerView(curr);
        return curr;
    }

    private void buildRecyclerView(View view) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) mLayoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        List<String> sharesNames = FavouriteStorage.getFavoritesList(getContext());
        SharesInitializer initializer = new SharesInitializer(getContext(), sharesNames, list, mAdapter, animation);
        initializer.fillSharesFiled();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.textStocks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Favourite.this)
                        .navigate(R.id.action_Favourite_to_Stock);
            }
        });
        view.findViewById(R.id.editText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController nav = NavHostFragment.findNavController(Favourite.this);
                nav.navigate(R.id.action_global_SearchFragment);
            }
        });
    }
}