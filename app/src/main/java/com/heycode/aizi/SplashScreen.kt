package com.heycode.aizi

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    private lateinit var mProgress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        //creating animation object an animating the image
        val animation = AnimationUtils.loadAnimation(this, R.anim.scale)
        findViewById<ImageView>(R.id.splash_image).startAnimation(animation)

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
            finish()
        }, 4000) // 3000 is the delayed time in milliseconds.

    }
}