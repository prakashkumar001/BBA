package com.bba.ministries.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bba.ministries.Common.GlobalClass;
import com.bba.ministries.Common.Pastor;
import com.bba.ministries.R;
import com.bba.ministries.Utils.InternetPermissions;
import com.bba.ministries.adapter.PastorAdapter;
import com.bba.ministries.database.DBHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by v-62 on 12/9/2016.
 */

public class OurPastors extends Fragment {
    WebView text;
    RecyclerView list;
    PastorAdapter adapter;
    ArrayList<Pastor> data;
   // Toolbar toolbar;
    DBHelper database;
    InternetPermissions internetPermissions;
    private static final int NETPERMISSION = 1888;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.our_pastors, container, false);
        list=(RecyclerView)view.findViewById(R.id.recyclerlist);

       // toolbar = (Toolbar)view. findViewById(R.id.toolbar);

      database=new DBHelper(getActivity());


      /*  toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                getActivity().finish();
            }
        });*/

        DBDATA();


        return view;
    }
    public void PastorsDetails() {
        String tag_json_obj = "json_obj_req";

        String url = GlobalClass.webUrl+"pastors";

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Response", response.toString());

                        data=new ArrayList<Pastor>();

                        if(database.getresponse("pastors","1")=="No data")
                        {
                            database.add("pastors",response.toString());
                        }else {
                            database.update_table(response.toString(),"pastors","1");


                        }

                        pDialog.hide();
                        try{

                            Log.d("RRRRRRRRRRRRRRRRRRRRRRRRRR", response.toString());
                            JSONArray array=response;
                            for(int i=0;i<array.length();i++)
                            {
                                JSONObject object=array.getJSONObject(i);
                                String id=object.getString("id");
                                String name=object.getString("name");
                                String pic=object.getString("pic");
                                String Phone=object.getString("phone");
                                String facebook=object.getString("facebook");
                                String twitter=object.getString("twitter");
                                String email=object.getString("email");
                                String designation=object.getString("designation");



                                data.add(new Pastor(id,name,pic,Phone,facebook,twitter,email,designation));


                            }





                            adapter=new PastorAdapter(getActivity(),data);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            list.setLayoutManager(mLayoutManager);
                            list.setItemAnimator(new DefaultItemAnimator());
                            //recyclerView.addItemDecoration(new DividerItemDecoration(a, LinearLayoutManager.VERTICAL));
                            list.setAdapter(adapter);
                            list.setNestedScrollingEnabled(false);
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

    public void DBDATA()
    {
        String dbdetails=database.getresponse("pastors","1");

        try{

            JSONArray array=new JSONArray(dbdetails);
            for(int i=0;i<array.length();i++)
            {
                JSONObject object=array.getJSONObject(i);
                String id=object.getString("id");
                String name=object.getString("name");
                String pic=object.getString("pic");
                String Phone=object.getString("phone");
                String facebook=object.getString("facebook");
                String twitter=object.getString("twitter");
                String email=object.getString("email");
                String designation=object.getString("designation");



                data.add(new Pastor(id,name,pic,Phone,facebook,twitter,email,designation));


            }


            adapter=new PastorAdapter(getActivity(),data);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            list.setLayoutManager(mLayoutManager);
            list.setItemAnimator(new DefaultItemAnimator());
            //recyclerView.addItemDecoration(new DividerItemDecoration(a, LinearLayoutManager.VERTICAL));
            list.setAdapter(adapter);
            list.setNestedScrollingEnabled(false);
        }catch (Exception e)
        {

        }

    }
    @Override
    public void onResume() {
        super.onResume();
        internetPermissions=new InternetPermissions(getActivity());
        if(internetPermissions.isInternetOn())
        {
            PastorsDetails();
        }else
        {

            Snackbar snack= Snackbar.make(getActivity().findViewById(android.R.id.content), "No Internet Connection",Snackbar.LENGTH_LONG);
            View vv=snack.getView();
            TextView textView=(TextView)vv.findViewById(android.support.design.R.id.snackbar_text);
            textView.setGravity(Gravity.CENTER);
            snack.show();
            snack.setAction("Enable", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(internetPermissions.isInternetOn())
                    {
                        PastorsDetails();
                    }else
                    {
                        getActivity().startActivityForResult(new Intent(Settings.ACTION_SETTINGS),NETPERMISSION);
                    }
                }
            });


        }
    }
}
