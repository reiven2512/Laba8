package com.example.laba_map;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    List<LatLng> lt;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        lt = new ArrayList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(15.0f);
        mMap.setMaxZoomPreference(20.0f);
        // Add a marker in Sydney and move the camera
        double lat = getIntent().getExtras().getDouble("FromLat");
        double lng = getIntent().getExtras().getDouble("FromLng");
        Log.v("text_of_mine", Double.toString(lat));
        Log.v("text_of_mine", Double.toString(lng));
        LatLng first = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(first).title("From"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(first));
        lat = getIntent().getExtras().getDouble("ToLat");
        lng = getIntent().getExtras().getDouble("ToLng");
        Log.v("text_of_mine", Double.toString(lat));
        Log.v("text_of_mine", Double.toString(lng));
        LatLng last = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(last).title("To"));
        final String url = "https://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + first.latitude + "," + first.longitude
                + "&destination=" + last.latitude + "," + last.longitude
                + "&sensor=false&units=metric"
                + "&mode=driving"
                + "&key=MyKey";
        download(url);
    }

    public void download(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("text_of_mine", "1toString()");
                        try {
                            JSONArray routes = response.getJSONArray("routes");
                            for (int i = 0; i < routes.length(); i++){
                                Log.v("text_of_mine", "2toString()");
                                JSONObject object_0 = routes.getJSONObject(i);
                                JSONArray legs = object_0.getJSONArray("legs");
                                JSONObject tmp = legs.getJSONObject(i);
                                JSONArray steps = tmp.getJSONArray("steps");
                                for (int j = 0; j < steps.length(); j++){
                                    Log.v("text_of_mine", "3toString()");
                                    JSONObject object = steps.getJSONObject(j);
                                    if(j == 0){
                                        JSONObject start = object.getJSONObject("start_location");
                                        lt.add(new LatLng(Double.parseDouble(start.getString("lat")), Double.parseDouble(start.getString("lng"))));
                                        Log.v("text_of_mine", "start lat " + start.getString("lat"));
                                        Log.v("text_of_mine", "start lng " + start.getString("lng"));
                                    }
                                    JSONObject end = object.getJSONObject("end_location");
                                    lt.add(new LatLng(Double.parseDouble(end.getString("lat")), Double.parseDouble(end.getString("lng"))));
                                    Log.v("text_of_mine", "end lat " + end.getString("lat"));
                                    Log.v("text_of_mine", "end lng " + end.getString("lng"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setMap();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }
    public void setMap(){
        for (int i = 0; i < lt.size() - 1; i++) {
            LatLng src = lt.get(i);
            LatLng dest = lt.get(i + 1);
            Polyline line = mMap.addPolyline(
                    new PolylineOptions().add(
                            new LatLng(src.latitude, src.longitude),
                            new LatLng(dest.latitude,dest.longitude)
                    ).width(4).color(Color.BLUE).geodesic(true)
            );
        }
    }
}
