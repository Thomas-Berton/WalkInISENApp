package fr.isen.berton.projectapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.isen.berton.projectapp.R
import fr.isen.berton.projectapp.SignUpActivity
import kotlinx.android.synthetic.main.activity_valid.*

class ValidActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_valid)

        returnHome.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}
