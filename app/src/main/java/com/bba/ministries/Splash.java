package com.bba.ministries;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bba.ministries.Common.GlobalClass;
import com.bba.ministries.Utils.InternetPermissions;
import com.bba.ministries.database.DBHelper;

import org.json.JSONObject;

/**
 * Created by Prakash on 12/18/2016.
 */

public class Splash extends AppCompatActivity {

    DBHelper database;
    final int SPLASH_DISPLAY_TIME = 2000;
    InternetPermissions internetPermissions;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

         internetPermissions=new InternetPermissions(getApplicationContext());

        database=new DBHelper(getApplicationContext());

        int current = getRequestedOrientation();
        // only switch the orientation if not in portrait
        if (current != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        Sliders();


    }
    public void  Sliders() {
        String tag_json_obj = "json_obj_req";
        GlobalClass.webUrl="http://bbaministries.org/webservice/";
        GlobalClass.imageUrl="http://bbaministries.org/uploads/";

        String url = GlobalClass.webUrl+"sliders";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());


                        if(database.getresponse("home","1")=="No data")
                        {
                            database.add("home",response.toString());

                            new Handler().postDelayed(new Runnable() {
                                public void run() {

                                    Splash.this.finish();
                                    overridePendingTransition(R.anim.fadeinact,
                                            R.anim.fadeoutact);
              /*  Intent mainIntent = new Intent(
                        Splash.this,
                        MainActivity.class);

                Splash.this.startActivity(mainIntent);*/

                                    Intent mainIntent = new Intent(
                                            Splash.this,
                                            MainActivity.class);

                                    Splash.this.startActivity(mainIntent);




                                }
                            }, SPLASH_DISPLAY_TIME);

                        }else {
                            database.update_table(response.toString(),"home","1");

                            new Handler().postDelayed(new Runnable() {
                                public void run() {

                                    Splash.this.finish();
                                    overridePendingTransition(R.anim.fadeinact,
                                            R.anim.fadeoutact);
              /*  Intent mainIntent = new Intent(
                        Splash.this,
                        MainActivity.class);

                Splash.this.startActivity(mainIntent);*/

                                    Intent mainIntent = new Intent(
                                            Splash.this,
                                            MainActivity.class);

                                    Splash.this.startActivity(mainIntent);




                                }
                            }, SPLASH_DISPLAY_TIME);

                        }















                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Repsonse", "Error: " + error.getMessage());
                // hide the progress dialog
               // pDialog.hide();



                if(internetPermissions.isInternetOn())
                {

                    //Sliders();

                    if(database.getresponse("home","1")!=null)
                    {
                        Intent mainIntent = new Intent(
                                Splash.this,
                                MainActivity.class);

                        Splash.this.startActivity(mainIntent);
                    }else
                    {
                        Sliders();
                    }

                    Snackbar snack= Snackbar.make(findViewById(android.R.id.content), "NetConnection is slow",Snackbar.LENGTH_LONG);
                    View vv=snack.getView();
                    TextView textView=(TextView)vv.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setGravity(Gravity.CENTER);
                    snack.show();


                }else
                {

                    Snackbar snack= Snackbar.make(findViewById(android.R.id.content), "No Internet Connection",Snackbar.LENGTH_LONG);
                    View vv=snack.getView();
                    TextView textView=(TextView)vv.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setGravity(Gravity.CENTER);
                    snack.show();



                    new Handler().postDelayed(new Runnable() {
                        public void run() {

                            Splash.this.finish();
                            overridePendingTransition(R.anim.fadeinact,
                                    R.anim.fadeoutact);


                            Intent mainIntent = new Intent(
                                    Splash.this,
                                    MainActivity.class);

                            Splash.this.startActivity(mainIntent);




                        }
                    }, SPLASH_DISPLAY_TIME);

                }




            }
        });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(jsonObjReq);
    }
}