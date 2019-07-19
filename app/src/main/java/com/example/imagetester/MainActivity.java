package com.example.imagetester;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.io.Serializable;
import java.util.ArrayList;

import static java.lang.Float.valueOf;


public class MainActivity extends AppCompatActivity implements AccessoryListRecyclerViewAdapter.OnItemClickListener, View.OnClickListener

, AdapterView.OnItemSelectedListener {

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Action Bar Logo insert
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.boise4x4logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Year Spinner
        Spinner spinner = findViewById(R.id.spinnerYear);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.year, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);



        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        totalPriceTextView = findViewById(R.id.totalPrice);
        myCartList = new ArrayList<VehicleAccessory>();
        Bitmap smallImage = BitmapFactory.decodeResource(getResources(), R.drawable.jeep_tire);
        Log.i("Database", "W: " + smallImage.getWidth() + " H: " + smallImage.getHeight());


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


    private void buildAccessoryListRecyclerView() {

        myAccessoryListRecyclerView = findViewById(R.id.accessoryListRecyclerView);
        myAccessoryListRecyclerView.setHasFixedSize(true);
        myAccessoryListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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
            public void onDeleteClick(int position) {
                myCartList.remove(position);
                myCartListAdapter.notifyItemRemoved(position);
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

        Button buttonTires = findViewById(R.id.button_tires);
        Button buttonWheels = findViewById(R.id.button_wheels);
        Button buttonLightBars = findViewById(R.id.button_light_bars);
        Button buttonShocks = findViewById(R.id.button_shocks);
        Button buttonLiftKits = findViewById(R.id.button_lift_kits);
        buttonNextActivity = findViewById(R.id.button_send_to_email);

        buttonTires.setOnClickListener(this);
        buttonWheels.setOnClickListener(this);
        buttonLightBars.setOnClickListener(this);
        buttonShocks.setOnClickListener(this);
        buttonLiftKits.setOnClickListener(this);
        buttonNextActivity.setOnClickListener(this);
    }

    public void openSendEmailActivity(View v) {
        Intent intent = new Intent(this, SendEmailActivity.class);
        intent.putExtra("LIST", (Serializable) myCartList);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_send_to_email:
                Toast.makeText(this, "This will send your list to email", Toast.LENGTH_SHORT).show();
                openSendEmailActivity(v);
                break;
            case R.id.button_tires:
                Toast.makeText(this, "Tires List", Toast.LENGTH_SHORT).show();
                //clickedAccessoryType = "tires";
                GetJSONTiresFromAssets getTires = new GetJSONTiresFromAssets(this);
                myAccessoryList = getTires.getJSONTires();
                buildAccessoryListRecyclerView();
                break;
            case R.id.button_wheels:
                Toast.makeText(this, "Wheels List", Toast.LENGTH_SHORT).show();
                //clickedAccessoryType = "wheels";
                GetJSONWheelsFromAssets getWheels = new GetJSONWheelsFromAssets(this);
                myAccessoryList = getWheels.getJSONWheels();
                buildAccessoryListRecyclerView();
                break;
            case R.id.button_light_bars:
                Toast.makeText(this, "Light Bars List", Toast.LENGTH_SHORT).show();
                clickedAccessoryType = "light_bars";

                //GetLightBarsFromAPI getLightBars = new GetLightBarsFromAPI(this);
                //myAccessoryList = getLightBars.getJSONLightBar();

                GetLightBarListFromAPI getLightBars = new GetLightBarListFromAPI(this);
                Thread thread = new Thread(getLightBars);
                thread.start();
                myAccessoryList = GetLightBarListFromAPI.myAccessoryList;

                //GetJSONLightBarFromAssets getLightBars = new GetJSONLightBarFromAssets(this);
                //myAccessoryList = getLightBars.getJSONLightBar();

                //pause(6000);
                buildAccessoryListRecyclerView();
                break;
            case R.id.button_shocks:
                Toast.makeText(this, "Shocks List", Toast.LENGTH_SHORT).show();
                //clickedAccessoryType = "shocks";
                GetJSONShocksFromAssets getShocks = new GetJSONShocksFromAssets(this);
                myAccessoryList = getShocks.getJSONShocks();
                buildAccessoryListRecyclerView();
                break;
            case R.id.button_lift_kits:
                Toast.makeText(this, "Lift Kits List", Toast.LENGTH_SHORT).show();
                //clickedAccessoryType = "lift_kits";
                GetJSONLiftKitsFromAssets getLiftKits = new GetJSONLiftKitsFromAssets(this);
                myAccessoryList = getLiftKits.getJSONLiftKits();
                buildAccessoryListRecyclerView();
                break;
        }
    }

    public static void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}