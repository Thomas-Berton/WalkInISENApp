package fr.isen.berton.projectapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home_page.*

class RankingActivity : AppCompatActivity() {


    data class UserRank(

        @PropertyName("name") val userName: String?,
        @PropertyName("score") var userScore: Int?)
    {
        constructor() : this(null,null)
    }

    var userRankList : ArrayList<UserRank?> =  ArrayList<UserRank?>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        getUsersScore()

    }


    private fun getUsersScore () {

        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot){
                dataSnapshot.children.forEach{
                    val user = it.getValue(SignUpActivity.User::class.java)
                    val userRank = UserRank( user?.userName.toString(),user?.userScore)
                    userRankList.add(userRank)
                }
                Log.d("Rank ARRAY",userRankList.toString() )
                userRankList.sortBy { it?.userScore }

                Log.d("sorted RANKLIST",userRankList.toString())

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}


