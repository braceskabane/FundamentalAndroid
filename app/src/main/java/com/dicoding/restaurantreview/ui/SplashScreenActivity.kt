package com.dicoding.restaurantreview.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.restaurantreview.R

class SplashScreenActivity : AppCompatActivity() {

    private companion object {
        const val SPLASH_TIMEOUT: Long = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Log.d("SplashScreenActivity", "Splash screen started")

        Handler(Looper.getMainLooper()).postDelayed({
            Log.d("SplashScreenActivity", "Moving to MainActivity")

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIMEOUT)
    }
}