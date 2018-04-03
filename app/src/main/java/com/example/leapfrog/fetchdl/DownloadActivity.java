package com.example.leapfrog.fetchdl;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.tonyodev.fetch.Fetch;
import com.tonyodev.fetch.request.RequestInfo;

import java.util.ArrayList;
import java.util.List;

public class DownloadActivity extends AppCompatActivity implements ActionListener {

    private static final int STORAGE_PERMISSION_CODE = 200;
    ArrayList<Download> arrayList = new ArrayList<Download>();
    DownlaodManagerImpl downlaodManagerImpl;
    private Fetch fetch;
    private ImageView imageView;
    private String dirpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image);
        downlaodManagerImpl = new DownlaodManagerImpl(this);
        new Fetch.Settings(this)
                .setAllowedNetwork(Fetch.NETWORK_ALL)
                .enableLogging(true)
                .setConcurrentDownloadsLimit(1)
                .apply();
         fetch = Fetch.newInstance(this);
        //downlaodManagerImpl.clearAllDownloads();
    }


    /*Removes all downloads managed by Fetch*/


    @Override
    protected void onResume() {
        super.onResume();

        List<RequestInfo> infos = fetch.get();
        fetch.addFetchListener(new DownlaodManagerImpl(this));
        for (RequestInfo info : infos) {
            downlaodManagerImpl.onUpdate(info.getId(), info.getStatus()
                    , info.getProgress(), info.getDownloadedBytes(), info.getFileSize(), info.getError());
        }

        fetch.addFetchListener(new DownlaodManagerImpl(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        fetch.removeFetchListener(downlaodManagerImpl);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fetch.release();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE || grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            downlaodManagerImpl.enqueueDownloads();

        } else {
            // Toast.makeText(this,R.string.permission_not_enabled, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPauseDownload(long id) {

    }

    @Override
    public void onResumeDownload(long id) {

    }

    @Override
    public void onRemoveDownload(long id) {

    }

    @Override
    public void onRetryDownload(long id) {

    }

    @Override
    public void onDownloadComplete(long id, Bitmap bmp) {
        Log.e("complete", "kkkk");
        imageView.setImageBitmap(bmp);
    }
}


