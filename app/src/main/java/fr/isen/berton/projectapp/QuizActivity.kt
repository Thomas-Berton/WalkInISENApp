package fr.isen.berton.projectapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_quiz.*

class QuizActivity : AppCompatActivity() {

    private val tipsList = arrayListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        radioButton1.setOnClickListener {
            if (radioButton1.isChecked === true ) {
                radioButton2.isChecked = false
                radioButton3.isChecked = false
            }
        }

        radioButton2.setOnClickListener {
            if (radioButton2.isChecked === true ) {
                radioButton1.isChecked = false
                radioButton3.isChecked = false
            }
        }

        radioButton3.setOnClickListener {
            if (radioButton3.isChecked === true ) {
                radioButton1.isChecked = false
                radioButton2.isChecked = false
            }
        }

        validateButton.setOnClickListener {
               val intent = Intent(this, QuizActivity::class.java)
               intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
               startActivity(intent)
           }
       }


}
