package com.erikaosgue.mychatapp.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.erikaosgue.mychatapp.R
import com.erikaosgue.mychatapp.activities.ChatActivity
import com.erikaosgue.mychatapp.activities.ProfileActivity
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

var options: FirebaseRecyclerOptions<Users> = FirebaseRecyclerOptions.Builder<Users>()
    .setQuery(query, Users::class.java)
    .build()


class UsersAdapter(var context: Context?): FirebaseRecyclerAdapter<Users, UsersAdapter.UsersViewHolder>(options) {

        companion object{
            fun getAdapter(context: Context?) = UsersAdapter(context)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
            // Create a new instance of the ViewHolder, in this case we are using a custom
            // layout called R.layout.message for each item
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.users_row, parent, false)
            return UsersViewHolder(view)
        }

        override fun onBindViewHolder(holder: UsersViewHolder, position: Int, users: Users) {
//            Toast.makeText(context, "databaseQuery", Toast.LENGTH_LONG).show()
            val userId = getRef(position).key
            Log.d("DatabaseQuery: ", "Database")
            holder.bindView(users, userId!!)


        }

        inner class UsersViewHolder(var itemView: View): RecyclerView.ViewHolder(itemView) {

            var userNameTxt: String? = null
            var userStatusTxt: String? = null
            var userProfilePicLink: String? = null



            fun bindView(user: Users, userId: String) {
                val userName = itemView.findViewById<TextView>(R.id.userName)
                val userStatus = itemView.findViewById<TextView>(R.id.userStatus)
                val userProfilePic = itemView.findViewById<CircleImageView>(R.id.usersProfile)

                //set the strings so we can pass in the intent
                userNameTxt = user.display_name
                userStatusTxt = user.status
                userProfilePicLink = user.thumb_image

                userName.text = user.display_name
                userStatus.text = user.status

                Log.d("UsersAdapter: ", "UsersAdapter")
                Toast.makeText(context,  "UsersAdapter UsersAdapter", Toast.LENGTH_LONG).show()
                Picasso.get()
                    .load(userProfilePicLink)
                    .placeholder(R.drawable.profile_img)
                    .into(userProfilePic)


                showDialogSelectOptions(itemView, userId, userNameTxt, userStatusTxt, userProfilePicLink)


            }
    }
    fun showDialogSelectOptions(itemView: View, userId: String,
        userNameTxt: String?, userStatusTxt:String?, userProfilePicLink: String?) {


        itemView.setOnClickListener {
            Log.d("Users: ", userId)
            val options = arrayOf("Open Profile", "Send Message")
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Select Options")
            builder.setItems(options, DialogInterface.OnClickListener { dialogInterface, i ->

                userNameTxt
                userStatusTxt
                userProfilePicLink

                if (i == 0) {
                    //open user profile
                    val profileIntent = Intent(context, ProfileActivity::class.java)
                    profileIntent.putExtra("userId", userId)
                    context?.startActivity(profileIntent)

                } else {
                    //Send Message
                    val chatIntent = Intent(context, ChatActivity::class.java).apply {
                        putExtra("userId", userId)
                        putExtra("name", userNameTxt)
                        putExtra("status", userStatusTxt)
                        putExtra("profile", userProfilePicLink)
                    }
                    context?.startActivity(chatIntent)
                }
            })
            // SHow the Dialog
            builder.show()

        }
    }
}




