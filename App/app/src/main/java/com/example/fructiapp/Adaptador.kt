package com.example.fructiapp

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class Adaptador(actividad: AppCompatActivity) : FragmentStateAdapter(actividad) {

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return ConfigFragment.newInstance()
            1 -> return CamaraFragment.newInstance()
            2 -> return HistorialFragment.newInstance()
        }
        return null!!
    }

    override fun getItemCount(): Int {
        return 3
    }
}