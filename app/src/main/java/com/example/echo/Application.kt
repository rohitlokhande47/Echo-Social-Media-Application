package com.example.echo

import android.app.Application
import com.cloudinary.android.MediaManager
import com.google.firebase.FirebaseApp

class EchoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Initialize Cloudinary
        val config: HashMap<String, String> = HashMap()
        config["cloud_name"] = "dgga480kz"
        config["api_key"] = "846831235626871"
        config["api_secret"] = "6613KGsOb8_wkie1QFBd6u6Rq48"
        MediaManager.init(this, config)
    }
}
