package com.erikaosgue.mychatapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.erikaosgue.mychatapp.R
import com.erikaosgue.mychatapp.models.Users
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


var query: Query = FirebaseDatabase.getInstance()
    .reference
    .child("Users")

var options: FirebaseRecyclerOptions<Users> = FirebaseRecyclerOptions.Builder<Users>()
    .setQuery(query, Users::class.java)
    .build()

var firebaseRecyclerAdapter: FirebaseRecyclerAdapter<*, *> =
    object : FirebaseRecyclerAdapter<Users, UsersViewHolder>(options) {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
            // Create a new instance of the ViewHolder, in this case we are using a custom
            // layout called R.layout.message for each item
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.users_row, parent, false)
            return UsersViewHolder(view)
        }

        override fun onBindViewHolder(holder: UsersViewHolder, position: Int, users: Users) {
//            Toast.makeText(context, "databaseQuery", Toast.LENGTH_LONG).show()
            Log.d("DatabaseQuery: ", "Database")
            holder.bindView(users)
        }
    }


    class UsersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var userNameTxt: String? = null
        var userStatusTxt: String? = null
        var userProfilePicLink: String? = null



        fun bindView(user: Users) {
            var userName = itemView.findViewById<TextView>(R.id.userName)
            var userStatus = itemView.findViewById<TextView>(R.id.userStatus)
            var userProfilePic = itemView.findViewById<CircleImageView>(R.id.usersProfile)

            //set the strings so we can pass in the intent
            userNameTxt = user.display_name
            userStatusTxt = user.status
            userProfilePicLink = user.thumb_image

            userName.text = user.display_name
            userStatus.text = user.status

            Log.d("UsersAdapter: ", "UsersAdapter")
//            Toast.makeText(context,  /"UsersAdapter UsersAdapter", Toast.LENGTH_LONG).show()
            Picasso.get()
                .load(userProfilePicLink)
                .placeholder(R.drawable.profile_img)
                .into(userProfilePic)

        }

}




