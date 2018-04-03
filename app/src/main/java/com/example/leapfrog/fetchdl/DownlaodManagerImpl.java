package com.example.leapfrog.fetchdl;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.tonyodev.fetch.Fetch;
import com.tonyodev.fetch.listener.FetchListener;
import com.tonyodev.fetch.request.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leapfrog on 12/14/17.
 */

public class DownlaodManagerImpl implements FetchListener {
    private static final int STORAGE_PERMISSION_CODE = 200;
    ArrayList<Download> arrayList = new ArrayList<Download>();
    ActionListener actionListener;
    Download download = new Download();
    private Fetch fetch;
    public Context context;

    public DownlaodManagerImpl(Context context) {
        this.context = context;
       // fetch = Fetch.newInstance(context);

    }

    @Override
    public void onUpdate(long id, int status, int progress, long downloadedBytes, long fileSize, int error) {
        Download download = getDownload(id);
        Log.e("error",error+" progress" +progress+" status"+status+""+id);
       // fetch = Fetch.newInstance(context);
        if(download != null) {

            int position = getPosition(id);

            if(status != Fetch.STATUS_REMOVED) {
               // fetch.enqueue(id);
            }else {
               // arrayList.remove(position);

            }
        }
        if(status == Fetch.STATUS_DONE){
            Log.e("Success",error+"progress" +progress+" status"+status);
        }else if(status == Fetch.STATUS_ERROR){
          //  fetch.remove(id);
            Log.e("Success",error+"progress" +progress+" status"+status);

            fetch.retry(id);

        }
           //Bitmap bmp = BitmapFactory.decodeFile(String.valueOf(Uri.parse(arrayList.get(0).getFilePath())));
           // Log.e("file",arrayList.get(0).getFilePath());
          // actionListener.onDownloadComplete(id,bmp);

    }

    public Download getDownload(long id) {

        for (Download download : arrayList) {

            if(download.getId() == id) {
                return download;
            }
        }
        fetch = null;
        return null;
    }

    public int getPosition(long id) {

        for (int i = 0; i < arrayList.size(); i++) {

            Download download = arrayList.get(i);

            if(download.getId() == id) {
                return i;
            }
        }
        return  -1;
    }

    public void clearAllDownloads() {

         fetch = Fetch.newInstance(context);
        fetch.removeAll();

        createNewRequests();
       // fetch.release();
    }

    private void createNewRequests() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ((Activity) context).requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        } else {
            enqueueDownloads();
        }
    }

    public void enqueueDownloads() {

        List<Request> requests = Data.getFetchRequests();
       // Log.e("enque", String.valueOf(requests));
        List<Long> ids = fetch.enqueue(requests);

        for (int i = 0; i < requests.size(); i++) {

            Request request = requests.get(i);
            long id = ids.get(i);

            download = new Download();
            download.setId(id);
            download.setUrl(request.getUrl());
            download.setFilePath(request.getFilePath());
            download.setError(Fetch.DEFAULT_EMPTY_VALUE);
            download.setProgress(0);
            download.setStatus(Fetch.STATUS_QUEUED);

            arrayList.add(download);
        }
    }


}
//add