package fr.isen.berton.projectapp

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.hardware.SensorEventListener

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
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
import java.text.SimpleDateFormat
import java.util.*


class HomePageActivity : AppCompatActivity(), SensorEventListener{
    var currentProgress = 0
    var etage = 0
    var answersPoint: Int = 0
    var thisActivity = this
    var fetchedScore: Int = 0
    var fetchedSteps: Int = 0
    var currentDate = Date()
    var totalSteps : Int = 0
    var date : String = ""
    var running = false
    var sensorManager: SensorManager? = null
    var totalScore: Int = 0;




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager


        date = displayDate()

        dateView.text = "Aujourd'hui \n\n"+"${date}"

        //-------------Navigation menu----------------------------
        navigation_view.setSelectedItemId(R.id.action_home);
        navigation_view.setOnNavigationItemSelectedListener {item ->
            var activity = ""
            when(item.itemId){
                R.id.action_home-> activity = "HomePageActivity"
                R.id.action_Podium -> activity = "RankingActivity"
                R.id.action_quiz -> activity = "QuizActivity"
                R.id.action_pinguin -> activity = "PinguinGame"
            }
           // Toast.makeText(this@HomePageActivity, "$activity clicked!", Toast.LENGTH_SHORT).show()
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

        checkIfUserLoggedIn()
        getUserData()
        //updateUserStepsScore(1)
        //resetEtage()

        val sharedPref = thisActivity?.getPreferences(Context.MODE_PRIVATE) ?: return
        etage = sharedPref.getInt("points", 0)
/*
        increaseProgress()
*/



      /*  RankPicture.setOnClickListener {}
        AwarenessPicture.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            // intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            //Log.d("test","Les points sont : "+etage)

            startActivityForResult(intent, 0)

        }
        GamePicture.setOnClickListener { }*/


    }

    private fun checkIfUserLoggedIn () {
        val uid = FirebaseAuth.getInstance().uid
        if(uid == null){
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        FirebaseAuth.getInstance().signOut()

        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu_logout,menu)
        return super.onCreateOptionsMenu(menu)
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
                fetchedSteps = user!!.userSteps
                fetchedScore = user!!.userScore
                progressBarHorizontal.progress = fetchedScore
                totalSteps = fetchedSteps
                totalScore = fetchedScore + fetchedSteps
                displayScore()
                displaySteps()


            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun updateUserStepsScore (additionalStepScore: Int) {

        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/")
        val refScore = FirebaseDatabase.getInstance().getReference("/users/$uid/userScore")
        val refSteps = FirebaseDatabase.getInstance().getReference("/users/$uid/userSteps")
        var userScore: Int = 0
        var userUpdatedScore: Int = 0
        var userSteps: Int = 0
        var userUpdatedSteps: Int = 0
        ref.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot){
                val user = dataSnapshot.getValue(SignUpActivity.User::class.java)
                userScore = user!!.userScore
                userUpdatedScore = userScore + additionalStepScore
                totalScore = userUpdatedScore

                userSteps = user!!.userSteps
                userUpdatedSteps = userSteps + additionalStepScore
                totalSteps = userUpdatedSteps



                refScore.setValue(userUpdatedScore)
                    .addOnSuccessListener {
                        Log.d("USER SCORE UPDATED ", userUpdatedScore.toString())
                    }

                refSteps.setValue(userUpdatedSteps)
                    .addOnSuccessListener {
                        Log.d("USER STEPS UPDATED ", userUpdatedSteps.toString())
                    }

                displayScore()
                displaySteps()

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }

    public fun displayScore(){
        Score.text = totalScore.toString()
    }

    public fun displaySteps(){
        stepsValue.text = totalSteps.toString()
    }

    override fun onResume() {
        super.onResume()
        running = true
        var stepsSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepsSensor == null) {
            Toast.makeText(this, "No Step Counter Sensor !", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepsSensor, SensorManager.SENSOR_DELAY_UI)


        }
    }

    override fun onPause() {
        super.onPause()
        running = false
        sensorManager?.unregisterListener(this)
    }


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }


    override fun onSensorChanged(event: SensorEvent) {
        if (running) {
            updateUserStepsScore(event.values[0].toInt())


            /*       if (currentProgress < 1000) {
                progressBarHorizontal.progress = fetchedScore
                Score.text = fetchedScore.toString()

        */

        }
        }

        fun resetEtage() {
            val sharedPref = thisActivity?.getPreferences(Context.MODE_PRIVATE) ?: return
            with(sharedPref.edit()) {
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

        public fun displayDate(): String {
            val formatter = SimpleDateFormat("dd/MM/yyyy/H/mm/ss")
            var dateString = formatter.format(currentDate)
            val components = dateString.split("/")

            var year = components[2]
            var month = components[1]
            var day = components[0]

            if (month == "01") {
                month = "Janvier"
            } else if (month == "02") {
                month = "Février"
            } else if (month == "03") {
                month = "Mars"
            } else if (month == "04") {
                month = "Avril"
            } else if (month == "05") {
                month = "Mai"
            } else if (month == "06") {
                month = "Juin"
            } else if (month == "07") {
                month = "Juillet"
            } else if (month == "08") {
                month = "Août"
            } else if (month == "09") {
                month = "Septembre"
            } else if (month == "10") {
                month = "Octobre"
            } else if (month == "11") {
                month = "Novembre"
            } else if (month == "12") {
                month = "Décembre"
            }

            var dateJour = "$day $month $year"
            return dateJour
        }
    }

