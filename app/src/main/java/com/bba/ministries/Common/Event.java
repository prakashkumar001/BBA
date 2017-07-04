package com.bba.ministries.Common;

/**
 * Created by Prakash on 12/6/2016.
 */

public class Event {
   public String id,title,datetime,eventstart,contact,place,description,latitude,longitude;

    public Event(String id, String title, String datetime, String eventstart, String contact, String place, String description, String latitude, String longitude)
    {
        this.id=id;
        this.title=title;
        this.datetime=datetime;
        this.eventstart=eventstart;
        this.contact=contact;
        this.place=place;
        this.latitude=latitude;
        this.longitude=longitude;
        this.description=description;
    }
}
