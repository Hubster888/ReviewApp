package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import android.content.Intent
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private var recyclerview: RecyclerView? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
        }
    }

    var query: Query = FirebaseFirestore.getInstance()
        .collection("restaurant")
        .orderBy("review", Query.Direction.DESCENDING)

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
                model.uid = snapshots.getSnapshot(position).id
                val goToRestaurant = Intent(applicationContext, RestaurantActivity::class.java)
                goToRestaurant.putExtra("restID", model.uid)
                goToRestaurant.putExtra("headerImg", model.backPic)
                goToRestaurant.putExtra("restName", model.name)

                val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
                val defaultValue = resources.getString(R.string.def_loc_key)
                val lat = sharedPref.getFloat(getString(R.string.latitude_key), defaultValue.toFloat())
                val long = sharedPref.getFloat(getString(R.string.longitude_key), defaultValue.toFloat())
                model.calculateDistance(lat, long)

                // sets the text to the textview from our itemHolder class
                holder.distance.text = String.format("%.1f mi", model.distance)
                holder.name.text = model.name
                holder.reviewNum.text = String.format("(%1d)", model.numReview)
                holder.review.rating = model.review
                if (position == 0) {
                    holder.card.setCardBackgroundColor(ContextCompat.getColor(holder.card.context, R.color.recommendedCard))
                }
                holder.logo.setImageResource(
                    resources.getIdentifier(model.logo , "drawable",
                        packageName
                    ))
                holder.card.setOnClickListener {
                    startActivity(goToRestaurant)
                }
            }
        }

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

    @SuppressLint("ResourceType", "NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastKnownLocation()
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))

        adapter.startListening()

        recyclerview = findViewById<RecyclerView>(R.id.restaurant_recyclerview);
        recyclerview?.layoutManager = WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        LinearLayoutManager(this)
        recyclerview?.adapter = adapter

        val sortButton = findViewById<ImageButton>(R.id.sortButton)
            .setOnClickListener {
                adapter.stopListening()
                query = FirebaseFirestore.getInstance()
                    .collection("restaurant")
                    .orderBy("numReviews", Query.Direction.DESCENDING)

                adapter.startListening()

                recyclerview?.recycledViewPool?.clear();
                adapter.notifyDataSetChanged();
            }

        val profileButton = findViewById<ImageButton>(R.id.profileButton)
            .setOnClickListener {
                if(AuthUI.getInstance().auth.currentUser == null){
                    val signInIntent = AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build()
                    signInLauncher.launch(signInIntent)
                }else{
                    val goToProfile = Intent(applicationContext, ProfileActivity::class.java)
                    startActivity(goToProfile)
                }
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        recyclerview?.recycledViewPool?.clear();
        adapter.notifyDataSetChanged();
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        //adapter.stopListening();
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location->
                println(location)
                if (location != null) {
                    // use your location object
                    // get latitude , longitude and other info from this
                    val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return@addOnSuccessListener
                    with (sharedPref.edit()) {
                        putFloat(getString(R.string.latitude_key), location.latitude.toFloat())
                        putFloat(getString(R.string.longitude_key), location.longitude.toFloat())
                        apply()
                    }
                }
            }
    }
}


