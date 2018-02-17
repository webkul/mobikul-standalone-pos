//package com.webkul.mobikul.mobikulstandalonepos;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.support.v4.util.LruCache;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.ImageLoader;
//import com.android.volley.toolbox.Volley;
//
//public class MySingleton {
//    private static MySingleton mInstance;
//    private Context mCtx;
//    private static Request previousRequest;
//    private RequestQueue mRequestQueue;
//    private ImageLoader mImageLoader;
//
//    private MySingleton(Context context) {
//        mCtx = context;
//        mRequestQueue = getRequestQueue();
//
//        mImageLoader = new ImageLoader(mRequestQueue,
//                new ImageLoader.ImageCache() {
//                    private final LruCache<String, Bitmap> cache = new LruCache<>(20);
//
//                    @Override
//                    public Bitmap getBitmap(String url) {
//                        return cache.get(url);
//                    }
//
//                    @Override
//                    public void putBitmap(String url, Bitmap bitmap) {
//                        cache.put(url, bitmap);
//                    }
//                });
//    }
//
//    public static synchronized MySingleton getInstance(Context context) {
//        if (mInstance == null) {
//            mInstance = new MySingleton(context.getApplicationContext());
//        }
//        return mInstance;
//    }
//
//
//
//    public RequestQueue getRequestQueue() {
//        if (mRequestQueue == null) {
//            // getApplicationContext() is key, it keeps you from leaking the
//            // Activity or BroadcastReceiver if someone passes one in.
//            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
//            mRequestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
//                @Override
//                public void onRequestFinished(Request<Object> request) {
//                    previousRequest = request;
//                }
//            });
//        }
//        return mRequestQueue;
//    }
//
//    static Request getPreviousRequest(){
//        if(previousRequest!=null){
//            return previousRequest;
//        }
//        return null;
//    }
//
//    public <T> void addToRequestQueue(Request<T> req) {
//        getRequestQueue().add(req);
//    }
//
//    public ImageLoader getImageLoader() {
//        return mImageLoader;
//    }
//
//}