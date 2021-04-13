package com.erikaosgue.mychatapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erikaosgue.mychatapp.R
import com.erikaosgue.mychatapp.adapters.UsersAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


/**
 * A simple [Fragment] subclass.
 * Use the [UsersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UsersFragment : Fragment() {

    var mUserDatabase: DatabaseReference ?= null

    var adapter: UsersAdapter?= null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mUserDatabase = FirebaseDatabase.getInstance().reference.child("Users")

        Toast.makeText(context,"databaseQuery $mUserDatabase", Toast.LENGTH_LONG).show()
        Log.d("User fragment DBQuery: ", mUserDatabase.toString())

        val userRecyclerView = view.findViewById<RecyclerView>(R.id.usersRecyclerViewId)
        userRecyclerView.setHasFixedSize(true)

        userRecyclerView.layoutManager = linearLayoutManager
//        userRecyclerView.adapter = firebaseRecyclerAdapter
        adapter = UsersAdapter.getAdapter(context)
        userRecyclerView.adapter = adapter

    }

    override fun onStart() {

        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

}