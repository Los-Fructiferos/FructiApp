package com.example.fructiapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.text.SimpleDateFormat

class CardAdapter(options: FirestoreRecyclerOptions<Card>): FirestoreRecyclerAdapter<Card, CardAdapter.CardHolder>(options) {

    class CardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewFruta: TextView
        var textViewEstado: TextView
        var textViewFecha: TextView

        init {
            textViewFruta = itemView.findViewById(R.id.fruta)
            textViewEstado = itemView.findViewById(R.id.estado)
            textViewFecha = itemView.findViewById(R.id.fecha)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        var vista: View = LayoutInflater.from(parent.context).inflate(R.layout.historial_item, parent, false)
        return CardHolder(vista)
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int, model: Card) {
        holder.textViewFruta.text = model.fruta
        holder.textViewEstado.text = model.estado
        holder.textViewFecha.text = SimpleDateFormat("MM/dd/yyyy").format(model.fecha?.toDate())
    }

}
