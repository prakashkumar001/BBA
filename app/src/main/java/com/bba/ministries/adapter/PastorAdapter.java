package com.bba.ministries.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bba.ministries.Common.GlobalClass;
import com.bba.ministries.Common.Pastor;
import com.bba.ministries.R;
import com.bba.ministries.WebSite;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by v-62 on 12/9/2016.
 */

public class PastorAdapter extends RecyclerView.Adapter<PastorAdapter.MyViewHolder>{

    private static final int REQUEST_PERMISSIONS = 20;

    private SparseIntArray mErrorString;

    ArrayList<Pastor> data=new ArrayList<Pastor>();
    ImageLoader loader;

    Activity context;
    int callposition;
    //String ArrayList<String> monthInteger=new ArrayList<String>();[];

    public class MyViewHolder extends RecyclerView.ViewHolder {
       // public TextView title,dates,monthyear,place,times;

        public ImageView image,fb,twitter,email,phone;
        public LinearLayout lay;
        public View fbview,twitterview,emailview;
        public TextView title,designation;




        public MyViewHolder(View view) {
            super(view);
            //sellerprice = (TextView) view.findViewById(R.id.sellerprice);

            image = (ImageView) view.findViewById(R.id.image);
            fb = (ImageView) view.findViewById(R.id.fb);
            twitter = (ImageView) view.findViewById(R.id.twitter);
            email = (ImageView) view.findViewById(R.id.email);
            phone = (ImageView) view.findViewById(R.id.phone);
            lay=(LinearLayout) view.findViewById(R.id.lay);
            fbview=(View) view.findViewById(R.id.fbview);
            twitterview=(View) view.findViewById(R.id.twitterview);
            emailview=(View) view.findViewById(R.id.emailview);
            title=(TextView) view.findViewById(R.id.title);
            designation=(TextView) view.findViewById(R.id.designation);


          /*  title = (TextView) view.findViewById(R.id.title);
            dates = (TextView) view.findViewById(R.id.dates);
            monthyear = (TextView) view.findViewById(R.id.monthyear);
            place = (TextView) view.findViewById(R.id.place);
            times = (TextView) view.findViewById(R.id.times);*/
            // view=(LinearLayout) view.findViewById(R.id.view);
            ///  download=(LinearLayout) view.findViewById(R.id.download);


        }
    }


    public PastorAdapter(Activity context, ArrayList<Pastor> data) {

        this.data=data;
        loader=ImageLoader.getInstance();

        this.context=context;
        mErrorString = new SparseIntArray();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.our_pastors_two, parent, false);

        MyViewHolder mViewHold = new MyViewHolder(itemView);



