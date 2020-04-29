package fr.isen.berton.projectapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home_page.*


class HomePageActivity : AppCompatActivity() {
    var currentProgress = 0
    var etage = 0
    var answersPoint : Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)


        RankPicture.setOnClickListener {}
        AwarenessPicture.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            // intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            //Log.d("test","Les points sont : "+etage)
            startActivityForResult(intent,0)
        }
        GamePicture.setOnClickListener { }


        reloadButton.setOnClickListener{
           if ( reloadButton.isPressed === true) {
               //Toast.makeText(this, "Test OK", Toast.LENGTH_LONG).show();
           }
       }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
           etage += data?.getIntExtra("points",0)!!

            increaseProgress()
        }
    }

    fun increaseProgress() {
        if (currentProgress < 100) {

            progressBarHorizontal.progress = etage
            textViewHorizontalProgress.text = etage.toString()
        }
    }

    fun getPoints(){
        answersPoint += intent.getSerializableExtra("points") as Int
        etage = etage + answersPoint
    }

    fun saveData(v: View) {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt("points", textViewHorizontalProgress.text.toString().toInt())
            commit()
        }
    }

    fun getData(view: View) {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        val int_number = sharedPref.getInt("points", 0)
        Toast.makeText(this, " Points : $int_number", Toast.LENGTH_LONG).show()

    }
}