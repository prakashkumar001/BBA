package com.bba.ministries;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bba.ministries.Common.GlobalClass;
import com.bba.ministries.Common.PromiseCard;
import com.bba.ministries.Utils.InternetPermissions;
import com.bba.ministries.adapter.PromiseCardAdapter;
import com.bba.ministries.database.DBHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by v-62 on 12/2/2016.
 */

public class PromiseCardActivity extends RuntimePermissionActivity {
    RecyclerView list;
    PromiseCardAdapter adapter;
    ArrayList<PromiseCard> data;
    Toolbar toolbar;
    DBHelper database;
    private static final int REQUEST_PERMISSIONS = 20;
    InternetPermissions internetPermissions;
    private static final int NETPERMISSION = 1888;
    TextView text;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promise_card);
        list=(RecyclerView)findViewById(R.id.recyclerlist);
        data=new ArrayList<PromiseCard>();
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        text=(TextView)findViewById(R.id.text);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.logos);
        getSupportActionBar().setTitle("  Promise Card");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database=new DBHelper(getApplicationContext());

      toolbar.setNavigationOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent i=new Intent(getApplicationContext(), MainActivity.class);
              startActivity(i);
             overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
              finish();
          }
      });


        DBDATA();

        PromiseCardActivity.super.requestAppPermissions(new
                        String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, R.string
                        .runtime_permissions_txt
                , REQUEST_PERMISSIONS);











    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if ((grantResults.length > 0) && (grantResults[0] +
                        grantResults[1]) == PackageManager.PERMISSION_GRANTED) {
                    //Call whatever you want


                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Enable Permissions from settings",
                            Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                                    intent.setData(Uri.parse("package:" + getPackageName()));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    startActivity(intent);
                                }
                            }).show();
                }
                return;
            }
        }
    }
    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    public void promiseCardDetails() {
        String tag_json_obj = "json_obj_req";

        String url =  GlobalClass.webUrl+"promise-cards";

        final ProgressDialog pDialog = new ProgressDialog(PromiseCardActivity.this);
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

                        try{
                            array=new JSONArray(response);
                        }catch (Exception e)
                        {

                        }
                        if(database.getresponse("promisecard","1")== "No data")
                        {
                            database.add("promisecard",response.toString());
                        }else {
                            database.update_table(response.toString(),"promisecard","1");
                        }
                        try{


                            if(array==null || response.equalsIgnoreCase("null"))
                            {
                                pDialog.hide();


                                text.setVisibility(View.VISIBLE);

                            }else
                            {
                                for(int i=0;i<array.length();i++)
                                {
                                    JSONObject object=array.getJSONObject(i);
                                    String id=object.getString("id");
                                    String title=object.getString("title");
                                    String year=object.getString("year");
                                    String img=object.getString("img");
                                    String description=object.getString("description");

                                    data.add(new PromiseCard(id,title,year,img,description));


                                }
                                pDialog.hide();

                                adapter=new PromiseCardAdapter(PromiseCardActivity.this,data);
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
        RequestQueue queue = Volley.newRequestQueue(PromiseCardActivity.this);
        queue.add(jsonObjReq);
    }


    public void DBDATA()
    {

        String dbdetails=database.getresponse("promisecard","1");
        try{
            JSONArray array=new JSONArray(dbdetails);;







                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String id = object.getString("id");
                    String title = object.getString("title");
                    String year = object.getString("year");
                    String img = object.getString("img");
                    String description = object.getString("description");

                    data.add(new PromiseCard(id, title, year, img, description));


                }


                adapter = new PromiseCardAdapter(PromiseCardActivity.this, data);
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        finish();
    }
    @Override
    public void onResume() {
        super.onResume();
        internetPermissions=new InternetPermissions(PromiseCardActivity.this);
        if(internetPermissions.isInternetOn())
        {
            promiseCardDetails();
        }else
        {

            Snackbar snack= Snackbar.make(findViewById(android.R.id.content), "No Internet Connection",Snackbar.LENGTH_LONG);
            View vv=snack.getView();
            TextView textView=(TextView)vv.findViewById(android.support.design.R.id.snackbar_text);
            textView.setGravity(Gravity.CENTER);
            snack.show();
            snack.setAction("Enable", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(internetPermissions.isInternetOn())
                    {
                        promiseCardDetails();
                    }else
                    {
                        startActivityForResult(new Intent(Settings.ACTION_SETTINGS),NETPERMISSION);
                    }
                }
            });


        }
    }
}
