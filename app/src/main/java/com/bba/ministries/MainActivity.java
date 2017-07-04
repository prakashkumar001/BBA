package com.bba.ministries;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;

import com.bba.ministries.Common.GlobalClass;
import com.bba.ministries.Utils.AnimatedExpandableListView;
import com.bba.ministries.adapter.ExpandableAdapter;
import com.bba.ministries.fragments.ContactUS;
import com.bba.ministries.fragments.FB_covers;
import com.bba.ministries.fragments.Home;
import com.bba.ministries.fragments.OurBelief;
import com.bba.ministries.fragments.OurMinistries;
import com.bba.ministries.fragments.OurPastors;
import com.bba.ministries.fragments.OurStory;
import com.bba.ministries.fragments.PrayerRequest;
import com.bba.ministries.pdfdownload.PdfDownloadTask;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
         {
    protected View view;
   // private ImageButton btnNext, btnFinish;
   private Handler handler;
    private int delay = 5000; //milliseconds
   private AnimatedExpandableListView mExpandableListView;
    public ArrayList<ExpandableAdapter.GroupItem> grouplist;
    public ArrayList<ExpandableAdapter.ChildItem> childlist;
    ExpandableAdapter adapter;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    //ImageView lines;
    int lastExpandedPosition=-1;
             private FrameLayout frame;
             private float lastTranslate = 0.0f;
   // private ImageView left,right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // left=(ImageView) findViewById(R.id.left);
       // right=(ImageView) findViewById(R.id.right);


        mExpandableListView = (AnimatedExpandableListView) findViewById(R.id.navList);
        frame = (FrameLayout) findViewById(R.id.container);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                super.onDrawerSlide(drawerView, slideOffset);
                float moveFactor = (mExpandableListView.getWidth() * slideOffset);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                {
                    drawer.setScrimColor(android.R.color.transparent);
                    frame.setX(moveFactor);
                    toolbar.setX(moveFactor);
                    //frame.bringChildToFront(drawerView);
                   // frame.requestLayout();
                    //overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);

                }
                else
                {
                    drawer.setScrimColor(android.R.color.transparent);
                    frame.setTranslationX(moveFactor);
                    toolbar.setTranslationX(moveFactor);

                   /* TranslateAnimation anim = new TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f);
                    anim.setDuration(0);
                    anim.setFillAfter(true);
                    frame.startAnimation(anim);

                    lastTranslate = moveFactor;*/
                   // overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                }
            }
        };


        drawer.setDrawerListener(toggle);

   /* }
        drawer.setDrawerListener(toggle);*/
        toggle.syncState();
        getSupportActionBar().setIcon(R.mipmap.logos);
        getSupportActionBar().setTitle(" BBA Ministries");
        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.nav_header_main, null, false);
        mExpandableListView.addHeaderView(listHeaderView);


       /* toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            }
        });*/

        try {
            PackageInfo info = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(),  // replace with your unique package name
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }




        Home comedy = new Home();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, comedy, comedy.getClass().getSimpleName()).commit();




        grouplist=new ArrayList<>();
       // ExpandableAdapter.GroupItem group=new ExpandableAdapter.GroupItem();
        childlist=new ArrayList<>();
        grouplist.add(new ExpandableAdapter.GroupItem("Home",childlist,R.mipmap.home));
        childlist=new ArrayList<>();
        childlist.add(new ExpandableAdapter.ChildItem("Our story"));
        childlist.add(new ExpandableAdapter.ChildItem("Our Belief"));
        childlist.add(new ExpandableAdapter.ChildItem("Our Pastors"));
        childlist.add(new ExpandableAdapter.ChildItem("Our Ministries"));

        grouplist.add(new ExpandableAdapter.GroupItem("About Us",childlist,R.mipmap.users));
        childlist=new ArrayList<>();
        childlist.add(new ExpandableAdapter.ChildItem("FB Covers"));
        childlist.add(new ExpandableAdapter.ChildItem("Promise Cards"));
        childlist.add(new ExpandableAdapter.ChildItem("Magazines"));


        grouplist.add(new ExpandableAdapter.GroupItem("Resources",childlist,R.mipmap.resource));
        childlist=new ArrayList<>();
        grouplist.add(new ExpandableAdapter.GroupItem("Events",childlist,R.mipmap.calender));
        childlist=new ArrayList<>();
        grouplist.add(new ExpandableAdapter.GroupItem("Prayer Request",childlist,R.mipmap.prayer));
        childlist=new ArrayList<>();
        grouplist.add(new ExpandableAdapter.GroupItem("Contact",childlist,R.mipmap.mail_logo));





        //setUiPageViewController();

       /* final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        intro_images.setAnimation(animationFadeIn);
*/





        adapter=new ExpandableAdapter(getApplicationContext());
        adapter.setData(grouplist);
        mExpandableListView.setAdapter(adapter);
        // mExpandableListView.setAdapter(mExpandableListAdapter);
        adapter.notifyDataSetChanged();


        // mExpandableListTitle = new ArrayList(mExpandableListData.keySet());
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.

                int count = parent.getExpandableListAdapter().getChildrenCount(groupPosition);



                GlobalClass.groupSelection=groupPosition;
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    mExpandableListView.collapseGroupWithAnimation(lastExpandedPosition);


                }
                lastExpandedPosition = groupPosition;

             Log.i("FFFFFFFFFFFFFFFFFFF","FFFFFFFFFFFFF"+GlobalClass.groupSelection);

