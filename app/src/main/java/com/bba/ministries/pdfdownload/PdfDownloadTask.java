package com.bba.ministries.pdfdownload;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
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
import android.widget.GridView;
import android.widget.ListView;
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
import com.bba.ministries.MainActivity;
import com.bba.ministries.PromiseCardActivity;
import com.bba.ministries.R;
import com.bba.ministries.RuntimePermissionActivity;
import com.bba.ministries.Utils.InternetPermissions;
import com.bba.ministries.adapter.PromiseCardAdapter;
import com.bba.ministries.database.DBHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Prakash on 6/5/2017.
 */

public class PdfDownloadTask extends RuntimePermissionActivity {

    ListView list;
    DBHelper database;
    InternetPermissions internetPermissions;
        DownloadInfoArrayAdapter adapter;
        ArrayList<DownloadInfo> data;
    private static final int NETPERMISSION = 1888;
    private static final int REQUEST_PERMISSIONS = 20;
    Toolbar toolbar;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.pdf_task);

            list=(ListView)findViewById(R.id.list);
            database=new DBHelper(getApplicationContext());
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setIcon(R.mipmap.logos);
            getSupportActionBar().setTitle("  Magazines");
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
            PdfDownloadTask.super.requestAppPermissions(new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, R.string
                            .runtime_permissions_txt
                    , REQUEST_PERMISSIONS);


            // pdflist();

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


    public void pdflist() {
        String tag_json_obj = "json_obj_req";

        String url = GlobalClass.webUrl+"magazines";


       /* final ProgressDialog pDialog = new ProgressDialog(CircularProgressActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();*/

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response.toString());

                        if(database.getresponse("pdfs","1")== "No data")
                        {
                            database.add("pdfs",response.toString());
                        }else {
                            database.update_table(response.toString(),"pdfs","1");
                        }
                        //pDialog.hide();
                        data=new ArrayList<DownloadInfo>();


                        try{

                            JSONArray array=new JSONArray(response);


                            for(int i=0;i<array.length();i++)
                            {
                                JSONObject object=array.getJSONObject(i);
                                String id=object.getString("id");
                                String title=object.getString("title");
                                String banner=object.getString("banner");
                                String file=object.getString("file");
                               // String filename=object.getString("filename");
                                // String filesize=object.getString("filesize");
                                //String fileurl=object.getString("fileurl");


                                data.add(new DownloadInfo(id,title,banner,file));


                            }


                            adapter=new DownloadInfoArrayAdapter(PdfDownloadTask.this,R.layout.pdf_list_item,data);
                            list.setAdapter(adapter);
                               /* RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                list.setLayoutManager(mLayoutManager);
                                list.setItemAnimator(new DefaultItemAnimator());
                                //recyclerView.addItemDecoration(new DividerItemDecoration(a, LinearLayoutManager.VERTICAL));

                                list.setNestedScrollingEnabled(false);

                                adapter.notifyDataSetChanged();*/

                        }


                        catch (Exception e)
                        {

                        }




                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Repsonse", "Error: " + error.getMessage());
                // hide the progress dialog
                //  pDialog.hide();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(PdfDownloadTask.this);
        queue.add(jsonObjReq);
    }
    public void DBDATA() {

        String dbdetails = database.getresponse("pdfs", "1");
        try {
            JSONArray array = new JSONArray(dbdetails);

            data = new ArrayList<DownloadInfo>();


                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String id = object.getString("id");
                    String title = object.getString("title");
                    String banner = object.getString("banner");
                    String file = object.getString("file");
                    // String filename=object.getString("filename");
                    // String filesize=object.getString("filesize");
                    //String fileurl=object.getString("fileurl");


                    data.add(new DownloadInfo(id, title, banner, file));


                }


                adapter = new DownloadInfoArrayAdapter(PdfDownloadTask.this, R.layout.pdf_list_item, data);
                list.setAdapter(adapter);
            } catch (Exception e) {

            }


    }

    @Override
    protected void onResume() {
        super.onResume();
        internetPermissions=new InternetPermissions(PdfDownloadTask.this);
        if(internetPermissions.isInternetOn())
        {
            pdflist();
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
                        pdflist();
                    }else
                    {
                        startActivityForResult(new Intent(Settings.ACTION_SETTINGS),NETPERMISSION);
                    }
                }
            });


        }
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(PdfDownloadTask.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        finish();    }
}
