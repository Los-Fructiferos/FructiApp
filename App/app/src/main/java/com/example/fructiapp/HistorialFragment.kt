package com.example.fructiapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fructiapp.databinding.FragmentHistorialBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase


class HistorialFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adaptador : CardAdapter
    private var frutidb: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_historial, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        setUpRecyclerView(itemView)
    }

    private fun setUpRecyclerView(itemview: View){
        var query: Query = frutidb.collection("detalle_historial").whereEqualTo("uid",FirebaseAuth.getInstance().uid)
        var opciones: FirestoreRecyclerOptions<Card> = FirestoreRecyclerOptions.Builder<Card>().setQuery(query, Card::class.java).build()
        adaptador = CardAdapter(opciones)
        recyclerView = itemview.findViewById(R.id.recyler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity as AppCompatActivity)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adaptador
    }


    override fun onStart() {
        super.onStart()
        adaptador.startListening()
    }

    override fun onStop() {
        super.onStop()
        adaptador.stopListening()
    }

    companion object {

        @JvmStatic
        fun newInstance(): HistorialFragment {
            return HistorialFragment()
        }
    }
}