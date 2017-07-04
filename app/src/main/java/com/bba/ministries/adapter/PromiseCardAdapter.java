package com.bba.ministries.adapter;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bba.ministries.Common.GlobalClass;
import com.bba.ministries.Common.PromiseCard;
import com.bba.ministries.PromiseCardViewPage;
import com.bba.ministries.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by v-62 on 12/2/2016.
 */

public class PromiseCardAdapter extends RecyclerView.Adapter<PromiseCardAdapter.MyViewHolder> {



    ArrayList<PromiseCard> data=new ArrayList<PromiseCard>();
    ImageLoader loader;
    Activity context;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //public TextView sellerprice;

        public ImageView image;
        public LinearLayout views,download;


        public MyViewHolder(View view) {
            super(view);
            //sellerprice = (TextView) view.findViewById(R.id.sellerprice);

            image = (ImageView) view.findViewById(R.id.image);
            views=(LinearLayout) view.findViewById(R.id.views);
            download=(LinearLayout) view.findViewById(R.id.download);

            views.setOnClickListener(this);
            download.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            switch (v.getId())
            {
                case R.id.views:
                    Intent i=new Intent(context, PromiseCardViewPage.class);
                    i.putExtra("title",data.get(getAdapterPosition()).title);
                    i.putExtra("image",GlobalClass.imageUrl+"promise-cards/"+data.get(getAdapterPosition()).image);
                    i.putExtra("description",data.get(getAdapterPosition()).description);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                    context.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    context.finish();

                    break;

                case R.id.download:

                    Bitmap bm=loader.loadImageSync(GlobalClass.imageUrl+"promise-cards/"+data.get(getAdapterPosition()).image);
                    OutputStream fOut = null;
                    Uri outputFileUri;
                    try {
                        File root = new File(Environment.getExternalStorageDirectory()
                                + File.separator + "BBA_Ministries" + File.separator+ "Promise Card" + File.separator+ data.get(getAdapterPosition()).year + File.separator);
                        root.mkdirs();
                        File sdImageMainDirectory = new File(root, data.get(getAdapterPosition()).title+".jpg");
                        outputFileUri = Uri.fromFile(sdImageMainDirectory);
                        fOut = new FileOutputStream(sdImageMainDirectory);
                  /*  Toast.makeText(context, "Successfully saved image.",
                            Toast.LENGTH_SHORT).show();*/
                        showBigNotification(bm,"Successfully saved image",sdImageMainDirectory);
                    } catch (Exception e) {
                        Toast.makeText(context, "Error occured. Please try again later.",
                                Toast.LENGTH_SHORT).show();
                    }
                    try {
                        bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (Exception e) {
                    }

                    break;

                default:
                    break;


            }

        }

    }


    public PromiseCardAdapter(Activity context, ArrayList<PromiseCard> data) {

     this.data=data;
        loader=ImageLoader.getInstance();
        this.context=context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.promisecard_listitem, parent, false);




        return new MyViewHolder(itemView);
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


        loader.displayImage(GlobalClass.imageUrl+"promise-cards/"+data.get(position).image,holder.image,options);

       /* loader.loadImage(GlobalClass.imageUrl+"promise-cards/"+data.get(position).image, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                // Do whatever you want with Bitmap

                holder.image.setImageBitmap(loadedImage);


            }
        });*/
            // holder.image.setImageResource(data.get(position).image);

      /*  holder.views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Activity activity = (Activity) context;
                Intent i=new Intent(context, PromiseCardViewPage.class);
                i.putExtra("title",data.get(position).title);
                i.putExtra("image",data.get(position).image);
                i.putExtra("description",data.get(position).description);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                context.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                context.finish();
            }
        });*/

      /*  holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bm=loader.loadImageSync(data.get(position).image);
                OutputStream fOut = null;
                Uri outputFileUri;
                try {
                    File root = new File(Environment.getExternalStorageDirectory()
                            + File.separator + "BBA_Ministries" + File.separator+ "Promise Card" + File.separator+ data.get(position).year + File.separator);
                    root.mkdirs();
                    File sdImageMainDirectory = new File(root, data.get(position).title+".jpg");
                    outputFileUri = Uri.fromFile(sdImageMainDirectory);
                    fOut = new FileOutputStream(sdImageMainDirectory);
                  *//*  Toast.makeText(context, "Successfully saved image.",
                            Toast.LENGTH_SHORT).show();*//*
                    showBigNotification(bm,"Successfully saved image",sdImageMainDirectory);
                } catch (Exception e) {
                    Toast.makeText(context, "Error occured. Please try again later.",
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
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    private void showBigNotification(Bitmap bitmap, String message, File file) {

        //Intent  intent=new Intent(Intent.ACTION_VIEW);
       // Intent intent = new Intent();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_ONE_SHOT
                );

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context);
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(message);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        Notification notification;
        notification = mBuilder.setSmallIcon(R.mipmap.logos).setTicker(message).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(message)
                .setContentIntent(resultPendingIntent)
                .setStyle(bigPictureStyle)
                .setSmallIcon(R.mipmap.logos)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.logos))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID_BIG_IMAGE, notification);
    }
}