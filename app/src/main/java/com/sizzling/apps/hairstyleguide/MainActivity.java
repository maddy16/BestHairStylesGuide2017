package com.sizzling.apps.hairstyleguide;

import android.*;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusOneButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    static HashMap<Integer, ArrayList<Integer>> dataMap;
    static InterstitialAd mInterstitialAd;
    static ArrayList<String> styleNames;
    static ArrayList<Integer> allStyles;
    static boolean testingMode = true;
    RecyclerView recyclerView;
    private PlusOneButton mPlusOneButton;
    private GoogleApiClient mGoogleApiClient;
    private static String APP_URL;
    private PlusOneButton.OnPlusOneClickListener mPlusOneClickListener;
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AdView mAdView = (AdView) findViewById(R.id.bannerAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        prepareData();
        loadAds(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.mainRecyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
//        APP_URL = "https://play.google.com/store/apps/details?id=" + getPackageName();
        APP_URL = "https://play.google.com/store/apps/details?id=com.logixity.apps.ringtonesmanager";
        mPlusOneClickListener = new PlusOneButton.OnPlusOneClickListener() {
            @Override
            public void onPlusOneClick(Intent intent) {
                if (intent != null)
                    startActivityForResult(intent, PLUS_ONE_REQUEST_CODE);
            }
        };
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .useDefaultAccount()
                .build();
        AllStylesAdapter adapter = new AllStylesAdapter(getApplicationContext(), allStyles);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // In   flate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rate) {
            rateApp();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_plus);
        RelativeLayout view = (RelativeLayout) item.getActionView();
        mPlusOneButton = (PlusOneButton) view.findViewById(R.id.plus_one_button);
        mPlusOneButton.initialize(APP_URL, mPlusOneClickListener);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        if (mPlusOneButton != null)
            mPlusOneButton.initialize(APP_URL, mPlusOneClickListener);
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    void rateApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }


    }
    void prepareNames(){
        styleNames = new ArrayList<>();
        styleNames.add("sexy swept side braid");
        styleNames.add("milkmaid braids");
        styleNames.add("twisted fishtail");
        styleNames.add("twisted hairband");
        styleNames.add("frend braid tie back");
        styleNames.add("half up hair bun");
        styleNames.add("half up twisted bun");
        styleNames.add("vintage half up");
        styleNames.add("60s ponytail");
        styleNames.add("top bun");
        styleNames.add("bouffant");
        styleNames.add("bun and hairbow");
        styleNames.add("fishtail braid");
        styleNames.add("glam up");
        styleNames.add("bow tiful style");
        styleNames.add("sexy side chingon");
        styleNames.add("sultry mermaid waves");
        styleNames.add("perfect party hair");
        styleNames.add("sleeky sexy ponytail");
        styleNames.add("easy everyday curls");
        styleNames.add("braided hair");
        styleNames.add("half up half down");
        styleNames.add("half up twisted tail");
        styleNames.add("twist back");
        styleNames.add("tie up braids");
        styleNames.add("half bun half braid");
        styleNames.add("twisted crown");
        styleNames.add("three headband");
        styleNames.add("fishtail braided headband");
        styleNames.add("french braid bun");
        styleNames.add("feminine french twist");
        styleNames.add("bow braid hairstyle");
        styleNames.add("four strand braid");
        styleNames.add("braided ponytail");
        styleNames.add("headband updo");
        styleNames.add("dutch side braid");
        styleNames.add("longer fuller ponytail");
        styleNames.add("valentine's messy braid");
        styleNames.add("vintage");
        styleNames.add("halfup hair knot");

    }
    void prepareData() {
        prepareNames();
        dataMap = new HashMap<>();
        allStyles = new ArrayList<>();
        HashMap<Integer, ArrayList<Integer>> nums = new HashMap<>();
        int maxStyleNum = 0;
        Field[] fields = R.drawable.class.getFields();
        for (Field field : fields) {
            String name = field.getName();
            if (name.startsWith("hs")) {
                String[] cats = name.split("_");
                String style = cats[1];

                int styleNum = Integer.parseInt(style);
                if (maxStyleNum < styleNum)
                    maxStyleNum = styleNum;
                ArrayList<Integer> stepsList = dataMap.get(styleNum);
                ArrayList<Integer> numsList = nums.get(styleNum);
                if (stepsList == null) {
                    stepsList = new ArrayList<>();
                    numsList = new ArrayList<>();
                    dataMap.put(styleNum, stepsList);
                    nums.put(styleNum, numsList);
                }
                String step = cats[2];
                int stepNum = Integer.parseInt(step);
                stepsList.add(getResources().getIdentifier(field.getName(), "drawable", getPackageName()));
                numsList.add(stepNum);
            }
        }
        for(int i = 1;i<=maxStyleNum;i++){
            ArrayList<Integer> stepsList = dataMap.get(i);
            ArrayList<Integer> numsList = nums.get(i);
            Integer [] stepsArray = new Integer[stepsList.size()];
            Integer [] numsArray = new Integer[numsList.size()];
            stepsArray = stepsList.toArray(stepsArray);
            numsArray = numsList.toArray(numsArray);
            for(int j=0;j<stepsList.size();j++){
                for(int k=j+1;k<stepsList.size();k++){

                    if(numsArray[j]>numsArray[k]){
                        int temp = numsArray[j];
                        numsArray[j] = numsArray[k];
                        numsArray[k] = temp;
                        temp = stepsArray[j];
                        stepsArray[j] = stepsArray[k];
                        stepsArray[k] = temp;
                    }

                }
            }
            stepsList = new ArrayList<>(Arrays.asList(stepsArray));
            dataMap.put(i,stepsList);
            allStyles.add(stepsList.get(stepsList.size()-1));
        }

    }
    static void loadAds(Context context){
        mInterstitialAd = new InterstitialAd(context);
        if(testingMode){
            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712"); // Test Ad Id
        } else{
            mInterstitialAd.setAdUnitId(context.getString(R.string.interstitial_ad_id));
        }



        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
//                requestNewInterstitial();
            }
        });
        requestNewInterstitial();
    }
    static public void requestNewInterstitial() {
        AdRequest adRequest = null;
        if(testingMode){
            adRequest= new AdRequest.Builder()
                    .addTestDevice("55757F6B6D6116FAC42122EC92E5A58C")
                    .build();
        } else {
            adRequest = new AdRequest.Builder()
                    .build();
        }
        mInterstitialAd.loadAd(adRequest);
    }

}
