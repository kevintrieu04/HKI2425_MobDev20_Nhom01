package com.example.cinemaapp.network

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class DbConect {
    val db = Firebase.firestore

    // Create a new user with a first and last name
    private val person = hashMapOf(
        "name" to "Ada Lovelace",
        "year" to 1815,
        "bio" to "actor"
    )

    fun addData() {
        // Add a new document with a generated ID
        db.collection("person")
            .add(person)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun readData() {
        db.collection("person")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }


}