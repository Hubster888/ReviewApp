package com.example.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.FirebaseFirestore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter

class MainActivity : AppCompatActivity() {

    private val query: Query = FirebaseFirestore.getInstance()
        .collection("restaurant")

    val options: FirestoreRecyclerOptions<Restaurant> = FirestoreRecyclerOptions.Builder<Restaurant>()
        .setQuery(query, Restaurant::class.java)
        .build()

    private var adapter: FirestoreRecyclerAdapter<*, *> =
        object : FirestoreRecyclerAdapter<Restaurant, RestaurantListAdapter.ViewHolder?>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantListAdapter.ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.restaurant_card_view_design, parent, false)
                return RestaurantListAdapter.ViewHolder(view)
            }

            @SuppressLint("ResourceAsColor")
            override fun onBindViewHolder(
                holder: RestaurantListAdapter.ViewHolder , position: Int, model: Restaurant
            ) {
                // sets the image to the imageview from our itemHolder class
                //holder.logo.setImageResource(model.logo)

                // sets the text to the textview from our itemHolder class
                holder.name.text = model.name
                holder.distance.text = "N/A"
                holder.reviewNum.text = model.numReview.toString()
                //holder.card.setCardBackgroundColor(R.color.colorPrimary) // TODO: Change this if restaurant is recommended
                holder.logo.setImageResource(
                    resources.getIdentifier(model.logo , "drawable",
                    packageName
                ))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.restaurant_recyclerview)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        adapter.startListening()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening();
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening();
    }
}


