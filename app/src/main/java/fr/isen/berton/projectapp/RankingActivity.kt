package fr.isen.berton.projectapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_ranking.*
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class RankingActivity : AppCompatActivity() {


    data class UserRank(

        @PropertyName("id") var userId: String?,
        @PropertyName("name") val userSurName: String?,
        @PropertyName("score") var userScore: Int?)

    {
        constructor() : this(null,null,null)
    }

    var userRankList : ArrayList<UserRank?> =  ArrayList<UserRank?>()


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

        getUsersScore()

    }


    private fun getUsersScore () {

        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        val refUser = FirebaseDatabase.getInstance().getReference("/users/$uid")
        var loggedUserRank = UserRank()
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot){
                dataSnapshot.children.forEach{

                    val user = it.getValue(SignUpActivity.User::class.java)
                    val userRank = UserRank(it.key.toString(), user?.userSurname.toString(),user?.userScore)
                    userRankList.add(userRank)

                    if(it.key == uid){
                        loggedUserRank = UserRank(uid, user?.userSurname.toString(),user?.userScore )
                    }

                }
                Log.d("Rank ARRAY",userRankList.toString() )
                userRankList.sortBy { it?.userScore }

                Log.d("sorted RANKLIST",userRankList.toString())

                displayUsersRanking(loggedUserRank)

            }



            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }

    public fun displayUsersRanking (userToFind: UserRank) {
        var arrayLength: Int = userRankList.size

        Log.d("ARRAYLENGTH",arrayLength.toString())

        userRnk1.text = userRankList[arrayLength-1]?.userSurName
        userScore1.text = userRankList[arrayLength-1]?.userScore.toString()
        userRnk2.text = userRankList[arrayLength-2]?.userSurName
        userScore2.text = userRankList[arrayLength-2]?.userScore.toString()
        userRnk3.text = userRankList[arrayLength-3]?.userSurName
        userScore3.text = userRankList[arrayLength-3]?.userScore.toString()
        userRnk4.text = userRankList[arrayLength-4]?.userSurName
        userScore4.text = userRankList[arrayLength-4]?.userScore.toString()
        userRnk5.text = userRankList[arrayLength-5]?.userSurName
        userScore5.text = userRankList[arrayLength-5]?.userScore.toString()
        userRnk6.text = userRankList[arrayLength-6]?.userSurName
        userScore6.text = userRankList[arrayLength-6]?.userScore.toString()
        userRnk7.text = userRankList[arrayLength-7]?.userSurName
        userScore7.text = userRankList[arrayLength-7]?.userScore.toString()

       /* userRankList.find(){
            it?.userId == uid
            return
        }*/
        val loggedUserIndex = userRankList.indexOf(userToFind)
        val loggedUserRankFromIndex = arrayLength - loggedUserIndex
        if(loggedUserIndex != -1){
            loggedUserScore.text = userRankList[loggedUserIndex]?.userScore.toString()
            loggedUserRank.text = loggedUserRankFromIndex.toString()
        }

    }
}


