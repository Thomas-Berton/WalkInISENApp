package fr.isen.berton.projectapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_quiz.*

class RankingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
        //-------------Navigation menu----------------------------
        navigation_view.setSelectedItemId(R.id.action_Podium);
        navigation_view.setOnNavigationItemSelectedListener {item ->
            var activity = ""
            when(item.itemId){
                R.id.action_home-> activity = "HomePageActivity"
                R.id.action_Podium -> activity = "RankingActivity"
                R.id.action_quiz -> activity = "QuizActivity"
                R.id.action_pinguin -> activity = "PinguinGame"
            }
            // Toast.makeText(this@QuizActivity, "$activity clicked!", Toast.LENGTH_SHORT).show()
            if(activity == "HomePageActivity"){
                startActivity(Intent(this, HomePageActivity::class.java))
            }
            if(activity == "RankingActivity"){
                startActivity(Intent(this, RankingActivity::class.java))
               }
            if(activity == "QuizActivity"){
                startActivity(Intent(this, QuizActivity::class.java))
            }
            if(activity == "PinguinGame"){
                startActivity(Intent(this, PinguinGame::class.java))
            }
            return@setOnNavigationItemSelectedListener true
        }
        //--------------------------------------------------------------
    }
}
