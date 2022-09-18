package com.example.fructiapp

 data class Card(var fruta: String? = "",var estado: String?= "", var fecha: String? = ""){
  constructor() : this("", "", "")
 }