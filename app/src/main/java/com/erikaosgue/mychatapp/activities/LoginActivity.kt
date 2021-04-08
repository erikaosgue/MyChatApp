package com.erikaosgue.mychatapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.erikaosgue.mychatapp.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null

    lateinit var actLoginBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(actLoginBinding.root)

        mAuth = FirebaseAuth.getInstance()

        setUpUI()


    }

    private fun setUpUI() {

        actLoginBinding.loginButtonId.setOnClickListener {

            val email = actLoginBinding.loginEmailId.text.toString().trim()
            val password = actLoginBinding.loginPasswordId.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                singIn(email, password)
            }
            else {
                Toast.makeText(this, "Sorry! Login fail", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun singIn(email: String, password: String) {

        mAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener { task: Task<AuthResult> ->

                if (task.isSuccessful) {
                    Toast.makeText(this, "Successful Login", Toast.LENGTH_LONG).show()

                    val userName = email.split("@")[0]
                    val intent = DashboardActivity.getNewIntent(this).apply {
                        putExtra("name",userName)
                    }
                    startActivity(intent)
                    finish()
                }
                else {
                    Toast.makeText(this, "Fail Login ${task.exception}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
