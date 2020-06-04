package fr.isen.berton.projectapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import fr.isen.berton.projectapp.R
import fr.isen.berton.projectapp.SignUpActivity

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val intent = Intent(this, SignUpActivity::class.java)
        Handler().postDelayed({
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)
    }
}

