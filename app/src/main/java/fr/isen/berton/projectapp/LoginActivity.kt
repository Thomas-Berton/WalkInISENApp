package fr.isen.berton.projectapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance();

        validateToLogin.setOnClickListener{
            doLogin()
        }

        button_sign_up.setOnClickListener() {
            startActivity(Intent(this, SignUpActivity::class.java))
        }


    }

  /*  public override fun onStart() {
        super.onStart()

        if(Constant.justDisconnected == false){

            val currentUser = mAuth.currentUser
            updateUI(currentUser)
        }
        // Check if user is signed in (non-null) and update UI accordingly.

    }*/

    fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            Toast.makeText(this, "U Signed In successfully", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, HomePageActivity::class.java))
        } else {
        }
    }

    public fun show_password_now(view : View){

        editPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
    }

    public fun doLogin(){
        val email = editUserName.text.toString()
        val password = editPassword.text.toString()

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(baseContext, "Authentication success.",
                            Toast.LENGTH_SHORT).show()
                        val user = mAuth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Connexion", "Email ou mot de passe incorrect", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }

                }




    }



    public fun switchSignUp(view: View){
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent);
    }

}