package com.example.imagetester;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    //Comment from Wendy new
    //Comment from Josh
    //Comment from Mark

    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bitmap bigImage = BitmapFactory.decodeResource(getResources(), R.drawable.jeep_body);
        Log.i("Information: ","Width is " + Integer.toString(bigImage.getWidth()));
        Bitmap smallImage = BitmapFactory.decodeResource(getResources(), R.drawable.jeep_tire);
        Bitmap mergedImages = mergeImages(bigImage, smallImage, 485, 700);
        mergedImages = mergeImages(mergedImages, smallImage, 1650, 700);

        img = findViewById(R.id.imageView5);
        img.setImageBitmap(mergedImages);

    }

    // Merge conflicts galore and more.

    private Bitmap mergeImages(Bitmap firstImage, Bitmap secondImage, int x, int y){

        Bitmap result = Bitmap.createBitmap(firstImage.getWidth(), firstImage.getHeight(), firstImage.getConfig());
        Canvas canvas = new Canvas(result);
        int newX = x * firstImage.getWidth() / 2400;
        int newY = y * firstImage.getHeight() / 1260;
        Log.i("Information", "Canvas width is " + canvas.getWidth());
        Log.i("Information", "Canvas height is " + canvas.getHeight());
        canvas.drawBitmap(firstImage, 0f, 0f, null);
        canvas.drawBitmap(secondImage, newX, newY, null);
        return result;
    }

    public void changeTire(View view) {
        Bitmap bigImage = BitmapFactory.decodeResource(getResources(), R.drawable.jeep_body);
        Bitmap smallImage = BitmapFactory.decodeResource(getResources(), R.drawable.jeep_tire_2);
        Bitmap mergedImages = mergeImages(bigImage, smallImage, 485, 700);
        mergedImages = mergeImages(mergedImages, smallImage, 1650, 700);

        ImageView img = findViewById(R.id.imageView5);


        img.setImageBitmap(mergedImages);


        Log.i("Information: ", "Imageview width is " + img.getWidth());
    }

    public void changePaint(View view){
        /*BitmapDrawable bd = (BitmapDrawable) img.getDrawable();
        Bitmap bm = bd.getBitmap();

        Paint paint = new Paint();
        ColorFilter filter = new PorterDuffColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);

        paint.setColorFilter(filter);

        Canvas canvas = new Canvas(bm);
        canvas.drawBitmap(bm,0,0,paint);
        img.setImageBitmap(bm);
        */

//        LightingColorFilter filter = new LightingColorFilter(Color.WHITE, Color.BLUE);
        BitmapDrawable bd = (BitmapDrawable) img.getDrawable();
        Bitmap image = bd.getBitmap();


        /****************************************************************************
         * This code is for changing the color, however it is currently too unstable
         * to be used reliably and will need to be run on a separate thread if used
         * in the final project.
         * **************************************************************************
         *
        for (int x = 0; x < image.getWidth(); x++){
            for (int y = 0; y < image.getHeight(); y++){
                int pixel = image.getPixel(x,y);
                int red = Color.red(pixel);
                int blue = Color.blue(pixel);
                int green = Color.green(pixel);
                int color2 = Color.RED;


                if (Math.abs (red - Color.red (color2)) > 10 && Math.abs (green - Color.green (color2)) <= 120 && Math.abs (blue - Color.blue (color2)) <= 120 && red > 100 ){
                    int newPixel = pixel;

                    //image.setPixel(x,y, Color.rgb(0, 0, Color.blue(Color.BLUE)));
                }
            }
        }

        //img.setImageBitmap(image);
         */
    }
}
