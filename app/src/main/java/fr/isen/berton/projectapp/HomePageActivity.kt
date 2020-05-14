package fr.isen.berton.projectapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
/*
import androidx.preference.PreferenceManager
*/
import kotlinx.android.synthetic.main.activity_home_page.*



class HomePageActivity : AppCompatActivity() {
    var currentProgress = 0
    var etage = 0
    var answersPoint: Int = 0
    var thisActivity = this
    var fetchedScore: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        //-------------Navigation menu----------------------------
        navigation_view.setSelectedItemId(R.id.action_home);
        navigation_view.setOnNavigationItemSelectedListener {item ->
            var activity = ""
            when(item.itemId){
                R.id.action_home-> activity = "HomePageActivity"
                //R.id.action_Podium -> activity = "Podium"
                R.id.action_quiz -> activity = "QuizActivity"
                //R.id.action_pinguin -> activity = "@string/pinguin"
            }
           // Toast.makeText(this@HomePageActivity, "$activity clicked!", Toast.LENGTH_SHORT).show()
            if(activity == "HomePageActivity"){
                startActivity(Intent(this, HomePageActivity::class.java))
            }
/*            if(activity == "Podium"){
               }*/
            if(activity == "QuizActivity"){
                startActivity(Intent(this, QuizActivity::class.java))
            }
/*            if(activity == ""){
            }*/
            return@setOnNavigationItemSelectedListener true
        }
        //--------------------------------------------------------------

        getUserData()

        //resetEtage()

        val sharedPref = thisActivity?.getPreferences(Context.MODE_PRIVATE) ?: return
        etage = sharedPref.getInt("points", 0)
/*
        increaseProgress()
*/


        RankPicture.setOnClickListener {}
        AwarenessPicture.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            // intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            //Log.d("test","Les points sont : "+etage)

            startActivityForResult(intent, 0)

        }
        GamePicture.setOnClickListener { }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            etage += data?.getIntExtra("points", 0)!!
            val sharedPref = thisActivity?.getPreferences(Context.MODE_PRIVATE) ?: return
            with (sharedPref.edit()) {
                putInt("points", etage)
                commit()
            }
/*
            increaseProgress()
*/
        }
    }

    private fun getUserData () {

        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{



            override fun onDataChange(dataSnapshot: DataSnapshot){
               val user = dataSnapshot.getValue(SignUpActivity.User::class.java)
                Name.text =   user?.userName.toString().capitalize()
                Surame.text = user?.userSurname.toString().capitalize()
                fetchedScore = user!!.userScore
                progressBarHorizontal.progress = fetchedScore
                Score.text = fetchedScore.toString()

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    fun resetEtage(){
        val sharedPref = thisActivity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt("points", 0)
            commit()
        }
    }

    /*fun increaseProgress() {
        if (currentProgress < 100) {

        }
    }*/

    fun getPoints() {
        answersPoint += intent.getSerializableExtra("points") as Int
        etage = etage + answersPoint
    }

}