/*
              if(GlobalClass.groupSelection==groupPosition)
                {


                    v.setBackgroundResource(R.drawable.toolbar_color);
                }else
                {

                    v.setBackgroundColor(Color.parseColor("#085464"));

                }*/

                           //




                GlobalClass.childSelection="";
                if (mExpandableListView.isGroupExpanded(groupPosition)) {



                    mExpandableListView.collapseGroupWithAnimation(groupPosition);




                 /*   ShapeDrawable sd2 = new ShapeDrawable(new RectShape());
                    sd2.getPaint().setColor(android.R.color.darker_gray);

                    mExpandableListView.setDivider(sd2);
*/



                } else if(count>0) {



                 /*   ShapeDrawable sd1 = new ShapeDrawable(new RectShape());
                    sd1.getPaint().setColor(android.R.color.transparent);
                    mExpandableListView.setDivider(sd1);*/

                  /*  adapter.setData(grouplist);
                    mExpandableListView.setAdapter(adapter);*/
                    //adapter.notifyDataSetChanged();
                    mExpandableListView.expandGroupWithAnimation(groupPosition);


                  /* View vv= adapter.getGroupView(groupPosition,true,v,parent);
                       ExpandableAdapter.GroupHolder.lines.setBackgroundColor(android.R.color.black);
*/
                }else
                {

                   // Toast.makeText(MainActivity.this, "Group has no childitems", Toast.LENGTH_SHORT).show();

                /*    if(GlobalClass.groupSelection==groupPosition)
                    {
                        v.setBackgroundResource(R.drawable.toolbar_color);
                    }else
                    {

                        v.setBackgroundColor(Color.parseColor("#085464"));
                    }*/

                  /*  for(int i=0;i<parent.getExpandableListAdapter().getGroupCount();i++)
                    {
                        if(mExpandableListView.isGroupExpanded(i))
                        {
                            mExpandableListView.collapseGroupWithAnimation(i);


                        }


                    }*/

                   // mExpandableListView.collapseGroupWithAnimation(groupPosition);
                  // transform(groupPosition);
                    adapter=new ExpandableAdapter(getApplicationContext());
                    adapter.setData(grouplist);
                    mExpandableListView.setAdapter(adapter);
                    // mExpandableListView.setAdapter(mExpandableListAdapter);
                    adapter.notifyDataSetChanged();


                    drawer.closeDrawers();

                    ExpandableAdapter.GroupItem item =adapter.getGroup(groupPosition);


                    if(item.categoryname.equalsIgnoreCase("Contact"))
                    {
                        ContactUS comedy=new ContactUS();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, comedy, comedy.getClass().getSimpleName()).commit();
                        getSupportActionBar().setTitle("  BBA Ministries");
                    }else  if(item.categoryname.equalsIgnoreCase("Events"))
                   {

                       Intent i=new Intent(MainActivity.this, Events.class);
                       startActivity(i);
                       overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                       finish();

                    }else  if(item.categoryname.equalsIgnoreCase("Prayer Request"))
                    {

                        PrayerRequest comedy=new PrayerRequest();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, comedy, comedy.getClass().getSimpleName()).commit();
                        getSupportActionBar().setTitle("  BBA Ministries");

                    }
                    else
                    {
                        Home comedy=new Home();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, comedy, comedy.getClass().getSimpleName()).commit();
                        getSupportActionBar().setTitle("  BBA Ministries");

                    }




