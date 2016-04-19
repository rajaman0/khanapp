package com.example.chirag.khanapp;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    public static final String CAT_URL = "http://www.khanacademy.org/api/v1/badges/categories";
    public static final String ERROR_TAG = "ERR";
    private GridView gridView;
    public String category;
    private static int index = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                Log.e(ERROR_TAG, "Error occurred with Khan Academy Badges HTTP request " + error.toString());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, CAT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        category = response;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(ERROR_TAG, "Error occurred with Khan Academy Categories HTTP request " + error.toString());
            }
        });
        queue.add(stringRequest2);

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
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                    Bundle b = new Bundle();
                    b.putString("Category", category);
                    b.putString("Badge", badges.get(position).toString());
                    intent.putExtras(b);

                    PendingIntent pendingIntent =
                            TaskStackBuilder.create(getApplicationContext())
                                    // add all of DetailsActivity's parents to the stack,
                                    // followed by DetailsActivity itself
                                    .addNextIntentWithParentStack(intent)
                                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                    builder.setContentIntent(pendingIntent);
                    startActivity(intent);

                }
            });
        } catch (JSONException e){
            Log.e(ERROR_TAG, "Error occurred with Khan Academy Badges HTTP response parsing.");
            return;
        }
    }

    public void onResume(){
        super.onResume();
        Log.v("TAG", "selection is " + index);
        gridView.setSelection(index);
    }

    public void onPause(){
        super.onPause();
        index = gridView.getFirstVisiblePosition();
        Log.v("TAG", "first visible position is " + index);
    }
}
