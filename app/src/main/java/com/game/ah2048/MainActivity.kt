package com.game.ah2048;

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus

class MainActivity : ComponentActivity() {

    private lateinit var adView : AdView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(
            this
        ) {  _ : InitializationStatus? -> }
        val adView = findViewById<AdView>(R.id.adView)
        this.adView = adView

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
}