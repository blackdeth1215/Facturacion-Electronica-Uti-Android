package ec.uti.edu.utifact.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import ec.uti.edu.utifact.R
import ec.uti.edu.utifact.adapter.ClienteAdapter
import ec.uti.edu.utifact.database

class FragmentClient: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var clienteAdapter: ClienteAdapter
    private lateinit var dbHelper: database
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_clientes, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inicializar el RecyclerView
        recyclerView = view.findViewById(R.id.rcvClient)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Inicializar la base de datos
        dbHelper = database(requireContext())

        // Obtener datos de la base de datos
        val clientes = dbHelper.getClientes()
        // Configurar el adaptador
        clienteAdapter = ClienteAdapter(
            clientes,
            onEditClick = { cliente ->
                // Lógica para editar cliente
                Toast.makeText(requireContext(), "Editar: ${cliente.nombre}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { cliente ->
                // Lógica para eliminar cliente
                Toast.makeText(requireContext(), "Eliminar: ${cliente.nombre}", Toast.LENGTH_SHORT).show()
            }
        )
        // Asignar el adaptador al RecyclerView
        recyclerView.adapter = clienteAdapter

        val button = view.findViewById<MaterialButton>(R.id.btnBusClie)
        button.setOnClickListener {
            onBuscarCliente(view)
        }
        val BtnAdd = view.findViewById<MaterialButton>(R.id.btnAddClie)
        BtnAdd.setOnClickListener {
            onBuscarCliente(view)
        }
    }

    private fun onBuscarCliente(view: View) {
        Toast.makeText(requireContext(), "Buscar cliente clickeado", Toast.LENGTH_SHORT).show()
    }
    private fun onAgregarCliente(view: View) {
        Toast.makeText(requireContext(), "Agregar cliente intent", Toast.LENGTH_SHORT).show()
    }
}