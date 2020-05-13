package fr.isen.berton.projectapp

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_home_page.*



class HomePageActivity : AppCompatActivity() {
    var currentProgress = 0
    var etage = 0
    var thisActivity = this


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        //resetPoints()
        val sharedPref = thisActivity?.getPreferences(Context.MODE_PRIVATE) ?: return
        etage = sharedPref.getInt("points", 0)
        increaseProgress()


        RankPicture.setOnClickListener {}
        AwarenessPicture.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivityForResult(intent, 0)


        }
        GamePicture.setOnClickListener { }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            //RECUPERATION DES POINTS DU QUIZ avec GETINTEXTRA
            etage += data?.getIntExtra("points", 0)!!
            //Sauvegarde dans un shared preference
            val sharedPref = thisActivity?.getPreferences(Context.MODE_PRIVATE) ?: return
            with (sharedPref.edit()) {
                putInt("points", etage)
                commit()
            }
            increaseProgress()
        }
    }


    fun increaseProgress() {
        if (currentProgress < 100) {
            progressBarHorizontal.progress = etage
            textViewHorizontalProgress.text = etage.toString()
        }
    }

    fun resetPoints(){
        val sharedPref = thisActivity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt("points", 0)
            commit()
        }
    }

    /*fun getPoints() {
        answersPoint += intent.getSerializableExtra("points") as Int
        etage = etage + answersPoint
    }*/
}
