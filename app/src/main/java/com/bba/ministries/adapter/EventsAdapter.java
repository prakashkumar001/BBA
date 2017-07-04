package com.bba.ministries.adapter;

/**
 * Created by v-62 on 12/3/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bba.ministries.Common.Event;
import com.bba.ministries.EventDetailActivity;
import com.bba.ministries.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by v-62 on 12/2/2016.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder>{



    ArrayList<Event> data=new ArrayList<Event>();
    ImageLoader loader;
    //ArrayList<String> monthInteger=new ArrayList<String>();
   // ArrayList<String> monthName=new ArrayList<String>();
    String monthInteger[];
    String monthName[];
    Activity context;
    //String ArrayList<String> monthInteger=new ArrayList<String>();[];

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,dates,monthyear,place,times;

        public ImageView image;
        public LinearLayout view,download;




        public MyViewHolder(View view) {
            super(view);
            //sellerprice = (TextView) view.findViewById(R.id.sellerprice);

           // image = (ImageView) view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.title);
            dates = (TextView) view.findViewById(R.id.dates);
            monthyear = (TextView) view.findViewById(R.id.monthyear);
            place = (TextView) view.findViewById(R.id.place);
            times = (TextView) view.findViewById(R.id.times);
            // view=(LinearLayout) view.findViewById(R.id.view);
            ///  download=(LinearLayout) view.findViewById(R.id.download);


        }
    }


    public EventsAdapter(Activity context, ArrayList<Event> data) {

        this.data=data;
        loader=ImageLoader.getInstance();

        Log.i("RRRRRRRRRRRRRRRRR","RRRRRRRRRRRRRRR");


        monthInteger=new String[] {"01","02","03","04","05","06","07","08","09","10","11","12"};
        monthName=new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};
this.context=context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.events_items, parent, false);




        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher) // resource or drawable
                .showImageForEmptyUri(R.mipmap.ic_launcher) // resource or drawable
                .showImageOnFail(R.mipmap.ic_launcher) // resource or drawable
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(10)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .build();

        //loader.displayImage(data.get(position).image,holder.image,options);

        String datetime=data.get(position).datetime;
        String starttime=data.get(position).eventstart;

        String datearr[]=datetime.split("-");
        String year=datearr[0];
        String month=datearr[1];
        final String date=datearr[2];

        int index= Arrays.asList(monthInteger).indexOf(month);


        Log.i("IIIIIIIIIIIIIIIIIII","IIIIIIIIIIIIIII"+index);
         holder.title.setText(data.get(position).title);
        holder.dates.setText(date);
        holder.monthyear.setText(monthName[index]+" "+year);
        holder.place.setText(data.get(position).place);
        String starttimearr[]=starttime.split(":");
        String starthour=starttimearr[0];
        String startmin=starttimearr[1];



        holder.times.setText(starthour+":"+startmin);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                String starttimearr[]=data.get(position).eventstart.split(":");
                String starthour=starttimearr[0];
                String startmin=starttimearr[1];



                String datetime=data.get(position).datetime;

                String datearr[]=datetime.split("-");
                String year=datearr[0];
                String month=datearr[1];
                final String date=datearr[2];

                int index= Arrays.asList(monthInteger).indexOf(month);


                Intent i=new Intent(context, EventDetailActivity.class);
                i.putExtra("latitude",data.get(position).latitude);
                i.putExtra("longitude",data.get(position).longitude);
                i.putExtra("description",data.get(position).description);
                i.putExtra("contact",data.get(position).contact);
                i.putExtra("title",data.get(position).title);
                i.putExtra("eventtime",data.get(position).eventstart);
                i.putExtra("place",data.get(position).place);
                i.putExtra("date",date);
                i.putExtra("monthyear",monthName[index]+" "+year);

                context.startActivity(i);
                context.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                context.finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}