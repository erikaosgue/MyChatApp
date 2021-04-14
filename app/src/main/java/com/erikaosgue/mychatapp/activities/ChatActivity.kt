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
        mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager?.stackFromEnd = true

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        // My three options to call Firebase Adapter

        //With an Instance of the object
//        mFirebaseAdapter = messengerAdapterFirebase
        // With Object Declaration
//        mFirebaseAdapter = MessengerAdapterFirebase
        // With companion Object
        mFirebaseAdapter = MessengerAdapterFirebase.getMessengerAdapter(otherUserId)

        //===========================================================
       /* val messageQuery = FirebaseDatabase.getInstance().reference.child("messages")
        val messageOptions = FirebaseRecyclerOptions.Builder<FriendlyMessage>()
            .setQuery(messageQuery, FriendlyMessage::class.java)
            .build()



        // FirebaseRecyclerAdapter<model(class), ViewHolder>
//var messengerAdapterFirebase : FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>? = object: FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>(messageOptions){
//object MessengerAdapterFirebase : FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>(messageOptions){

        class MessageViewHolder(itemView: View, var otherUserId: String?): RecyclerView.ViewHolder(itemView) {


            fun bindView(friendlyMessage: FriendlyMessage){
                Log.d("Chat 2: ", "Chats messages")
                val messageTextView = itemView.findViewById<TextView>(R.id.messageTextview)
                val messengerTextView = itemView.findViewById<TextView>(R.id.messengerTextview)
                val profileImageView = itemView.findViewById<ImageView>(R.id.messengerImageView)
                val profileImageViewRight = itemView.findViewById<ImageView>(R.id.messengerImageViewRight)

                messengerTextView.text = friendlyMessage.name
                messageTextView.text = friendlyMessage.text

                val currentUserId = com.erikaosgue.mychatapp.adapters.mFirebaseUser?.uid

                val isMe: Boolean = friendlyMessage.id == currentUserId

                if (isMe) {
                    //Me to the right Side
                    profileImageViewRight.visibility = View.VISIBLE
                    profileImageView.visibility = View.GONE
                    messageTextView.gravity = (Gravity.CENTER_VERTICAL or Gravity.END)
                    messengerTextView.gravity = (Gravity.CENTER_VERTICAL or Gravity.END)
                    getImage(currentUserId, profileImageViewRight, profileImageView, messageTextView, messengerTextView)

                }else {
                    // The other person show image view to the left side
                    profileImageViewRight.visibility = View.GONE
                    profileImageView.visibility = View.VISIBLE
                    messageTextView.gravity = (Gravity.CENTER_VERTICAL or Gravity.START)
                    messengerTextView.gravity = (Gravity.CENTER_VERTICAL or Gravity.START)
                    getImage(otherUserId,  profileImageViewRight, profileImageView, messageTextView, messengerTextView)

                }

            }
            private fun getImage(userId: String?, profileImageViewRight: ImageView, profileImageView: ImageView, messageTextView: TextView, messengerTextView: TextView) {


                //Get the Image URl
                com.erikaosgue.mychatapp.adapters.mFirebaseDatabaseRef
                    .child("Users")
                    .child(userId!!)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            Log.d("Chat3: ", "Chats messages")

                            val imageUrl = snapshot.child("thumb_image").value.toString()
                            val displayName = snapshot.child("display_name").value

                            messageTextView.text = displayName.toString()

                            Picasso.get()
                                .load(imageUrl)
                                .placeholder(R.drawable.profile_img)
                                .into(profileImageViewRight)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })

            }
        }
        mFirebaseAdapter = object : FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>(messageOptions){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
                Log.d("Chat1: ", "Chats messages")
                val layoutInflate = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
                return MessageViewHolder(layoutInflate, otherUserId)

            }

            override fun onBindViewHolder(holder: MessageViewHolder, position: Int, friendlyMessage: FriendlyMessage) {
                if (friendlyMessage.text != null) {
                    holder.bindView(friendlyMessage)
                }
            }


        }*/



        //==============================================================
        actChatBinding.messageRecyclerView.apply {
            layoutManager = mLinearLayoutManager
            adapter = mFirebaseAdapter
        }

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
                Log.d("friendly Message: ", friendlyMessage.text.toString())

                mFirebaseDatabaseRef = FirebaseDatabase.getInstance().reference.child("messages")
//                mFirebaseDatabaseRef = FirebaseDatabase.getInstance().getReference("messages")
                mFirebaseDatabaseRef!!.push()
                    .setValue(friendlyMessage).addOnCompleteListener{
                            task: Task<Void> ->

                        if (task.isSuccessful) {
                            Toast.makeText(this, "Successful Message created!", Toast.LENGTH_LONG).show()
                            Log.d("Success", "Message created!")
                        }
                        else {
                            Toast.makeText(this, "Fail Message not created! ${task.exception}", Toast.LENGTH_LONG).show()
                            Log.d("Fail", "Fail Message not created!, ${task.exception}")
                        }
                    }

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