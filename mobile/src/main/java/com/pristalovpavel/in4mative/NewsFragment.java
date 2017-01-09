package com.pristalovpavel.in4mative;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.pristalovpavel.in4mative.view.NewsView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    public static final int NEWS_COUNT = 5;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        if(v != null && v instanceof LinearLayout)
        {
            for(int i= 0; i < NEWS_COUNT; ++i)
                ((LinearLayout) v).addView(new NewsView(getContext()));
        }
        return v;
    }
}
