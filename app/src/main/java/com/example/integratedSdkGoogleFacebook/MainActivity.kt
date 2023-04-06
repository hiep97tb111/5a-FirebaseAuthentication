package com.example.integratedSdkGoogleFacebook

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tvOpenLoginGoogle).setOnClickListener {
            startActivity(Intent(this, LoginGoogleAct::class.java))
        }

        findViewById<TextView>(R.id.tvOpenLoginFacebook).setOnClickListener {
            startActivity(Intent(this, LoginFacebookAct::class.java))
        }

    }
}