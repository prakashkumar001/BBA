package com.bba.ministries.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bba.ministries.R;

import java.util.ArrayList;

/**
 * Created by v-62 on 12/8/2016.
 */

public class SpinnerAdapter extends ArrayAdapter<String> {

    private Context activity;
    private ArrayList<String> data;
    public Resources res;
    LayoutInflater inflater;


    /*************  CustomAdapter Constructor *****************/
    public SpinnerAdapter(Context activitySpinner, int textViewResourceId, ArrayList<String> objects)
    {
        super(activitySpinner, textViewResourceId, objects);

        /********** Take passed values **********/
        activity = activitySpinner;
        data     = objects;


        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.prayer_request_item, parent, false);

        /***** Get each Model object from Arraylist ********/


        TextView label        = (TextView)row.findViewById(R.id.expandedListItem);
        ImageView vv        = (ImageView)row.findViewById(R.id.lines);



        if(position==0){

            // Default selected Spinner item
            label.setText("---Please select---");
            label.setTextColor(Color.parseColor("#808080"));



        }
        label.setText(data.get(position));

        if(position==6)
        {
            label.setText(data.get(position));
            vv.setVisibility(View.GONE);
        }


        return row;
    }
}