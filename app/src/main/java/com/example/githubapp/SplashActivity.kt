package com.example.githubapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, RepositoryListActivity::class.java))
            finish()
        }, 3500)
    }
}