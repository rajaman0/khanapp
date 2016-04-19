package com.example.chirag.khanapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<JSONObject> badges = new ArrayList<JSONObject>(12);
    public static final String BADGE_URL = "http://www.khanacademy.org/api/v1/badges";
    public static final String ERROR_TAG = "ERR";
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridview);


        populateBadges();
    }

    public void populateBadges(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BADGE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        setUpGridView(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v(ERROR_TAG, "Error occurred with Khan Academy Badges HTTP request");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void setUpGridView(String response){
        JSONArray badgeArrayJSON;
        try {
            badgeArrayJSON = new JSONArray(response);

            for (int i = 0; i < badgeArrayJSON.length(); i ++){
                JSONObject jo = badgeArrayJSON.getJSONObject(i);
                badges.add(jo);
            }

            gridView.setAdapter(new DetailAdapter(getApplicationContext(), badges));
        } catch (JSONException e){
            Log.v(ERROR_TAG, "Error occurred with Khan Academy Badges HTTP response parsing.");
            return;
        }
    }



}
