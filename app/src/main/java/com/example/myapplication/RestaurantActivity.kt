package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import android.app.Dialog
import android.graphics.Color

import android.graphics.drawable.ColorDrawable




class RestaurantActivity: AppCompatActivity() {

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.restaurant_layout)

        // Set up the recycle view adapter
        val restID = intent.extras?.getString("restID").toString()

        val query: Query = FirebaseFirestore.getInstance()
            .collection("review")
            .whereEqualTo("restaurant", restID)
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
        val recyclerview = findViewById<RecyclerView>(R.id.review_recycler)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        LinearLayoutManager(this)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        // Initialize UI for restaurant
        val headerImgView: FrameLayout = this.findViewById(R.id.headerImg)
        val headerImg = intent.extras?.getString("headerImg").toString()
        val imgId = resources.getIdentifier(headerImg , "drawable", packageName)

        if(imgId == 0){
            headerImgView.background = getDrawable(resources.getIdentifier("default_back_img" , "drawable", packageName))
        }else{
            headerImgView.background = getDrawable(imgId)
        }

        val title: TextView = this.findViewById(R.id.displayName)
        title.text = intent.extras?.getString("restName").toString()

        val addButton = findViewById<ImageButton>(R.id.addButton)
            .setOnClickListener {
                val createReview = Intent(applicationContext, ReviewDialogue::class.java)
                createReview.putExtra("rest", restID)
                startActivity(createReview)
            }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        //adapter.stopListening();
    }
}