package com.erikaosgue.mychatapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erikaosgue.mychatapp.R
import com.erikaosgue.mychatapp.adapters.LinearLayoutManagerWithSmoothScroller
import com.erikaosgue.mychatapp.adapters.MessengerAdapterFirebase
import com.erikaosgue.mychatapp.databinding.ActivityChatBinding
import com.erikaosgue.mychatapp.models.FriendlyMessage
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class ChatActivity : AppCompatActivity() {

    //To use it with the Database
    var otherUserId: String?=null
    var mFirebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    var mFirebaseDatabaseRef : DatabaseReference?= null

    // Recycler view variables
    var mLinearLayoutManager: LinearLayoutManager? = null
    var mFirebaseAdapter : MessengerAdapterFirebase? = null

    lateinit var actChatBinding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actChatBinding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(actChatBinding.root)

        mFirebaseDatabaseRef = FirebaseDatabase.getInstance().reference

        otherUserId = intent.extras?.getString("otherUserId")
        var profileImageLink = intent.extras?.getString("profile").toString()
        mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager?.stackFromEnd = true
        mLinearLayoutManager?.setSmoothScrollbarEnabled(true)


        supportActionBar?.setDisplayShowTitleEnabled(false)
        // Set the arrow to go back

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowCustomEnabled(true)

        val inflater = this.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // Inflating a custom bar Name to show name and Image of the person the user is talking to
        val actionBarView = inflater.inflate(R.layout.custom_bar_image, null)
        val customBarName = actionBarView.findViewById<TextView>(R.id.customBarName)
        val customBarImage = actionBarView.findViewById<ImageView>(R.id.customBarCircleImage)
        customBarName.text = intent.extras?.getString("name")

        Picasso.get()
            .load(profileImageLink)
            .placeholder(R.drawable.profile_img)
            .into(customBarImage)


        // Add a custom bar Name to show name and Image of the person the user is talking to
        supportActionBar?.customView = actionBarView

        // Create A unique instance of MessengerAdapterFirebase
        mFirebaseAdapter = MessengerAdapterFirebase.getMessengerAdapter(otherUserId)


        actChatBinding.messageRecyclerView.apply {
            layoutManager = mLinearLayoutManager
            adapter = mFirebaseAdapter
        }

        val sizeList = actChatBinding.messageRecyclerView.adapter.itemCount
        actChatBinding.messageRecyclerView.smoothScrollToPosition(sizeList!!)

        actChatBinding.sendButton.setOnClickListener {

            if (intent.extras?.get("name").toString().isNotEmpty()) {

                val currentUserName = intent.extras?.get("name").toString().trim()
                val mCurrentUserId = mFirebaseUser?.uid.toString()

                // Create the object Message to add it into the database
                /* CurrentUserId, message, UserName */
                val friendlyMessage = FriendlyMessage(
                    mCurrentUserId,
                    actChatBinding.messageEdt.text.toString().trim(),
                    currentUserName)

                mFirebaseDatabaseRef = FirebaseDatabase.getInstance().reference.child("messages")
                mFirebaseDatabaseRef!!.push()
                    .setValue(friendlyMessage).addOnCompleteListener{
                            task: Task<Void> ->

                        if (task.isSuccessful) {
                            Log.d("Success", "Message created!")
                        }
                        else {
                            Log.d("Fail", "Fail Message not created!, ${task.exception}")
                        }
                    }

                // Set the bar to write a message to empty every time the users
                // send a message
                actChatBinding.messageEdt.setText("")
            }
        }
    }
    override fun onStart() {

        super.onStart()
        mFirebaseAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        mFirebaseAdapter?.stopListening()
    }

}