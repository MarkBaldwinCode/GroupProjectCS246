package com.example.imagetester;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

import static java.lang.Float.valueOf;


public class MainActivity extends AppCompatActivity implements AccessoryListRecyclerViewAdapter.OnItemClickListener, View.OnClickListener {

    //branch test

    ImageView img;

    private RecyclerView myRecyclerView;
    private CartListRecyclerViewAdapter myCartListAdapter;
    private RecyclerView.LayoutManager myLayoutManager;

    private RecyclerView myAccessoryListRecyclerView;
    private AccessoryListRecyclerViewAdapter myAccessoryListRecyclerViewAdapter;
    private RequestQueue myRequestQueue;

    private ArrayList<VehicleAccessory> myCartList;
    private ArrayList<VehicleAccessory> myAccessoryList;

    private TextView totalPriceTextView;
    private Button buttonNextActivity;
    private String clickedAccessoryType;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        totalPriceTextView = findViewById(R.id.totalPrice);
        myCartList = new ArrayList<VehicleAccessory>();
        Bitmap smallImage = BitmapFactory.decodeResource(getResources(), R.drawable.jeep_tire);
        Log.i("Database", "W: " + smallImage.getWidth() + " H: " + smallImage.getHeight());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createCartList();
        //buildAccessoryListRecyclerView();
        buildRecyclerView();
        calculateTotalPrice();
        setButtons();
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth  = outMetrics.widthPixels / density;

        img = findViewById(R.id.imageView5);

        img.getLayoutParams().height = (int)(dpWidth / 4);
        img.getLayoutParams().width = (int)(dpWidth / 2);

        if (img.getLayoutParams().height > 150 || img.getLayoutParams().width > 300){
            img.getLayoutParams().height = 150;
            img.getLayoutParams().width = 300;
        }
        img.requestLayout();
        /*
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String cartJson = sharedPref.getString("CART", "");
        String currentVehicleJson = sharedPref.getString("CURRENT_TAB", "");
        String currentTabString = sharedPref.getString("CURRENT_VEHICLE", "");

        Gson gson = new Gson();
        myCartObject cart = gson.fromJson(cartJson, myCartObject.class);
        myVehicleObject currentVehicle = gson.fromJson(currentVehicleJson, myVehicleObject.class);
        */

    }

    @Override
    public void onAddToCartIconClick(int position) {

        VehicleAccessory clickedItem = myAccessoryList.get(position);

        int cartPosition = myCartList.size();

        myCartList.add(cartPosition, clickedItem);
        calculateTotalPrice();
        myCartListAdapter.notifyItemInserted(cartPosition);
    }


    public void createCartList() {

        myCartList = new ArrayList<>();
    }


    private void buildAccessoryListRecyclerView() {

        myAccessoryListRecyclerView = findViewById(R.id.accessoryListRecyclerView);
        myAccessoryListRecyclerView.setHasFixedSize(true);
        myAccessoryListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

       switch(clickedAccessoryType) {
           case "tires":
               break;
           case "light_bars":
               GetJSONLightBarFromAssets getLightBars = new GetJSONLightBarFromAssets(this);
               myAccessoryList = getLightBars.getJSONLightBar();
               break;
           case "wheels":
               break;
           case "shocks":
               break;

       }

        myAccessoryListRecyclerViewAdapter = new AccessoryListRecyclerViewAdapter(this, myAccessoryList);
        myAccessoryListRecyclerView.setAdapter(myAccessoryListRecyclerViewAdapter);
        myAccessoryListRecyclerViewAdapter.setOnItemClickListener(this);

    }


    public void buildRecyclerView() {

        myRecyclerView = findViewById(R.id.cartRecyclerView);
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(this);
        myCartListAdapter = new CartListRecyclerViewAdapter(myCartList);

        myRecyclerView.setLayoutManager(myLayoutManager);
        myRecyclerView.setAdapter(myCartListAdapter);

        myCartListAdapter.setOnItemClickListener(new CartListRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
                calculateTotalPrice();
            }
        });
    }

    public void calculateTotalPrice() {
        Float total = 0f;

        for (int i = 0; i < myCartList.size(); i++) {
            total += valueOf(myCartList.get(i).getPartPrice());
        }

        totalPriceTextView = findViewById(R.id.totalPrice);
        totalPriceTextView.setText("Total: $ " + String.format("%.2f", total));
    }

    public void setButtons() {

        buttonNextActivity = findViewById(R.id.button2);
        Button buttonTires = findViewById(R.id.button4);
        Button buttonWheels = findViewById(R.id.button6);
        Button buttonLightBars = findViewById(R.id.button7);

        buttonTires.setOnClickListener(this);
        buttonWheels.setOnClickListener(this);
        buttonLightBars.setOnClickListener(this);
        buttonNextActivity.setOnClickListener(this);
    }

    public void removeItem(int position) {
        myCartList.remove(position);
        myCartListAdapter.notifyItemRemoved(position);
    }

    public void openSendEmailActivity(View v) {
        Intent intent = new Intent(this, SendEmailActivity.class);
        intent.putExtra("LIST", (Serializable) myCartList);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button2:
                Toast.makeText(this, "This will send your list to email", Toast.LENGTH_SHORT).show();
                openSendEmailActivity(v);
                break;
            case R.id.button4:
                Toast.makeText(this, "Tires List", Toast.LENGTH_SHORT).show();
                clickedAccessoryType = "tires";
                buildAccessoryListRecyclerView();
                break;
            case R.id.button6:
                Toast.makeText(this, "Wheels List", Toast.LENGTH_SHORT).show();
                clickedAccessoryType = "wheels";
                buildAccessoryListRecyclerView();
                break;
            case R.id.button7:
                Toast.makeText(this, "Light Bars List", Toast.LENGTH_SHORT).show();
                clickedAccessoryType = "light_bars";
                buildAccessoryListRecyclerView();
                break;
        }
    }
}