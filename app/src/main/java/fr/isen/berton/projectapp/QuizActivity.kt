package fr.isen.berton.projectapp

import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_quiz.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
//import kotlinx.android.synthetic.main.activity_home_page.*
//import kotlinx.android.synthetic.main.activity_quiz.HomePagePicture
import kotlinx.android.synthetic.main.activity_sign_up.*
/*
import kotlinx.android.synthetic.main.activity_quiz_done.*
*/
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random



class QuizActivity : AppCompatActivity() {
    private val TAG = "firebase"
    private val tipsList = arrayListOf<String>()
    private val questionList = arrayListOf<String>()
    private val r1List = arrayListOf<String>()
    private val r2List = arrayListOf<String>()
    private val rvList = arrayListOf<String>()
    private var index = 0
    private var answersPoints = 0
    var thisActivity = this
    var oldDate = 0
    var date = 0
    var currentDate = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        date = displayDate()

        //Toast.makeText(this,"Nous sommes le : "+date, Toast.LENGTH_LONG).show()
        val sharedPref = thisActivity?.getPreferences(Context.MODE_PRIVATE) ?: return
        oldDate = sharedPref.getInt("date",0)
        Log.d("testPref","La date est : "+oldDate)

        val sharedPrefIndex = thisActivity?.getPreferences(Context.MODE_PRIVATE) ?: return
        index = sharedPrefIndex.getInt("index",0)

        if (oldDate < date){
            setContentView(R.layout.activity_quiz)
            goodAnswer.isVisible = false
            badAnswer.isVisible = false
            pengouinGood.isVisible = false
     /*       HomePagePicture.setOnClickListener{
                val intent = Intent(this, HomePageActivity::class.java)
                //ENVOI DES POINTS DU QUIZ VERS LA HOME PAGE avec PUTEXTRA
                intent.putExtra("points", answersPoints)
                setResult(RESULT_OK, intent)

                finish()

            }*/
            //-------------Navigation menu----------------------------
            navigation_view.setSelectedItemId(R.id.action_quiz);
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


            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("tips")
            val myRefQ = database.getReference("quiz/questions")
            val myRefR1 = database.getReference("quiz/réponse1")
            val myRefR2 = database.getReference("quiz/réponse2")
            val myRefRV = database.getReference("quiz/réponseV")

            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach {
                        val value = it.getValue(String::class.java)
                        Log.d(TAG, "Value is: $value")
                        tipsList.add(value ?: "")
                    }
                    displayRandomTips()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })


            myRefQ.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach {
                        val value = it.getValue(String::class.java)
                        questionList.add(value ?: "")
                    }
                    displayRandomQuestion(index)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })

            myRefR1.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach {
                        val value = it.getValue(String::class.java)
                        r1List.add(value ?: "")
                    }
                    displayRandomAnswer1(index)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })

            myRefR2.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach {
                        val value = it.getValue(String::class.java)
                        r2List.add(value ?: "")
                    }
                    displayRandomAnswer2(index)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })

            myRefRV.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach {
                        val value = it.getValue(String::class.java)
                        rvList.add(value ?: "")
                    }
                    displayRandomAnswerV(index)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })

            r1.setOnClickListener {
                if (r1.isChecked === true) {
                    r2.isChecked = false
                    r3.isChecked = false
                }
            }

            r2.setOnClickListener {
                if (r2.isChecked === true) {
                    r1.isChecked = false
                    r3.isChecked = false
                }
            }

            r3.setOnClickListener {
                if (r3.isChecked === true) {
                    r1.isChecked = false
                    r2.isChecked = false
                }
            }

            validateButton.setOnClickListener {
                //index++ appel a la fct des jours
                displayRandomQuestion(index)
                displayRandomAnswer1(index)
                displayRandomAnswer2(index)
                displayRandomAnswerV(index)
                if (r3.isChecked === true) {
                    updateUserScore()                               //tout se passe ici
                    answersPoints = 10
                    goodAnswer.isVisible = true
                    quizView.isVisible = false
                    r1.isVisible = false
                    r2.isVisible = false
                    r3.isVisible = false
                    validateButton.isVisible = false

                }
                if (r1.isChecked === true || r2.isChecked === true) {
                    badAnswer.isVisible = true
                    quizView.isVisible = false
                    r1.isVisible = false
                    r2.isVisible = false
                    r3.isVisible = false
                    validateButton.isVisible = false
                }
                sharedPref ()
                index += 1
                sharedPrefIndex()
                validateButton.isVisible = false

            }
        }else
        {
            //Si le quiz a deja été fait
            setContentView(R.layout.activity_quiz_done)
        }
    }

    private fun updateUserScore () {

        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        var userScore: Int = 0
        var userUpdatedScore: Int = 0
        ref.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot){
                val user = dataSnapshot.getValue(SignUpActivity.User::class.java)
                    val userName =   user?.userName.toString().capitalize()
                    val userSurname = user?.userSurname.toString().capitalize()
                    val userMail = user?.userEmail.toString()
                    userScore = user!!.userScore
                    userUpdatedScore = userScore + 10

                val updatedUser = SignUpActivity.User(
                    userMail,
                    userSurname,
                    userName,
                    userUpdatedScore
                )

                ref.setValue(updatedUser)
                    .addOnSuccessListener {
                        Log.d("user score updated ", userUpdatedScore.toString())
                    }

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })



    }

    private fun displayRandomTips() {
        tipsView.isVisible = true
        val randomIndexList = Random.nextInt(tipsList.size)
        val element = tipsList[randomIndexList]
        tipsView.text = element
    }

    private fun displayRandomQuestion(index: Int) {
        quizView.isVisible = true
        val enonce = questionList[index]
        quizView.text = enonce

    }

    private fun displayRandomAnswer1(index: Int) {
        r1.isVisible = true
        val rep1 = r1List[index]
        r1.text = rep1
    }

    private fun displayRandomAnswer2(index: Int) {
        r2.isVisible = true
        val rep2 = r2List[index]
        r2.text = rep2
    }

    private fun displayRandomAnswerV(index: Int) {
        r3.isVisible = true
        val repV = rvList[index]
        r3.text = repV
    }

    private fun displayDate() : Int
    {
        val formatter = SimpleDateFormat("dd/MM/yyyy/H/mm/ss")
        var dateString = formatter.format(currentDate)
        val components = dateString.split("/")

        var hour = components[3].toInt()
        var minutes = components[4].toInt()
        var seconds = components[5].toInt()
        var year = components[2].toInt()
        var month = components[1].toInt()
        var day = components[0].toInt()

        //Toast.makeText(this,"Nous sommes le $day $month $year"+"Il est $hour $minutes $seconds",Toast.LENGTH_LONG).show()
        var dateJour = day+(month*100)+(year*1000)
        return dateJour
    }

    private fun sharedPref ()
    {
        val sharedPref = thisActivity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt("date", date)
            commit()
        }
    }

    private fun sharedPrefIndex ()
    {
        val sharedPrefIndex = thisActivity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPrefIndex.edit()) {
            putInt("index", index)
            commit()
        }
    }

}
