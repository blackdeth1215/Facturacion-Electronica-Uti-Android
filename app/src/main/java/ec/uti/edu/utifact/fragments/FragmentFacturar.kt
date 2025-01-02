package ec.uti.edu.utifact.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import ec.uti.edu.utifact.R
import ec.uti.edu.utifact.adapter.ClienteAdapter
import ec.uti.edu.utifact.adapter.ProductoAdapter
import ec.uti.edu.utifact.database

class FragmentFacturar: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var clienteAdapter: ClienteAdapter
    private lateinit var dbHelper: database
    private lateinit var productoAdapter: ProductoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_facturacion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inicializar el RecyclerView
        recyclerView = view.findViewById(R.id.rcvProdF)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Inicializar la base de datos
        dbHelper = database(requireContext())

        // Obtener datos de la base de datos



    }
}