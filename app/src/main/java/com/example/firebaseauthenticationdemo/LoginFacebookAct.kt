package com.example.firebaseauthenticationdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import org.json.JSONObject


class LoginFacebookAct: AppCompatActivity() {

    lateinit var  callbackManager: CallbackManager
    lateinit var loginButton: LoginButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_login_facebook)

        initView()

        // Handle event when click Button Login Facebook and request server
        clickAndRequestLoginButton()

        // When Logged then auto login and response data info
        getInfoWhenLastLogin()

        // Enter username, password -> ActivityResult -> responseInfo
        responseInfo()

    }

    private fun responseInfo() {
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    // App code
                    Log.e("Logger", "onSuccess")
                    Log.e("Logger", loginResult!!.accessToken.source.name)
                    getInfo()
                }

                override fun onCancel() {
                    // App code
                    Log.e("Logger", "onCancel")
                }

                override fun onError(exception: FacebookException) {
                    // App code
                    Log.e("Logger", exception.message.toString())
                }
            })
    }

    private fun getInfoWhenLastLogin() {
        getInfo()
    }

    private fun clickAndRequestLoginButton() {
        loginButton.setReadPermissions(listOf("email","public_profile"))
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(result: LoginResult?) {}

            override fun onCancel() {}

            override fun onError(error: FacebookException) {}
        })
    }

    private fun initView() {
        callbackManager = CallbackManager.Factory.create()
        loginButton = findViewById(R.id.login_button)
    }

    private fun getInfo() {
        val accessToken = AccessToken.getCurrentAccessToken()
        val request = GraphRequest.newMeRequest(accessToken, object : GraphRequest.GraphJSONObjectCallback{
            override fun onCompleted(obj: JSONObject?, response: GraphResponse?) {
                // Handle the response
                try {
                    val name: String = obj!!.getString("name")
                    val id: String = obj.getString("id")
                    val avatarUrl = "https://graph.facebook.com/" + obj.getString("id")
                        .toString() + "/picture?type=large"
                    val url = obj.getJSONObject("picture").getJSONObject("data").getString("url")

                    // Use the retrieved information as needed
                    Log.d("Logger", "Name: $name")
                    Log.d("Logger", "Id: $id")
                    Log.d("Logger", "Avatar URL: $avatarUrl")
                    Log.d("Logger", "URL: $url" )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        })

        // must contains parameter 
        val parameters = Bundle()
        parameters.putString("fields", "id, name, link, picture.type(large)")
        request.parameters = parameters
        request.executeAsync();
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}