        return mViewHold;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loadimage) // resource or drawable
                .showImageForEmptyUri(R.drawable.loadimage) // resource or drawable
                .showImageOnFail(R.drawable.loadimage) // resource or drawable
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(10)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .build();

        loader.displayImage(GlobalClass.imageUrl+"pastors/"+data.get(position).pic,holder.image,options);
        holder.title.setText(data.get(position).name);
        holder.designation.setText(data.get(position).designation);

        if(data.get(position).facebook.equalsIgnoreCase("") || data.get(position).twitter.equalsIgnoreCase(""))
        {
            if(data.get(position).facebook.equalsIgnoreCase("")&&data.get(position).twitter.equalsIgnoreCase(""))
            {
                data.get(position).setIsfbSelected(false);
                data.get(position).setIstwitterSelected(false);
                holder.fb.setVisibility(View.GONE);
                holder.twitter.setVisibility(View.GONE);
                holder.lay.setWeightSum(2f);
                holder.fbview.setVisibility(View.GONE);
                holder.twitterview.setVisibility(View.GONE);


            }else if(data.get(position).facebook.equalsIgnoreCase(""))
            {
                data.get(position).setIsfbSelected(false);
                holder.fb.setVisibility(View.GONE);
                holder.lay.setWeightSum(3f);
                holder.fbview.setVisibility(View.GONE);



            }else if(data.get(position).twitter.equalsIgnoreCase(""))
            {
                data.get(position).setIstwitterSelected(false);
                holder.twitter.setVisibility(View.GONE);
                holder.lay.setWeightSum(3f);
                holder.twitterview.setVisibility(View.GONE);

            }

        }

        if(!data.get(position).facebook.equalsIgnoreCase("") || !data.get(position).twitter.equalsIgnoreCase(""))
        {
            if(!data.get(position).facebook.equalsIgnoreCase("")&& !data.get(position).twitter.equalsIgnoreCase(""))
            {
                Log.i("FFFFFFFFFFFFFFFFf","FFFFFFFFFFFFFf"+position);
                data.get(position).setIsfbSelected(true);
                data.get(position).setIstwitterSelected(true);
                holder.fb.setVisibility(View.VISIBLE);
                holder.twitter.setVisibility(View.VISIBLE);
                holder.lay.setWeightSum(4f);
                holder.fbview.setVisibility(View.VISIBLE);
                holder.twitterview.setVisibility(View.VISIBLE);


            }else if(!data.get(position).facebook.equalsIgnoreCase(""))
            {


                data.get(position).setIsfbSelected(true);
                holder.fb.setVisibility(View.VISIBLE);
                holder.lay.setWeightSum(3f);
                holder.fbview.setVisibility(View.VISIBLE);



            }else if(!data.get(position).twitter.equalsIgnoreCase(""))
            {

                data.get(position).setIstwitterSelected(true);
                holder.twitter.setVisibility(View.VISIBLE);

                holder.lay.setWeightSum(3f);
                holder.twitterview.setVisibility(View.VISIBLE);


            }
        }




    holder.fb.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i=new Intent(context, WebSite.class);
            i.putExtra("url",data.get(position).facebook);
            i.putExtra("title","Facebook");
            context.startActivity(i);
            context.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
        }
    });


        holder.twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, WebSite.class);
                i.putExtra("url",data.get(position).twitter);
                i.putExtra("title","Twitter");
                context.startActivity(i);
                context.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }
        });


        holder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(Intent.ACTION_SEND);

                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{data.get(position).email});
                //intent.putExtra(Intent.EXTRA_SUBJECT, "BBA MINISTRIES");
                intent.putExtra(Intent.EXTRA_TEXT, "");

                intent.setType("message/rfc822");

                context.startActivity(Intent.createChooser(intent, "Select Email Sending App :"));
            }
        });

     holder.phone.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             callposition=position;


             requestAppPermissions(new
                             String[]{Manifest.permission.CALL_PHONE}, R.string
                             .runtime_permissions_txt
                     , REQUEST_PERMISSIONS);




         }
     });


       /* if(data.get(position).facebook.equalsIgnoreCase(""))
        {
            holder.fb.setVisibility(View.GONE);
        }*/

        if(data.get(position).istwitterSelected())
        {
            holder.twitter.setVisibility(View.VISIBLE);
            holder.twitterview.setVisibility(View.VISIBLE);
        }

        if(data.get(position).isfbSelected())
        {
            holder.fb.setVisibility(View.VISIBLE);
            holder.fbview.setVisibility(View.VISIBLE);
        }

       /* if(data.get(position).isfbSelected() || data.get(position).istwitterSelected())
        {
            if(data.get(position).isfbSelected()&& !data.get(position).istwitterSelected())
            {
                holder.fb.setVisibility(View.VISIBLE);
                holder.fbview.setVisibility(View.VISIBLE);
                holder.twitter.setVisibility(View.VISIBLE);
                holder.twitterview.setVisibility(View.VISIBLE);

            }else if(data.get(position).isfbSelected())
            {
                holder.fb.setVisibility(View.VISIBLE);
                holder.fbview.setVisibility(View.VISIBLE);
               // holder.twitter.setVisibility(View.VISIBLE);
               // holder.twitterview.setVisibility(View.VISIBLE);

            }else if(data.get(position).istwitterSelected())
            {
                //holder.fb.setVisibility(View.VISIBLE);
               // holder.fbview.setVisibility(View.VISIBLE);
                holder.twitter.setVisibility(View.VISIBLE);
                holder.twitterview.setVisibility(View.VISIBLE);

            }

        }*/


       /* if(data.get(position).isfbSelected() && data.get(position).istwitterSelected())
        {
            holder.fb.setVisibility(View.VISIBLE);
            holder.fbview.setVisibility(View.VISIBLE);
            holder.twitter.setVisibility(View.VISIBLE);
            holder.twitterview.setVisibility(View.VISIBLE);
        }else if()
        if(data.get(position).istwitterSelected())
        {
            holder.twitter.setVisibility(View.VISIBLE);
            holder.twitterview.setVisibility(View.VISIBLE);
        }*/




    }

    @Override
    public int getItemCount() {
        return data.size();
    }




    public void onPermissionsGranted(final int requestCode) {
       // Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();

    callfuntion();

    }
    public void callfuntion()
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+data.get(callposition).phone));
        context.startActivity(callIntent);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted(requestCode);

        } else {
            Snackbar.make(context.findViewById(android.R.id.content), mErrorString.get(requestCode),
                    Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setData(Uri.parse("package:" + context.getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            context.startActivity(intent);
                        }
                    }).show();
        }
    }

    public void requestAppPermissions(final String[] requestedPermissions,
                                      final int stringId, final int requestCode) {
        mErrorString.put(requestCode, stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(context, permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(context, permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                Snackbar.make(context.findViewById(android.R.id.content), stringId,
                        Snackbar.LENGTH_INDEFINITE).setAction("GRANT",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(context, requestedPermissions, requestCode);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(context, requestedPermissions, requestCode);
            }
        } else {
            onPermissionsGranted(requestCode);

        }
    }



}


