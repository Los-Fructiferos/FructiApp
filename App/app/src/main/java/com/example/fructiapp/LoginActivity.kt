package com.example.fructiapp

import android.content.Intent
import android.os.Bundle
import com.example.fructiapp.MainActivity
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    // [START auth_fui_create_launcher]
    // See: https://developer.android.com/training/basics/intents/result
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }
    // [END auth_fui_create_launcher]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            createSignInIntent()
        } else {
            //moverse al main activity
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun showSnackbar(message: String) {
        // [START_EXCLUDE]
        val snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
        snackbar.show()
        // [END_EXCLUDE]
    }

    private fun createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build())

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            //por motivos de desarollo, borrar despues ...
            //evita que al cerrar y volver a abrir se mantenga sesion iniciada:
            .setIsSmartLockEnabled(false, true)
            .setAvailableProviders(providers)
            .setLogo(R.mipmap.ic_launcher_foreground) // Set logo drawable
            .setTheme(R.style.Theme_Fructiapp) // Set theme
            .setTosAndPrivacyPolicyUrls(
            "https://www.freeprivacypolicy.com/live/e7d3b692-0fab-4d34-b99e-91e64ef9a81c",
            "https://www.freeprivacypolicy.com/live/87c996f1-7419-4edb-bc48-b029da27596b")
            .build()
        signInLauncher.launch(signInIntent)
        // [END auth_fui_create_intent]
    }

    // [START auth_fui_result]
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            // moverse al mainactivity
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }
        } else {
            /*if (response != null) {
                showSnackbar(response.error!!.message!!)
            }*/
            if (response == null) {
                // User pressed back button
                //show a snackbar with a message
                showSnackbar("Inicio de sesión cancelado")
                return;
            }
        }
    }
    // [END auth_fui_result]

    private fun signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                // ...
            }
        // [END auth_fui_signout]
    }

    private fun delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
            .delete(this)
            .addOnCompleteListener {
                // ...
            }
        // [END auth_fui_delete]
    }

}