package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class ReviewDialogue : AppCompatActivity(){

    private var contentBox : TextView? = null

    private var ratingSelector : RatingBar? = null

    @SuppressLint("SimpleDateFormat")
    fun makeReview(): Review? {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val userName = FirebaseAuth.getInstance().currentUser?.displayName
        val currentDate = Date()
        val content = contentBox?.text.toString()
        val rating = ratingSelector?.rating
        val restId = intent.extras?.getString("rest").toString()
        val restPic = "none" //TODO: Implement this

        return if(content.isBlank() || rating!!.isNaN() || rating == (0.0).toFloat()){
            null
        }else{
            Review(
                userId, userName, currentDate,
                content, rating, restId, restPic
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.review_dialogue)

        val saveButton = findViewById<Button>(R.id.saveButton)
            .setOnClickListener {
                val data : Review? = makeReview()
                if(data == null){
                    AlertDialog.Builder(this)
                        .setTitle("Missing Values!")
                        .setMessage("Please add text and a star rating.") // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK",
                            DialogInterface.OnClickListener { dialog, which ->

                            }) // A null listener allows the button to dismiss the dialog and take no further action.
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show()
                }else{
                    FirebaseFirestore.getInstance().collection("review")
                        .add(data)
                        .addOnSuccessListener { documentReference ->
                            finish()
                        }
                        .addOnFailureListener { e ->
                            // Alert failed
                        }
                }
            }

        contentBox = findViewById(R.id.contentBox)

        ratingSelector = findViewById(R.id.ratingSelect)
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