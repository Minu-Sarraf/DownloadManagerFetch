package com.example.leapfrog.fetchdl;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.tonyodev.fetch.Fetch;
import com.tonyodev.fetch.listener.FetchListener;
import com.tonyodev.fetch.request.Request;
import com.tonyodev.fetch.request.RequestInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadActivity extends AppCompatActivity implements FetchListener,ActionListener {

    private static final int STORAGE_PERMISSION_CODE = 200;
    ArrayList<Download> arrayList = new ArrayList<Download>();

    private Fetch fetch;
    private ImageView imageView;
    private String dirpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image);
        new Fetch.Settings(this)
                .setAllowedNetwork(Fetch.NETWORK_ALL)
                .enableLogging(true)
                .setConcurrentDownloadsLimit(1)
                .apply();


        fetch = Fetch.getInstance(this);
        clearAllDownloads();
    }

    /*Removes all downloads managed by Fetch*/
    private void clearAllDownloads() {

        Fetch fetch = Fetch.getInstance(this);
        fetch.removeAll();

        createNewRequests();
        fetch.release();
    }


    @Override
    protected void onResume() {
        super.onResume();

        List<RequestInfo> infos = fetch.get();

        for (RequestInfo info : infos) {

            onUpdate(info.getId(), info.getStatus()
                    , info.getProgress(), info.getDownloadedBytes(), info.getFileSize(), info.getError());
        }

        fetch.addFetchListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        fetch.removeFetchListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fetch.release();
    }

    private void createNewRequests() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        } else {
            enqueueDownloads();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE || grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            enqueueDownloads();

        } else {
            // Toast.makeText(this,R.string.permission_not_enabled, Toast.LENGTH_SHORT).show();
        }
    }

    private void enqueueDownloads() {

        List<Request> requests = Data.getFetchRequests();
        List<Long> ids = fetch.enqueue(requests);

        for (int i = 0; i < requests.size(); i++) {

            Request request = requests.get(i);
            long id = ids.get(i);

            Download download = new Download();
            download.setId(id);
            download.setUrl(request.getUrl());
            download.setFilePath(request.getFilePath());
            download.setError(Fetch.DEFAULT_EMPTY_VALUE);
            download.setProgress(0);
            download.setStatus(Fetch.STATUS_QUEUED);

            arrayList.add(download);
        }
    }



    @Override
    public void onUpdate(long id, int status, int progress, long downloadedBytes, long fileSize, int error) {
        if (status == Fetch.STATUS_DONE) {
            Bitmap bmp = BitmapFactory.decodeFile(arrayList.get(0).getFilePath());
            imageView.setImageBitmap(bmp);
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
}