package com.example.sharestracker.view;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.sharestracker.R;
import com.example.sharestracker.adapters.ShareFieldsAdapter;
import com.example.sharestracker.adapters.ShareData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchWelcome  extends Fragment {
    private RecyclerView mRecyclerView;
    private ShareFieldsAdapter mAdapter;
    private final List<ShareData> states = Collections.synchronizedList(new ArrayList<>());

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View curr = inflater.inflate(R.layout.last_searched_fragment, container, false);
        mRecyclerView = curr.findViewById(R.id.recyclerView);
        mAdapter = new ShareFieldsAdapter(getContext(), states);

        buildRecyclerView(curr);
        EditText search = curr.findViewById(R.id.editText);
        search.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                try {
                    NavController nav = NavHostFragment.findNavController(SearchWelcome.this);
                    Bundle bundle = new Bundle();
                    bundle.putString("input", search.getText().toString());
                    nav.navigate(R.id.SearchFragment, bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        });
        return curr;
    }


    private void buildRecyclerView(View view) {
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, OrientationHelper.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}