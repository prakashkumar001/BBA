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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.bba.ministries.R;
import com.bba.ministries.Utils.InternetPermissions;
import com.bba.ministries.adapter.FB_CoversAdapter;
import com.bba.ministries.database.DBHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by v-62 on 11/21/2016.
 */

public class FB_covers extends Fragment {
    RecyclerView list;
    FB_CoversAdapter adapter;
    ArrayList<PromiseCard> data;
    Toolbar toolbar;
    DBHelper database;
    private static final int NETPERMISSION = 1888;
    InternetPermissions internetPermissions;
    TextView text;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fb_covers, container, false);
        list=(RecyclerView)view.findViewById(R.id.recyclerlist);
        data=new ArrayList<PromiseCard>();
        toolbar = (Toolbar)view. findViewById(R.id.toolbar);
        text=(TextView)view.findViewById(R.id.text);
        database=new DBHelper(getActivity());


       /* toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                getActivity().finish();
            }
        });*/

        DBDATA();






        /*PromiseCard p=new PromiseCard();
        p.image=R.drawable.jesus;
        data.add(p);
        data.add(p);
        data.add(p);
        data.add(p);
        data.add(p);
        data.add(p);
        data.add(p);*//*
        adapter=new FB_CoversAdapter(getActivity(),data);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(mLayoutManager);
        list.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(a, LinearLayoutManager.VERTICAL));
        list.setAdapter(adapter);
        list.setNestedScrollingEnabled(false);

        adapter.notifyDataSetChanged();*/
        return view;
    }
        /*download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image.buildDrawingCache();
                Bitmap bm=image.getDrawingCache();
                OutputStream fOut = null;
                Uri outputFileUri;
                try {
                    File root = new File(Environment.getExternalStorageDirectory()
                            + File.separator + "BBA_Ministries" + File.separator);
                    root.mkdirs();
                    File sdImageMainDirectory = new File(root, "Jesus.jpg");
                    outputFileUri = Uri.fromFile(sdImageMainDirectory);
                    fOut = new FileOutputStream(sdImageMainDirectory);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error occured. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }
                try {
                    bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (Exception e) {
                }
            }
        });
*/
        public void fb_Covers() {


            String tag_json_obj = "json_obj_req";

            String url = GlobalClass.webUrl+"facebook-covers";
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
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
                            if(database.getresponse("fbcover","1")== "No data")
                            {
                                database.add("fbcover",response.toString());
                            }else {
                                database.update_table(response.toString(),"fbcover","1");
                            }

                            try{

                                try {
                                    array=new JSONArray(response);
                                }catch (Exception e)
                                {

                                }


                                if(array==null || response.equalsIgnoreCase("null"))
                                {
                                    text.setVisibility(View.VISIBLE);

                                }else
                                {
                                    for(int i=0;i<array.length();i++)
                                    {
                                        Log.i("CCCCCCCCCCC","CCCCCCCCCCCCCCCC");

                                        JSONObject object=array.getJSONObject(i);
                                        String id=object.getString("id");
                                        String title=object.getString("title");
                                        String img=object.getString("img");
                                        //String description=object.getString("description");

                                        data.add(new PromiseCard(id,title,"",img,""));


                                    }
                                    pDialog.hide();
                                    Log.d("Response", response.toString());
                                    adapter=new FB_CoversAdapter(getActivity(),data);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
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
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(jsonObjReq);
        }

    public void DBDATA()
    {


        String dbdetails=database.getresponse("fbcover","1");

        Log.i("DDDDDDDDDDDDDDDDDBBBBBBBBBB","DDDDDDDDDDDDDDDDDBBBBBBBBBB"+dbdetails);

        try{
            JSONArray array=new JSONArray(dbdetails);
/*
            if(array.length()==0)
            {
                getActivity().setContentView(R.layout.empty_page);
                TextView text=(TextView)getView().findViewById(R.id.text);
                text.setText("No Events Available");

            }else {*/
                for (int i = 0; i < array.length(); i++) {

                    JSONObject object = array.getJSONObject(i);
                    String id = object.getString("id");
                    String title = object.getString("title");
                    String img = object.getString("img");
                    //String description = object.getString("description");

                    data.add(new PromiseCard(id, title, "", img, ""));


                }

                adapter = new FB_CoversAdapter(getActivity(), data);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                list.setLayoutManager(mLayoutManager);
                list.setItemAnimator(new DefaultItemAnimator());
                //recyclerView.addItemDecoration(new DividerItemDecoration(a, LinearLayoutManager.VERTICAL));
                list.setAdapter(adapter);
                list.setNestedScrollingEnabled(false);

                adapter.notifyDataSetChanged();
            //}
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
            fb_Covers();
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
                        fb_Covers();
                    }else
                    {
                        getActivity().startActivityForResult(new Intent(Settings.ACTION_SETTINGS),NETPERMISSION);
                    }
                }
            });


        }
    }
}