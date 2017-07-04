package com.bba.ministries.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.bba.ministries.R;

/**
 * Created by v-62 on 11/19/2016.
 */

public class OurStory extends Fragment {
    WebView text;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.our_story, container, false);

        text=(WebView) view.findViewById(R.id.web);
        text.loadUrl("file:///android_asset/ourstory.html");
        return view;
    }
}
