package com.example.fructiapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class UiActivity:AppCompatActivity() {
    lateinit var adapter: FragmentStateAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ui)
        val viewPager = findViewById<ViewPager2>(R.id.Pager)
        adapter = Adaptador(this)
        viewPager.adapter = adapter
        viewPager.currentItem = 1
    }
}