package com.example.leapfrog.fetchdl;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.tonyodev.fetch.Fetch;
import com.tonyodev.fetch.listener.FetchListener;
import com.tonyodev.fetch.request.Request;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    long downloadId;
    String url = "https://s3-us-west-2.amazonaws.com/com.naturecollections.pics/images/badge/1502364393563.png";
    String dirpath;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image);
        Fetch fetch = Fetch.getInstance(this);
        dirpath = Data.getSaveDir();
        Request request = new Request(url, dirpath, "Testfetch1.jpg");
        downloadId = fetch.enqueue(request);

        if (downloadId != Fetch.ENQUEUE_ERROR_ID) {
            Log.e("main", "call");
            //Download was successfully que
        }

        fetch.addFetchListener(new FetchListener() {

            @Override
            public void onUpdate(long id, int status, int progress, long downloadedBytes, long fileSize, int error) {

                if (status == Fetch.STATUS_DONE) {
                    Bitmap bmp = BitmapFactory.decodeFile(dirpath+File.separator + "Testfetch.jpg");
                    imageView.setImageBitmap(bmp);
                }

                if (downloadId == id && status == Fetch.STATUS_DOWNLOADING) {

                    //  progressBar.setProgress(progress);

                } else if (error != Fetch.NO_ERROR) {
                    //An error occurred

                    if (error == Fetch.ERROR_HTTP_NOT_FOUND) {
                        //handle error
                    }

                }
            }
        });


    }
    @Override
    public void onResume() {

        super.onResume();
        Bitmap bmp = BitmapFactory.decodeFile(dirpath + "Testfetch.jpg");
        if(bmp!=null)
        imageView.setImageBitmap(bmp);
    }
}