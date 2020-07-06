package com.app.firebasetutorial.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.app.firebasetutorial.R
import com.app.firebasetutorial.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {


    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sign_up_text.setOnClickListener(::navigateToSignUp)

        database = Firebase.database.reference
        sign_in_btn.setOnClickListener(::processData)
    }

    private fun navigateToSignUp(view:View){
        findNavController().navigate(R.id.authenticationFragment)
    }


    private fun processData(view: View){
        if(sign_in_email.text.toString().isEmpty()){
            sign_in_email.error = "Please enter email"
            sign_in_email.requestFocus()
            return
        }

        if(sign_in_password.toString().isEmpty()){
            sign_in_password.error = "Please enter password"
            sign_in_password.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(sign_in_email.text.toString(), sign_in_password.text.toString())
            .addOnCompleteListener {task ->
                if(task.isSuccessful){
                    println(task.result!!.user)
                    Toast.makeText(context, "Sign In Success!", Toast.LENGTH_LONG).show()
                    getUser()
                }else{
                    Toast.makeText(context, task.exception!!.message, Toast.LENGTH_LONG).show()
                }

            }
    }

    private fun getUser() {
        if (auth.currentUser !== null){
            val messageListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val message = dataSnapshot.getValue(User::class.java)
                    println(message)

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Failed to read value
            }
        }


            getDatabaseRef().addValueEventListener(messageListener)
        }
    }

    private fun getDatabaseRef(): DatabaseReference{
        return FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid)
    }

}
