package com.bba.ministries.Common;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by v-62 on 11/30/2016.
 */

public class GlobalClass extends Application {

    public static int groupSelection=-1;

    public static String childSelection="";
    public static final String TAG = GlobalClass.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private static GlobalClass mInstance;
    public static String webUrl="";
    public static String imageUrl="";
    public void onCreate() {



        super.onCreate();
        mInstance = this;
        initImageLoader(getApplicationContext());

    }


    public static synchronized GlobalClass getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public static void initImageLoader(Context context) {



        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)

                .threadPriority(Thread.NORM_PRIORITY - 2)

                .denyCacheImageMultipleSizesInMemory()

                .discCacheFileNameGenerator(new Md5FileNameGenerator())

                .tasksProcessingOrder(QueueProcessingType.LIFO)

                .build();



        ImageLoader.getInstance().init(config);

    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


}
