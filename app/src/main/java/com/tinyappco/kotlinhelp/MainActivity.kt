package com.tinyappco.kotlinhelp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tinyappco.kotlinhelp.databinding.ActivityMainBinding
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    private var prevUrl: String? = null
    private val encodedSearchTerm : String
        get() = URLEncoder.encode(binding.etSearch.text.toString(),"UTF-8")

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        //todo: handle activity result
        if (result.resultCode == Activity.RESULT_OK) {
            val url = result.data?.getStringExtra("url")
            if (url != null) {
                prevUrl = url
                binding.btnPrevious.visibility = View.VISIBLE
            } else {
                binding.btnPrevious.visibility = View.INVISIBLE
            }
        }
    }

    private fun loadWebActivity(url: String) {
        val intent = Intent(this, WebSearchActivity::class.java)
        intent.putExtra("url", url)
        resultLauncher.launch(intent)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnGoogle.setOnClickListener {
            loadWebActivity("https://google.co.uk/search?q=$encodedSearchTerm")
        }
        binding.btnStackOverflow.setOnClickListener {
            loadWebActivity("https://stackoverflow.com/search?q=$encodedSearchTerm")
        }
        binding.btnKotlin.setOnClickListener{
            loadWebActivity("https://kotlinlang.org/?q=$encodedSearchTerm&p=0")
        }
        binding.btnAndroid.setOnClickListener{
            loadWebActivity("https://developer.android.com/s/results/?q=$encodedSearchTerm")
        }

        binding.btnPrevious.setOnClickListener {
            loadWebActivity(prevUrl?: "")
        }


        binding.etSearch.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                toggleButtonsState(binding.etSearch.text.length > 1)
            }

        })

        toggleButtonsState(false)

    }

    private fun toggleButtonsState(enabled: Boolean){
        binding.btnAndroid.isEnabled = enabled
        binding.btnKotlin.isEnabled = enabled
        binding.btnStackOverflow.isEnabled = enabled
        binding.btnGoogle.isEnabled = enabled
    }
}