package com.app.firebasetutorial.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.app.firebasetutorial.R
import com.app.firebasetutorial.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_authentication.*


class AuthenticationFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_authentication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = Firebase.database.reference
        register_btn.setOnClickListener(::processData)
    }

    private fun processData(view: View){
        if(username_et.text.toString().isEmpty()){
            username_et.error = "Please enter email"
            username_et.requestFocus()
            return
        }

        if(password_et.toString().isEmpty()){
            password_et.error = "Please enter password"
            password_et.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(username_et.text.toString(), password_et.text.toString())
            .addOnCompleteListener {task ->
                if(task.isSuccessful){
                    println(task.result!!.user)
                    Toast.makeText(context, "Sign Up Success!", Toast.LENGTH_LONG).show()
                    getUser()
                }else{
                    Toast.makeText(context, task.exception!!.message, Toast.LENGTH_LONG).show()
                }

            }
    }

    private fun getUser() {
        if (auth.currentUser !== null){
            registerUser(auth.currentUser!!.uid)
        }
    }


    private fun registerUser(userId: String){
        val user = User("france", "lodonia", "05454587872")
        database.child("users").child(userId).setValue(user)
    }

}
