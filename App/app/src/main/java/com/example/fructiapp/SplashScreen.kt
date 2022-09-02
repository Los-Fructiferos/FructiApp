package com.example.fructiapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class SplashScreen: AppCompatActivity() {

    lateinit var fade: Animation
    lateinit var logo: ImageView
    lateinit var titulo: TextView
    val duracion = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.splash_screen)

        fade = AnimationUtils.loadAnimation(this, R.anim.fade_splashscreen)
        logo = findViewById(R.id.fructilogo)
        logo.animation = fade
        titulo = findViewById(R.id.titulo)
        titulo.animation = fade

       Handler(Looper.getMainLooper()).postDelayed(Runnable
        {
                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                }
                finish()

        }, duracion.toLong())
    }
}