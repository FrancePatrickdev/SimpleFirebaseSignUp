package com.app.firebasetutorial.helper

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseReference {

    private fun getUserInfo(userId: String): DatabaseReference {
        return FirebaseDatabase.getInstance().reference.child("users").child(userId)
    }

}