/*
                    Intent i=new Intent(MainActivity.this,TestActivity.class);
                    startActivity(i);*/
                }
                return true;
            }

        });

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                ExpandableAdapter.ChildItem item = adapter.getChild(groupPosition,childPosition);



                GlobalClass.childSelection=item.subcategoryname;
                adapter=new ExpandableAdapter(getApplicationContext());
                adapter.setData(grouplist);
                mExpandableListView.setAdapter(adapter);
                // mExpandableListView.setAdapter(mExpandableListAdapter);
                adapter.notifyDataSetChanged();



                mExpandableListView.expandGroup(groupPosition);

                drawer.closeDrawers();

//FB Covers
                Log.i("ZZZZZZZZZZ","ZZZZZZZ"+item.subcategoryname);
                if(item.subcategoryname.equalsIgnoreCase("Our story"))
                {
                    OurStory comedy = new OurStory();

                    Bundle bundle = new Bundle();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, comedy, comedy.getClass().getSimpleName()).commit();

                    getSupportActionBar().setTitle("  Our Story");
                }else if(item.subcategoryname.equalsIgnoreCase("Our Belief"))
                {
                    OurBelief comedy = new OurBelief();

                    Bundle bundle = new Bundle();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, comedy, comedy.getClass().getSimpleName()).commit();
                    getSupportActionBar().setTitle("  Our Belief");
                }else if(item.subcategoryname.equalsIgnoreCase("FB Covers"))
                {
                    FB_covers comedy = new FB_covers();

                    Bundle bundle = new Bundle();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, comedy, comedy.getClass().getSimpleName()).commit();
                    getSupportActionBar().setTitle("  Facebook Covers");
                }else if(item.subcategoryname.equalsIgnoreCase("Our Ministries"))
                {
                    OurMinistries comedy = new OurMinistries();

                    Bundle bundle = new Bundle();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, comedy, comedy.getClass().getSimpleName()).commit();
                    getSupportActionBar().setTitle("  Our Ministries");
                }else if(item.subcategoryname.equalsIgnoreCase("Our Pastors"))
                {
                    OurPastors comedy = new OurPastors();

                    Bundle bundle = new Bundle();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, comedy, comedy.getClass().getSimpleName()).commit();
                    getSupportActionBar().setTitle("  Our Pastors");
                }



                else if(item.subcategoryname.equalsIgnoreCase("Promise Cards"))
                {

                    Intent i=new Intent(MainActivity.this, PromiseCardActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    finish();
                }
                else if(item.subcategoryname.equalsIgnoreCase("Magazines"))
                {

                    Intent i=new Intent(MainActivity.this, PdfDownloadTask.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    finish();
                }



                return false;
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        } else {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure want to exit?");
                    alertDialogBuilder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                    GlobalClass.groupSelection=-1;
                                    GlobalClass.childSelection="";
                                    arg0.dismiss();
                                    ActivityCompat.finishAffinity(MainActivity.this);
                                }
                            });

            alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

   /* @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/






}
