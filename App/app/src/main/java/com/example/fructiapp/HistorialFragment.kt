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
import com.google.firebase.firestore.*

class HistorialFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartas: ArrayList<Card>
    private lateinit var frutidb: FirebaseFirestore
    private lateinit var adaptador : CardAdapter
    private lateinit var binding: FragmentHistorialBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistorialBinding.inflate(layoutInflater)
        setUpRecyclerView()
        return inflater.inflate(R.layout.fragment_historial, container, false)
    }

    private fun setUpRecyclerView(){

        recyclerView = binding.root.findViewById(R.id.recyler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity as AppCompatActivity)
        recyclerView.setHasFixedSize(true)
        cartas = arrayListOf()
        adaptador = CardAdapter(cartas)
        recyclerView.adapter = adaptador
        EventChangeListener()


    }

    private fun EventChangeListener() {
        frutidb = FirebaseFirestore.getInstance()
        frutidb.collection("historial").addSnapshotListener(object : EventListener<QuerySnapshot>{
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Firestore Error: ", error.message.toString())
                    return
                }
                println("Prueba de si entro al metodo firebase")
                for (dc : DocumentChange in value?.documentChanges!!){

                    if (dc.type == DocumentChange.Type.ADDED){
                        cartas.add(dc.document.toObject(Card::class.java))
                    }
                }

                adaptador.notifyDataSetChanged()
            }
        })
    }


    companion object {

        @JvmStatic
        fun newInstance(): HistorialFragment {
            return HistorialFragment()
        }
    }
}