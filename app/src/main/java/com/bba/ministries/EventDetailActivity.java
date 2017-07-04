package com.bba.ministries;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bba.ministries.Utils.InternetPermissions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by v-62 on 12/7/2016.
 */

public class EventDetailActivity extends RuntimePermissionActivity implements

        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener

        {

    //Our Map
    private GoogleMap mMap;
            Toolbar toolbar;
            Marker marker;
            LocationRequest mLocationRequest;
            private static final int REQUEST_PERMISSIONS = 20;

            InternetPermissions internetPermissions;

    //To store longitude and latitude from map
            private double longitude;
            private double latitude;
            String description,contact;
            private static final int NETPERMISSION = 1888;
            LinearLayout maplay;
            TextView title,eventstarttime,dates,monthyear,descriptions,contacts,places;

    //Google ApiClient
    private GoogleApiClient googleApiClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        title=(TextView) findViewById(R.id.title);
        dates=(TextView) findViewById(R.id.dates);
        monthyear=(TextView) findViewById(R.id.monthyear);
        contacts=(TextView) findViewById(R.id.contact);
        descriptions=(TextView) findViewById(R.id.description);
        eventstarttime=(TextView) findViewById(R.id.times);
        places=(TextView) findViewById(R.id.place);

        maplay=(LinearLayout)findViewById(R.id.maplay);

        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.logos);
        getSupportActionBar().setTitle("  Events");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        Intent i=getIntent();

        contact=i.getStringExtra("contact");
        String lat=i.getStringExtra("latitude");
        String lon=i.getStringExtra("longitude");
        Log.i("Lat","lon"+lat+lon);
        description=i.getStringExtra("description");
        String titles=i.getStringExtra("title");
        String month_year=i.getStringExtra("monthyear");
        String date=i.getStringExtra("date");
        String eventtime=i.getStringExtra("eventtime");
        String place=i.getStringExtra("place");


        if(lat.equalsIgnoreCase("") || lon.equalsIgnoreCase(""))
        {
            mMap=null;
            maplay.setVisibility(View.INVISIBLE);

        }else
        {
            latitude= Double.parseDouble(lat);
            longitude= Double.parseDouble(lon);

        }

        title.setText(titles);
        dates.setText(date);
        monthyear.setText(month_year);
        contacts.setText(contact);
        descriptions.setText(description);
        eventstarttime.setText(eventtime);
        places.setText(place);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), Events.class);
                startActivity(i);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
            }
        });


        EventDetailActivity.super.requestAppPermissions(new
                        String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, R.string
                        .runtime_permissions_txt
                , REQUEST_PERMISSIONS);


                  //toggleGPS(true);


    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Initializing our map
        mMap = googleMap;
        //Creating a random coordinate
      /*  LatLng latLng = new LatLng(-34, 151);
        //Adding marker to that coordinate
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));*/
        //Setting onMarkerDragListener to track the marker drag
       // mMap.setOnMarkerDragListener(this);
        //Adding a long click listener to the map
        //mMap.setOnMapLongClickListener(this);
    }

    //Getting current location
    private void getCurrentLocation(double lat,double lon) {
        //Creating a location object


             lat=latitude;
            lon=longitude;
            moveMap(lat,lon);


            //moving the map to location


    }

    //Function to move the map
    private void moveMap(double lat,double lon) {
        //String to display current latitude and longitude
        String msg = lat + ", "+lon;

        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(lat, lon);

         if(mMap!=null)
         {
             mMap.clear();
         }

        Log.i("latlong", String.valueOf(latLng));

        //Adding marker to map
        marker=  mMap.addMarker(new MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title("Event Location")); //Adding a title

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat, lon)).zoom(12).tilt(30).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //Moving the camera
      /*  mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Animating the camera
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
*/
        //Displaying current coordinates in toast
       // Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onStart() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        googleApiClient.connect();
        // Create the LocationRequest object

        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onConnected(Bundle bundle) {

        internetPermissions=new InternetPermissions(getApplicationContext());
        if(internetPermissions.isInternetOn())
        {

            try{
                getCurrentLocation(latitude,longitude);

            }catch (Exception e)
            {

            }

        }else
        {
            try{
                getCurrentLocation(latitude,longitude);

            }catch (Exception e)
            {

            }
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
                        getCurrentLocation(latitude,longitude);
                    }else
                    {
                        startActivityForResult(new Intent(Settings.ACTION_SETTINGS),NETPERMISSION);
                    }
                }
            });


        }

            //getCurrentLocation(latitude,longitude);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //Clearing all the markers
        mMap.clear();

        //Adding a new marker to the current pressed position
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Getting the coordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        //Moving the map
        moveMap(latitude,longitude);
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

                            googleApiClient = new GoogleApiClient.Builder(this)
                                    .addConnectionCallbacks(this)
                                    .addOnConnectionFailedListener(this)
                                    .addApi(LocationServices.API)
                                    .build();

                            // Create the LocationRequest object
                            mLocationRequest = LocationRequest.create()
                                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                    .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                                    .setFastestInterval(1 * 1000); // 1 second, in milliseconds
                            googleApiClient.connect();
                            // Create the LocationRequest object


                            loadActivity();
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

            public void loadActivity()
            {


                //Initializing googleapi client





                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);





            }
            @Override
            public void onPermissionsGranted(final int requestCode) {
                ///Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();



                loadActivity();
            }

            @Override
            public void onBackPressed() {
                super.onBackPressed();
                Intent i=new Intent(getApplicationContext(), Events.class);
                startActivity(i);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
            }

          /*  @Override
            public void onResume() {
                super.onResume();
                internetPermissions=new InternetPermissions(getApplicationContext());

                Intent i=getIntent();
                String lat=i.getStringExtra("latitude");
                String lon=i.getStringExtra("longitude");
                latitude=Double.parseDouble(lat);
                longitude=Double.parseDouble(lon);


                internetPermissions=new InternetPermissions(getApplicationContext());
                if(internetPermissions.isInternetOn())
                {
                    Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

                    getCurrentLocation(latitude,longitude);
                }else
                {

                    Snackbar snack= Snackbar.make(findViewById(android.R.id.content), "No Internet Connection Available",Snackbar.LENGTH_LONG);
                    View vv=snack.getView();
                    TextView textView=(TextView)vv.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setGravity(Gravity.CENTER);
                    snack.show();
                    snack.setAction("Enable", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(internetPermissions.isInternetOn())
                            {
                                getCurrentLocation(latitude,longitude);
                            }else
                            {
                               startActivityForResult(new Intent(Settings.ACTION_SETTINGS),NETPERMISSION);
                            }
                        }
                    });


                }
            }*/
}
