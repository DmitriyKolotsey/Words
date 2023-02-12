package com.kolotseyd.words;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {
    private static final long SPLASH_DURATION = 2000L;

    private Handler mHandler;
    private Runnable mRunnable;

    private String interstitialAdUnitId = "ca-app-pub-8542657706295420/4008511045";
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MobileAds.initialize(this, initializationStatus -> {
        });

        loadInterstitialAd();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_splash_screen);

        TextView textView = findViewById(R.id.splash_screen_tv);
        textView.setText(R.string.splash_screen_string);

        mHandler = new Handler();
        mRunnable = this::dismissSplash;

        View rootView = findViewById(android.R.id.content);
        rootView.setOnClickListener(v -> dismissSplash());
    }

    private void loadInterstitialAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, interstitialAdUnitId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                SplashScreenActivity.this.interstitialAd = interstitialAd;
                interstitialAd.setFullScreenContentCallback(
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                SplashScreenActivity.this.interstitialAd = null;
                                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                                Log.d("TAG", "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                SplashScreenActivity.this.interstitialAd = null;
                                Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                Log.d("TAG", "The interstitial ad was shown.");
                            }
                        }
                );
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.i("TAG", loadAdError.getMessage());
                interstitialAd = null;
            }
        });
    }

    private void showInterstitial() {
        if (interstitialAd != null) {
            interstitialAd.show(SplashScreenActivity.this);
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, SPLASH_DURATION);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    private void dismissSplash() {
        Log.d("TAG", "splashEnd");
        showInterstitial();
        //startActivity(new Intent(this, MainActivity.class));
        //finish();
    }

}