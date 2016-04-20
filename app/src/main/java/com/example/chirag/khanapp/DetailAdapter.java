package com.example.chirag.khanapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Chirag on 4/18/2016.
 * Basic adapter that populates the GridView using JSON objects passed from the main
 * activity.
 */
public class DetailAdapter extends BaseAdapter {

    public List<JSONObject> items;
    private Context mContext;

    /*List <JSONObject> items is the list that contains all the badge information. */
    public DetailAdapter(Context context, List<JSONObject> items) {
        mContext = context;
        this.items = items;
    }


    @Override
    public int getCount () {
        return items.size();
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    public JSONObject getItem (int position) {
        return items.get(position);
    }


    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;

        //no need to reinflate the view every time, only on instantiation.
        //(standard practice that improves performance)
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(R.layout.grid_item_layout, parent, false);
        }

        JSONObject obj = getItem(position);


        if (obj != null){
            ImageView smallImage = (ImageView) v.findViewById(R.id.imgThumbnail);
            TextView tv = (TextView) v.findViewById(R.id.description);

            String img, name;
            //parses the JSON to get the badge name and url for the image.
            try {
                img = obj.getJSONObject("icons").getString("email");
                name = obj.getString("description");
            } catch (JSONException e){
                Log.v(MainActivity.ERROR_TAG, "Issue with getting url.");
                //default image
                img = "https://fastly.kastatic.org/images/khan-logo-vertical-transparent.png";
                name = "Default";
            }

            //Uses Picasso image library to load and cache image for great performance.
            if (smallImage != null){
                Picasso.with(mContext).load(img).into(smallImage);
            }

            //sets the text view
            if (tv != null){
                tv.setText(name);
            }
        }
        return v;
    }
}
