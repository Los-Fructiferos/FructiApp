package com.example.fructiapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fructiapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

     private lateinit var binding: ActivityLoginBinding
     private lateinit var firebaseAuth: FirebaseAuth


     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)

         binding = ActivityLoginBinding.inflate(layoutInflater)
         setContentView(binding.root)

         binding.singuptext.setOnClickListener{
             val intent = Intent(this, RegisterActivity::class.java)
             startActivity(intent);
         }

         binding.btnlogin.setOnClickListener {
             val email = binding.inputEmail.text.toString()
             val pass = binding.inputPasswordL.text.toString()

             if (email.isNotEmpty() && pass.isNotEmpty()) {
                     firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                         if (it.isSuccessful) {
                             val intent = Intent(this, LoginActivity::class.java)
                             Toast.makeText(this, "Logged in!!", Toast.LENGTH_SHORT).show()
                         } else {
                             Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                         }
                     }

             } else {
                 Toast.makeText(this, "Don't leave empty fields.", Toast.LENGTH_SHORT).show()
             }
         }
     }
}