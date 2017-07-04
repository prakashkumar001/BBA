package com.bba.ministries.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bba.ministries.Common.GlobalClass;
import com.bba.ministries.Events;
import com.bba.ministries.HomeViewAdapter;
import com.bba.ministries.PromiseCardActivity;
import com.bba.ministries.R;
import com.bba.ministries.Utils.InternetPermissions;
import com.bba.ministries.database.DBHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by v-62 on 11/19/2016.
 */

public  class Home extends Fragment implements ViewPager.OnPageChangeListener{

    private ViewPager intro_images;
    DBHelper database;
    private LinearLayout promisecard,events;
    private int dotsCount;
    ArrayList<String> datas;
    public static String verse,label;
    private SparseIntArray mErrorString;
    private static final int NETPERMISSION = 1888;


    private ImageView[] dots;
    int page=0;
    private HomeViewAdapter mAdapter;
    TextView verses,labels;
    InternetPermissions internetPermissions;






    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);
        intro_images = (ViewPager) view.findViewById(R.id.pager_introduction);
        promisecard = (LinearLayout) view.findViewById(R.id.promisecard);
        events = (LinearLayout) view.findViewById(R.id.events);
        verses = (TextView) view.findViewById(R.id.verse);
        labels = (TextView) view.findViewById(R.id.label);
        database=new DBHelper(getActivity());
        mErrorString = new SparseIntArray();

        datas=new ArrayList<>();


        // left=(ImageView) findViewById(R.id.left);
        // right=(ImageView) findViewById(R.id.right);




        DBDATA();





        intro_images.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                // do transformation here
                //page.animate()==null;
                final Animation animationFadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
                view.startAnimation(animationFadeIn);

                view.setTranslationX(view.getWidth() * -position);

                if(position <= -1.0F || position >= 1.0F) {
                    view.setAlpha(0.0F);
                } else if( position == 0.0F ) {
                    view.setAlpha(1.0F);
                } else {
                    // position is between -1.0F & 0.0F OR 0.0F & 1.0F
                    view.setAlpha(1.0F - Math.abs(position));
                }
            }
        });

        promisecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getActivity(), PromiseCardActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                getActivity().finish();
            }
        });



        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getActivity(), Events.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                getActivity().finish();
            }
        });


        return view;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        final Animation animationFadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
        intro_images.setAnimation(animationFadeIn);


    }

    @Override
    public void onPageSelected(int position) {

    }
    private int getItem() {
        Log.i("Current Page","Current Page"+intro_images.getCurrentItem());
        return intro_images.getCurrentItem();
    }

    public void  Sliders() {
        String tag_json_obj = "json_obj_req";

        String url = GlobalClass.webUrl+"sliders";


        // final ProgressDialog pDialog = new ProgressDialog(getActivity());
        // pDialog.setMessage("Loading...");
        // pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());

                        datas.clear();

                        if(database.getresponse("home","1")=="No data")
                        {
                            database.add("home",response.toString());
                        }else {
                            database.update_table(response.toString(),"home","1");
                        }

                        try{
                            JSONObject object=response.getJSONObject("verse");

                            verse=object.getString("verse");
                            label=object.getString("label");
                            verses.setText(verse);
                            labels.setText(label);

                            JSONArray array=response.getJSONArray("slider");
                            Log.i("CCCCCCCCCCC","CCCCCCCCCCCCCCCC"+array);

                            for(int i=0;i<array.length();i++)
                            {

                                JSONObject ob=array.getJSONObject(i);
                                String img=ob.getString("slider_img");

                                datas.add(img);


                            }
                            //  pDialog.hide();
                            Log.d("Response", response.toString());
                            mAdapter = new HomeViewAdapter(getActivity(), datas);
                            intro_images.setAdapter(mAdapter);
                            //intro_images.setOnPageChangeListener(this);

                            final Handler handler = new Handler();

                            final Runnable update = new Runnable() {
                                public void run() {
                                  /*  if (getItem() == 2 - 1) {
                                        page = 0;
                                    }*/


                                    if(datas.size()==page)
                                    {
                                        page=0;
                                        intro_images.setCurrentItem(page);
                                    }else
                                    {
                                        intro_images.setCurrentItem(page++);

                                    }





                                }
                            };


                            new Timer().schedule(new TimerTask() {

                                @Override
                                public void run() {
                                    handler.post(update);
                                }
                            }, 1000, 5000);
                        }catch (Exception e)
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
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jsonObjReq);
    }


    public void DBDATA() {


        String dbdetails = database.getresponse("home", "1");
        try {
            JSONObject response = new JSONObject(dbdetails);
            JSONObject object = response.getJSONObject("verse");

            verse=object.getString("verse");
            label=object.getString("label");
            verses.setText(verse);
            labels.setText(label);

            JSONArray array = response.getJSONArray("slider");
            Log.i("CCCCCCCCCCC", "CCCCCCCCCCCCCCCC" + array);

            for (int i = 0; i < array.length(); i++) {

                JSONObject ob = array.getJSONObject(i);
                String img = ob.getString("slider_img");

                datas.add(img);


            }

            Log.d("Response", response.toString());
            mAdapter = new HomeViewAdapter(getActivity(), datas);
            intro_images.setAdapter(mAdapter);
            //intro_images.setOnPageChangeListener(this);


            final Handler handler = new Handler();

            final Runnable update = new Runnable() {
                public void run() {
                                  /*  if (getItem() == 2 - 1) {
                                        page = 0;
                                    }*/
                    if(datas.size()==page)
                    {
                        page=0;
                        intro_images.setCurrentItem(page);
                    }else
                    {
                        intro_images.setCurrentItem(page++);

                    }




                }
            };


            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    handler.post(update);
                }
            }, 1000, 5000);
        }catch (Exception e)
        {

        }



    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NETPERMISSION && resultCode == Activity.RESULT_OK) {
            try {

                Sliders();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        internetPermissions=new InternetPermissions(getActivity());
        if(internetPermissions.isInternetOn())
        {
            // Sliders();
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
                        // Sliders();
                    }else
                    {
                        getActivity().startActivityForResult(new Intent(Settings.ACTION_SETTINGS),NETPERMISSION);
                    }
                }
            });


        }
    }



}