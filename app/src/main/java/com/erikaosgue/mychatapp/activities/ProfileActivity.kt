package com.erikaosgue.mychatapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.erikaosgue.mychatapp.R
import com.erikaosgue.mychatapp.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {

    var mCurrentUser: FirebaseUser? = null
    var mUsersDatabase: DatabaseReference? = null
    var userId: String? = null

    lateinit var actProfileBinding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actProfileBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(actProfileBinding.root)


        supportActionBar?.title = "Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.extras != null) {
            userId = intent.extras!!["userId"].toString()

            mCurrentUser = FirebaseAuth.getInstance().currentUser
            mUsersDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(userId!!)

            setUpProfile()
        }

    }
    private fun setUpProfile() {
        mUsersDatabase?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Retrieve

                var displayName = snapshot.child("display_name").value.toString()
                var status = snapshot.child("status").value.toString()
                var image = snapshot.child("image").value.toString()

                actProfileBinding.profileName.text = displayName
                actProfileBinding.profileStatus.text = status

                Picasso.get()
                    .load(image)
                    .placeholder(R.drawable.profile_img)
                    .into(actProfileBinding.profilePicture)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}