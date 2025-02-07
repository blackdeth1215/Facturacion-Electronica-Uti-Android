package ec.uti.edu.utifact.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import ec.uti.edu.utifact.R
import ec.uti.edu.utifact.adapter.ClienteAdapter
import ec.uti.edu.utifact.adapter.UserAdapter
import ec.uti.edu.utifact.database.AppDatabase
import ec.uti.edu.utifact.database.databaseDao.ClienteDao
import ec.uti.edu.utifact.database.databaseDao.UserDao
import ec.uti.edu.utifact.databasebd
import ec.uti.edu.utifact.ui.AdminActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentClient: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var clienteAdapter: ClienteAdapter
    private lateinit var clienteDao: ClienteDao
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_clientes, container, false)
    }
    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.setFragmentTitle("Clientes")
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inicializar el RecyclerView
        recyclerView = view.findViewById(R.id.rcvClient)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Inicializar la base de datos
        val database = AppDatabase.getDatabase(requireContext())

        // Obtener datos de la base de datos
        clienteDao = database.clienteDao()

        //actualizacion de datos en el recicler view
        loadCiente()

        val button = view.findViewById<MaterialButton>(R.id.btnBusClie)
        button.setOnClickListener {
            onBuscarCliente(view)
        }
    }

    private fun onBuscarCliente(view: View) {
        var cedula = view.findViewById<EditText>(R.id.editTextText).text.toString()
        lifecycleScope.launch {
            val clientes = withContext(Dispatchers.IO) { clienteDao.findByCedula(cedula) }
                ?.let { listOf(it) } ?: emptyList()

            val clientesFinal = if (clientes.isEmpty()) {
                // Si no se encontraron resultados, mostrar el mensaje
                Toast.makeText(requireContext(), "No se encontraron resultados", Toast.LENGTH_SHORT).show()
                // Obtener todos los clientes
                withContext(Dispatchers.IO) { clienteDao.getAll() }
            } else {
                clientes
            }
            if (clientesFinal.isNotEmpty()) {
                // Configurar el adaptador con los clientes encontrados
                clienteAdapter = ClienteAdapter(
                    clientesFinal,
                    onEditClick = { cliente ->
                        Toast.makeText(requireContext(), "Editar: ${cliente.nombresClient}", Toast.LENGTH_SHORT).show()
                    },
                    onDeleteClick = { cliente ->
                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) { clienteDao.delete(cliente) }
                            loadCiente()
                            Toast.makeText(requireContext(), "Eliminado: ${cliente.nombresClient}", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
                recyclerView.adapter = clienteAdapter
            } else {
                Toast.makeText(requireContext(), "No hay clientes disponibles", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadCiente() {
        // Obtener datos desde Room (operación en segundo plano)
        lifecycleScope.launch {
            val clientes = withContext(Dispatchers.IO) { clienteDao.getAll() }
            clienteAdapter = ClienteAdapter(
                clientes,
                onEditClick = { clientes ->
                    // Lógica para editar usuario
                    val newFragment = FragmentEdAcCliente()

                    // Enviar dato al otro fragmento
                    val bundle = Bundle()
                    bundle.putString("username", clientes.cedulaClient)
                    Log.d("FragmentEdAcUser", "envío de dato bundle ${bundle.toString()}.")

                    // Asociar el bundle al nuevo fragmento
                    newFragment.arguments = bundle

                    // Obtener el FragmentManager y comenzar una nueva transacción
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()

                    // Reemplazar el contenido del contenedor de fragmentos con el nuevo fragmento
                    transaction.replace(R.id.fragment_container, newFragment)

                    // Agregar la transacción a la pila para que el usuario pueda regresar
                    transaction.addToBackStack(null)

                    // Confirmar la transacción
                    transaction.commit()
                },
                onDeleteClick = { clientes ->
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) { clienteDao.delete(clientes) }
                        loadCiente() // Recargar usuarios después de eliminar
                        Toast.makeText(requireContext(), "Eliminado: ${clientes.nombresClient}", Toast.LENGTH_SHORT).show()
                    }
                }
            )
            recyclerView.adapter = clienteAdapter
        }
    }
}