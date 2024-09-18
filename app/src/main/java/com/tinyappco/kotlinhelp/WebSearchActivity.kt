package com.tinyappco.kotlinhelp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tinyappco.kotlinhelp.databinding.ActivityWebSearchBinding

class WebSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityWebSearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        var url = intent.getStringExtra("url")
//        if (savedInstanceState != null) {
//            url = savedInstanceState.getString("url")
//        }

        val url = savedInstanceState?.getString("url") ?: intent.getStringExtra("url")

        binding.webView.webViewClient = WebViewClient() //prevents opening in browser app
        binding.webView.settings.javaScriptEnabled = true // required for search functionality on Kotlin and Android sites
        binding.webView.loadUrl(url!!)

        onBackPressedDispatcher.addCallback(this, backPressedCallback)

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState?.putString("url",binding.webView.url)
    }


    private val backPressedCallback = object: OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent()
            intent.putExtra("url",binding.webView.url)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

}