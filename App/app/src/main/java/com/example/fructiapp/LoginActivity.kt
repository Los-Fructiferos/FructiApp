package com.example.fructiapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fructiapp.databinding.ActivityLoginBinding
import com.facebook.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider

class LoginActivity : AppCompatActivity() {

    var callbackManager = CallbackManager.Factory.create()
    var auth = FirebaseAuth.getInstance()
    var TAG=""

    companion object{
        private const val RC_SIGN_IN = 120
    }

     private lateinit var binding: ActivityLoginBinding
     private lateinit var firebaseAuth: FirebaseAuth
     private lateinit var googleSignInClient: GoogleSignInClient


     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         binding = ActivityLoginBinding.inflate(layoutInflater)
         val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
             .requestIdToken(getString(R.string.default_web_client_id))
             .requestEmail()
             .build()

         googleSignInClient = GoogleSignIn.getClient(this, gso)

         setContentView(binding.root)

         var loginbutton = findViewById<LoginButton>(R.id.btnFacebook)

         loginbutton.setOnClickListener{
             if(userLoggedIn()){
                    auth.signOut();
             }else{
                 LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile", "email"))
             }
         }


         LoginManager.getInstance().registerCallback(callbackManager, object :
             FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                 Log.d(TAG, "facebook:onSuccess:$loginResult")

                 handleFacebookAccessToken(loginResult.accessToken)

             }

             override fun onCancel() {
                 Log.d(TAG, "facebook:onCancel")
                 // ...
             }

             override fun onError(error: FacebookException) {
                 Log.d(TAG, "facebook:onError", error)
                 // ...
             }
         })
         // ...



         binding.singuptext.setOnClickListener{
             val intent = Intent(this, RegisterActivity::class.java)
             startActivity(intent);
         }

         binding.btnGoogle.setOnClickListener{
             signIn()
         }

         binding.btnlogin.setOnClickListener {
             val email = binding.inputEmail.text.toString()
             val pass = binding.inputPasswordL.text.toString()

             if (email.isNotEmpty() && pass.isNotEmpty()) {
                     auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                         if (it.isSuccessful) {
                             //val intent = Intent(this, LoginActivity::class.java)
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

    private fun userLoggedIn(): Boolean {
        if(auth.currentUser != null && !AccessToken.getCurrentAccessToken()!!.isExpired ){
            return true
        }

        return false
    }

    private fun signIn(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("LoginActivity", "firebaseAuthWithGoogle" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException){
                Log.w("LoginActivity", "Google sign in failed", e)
                }
            } else {
                Log.w("LoginActivity", exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful){
                    Log.d("LoginActivity", "signInWithCredential:success")
                    Toast.makeText(this, "Logged in!!", Toast.LENGTH_SHORT).show()
                }else{
                    Log.w("LoginActivity", "signInWithCredential:failuure", task.exception)
                }
            }
    }

    fun onActivityResultt(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    Toast.makeText(this, "Logged in!!", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
    }


}