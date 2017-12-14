package com.example.leapfrog.fetchdl;

import android.net.Uri;
import android.os.Environment;

import com.tonyodev.fetch.Fetch;
import com.tonyodev.fetch.request.Request;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonyofrancis on 1/24/17.
 */

public final class Data {

    public static final String[] sampleUrls = new String[] {
            "http://www.news1.co.il/uploadFiles/9760f342e7.jpg",
            "https://http2.mlstatic.com/impresora-de-recibos-termicos-58mm-imagestore-brainydeal-eu-D_NQ_NP_358221-MCO20742849696_052016-F.jpg",
            "http://storage.googleapis.com/ix_choosemuse/uploads/2016/02/android-logo.png"};

    private Data() {
    }

    static List<Request> getFetchRequests() {

        List<Request> requests = new ArrayList<>();

        for (String sampleUrl : sampleUrls) {

            Request request = new Request(sampleUrl,getFilePath(sampleUrl));
            requests.add(request);
        }

        return requests;
    }

    public static String getFilePath(String url) {

        Uri uri = Uri.parse(url);

        String fileName = uri.getLastPathSegment();

        String dir = getSaveDir();

        return (dir + "/DownloadList/" + System.nanoTime() +"_"+ fileName);
    }



    public static String getSaveDir() {

        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + File.separator+ "fetch";
    }
}
