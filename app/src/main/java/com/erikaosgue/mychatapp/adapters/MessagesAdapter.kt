package com.erikaosgue.mychatapp.adapters

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.erikaosgue.mychatapp.R
import com.erikaosgue.mychatapp.models.FriendlyMessage
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


var mFirebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
var mFirebaseDatabaseRef = FirebaseDatabase.getInstance().reference

val messageQuery = FirebaseDatabase.getInstance()
    .reference
    .child("messages")

val messageOptions: FirebaseRecyclerOptions<FriendlyMessage> = FirebaseRecyclerOptions.Builder<FriendlyMessage>()
    .setQuery(messageQuery, FriendlyMessage::class.java)
    .build()



class MessengerAdapterFirebase(var otherUserId: String?) : FirebaseRecyclerAdapter<FriendlyMessage, MessengerAdapterFirebase.MessageViewHolder>(messageOptions) {

    companion object {
        fun getMessengerAdapter(otherUserId: String?): MessengerAdapterFirebase {
            return MessengerAdapterFirebase(otherUserId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view, otherUserId)

    }

    override fun onBindViewHolder(
        holder: MessageViewHolder,
        position: Int,
        friendlyMessage: FriendlyMessage
    ) {
        if (friendlyMessage.text != null) {
            holder.bindView(friendlyMessage)
        }
    }


    class MessageViewHolder(itemView: View, var otherUserId: String?) :
        RecyclerView.ViewHolder(itemView) {


        fun bindView(friendlyMessage: FriendlyMessage) {
            val messageTextView = itemView.findViewById<TextView>(R.id.messageTextview)
            val messengerTextView = itemView.findViewById<TextView>(R.id.messengerTextview)
            val profileImageView = itemView.findViewById<ImageView>(R.id.messengerImageView)
            val profileImageViewRight =
                itemView.findViewById<ImageView>(R.id.messengerImageViewRight)

            val currentUserId = mFirebaseUser?.uid

            val isMe: Boolean = friendlyMessage.id == currentUserId

            if (isMe) {
                //Me to the right Side
                profileImageViewRight.visibility = View.VISIBLE
                profileImageView.visibility = View.GONE
                messageTextView.gravity = (Gravity.CENTER_VERTICAL or Gravity.END)
                messengerTextView.gravity = (Gravity.CENTER_VERTICAL or Gravity.END)
                getImage(
                    currentUserId,
                    profileImageViewRight,
                    messageTextView,
                    messengerTextView,
                    friendlyMessage.text
                )

            } else {
                // The other person show image view to the left side
                profileImageViewRight.visibility = View.GONE
                profileImageView.visibility = View.VISIBLE
                messageTextView.gravity = (Gravity.CENTER_VERTICAL or Gravity.START)
                messengerTextView.gravity = (Gravity.CENTER_VERTICAL or Gravity.START)
                getImage(
                    otherUserId,
                    profileImageView,
                    messageTextView,
                    messengerTextView,
                    friendlyMessage.text
                )
            }
        }
        private fun getImage(
            userId: String?,
            profileImage: ImageView,
            messageTextView: TextView,
            messengerTextView: TextView,
            message: String?
        ) {


            //Get the Image URl
            mFirebaseDatabaseRef
                .child("Users")
                .child(userId!!)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val imageUrl = snapshot.child("thumb_image").value.toString()
                        val displayName = snapshot.child("display_name").value.toString()

                        messageTextView.text = message
                        messengerTextView.text = "$displayName wrote..."

                        Picasso.get()
                            .load(imageUrl)
                            .placeholder(R.drawable.profile_img)
                            .into(profileImage)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("Error: ", error.details)
                        TODO("Not yet implemented")
                    }

                })

        }
    }
}
