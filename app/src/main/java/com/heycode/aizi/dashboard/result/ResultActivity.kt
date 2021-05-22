package com.heycode.aizi.dashboard.result

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.heycode.aizi.R

class ResultActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var fab: FloatingActionButton
    private val WEB_URL =
        "https://share.streamlit.io/heykaali/bioactivity-prediction-app/main/app.py"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        //Action Bar
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.purple))
        supportActionBar?.title = "Result Analysis"

        webView = findViewById(R.id.webView)
        fab = findViewById(R.id.fab)

        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(WEB_URL)
            settings.javaScriptEnabled = true
        }
        fab.setOnClickListener {
            webView.reload()
            Toast.makeText(this, "Reloading...", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            webView.clearCache(true)
            finish()
        }
    }
}