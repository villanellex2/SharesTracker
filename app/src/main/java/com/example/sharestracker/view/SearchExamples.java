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

import com.example.sharestracker.File.LastSearchedStorage;
import com.example.sharestracker.R;
import com.example.sharestracker.adapters.SearchFieldsAdapter;

import java.util.Arrays;
import java.util.List;

public class SearchExamples extends Fragment {
    private RecyclerView mRecyclerViewLast;
    private RecyclerView mRecyclerViewPopular;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View curr = inflater.inflate(R.layout.last_searched_fragment, container, false);
        mRecyclerViewLast = curr.findViewById(R.id.lastSearchedRecyclerView);
        mRecyclerViewPopular = curr.findViewById(R.id.popularRecyclerView);

        buildRecyclerView(curr);
        EditText search = curr.findViewById(R.id.editText);
        search.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                try {
                    NavController nav = NavHostFragment.findNavController(SearchExamples.this);
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
        mRecyclerViewLast.setLayoutManager(mLayoutManager);
        mRecyclerViewLast.setAdapter(new SearchFieldsAdapter(getContext(), LastSearchedStorage.getLastSearched(getContext())));
        mLayoutManager = new StaggeredGridLayoutManager(2, OrientationHelper.HORIZONTAL);
        mRecyclerViewPopular.setLayoutManager(mLayoutManager);
        List<String> popular = Arrays.asList("Amazon", "Apple", "Google", "Tesla", "Alibaba", "Yandex", "Facebook", "First Solar", "Microsoft", "Visa");
        mRecyclerViewPopular.setAdapter(new SearchFieldsAdapter(getContext(), popular));
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}