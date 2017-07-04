package com.bba.ministries.adapter;

/**
 * Created by v-62 on 12/3/2016.
 */

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
import android.widget.TextView;
import android.widget.Toast;

import com.bba.ministries.Common.GlobalClass;
import com.bba.ministries.Common.PromiseCard;
import com.bba.ministries.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by v-62 on 12/2/2016.
 */

public class FB_CoversAdapter extends RecyclerView.Adapter<FB_CoversAdapter.MyViewHolder> {


    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    ArrayList<PromiseCard> data=new ArrayList<PromiseCard>();
    ImageLoader loader;
    Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public ImageView image;
        public LinearLayout view,download;




        public MyViewHolder(View view) {
            super(view);
            //sellerprice = (TextView) view.findViewById(R.id.sellerprice);

            image = (ImageView) view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.title);
           // view=(LinearLayout) view.findViewById(R.id.view);
            download=(LinearLayout) view.findViewById(R.id.download);


        }
    }


    public FB_CoversAdapter(Context context, ArrayList<PromiseCard> data) {

        this.data=data;
        loader=ImageLoader.getInstance();
        this.context=context;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fbcovers_items, parent, false);




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

        loader.displayImage(GlobalClass.imageUrl+"facebook-covers/"+data.get(position).image,holder.image,options);
        holder.title.setText(data.get(position).title);
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap bm=loader.loadImageSync(GlobalClass.imageUrl+"facebook-covers/"+data.get(position).image);
                FileOutputStream fOut = null;



                Uri outputFileUri;
                try {
                    File root = new File(Environment.getExternalStorageDirectory()
                            + File.separator + "BBA_Ministries" + File.separator+ "Facebook Covers" + File.separator);
                    root.mkdirs();
                    File sdImageMainDirectory = new File(root, data.get(position).title+".jpg");
                   // outputFileUri = Uri.fromFile(sdImageMainDirectory);
                   fOut = new FileOutputStream(sdImageMainDirectory);

                    try {
                        bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (Exception e) {
                    }


                    showBigNotification(bm,"Successfully saved image",sdImageMainDirectory);
                   //dialog.dismiss();
                   /* Toast.makeText(context, "Successfully saved image.",
                            Toast.LENGTH_SHORT).show();*/
                } catch (Exception e) {
                   // dialog.dismiss();
                    Toast.makeText(context, "Error occured. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void showBigNotification(Bitmap bitmap, String message, File root) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(root), "image/*");
         PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
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