package fr.isen.berton.projectapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home_page.*

class HomePageActivity : AppCompatActivity() {
    var currentProgress = 0
    var etage = 50

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        increaseProgress()
        RankPicture.setOnClickListener {}
        AwarenessPicture.setOnClickListener {}
        GamePicture.setOnClickListener { }
    }



    fun increaseProgress() {
        if (currentProgress < 100) {

            progressBarHorizontal.progress = etage
            textViewHorizontalProgress.text = etage.toString()
        }
    }
}