package com.erikaosgue.mychatapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.erikaosgue.mychatapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

	private var mAuth: FirebaseAuth? = null
	private var user: FirebaseUser? = null
	private var mAuthListener: FirebaseAuth.AuthStateListener? = null

	lateinit var  activityMainBinding: ActivityMainBinding
 	override fun onCreate(savedInstanceState: Bundle?) {
 		super.onCreate(savedInstanceState)
 		activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
 		setContentView(activityMainBinding.root)

		mAuth = FirebaseAuth.getInstance()

		//Checks if the user is login or logOut
		checkUserLogIn()


		setUpUICreateAccount()
		setUpUISingIn()

  }

	override fun onStart() {
		super.onStart()
		mAuth?.addAuthStateListener(mAuthListener)

	}

	override fun onStop() {
		super.onStop()

		if (mAuthListener != null) {
			mAuth?.removeAuthStateListener(mAuthListener!!)
		}
	}

	private fun checkUserLogIn() {
		mAuthListener = FirebaseAuth.AuthStateListener {
				firebaseAuth: FirebaseAuth ->

			user = firebaseAuth.currentUser
			if (user != null) {
				//Go to dashBoard
				startActivity(Intent(this, DashboardActivity::class.java))
				finish()
			}else{
				Toast.makeText(this, "Not Sing In", Toast.LENGTH_LONG).show()
			}
		}
	}


	private fun setUpUICreateAccount() {
		activityMainBinding.createAccountButton.setOnClickListener {
			startActivity(Intent(this, CreateAccountActivity::class.java))
		}
	}


	private fun setUpUISingIn(){
		activityMainBinding.logInButton.setOnClickListener {
			startActivity(Intent(this, LoginActivity::class.java))
		}
	}



}