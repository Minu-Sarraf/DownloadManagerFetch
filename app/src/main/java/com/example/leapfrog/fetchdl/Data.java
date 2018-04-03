package com.example.leapfrog.fetchdl;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.tonyodev.fetch.Fetch;
import com.tonyodev.fetch.request.Request;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonyofrancis on 1/24/17.
 */

public final class Data {

    public static final String[] sampleUrls = new String[]{
            //"http://dev.naturecollections.ischool.uw.edu/public/images/classification/1514993930048bbf08.jpg",
            // "http://dev.naturecollections.ischool.uw.edu/public/images/badge/1501827267616.png",
            // "http://dev.naturecollections.ischool.uw.edu/public/images/character/moose_introduction.png",
            "http://dev.naturecollections.ischool.uw.edu/public/images/kahsdf/asdf.png",
            // "http://dev.naturecollections.ischool.uw.edu/images/badge/1501826648697.png",
            //"http://dev.naturecollections.ischool.uw.edu/images/badge/1501826648711.png",
            //"http://dev.naturecollections.ischool.uw.edu/public/images/badge/1501827267635.png",
            //"http://dev.naturecollections.ischool.uw.edu/images/badge/1501826648730.png",};
    };

    private Data() {
    }

    static List<Request> getFetchRequests() {

        List<Request> requests = new ArrayList<>();

        for (String sampleUrl : sampleUrls) {

            Request request = new Request(sampleUrl, getFilePath(sampleUrl));
            requests.add(request);
        }
       // Log.e("request", String.valueOf(requests));
        return requests;
    }

    public static String getFilePath(String url) {

        Uri uri = Uri.parse(url);

        String fileName = uri.getLastPathSegment();

        String dir = getSaveDir();

        return (dir + File.separator+"DownloadList"+File.separator + System.nanoTime() + "_" + fileName);
    }



    public static String getSaveDir() {

        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + File.separator + "fetch";
    }
}
