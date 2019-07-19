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

    //public static LightBars myLightBars;
    //public static LightBarItem myLightBarItem;
    public static ArrayList<VehicleAccessory> myAccessoryList;

    //private WeakReference<Activity> activityWeakReference;

    private MainActivity activity;


    public GetLightBarListFromAPI(MainActivity activity) {
        this.activity = activity;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        String json = null;

        String accessoryType = "light_bars";

        try {
            URL myURL = new URL("https://openrpg.org/api/" + accessoryType + "/read.php");
            URLConnection myURLConnection = myURL.openConnection();
            myURLConnection.connect();

            InputStream response = myURLConnection.getInputStream();

            try (Scanner scanner = new Scanner(response)) {
                json = scanner.useDelimiter("\\A").next();
            }
        } catch (MalformedURLException e) {

        } catch (IOException e) {

            e.printStackTrace();
        }

        Gson gson = new Gson();

        LightBars myList = gson.fromJson(json, LightBars.class);

        //myLightBars = new LightBars();

        //myLightBars = gson.fromJson(responseBody, LightBars.class);

        //myLightBars = gson.fromJson(json, LightBars.class);

        //myLightBarItem = gson.fromJson(json, LightBarItem.class);

        //final Activity activity = activityWeakReference.get();

        System.out.println(json);

        //System.out.println(myLightBarItem.getSpecs());

        //System.out.println(myLightBars.get(0).amp_draw);

        //System.out.println(myLightBars.getPartName());

        myAccessoryList = new ArrayList<>();

        if (myList.light_bars != null) {
            for (int i = 0; i < myList.light_bars.size(); i++) {
                myAccessoryList.add(new VehicleAccessory(AccessoryType.LIGHTBAR,
                        myList.light_bars.get(i).partNumber,
                        myList.light_bars.get(i).name,
                        myList.light_bars.get(i).description,
                        myList.light_bars.get(i).brand,
                        myList.light_bars.get(i).manufacturerPartNumber,
                        myList.light_bars.get(i).price,
                        myList.light_bars.get(i).vehicleType,
                        myList.light_bars.get(i).getSpecs(),
                        myList.light_bars.get(i).image_url));
            }

            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "This should get lightbars", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
