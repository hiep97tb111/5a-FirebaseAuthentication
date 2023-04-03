package com.example.firebaseauthenticationdemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginGoogleAct: AppCompatActivity() {
    private val rcSignIn: Int = 101
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_login_google)

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)

        // Login Google when First Open or Logout
        findViewById<SignInButton>(R.id.sign_in_button).setOnClickListener {
            signIn()
        }

        // Logout Google
        findViewById<TextView>(R.id.tvLogoutGoogle).setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener {
            Log.e("Logger Logout", it.isComplete.toString())
        }
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, rcSignIn)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...)
        // The Task returned from this call is always completed, no need to attach
        // a listener.
        if (requestCode === rcSignIn) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun updateUI(acct: GoogleSignInAccount?) {
        if (acct != null) {
            val personName: String? = acct.displayName
            val personGivenName: String? = acct.givenName
            val personFamilyName: String? = acct.familyName
            val personEmail: String? = acct.getEmail()
            val personId: String? = acct.id
            val personPhoto: Uri? = acct.photoUrl
            Log.e("Logger", personEmail.toString()+"/"+personName+"/"+personPhoto)
        }
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            // Signed in successfully, show authenticated UI.
            val account = completedTask.getResult(ApiException::class.java)
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            Log.e("Logger", "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }
}