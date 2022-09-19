package com.example.fructiapp

import com.google.firebase.Timestamp

data class Card(var fruta: String? = "",var estado: String?= "", var fecha: Timestamp? = null){
 constructor() : this("", "", null)
}