package com.erikaosgue.mychatapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.erikaosgue.mychatapp.R
import com.erikaosgue.mychatapp.models.Users
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


var query: Query = FirebaseDatabase.getInstance()
    .reference
    .child("Users")
    .limitToLast(50)

var options: FirebaseRecyclerOptions<Users> = FirebaseRecyclerOptions.Builder<Users>()
    .setQuery(query, Users::class.java)
    .build()


var adapter: FirebaseRecyclerAdapter<*, *> =
    object : FirebaseRecyclerAdapter<Users?, UsersViewHolder?>(options) {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
            // Create a new instance of the ViewHolder, in this case we are using a custom
            // layout called R.layout.message for each item
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.users_row, parent, false)
            return UsersViewHolder(view)
        }

        override fun onBindViewHolder(holder: UsersViewHolder, position: Int, model: Users) {
            UsersViewHolder.bindView(position)
        }
    }

private class UsersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var userNameTxt: String? = null
    var userStatusTxt: String? = null
    var userProfilePicLink: String? = null


    fun bindView(user: Users, context: Context) {
        val userName = itemView.findViewById<TextView>(R.id.userName)
        val userStatus = itemView.findViewById<TextView>(R.id.userStatus)
        val userProfilePic = itemView.findViewById<CircleImageView>(R.id.usersProfile)

        //set the strings so we can pass in the intent
        userNameTxt = user.display_name
        userStatusTxt = user.user_status
        userProfilePicLink = user.thumb_image

        userName.text = user.display_name
        userStatus.text = user.user_status

        Picasso.get()
            .load(userProfilePicLink)
            .placeholder(R.drawable.profile_img)
            .into(userProfilePic)

    }

}

