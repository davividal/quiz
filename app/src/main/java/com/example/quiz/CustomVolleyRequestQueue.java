package com.example.quiz;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

/**
 * Created by davi on 18/11/2016.
 */
public class CustomVolleyRequestQueue {
    private static CustomVolleyRequestQueue ourInstance;
    private RequestQueue requestQueue;
    private Context context;

    private CustomVolleyRequestQueue(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
    }

    public static CustomVolleyRequestQueue getInstance() {
        return ourInstance;
    }

    public static synchronized CustomVolleyRequestQueue getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new CustomVolleyRequestQueue(context);
        }

        return ourInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            Cache cache = new DiskBasedCache(context.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            requestQueue = new RequestQueue(cache, network);
            requestQueue.start();
        }

        return requestQueue;
    }
}
