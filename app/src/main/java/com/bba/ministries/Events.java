package com.bba.ministries;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bba.ministries.Common.Event;
import com.bba.ministries.Common.GlobalClass;
import com.bba.ministries.Utils.InternetPermissions;
import com.bba.ministries.adapter.EventsAdapter;
import com.bba.ministries.database.DBHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by v-62 on 12/3/2016.
 */

public class Events extends AppCompatActivity {
    RecyclerView list;
    EventsAdapter adapter;
    ArrayList<Event> data;
    Toolbar toolbar;
    DBHelper database;
    InternetPermissions internetPermissions;
    private static final int NETPERMISSION = 1888;
    TextView text;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);
        list=(RecyclerView)findViewById(R.id.recyclerlist);
        data=new ArrayList<Event>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        text=(TextView)findViewById(R.id.text);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.logos);
        getSupportActionBar().setTitle("  Events");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
            }
        });

        database=new DBHelper(getApplicationContext());

        DBDATA();



    }

    public void EventsDetails() {
        String tag_json_obj = "json_obj_req";

        String url = GlobalClass.webUrl+"events";

        final ProgressDialog pDialog = new ProgressDialog(Events.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response.toString());

                        data.clear();
                        JSONArray array=null;
                        if(database.getresponse("events","1")== "No data")
                        {
                            database.add("events",response.toString());
                        }else {
                            database.update_table(response.toString(),"events","1");
                        }

                        try{
                             array=new JSONArray(response);
                        }catch (Exception e)
                        {

                        }

                        try{

                            if(array==null || response.equalsIgnoreCase("null"))
                            {
                                pDialog.hide();
                              text.setVisibility(View.VISIBLE);

                            }else {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    String id = object.getString("id");
                                    String title = object.getString("title");
                                    String datetime = object.getString("datetime");
                                    String startevent = object.getString("event_start_time");
                                    //String endevent=object.getString("event_end_time");
                                    String place = object.getString("place");
                                    String contact = object.getString("contact");
                                    String latitude = object.getString("latitude");
                                    String longitude = object.getString("longitude");
                                    String description = object.getString("description");


                                /*    if (latitude.equals(" ")) {
                                        latitude = "14.5401";

                                    }
                                    if (longitude.equals(" ")) {
                                        longitude = "74.9676";

                                    }*/

                                    Log.i("DDDDDDDDDDD","DDDDDDDDDdd"+description);

                                    data.add(new Event(id, title, datetime, startevent, contact, place, description, latitude, longitude));


                                }
                                pDialog.hide();

                                adapter = new EventsAdapter(Events.this, data);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                list.setLayoutManager(mLayoutManager);
                                list.setItemAnimator(new DefaultItemAnimator());
                                //recyclerView.addItemDecoration(new DividerItemDecoration(a, LinearLayoutManager.VERTICAL));
                                list.setAdapter(adapter);
                                list.setNestedScrollingEnabled(false);

                                adapter.notifyDataSetChanged();
                            }
                        }catch (Exception e)
                        {

                        }




                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Repsonse", "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
            }
        });
        GlobalClass.getInstance().addToRequestQueue(jsonObjReq,
                tag_json_obj);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        finish();
    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet
             EventsDetails();
           // Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    public void DBDATA()
    {

        String dbdetails=database.getresponse("events","1");

        try{
            JSONArray array=null;


            try{
                array=new JSONArray(dbdetails);
            }catch (Exception e)
            {

            }




                for(int i=0;i<array.length();i++)
                {
                    JSONObject object=array.getJSONObject(i);
                    String id=object.getString("id");
                    String title=object.getString("title");
                    String datetime=object.getString("datetime");
                    String startevent=object.getString("event_start_time");
                    //String endevent=object.getString("event_end_time");
                    String place=object.getString("place");
                    String contact=object.getString("contact");
                    String latitude=object.getString("latitude");
                    String longitude=object.getString("longitude");
                    String description=object.getString("description");



                    data.add(new Event(id,title,datetime,startevent,contact,place,description,latitude,longitude));


                }


                adapter=new EventsAdapter(Events.this,data);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                list.setLayoutManager(mLayoutManager);
                list.setItemAnimator(new DefaultItemAnimator());
                //recyclerView.addItemDecoration(new DividerItemDecoration(a, LinearLayoutManager.VERTICAL));
                list.setAdapter(adapter);
                list.setNestedScrollingEnabled(false);

                adapter.notifyDataSetChanged();

        }catch (Exception e)
        {

        }


    }
    @Override
    public void onResume() {
        super.onResume();
        internetPermissions=new InternetPermissions(Events.this);
        if(internetPermissions.isInternetOn())
        {
            EventsDetails();
        }else
        {

            Snackbar snack= Snackbar.make(findViewById(android.R.id.content), "No Internet Connection Available",Snackbar.LENGTH_LONG);
            View vv=snack.getView();
            TextView textView=(TextView)vv.findViewById(android.support.design.R.id.snackbar_text);
            textView.setGravity(Gravity.CENTER);
           // snack.setActionTextColor(R.color.colorPrimary);
            snack.show();
            snack.setAction("Enable", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(internetPermissions.isInternetOn())
                    {
                        EventsDetails();
                    }else
                    {
                        startActivityForResult(new Intent(Settings.ACTION_SETTINGS),NETPERMISSION);
                    }
                }
            });


        }
    }
}
