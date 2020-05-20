package com.example.laba_map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    DelayAutoCompleteTextView bookTitle;
    DelayAutoCompleteTextView bookTitle2;
    double from_lat;
    double from_lng;
    double to_lat;
    double to_lng;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        bookTitle = findViewById(R.id.title);
        bookTitle.setThreshold(8);
        bookTitle.setAdapter(new MapAdapter(this));
        bookTitle.setLoadingIndicator((ProgressBar) findViewById(R.id.progress_bar));
        bookTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Place place = (Place) adapterView.getItemAtPosition(position);
                bookTitle.setText(place.getName());
                from_lat = place.getLat();
                from_lng = place.getLng();
                Log.v("text_of_mine", Double.toString(from_lat));
                Log.v("text_of_mine", Double.toString(from_lng));
            }
        });
        bookTitle2 = findViewById(R.id.title2);
        bookTitle2.setThreshold(8);
        bookTitle2.setAdapter(new MapAdapter(this));
        bookTitle2.setLoadingIndicator((ProgressBar) findViewById(R.id.progress_bar2));
        bookTitle2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Place place = (Place) adapterView.getItemAtPosition(position);
                bookTitle2.setText(place.getName());
                to_lat = place.getLat();
                to_lng = place.getLng();
                Log.v("text_of_mine", Double.toString(to_lat));
                Log.v("text_of_mine", Double.toString(to_lng));
            }
        });
        Button bt = findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MapsActivity.class);
                intent.putExtra("FromLat", from_lat);
                intent.putExtra("FromLng", from_lng);
                intent.putExtra("ToLat", to_lat);
                intent.putExtra("ToLng", to_lng);
                startActivity(intent);
            }
        });
    }
}
