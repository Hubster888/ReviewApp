package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.FirebaseFirestore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.models.PermissionRequest

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            } else -> {
            // No location access granted.
        }
        }
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

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
                holder.reviewNum.text = "(" + model.numReview.toString() + ")"
                holder.review.rating = model.review
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this!!)

        getLastKnownLocation() // returns location

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.restaurant_recyclerview)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        adapter.startListening()

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
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

    fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location->
                if (location != null) {
                    // use your location object
                    // get latitude , longitude and other info from this
                    println(location.latitude)
                    println(location.longitude)
                }
            }
    }
}


