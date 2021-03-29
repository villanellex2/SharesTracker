package com.example.sharestracker.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharestracker.JSON.FileHandler;
import com.example.sharestracker.R;
import com.example.sharestracker.adapters.FieldsAdapter;

public class MainActivity extends AppCompatActivity {
    public FieldsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceField) {
        super.onCreate(savedInstanceField);
        setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onResume() {
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeFavourite(View view){
        ConstraintLayout layout = (ConstraintLayout) view.getParent();
        TextView shareNameView = (TextView) layout.getChildAt(0);
        String share = shareNameView.getText().toString();
        FileHandler handler = new FileHandler(this);
        if (handler.isFavourite(share)){
            handler.deleteFromFavourite(share);
        }
        else {
            handler.addToFavorite(share);
        }
        mAdapter.notifyDataSetChanged();
    }
}