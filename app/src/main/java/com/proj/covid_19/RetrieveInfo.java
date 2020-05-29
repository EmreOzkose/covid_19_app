package com.proj.covid_19;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RetrieveInfo {

    String url;
    String response_text;
    String TAG = "DEVELOPER";

    private Context context;
    RequestQueue queue;
    int counter;

    private HashMap<String, String[]> cities_info;


    public RetrieveInfo(Context context, String url){
        this.url = url;
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
        this.response_text = null;
        this.cities_info = new HashMap<String, String[]>();
        counter = 0;
    }

    public void get_response(){
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, this.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        response_text = response;
                        process_response();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
                Log.d(TAG, "That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        this.queue.add(stringRequest);
    }

    public void process_response() {
        if (this.response_text == null) {Log.d(TAG, "response text is null"); return;};

        /*
        // find country names
        String pattern_st = "country/.*?/";
        Pattern pattern = Pattern.compile(pattern_st);
        Matcher matcher = pattern.matcher(response_text);

        Set<String> county_names = new HashSet<String>();
        while (matcher.find()){
            String name = matcher.group().split("/")[1];
            county_names.add(name);
        }

        Object[] country_array = county_names.toArray();
        Arrays.sort(country_array);
        for (int i=0; i<county_names.size(); i++){
            Log.d(TAG, (String) country_array[i]);
        }*/

        HashMap<String, String[]> cities_info = new HashMap<String, String[]>();

        // find info
        String pattern_st = "<tr style=.*?</tr>";
        Pattern pattern = Pattern.compile(pattern_st);
        Matcher matcher = pattern.matcher(response_text.replaceAll("\n", ""));

        Log.d(TAG, "LA");
        while (matcher.find()){
            String name = matcher.group();

            name = name.replaceAll("<tr .*?country/", "");
            name = name.replaceAll("</tr>", "");
            name = name.replaceAll(",", ".");
            name = name.replaceAll("<td.*?>", ",");
            name = name.replaceAll(".*?/\">", "");
            name = name.replaceAll("</td>", "");
            name = name.replaceAll("</a>", "");

            if (name.contains("<tr")) continue;

            Log.d("LA", name);

            String [] info_array = name.split(",");
            // Log.d(TAG, name);
            cities_info.put(info_array[0], info_array);

        }

        if (!cities_info.isEmpty())
            setCities_info(cities_info);

        // Log.d("DEVOP", String.valueOf(getCities_info().size()));
    }

    public HashMap<String, String[]> getCities_info() {
        return cities_info;
    }

    public void setCities_info(HashMap<String, String[]> cities_info) {
        this.cities_info = cities_info;
    }

    public TreeMap<String, String[]> sort_by_key()
    {
        // TreeMap to store values of HashMap
        TreeMap<String, String[]> sorted = new TreeMap<>();

        // Copy all data from hashMap into TreeMap
        sorted.putAll(getCities_info());

        return sorted;
    }


}
