package com.example.fructiapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class CardAdapter(private val cartas : ArrayList<Card>): RecyclerView.Adapter<CardAdapter.CardHolder>() {

    class CardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewFruta: TextView = itemView.findViewById(R.id.fruta)
        var textViewEstado: TextView = itemView.findViewById(R.id.estado)
        var textViewFecha: TextView = itemView.findViewById(R.id.fecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.historial_item, parent, false)
        return CardHolder(vista)
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        val model: Card = cartas[position]
        holder.textViewFruta.text = model.fruta
        holder.textViewEstado.text = model.estado
        holder.textViewFecha.text = model.fecha
    }

    override fun getItemCount(): Int {
        return cartas.size
    }

}
