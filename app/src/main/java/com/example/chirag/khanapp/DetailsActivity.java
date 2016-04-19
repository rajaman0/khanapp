package com.example.chirag.khanapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Chirag on 4/19/2016.
 */
public class DetailsActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_detail_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        String badge = b.getString("Badge");
        String category = b.getString("Category");
        JSONObject badge_obj;
        JSONArray cat_obj;
        try {
            badge_obj = new JSONObject(badge);
            cat_obj = new JSONArray(category);
        } catch (JSONException e){
            Log.v(MainActivity.ERROR_TAG, "Error parsing json for details");
            return;
        }

        Log.v("VIEW", "setting values");
        ImageView mainImage = (ImageView) findViewById(R.id.main_image);
        TextView mainName = (TextView) findViewById(R.id.name);
        TextView mainDescription = (TextView) findViewById(R.id.extended_description);
        TextView mainPoints = (TextView) findViewById(R.id.points);
        ImageView categoryImage = (ImageView) findViewById(R.id.category_image);
        TextView categoryName = (TextView) findViewById(R.id.category_name);
        TextView categoryDescription = (TextView) findViewById(R.id.category_description);



        String mainImg_str, mainName_str, mainDescription_str, categoryImage_str, categoryName_str,
        categoryDescription_str;
        int mainPoints_int = -1;
        try {
            mainImg_str = badge_obj.getJSONObject("icons").getString("large");
            mainName_str = badge_obj.getString("description");
            mainDescription_str = badge_obj.getString("translated_safe_extended_description");
            mainPoints_int = badge_obj.getInt("points");

            int categoryNumber = badge_obj.getInt("badge_category");

            JSONObject categoryObject = cat_obj.getJSONObject(categoryNumber);
            categoryImage_str = categoryObject.getString("large_icon_src");
            categoryName_str = categoryObject.getString("type_label");
            categoryDescription_str = categoryObject.getString("translated_description");
        } catch (JSONException e){
            Log.e(MainActivity.ERROR_TAG, "Issue with getting url." + e.toString());
            //default image
            mainImg_str = categoryImage_str = "https://fastly.kastatic.org/images/khan-logo-vertical-transparent.png";
            mainName_str = mainDescription_str = categoryDescription_str = categoryName_str = "Default";


        }

        Picasso.with(getApplicationContext()).load(mainImg_str).into(mainImage);
        Picasso.with(getApplicationContext()).load(categoryImage_str).into(categoryImage);

        mainName.setText(mainName_str);
        mainDescription.setText(mainDescription_str);
        mainPoints.setText("Points: " + mainPoints_int);
        categoryName.setText(categoryName_str);
        categoryDescription.setText(categoryDescription_str);


    }
}
