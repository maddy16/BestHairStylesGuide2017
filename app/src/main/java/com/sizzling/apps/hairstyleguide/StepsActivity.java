package com.sizzling.apps.hairstyleguide;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class StepsActivity extends AppCompatActivity {

    ViewPager slider;
    ArrayList<Integer> data;
    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 110;
    static int option = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        AdRequest request = null;
        if (MainActivity.testingMode)
            request = new AdRequest.Builder().addTestDevice("55757F6B6D6116FAC42122EC92E5A58C").build();
        else
            request = new AdRequest.Builder().build();
        final NativeExpressAdView adView = (NativeExpressAdView) findViewById(R.id.nativeAdView);
        adView.loadAd(request);
        if (MainActivity.mInterstitialAd !=null && MainActivity.mInterstitialAd.isLoaded()) {
            MainActivity.mInterstitialAd.show();
        } else {
            Log.d("MADDY", "Interstitial Not Loaded");
            MainActivity.requestNewInterstitial();
//                    App.instance.countIntAd--;
        }
        slider = (ViewPager) findViewById(R.id.stepsPager);
        int stylenum = getIntent().getIntExtra("stylenum", 0);
        if(stylenum==0){
            Toast.makeText(this, "Unable to Show Style", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        data = MainActivity.dataMap.get(stylenum);
        StepsAdapter adapter = new StepsAdapter(this, data);
        slider.setAdapter(adapter);

    }

    public void swipeLeft(View view) {
        if(slider!=null){
            int currentItem = slider.getCurrentItem();
            if(currentItem>0)
                slider.setCurrentItem(currentItem-1);
        }
    }

    public void swipeRight(View view) {
        if(slider!=null){
            int currentItem = slider.getCurrentItem();
            if(currentItem<(data.size()-1))
                slider.setCurrentItem(currentItem+1);
        }
    }
    public void shareImg(View v) {
        option = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        } else {
            permissionAllowed();
        }
    }

    @TargetApi(23)
    public void checkPermissions() {
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            permissionAllowed();
        }
    }

    public void permissionAllowed() {
        if (option == 1) {
            shareImage();
        } else if (option == 2) {
            downloadImage();
        }

    }

    void downloadImage() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), data.get(slider.getCurrentItem()));
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "hairstyle_" + System.currentTimeMillis() + ".jpg";
        OutputStream out = null;
        File file = new File(path);
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        path = file.getPath();


        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, path);

        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Toast.makeText(this, "HairStyle Saved to Gallery", Toast.LENGTH_LONG).show();
    }

    public void shareImage() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), data.get(slider.getCurrentItem()));
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/HairStyleShared.jpg";
        OutputStream out = null;
        File file = new File(path);
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        path = file.getPath();
        Uri bmpUri = Uri.parse("file://" + path);
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out new Exclusive HairStyles Step by Step. Live at the Playstore.\nhttps://play.google.com/store/apps/details?id=" + getPackageName());
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, "Share with"));
    }

    public void downloadImgClicked(View v) {

        if (MainActivity.mInterstitialAd !=null && MainActivity.mInterstitialAd.isLoaded()) {
            MainActivity.mInterstitialAd.show();
        } else {
            Log.d("MADDY", "Interstitial Not Loaded");
            MainActivity.requestNewInterstitial();
//                    App.instance.countIntAd--;
        }
        option = 2;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        } else {
            permissionAllowed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionAllowed();
                } else {
                    Toast.makeText(this, "Application Requires Permission for sharing / Downloading", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
}
