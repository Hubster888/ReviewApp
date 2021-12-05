package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import android.content.DialogInterface
import android.widget.FrameLayout
import android.widget.TextView


class ProfileActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_layout)

        // Set up the recycle view adapter
        val userID = FirebaseAuth.getInstance().currentUser?.uid

        val query: Query = FirebaseFirestore.getInstance()
            .collection("review")
            .whereEqualTo("user", userID)
            .orderBy("date", Query.Direction.DESCENDING)

        val options: FirestoreRecyclerOptions<Review> = FirestoreRecyclerOptions.Builder<Review>()
            .setQuery(query, Review::class.java)
            .build()

        val adapter: FirestoreRecyclerAdapter<*, *> =
            object : FirestoreRecyclerAdapter<Review, ReviewListAdapter.ViewHolder?>(options) {

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewListAdapter.ViewHolder {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.review_card, parent, false)
                    return ReviewListAdapter.ViewHolder(view)
                }

                @SuppressLint("ResourceAsColor")
                override fun onBindViewHolder(
                    holder: ReviewListAdapter.ViewHolder , position: Int, model: Review
                ) {

                    // sets the text to the textview from our itemHolder class
                    holder.userName.text = model.userName
                    holder.ratingBar.rating = model.score
                    holder.content.text = model.content
                    if(model.pic == "none"){
                        holder.image.isInvisible = true
                    }
                    holder.image.setImageResource(
                        resources.getIdentifier(model.pic , "drawable",
                            packageName
                        ))
                    holder.content.movementMethod = ScrollingMovementMethod();
                }
            }

        adapter.startListening()
        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.review_recycler);

        // this creates a vertical layout Manager
        recyclerview.layoutManager = WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        LinearLayoutManager(this)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        val signOutButton = findViewById<ImageButton>(R.id.signOut)
            .setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle("Sign Out")
                    .setMessage("Want to sign out?") // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton("Sign Out",
                        DialogInterface.OnClickListener { dialog, which ->
                            FirebaseAuth.getInstance().signOut()
                            finish()
                        }) // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton("Cancel", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
            }

        val userName: TextView = this.findViewById(R.id.displayName)
        userName.text = FirebaseAuth.getInstance().currentUser?.displayName


    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }
}