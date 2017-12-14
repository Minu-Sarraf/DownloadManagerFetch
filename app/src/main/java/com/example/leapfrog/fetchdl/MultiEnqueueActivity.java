package com.example.leapfrog.fetchdl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.tonyodev.fetch.Fetch;
import com.tonyodev.fetch.listener.FetchListener;
import com.tonyodev.fetch.request.Request;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonyofrancis on 1/29/17.
 */

public class MultiEnqueueActivity extends AppCompatActivity  {
     ImageView imageView;
    String filePath;
    Fetch fetch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.image);
        new Fetch.Settings(this)
                .setAllowedNetwork(Fetch.NETWORK_ALL)
                .enableLogging(true)
                .setConcurrentDownloadsLimit(1)
                .apply();

        fetch = Fetch.newInstance(this);
        List<Request> requests = new ArrayList<>();

        final String url = "http://www.news1.co.il/uploadFiles/9760f342e7.jpg";

        final int size = 15;

        for(int x = 0; x < size; x++) {

             filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                     .toString()
                    .concat(""+(x+1))
                    .concat(".jpg");

            Request request = new Request(url,filePath);
            requests.add(request);
        }

        fetch.enqueue(requests);

        Toast.makeText(this,"Enqueued " + size + " requests. Check Logcat for" +
                "progress status", Toast.LENGTH_LONG).show();
        fetch.addFetchListener(new FetchListener() {
            @Override
            public void onUpdate(long id, int status, int progress, long downloadedBytes, long fileSize, int error) {
                if (status == Fetch.STATUS_DONE) {
                    Bitmap bmp = BitmapFactory.decodeFile(filePath);
                    imageView.setImageBitmap(bmp);
                }

                Log.d("fetchDebug","id:" + id + ",status:" + status + ",progress:" + progress
                        + ",error:" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .toString());

                Log.i("fetchDebug","id: " + id + " downloadedBytes: " + downloadedBytes + " / fileSize: " + fileSize);
            }
        });


        fetch.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("fetchDebug","id:"
                + ",error:" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString());

        Bitmap bmp = BitmapFactory.decodeFile(filePath);
        if(bmp!=null)
            imageView.setImageBitmap(bmp);
        //((App)getApplication()).getFetch().addFetchListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //((App)getApplication()).getFetch().removeFetchListener(this);
    }


}
