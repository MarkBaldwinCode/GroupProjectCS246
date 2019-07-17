package com.example.imagetester;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class GetLightBarListFromAPI implements Runnable {

    public static ListOfLightBars myListOfLightBars;
    public static LightBars myLightBars;

    private WeakReference<Activity> activityWeakReference;

    public GetLightBarListFromAPI(Activity activity) {
        this.activityWeakReference = new WeakReference<Activity>(activity);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        String responseBody = null;

        String accessoryType = "light_bars";

        try {
            URL myURL = new URL("https://openrpg.org/api/" + accessoryType +"/read.php");
            URLConnection myURLConnection = myURL.openConnection();
            myURLConnection.connect();

            InputStream response = myURLConnection.getInputStream();

            try (Scanner scanner = new Scanner(response)) {
                responseBody = scanner.useDelimiter("\\A").next();
            }
        }
        catch (MalformedURLException e) {

        }
        catch (IOException e) {

            e.printStackTrace();
        }

        Gson gson = new Gson();

        //myListOfLightBars = new ListOfLightBars();

        //myListOfLightBars = gson.fromJson(responseBody, ListOfLightBars.class);

        myLightBars = gson.fromJson(responseBody, LightBars.class);

        final Activity activity = activityWeakReference.get();

        System.out.println(responseBody);

        //System.out.println(myListOfLightBars.get(0).amp_draw);

        //System.out.println(myLightBars.getPartName());

        if(activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "This should get lightbars", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}