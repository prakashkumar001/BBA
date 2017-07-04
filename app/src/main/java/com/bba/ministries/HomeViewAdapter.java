package com.bba.ministries;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bba.ministries.Common.GlobalClass;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Prakash on 11/6/2016.
 */

public class HomeViewAdapter extends PagerAdapter {

    private Context mContext;
    ArrayList<String> data;
    DisplayImageOptions options;
    ImageLoader loader;
    public HomeViewAdapter(Context mContext, ArrayList<String> data) {
        this.mContext = mContext;
        this.data = data;
        loader=ImageLoader.getInstance();

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item, container, false);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loadimage) // resource or drawable
                .showImageForEmptyUri(R.drawable.loadimage) // resource or drawable
                .showImageOnFail(R.drawable.loadimage) // resource or drawable
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(10)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .build();
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);


        loader.displayImage(GlobalClass.imageUrl+"sliders/"+data.get(position),imageView,options);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}

