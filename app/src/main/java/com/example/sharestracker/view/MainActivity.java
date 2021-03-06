package com.example.sharestracker.view;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharestracker.File.FavouriteStorage;
import com.example.sharestracker.R;
import com.example.sharestracker.adapters.ShareFieldsAdapter;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public ShareFieldsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceField) {
        super.onCreate(savedInstanceField);
        setContentView(R.layout.activity_main);
        trimCache();
    }


    public boolean trimCache() {
        try {
            deleteDir(getCacheDir());
            return deleteDir(getDataDir());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else {
            return false;
        }
    }


    public void onResume() {
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.lastSearchedRecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        Log.println(Log.INFO, "cache trimmed: ", String.valueOf(trimCache()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        trimCache();
        return super.onOptionsItemSelected(item);
    }

    public void changeFavourite(View view){
        ConstraintLayout layout = (ConstraintLayout) view.getParent();
        TextView shareNameView = (TextView) layout.getChildAt(0);
        String share = shareNameView.getText().toString();
        if (FavouriteStorage.isFavourite(this,share)){
            FavouriteStorage.deleteFromFavourite(this, share);
        }
        else {
            FavouriteStorage.addToFavorite(this, share);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void getRequest(View view){
        NavController nav = Navigation.findNavController(this, R.id.last_searched_fragment);
        Bundle bundle = new Bundle();
        bundle.putString("input", ((TextView)view).getText().toString());
        nav.navigate(R.id.SearchFragment, bundle);
    }
}