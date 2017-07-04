package com.bba.ministries.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.bba.ministries.R;

/**
 * Created by v-62 on 12/9/2016.
 */

public class OurMinistries  extends Fragment {
    WebView text;
    TextView title;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.our_story, container, false);

        text=(WebView) view.findViewById(R.id.web);
        title=(TextView) view.findViewById(R.id.title);
        title.setText("Our Ministries");
        text.loadUrl("file:///android_asset/ourministries.html");
        return view;
    }
}
