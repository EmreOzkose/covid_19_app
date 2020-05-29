package com.proj.covid_19.ui.home;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.proj.covid_19.R;
import com.proj.covid_19.RetrieveInfo;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Map;
import java.util.TreeMap;

public class HomeFragment extends Fragment{

    private HomeViewModel homeViewModel;
    private int counter = 0 ;
    RetrieveInfo retrieveInfo;
    private AdView mAdView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mAdView = root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
*/
        String url ="https://www.worldometers.info/coronavirus/";
        this.retrieveInfo = new RetrieveInfo(root.getContext(), url);
        this.retrieveInfo.get_response();

        content(root);

        return root;
    }

    public void content(View root){
        Log.d("DEVOP", "context");
        String first_line = String.format("%1$-25s%2$-10s%3$-10s", getString(R.string.city_name), getString(R.string.total), getString(R.string.total));
        String second_line = String.format("%1$-25s%2$-10s%3$-10s\n", "", getString(R.string.case_case), getString(R.string.death));

        TextView textView0 = new TextView(root.getContext());
        textView0.setTypeface(Typeface.MONOSPACE);
        textView0.setText(first_line);

        TextView textView1 = new TextView(root.getContext());
        textView1.setTypeface(Typeface.MONOSPACE);
        textView1.setText(second_line);

        LinearLayout l1 = (LinearLayout) root.findViewById(R.id.city_linear_layout);
        if(((LinearLayout) l1).getChildCount() > 0) ((LinearLayout) l1).removeAllViews();

        l1.addView(textView0);
        l1.addView(textView1);

        TreeMap<String, String[]> city_info = this.retrieveInfo.sort_by_key();

        // Get keys and values
        TextView textView;
        counter = 0;
        for (Map.Entry<String, String[]> entry : city_info.entrySet()) {
            String k = entry.getKey();
            String[] v = entry.getValue();

            textView = new TextView(root.getContext());
            String gfg2 = String.format("%1$-25s%2$-10s%3$-10s", k.trim(), v[1].trim(), v[3].trim());
            textView.setText(gfg2);
            textView.setTypeface(Typeface.MONOSPACE);
            if (counter++ % 2 == 0) {
                textView.setTextColor(Color.BLACK);
                textView.setBackgroundColor(Color.GRAY);
            }
            else {
                textView.setTextColor(Color.GRAY);
                // textView.setBackgroundColor(Color.BLACK);
            }
            textView.setGravity(Gravity.START);

            l1.addView(textView);

        }
        l1.invalidate();

        refresh(root, 2000);
    }

    private void refresh(final View root, int milliseconds){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                content(root);
            }
        };

        handler.postDelayed(runnable, milliseconds);
    }



}
