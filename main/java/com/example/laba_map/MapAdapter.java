package com.example.laba_map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapAdapter extends BaseAdapter implements Filterable {

    private static final int MAX_RESULTS = 10;

    private final Context mContext;
    private List<Place> mResults;
    private List<Place> tmp;
    private boolean isCompleted;

    public MapAdapter(Context context) {
        mContext = context;
        mResults = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public Place getItem(int index) {
        return mResults.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item, parent, false);
        }
        Place place = getItem(position);
        ((TextView) convertView.findViewById(R.id.text1)).setText(place.getName());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    findBooks(constraint.toString());
                    isCompleted = false;
                    while(!isCompleted)
                    // Assign the data to the FilterResults
                    filterResults.values = tmp;
                    filterResults.count = tmp.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    mResults = (List<Place>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    /**
     * Returns a search result for the given book title.
     */
    private void findBooks(String bookTitle) {
        final String url = "https://maps.googleapis.com/maps/api/geocode/json?"
                + "address=" + bookTitle
                + "&key=MyKey";
        tmp = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("text_of_mine", "1toString()");
                        try {
                            JSONArray results = response.getJSONArray("results");
                            for(int i = 0; i < results.length(); i++){
                                JSONObject object = results.getJSONObject(i);
                                Log.v("text_of_mine", object.toString());
                                String name = object.getString("formatted_address");
                                JSONObject object1 = object.getJSONObject("geometry");
                                JSONObject object2 = object1.getJSONObject("location");
                                String lat = object2.getString("lat");
                                String lng = object2.getString("lng");
                                tmp.add(new Place(name, lat, lng));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            isCompleted = true;
                        }
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
}
